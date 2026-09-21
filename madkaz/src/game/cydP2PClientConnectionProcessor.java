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
import java.io.IOException;
import javax.microedition.io.StreamConnection;

public class cydP2PClientConnectionProcessor {
    public StreamConnection m_streamConnection;
    public DataInputStream m_streamDIS;
    public DataOutputStream m_streamDOS;
    
    public int m_lastSentCommand = cydP2PCanvas.C_CMD_NONE;
    
    public int m_maxConnections;
    public int m_activeConnections;
    public cydLevelStorageSystem m_lss;
    
    public char [][]m_recvdLevelList;
    public byte []m_recvdLevel;
    
    public void reset(StreamConnection streamConnection, cydLevelStorageSystem lss) throws IOException {
        m_streamConnection = streamConnection;
        m_streamDIS = streamConnection.openDataInputStream();
        m_streamDOS = streamConnection.openDataOutputStream();
        
        m_lss = lss;
    }
    
    public void sendCommand(int command, int param) throws IOException {
        m_recvdLevelList = null;
        m_recvdLevel = null;
        
        m_streamDOS.writeInt(command);
        m_streamDOS.writeInt(param);
        m_streamDOS.flush();
        
        m_lastSentCommand = command;
    }
    
    public byte []getLevel() {
        return m_recvdLevel;
    }
    
    public char [][]getLevelList() {
        return m_recvdLevelList;
    }
    
    public void recvFully() throws IOException {
        switch (m_lastSentCommand) {
            case cydP2PCanvas.C_CMD_MAP_LIST_REQ: {
                int count = m_streamDIS.readInt();
                
                m_recvdLevelList = new char[count][];
                
                for (int i = 0; i < count; i++)
                    m_recvdLevelList[i] = m_streamDIS.readUTF().toCharArray();
            }
            break;
            case cydP2PCanvas.C_CMD_MAP_DOWNLOAD_REQ: {
                int count = m_streamDIS.readInt();
                
                m_recvdLevel = new byte[count];
                
                m_streamDIS.readFully(m_recvdLevel);
            }
            break;
            default:
                throw new IOException("unknown cmd");
        }
    }
    
    public void close() {
        try { m_streamDIS.close(); } catch (Throwable t1) { }
        try { m_streamDOS.close(); } catch (Throwable t1) { }
        try { m_streamConnection.close(); } catch (Throwable t1) { }
        
        m_streamDIS = null;
        m_streamDOS = null;
        m_streamConnection = null;
    }
}
