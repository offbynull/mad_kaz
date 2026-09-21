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

import bluetooth.cydBTDiscovery;
import bluetooth.cydBTRFCOMMServer;
import framework.cydGameCanvas;
import framework.cydImageHeaderMenuWindowOutput;
import framework.cydMessageWindowOutput;
import framework.cydSimpleAskWindowOutput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Graphics;
import vm.cydByteArrayInputStream;

public class cydP2PCanvas extends cydGameCanvas {
    public cydGameManager m_gm;
    
    public static final int INDEX_MAIN_MENU_TITLE               = 100;
    public static final int INDEX_MAIN_MENU_HOST_RACE_OPTION    = 101;
    public static final int INDEX_MAIN_MENU_JOIN_RACE_OPTION    = 102;
    public static final int INDEX_MAIN_MENU_HELP_OPTION         = 103;
    public static final int INDEX_MAIN_MENU_BACK_OPTION         = 104;
    
    public static final int INDEX_SEARCHING_TITLE               = 200;
    public static final int INDEX_SEARCHING_SEARCHING_TEXT      = 201;
    public static final int INDEX_SEARCHING_NON_FOUND_TEXT      = 202;
    public static final int INDEX_SEARCHING_BACK_OPTION         = 203;
    
    public static final int INDEX_SERVER_LIST_TITLE             = 300;
    public static final int INDEX_SERVER_LIST_BACK_OPTION       = 301;
    
    public static final int INDEX_WAITING_TITLE                 = 400;
    public static final int INDEX_WAITING_BACK_OPTION           = 401;
    public static final int INDEX_WAITING_TEXT                  = 402;
    
    public static final int INDEX_HELP_TITLE                    = 500;
    public static final int INDEX_HELP_BACK_OPTION              = 501;
    public static final int INDEX_HELP_TEXT_BASE                = 502;
    
    public static final int INDEX_UNREACHABLE_TITLE             = 600;
    public static final int INDEX_UNREACHABLE_TEXT              = 601;
    public static final int INDEX_UNREACHABLE_BACK_OPTION       = 602;
    
    public static final int INDEX_LEVEL_LIST_TITLE              = 700;
    public static final int INDEX_LEVEL_LIST_BACK_OPTION        = 701;
    
    public static final int INDEX_UNABLE_TO_STORE_TITLE         = 800;
    public static final int INDEX_UNABLE_TO_STORE_TEXT          = 801;
    public static final int INDEX_UNABLE_TO_STORE_BACK_OPTION   = 802;
    
    public static final int INDEX_SUCCESSFUL_STORE_TITLE        = 900;
    public static final int INDEX_SUCCESSFUL_STORE_TEXT         = 901;
    public static final int INDEX_SUCCESSFUL_STORE_BACK_OPTION  = 902;
    
    public static final int INDEX_BLUETOOTH_UUID                = 22000;
    
    
    
    
    
    public static final int SELECTION_MAIN_MENU_HOST            = 0;
    public static final int SELECTION_MAIN_MENU_JOIN            = 1;
    public static final int SELECTION_MAIN_MENU_HELP            = 2;
    public static final int SELECTION_MAIN_MENU_BACK            = 3;
    
    public static final int SELECTION_UNREACHABLE_BACK          = 0;
    public static final int SELECTION_UNABLE_TO_STORE_BACK      = 0;
    public static final int SELECTION_SUCCESSFUL_STORE_BACK     = 0;
    
    
    
    
    public cydImageHeaderMenuWindowOutput m_mainMenuWindow;
    public cydImageHeaderMenuWindowOutput m_serverListWindow;
    public cydMessageWindowOutput m_searchingMessageWindow;
    public cydMessageWindowOutput m_waitingMessageWindow;
    public cydMessageWindowOutput m_helpMessageWindow;
    public cydSimpleAskWindowOutput m_unreachableConfirmWindow;
    public cydSimpleAskWindowOutput m_successfullyStoredConfirmWindow;
    public cydSimpleAskWindowOutput m_alreadyExistsConfirmWindow;
    public cydImageHeaderMenuWindowOutput m_levelListWindow;
    
    
    
    
    
    public char[] m_searchNonFoundText;
    public char[] m_searchWaitText;
    public char[] m_waitServerText;
    public char[] m_serverListBackOption;
    public char[] m_levelListBackOption;
    
    
    
    
    public static final char []EMPTY_BUFFER                     = new char[0];
    
    
    
    
    
