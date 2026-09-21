/*
    Mad Kaz   Copyright 2007 CodeYield Development, Inc.  inquiries@codeyielddevelopment.com

    This file is part of Mad Kaz.

    Mad Kaz is free software; you can redistribute it and/or modify it under 
    the terms of the GNU General Public License as published by the Free 
    Software Foundation; either version 3 of the License, or (at your option) 
    any later version.

    Mad Kaz is distributed in the hope that it will be useful, but WITHOUT ANY 
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
    FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
    details.

    You should have received a copy of the GNU General Public License along 
    with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package bluetooth;
import javax.bluetooth.L2CAPConnection;
import javax.microedition.io.Connector;

public abstract class cydBTL2CAPTightClient {
    protected L2CAPConnection m_connection;
    protected int m_maxSendLatency;
    protected boolean m_started = false;
    protected boolean m_connected = false;
    protected int m_minWaitTime;
    protected byte []m_sendBuffer;
    protected byte []m_recvBuffer;
    
    public boolean start(String url, int maxSendLatency, int sendBufferSize, int recvBufferSize, int minWaitTime, boolean threaded) {
        boolean ret = false;
        
        try {
            ret = start((L2CAPConnection)Connector.open(url), maxSendLatency, sendBufferSize, recvBufferSize, minWaitTime, threaded);
        } catch (Throwable t) {}
        
        return ret;
    }
    
    public boolean start(L2CAPConnection conn, int maxSendLatency, int sendBufferSize, int recvBufferSize, int minWaitTime, boolean threaded) {
        if (m_started)
            return false;
        
        m_maxSendLatency = maxSendLatency;
        m_started = true;
        
        m_sendBuffer = new byte[sendBufferSize];
        m_recvBuffer = new byte[recvBufferSize];
        
        m_connection = conn;
        
        m_connected = true;
        
        if (threaded) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (communicate());
                }
            });
            
            t.start();
        }
        
        return true;
    }
    
    public boolean communicate() {
        try {
            long oldTime = System.currentTimeMillis();
            long snapshotTime = 0L;
            
            snapshotTime = System.currentTimeMillis();
            
            int diffTime = (int)(snapshotTime - oldTime);
            
            if (diffTime < m_minWaitTime)
                return m_connected;
            
            if (sendData(m_sendBuffer, snapshotTime, diffTime)) {
                m_connection.send(m_sendBuffer);
                
                long postSendTime = System.currentTimeMillis();
                int sendLatency = (int)(postSendTime - snapshotTime);
                
                if (sendLatency > m_maxSendLatency)
                    sendLatencyWarning(sendLatency);
            }
            
            if (m_connection.ready()) {
                m_connection.receive(m_recvBuffer);
                recvData(m_recvBuffer, snapshotTime, diffTime);
            }
            
            snapshotTime = oldTime;
        } catch (Throwable t) {
            try { m_connection.close(); } catch (Throwable t1) {}
            m_connected = false;
        }
        
        return m_connected;
    }
    
    public void close() {
        try { m_connection.close(); } catch (Throwable t1) {}
    }
    
    public boolean isConnected() {
        return m_connected;
    }
    
    protected abstract boolean sendData(byte []sendBuffer, long timeSnapshot, int diffTime);
    protected abstract void recvData(byte []recvBuffer, long timeSnapshot, int diffTime);
    protected abstract void sendLatencyWarning(int latency);
}
