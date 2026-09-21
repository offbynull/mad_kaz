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
import framework.cydSimpleMenuWindowOutput;
import java.io.DataInputStream;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import vm.cydByteArrayInputStream;
import vm.cydVMEngine;

public class cydLevelSelectCanvas extends cydGameCanvas {
    public cydGameManager m_gm;
    
    public static final char []EMPTY_BUFFER                     = new char[0];
    
    
    
    
    
    
    public static final int MODE_NORMAL_OPTION                                  = 0;
    public static final int MODE_BLACKOUT_OPTION                                = 1;
    public static final int MODE_INFARED_OPTION                                 = 2;
    public static final int MODE_FAST_FORWARD_OPTION                            = 3;
    public static final int MODE_TIME_WARP_OPTION                               = 4;
    public static final int MODE_SUPER_JUMP_OPTION                              = 5;
    public static final int MODE_ULTRA_SHAKE_OPTION                             = 6;
    public static final int MODE_PSYCHOTIC_OPTION                               = 7;
    
    
    
    
    
    
    
    
    
    
    public static final int INDEX_ZONE_COUNT                                    = 0;
    public static final int INDEX_SELECT_ZONE_IMAGE                             = 1;
    public static final int INDEX_SELECT_LEVEL_IMAGE                            = 2;
    
    public static final int INDEX_CHOOSE_ZONE_TITLE                             = 10;
    public static final int INDEX_CHOOSE_ZONE_HELP_OPTION                       = 11;
    public static final int INDEX_CHOOSE_ZONE_BACK_OPTION                       = 12;
    
    public static final int INDEX_ZONE_DETAILS_TITLE                            = 20;
    public static final int INDEX_ZONE_DETAILS_PLAY_OPTION                      = 21;
    public static final int INDEX_ZONE_DETAILS_BACK_OPTION                      = 22;
    
    public static final int INDEX_GAME_MODE_TITLE                               = 50;
    public static final int INDEX_GAME_MODE_NORMAL_OPTION                       = 51;
    public static final int INDEX_GAME_MODE_BLACKOUT_OPTION                     = 52;
    public static final int INDEX_GAME_MODE_INFARED_OPTION                      = 53;
    public static final int INDEX_GAME_MODE_FAST_FORWARD_OPTION                 = 54;
    public static final int INDEX_GAME_MODE_TIME_WARP_OPTION                    = 55;
    public static final int INDEX_GAME_MODE_SUPER_JUMP_OPTION                   = 56;
    public static final int INDEX_GAME_MODE_ULTRA_SHAKE_OPTION                  = 57;
    public static final int INDEX_GAME_MODE_PSYCHOTIC_OPTION                    = 58;
    
    public static final int INDEX_RESULTS_TITLE                                 = 70;
    public static final int INDEX_RESULTS_COMPLETE_TEXT                         = 71;
    public static final int INDEX_RESULTS_COMPLETE_UNLOCKED_ALL_TEXT            = 72;
    public static final int INDEX_RESULTS_FAIL_TEXT                             = 73;
    public static final int INDEX_RESULTS_OK_OPTION                             = 74;
    
    public static final int INDEX_LOCKED_MODE_TITLE                             = 80;
    public static final int INDEX_LOCKED_MODE_LOCKED_TEXT                       = 81;
    public static final int INDEX_LOCKED_MODE_OK_OPTION                         = 82;
    
    public static final int INDEX_CHOOSE_LEVEL_TITLE                            = 90;
    
    public static final int INDEX_LEFT_TO_CLOSE                                 = 16999;
    public static final int INDEX_HELP_TITLE                                    = 17000;
    public static final int INDEX_HELP_TEXT_BASE                                = 17001;
    
    
    
    
    
    
    public static final int SELECTION_ZONE_DETAILS_PLAY_OPTION                  = 0;
    public static final int SELECTION_ZONE_DETAILS_BACK_OPTION                  = 1;
    
