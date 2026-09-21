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

package framework;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.media.Player;

public abstract class cydGameCanvas extends GameCanvas implements Runnable {
    public static final char []LOADING_TEXT = "LOADING! PLEASE WAIT...".toCharArray();
    
    public Graphics m_graphics;
    public Thread m_thread;
    public boolean m_paused;
    public Player m_noise;
    
    public cydFontLib m_font;
    public int m_background = -1;
    
    public int m_slowDownFactor;
    public int m_whiteOutColor                                  = -1;
    
    public int m_width;
    public int m_height;
    
    public cydGameCanvas(cydFontLib font) {
        super(true);
        
        m_graphics = getGraphics();
        m_font = font;
        
        if (m_font != null) {
            m_font.setForegroundFillColor(0xFFFFFF);
            m_font.setForegroundOutlineColor(0xFFFFFF);
        }
        
        setFullScreenMode(true);
        
        Thread.yield();
    }
    
    public cydGameCanvas(cydFontLib font, boolean keySupress) {
        super(keySupress);
        
        m_graphics = getGraphics();
        m_font = font;
        
        if (m_font != null) {
            m_font.setForegroundFillColor(0xFFFFFF);
            m_font.setForegroundOutlineColor(0xFFFFFF);
        }
        
        setFullScreenMode(true);
        
        Thread.yield();
    }
    
    public void setBackground(int bg) {
        m_background = bg;
    }
    
    public boolean start() {
        if (m_thread != null)
            return false;
        
        m_thread = new Thread(this);
        m_thread.start();
        
        return true;
    }
    
    public void startWithoutNewThread() {
        run();
    }
    
    public void stop() {
    }
    
    public void pause() {
        m_paused = true;
    }
    
    public void unpause() {
        m_paused = false;
    }
    
    public void loadingScreen() {
    }
    
    public void doneLoading() {
    }
    
    public void run() {
        Thread.yield();
        
        m_width = getWidth();
        m_height = getHeight();
        
        if (m_width != 176)
            throw new RuntimeException("phone width > 176");
        
        loadingScreen();
        flushGraphics();
        setup();
        doneLoading();
        
        long lastTime = System.currentTimeMillis();
        long currentTime = 0L;
        int timeElapsed = 0;
        
        int width = m_width;
        int height = m_height;
        
        while (true) {
            currentTime = System.currentTimeMillis();
            timeElapsed = (int)(currentTime - lastTime) >> m_slowDownFactor;
            
            if (timeElapsed == 0)
                continue;
            
            // process
            if (!m_paused) {
                if (!process(timeElapsed)) {
                    stop();
                    System.gc();
                    Runtime.getRuntime().gc();
                    return;
                }
            }
            
            
            
            
            // draw background
            if (m_whiteOutColor != -1) {
                m_graphics.setColor(m_whiteOutColor);
                m_graphics.fillRect(0, 0, m_width, m_height);
            }  else {
                if (m_background != -1) {
                    m_graphics.setColor(m_background);
                    m_graphics.fillRect(0, 0, width, height);
                }
                
                
                
                // draw scene
                draw();
            }
            
            
            
            
            // flush
            flushGraphics();
            
            
            
            // paused
            if (m_paused) {
                lastTime = System.currentTimeMillis();  // reset time so when unpaused can begin normally
            } else {
                lastTime = currentTime;
            }
        }
    }
    
    public abstract void setup();
    public abstract boolean process(int timeElapsed);
    public abstract void draw();
}
