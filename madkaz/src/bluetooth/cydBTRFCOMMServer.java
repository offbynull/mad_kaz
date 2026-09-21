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
import java.io.IOException;
import java.io.InputStream;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;


public class cydBTRFCOMMServer extends cydBTServer {
    public void listen(String uuid, String name) {
        m_serverRunning = true;

        try {
            LocalDevice local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);
        } catch (Throwable t) {
            if (m_showServerMessages)
                m_serverMessages.addElement("Server cannot start: " + t.getMessage());
            
            m_serverRunning = false;
            return;
        }
        
        StreamConnectionNotifier rfcommServer = null;

        try {
            rfcommServer = (StreamConnectionNotifier)Connector.open("btspp://localhost:" + uuid + ";name=" + name);
            m_server = rfcommServer;
        } catch (Throwable t) {
            if (m_showServerMessages)
                m_serverMessages.addElement("Server cannot start: " + t.getMessage());
            
            m_serverRunning = false;
            return;
        }

        while (m_serverRunning) {
            StreamConnection conn = null;

            try {
                conn = rfcommServer.acceptAndOpen();
                
                m_serverClients.addElement(conn);
                
                if (m_showServerMessages)
                    m_serverMessages.addElement("New incoming connection accepted");
            } catch (Throwable t) {
                if (m_showServerMessages)
                    m_serverMessages.addElement("Error occured while waiting: " + t.getMessage());
                
                m_serverRunning = false;
                return;
            }
        }
    }
}
