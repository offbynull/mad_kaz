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
import framework.cydPointInterpolator;
import framework.cydSimpleAskWindowOutput;
import imagemanip.cydImageResize;
import java.io.DataInputStream;
import java.util.Random;
import vm.cydByteArrayInputStream;

public class cydSetupCanvas extends cydGameCanvas {
    public cydGameManager m_gm;
    
    public cydSimpleAskWindowOutput m_windowOutput;
    
    public int m_width;
    public int m_height;
    
    public static final int README_SCREEN = 0;
    public static final int MUSIC_SCREEN = 1;
    public static final int SCALING_SCREEN = 2;
    public static final int SAVE_SCREEN = 3;
    public static final int DONE_SCREEN = 4;
    
    

    public static final int INDEX_OK                             = 0;
    public static final int INDEX_YES                            = 1;
    public static final int INDEX_NO                             = 2;
    public static final int INDEX_BACK                           = 3;
    
    public static final int INDEX_SETUP_README_TITLE             = 100;
    public static final int INDEX_SETUP_README_TEXT              = 101;
    
    public static final int INDEX_SETUP_MUSIC_TITLE              = 200;
    public static final int INDEX_SETUP_MUSIC_TEXT               = 201;
    
    public static final int INDEX_SETUP_SCALING_TITLE            = 300;
    public static final int INDEX_SETUP_SCALING_TEXT             = 301;
    
    public static final int INDEX_SETUP_SAVE_TITLE               = 400;
    public static final int INDEX_SETUP_SAVE_TEXT                = 401;
    
    
    

    public char []m_readmeTitle = null;
    public char []m_readmeText = null;
    
    public char []m_musicTitle = null;
    public char []m_musicText = null;
    
    public char []m_scalingTitle = null;
    public char []m_scalingText = null;
    
    public char []m_saveTitle = null;
    public char []m_saveText = null;
    
    public char [][] m_okOptions = null;
    public char [][] m_yesNoOptions = null;
    public char [][] m_yesNoBackOptions = null;
    
    public static final int SELECTION_YES = 0;
    public static final int SELECTION_NO = 1;
    public static final int SELECTION_BACK = 2;
    
    public boolean m_goThru;
    
    public int m_translateX;
    public int m_translateY;
    
    public cydPointInterpolator m_downInterpolator;
    public cydPointInterpolator m_upInterpolator;
    public cydPointInterpolator m_leftInterpolator;
    public cydPointInterpolator m_rightInterpolator;
    
    public Random m_random;
    
    int m_currentTransition;
    int m_currentScreen = -1;
    
    public static final int TRANSITION_DOWN = 0;
    public static final int TRANSITION_UP = 1;
    public static final int TRANSITION_LEFT = 2;
    public static final int TRANSITION_RIGHT = 3;
    public static final int TRANSITION_DONE = 4;
    
    public cydMainStorageSystem m_storageSystem;
    
    public cydSetupCanvas(cydGameManager gm, boolean forceReset) {
        super(gm.m_font);
        
        m_gm = gm;
        
        m_storageSystem = new cydMainStorageSystem();
        
        if (forceReset) {
            m_storageSystem.setSettingsSaved(false);
            
            m_goThru = true;
        } else if (!m_storageSystem.isSettingsSaved()) {
            m_goThru = true;
        } else if (m_storageSystem.isSettingsSaved()) {
            m_gm.m_musicOn = m_storageSystem.isMusicOn();
            m_gm.m_scalingType = m_storageSystem.isBilinearScalingOn() ? cydImageResize.BILINEAR : cydImageResize.NORMAL;
        }
    }
    
