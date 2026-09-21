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
import framework.cydGraphicsManager;
import framework.cydImageHeaderMenuWindowOutput;
import framework.cydMessageWindowOutput;
import framework.cydMusicPlayer;
import framework.cydPointInterpolator;
import framework.cydSimpleAskWindowOutput;
import java.io.DataInputStream;
import vm.cydByteArrayInputStream;
import vm.cydVMEngine;

public class cydMainMenuCanvas extends cydGameCanvas {
    public static boolean m_timerAlreadyElapsed = false;
    
    public int m_gotoState = 0;
    public cydGameManager m_gm;
    public cydGraphicsManager m_gfxMnger;
    
    public static final int IMAGE_ID_BG                   = 0;
    public static final int IMAGE_ID_MAIN_MENU            = 1;
    public static final int IMAGE_ID_ABOUT                = 2;
    public static final int IMAGE_ID_HELP                 = 3;
    
    public static final int IMAGE_TOTAL                   = 4;
    
    public static final char []EMPTY_BUFFER                   = new char[0];
    
    
    
    
    public static final int INDEX_BG_IMAGE                    = 20000;
    public static final int INDEX_MAIN_MENU_IMAGE             = 20001;
    public static final int INDEX_ABOUT_IMAGE                 = 20002;
    public static final int INDEX_HELP_IMAGE                  = 20003;
    
    
    
    public static final int INDEX_MAIN_MENU_PLAY_ITEM         = 10;
    public static final int INDEX_MAIN_MENU_RESET_ITEM        = 11;
    public static final int INDEX_MAIN_MENU_PRACTICE_ITEM     = 12;
    public static final int INDEX_MAIN_MENU_CUSTOM_MAPS_ITEM  = 13;
    public static final int INDEX_MAIN_MENU_SETTINGS_ITEM     = 14;
    public static final int INDEX_MAIN_MENU_HELP_ITEM         = 15;
    public static final int INDEX_MAIN_MENU_ABOUT_ITEM        = 16;
    public static final int INDEX_MAIN_MENU_CREDITS_ITEM      = 17;
    public static final int INDEX_MAIN_MENU_EXIT_ITEM         = 18;
    
    public static final int INDEX_QUIT_YES_ITEM               = 20;
    public static final int INDEX_QUIT_NO_ITEM                = 21;
    
    public static final int INDEX_PRACTICE_CLIMB_ITEM         = 30;
    public static final int INDEX_PRACTICE_DROP_ITEM          = 31;
    public static final int INDEX_PRACTICE_FREE_ITEM          = 32;
    public static final int INDEX_PRACTICE_BACK_ITEM          = 33;
    
    public static final int INDEX_SETTINGS_YES_ITEM           = 50;
    public static final int INDEX_SETTINGS_NO_ITEM            = 51;
    
    public static final int INDEX_RESET_YES_ITEM              = 60;
    public static final int INDEX_RESET_NO_ITEM               = 61;
    
    public static final int INDEX_LEFT_TO_CLOSE               = 70;
    
    
    public static final int INDEX_MAIN_MENU_TITLE             = 0;
    public static final int INDEX_QUIT_TITLE                  = 100;
    public static final int INDEX_PRACTICE_TITLE              = 200;
    public static final int INDEX_SETTINGS_TITLE              = 400;
    public static final int INDEX_RESET_TITLE                 = 500;
    public static final int INDEX_HELP_TITLE                  = 600;
    public static final int INDEX_ABOUT_TITLE                 = 700;
    
    
    
    public static final int INDEX_QUIT_TEXT                   = 101;
    public static final int INDEX_PRACTICE_TEXT               = 201;
    public static final int INDEX_SETTINGS_TEXT               = 401;
    public static final int INDEX_RESET_TEXT                  = 501;
    public static final int INDEX_HELP_TEXT_BASE              = 601;
    public static final int INDEX_ABOUT_TEXT_BASE             = 701;
    
    
    
    
    

