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

package framework;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public final class cydNetworkAccess implements Runnable {
    private static byte []m_data = null;
    private static boolean m_waiting = false;
    private static Object m_lockObject = new Object();
    private static String m_url;
    
    public static boolean isWaiting() {
        synchronized(m_lockObject) {
            return m_waiting;
        }
    }
    
    public static byte []getAndClearData() {
        synchronized(m_lockObject) {
            byte []data = m_data;
            m_data = null;

            return data;
        }
    }
    
    public static boolean accessURL(String url) {
        synchronized(m_lockObject) {
            if (m_waiting)
                return false;
            
            m_data = null;
            
            m_waiting = true;
            
            m_url = url;
            
            Thread t = new Thread(new cydNetworkAccess());
            t.start();
            
            return true;
        }
    }
    
    private cydNetworkAccess() {
    }
    
    public void run() {
        String url;
        
        synchronized(m_lockObject) {
            url = m_url;
        }
        
        HttpConnection c = null;
        InputStream is = null;
        int rc;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            c = (HttpConnection)Connector.open(url);
            
            c.setRequestMethod(HttpConnection.GET);

            rc = c.getResponseCode();
            is = c.openInputStream();
            
            byte []input = new byte[1024];
            int readAmount = 0;
            
            while ((readAmount = is.read(input)) != -1)
                baos.write(input, 0, readAmount);
            
            is.close();
            c.close();
        } catch (Throwable t) {
            System.out.println("NET CALL FAILED " + t);
            try { is.close(); } catch (Throwable t1) { }
            try { c.close(); } catch (Throwable t1) { }
        }

        synchronized(m_lockObject) {
            if (baos.size() > 0)
                m_data = baos.toByteArray();
            else
                m_data = null;
                        
            m_waiting = false;
        }
    }
}