    public void loadText() {
        byte []vmData = cydGameManager.getISToByteArray(getClass().getResourceAsStream("/setup_res.script"));
        
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        m_readmeTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SETUP_README_TITLE);
        m_readmeText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SETUP_README_TEXT);

        m_musicTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SETUP_MUSIC_TITLE);
        m_musicText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SETUP_MUSIC_TEXT);

        m_scalingTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SETUP_SCALING_TITLE);
        m_scalingText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SETUP_SCALING_TEXT);

        m_saveTitle = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SETUP_SAVE_TITLE);
        m_saveText = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SETUP_SAVE_TEXT);

        m_okOptions = new char[1][];
        m_okOptions[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_OK);
        
        m_yesNoOptions = new char[2][];
        m_yesNoOptions[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_YES);
        m_yesNoOptions[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_NO);
        
        m_yesNoBackOptions = new char[3][];
        m_yesNoBackOptions[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_YES);
        m_yesNoBackOptions[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_NO);
        m_yesNoBackOptions[2] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_BACK);
        
    }
    
    public void setup() {
        m_width = getWidth();
        m_height = getHeight();
        
        loadText();
        
        m_windowOutput =  new cydSimpleAskWindowOutput(m_gm.m_font, null, null, null, null, 10, 10, m_width - 20, m_height - 20, 0x0000FF);
        
        int []transitionTime = new int[] { 400, 400 };
        
        m_downInterpolator = new cydPointInterpolator(2, new int [] {0, -m_height, 0, 0}, transitionTime, 0, true);
        m_upInterpolator = new cydPointInterpolator(2, new int [] {0, m_height, 0, 0}, transitionTime, 0, true);
        m_leftInterpolator = new cydPointInterpolator(2, new int [] {-m_width, 0, 0, 0}, transitionTime, 0, true);
        m_rightInterpolator = new cydPointInterpolator(2, new int [] {m_width, 0, 0, 0}, transitionTime, 0, true);
        
        m_random = new Random();
        
        m_currentTransition = m_random.nextInt()%4;
        
        if (m_currentTransition < 0)
            m_currentTransition = -m_currentTransition;
        
        switchMode(README_SCREEN);
    }
    
    public boolean process(int timeElapsed) {
        if (!m_goThru)
            return false;
        
        cydPointInterpolator interpolator = null;
        int []points = null;
        
        switch (m_currentTransition) {
            case TRANSITION_DOWN: {
                interpolator = m_downInterpolator;
            }
            break;
            case TRANSITION_UP: {
                interpolator = m_upInterpolator;
            }
            break;
            case TRANSITION_LEFT: {
                interpolator = m_leftInterpolator;
            }
            break;
            case TRANSITION_RIGHT: {
                interpolator = m_rightInterpolator;
            }
            break;
            case TRANSITION_DONE: {
                m_translateX = 0;
                m_translateY = 0;
                
                if (m_currentScreen == DONE_SCREEN)
                    return false;
                
                int keyStates = getKeyStates();
                
                keyStates = m_gm.m_keyInputSlowDowner.processKeyPress(keyStates, timeElapsed);
                
                int res = m_windowOutput.process(timeElapsed, keyStates);
                
                if (res != -1 && res != SELECTION_BACK) {
                    applySetting(res);
                    
                    // check if yes or no was selected and set proper settings here
                    switchMode(m_currentScreen+1);
                } else if (res == SELECTION_BACK) {
                    if (m_currentScreen != MUSIC_SCREEN)
                        switchMode(m_currentScreen-1);
                }
            }
            return true;
        }
        
        interpolator.interpolate(timeElapsed);
        points = interpolator.getPoints();
        
        m_translateX = points[0];
        m_translateY = points[1];
        
        if (interpolator.isDone()) {
            interpolator.restart();
            m_currentTransition = TRANSITION_DONE;
        }
        
        return true;
    }
    
    public void draw() {
        m_graphics.setColor(0xFFFFFF);
        m_graphics.fillRect(0, 0, m_width, m_height);
        
        m_graphics.translate(m_translateX, m_translateY);
        
        m_windowOutput.draw(m_graphics);
        
        m_graphics.translate(-m_translateX, -m_translateY);
    }
    
    public void applySetting(int res) {
        switch (m_currentScreen) {
            case MUSIC_SCREEN: {
                m_gm.m_musicOn = res == SELECTION_YES;
            }
            break;
            case SCALING_SCREEN: {
                m_gm.m_scalingType = (res == SELECTION_YES ? cydImageResize.BILINEAR : cydImageResize.NORMAL);
            }
            break;
            case SAVE_SCREEN: {
                if (res == SELECTION_YES) {
                    m_storageSystem.setSettingsSaved(true);
                    m_storageSystem.setMusicOn(m_gm.m_musicOn);
                    m_storageSystem.setBilinearScalingOn(m_gm.m_scalingType == cydImageResize.BILINEAR ? true : false);
                }
            }
            break;
        }
    }
    
    public void switchMode(int mode) {
        switch (mode) {
            case README_SCREEN: {
                m_windowOutput.reset(null, m_readmeTitle, m_readmeText, m_okOptions, null, -1, -1, -1, -1, -1);
                m_windowOutput.m_textOutput.renew();
            }
            break;
            case MUSIC_SCREEN: {
                m_windowOutput.reset(null, m_musicTitle, m_musicText, m_yesNoOptions, null, -1, -1, -1, -1, -1);
                m_windowOutput.m_textOutput.renew();
            }
            break;
            case SCALING_SCREEN: {
                m_windowOutput.reset(null, m_scalingTitle, m_scalingText, m_yesNoBackOptions, null, -1, -1, -1, -1, -1);
                m_windowOutput.m_textOutput.renew();
            }
            break;
            case SAVE_SCREEN: {
                m_windowOutput.reset(null, m_saveTitle, m_saveText, m_yesNoBackOptions, null, -1, -1, -1, -1, -1);
                m_windowOutput.m_textOutput.renew();
            }
            break;
            case DONE_SCREEN: {
                m_currentScreen = mode;
            }
            return;
        }
        
        m_currentScreen = mode;
        m_currentTransition = m_random.nextInt()%4;
        
        if (m_currentTransition < 0)
            m_currentTransition = -m_currentTransition;
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