    public static final int SELECTION_MAIN_MENU_PLAY_ITEM         = 0;
    public static final int SELECTION_MAIN_MENU_RESET_ITEM        = 1;
    public static final int SELECTION_MAIN_MENU_PRACTICE_ITEM     = 2;
    public static final int SELECTION_MAIN_MENU_CUSTOM_MAPS_ITEM  = 3;
    public static final int SELECTION_MAIN_MENU_SETTINGS_ITEM     = 4;
    public static final int SELECTION_MAIN_MENU_HELP_ITEM         = 5;
    public static final int SELECTION_MAIN_MENU_ABOUT_ITEM        = 6;
    public static final int SELECTION_MAIN_MENU_CREDITS_ITEM      = 7;
    public static final int SELECTION_MAIN_MENU_EXIT_ITEM         = 8;
    
    public static final int SELECTION_QUIT_YES_ITEM               = 0;
    public static final int SELECTION_QUIT_NO_ITEM                = 1;
    
    public static final int SELECTION_PRACTICE_CLIMB_ITEM         = 0;
    public static final int SELECTION_PRACTICE_DROP_ITEM          = 1;
    public static final int SELECTION_PRACTICE_FREE_ITEM          = 2;
    public static final int SELECTION_PRACTICE_BACK_ITEM          = 3;
    
    public static final int SELECTION_SETTINGS_YES_ITEM           = 0;
    public static final int SELECTION_SETTINGS_NO_ITEM            = 1;
    
    public static final int SELECTION_RESET_YES_ITEM              = 0;
    public static final int SELECTION_RESET_NO_ITEM               = 1;
    
    
    
    
    public static final int WINDOW_OFFSET_LEVEL = 10;
    public static final int WINDOW_OFFSET_LEVEL_2 = 15;
    
    public cydImageHeaderMenuWindowOutput m_mainMenuWindow;
    public cydSimpleAskWindowOutput m_quitConfirmWindow;
    public cydSimpleAskWindowOutput m_settingsConfirmWindow;
    public cydSimpleAskWindowOutput m_practiceConfirmWindow;
    public cydSimpleAskWindowOutput m_resetConfirmWindow;
    public cydMessageWindowOutput m_helpMessageWindow;
    public cydMessageWindowOutput m_aboutMessageWindow;
    
    public int m_width;
    public int m_height;
    
    public static final int STATE_PREWINDOW                              = 0;
    public static final int STATE_MAIN_MENU                              = 1;
    public static final int STATE_QUIT_CONFIRM                           = 2;
    public static final int STATE_PRACTICE_CONFIRM                       = 3;
    public static final int STATE_RESET_CONFIRM                          = 4;
    public static final int STATE_HELP_MESSAGE                           = 5;
    public static final int STATE_ABOUT_MESSAGE                          = 6;
    public static final int STATE_SETTINGS_CONFIRM                       = 7;
    
    public static final int STATE_PREWINDOW_WAIT_TIME                    = 3000;
    
    public int m_timeElapsed;
    
    public int m_state = STATE_PREWINDOW;
    
    public cydPointInterpolator m_bgMover;
    
    public static final int RETURN_QUIT                                  = 0;
    public static final int RETURN_PRACTICE_CLIMB                        = 1;
    public static final int RETURN_PRACTICE_DROP                         = 2;
    public static final int RETURN_PRACTICE_FREE                         = 3;
    public static final int RETURN_CONTINUE                              = 4;
    public static final int RETURN_PLAY                                  = 5;
    public static final int RETURN_RESET                                 = 6;
    public static final int RETURN_CUSTOM_MAPS                           = 7;
    public static final int RETURN_CREDITS                               = 8;
    
    public int m_return;
    
    public cydMainMenuCanvas(cydGameManager gm) {
        super(gm.m_font);
        
        m_gm = gm;
    }
    
