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

import framework.cydGameCanvas;
import framework.cydMessageWindowOutput;
import framework.cydNetworkAccess;
import framework.cydSimpleAskWindowOutput;
import framework.cydSimpleMenuWindowOutput;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import vm.cydByteArrayInputStream;

public class cydNetworkCanvas extends cydGameCanvas {
    public static final int DATA_UNKNOWN                        = 0;
    public static final int DATA_UI                             = 1;
    public static final int DATA_LEVELS                         = 2;
    
    
    
    // res ids
    public static final int INDEX_URL_ENTRYPOINT                = 0;
    
    public static final int INDEX_NETWORK_TITLE_BASE            = 100;
    public static final int INDEX_NETWORK_CANCEL_OPTION         = 104;
    public static final int INDEX_NETWORK_WAITING_TEXT          = 105;
    public static final int INDEX_NETWORK_ACCESS_TEXT           = 106;
    public static final int INDEX_NETWORK_FAILED_TEXT           = 107;
    public static final int INDEX_NETWORK_TOTAL_FAILED_TEXT     = 108;
    public static final int INDEX_NETWORK_UNRECOGNIZED_DATA     = 109;
    
    public static final int INDEX_MESSAGE_TITLE                 = 200;
    public static final int INDEX_MESSAGE_CONTINUE              = 201;
    
    public static final int INDEX_LIST_TITLE                    = 300;
    public static final int INDEX_LIST_CLOSE                    = 301;
    
    public static final int INDEX_CREATED_TIME                  = 400;
    public static final int INDEX_STORED_TIME                   = 401;
    public static final int INDEX_SHORT_NAME                    = 402;
    public static final int INDEX_LONG_NAME                     = 403;
    public static final int INDEX_AUTHOR_NAME                   = 404;
    public static final int INDEX_COMMENTS                      = 405;
    public static final int INDEX_STORED_SIZE                   = 406;
    public static final int INDEX_SUCCESSFUL                    = 407;
    public static final int INDEX_FAILED                        = 408;
    
    
    public static final int SELECTION_NETWORK_CANCEL            = 0;
    
    public static final int SELECTION_LIST_CLOSE                = 0;
    
    
    
    
    public cydSimpleAskWindowOutput m_networkStatusWindow;
    public cydMessageWindowOutput m_serverMessageWindow;
    public cydSimpleMenuWindowOutput m_serverListWindow;
    
    public char []m_closeListItemText;
    public Vector m_listLinks = new Vector();
    
    
    
    public static final int MAX_TITLE                           = 4;
    
    public static final int NET_STATE_WAITING                   = 0;
    public static final int NET_STATE_ACCESSING                 = 1;
    public static final int NET_STATE_FAILED                    = 2;
    public static final int NET_STATE_TOTAL_FAILED              = 3;
    public static final int NET_STATE_UNKNOWN_DATA              = 4;
    
    public char [][]m_networkStatusTitle;
    public char [][]m_networkStatusStates;
    
    public int m_networkState = 0;
    public int m_networkTitle = 0;
    
    public int m_failedElapsedTime = 0;
    public int m_failedCount = 0;
    
    public static final int MAX_FAILED_COUNT                    = 3;
    public static final int TOTAL_TIME_TILL_RETRY               = 8000;
    
    public String m_url;
    
    
    
    
    
    public static final int STATE_START_NETWORKING              = 0;
    public static final int STATE_NETWORKING                    = 1;
    public static final int STATE_FAILED_NETWORKING             = 2;
    public static final int STATE_SERVER_MSG                    = 3;
    public static final int STATE_LIST_MSG                      = 4;
    
    public int m_state = STATE_START_NETWORKING;
    
    
    
    
    
    public char []m_createdTimeText;
    public char []m_storedTimeText;
    public char []m_shortNameText;
    public char []m_longNameText;
    public char []m_authorNameText;
    public char []m_commentsText;
    public char []m_mapSizeText;
    public char []m_successfulText;
    public char []m_failedText;
    
    
    
    
    public cydGameManager m_gm;
    
    
    public static final char []EMPTY_BUFFER                     = new char[0];
    
    
    
    private static final int MAGIC_NUM_LEVEL                    = 0xFADEDACE;
    private static final int MAGIC_NUM_UI_LIST                  = 0xBABEF00D;
    
    
    
    public cydNetworkCanvas(cydGameManager gm) {
        super(gm.m_font);
        
        m_gm = gm;
    }
    
    public void setup() {
        setBackground(0x000000);
        
        byte []vmData = cydGameManager.getISToByteArray(getClass().getResourceAsStream("/net_res.script"));
        
        loadNetworkingDetails(vmData);
        loadNetworkStatusWindow(vmData);
        loadServerMsgWindow(vmData);
        loadMenuItem(vmData);
        loadLevelDetailsString(vmData);
    }
    
