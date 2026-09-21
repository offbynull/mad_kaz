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
import framework.cydSimpleAskWindowOutput;
import framework.cydSimpleMenuWindowOutput;
import java.io.DataInputStream;
import java.util.Date;
import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;
import vm.cydByteArrayInputStream;

public class cydBrowseCanvas extends cydGameCanvas {
    public static final int RETURN_BACK                         = 0;
    public static final int RETURN_PLAY                         = 1;
    
    public int m_return                                         = -1;
    
    
    // res ids
    public static final int INDEX_LEVEL_LIST_TITLE              = 0;
    public static final int INDEX_LEVEL_LIST_BACK               = 1;
    
    public static final int INDEX_LEVEL_SELECTED_TITLE          = 100;
    public static final int INDEX_LEVEL_SELECTED_PLAY           = 101;
    public static final int INDEX_LEVEL_SELECTED_DETAILS        = 102;
    public static final int INDEX_LEVEL_SELECTED_DELETE         = 103;
    public static final int INDEX_LEVEL_SELECTED_BACK           = 104;
    
    public static final int INDEX_LEVEL_DETAILS_TITLE           = 200;
    public static final int INDEX_LEVEL_DETAILS_CREATED_TIME    = 201;
    public static final int INDEX_LEVEL_DETAILS_STORED_TIME     = 202;
    public static final int INDEX_LEVEL_DETAILS_SHORT_NAME      = 203;
    public static final int INDEX_LEVEL_DETAILS_LONG_NAME       = 204;
    public static final int INDEX_LEVEL_DETAILS_AUTHOR_NAME     = 205;
    public static final int INDEX_LEVEL_DETAILS_COMMENTS        = 206;
    public static final int INDEX_LEVEL_DETAILS_STORED_SIZE     = 207;
    public static final int INDEX_LEVEL_DETAILS_BACK            = 208;
    
    public static final int INDEX_LEVEL_DELETE_TITLE            = 300;
    public static final int INDEX_LEVEL_DELETE_TEXT             = 301;
    public static final int INDEX_LEVEL_DELETE_YES              = 302;
    public static final int INDEX_LEVEL_DELETE_NO               = 303;
    
    
    
    
    public static final int SELECTION_LEVEL_LIST_BACK           = 0;
    
    public static final int SELECTION_LEVEL_SELECTED_PLAY       = 0;
    public static final int SELECTION_LEVEL_SELECTED_DETAILS    = 1;
    public static final int SELECTION_LEVEL_SELECTED_DELETE     = 2;
    public static final int SELECTION_LEVEL_SELECTED_BACK       = 3;
    
    public static final int SELECTION_LEVEL_DELETE_YES          = 0;
    public static final int SELECTION_LEVEL_DELETE_NO           = 1;
    
    
    
    
    
    public cydSimpleMenuWindowOutput m_levelListWindow;
    public cydSimpleMenuWindowOutput m_optionsWindow;
    public cydSimpleAskWindowOutput m_deleteWindow;
    public cydMessageWindowOutput m_detailsWindow;
    
    
    
    
    public cydLevelStorageSystem m_lss = new cydLevelStorageSystem();
    
    
    
    
    public static final int STATE_LEVEL_LIST                    = 0;
    public static final int STATE_OPTION_LIST                   = 1;
    public static final int STATE_DELETE                        = 2;
    public static final int STATE_DETAILS                       = 3;
    
    public int m_state = STATE_LEVEL_LIST;
    
    
    
    
    
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
    
    
    
    public byte []m_vmData                                             = null;
    public int m_selectedIndex                                         = -1;

    public char[] m_levelListClose;
    public char[] m_levelListTitle;

    public boolean m_ignoreNextKeyPress;
    
    
    
    public cydBrowseCanvas(cydGameManager gm) {
        super(gm.m_font);
        
        m_gm = gm;
    }
    
    public void setup() {
        setBackground(0x000000);
        
        byte []vmData = cydGameManager.getISToByteArray(getClass().getResourceAsStream("/browse_res.script"));
        
        m_lss.open();
        m_lss.loadHeaderData();
        m_lss.close();
        
        loadConstListStrings(vmData);
        loadLevelListItems();
        
        loadDeleteWindow(vmData);
        loadDetailsWindow(vmData);
        loadLevelDetailsString(vmData);
        loadOptionsListItems(vmData);
    }

