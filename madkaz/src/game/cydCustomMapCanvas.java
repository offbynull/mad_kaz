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
import framework.cydImageHeaderMenuWindowOutput;
import framework.cydMessageWindowOutput;
import framework.cydSimpleAskWindowOutput;
import java.io.DataInputStream;
import javax.microedition.lcdui.Graphics;
import vm.cydVMEngine;
import vm.cydByteArrayInputStream;

public class cydCustomMapCanvas extends cydGameCanvas {
    public static boolean m_disclaimerAlreadyAccepted = false;
    
    // res ids
    public static final int INDEX_DISCLAIMER_TITLE              = 100;
    public static final int INDEX_DISCLAIMER_TEXT               = 101;
    public static final int INDEX_DISCLAIMER_AGREE_OPTION       = 102;
    public static final int INDEX_DISCLAIMER_BACK_OPTION        = 103;
    
    public static final int INDEX_MAIN_MENU_TITLE               = 200;
    public static final int INDEX_MAIN_MENU_LOAD_MAP_OPTION     = 201;
    public static final int INDEX_MAIN_MENU_NEW_MAP_OPTION      = 202;
    public static final int INDEX_MAIN_MENU_CREATE_OPTION       = 203;
    public static final int INDEX_MAIN_MENU_HELP_OPTION         = 204;
    public static final int INDEX_MAIN_MENU_BACK_OPTION         = 205;
    
    public static final int INDEX_CREATE_MAP_TITLE              = 300;
    public static final int INDEX_CREATE_MAP_BACK_OPTION        = 301;
    public static final int INDEX_CREATE_MAP_DESC_BASE          = 302;
    
    public static final int INDEX_HELP_TITLE                    = 400;
    public static final int INDEX_HELP_BACK_OPTION              = 401;
    public static final int INDEX_HELP_DESC_BASE                = 402;
    
    public static final int INDEX_SHARE_MAPS_TITLE              = 500;
    public static final int INDEX_SHARE_MAPS_TEXT               = 501;
    public static final int INDEX_SHARE_MAPS_ONLINE_OPTION      = 502;
    public static final int INDEX_SHARE_MAPS_P2P_OPTION         = 503;
    public static final int INDEX_SHARE_MAPS_BACK_OPTION        = 504;
    
    public static final int INDEX_LOAD_MAP_TITLE                = 600;
    public static final int INDEX_LOAD_MAP_BACK_OPTION          = 601;
    
    
    
    
    public static final int SELECTION_DISCLAIMER_AGREE          = 0;
    public static final int SELECTION_DISCLAIMER_DISAGREE       = 1;
    
    public static final int SELECTION_MAIN_MENU_LOAD_MAP        = 0;
    public static final int SELECTION_MAIN_MENU_NEW_MAP         = 1;
    public static final int SELECTION_MAIN_MENU_CREATE          = 2;
    public static final int SELECTION_MAIN_MENU_HELP            = 3;
    public static final int SELECTION_MAIN_MENU_BACK            = 4;
    
    public static final int SELECTION_CREATE_MAP_BACK           = 1;
    public static final int SELECTION_HELP_BACK                 = 1;
    
    public static final int SELECTION_SHARE_MAPS_INTERNET       = 0;
    public static final int SELECTION_SHARE_MAPS_P2P            = 1;
    public static final int SELECTION_SHARE_MAPS_BACK           = 2;
    
    public static final int SELECTION_LOAD_MAP_BACK             = 0;
    
    
    public cydSimpleAskWindowOutput m_disclaimerWindow;
    public cydSimpleAskWindowOutput m_getMapsWindow;
    public cydMessageWindowOutput m_createWindow;
    public cydMessageWindowOutput m_helpWindow;
    public cydImageHeaderMenuWindowOutput m_mainMenuWindow;
    public cydImageHeaderMenuWindowOutput m_loadMapWindow;
    
    public cydGameManager m_gm;
    
    
    