    public void loadLevelDetailsString(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
    
        m_createdTimeText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_CREATED_TIME);
        m_storedTimeText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_STORED_TIME);
        m_shortNameText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SHORT_NAME);
        m_longNameText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LONG_NAME);
        m_authorNameText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_AUTHOR_NAME);
        m_commentsText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_COMMENTS);
        m_mapSizeText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_STORED_SIZE);
        m_successfulText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SUCCESSFUL);
        m_failedText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_FAILED);
    }
    
    public void loadMenuItem(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        m_closeListItemText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LIST_CLOSE);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LIST_TITLE);
        
        m_serverListWindow = new cydSimpleMenuWindowOutput(m_gm.m_font, title, null, null, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadServerMsgWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MESSAGE_TITLE);
        char []rightOption = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MESSAGE_CONTINUE);
        
        m_serverMessageWindow = new cydMessageWindowOutput(m_gm.m_font, title, EMPTY_BUFFER, rightOption, 15, 15, m_width - 30, m_height - 30, 0x0000FF, m_width, m_height);
    }
    
    public void loadNetworkingDetails(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        m_url = new String(cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_URL_ENTRYPOINT));
    }
    
    public void loadNetworkStatusWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        m_networkStatusTitle = new char[4][];
        m_networkStatusStates = new char[5][];
        
        
        for (int i = 0; i < MAX_TITLE; i++)
            m_networkStatusTitle[i] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_NETWORK_TITLE_BASE + i);
        
        m_networkStatusStates[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_NETWORK_WAITING_TEXT);
        m_networkStatusStates[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_NETWORK_ACCESS_TEXT);
        m_networkStatusStates[2] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_NETWORK_FAILED_TEXT);
        m_networkStatusStates[3] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_NETWORK_TOTAL_FAILED_TEXT);
        m_networkStatusStates[4] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_NETWORK_UNRECOGNIZED_DATA);
        
        char []title = m_networkStatusTitle[0];
        char []text = m_networkStatusStates[NET_STATE_WAITING];
        
        char [][]options = new char[1][];
        
        options[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_NETWORK_CANCEL_OPTION);
        
        m_networkStatusWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, options, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void draw() {
        Graphics g = m_graphics;
        
        switch (m_state) {
            case cydNetworkCanvas.STATE_START_NETWORKING:
            case cydNetworkCanvas.STATE_NETWORKING:
            case cydNetworkCanvas.STATE_FAILED_NETWORKING: {
                m_networkStatusWindow.draw(g);
            }
            break;
            case cydNetworkCanvas.STATE_SERVER_MSG: {
                m_serverMessageWindow.draw(g);
            }
            break;
            case cydNetworkCanvas.STATE_LIST_MSG: {
                m_serverListWindow.draw(g);
            }
            break;
        }
    }
    
    public int processIncomingData(byte []data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        
        try {
            int magicNum = dis.readInt();
            
            if (magicNum == cydNetworkCanvas.MAGIC_NUM_UI_LIST) {
                m_serverMessageWindow.removeAllItems();
                
                
                int msgStringCount = dis.readInt();
                
                for (int i = 0; i < msgStringCount; i++)
                    m_serverMessageWindow.addItem(dis.readUTF().toCharArray());
                
                
                
                
                m_listLinks.removeAllElements();
                
                int listItemCount = dis.readInt();
                
                char [][]listItems = new char[listItemCount + 1][];
                
                listItems[0] = m_closeListItemText;
                
                for (int i = 0; i < listItemCount; i++) {
                    listItems[i+1] = dis.readUTF().toCharArray();
                    m_listLinks.addElement(dis.readUTF());
                }
                
                m_serverListWindow.reset(null, null, null, listItems, null, -1, -1, -1, -1, -1);
                
                return cydNetworkCanvas.DATA_UI;
            } else if (magicNum == cydLevelStorageSystem.LEVEL_MAGIC_NUM) {
                cydLevelStorageSystem lss = new cydLevelStorageSystem();
                
                lss.open();

                Hashtable levelMap = lss.levelEntryToHash(data);
                
                if (levelMap == null) {
                    lss.close();
                    return cydNetworkCanvas.DATA_UNKNOWN;
                }
                
                
                // check to see if level exists already
                lss.loadHeaderData();
                char [][]storedShortNames = lss.getLevelShortNames();
                String downloadedShortName = (String)levelMap.get(cydLevelStorageSystem.KEY_SHORT_NAME);
                
                for (int i = 0; i < storedShortNames.length; i++) {
                    if (downloadedShortName.equalsIgnoreCase(new String(storedShortNames[i]))) {
                        m_serverMessageWindow.removeAllItems();

                        m_serverMessageWindow.addItem(EMPTY_BUFFER);
                        m_serverMessageWindow.addItem((new String(m_shortNameText) + (String)levelMap.get(cydLevelStorageSystem.KEY_SHORT_NAME)).toCharArray());
                        m_serverMessageWindow.addItem(EMPTY_BUFFER);
                        m_serverMessageWindow.addItem(m_failedText);
                
                        lss.close();
                        return cydNetworkCanvas.DATA_LEVELS;
                    }
                }
                
                
                
                if (lss.addLevel(data) == false) {
                    lss.close();
                    return cydNetworkCanvas.DATA_UNKNOWN;
                }
                
                lss.close();
                
                
                // rebuild hash after loading level to get updated stored time. previous hash loading was to check for validity of header information.
                levelMap = lss.levelEntryToHash(data);
                
                
                m_serverMessageWindow.removeAllItems();
                
                m_serverMessageWindow.addItem(EMPTY_BUFFER);
                m_serverMessageWindow.addItem(m_successfulText);
                m_serverMessageWindow.addItem(EMPTY_BUFFER);
                m_serverMessageWindow.addItem((new String(m_createdTimeText) + new Date(((Long)levelMap.get(cydLevelStorageSystem.KEY_CREATED_TIME)).longValue()).toString()).toCharArray());
                m_serverMessageWindow.addItem(EMPTY_BUFFER);
                m_serverMessageWindow.addItem((new String(m_storedTimeText) + new Date(((Long)levelMap.get(cydLevelStorageSystem.KEY_STORED_TIME)).longValue()).toString()).toCharArray());
                m_serverMessageWindow.addItem(EMPTY_BUFFER);
                m_serverMessageWindow.addItem((new String(m_shortNameText) + (String)levelMap.get(cydLevelStorageSystem.KEY_SHORT_NAME)).toCharArray());
                m_serverMessageWindow.addItem(EMPTY_BUFFER);
                m_serverMessageWindow.addItem((new String(m_longNameText) + (String)levelMap.get(cydLevelStorageSystem.KEY_LONG_NAME)).toCharArray());
                m_serverMessageWindow.addItem(EMPTY_BUFFER);
                m_serverMessageWindow.addItem((new String(m_authorNameText) + (String)levelMap.get(cydLevelStorageSystem.KEY_AUTHOR_NAME)).toCharArray());
                m_serverMessageWindow.addItem(EMPTY_BUFFER);
                m_serverMessageWindow.addItem((new String(m_commentsText) + (String)levelMap.get(cydLevelStorageSystem.KEY_COMMENTS)).toCharArray());
                m_serverMessageWindow.addItem(EMPTY_BUFFER);
                m_serverMessageWindow.addItem((new String(m_mapSizeText) + data.length).toCharArray());
                m_serverMessageWindow.addItem(EMPTY_BUFFER);
                
                return cydNetworkCanvas.DATA_LEVELS;
            }
        } catch (Throwable t) {
            return cydNetworkCanvas.DATA_UNKNOWN;
        }
        
        return cydNetworkCanvas.DATA_UNKNOWN;
    }
    
    public boolean process(int timeElapsed) {
        int keyStates = m_gm.m_keyInputSlowDowner.processKeyPress(getKeyStates(), timeElapsed);
        
        switch (m_state) {
            case cydNetworkCanvas.STATE_START_NETWORKING: {
                switch (m_networkStatusWindow.process(timeElapsed, keyStates)) {
                    case cydNetworkCanvas.SELECTION_NETWORK_CANCEL:
                        return false;
                }
                
                m_networkTitle = (m_networkTitle + 1) % m_networkStatusTitle.length;
                m_networkStatusWindow.reset(null, m_networkStatusTitle[m_networkTitle], null, null, null, -1, -1, -1, -1, -1);
                
                if (!cydNetworkAccess.isWaiting()) {
                    cydNetworkAccess.accessURL(m_url);
                    m_state = cydNetworkCanvas.STATE_NETWORKING;
                    m_networkStatusWindow.m_textOutput.setBuffer(m_networkStatusStates[NET_STATE_ACCESSING]);
                }
            }
            break;
            case cydNetworkCanvas.STATE_NETWORKING: {
                switch (m_networkStatusWindow.process(timeElapsed, keyStates)) {
                    case cydNetworkCanvas.SELECTION_NETWORK_CANCEL:
                        return false;
                }
                
                m_networkTitle = (m_networkTitle + 1) % m_networkStatusTitle.length;
                m_networkStatusWindow.reset(null, m_networkStatusTitle[m_networkTitle], null, null, null, -1, -1, -1, -1, -1);
                
                if (!cydNetworkAccess.isWaiting()) {
                    byte []data = cydNetworkAccess.getAndClearData();
                    
                    if (data != null) {
                        int res = processIncomingData(data);
                        
                        switch (res) {
                            case cydNetworkCanvas.DATA_UNKNOWN: {
                                m_failedElapsedTime = 0;
                                m_failedCount++;

                                if (m_failedCount == cydNetworkCanvas.MAX_FAILED_COUNT) {
                                    m_networkStatusWindow.reset(null, m_networkStatusTitle[0], null, null, null, -1, -1, -1, -1, -1);
                                    m_state = cydNetworkCanvas.STATE_FAILED_NETWORKING;
                                    m_networkStatusWindow.m_textOutput.setBuffer(m_networkStatusStates[NET_STATE_TOTAL_FAILED]);
                                } else {
                                    m_state = cydNetworkCanvas.STATE_FAILED_NETWORKING;
                                    m_networkStatusWindow.m_textOutput.setBuffer(m_networkStatusStates[NET_STATE_UNKNOWN_DATA]);
                                }
                            }
                            break;
                            case cydNetworkCanvas.DATA_UI: {
                                m_networkStatusWindow.reset(null, m_networkStatusTitle[0], null, null, null, -1, -1, -1, -1, -1);
                                m_state = cydNetworkCanvas.STATE_SERVER_MSG;
                            }
                            break;
                            case cydNetworkCanvas.DATA_LEVELS: {
                                m_networkStatusWindow.reset(null, m_networkStatusTitle[0], null, null, null, -1, -1, -1, -1, -1);
                                m_state = cydNetworkCanvas.STATE_SERVER_MSG;
                            }
                            break;
                        }
                    } else {
                        m_failedElapsedTime = 0;
                        m_failedCount++;
                        
                        if (m_failedCount == cydNetworkCanvas.MAX_FAILED_COUNT) {
                            m_networkStatusWindow.reset(null, m_networkStatusTitle[0], null, null, null, -1, -1, -1, -1, -1);
                            m_state = cydNetworkCanvas.STATE_FAILED_NETWORKING;
                            m_networkStatusWindow.m_textOutput.setBuffer(m_networkStatusStates[NET_STATE_TOTAL_FAILED]);
                        } else {
                            m_state = cydNetworkCanvas.STATE_FAILED_NETWORKING;
                            m_networkStatusWindow.m_textOutput.setBuffer(m_networkStatusStates[NET_STATE_FAILED]);
                        }
                    }
                }
            }
            break;
            case cydNetworkCanvas.STATE_FAILED_NETWORKING: {
                switch (m_networkStatusWindow.process(timeElapsed, keyStates)) {
                    case cydNetworkCanvas.SELECTION_NETWORK_CANCEL:
                        return false;
                }
                
                if (m_failedCount != cydNetworkCanvas.MAX_FAILED_COUNT) {
                    m_networkTitle = (m_networkTitle + 1) % m_networkStatusTitle.length;
                    m_networkStatusWindow.reset(null, m_networkStatusTitle[m_networkTitle], null, null, null, -1, -1, -1, -1, -1);
                    
                    m_failedElapsedTime += timeElapsed;
                    
                    if (m_failedElapsedTime >= TOTAL_TIME_TILL_RETRY) {
                        m_state = cydNetworkCanvas.STATE_START_NETWORKING;
                        m_networkStatusWindow.m_textOutput.setBuffer(m_networkStatusStates[NET_STATE_WAITING]);
                    }
                }
            }
            break;
            case cydNetworkCanvas.STATE_SERVER_MSG: {
                int res = m_serverMessageWindow.process(timeElapsed, keyStates);
                
                if (res == 2)
                    m_state = cydNetworkCanvas.STATE_LIST_MSG;
            }
            break;
            case cydNetworkCanvas.STATE_LIST_MSG: {
                int res = m_serverListWindow.process(timeElapsed, keyStates);
                
                switch (res) {
                    case cydNetworkCanvas.SELECTION_LIST_CLOSE: {
                    }
                    return false;
                    default: {
                        if (res == -1)
                            break;
                        
                        res--;
                        
                        m_url = (String)m_listLinks.elementAt(res);
                        m_failedCount = 0;
                        m_failedElapsedTime = 0;
                        
                        m_state = cydNetworkCanvas.STATE_START_NETWORKING;
                        m_networkStatusWindow.m_textOutput.setBuffer(m_networkStatusStates[NET_STATE_WAITING]);
                    }
                    break;
                }
            }
            break;
        }
        
        return true;
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
