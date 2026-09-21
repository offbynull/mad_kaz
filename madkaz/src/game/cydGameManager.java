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

import framework.cydFontLib;
import framework.cydIndexedCombinedReader;
import framework.cydKeyInputSlowDowner;
import framework.cydKeyInputUnrepeater;
import framework.cydMusicPlayer;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Random;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import vm.cydByteArrayInputStream;
import vm.cydVMEngine;

public class cydGameManager implements Runnable {
    public static final int RMS_CONFIG_INDEX_MUSIC_ON              = 0;
    public static final int RMS_CONFIG_INDEX_NORMAL_EXIT           = 1;
    public static final int RMS_CONFIG_INDEX_SCALING               = 2;
    public static final int RMS_CONFIG_LENGTH                      = 3;
    
    public static final String RMS_CONF_NAME                       = "conf";
    
    public static final int GAME_START                             = 0;
    public static final int GAME_SETUP                             = 1;
    public static final int GAME_TOGGLE_BACKLIGHT                  = 2;
    public static final int GAME_COMPANY_LOGO                      = 3;
    public static final int GAME_MAIN_MENU                         = 4;
    public static final int GAME_LEVEL_SELECT                      = 5;
    public static final int GAME_PRACTICE                          = 6;
    public static final int GAME_CUSTOM_MAPS                       = 7;
    public static final int GAME_NETWORK                           = 8;
    public static final int GAME_BROWSE                            = 9;
    public static final int GAME_PLAY_CUSTOM                       = 10;
    public static final int GAME_PLAY                              = 11;
    public static final int GAME_P2P                               = 12;
    public static final int GAME_FINISH_GAME_SCROLLING_CREDITS     = 13;
    public static final int GAME_EXIT_GAME                         = 14;
    
    public cydKeyInputSlowDowner m_keyInputSlowDowner = new cydKeyInputSlowDowner();
    public cydKeyInputUnrepeater m_keyInputUnrepeater = new cydKeyInputUnrepeater();
    
    public Thread m_thread;
    
    public cydMusicPlayer m_player = new cydMusicPlayer();
    public cydBacklightInvoker m_backlight = new cydBacklightInvoker(this);
    public cydFontLib m_font;
    
    public cydGame m_gameMidlet;
    
    public boolean m_forceReset = false;
    
    public boolean m_musicOn = true;
    public boolean m_saveSettings = false;
    public int m_scalingType;
    
    public static final int INTENDED_WIDTH = 176;
    public static final int INTENDED_HEIGHT = 205;
    
    public static final String MODE_KEY = "__mode";
    
    public cydGameManager(cydGame game) {
        m_gameMidlet = game;
        
        byte[] fontData = getISToByteArray(getClass().getResourceAsStream("/romanp.vfs"));
        m_font = new cydFontLib(fontData,0);
    }
    
    public boolean start() {
        if (m_thread != null)
            return false;
        
        m_thread = new Thread(this);
        m_thread.start();
        
        return true;
    }
    
    public static byte []getISToByteArray(InputStream is) {
        byte b[] = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        int len = -1;

        //System.out.println("datfree: " + Runtime.getRuntime().freeMemory());
        
        System.gc();
        Runtime.getRuntime().gc();
        
        try {
            while ((len = is.read(b)) != -1) {
                baos.write(b, 0, len);
            }
        } catch (Throwable t) { }
        
        System.gc();
        Runtime.getRuntime().gc();
        
        //System.out.println("datfree: " + Runtime.getRuntime().freeMemory());
        
        return baos.toByteArray();
    }
    