    public void loadImages(byte []vmData) {
        try {
            cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
            DataInputStream vmDataDIS = new DataInputStream(vmDataIS);

            int offset = 0;
            int length = 0;

            offset = cydVMEngine.getResourceOffset(vmData, INDEX_BG_IMAGE);
            vmDataIS.setPosition(offset);
            length = vmDataDIS.readInt();
            offset += 4;
            m_gfxMnger.addImage(IMAGE_ID_BG, vmData, true, offset, length);



            offset = cydVMEngine.getResourceOffset(vmData, INDEX_MAIN_MENU_IMAGE);
            vmDataIS.setPosition(offset);
            length = vmDataDIS.readInt();
            offset += 4;
            m_gfxMnger.addImage(IMAGE_ID_MAIN_MENU, vmData, true, offset, length);



            offset = cydVMEngine.getResourceOffset(vmData, INDEX_ABOUT_IMAGE);
            vmDataIS.setPosition(offset);
            length = vmDataDIS.readInt();
            offset += 4;
            m_gfxMnger.addImage(IMAGE_ID_ABOUT, vmData, true, offset, length);



            offset = cydVMEngine.getResourceOffset(vmData, INDEX_HELP_IMAGE);
            vmDataIS.setPosition(offset);
            length = vmDataDIS.readInt();
            offset += 4;
            m_gfxMnger.addImage(IMAGE_ID_HELP, vmData, true, offset, length);

            m_gfxMnger.loadImage(-1);
            m_gfxMnger.unloadBuffers(-1);
        } catch (Throwable t) { 
        }
    }
    