    // game states
    public static final int STATE_MAIN_MENU                     = 0;
    public static final int STATE_JOIN_DEVICE_SEARCH            = 1;
    public static final int STATE_JOIN_DEVICE_SELECT            = 2;
    public static final int STATE_HOST                          = 3;
    public static final int STATE_HELP                          = 4;
    public static final int STATE_LEVEL_LIST                    = 5;
    public static final int STATE_UNREACHABLE                   = 6;
    public static final int STATE_UNABLE_TO_STORE               = 7;
    public static final int STATE_SUCCESSFUL_STORE              = 8;
    
    
    
    
    public int m_state = STATE_MAIN_MENU;
    
    
    
    public String m_bluetoothUUID;
    
    cydLevelStorageSystem m_lss = new cydLevelStorageSystem();
    
    public cydP2PClientConnectionProcessor m_clientConnectionProcessor = new cydP2PClientConnectionProcessor();
    public cydP2PServerConnectionProcessor m_serverConnectionProcessor = new cydP2PServerConnectionProcessor();
    public cydBTRFCOMMServer m_rfcommServer = new cydBTRFCOMMServer();
    public cydBTDiscovery m_rfcommDiscovery = new cydBTDiscovery();
    
    public char [][]m_serverListItems = null;
    public char [][]m_levelListItems = null;
    public String []m_serverURLs = null;
    
    public boolean m_searchComplete;
    
    
    
    
    
    
    
    public static final int C_CMD_NONE                          = -1;
    public static final int C_CMD_MAP_LIST_REQ                  = 0;
    public static final int C_CMD_MAP_DOWNLOAD_REQ              = 1;
    
    
    
    
    public cydP2PCanvas(cydGameManager gm) {
        super(gm.m_font);
        
        m_gm = gm;
    }
    
    public void setup() {
        setBackground(0x000000);
        
        byte []vmData = cydGameManager.getISToByteArray(getClass().getResourceAsStream("/p2ph_res.script"));
        
        loadMainMenuWindow(vmData);
        loadSearchingWindow(vmData);
        loadServerListWindow(vmData);
        loadWaitingWindow(vmData);
        loadHelpMessageWindow(vmData);
        loadUnreachableWindow(vmData);
        loadLevelListWindow(vmData);
        loadUUID(vmData);
        loadUnableToStoreWindow(vmData);
        loadSuccessfullStoreWindow(vmData);
        
        m_lss.open();
        m_lss.loadHeaderData();
    }
    
    public void stop() {
        m_lss.close();
    }
    