    public static final int SELECTION_GAME_MODE_NORMAL_OPTION                   = 0;
    public static final int SELECTION_GAME_MODE_BLACKOUT_OPTION                 = 1;
    public static final int SELECTION_GAME_MODE_INFARED_OPTION                  = 2;
    public static final int SELECTION_GAME_MODE_FAST_FORWARD_OPTION             = 3;
    public static final int SELECTION_GAME_MODE_TIME_WARP_OPTION                = 4;
    public static final int SELECTION_GAME_MODE_SUPER_JUMP_OPTION               = 5;
    public static final int SELECTION_GAME_MODE_ULTRA_SHAKE_OPTION              = 6;
    public static final int SELECTION_GAME_MODE_PSYCHOTIC_OPTION                = 7;
    
    public static final int SELECTION_LOCKED_MODE_OK_OPTION                     = 0;
    
    public static final int SELECTION_RESULT_OK_OPTION                          = 0;
    
    
    
    
    
    public cydImageHeaderMenuWindowOutput m_zoneSelectWindow;
    public cydSimpleMenuWindowOutput m_gameModeWindow;
    public cydImageHeaderMenuWindowOutput m_levelsWindow;
    public cydSimpleAskWindowOutput m_lockedModeWindow;
    public cydSimpleAskWindowOutput m_zoneDetailsWindow;
    public cydSimpleAskWindowOutput m_zoneResultWindow;
    public cydMessageWindowOutput m_helpMessageWindow;
    
    
    public char []m_unlockedNewMode;
    public char []m_allModesUnlocked;
    public char []m_failedZone;
    
    
    public int m_zoneCount;
    public int m_selectedZone;
    public Image m_zoneTitleImage;
    public Image m_levelTitleImage;
    
    public Vector m_zoneDesc;
    
    public cydMainStorageSystem m_storageSystem;
    
    
    
    
    
    public static final int STATE_CHOOSE_ZONE                                   = 0;
    public static final int STATE_ZONE_DETAILS                                  = 1;
    public static final int STATE_GAME_MODE                                     = 2;
    public static final int STATE_START_LEVEL                                   = 3;
    public static final int STATE_LOCKED_MODE                                   = 4;
    public static final int STATE_DEV_LEVEL_SELECT                              = 5;
    public static final int STATE_ZONE_COMPLETE                                 = 6;
    public static final int STATE_HELP                                          = 7;
    
    
    
    public int m_state = STATE_CHOOSE_ZONE;
    
    public int m_gameResult;
    public int m_startLevel = -1;
    public int m_startZone = -1;
    public int m_startMode = -1;
    
    public boolean m_devMode = false;
    public int m_devDownCount;
    
    public static final int DEV_MODE_DOWN_COUNT = 7;
    public static final int MODE_UNLOCK_DOWN_COUNT = 10;
    
    public byte []m_vmData;
    
    
    public cydLevelSelectCanvas(cydGameManager gm, int startLevel, int startZone, int startMode, int gameResult) {
        super(gm.m_font);
        
        m_gm = gm;
        m_startLevel = startLevel;
        m_startZone = startZone;
        m_startMode = startMode;
        m_gameResult = gameResult;
    }
    
    public void setup() {
        setBackground(0x000000);
        
        
        m_storageSystem = new cydMainStorageSystem();
        
        
        byte []vmData = cydGameManager.getISToByteArray(getClass().getResourceAsStream("/ls_res.script"));
        
        loadImages(vmData);
        loadZoneWindow(vmData);
        loadZoneDetailsWindow(vmData);
        loadZoneDescs(vmData);
        loadGameModeWindow(vmData);
        loadLockedModeWindow(vmData);
        loadZoneResultsWindow(vmData);
        loadHelpMessageWindow(vmData);
        loadLevelSelectWindow(vmData);
        
        loadProperGameResultInfo(vmData);
        
        m_vmData = vmData;
    }
    