    // game states
    public static final int STATE_DISCLAIMER                    = 0;
    public static final int STATE_MAIN_MENU                     = 1;
    public static final int STATE_SHARE_MAP                     = 2;
    public static final int STATE_CREATE                        = 3;
    public static final int STATE_HELP                          = 4;
    
    public int m_state = STATE_DISCLAIMER;
    
    
    
    
    
    //
    public static final char []EMPTY_BUFFER                     = new char[0];
    
    
    
    
    public int m_return;
    
    public static final int RETURN_BACK                         = 0;
    public static final int RETURN_ONLINE                       = 1;
    public static final int RETURN_P2P                     = 2;
    public static final int RETURN_BROWSE                       = 3;
        
    public cydCustomMapCanvas(cydGameManager gm) {
        super(gm.m_font);
        
        m_gm = gm;
    }

    public void setup() {
        setBackground(0x000000);
        
        byte []vmData = cydGameManager.getISToByteArray(getClass().getResourceAsStream("/csm_res.script"));
        
        loadDisclaimerWindow(vmData);
        loadCreateWindow(vmData);
        loadHelpWindow(vmData);
        loadMainMenuWindow(vmData);
        loadGetMapsWindow(vmData);
        loadLoadMapWindow(vmData);
    }
    
    public void loadLoadMapWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LOAD_MAP_TITLE);
        
        char [][]options = new char[1][];
        
        options[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LOAD_MAP_BACK_OPTION);
        
        m_loadMapWindow = new cydImageHeaderMenuWindowOutput(m_gm.m_font, title, null, options, null, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadGetMapsWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SHARE_MAPS_TITLE);
        char []text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SHARE_MAPS_TEXT);
        
        char [][]options = new char[3][];
        
        options[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SHARE_MAPS_ONLINE_OPTION);
        options[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SHARE_MAPS_P2P_OPTION);
        options[2] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SHARE_MAPS_BACK_OPTION);
        
        m_getMapsWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, options, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadMainMenuWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_TITLE);
        
        char [][]options = new char[5][];
        
        options[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_LOAD_MAP_OPTION);
        options[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_NEW_MAP_OPTION);
        options[2] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_CREATE_OPTION);
        options[3] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_HELP_OPTION);
        options[4] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_BACK_OPTION);
        
        m_mainMenuWindow = new cydImageHeaderMenuWindowOutput(m_gm.m_font, title, null, options, null, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadDisclaimerWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_DISCLAIMER_TITLE);
        char []text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_DISCLAIMER_TEXT);
        
        char [][]options = new char[2][];
        
        options[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_DISCLAIMER_AGREE_OPTION);
        options[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_DISCLAIMER_BACK_OPTION);
        
        m_disclaimerWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, options, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadCreateWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_CREATE_MAP_TITLE);
        char []leftOption = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_CREATE_MAP_BACK_OPTION);
        
        
        m_createWindow = new cydMessageWindowOutput(m_gm.m_font, title, leftOption, EMPTY_BUFFER, 15, 15, m_width - 30, m_height - 30, 0x0000FF, m_width, m_height);
        
        int next = 0;
        
        char []text = null;
        while ((text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_CREATE_MAP_DESC_BASE + next)) != null) {
            m_createWindow.addItem(text);
            next++;
        }
    }
    
    public void loadHelpWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_HELP_TITLE);
        char []leftOption = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_HELP_BACK_OPTION);
        
        
        m_helpWindow = new cydMessageWindowOutput(m_gm.m_font, title, leftOption, EMPTY_BUFFER, 15, 15, m_width - 30, m_height - 30, 0x0000FF, m_width, m_height);
        
        int next = 0;
        
        char []text = null;
        while ((text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_HELP_DESC_BASE + next)) != null) {
            m_helpWindow.addItem(text);
            next++;
        }
    }
    


    public void draw() {
        Graphics g = m_graphics;
        
        switch (m_state) {
            case cydCustomMapCanvas.STATE_DISCLAIMER: {
                m_disclaimerWindow.draw(g);
            }
            break;
            case cydCustomMapCanvas.STATE_MAIN_MENU: {
                m_mainMenuWindow.draw(g);
            }
            break;
            case cydCustomMapCanvas.STATE_SHARE_MAP: {
                m_getMapsWindow.draw(g);
            }
            break;
            case cydCustomMapCanvas.STATE_CREATE: {
                m_createWindow.draw(g);
            }
            break;
            case cydCustomMapCanvas.STATE_HELP: {
                m_helpWindow.draw(g);
            }
            break;
        }
    }

    public boolean process(int timeElapsed) {
        int keyStates = m_gm.m_keyInputSlowDowner.processKeyPress(getKeyStates(), timeElapsed);
        
        switch (m_state) {
            case cydCustomMapCanvas.STATE_DISCLAIMER: {
                if (!m_disclaimerAlreadyAccepted) {
                    int selection = m_disclaimerWindow.process(timeElapsed, keyStates);

                    switch (selection) {
                        case cydCustomMapCanvas.SELECTION_DISCLAIMER_AGREE: {
                            m_state = cydCustomMapCanvas.STATE_MAIN_MENU;
                        }
                        break;
                        case cydCustomMapCanvas.SELECTION_DISCLAIMER_DISAGREE: {
                        }
                        return false;
                    }
                } else {
                    m_state = cydCustomMapCanvas.STATE_MAIN_MENU;
                }
            }
            break;
            case cydCustomMapCanvas.STATE_MAIN_MENU: {
                m_disclaimerAlreadyAccepted = true;
                
                int selection = m_mainMenuWindow.process(timeElapsed, keyStates);
                
                switch (selection) {
                    case cydCustomMapCanvas.SELECTION_MAIN_MENU_LOAD_MAP: {
                        m_return = cydCustomMapCanvas.RETURN_BROWSE;
                    }
                    return false;
                    case cydCustomMapCanvas.SELECTION_MAIN_MENU_NEW_MAP: {
                        m_getMapsWindow.m_textOutput.renew();
                        m_state = cydCustomMapCanvas.STATE_SHARE_MAP;
                    }
                    break;
                    case cydCustomMapCanvas.SELECTION_MAIN_MENU_CREATE: {
                        m_state = cydCustomMapCanvas.STATE_CREATE;
                    }
                    break;
                    case cydCustomMapCanvas.SELECTION_MAIN_MENU_HELP: {
                        m_state = cydCustomMapCanvas.STATE_HELP;
                    }
                    break;
                    case cydCustomMapCanvas.SELECTION_MAIN_MENU_BACK: {
                        m_return = cydCustomMapCanvas.RETURN_BACK;
                    }
                    return false;
                }
            }
            break;
            case cydCustomMapCanvas.STATE_SHARE_MAP: {
                int selection = m_getMapsWindow.process(timeElapsed, keyStates);
                
                switch (selection) {
                    case cydCustomMapCanvas.SELECTION_SHARE_MAPS_INTERNET: {
                        m_return = cydCustomMapCanvas.RETURN_ONLINE;
                    }
                    return false;
                    case cydCustomMapCanvas.SELECTION_SHARE_MAPS_P2P: {
                        m_return = cydCustomMapCanvas.RETURN_P2P;
                    }
                    return false;
                    case cydCustomMapCanvas.SELECTION_SHARE_MAPS_BACK: {
                        m_state = cydCustomMapCanvas.STATE_MAIN_MENU;
                    }
                    break;
                }
            }
            break;
            case cydCustomMapCanvas.STATE_CREATE: {
                int selection = m_createWindow.process(timeElapsed, keyStates);
                
                switch (selection) {
                    case cydCustomMapCanvas.SELECTION_CREATE_MAP_BACK: {
                        m_state = cydCustomMapCanvas.STATE_MAIN_MENU;
                    }
                    break;
                }
            }
            break;
            case cydCustomMapCanvas.STATE_HELP: {
                int selection = m_helpWindow.process(timeElapsed, keyStates);
                
                switch (selection) {
                    case cydCustomMapCanvas.SELECTION_HELP_BACK: {
                        m_state = cydCustomMapCanvas.STATE_MAIN_MENU;
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