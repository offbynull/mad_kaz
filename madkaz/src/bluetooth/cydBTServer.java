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
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connection;

public abstract class cydBTServer {
    protected Connection m_server;
    protected boolean m_serverRunning = false;
    protected boolean m_showServerMessages = false;
    protected Vector m_serverMessages;
    protected Vector m_serverClients;
    
    public boolean isServerRunning() {
        return m_serverRunning;
    }
    
    public void startServer(final String uuid, final String name, final boolean showServerMsgs) {
        if (m_serverRunning) {
            if (m_showServerMessages)
                m_serverMessages.addElement("Server already started");
            
            return;
        }
        
        m_showServerMessages = showServerMsgs;
        
        m_serverMessages = new Vector();
        m_serverClients = new Vector();
        
        Thread t = new Thread(new Runnable() {
            public void run() {
                listen(uuid, name);
            }
        });
        
        t.start();
    }
    
    public void stopServer() {
        try {
            if (m_showServerMessages)
                m_serverMessages.addElement("Shutting down server");
            m_server.close();
        } catch (Throwable t) {
        }
    }
    
    protected abstract void listen(String uuid, String name);
    
    public void getPendingMessages(Vector retVector) {
        synchronized (m_serverMessages) {
            Enumeration e = m_serverMessages.elements();
            
            while (e.hasMoreElements())
                retVector.addElement(e.nextElement());
            
            m_serverMessages.removeAllElements();
        }
    }
    
    public void getPendingConnections(Vector retVector) {
        synchronized (m_serverMessages) {
            Enumeration e = m_serverClients.elements();
            
            while (e.hasMoreElements())
                retVector.addElement(e.nextElement());
            
            m_serverClients.removeAllElements();
        }
    }
}
