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

package game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;
import javax.microedition.io.StreamConnection;

public class cydP2PServerConnectionProcessor {
    public StreamConnection []m_streamConnections;
    public DataInputStream []m_streamDIS;
    public DataOutputStream []m_streamDOS;
    
    public int m_maxConnections;
    public int m_activeConnections;
    public cydLevelStorageSystem m_lss;
    
    public void reset(int maxConnections, cydLevelStorageSystem lss) {
        close();
        
        m_streamConnections = new StreamConnection[maxConnections];
        m_streamDIS = new DataInputStream[maxConnections];
        m_streamDOS = new DataOutputStream[maxConnections];
        
        m_maxConnections = maxConnections;
        m_activeConnections = 0;
        
        m_lss = lss;
    }
    
    public int incomingConnection(StreamConnection conn) {
        DataInputStream dis = null;
        DataOutputStream dos = null;
        
        try {
            dis = conn.openDataInputStream();
            dos = conn.openDataOutputStream();
            
            if (m_maxConnections == m_activeConnections)
                throw new RuntimeException();
        } catch (Throwable t) {
            try { dis.close(); } catch (Throwable t1) { }
            try { dos.close(); } catch (Throwable t1) { }
            try { conn.close(); } catch (Throwable t1) { }
            
            return -1;
        }
        
        int index = -1;
        for (int i = 0; i < m_maxConnections; i++) {
            if (m_streamConnections[i] == null && m_streamDIS[i] == null && m_streamDOS[i] == null) {
                index = i;
                break;
            }
        }
        
        if (index == -1)
            return -1;
        
        m_streamConnections[index] = conn;
        m_streamDIS[index] = dis;
        m_streamDOS[index] = dos;
        
        m_activeConnections++;
        
        return index;
    }
    
    public boolean terminateConnection(int index) {
        System.out.println("terminate connection called on " + index);
        if (index >= m_maxConnections)
            return false;
        
        try { m_streamDIS[index].close(); } catch (Throwable t1) { }
        try { m_streamDOS[index].close(); } catch (Throwable t1) { }
        try { m_streamConnections[index].close(); } catch (Throwable t1) { }
        
        m_streamDIS[index] = null;
        m_streamDOS[index] = null;
        m_streamConnections[index] = null;
        
        m_activeConnections--;
        
        return true;
    }
    
    public void process(int timeElapsed) {
        byte []empty = new byte[0];
        
        for (int i = 0; i < m_maxConnections; i++) {
            if (m_streamConnections[i] == null)
                continue;
            
            try {
                int available = m_streamDIS[i].available();
                
                if (available >= 8) {
                    int cmdCode = m_streamDIS[i].readInt();
                    int param = m_streamDIS[i].readInt();
                    
                    switch (cmdCode) {
                        case cydP2PCanvas.C_CMD_MAP_LIST_REQ: {
                            char [][] names = m_lss.getLevelShortNames();
                            
                            m_streamDOS[i].writeInt(names.length);
                            
                            for (int j = 0; j < names.length; j++)
                                m_streamDOS[i].writeUTF(new String(names[j]));
                        }
                        break;
                        case cydP2PCanvas.C_CMD_MAP_DOWNLOAD_REQ: {
                            byte []data = m_lss.getLevelDataFromIndex(param);
                            
                            if (data == null)
                                terminateConnection(i);
                            
                            m_streamDOS[i].writeInt(data.length);
                            m_streamDOS[i].write(data);
                        }
                        break;
                        default: {
                            terminateConnection(i);
                        }
                        break;
                    }
                } else {
                    m_streamDIS[i].read(empty);
                }
            } catch (Throwable t) {
                terminateConnection(i);
            }
        }
    }
    
    public void close() {
        for (int i = 0; i < m_maxConnections; i++)
            terminateConnection(i);
    }
}