    public void loadUUID(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        m_bluetoothUUID = new String(cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_BLUETOOTH_UUID));
    }
    
    public void loadUnreachableWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_UNREACHABLE_TITLE);
        char []text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_UNREACHABLE_TEXT);
        
        char [][]options = new char[1][];
        
        options[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_UNREACHABLE_BACK_OPTION);
        
        m_unreachableConfirmWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, options, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadMainMenuWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_TITLE);
        
        char [][]options = new char[4][];
        
        options[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_HOST_RACE_OPTION);
        options[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_JOIN_RACE_OPTION);
        options[2] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_HELP_OPTION);
        options[3] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_BACK_OPTION);
        
        m_mainMenuWindow = new cydImageHeaderMenuWindowOutput(m_gm.m_font, title, null, options, null, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadSearchingWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SEARCHING_TITLE);
        char []leftOption = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SEARCHING_BACK_OPTION);
        
        
        m_searchingMessageWindow = new cydMessageWindowOutput(m_gm.m_font, title, leftOption, EMPTY_BUFFER, 15, 15, m_width - 30, m_height - 30, 0x0000FF, m_width, m_height);
        
        m_searchWaitText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SEARCHING_SEARCHING_TEXT);
        m_searchNonFoundText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SEARCHING_NON_FOUND_TEXT);
    }
    
    public void loadServerListWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SERVER_LIST_TITLE);
        
        m_serverListBackOption = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SERVER_LIST_BACK_OPTION);
        
        m_serverListWindow = new cydImageHeaderMenuWindowOutput(m_gm.m_font, title, null, null, null, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadWaitingWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_WAITING_TITLE);
        char []leftOption = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_WAITING_BACK_OPTION);
        
        
        m_waitingMessageWindow = new cydMessageWindowOutput(m_gm.m_font, title, leftOption, EMPTY_BUFFER, 15, 15, m_width - 30, m_height - 30, 0x0000FF, m_width, m_height);
        
        m_waitServerText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_WAITING_TEXT);
    }
    
    public void loadHelpMessageWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_HELP_TITLE);
        char []leftOption = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_HELP_BACK_OPTION);
        
        
        m_helpMessageWindow = new cydMessageWindowOutput(m_gm.m_font, title, leftOption, EMPTY_BUFFER, 15, 15, m_width - 30, m_height - 30, 0x0000FF, m_width, m_height);
        
        int next = 0;
        
        char []text = null;
        while ((text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_HELP_TEXT_BASE + next)) != null) {
            m_helpMessageWindow.addItem(text);
            next++;
        }
    }
    
    public void loadLevelListWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_LIST_TITLE);
        
        m_levelListBackOption = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_LIST_BACK_OPTION);
        
        m_levelListWindow = new cydImageHeaderMenuWindowOutput(m_gm.m_font, title, null, null, null, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadUnableToStoreWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_UNABLE_TO_STORE_TITLE);
        char []text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_UNABLE_TO_STORE_TEXT);
        
        char [][]options = new char[1][];
        
        options[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_UNABLE_TO_STORE_BACK_OPTION);
        
        m_alreadyExistsConfirmWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, options, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadSuccessfullStoreWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SUCCESSFUL_STORE_TITLE);
        char []text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SUCCESSFUL_STORE_TEXT);
        
        char [][]options = new char[1][];
        
        options[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SUCCESSFUL_STORE_BACK_OPTION);
        
        m_successfullyStoredConfirmWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, options, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public boolean process(int timeElapsed) {
        int keyStates = m_gm.m_keyInputSlowDowner.processKeyPress(getKeyStates(), timeElapsed);
        
        switch (m_state) {
            case cydP2PCanvas.STATE_MAIN_MENU: {
                int selection = m_mainMenuWindow.process(timeElapsed, keyStates);
                
                switch (selection) {
                    case cydP2PCanvas.SELECTION_MAIN_MENU_HOST: {
                        m_rfcommServer.stopServer();
                        
                        Thread.yield();     // give chance for server to close (bad style, may still cause race conditions)
                        Thread.yield();
                        Thread.yield();
                        
                        m_rfcommServer.startServer(m_bluetoothUUID, "MADKAZ1", true);
                        
                        
                        m_waitingMessageWindow.removeAllItems();
                        m_waitingMessageWindow.addItem(EMPTY_BUFFER);
                        m_waitingMessageWindow.addItem(m_waitServerText);
                        
                        m_serverConnectionProcessor.reset(5, m_lss);
                        
                        m_state = cydP2PCanvas.STATE_HOST;
                    }
                    break;
                    case cydP2PCanvas.SELECTION_MAIN_MENU_JOIN: {
                        m_rfcommDiscovery.startAsync(cydBTDiscovery.BT_RFCOMM_TYPE, m_bluetoothUUID);
                        
                        m_searchingMessageWindow.removeAllItems();
                        m_searchingMessageWindow.addItem(EMPTY_BUFFER);
                        m_searchingMessageWindow.addItem(m_searchWaitText);
                        
                        m_searchComplete = false;
                        
                        m_state = cydP2PCanvas.STATE_JOIN_DEVICE_SEARCH;
                    }
                    break;
                    case cydP2PCanvas.SELECTION_MAIN_MENU_HELP: {
                        m_state = cydP2PCanvas.STATE_HELP;
                    }
                    break;
                    case cydP2PCanvas.SELECTION_MAIN_MENU_BACK: {
                    }
                    return false;
                }
            }
            break;
            case cydP2PCanvas.STATE_JOIN_DEVICE_SEARCH: {
                int selection = m_searchingMessageWindow.process(timeElapsed, keyStates);
                
                if (m_rfcommDiscovery.isCompleteAsync() && !m_searchComplete) {
                    Vector services = m_rfcommDiscovery.getServersAsync();
                    
                    if (services.size() == 0) {
                        m_searchComplete = true;
                        
                        m_searchingMessageWindow.addItem(EMPTY_BUFFER);
                        m_searchingMessageWindow.addItem(m_searchNonFoundText);
                    } else {
                        m_serverListItems = new char[services.size() + 1][];
                        m_serverListItems[0] = m_serverListBackOption;
                        
                        m_serverURLs = new String[services.size()];
                        
                        m_rfcommDiscovery.filterToRFCOMMOnly(services);
                        
                        Enumeration e = services.elements();
                        
                        int counter = 0;
                        
                        while (e.hasMoreElements()) {
                            ServiceRecord sr = (ServiceRecord)e.nextElement();
                            String name = "UNKNOWN DEVICE";
                            
                            try {
                                name = sr.getHostDevice().getFriendlyName(true);
                            } catch (IOException ex) { }
                            
                            m_serverListItems[counter + 1] = ((counter + 1) + "." + name).toCharArray();
                            m_serverURLs[counter] = sr.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                            
                            counter++;
                        }
                        
                        m_serverListWindow.reset(null, null, null, m_serverListItems, null, null, -1, -1, -1, -1, -1);
                        
                        m_state = cydP2PCanvas.STATE_JOIN_DEVICE_SELECT;
                    }
                } else if (selection == 1) {
                    m_rfcommDiscovery.cancel();
                    
                    Thread.yield();     // give chance for server to close (bad style, may still cause race conditions)
                    Thread.yield();
                    Thread.yield();
                    
                    m_state = cydP2PCanvas.STATE_MAIN_MENU;
                }
            }
            break;
            case cydP2PCanvas.STATE_JOIN_DEVICE_SELECT: {
                int selection = m_serverListWindow.process(timeElapsed, keyStates);
                
                if (selection == 0) {
                    m_state = cydP2PCanvas.STATE_MAIN_MENU;
                } else if (selection > 0) {
                    selection--;
                    
                    try {
                        StreamConnection conn = (StreamConnection)Connector.open(m_serverURLs[selection]);
                        
                        m_clientConnectionProcessor.reset(conn, m_lss);
                        m_clientConnectionProcessor.sendCommand(cydP2PCanvas.C_CMD_MAP_LIST_REQ, 0);
                        
                        m_clientConnectionProcessor.recvFully();
                        
                        char [][]incomingLevelList= m_clientConnectionProcessor.getLevelList();
                        m_levelListItems = new char[incomingLevelList.length + 1][];
                        m_levelListItems[0] = m_serverListBackOption;
                        System.arraycopy(incomingLevelList, 0, m_levelListItems, 1, incomingLevelList.length);
                        
                        m_levelListWindow.reset(null, null, null, m_levelListItems, null, null, -1, -1, -1, -1, -1);
                        
                        m_state = cydP2PCanvas.STATE_LEVEL_LIST;
                    } catch (IOException ex) {
                        m_clientConnectionProcessor.close();
                        m_state = cydP2PCanvas.STATE_UNREACHABLE;
                    }
                }
            }
            break;
            case cydP2PCanvas.STATE_HOST: {
                int selection = m_waitingMessageWindow.process(timeElapsed, keyStates);
                
                if (selection == 1) {
                    m_serverConnectionProcessor.close();
                    m_rfcommServer.stopServer();
                    
                    Thread.yield();     // give chance for server to close (bad style, may still cause race conditions)
                    Thread.yield();
                    Thread.yield();
                    
                    m_state = cydP2PCanvas.STATE_MAIN_MENU;
                }
                
                Vector msgs = new Vector();
                m_rfcommServer.getPendingMessages(msgs);
                Enumeration e = msgs.elements();
                
                while (e.hasMoreElements()) {
                    m_waitingMessageWindow.addItem(EMPTY_BUFFER);
                    m_waitingMessageWindow.addItem(((String)e.nextElement()).toCharArray());
                    m_waitingMessageWindow.forceDown();
                }
                
                Vector conns = new Vector();
                m_rfcommServer.getPendingConnections(conns);
                
                if (conns.size() > 0) {
                    StreamConnection conn = (StreamConnection)conns.elementAt(0);
                    m_serverConnectionProcessor.incomingConnection(conn);
                }
                
                m_serverConnectionProcessor.process(timeElapsed);
            }
            break;
            case cydP2PCanvas.STATE_HELP: {
                int selection = m_helpMessageWindow.process(timeElapsed, keyStates);
                
                if (selection == 1)
                    m_state = cydP2PCanvas.STATE_MAIN_MENU;
            }
            break;
            case cydP2PCanvas.STATE_LEVEL_LIST: {
                int selection = m_levelListWindow.process(timeElapsed, keyStates);
                
                if (selection == 0) {
                    m_clientConnectionProcessor.close();
                    m_state = cydP2PCanvas.STATE_MAIN_MENU;
                } else if (selection > 0) {
                    selection--;
                    
                    try {
                        m_clientConnectionProcessor.sendCommand(cydP2PCanvas.C_CMD_MAP_DOWNLOAD_REQ, selection);
                        m_clientConnectionProcessor.recvFully();
                        
                        byte []data = m_clientConnectionProcessor.getLevel();
                        
                        Hashtable levelMap = m_lss.levelEntryToHash(data);
                        
                        char [][]storedShortNames = m_lss.getLevelShortNames();
                        String downloadedShortName = (String)levelMap.get(cydLevelStorageSystem.KEY_SHORT_NAME);

                        boolean found = false;
                        
                        for (int i = 0; i < storedShortNames.length; i++) {
                            if (downloadedShortName.equalsIgnoreCase(new String(storedShortNames[i]))) {
                                found = true;
                                break;
                            }
                        }
                        
                        if (found) {
                            m_state = cydP2PCanvas.STATE_UNABLE_TO_STORE;
                        } else {
                            m_lss.addLevel(data);
                            m_lss.close();
                            m_lss.open();
                            m_lss.loadHeaderData();
                            m_state = cydP2PCanvas.STATE_SUCCESSFUL_STORE;
                        }
                    } catch (IOException ex) {
                        m_clientConnectionProcessor.close();
                        m_state = cydP2PCanvas.STATE_UNREACHABLE;
                    }
                }
            }
            break;
            case cydP2PCanvas.STATE_UNREACHABLE: {
                int selection = m_unreachableConfirmWindow.process(timeElapsed, keyStates);
                
                switch (selection) {
                    case cydP2PCanvas.SELECTION_UNREACHABLE_BACK:
                        m_state = cydP2PCanvas.STATE_MAIN_MENU;
                        break;
                }
            }
            break;
            case cydP2PCanvas.STATE_UNABLE_TO_STORE: {
                int selection = m_alreadyExistsConfirmWindow.process(timeElapsed, keyStates);
                
                switch (selection) {
                    case cydP2PCanvas.SELECTION_UNABLE_TO_STORE_BACK:
                        m_state = cydP2PCanvas.STATE_LEVEL_LIST;
                        break;
                }
            }
            break;
            case cydP2PCanvas.STATE_SUCCESSFUL_STORE: {
                int selection = m_successfullyStoredConfirmWindow.process(timeElapsed, keyStates);
                
                switch (selection) {
                    case cydP2PCanvas.SELECTION_SUCCESSFUL_STORE_BACK:
                        m_state = cydP2PCanvas.STATE_LEVEL_LIST;
                        break;
                }
            }
            break;
        }
        
        return true;
    }
    
    public void draw() {
        Graphics g = m_graphics;
        
        switch (m_state) {
            case cydP2PCanvas.STATE_MAIN_MENU: {
                m_mainMenuWindow.draw(g);
            }
            break;
            case cydP2PCanvas.STATE_JOIN_DEVICE_SEARCH: {
                m_searchingMessageWindow.draw(g);
            }
            break;
            case cydP2PCanvas.STATE_JOIN_DEVICE_SELECT: {
                m_serverListWindow.draw(g);
            }
            break;
            case cydP2PCanvas.STATE_HOST: {
                m_waitingMessageWindow.draw(g);
            }
            break;
            case cydP2PCanvas.STATE_HELP: {
                m_helpMessageWindow.draw(g);
            }
            break;
            case cydP2PCanvas.STATE_LEVEL_LIST: {
                m_levelListWindow.draw(g);
            }
            break;
            case cydP2PCanvas.STATE_UNREACHABLE: {
                m_unreachableConfirmWindow.draw(g);
            }
            break;
            case cydP2PCanvas.STATE_UNABLE_TO_STORE: {
                m_alreadyExistsConfirmWindow.draw(g);
            }
            break;
            case cydP2PCanvas.STATE_SUCCESSFUL_STORE: {
                m_successfullyStoredConfirmWindow.draw(g);
            }
            break;
        }
    }
    
    public void loadingScreen() {
        char []msg = LOADING_TEXT;
        
        m_graphics.setColor(0x000000);
        m_graphics.fillRect(0, 0, getWidth(), getHeight());
        
        int x = getWidth()/2 - m_gm.m_font.getWidth(msg)/2;
        int y = getHeight()/2 - m_gm.m_font.getHeight()/2;
        
        m_gm.m_font.setForegroundOutlineColor(0xFFFFFF);
        m_gm.m_font.drawString(m_graphics, msg, x, y, -1);
    }
}