    public void run() {
        int gameState = -10000;
        int gameReturnCode = cydGamePlayCanvas.RETURN_CODE_NONE;
        int startZone = -1;
        int startLevel = -1;
        int startMode = -1;
        
        byte []vmLevelData = null;
        
        try {
            gameState = cydGameManager.GAME_START;
            
            while (true) {
                switch (gameState) {
                    case cydGameManager.GAME_START: {
                        gameState = cydGameManager.GAME_SETUP;
                    }
                    break;
                    case cydGameManager.GAME_SETUP: {
                        cydSetupCanvas canvas = new cydSetupCanvas(this, m_forceReset);
                        Display.getDisplay(m_gameMidlet).setCurrent(canvas);
                        canvas.startWithoutNewThread();
                        canvas = null;
                        System.gc();
                        
                        gameState = cydGameManager.GAME_COMPANY_LOGO;
                        m_forceReset = false;
                    }
                    break;
                    case cydGameManager.GAME_COMPANY_LOGO: {
                        cydCompanyLogoCanvas canvas = new cydCompanyLogoCanvas(m_font);
                        Display.getDisplay(m_gameMidlet).setCurrent(canvas);
                        canvas.startWithoutNewThread();
                        canvas = null;
                        System.gc();
                        
                        gameState = cydGameManager.GAME_MAIN_MENU;
                    }
                    break;
                    case cydGameManager.GAME_MAIN_MENU: {
                        cydMainMenuCanvas canvas = new cydMainMenuCanvas(this);
                        Display.getDisplay(m_gameMidlet).setCurrent(canvas);
                        canvas.startWithoutNewThread();
                        
                        switch (canvas.m_return) {
                            case cydMainMenuCanvas.RETURN_QUIT:
                                gameState = cydGameManager.GAME_EXIT_GAME;
                                break;
                            case cydMainMenuCanvas.RETURN_PRACTICE_CLIMB:
                                vmLevelData = getISToByteArray(getClass().getResourceAsStream("/prac_climb.script"));
                                gameState = cydGameManager.GAME_PRACTICE;
                                break;
                            case cydMainMenuCanvas.RETURN_PRACTICE_DROP:
                                vmLevelData = getISToByteArray(getClass().getResourceAsStream("/prac_drop.script"));
                                gameState = cydGameManager.GAME_PRACTICE;
                                break;
                            case cydMainMenuCanvas.RETURN_PRACTICE_FREE:
                                vmLevelData = getISToByteArray(getClass().getResourceAsStream("/prac_free.script"));
                                gameState = cydGameManager.GAME_PRACTICE;
                                break;
                            case cydMainMenuCanvas.RETURN_PLAY:
                                gameState = cydGameManager.GAME_LEVEL_SELECT;
                                
                                gameReturnCode = cydGamePlayCanvas.RETURN_CODE_NONE;
                                startZone = -1;
                                startLevel = -1;
                                startMode = -1;
                                break;
                            case cydMainMenuCanvas.RETURN_RESET:
                                m_gameMidlet.destroyApp(true);      // stopping aux threads like musci and backlight
                                
                                m_forceReset = true;
                                gameState = cydGameManager.GAME_SETUP;
                                break;
                            case cydMainMenuCanvas.RETURN_CUSTOM_MAPS:
                                gameState = cydGameManager.GAME_CUSTOM_MAPS;
                                break;
                            case cydMainMenuCanvas.RETURN_CREDITS:
                                gameState = cydGameManager.GAME_FINISH_GAME_SCROLLING_CREDITS;
                                break;
                        }
                        
                        canvas = null;
                        System.gc();
                    }
                    break;
                    case cydGameManager.GAME_LEVEL_SELECT: {
                        cydLevelSelectCanvas canvas = new cydLevelSelectCanvas(this, startLevel, startZone, startMode, gameReturnCode);
                        Display.getDisplay(m_gameMidlet).setCurrent(canvas);
                        canvas.startWithoutNewThread();
                        
                        
                        
                        startZone = canvas.m_startZone;
                        startLevel = canvas.m_startLevel;
                        startMode = canvas.m_startMode;
                        
                        if (startZone == -1 && startLevel == -1 && startMode == -1)
                            gameState = cydGameManager.GAME_MAIN_MENU;
                        else
                            gameState = cydGameManager.GAME_PLAY;
                        
                        
                        canvas = null;
                        System.gc();
                    }
                    break;
                    case cydGameManager.GAME_PRACTICE: {
                        cydGamePlayCanvas canvas = new cydGamePlayCanvas(this, vmLevelData, 0, new Hashtable());
                        Display.getDisplay(m_gameMidlet).setCurrent(canvas);
                        canvas.startWithoutNewThread();
                        canvas = null;
                        System.gc();
                        
                        gameState = cydGameManager.GAME_MAIN_MENU;
                    }
                    break;
                    case cydGameManager.GAME_CUSTOM_MAPS: {
                        cydCustomMapCanvas canvas = new cydCustomMapCanvas(this);
                        Display.getDisplay(m_gameMidlet).setCurrent(canvas);
                        canvas.startWithoutNewThread();
                        
                        switch (canvas.m_return) {
                            case cydCustomMapCanvas.RETURN_BACK:
                                gameState = cydGameManager.GAME_MAIN_MENU;
                                break;
                            case cydCustomMapCanvas.RETURN_ONLINE:
                                gameState = cydGameManager.GAME_NETWORK;
                                break;
                            case cydCustomMapCanvas.RETURN_P2P:
                                gameState = cydGameManager.GAME_P2P;
                                break;
                            case cydCustomMapCanvas.RETURN_BROWSE:
                                gameState = cydGameManager.GAME_BROWSE;
                                break;
                        }
                        
                        canvas = null;
                        System.gc();
                    }
                    break;
                    case cydGameManager.GAME_NETWORK: {
                        cydNetworkCanvas canvas = new cydNetworkCanvas(this);
                        Display.getDisplay(m_gameMidlet).setCurrent(canvas);
                        canvas.startWithoutNewThread();
                        canvas = null;
                        System.gc();
                        
                        gameState = cydGameManager.GAME_CUSTOM_MAPS;
                    }
                    break;
                    case cydGameManager.GAME_BROWSE: {
                        cydBrowseCanvas canvas = new cydBrowseCanvas(this);
                        Display.getDisplay(m_gameMidlet).setCurrent(canvas);
                        canvas.startWithoutNewThread();
                        
                        switch (canvas.m_return) {
                            case cydBrowseCanvas.RETURN_BACK:
                                gameState = cydGameManager.GAME_CUSTOM_MAPS;
                                break;
                            case cydBrowseCanvas.RETURN_PLAY:
                                vmLevelData = canvas.m_vmData;
                                gameState = cydGameManager.GAME_PLAY_CUSTOM;
                                break;
                        }
                        
                        canvas = null;
                        System.gc();
                    }
                    break;
                    case cydGameManager.GAME_PLAY_CUSTOM: {
                        cydGamePlayCanvas canvas = new cydGamePlayCanvas(this, vmLevelData, 0, new Hashtable());
                        Display.getDisplay(m_gameMidlet).setCurrent(canvas);
                        canvas.startWithoutNewThread();
                        canvas = null;
                        System.gc();
                        
                        gameState = cydGameManager.GAME_CUSTOM_MAPS;
                    }
                    break;
                    case cydGameManager.GAME_PLAY: {
                        int enviroSetting = 0;
                        Hashtable vmTable = new Hashtable();
                        
                        // combining levels from same zones together saves about 30kb, combining them all together saves more but might cause OOM exception
                        vmLevelData = new cydIndexedCombinedReader(getISToByteArray(getClass().getResourceAsStream("/levels" + startZone + ".dat"))).get(0, startLevel);
                        
                        switch(startMode) {
                            case cydLevelSelectCanvas.MODE_BLACKOUT_OPTION: {
                                enviroSetting = cydGamePlayCanvas.NIGHT_SCOPE;
                            }
                            break;
                            case cydLevelSelectCanvas.MODE_INFARED_OPTION: {
                                int r = new Random().nextInt();
                                
                                if (r < 0)
                                    r = -r;
                                
                                r %= 2;
                                
                                enviroSetting = (r == 0 ? cydGamePlayCanvas.RED_SCOPE : cydGamePlayCanvas.GREEN_SCOPE);
                            }
                            break;
                            case cydLevelSelectCanvas.MODE_TIME_WARP_OPTION: {
                                enviroSetting = cydGamePlayCanvas.BW_SCOPE;
                            }
                            break;
                            case cydLevelSelectCanvas.MODE_PSYCHOTIC_OPTION: {
                                enviroSetting = cydGamePlayCanvas.INVERT_SCOPE;
                            }
                            break;
                        }
                        
                        vmTable.put(cydGameManager.MODE_KEY, new int [] { startMode });
                        
                        cydGamePlayCanvas canvas = new cydGamePlayCanvas(this, vmLevelData, enviroSetting, vmTable);
                        Display.getDisplay(m_gameMidlet).setCurrent(canvas);
                        canvas.startWithoutNewThread();
                        
                        gameReturnCode = canvas.m_returnCode;
                        
                        canvas = null;
                        System.gc();
                        
                        gameState = cydGameManager.GAME_LEVEL_SELECT;
                    }
                    break;
                    case cydGameManager.GAME_P2P: {
                        cydP2PCanvas canvas = new cydP2PCanvas(this);
                        Display.getDisplay(m_gameMidlet).setCurrent(canvas);
                        canvas.startWithoutNewThread();
                        canvas = null;
                        System.gc();
                        
                        gameState = cydGameManager.GAME_CUSTOM_MAPS;
                    }
                    break;
                    case cydGameManager.GAME_FINISH_GAME_SCROLLING_CREDITS: {
                        cydCreditScrollerCanvas canvas = new cydCreditScrollerCanvas(this);
                        Display.getDisplay(m_gameMidlet).setCurrent(canvas);
                        canvas.startWithoutNewThread();
                        canvas = null;
                        System.gc();
                        
                        gameState = cydGameManager.GAME_MAIN_MENU;
                    }
                    break;
                    case cydGameManager.GAME_EXIT_GAME: {
                        m_gameMidlet.destroyApp(true);
                        m_gameMidlet.notifyDestroyed();
                    }
                    return;
                }
            }
        } catch (Throwable t) {
            System.gc();
            
            t.printStackTrace();
            
            m_backlight.stop();
            m_player.stopMusic();
            m_player.closeMusic();
            
            cydErrorCanvas canvas = new cydErrorCanvas(this, m_font, t.getClass().getName() + " (" +t.getMessage() + ") <<<" + gameState + ">>>");
            Display.getDisplay(m_gameMidlet).setCurrent(canvas);
            
            canvas.startWithoutNewThread();
            canvas = null;
            System.gc();
            
            m_gameMidlet.destroyApp(true);
            m_gameMidlet.notifyDestroyed();
        }
    }
    