    public void loadHelpMessageWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_HELP_TITLE);
        char []leftOption = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LEFT_TO_CLOSE);
        
        
        m_helpMessageWindow = new cydMessageWindowOutput(m_gm.m_font, title, leftOption, EMPTY_BUFFER, 15, 15, m_width - 30, m_height - 30, 0x0000FF, m_width, m_height);
        
        int next = 0;
        
        char []text = null;
        while ((text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_HELP_TEXT_BASE + next)) != null) {
            m_helpMessageWindow.addItem(text);
            next++;
        }
    }
    
    public void loadProperGameResultInfo(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        switch (m_gameResult) {
            case cydGamePlayCanvas.RETURN_CODE_LEVEL_PASSED: {
                boolean nextLevelExists = cydVMEngine.getResourceOffset(vmData, (((m_startZone+1) * 100) + 2) + ((m_startLevel+1) * 2)) != -1;
                
                if (!nextLevelExists) {
                    if (m_startMode < 7) {
                        char []modeName = cydGameManager.readResourceString(vmDataIS, vmDataDIS, 51 + m_startMode + 1);
                        m_zoneResultWindow.reset(null, null, (new String(modeName) + new String(m_unlockedNewMode)).toCharArray(), null, null, -1, -1, -1, -1, -1);
                        
                        if (m_storageSystem.getMaxMode(m_startZone) <= m_startMode)
                            m_storageSystem.setMaxMode(m_startZone, m_startMode + 1);    
                    } else if (m_storageSystem.getMaxMode(m_startZone) == 7) {
                        m_zoneResultWindow.reset(null, null, m_allModesUnlocked, null, null, -1, -1, -1, -1, -1);
                        m_storageSystem.setMaxMode(m_startZone, m_startMode + 1);
                    } else {
                        m_zoneResultWindow.reset(null, null, m_allModesUnlocked, null, null, -1, -1, -1, -1, -1);
                    }
                    
                    m_state = STATE_ZONE_COMPLETE;
                } else {
                    m_startLevel++;
                    
                    char []levelDetails = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2) + 1);
                    char []levelTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2));
                    
                    m_zoneResultWindow.reset(null, levelTitle, levelDetails, null, null, -1, -1, -1, -1, -1);
                    
                    m_state = STATE_START_LEVEL;
                }
            }
            break;
            case cydGamePlayCanvas.RETURN_CODE_PREMATURE_EXIT:
            case cydGamePlayCanvas.RETURN_CODE_LEVEL_FAILED: {
                m_zoneResultWindow.reset(null, null, m_failedZone, null, null, -1, -1, -1, -1, -1);
                m_state = STATE_ZONE_COMPLETE;
            }
            break;
            default: {
                m_state = STATE_CHOOSE_ZONE;
            }
            break;
        }
    }
    
    public void loadImages(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        m_zoneTitleImage = cydGameManager.readResourceImage(vmDataIS, vmDataDIS, INDEX_SELECT_ZONE_IMAGE);
        m_levelTitleImage = cydGameManager.readResourceImage(vmDataIS, vmDataDIS, INDEX_SELECT_LEVEL_IMAGE);
    }
    
    public void loadZoneDescs(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        m_zoneDesc = new Vector();
        int counter = 0;
        
        while (true) {
            char []text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, ((counter + 1) * 100) + 1);
            
            if (text == null)
                break;
            
            m_zoneDesc.addElement(text);
            
            counter++;
        }
    }
    
    public void loadZoneWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        m_zoneCount = Integer.parseInt(new String(cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_ZONE_COUNT)));
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_CHOOSE_ZONE_TITLE);
        
        char [][]items = new char[m_zoneCount + 2][];
        
        
        items[m_zoneCount] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_CHOOSE_ZONE_HELP_OPTION);
        items[m_zoneCount + 1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_CHOOSE_ZONE_BACK_OPTION);
        
        for (int i = 0; i < m_zoneCount; i++)
            items[i] = ((i+1) + ". " + new String(cydGameManager.readResourceString(vmDataIS, vmDataDIS, (i+1) * 100))).toCharArray();
        
        m_zoneSelectWindow = new cydImageHeaderMenuWindowOutput(m_gm.m_font, title, null, items, null, m_zoneTitleImage, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }

    public void loadLevelSelectWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_CHOOSE_LEVEL_TITLE);
        
        char [][]items = new char[0][];
        
        m_levelsWindow = new cydImageHeaderMenuWindowOutput(m_gm.m_font, title, null, items, null, m_levelTitleImage, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
            
    public void loadGameModeWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        m_zoneCount = Integer.parseInt(new String(cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_ZONE_COUNT)));
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_GAME_MODE_TITLE);
        
        char [][]items = new char[8][];
        
        items[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_GAME_MODE_NORMAL_OPTION);
        items[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_GAME_MODE_BLACKOUT_OPTION);
        items[2] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_GAME_MODE_INFARED_OPTION);
        items[3] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_GAME_MODE_FAST_FORWARD_OPTION);
        items[4] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_GAME_MODE_TIME_WARP_OPTION);
        items[5] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_GAME_MODE_SUPER_JUMP_OPTION);
        items[6] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_GAME_MODE_ULTRA_SHAKE_OPTION);
        items[7] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_GAME_MODE_PSYCHOTIC_OPTION);
        
        m_gameModeWindow = new cydSimpleMenuWindowOutput(m_gm.m_font, title, null, items, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadZoneDetailsWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_ZONE_DETAILS_TITLE);
        char []text = EMPTY_BUFFER;
        
        char [][]items = new char[2][];
        
        items[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_ZONE_DETAILS_PLAY_OPTION);
        items[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_ZONE_DETAILS_BACK_OPTION);
        
        
        m_zoneDetailsWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, items, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadZoneResultsWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_RESULTS_TITLE);
        char []text = EMPTY_BUFFER;
        
        char [][]items = new char[1][];
        
        items[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_RESULTS_OK_OPTION);
        
        m_unlockedNewMode = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_RESULTS_COMPLETE_TEXT);
        m_allModesUnlocked = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_RESULTS_COMPLETE_UNLOCKED_ALL_TEXT);
        m_failedZone = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_RESULTS_FAIL_TEXT);
        
        
        m_zoneResultWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, items, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public void loadLockedModeWindow(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LOCKED_MODE_TITLE);
        char []text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LOCKED_MODE_LOCKED_TEXT);
        
        char [][]items = new char[1][];
        
        items[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_LOCKED_MODE_OK_OPTION);
        
        
        m_lockedModeWindow = new cydSimpleAskWindowOutput(m_gm.m_font, title, text, items, null, 15, 15, m_width - 30, m_height - 30, 0x0000FF);
    }
    
    public boolean process(int timeElapsed) {
        int keyStates = m_gm.m_keyInputSlowDowner.processKeyPress(getKeyStates(), timeElapsed);
        
        switch (m_state) {
            case cydLevelSelectCanvas.STATE_CHOOSE_ZONE: {
                int cheatKeyState = m_gm.m_keyInputUnrepeater.processKeyPress(getKeyStates());
                
                if (cheatKeyState == RIGHT_PRESSED) {
                    m_devDownCount++;
                    
                    if (m_devDownCount == DEV_MODE_DOWN_COUNT) {
                        m_devMode = true;
                        setBackground(0x00FF00);
                    } else if (m_devDownCount == MODE_UNLOCK_DOWN_COUNT) {
                        m_storageSystem.setAllMaxModes(8);
                        setBackground(0xFF0000);
                    }
                } else if (cheatKeyState == 0) {
                } else {
                    m_devDownCount = 0;
                }
                
                int res = m_zoneSelectWindow.process(timeElapsed, keyStates);
                
                if (res == m_zoneCount + 1) {
                    m_startLevel = -1;
                    m_startZone = -1;
                    m_startMode = -1;
                    
                    return false;
                } else if (res == m_zoneCount) {
                    m_state = cydLevelSelectCanvas.STATE_HELP;
                } else if (res != -1) {
                    m_selectedZone = res;
                    m_zoneDetailsWindow.reset(null, null, (char [])m_zoneDesc.elementAt(res), null, null, -1, -1, -1, -1, -1);
                    m_state = cydLevelSelectCanvas.STATE_ZONE_DETAILS;
                }
            }
            break;
            case cydLevelSelectCanvas.STATE_ZONE_DETAILS: {
                int res = m_zoneDetailsWindow.process(timeElapsed, keyStates);
                
                switch (res) {
                    case cydLevelSelectCanvas.SELECTION_ZONE_DETAILS_PLAY_OPTION: {
                        m_state = cydLevelSelectCanvas.STATE_GAME_MODE;
                        
                        int maxMode = m_storageSystem.getMaxMode(m_selectedZone);
                        
                        int []colors = new int[8];
                        
                        for (int i = maxMode + 1; i < colors.length; i++)
                            colors[i] = 0xFF0000;
                        
                        
                        m_gameModeWindow.reset(null, null, null, null, colors, -1, -1, -1, -1, -1);
                    }
                    break;
                    case cydLevelSelectCanvas.SELECTION_ZONE_DETAILS_BACK_OPTION: {
                        m_state = cydLevelSelectCanvas.STATE_CHOOSE_ZONE;
                    }
                    break;
                }
            }
            break;
            case cydLevelSelectCanvas.STATE_GAME_MODE: {
                int res = m_gameModeWindow.process(timeElapsed, keyStates);
                
                switch (res) {
                    case cydLevelSelectCanvas.SELECTION_GAME_MODE_NORMAL_OPTION: {
                        if (m_storageSystem.getMaxMode(m_selectedZone) < 0) {
                            m_state = cydLevelSelectCanvas.STATE_LOCKED_MODE;
                            m_lockedModeWindow.m_textOutput.renew();
                        } else {
                            m_startLevel = 0;
                            m_startZone = m_selectedZone;
                            m_startMode = 0;
                            
                            cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(m_vmData);
                            DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
                            
                            char []levelDetails = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2) + 1);
                            char []levelTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2));

                            m_zoneResultWindow.reset(null, levelTitle, levelDetails, null, null, -1, -1, -1, -1, -1);
                        }
                    }
                    break;
                    case cydLevelSelectCanvas.SELECTION_GAME_MODE_BLACKOUT_OPTION: {
                        if (m_storageSystem.getMaxMode(m_selectedZone) < 1) {
                            m_state = cydLevelSelectCanvas.STATE_LOCKED_MODE;
                            m_lockedModeWindow.m_textOutput.renew();
                        } else {
                            m_startLevel = 0;
                            m_startZone = m_selectedZone;
                            m_startMode = 1;
                            
                            cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(m_vmData);
                            DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
                            
                            char []levelDetails = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2) + 1);
                            char []levelTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2));

                            m_zoneResultWindow.reset(null, levelTitle, levelDetails, null, null, -1, -1, -1, -1, -1);
                        }
                    }
                    break;
                    case cydLevelSelectCanvas.SELECTION_GAME_MODE_INFARED_OPTION: {
                        if (m_storageSystem.getMaxMode(m_selectedZone) < 2) {
                            m_state = cydLevelSelectCanvas.STATE_LOCKED_MODE;
                            m_lockedModeWindow.m_textOutput.renew();
                        } else {
                            m_startLevel = 0;
                            m_startZone = m_selectedZone;
                            m_startMode = 2;
                            
                            cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(m_vmData);
                            DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
                            
                            char []levelDetails = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2) + 1);
                            char []levelTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2));

                            m_zoneResultWindow.reset(null, levelTitle, levelDetails, null, null, -1, -1, -1, -1, -1);
                        }
                    }
                    break;
                    case cydLevelSelectCanvas.SELECTION_GAME_MODE_FAST_FORWARD_OPTION: {
                        if (m_storageSystem.getMaxMode(m_selectedZone) < 3) {
                            m_state = cydLevelSelectCanvas.STATE_LOCKED_MODE;
                            m_lockedModeWindow.m_textOutput.renew();
                        } else {
                            m_startLevel = 0;
                            m_startZone = m_selectedZone;
                            m_startMode = 3;
                            
                            cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(m_vmData);
                            DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
                            
                            char []levelDetails = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2) + 1);
                            char []levelTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2));

                            m_zoneResultWindow.reset(null, levelTitle, levelDetails, null, null, -1, -1, -1, -1, -1);
                        }
                    }
                    break;
                    case cydLevelSelectCanvas.SELECTION_GAME_MODE_TIME_WARP_OPTION: {
                        if (m_storageSystem.getMaxMode(m_selectedZone) < 4) {
                            m_state = cydLevelSelectCanvas.STATE_LOCKED_MODE;
                            m_lockedModeWindow.m_textOutput.renew();
                        } else {
                            m_startLevel = 0;
                            m_startZone = m_selectedZone;
                            m_startMode = 4;
                            
                            cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(m_vmData);
                            DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
                            
                            char []levelDetails = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2) + 1);
                            char []levelTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2));

                            m_zoneResultWindow.reset(null, levelTitle, levelDetails, null, null, -1, -1, -1, -1, -1);
                        }
                    }
                    break;
                    case cydLevelSelectCanvas.SELECTION_GAME_MODE_SUPER_JUMP_OPTION: {
                        if (m_storageSystem.getMaxMode(m_selectedZone) < 5) {
                            m_state = cydLevelSelectCanvas.STATE_LOCKED_MODE;
                            m_lockedModeWindow.m_textOutput.renew();
                        } else {
                            m_startLevel = 0;
                            m_startZone = m_selectedZone;
                            m_startMode = 5;
                            
                            cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(m_vmData);
                            DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
                            
                            char []levelDetails = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2) + 1);
                            char []levelTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2));

                            m_zoneResultWindow.reset(null, levelTitle, levelDetails, null, null, -1, -1, -1, -1, -1);
                        }
                    }
                    break;
                    case cydLevelSelectCanvas.SELECTION_GAME_MODE_ULTRA_SHAKE_OPTION: {
                        if (m_storageSystem.getMaxMode(m_selectedZone) < 6) {
                            m_state = cydLevelSelectCanvas.STATE_LOCKED_MODE;
                            m_lockedModeWindow.m_textOutput.renew();
                        } else {
                            m_startLevel = 0;
                            m_startZone = m_selectedZone;
                            m_startMode = 6;
                            
                            cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(m_vmData);
                            DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
                            
                            char []levelDetails = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2) + 1);
                            char []levelTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2));

                            m_zoneResultWindow.reset(null, levelTitle, levelDetails, null, null, -1, -1, -1, -1, -1);
                        }
                    }
                    break;
                    case cydLevelSelectCanvas.SELECTION_GAME_MODE_PSYCHOTIC_OPTION: {
                        if (m_storageSystem.getMaxMode(m_selectedZone) < 7) {
                            m_state = cydLevelSelectCanvas.STATE_LOCKED_MODE;
                            m_lockedModeWindow.m_textOutput.renew();
                        } else {
                            m_startLevel = 0;
                            m_startZone = m_selectedZone;
                            m_startMode = 7;
                            
                            cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(m_vmData);
                            DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
                            
                            char []levelDetails = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2) + 1);
                            char []levelTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2));

                            m_zoneResultWindow.reset(null, levelTitle, levelDetails, null, null, -1, -1, -1, -1, -1);
                        }
                    }
                    break;
                }
                
                if (res != -1) {
                    if (!m_devMode) {
                        if (m_storageSystem.getMaxMode(m_startZone) - 1 >= m_startMode) {
                            loadLevelWindowItems();
                            m_state = cydLevelSelectCanvas.STATE_DEV_LEVEL_SELECT;
                        } else {
                            m_state = cydLevelSelectCanvas.STATE_START_LEVEL;
                        }
                    } else {
                        loadLevelWindowItems();

                        m_state = cydLevelSelectCanvas.STATE_DEV_LEVEL_SELECT;
                    }
                }
            }
            break;
            case cydLevelSelectCanvas.STATE_START_LEVEL: {
                int res = m_zoneResultWindow.process(timeElapsed, keyStates);
                
                switch (res) {
                    case cydLevelSelectCanvas.SELECTION_RESULT_OK_OPTION:
                        return false;
                }
            }
            break;
            case cydLevelSelectCanvas.STATE_LOCKED_MODE: {
                int res = m_lockedModeWindow.process(timeElapsed, keyStates);
                
                switch (res) {
                    case cydLevelSelectCanvas.SELECTION_LOCKED_MODE_OK_OPTION: {
                        m_state = cydLevelSelectCanvas.STATE_GAME_MODE;
                    }
                    break;
                }
            }
            break;
            case cydLevelSelectCanvas.STATE_DEV_LEVEL_SELECT: {
                int res = m_levelsWindow.process(timeElapsed, keyStates);
                
                if (res != -1) {
                    m_startLevel = res;
                    m_startZone = m_selectedZone;

                    cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(m_vmData);
                    DataInputStream vmDataDIS = new DataInputStream(vmDataIS);

                    char []levelDetails = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2) + 1);
                    char []levelTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, (((m_startZone + 1) * 100) + 2) + ((m_startLevel) * 2));

                    m_zoneResultWindow.reset(null, levelTitle, levelDetails, null, null, -1, -1, -1, -1, -1);

                    m_state = cydLevelSelectCanvas.STATE_START_LEVEL;
                }
            }
            break;
            case cydLevelSelectCanvas.STATE_ZONE_COMPLETE: {
                int res = m_zoneResultWindow.process(timeElapsed, keyStates);
                
                switch (res) {
                    case cydLevelSelectCanvas.SELECTION_RESULT_OK_OPTION: {
                        m_state = cydLevelSelectCanvas.STATE_CHOOSE_ZONE;
                    }
                    break;
                }
            }
            break;
            case cydLevelSelectCanvas.STATE_HELP: {
                int res = m_helpMessageWindow.process(timeElapsed, keyStates);
                
                if (res == 1)
                    m_state = cydLevelSelectCanvas.STATE_CHOOSE_ZONE;
            }
            break;
        }
        
        return true;
    }

    public void loadLevelWindowItems() {
        int levelCounter = 0;

        while (cydVMEngine.getResourceOffset(m_vmData, (((m_startZone+1) * 100) + 2) + (levelCounter * 2)) != -1)
            levelCounter++;

        char [][]items = new char[levelCounter][];

        for (int i = 0; i < levelCounter; i++) {
            items[i] = ("Level " + (i + 1)).toCharArray();
        }

        m_levelsWindow.reset(null, null, null, items, null, null, -1, -1, -1, -1, -1);
    }
    
    public void draw() {
        Graphics g = m_graphics;
        
        switch (m_state) {
            case cydLevelSelectCanvas.STATE_CHOOSE_ZONE: {
                m_zoneSelectWindow.draw(g);
            }
            break;
            case cydLevelSelectCanvas.STATE_ZONE_DETAILS: {
                m_zoneDetailsWindow.draw(g);
            }
            break;
            case cydLevelSelectCanvas.STATE_GAME_MODE: {
                m_gameModeWindow.draw(g);
            }
            break;
            case cydLevelSelectCanvas.STATE_START_LEVEL: {
                m_zoneResultWindow.draw(g);
            }
            break;
            case cydLevelSelectCanvas.STATE_LOCKED_MODE: {
                m_lockedModeWindow.draw(g);
            }
            break;
            case cydLevelSelectCanvas.STATE_DEV_LEVEL_SELECT: {
                m_levelsWindow.draw(g);
            }
            break;
            case cydLevelSelectCanvas.STATE_ZONE_COMPLETE: {
                m_zoneResultWindow.draw(g);
            }
            break;
            case cydLevelSelectCanvas.STATE_HELP: {
                m_helpMessageWindow.draw(g);
            }
            break;
        }
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