    public void loadConstListStrings(byte[] vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        m_levelListTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_LIST_TITLE);
        m_levelListClose = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_LIST_BACK);
    }
    
    public void loadDeleteWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_DELETE_TITLE);
        char []text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_DELETE_TEXT);
        
        char [][]options = new char[2][];
        
        options[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_DELETE_YES);
        options[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_DELETE_NO);
        
        m_deleteWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, options, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadDetailsWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_DETAILS_TITLE);
        char []leftOption = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_DETAILS_BACK);
        
        m_detailsWindow = new cydMessageWindowOutput(m_gm.m_font, title, leftOption, EMPTY_BUFFER, 15, 15, m_width - 30, m_height - 30, 0x0000FF, m_width, m_height);
    }
    
    public void loadLevelDetailsString(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        m_createdTimeText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_DETAILS_CREATED_TIME);
        m_storedTimeText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_DETAILS_STORED_TIME);
        m_shortNameText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_DETAILS_SHORT_NAME);
        m_longNameText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_DETAILS_LONG_NAME);
        m_authorNameText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_DETAILS_AUTHOR_NAME);
        m_commentsText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_DETAILS_COMMENTS);
        m_mapSizeText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_DETAILS_STORED_SIZE);
    }
    
    public void loadLevelListItems() {
        char [][]shortNames;
        shortNames = m_lss.getLevelShortNames();
        
        char [][]menuOptions = new char[shortNames.length + 1][];
        
        menuOptions[0] = m_levelListClose;
        
        for (int i = 0; i < shortNames.length; i++)
            menuOptions[i + 1] = (i + ". " + new String(shortNames[i])).toCharArray();
        
        m_levelListWindow = new cydSimpleMenuWindowOutput(m_gm.m_font, m_levelListTitle, null, menuOptions, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadOptionsListItems(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_SELECTED_TITLE);
        
        char [][]menuOptions = new char[4][];
        
        menuOptions[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_SELECTED_PLAY);
        menuOptions[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_SELECTED_DETAILS);
        menuOptions[2] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_SELECTED_DELETE);
        menuOptions[3] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEVEL_SELECTED_BACK);
        
        m_optionsWindow = new cydSimpleMenuWindowOutput(m_gm.m_font, title, null, menuOptions, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    
    public void draw() {
        Graphics g = m_graphics;
        
        switch (m_state) {
            case cydBrowseCanvas.STATE_LEVEL_LIST: {
                m_levelListWindow.draw(g);
            }
            break;
            case cydBrowseCanvas.STATE_OPTION_LIST: {
                m_optionsWindow.draw(g);
            }
            break;
            case cydBrowseCanvas.STATE_DELETE: {
                m_deleteWindow.draw(g);
            }
            break;
            case cydBrowseCanvas.STATE_DETAILS: {
                m_detailsWindow.draw(g);
            }
            break;
        }
    }
    
    public void loadLevelData() {
        m_lss.open();
        byte []data = m_lss.getLevelData(m_lss.getRecordIDs()[m_selectedIndex]);
        Hashtable levelMap = m_lss.levelEntryToHash(data);
        m_lss.close();
        
        m_detailsWindow.removeAllItems();
        
        m_detailsWindow.addItem(EMPTY_BUFFER);
        m_detailsWindow.addItem((new String(m_createdTimeText) + new Date(((Long)levelMap.get(cydLevelStorageSystem.KEY_CREATED_TIME)).longValue()).toString()).toCharArray());
        m_detailsWindow.addItem(EMPTY_BUFFER);
        m_detailsWindow.addItem((new String(m_storedTimeText) + new Date(((Long)levelMap.get(cydLevelStorageSystem.KEY_STORED_TIME)).longValue()).toString()).toCharArray());
        m_detailsWindow.addItem(EMPTY_BUFFER);
        m_detailsWindow.addItem((new String(m_shortNameText) + (String)levelMap.get(cydLevelStorageSystem.KEY_SHORT_NAME)).toCharArray());
        m_detailsWindow.addItem(EMPTY_BUFFER);
        m_detailsWindow.addItem((new String(m_longNameText) + (String)levelMap.get(cydLevelStorageSystem.KEY_LONG_NAME)).toCharArray());
        m_detailsWindow.addItem(EMPTY_BUFFER);
        m_detailsWindow.addItem((new String(m_authorNameText) + (String)levelMap.get(cydLevelStorageSystem.KEY_AUTHOR_NAME)).toCharArray());
        m_detailsWindow.addItem(EMPTY_BUFFER);
        m_detailsWindow.addItem((new String(m_commentsText) + (String)levelMap.get(cydLevelStorageSystem.KEY_COMMENTS)).toCharArray());
        m_detailsWindow.addItem(EMPTY_BUFFER);
        m_detailsWindow.addItem((new String(m_mapSizeText) + data.length).toCharArray());
        m_detailsWindow.addItem(EMPTY_BUFFER);
        
        m_vmData = (byte [])levelMap.get(cydLevelStorageSystem.KEY_VM_DATA);
    }
    
    public boolean process(int timeElapsed) {
        int keyStates = m_gm.m_keyInputSlowDowner.processKeyPress(getKeyStates(), timeElapsed);
        
        switch (m_state) {
            case cydBrowseCanvas.STATE_LEVEL_LIST: {
                int res = m_levelListWindow.process(timeElapsed, keyStates);
                
                switch (res) {
                    case cydBrowseCanvas.SELECTION_LEVEL_LIST_BACK: {
                        m_return = cydBrowseCanvas.RETURN_BACK;
                    }
                    return false;
                    default: {
                        res--;
                        
                        if (res < 0)
                            break;
                        
                        m_selectedIndex = res;
                        loadLevelData();
                        
                        m_ignoreNextKeyPress = true;
                        
                        m_state = cydBrowseCanvas.STATE_OPTION_LIST;
                    }
                    break;
                }
            }
            break;
            case cydBrowseCanvas.STATE_OPTION_LIST: {
                if (m_ignoreNextKeyPress) {
                    keyStates = 0;
                    m_ignoreNextKeyPress = false;
                }
                
                int res = m_optionsWindow.process(timeElapsed, keyStates);
                
                switch (res) {
                    case cydBrowseCanvas.SELECTION_LEVEL_SELECTED_PLAY: {
                        m_return = cydBrowseCanvas.RETURN_PLAY;
                    }
                    return false;
                    case cydBrowseCanvas.SELECTION_LEVEL_SELECTED_DETAILS: {
                        m_state = cydBrowseCanvas.STATE_DETAILS;
                    }
                    break;
                    case cydBrowseCanvas.SELECTION_LEVEL_SELECTED_DELETE: {
                        m_state = cydBrowseCanvas.STATE_DELETE;
                    }
                    break;
                    case cydBrowseCanvas.SELECTION_LEVEL_SELECTED_BACK: {
                        m_state = cydBrowseCanvas.STATE_LEVEL_LIST;
                    }
                    break;
                }
            }
            break;
            case cydBrowseCanvas.STATE_DELETE: {
                int res = m_deleteWindow.process(timeElapsed, keyStates);
                
                switch (res) {
                    case cydBrowseCanvas.SELECTION_LEVEL_DELETE_YES: {
                        m_lss.open();
                        m_lss.deleteLevelData(m_lss.getRecordIDs()[m_selectedIndex]);
                        m_lss.loadHeaderData();
                        m_lss.close();
                        
                        loadLevelListItems();
                        
                        m_state = cydBrowseCanvas.STATE_LEVEL_LIST;
                    }
                    break;
                    case cydBrowseCanvas.SELECTION_LEVEL_DELETE_NO: {
                        m_state = cydBrowseCanvas.STATE_OPTION_LIST;
                    }
                    break;
                }
            }
            break;
            case cydBrowseCanvas.STATE_DETAILS: {
                int res = m_detailsWindow.process(timeElapsed, keyStates);
                
                if (res == 1)
                    m_state = cydBrowseCanvas.STATE_OPTION_LIST;
                    
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