    public static int convertRatio(int fp1616Ratio, int value) {
        return (int)(((long)fp1616Ratio * (long)(value << 16)) >> 32);
    }
    
    public static Image generateHeaderBar(int width, int height, cydFontLib font) {
        try {
            Image headerImage = Image.createImage(width, height);

            Graphics g = headerImage.getGraphics();

            g.setColor(0x000000);
            g.fillRect(0,0,width,height);
            
            char []pwrString = new char[] {'P', 'W', 'R'};
            
            font.setForegroundOutlineColor(0xFFFFFF);
            font.drawString(g, pwrString, 1, 2, -1);
            
            g.setColor(0xFFFFFF);
            g.drawRect(25, 1, 52, 7);
            
            System.gc();

            return Image.createImage(headerImage);
        } catch (Throwable t) { throw new RuntimeException(t.getClass().getName()); }
    }
    
    public static char []readResourceString(cydByteArrayInputStream is, DataInputStream dis, int id) {
        int offset = cydVMEngine.getResourceOffset(is.getBuffer(), id);
        
        if (offset == -1)
            return null;
        
        offset += 4;
        
        char []ret = null;
        is.setPosition(offset);
        
        try {
            ret = dis.readUTF().toCharArray();
        } catch (Throwable t) {
            ret = new char[0];
        }
        
        return ret;
    }
    
    public static Image readResourceImage(cydByteArrayInputStream is, DataInputStream dis, int id) {
        int offset = cydVMEngine.getResourceOffset(is.getBuffer(), id);
        
        if (offset == -1)
            return null;
        
        Image ret = null;
        is.setPosition(offset);
        
        try {
            int size = dis.readInt();
            offset += 4;
        
            ret = Image.createImage(is.getBuffer(), offset, size);
        } catch (Throwable t) { throw new RuntimeException(t.getClass().getName()); }
        
        return ret;
    }
}