    public void loadMainMenuWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_TITLE);
        
        char [][]items = new char[9][];
        
        items[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_PLAY_ITEM);
        items[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_RESET_ITEM);
        items[2] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_PRACTICE_ITEM);
        items[3] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_CUSTOM_MAPS_ITEM);
        items[4] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_SETTINGS_ITEM);
        items[5] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_HELP_ITEM);
        items[6] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_ABOUT_ITEM);
        items[7] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_CREDITS_ITEM);
        items[8] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_MAIN_MENU_EXIT_ITEM);
        
        m_mainMenuWindow = new cydImageHeaderMenuWindowOutput(m_gm.m_font, title, null, items, null, m_gfxMnger.getImage(IMAGE_ID_MAIN_MENU), WINDOW_OFFSET_LEVEL, WINDOW_OFFSET_LEVEL, m_width - (WINDOW_OFFSET_LEVEL << 1), m_height - (WINDOW_OFFSET_LEVEL << 1), 0x0000FF);
    }
    
    public void loadQuitConfirmWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_QUIT_TITLE);
        char []text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_QUIT_TEXT);
        
        char [][]items = new char[2][];
        
        items[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_QUIT_YES_ITEM);
        items[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_QUIT_NO_ITEM);
        
        m_quitConfirmWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, items, null, WINDOW_OFFSET_LEVEL_2, WINDOW_OFFSET_LEVEL_2, m_width - (WINDOW_OFFSET_LEVEL_2 << 1), m_height - (WINDOW_OFFSET_LEVEL_2 << 1), 0x0000FF);
    }
    
    public void loadPracticeConfirmWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_PRACTICE_TITLE);
        char []text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_PRACTICE_TEXT);
        
        char [][]items = new char[4][];
        
        items[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_PRACTICE_CLIMB_ITEM);
        items[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_PRACTICE_DROP_ITEM);
        items[2] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_PRACTICE_FREE_ITEM);
        items[3] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_PRACTICE_BACK_ITEM);
        
        m_practiceConfirmWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, items, null, WINDOW_OFFSET_LEVEL_2, WINDOW_OFFSET_LEVEL_2, m_width - (WINDOW_OFFSET_LEVEL_2 << 1), m_height - (WINDOW_OFFSET_LEVEL_2 << 1), 0x0000FF);
    }
    
    public void loadSettingsConfirmWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SETTINGS_TITLE);
        char []text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SETTINGS_TEXT);
        
        char [][]items = new char[2][];
        
        items[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SETTINGS_YES_ITEM);
        items[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SETTINGS_NO_ITEM);
        
        m_settingsConfirmWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, items, null, WINDOW_OFFSET_LEVEL_2, WINDOW_OFFSET_LEVEL_2, m_width - (WINDOW_OFFSET_LEVEL_2 << 1), m_height - (WINDOW_OFFSET_LEVEL_2 << 1), 0x0000FF);
    }
    
    public void loadResetConfirmWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_RESET_TITLE);
        char []text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_RESET_TEXT);
        
        char [][]items = new char[2][];
        
        items[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_RESET_YES_ITEM);
        items[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_RESET_NO_ITEM);
        
        
        m_resetConfirmWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, items, null, WINDOW_OFFSET_LEVEL_2, WINDOW_OFFSET_LEVEL_2, m_width - (WINDOW_OFFSET_LEVEL_2 << 1), m_height - (WINDOW_OFFSET_LEVEL_2 << 1), 0x0000FF);
    }
    
    public void loadHelpMessageWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_HELP_TITLE);
        char []leftOption = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEFT_TO_CLOSE);
        
        
        m_helpMessageWindow = new cydMessageWindowOutput(m_gm.m_font, title, leftOption, EMPTY_BUFFER, WINDOW_OFFSET_LEVEL_2, WINDOW_OFFSET_LEVEL_2, m_width - (WINDOW_OFFSET_LEVEL_2 << 1), m_height - (WINDOW_OFFSET_LEVEL_2 << 1), 0x0000FF, m_width, m_height);
        
        
        m_helpMessageWindow.addItem(m_gfxMnger.getImage(IMAGE_ID_HELP));
        
        int next = 0;
        
        char []text = null;
        while ((text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_HELP_TEXT_BASE + next)) != null) {
            m_helpMessageWindow.addItem(text);
            next++;
        }
    }
    
    private void loadAboutMessageWindow(byte[] vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_ABOUT_TITLE);
        char []leftOption = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEFT_TO_CLOSE);
        
        
        m_aboutMessageWindow = new cydMessageWindowOutput(m_gm.m_font, title, leftOption, EMPTY_BUFFER, WINDOW_OFFSET_LEVEL_2, WINDOW_OFFSET_LEVEL_2, m_width - (WINDOW_OFFSET_LEVEL_2 << 1), m_height - (WINDOW_OFFSET_LEVEL_2 << 1), 0x0000FF, m_width, m_height);
        
        int next = 0;
        
        m_aboutMessageWindow.addItem(m_gfxMnger.getImage(IMAGE_ID_ABOUT));
        
        char []text = null;
        while ((text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_ABOUT_TEXT_BASE + next)) != null) {
            m_aboutMessageWindow.addItem(text);
            next++;
        }
    }
    
    public void setup() {
        setBackground(0x000000);
        
        if (m_gm.m_musicOn) {
            m_gm.m_player.stopMusic();
            m_gm.m_player.closeMusic();
            m_gm.m_player.newMusic(getClass().getResourceAsStream("/solitude.mid"), cydMusicPlayer.DESIRED_MIME_TYPE);
            m_gm.m_player.startMusic();
        }
        
        byte []vmData = cydGameManager.getISToByteArray(getClass().getResourceAsStream("/mm_res.script"));
        m_gfxMnger = new cydGraphicsManager(IMAGE_TOTAL);
        
        loadImages(vmData);
        
        
        m_width = getWidth();
        m_height = getHeight();
        
        loadMainMenuWindow(vmData);
        loadQuitConfirmWindow(vmData);
        loadPracticeConfirmWindow(vmData);
        loadSettingsConfirmWindow(vmData);
        loadResetConfirmWindow(vmData);
        loadHelpMessageWindow(vmData);
        loadAboutMessageWindow(vmData);
        
        // more than signed short and needs to be broken up into sections
        m_bgMover = new cydPointInterpolator(2, new int[] {0, 0, m_width+10, 0}, new int[] {30000, 1}, 0, true);
    }
    
    public void stop() {
        if (m_gm.m_musicOn) {
            m_gm.m_player.stopMusic();
            m_gm.m_player.closeMusic();
        }
        
        m_gfxMnger.removeLoadedImage(-1);
    }
    
    public void draw() {
        int []points = m_bgMover.getPoints();
        
        m_graphics.drawImage(m_gfxMnger.getImage(IMAGE_ID_BG), points[0], points[1], 0);
        
        if (m_state != STATE_PREWINDOW)
            m_mainMenuWindow.draw(m_graphics);
        
        switch (m_state) {
            case STATE_QUIT_CONFIRM: {
                m_quitConfirmWindow.draw(m_graphics);
            }
            break;
            case STATE_PRACTICE_CONFIRM: {
                m_practiceConfirmWindow.draw(m_graphics);
            }
            break;
            case STATE_RESET_CONFIRM: {
                m_resetConfirmWindow.draw(m_graphics);
            }
            break;
            case STATE_HELP_MESSAGE: {
                m_helpMessageWindow.draw(m_graphics);
            }
            break;
            case STATE_ABOUT_MESSAGE: {
                m_aboutMessageWindow.draw(m_graphics);
            }
            break;
            case STATE_SETTINGS_CONFIRM: {
                m_settingsConfirmWindow.draw(m_graphics);
            }
            break;
        }
    }
    
    public boolean process(int timeElapsed) {
        m_bgMover.interpolate(timeElapsed);
        
        int keyStates = m_gm.m_keyInputSlowDowner.processKeyPress(getKeyStates(), timeElapsed);
        
        switch (m_state) {
            case cydMainMenuCanvas.STATE_PREWINDOW: {
                m_timeElapsed += timeElapsed;
                
                if (m_timeElapsed >= cydMainMenuCanvas.STATE_PREWINDOW_WAIT_TIME || m_timerAlreadyElapsed) {
                    m_timerAlreadyElapsed = true;
                    
                    m_state = cydMainMenuCanvas.STATE_MAIN_MENU;
                    m_timeElapsed = 0;
                }
            }
            break;
            case cydMainMenuCanvas.STATE_MAIN_MENU: {
                int index = m_mainMenuWindow.process(timeElapsed, keyStates);            
                
                switch (index) {
                    case cydMainMenuCanvas.SELECTION_MAIN_MENU_PLAY_ITEM: {
                        m_return = cydMainMenuCanvas.RETURN_PLAY;
                    }
                    return false;
                    case cydMainMenuCanvas.SELECTION_MAIN_MENU_RESET_ITEM: {
                        m_state = cydMainMenuCanvas.STATE_RESET_CONFIRM;
                    }
                    break;
                    case cydMainMenuCanvas.SELECTION_MAIN_MENU_PRACTICE_ITEM: {
                        m_practiceConfirmWindow.m_textOutput.renew();
                        m_state = cydMainMenuCanvas.STATE_PRACTICE_CONFIRM;
                    }
                    break;
                    case cydMainMenuCanvas.SELECTION_MAIN_MENU_CUSTOM_MAPS_ITEM: {
                        m_return = cydMainMenuCanvas.RETURN_CUSTOM_MAPS;
                    }
                    return false;
                    case cydMainMenuCanvas.SELECTION_MAIN_MENU_SETTINGS_ITEM: {
                        m_settingsConfirmWindow.m_textOutput.renew();
                        m_state = cydMainMenuCanvas.STATE_SETTINGS_CONFIRM;
                    }
                    break;
                    case cydMainMenuCanvas.SELECTION_MAIN_MENU_HELP_ITEM: {
                        m_state = cydMainMenuCanvas.STATE_HELP_MESSAGE;
                    }
                    break;
                    case cydMainMenuCanvas.SELECTION_MAIN_MENU_ABOUT_ITEM: {
                        m_state = cydMainMenuCanvas.STATE_ABOUT_MESSAGE;
                    }
                    break;
                    case cydMainMenuCanvas.SELECTION_MAIN_MENU_CREDITS_ITEM: {
                        m_return = cydMainMenuCanvas.RETURN_CREDITS;
                    }
                    return false;
                    case cydMainMenuCanvas.SELECTION_MAIN_MENU_EXIT_ITEM: {
                        m_quitConfirmWindow.m_textOutput.renew();
                        m_state = cydMainMenuCanvas.STATE_QUIT_CONFIRM;
                    }
                    break;
                }
            }
            break;
            case cydMainMenuCanvas.STATE_QUIT_CONFIRM: {
                int index = m_quitConfirmWindow.process(timeElapsed, keyStates);
                
                switch (index) {
                    case cydMainMenuCanvas.SELECTION_QUIT_YES_ITEM:
                        m_return = cydMainMenuCanvas.RETURN_QUIT;
                        return false;
                    case cydMainMenuCanvas.SELECTION_QUIT_NO_ITEM:
                        m_state = cydMainMenuCanvas.STATE_MAIN_MENU;
                        break;
                }
            }
            break;
            case cydMainMenuCanvas.STATE_PRACTICE_CONFIRM: {
                int index = m_practiceConfirmWindow.process(timeElapsed, keyStates);
                
                switch (index) {
                    case cydMainMenuCanvas.SELECTION_PRACTICE_CLIMB_ITEM:
                        m_return = cydMainMenuCanvas.RETURN_PRACTICE_CLIMB;
                        return false;
                    case cydMainMenuCanvas.SELECTION_PRACTICE_DROP_ITEM:
                        m_return = cydMainMenuCanvas.RETURN_PRACTICE_DROP;
                        return false;
                    case cydMainMenuCanvas.SELECTION_PRACTICE_FREE_ITEM:
                        m_return = cydMainMenuCanvas.RETURN_PRACTICE_FREE;
                        return false;
                    case cydMainMenuCanvas.SELECTION_PRACTICE_BACK_ITEM:
                        m_state = cydMainMenuCanvas.STATE_MAIN_MENU;
                        break;
                }
            }
            break;
            case cydMainMenuCanvas.STATE_RESET_CONFIRM: {
                int index = m_resetConfirmWindow.process(timeElapsed, keyStates);
                
                switch (index) {
                    case cydMainMenuCanvas.SELECTION_RESET_YES_ITEM: {
                        new cydMainStorageSystem().clearAllMaxModes();
                        m_state = cydMainMenuCanvas.STATE_MAIN_MENU;
                    }
                    break;
                    case cydMainMenuCanvas.SELECTION_RESET_NO_ITEM: {
                        m_state = cydMainMenuCanvas.STATE_MAIN_MENU;
                    }
                    break;
                }
            }
            break;
            case cydMainMenuCanvas.STATE_HELP_MESSAGE: {
                if (m_helpMessageWindow.process(timeElapsed, keyStates) == 1)
                    m_state = cydMainMenuCanvas.STATE_MAIN_MENU;
            }
            break;
            case cydMainMenuCanvas.STATE_ABOUT_MESSAGE: {
                if (m_aboutMessageWindow.process(timeElapsed, keyStates) == 1)
                    m_state = cydMainMenuCanvas.STATE_MAIN_MENU;
            }
            break;
            case cydMainMenuCanvas.STATE_SETTINGS_CONFIRM: {
                int index = m_settingsConfirmWindow.process(timeElapsed, keyStates);
                
                switch (index) {
                    case cydMainMenuCanvas.SELECTION_SETTINGS_YES_ITEM:
                        m_return = cydMainMenuCanvas.RETURN_RESET;
                        return false;
                    case cydMainMenuCanvas.SELECTION_SETTINGS_NO_ITEM:
                        m_state = cydMainMenuCanvas.STATE_MAIN_MENU;
                        break;
                }
            }
            break;
        }
        
        return true;
    }
    
    public void loadingScreen() {
        m_graphics.setColor(0x000000);
        m_graphics.fillRect(0, 0, getWidth(), getHeight());
        
        int x = getWidth()/2 - m_gm.m_font.getWidth(LOADING_TEXT)/2;
        int y = getHeight()/2 - m_gm.m_font.getHeight()/2;
        
        m_gm.m_font.setForegroundOutlineColor(0xFFFFFF);
        m_gm.m_font.drawString(m_graphics, LOADING_TEXT, x, y, -1);
    }
}
