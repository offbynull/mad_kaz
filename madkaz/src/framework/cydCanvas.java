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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.media.Player;

public abstract class cydCanvas extends Canvas implements Runnable {
    public static final char []LOADING_TEXT = "LOADING! PLEASE WAIT...".toCharArray();
    
    public Thread m_thread;
    public boolean m_paused;
    public Player m_noise;
    
    public cydFontLib m_font;
    public int m_background = -1;
    
    public int m_width;
    public int m_height;
    
    public boolean m_setupComplete = false;
    
    public boolean m_ignoreWidthCheck;
    
    
    public cydCanvas(cydFontLib font, boolean ignoreWidthCheck) {
        m_ignoreWidthCheck = ignoreWidthCheck;
        
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
    
    public void run() {
        Thread.yield();
        
        m_width = getWidth();
        m_height = getHeight();
        
        if (m_width != 176 && !m_ignoreWidthCheck)
            throw new RuntimeException("phone width > 176");
        
        setup();
        m_setupComplete = true;
        
        long lastTime = System.currentTimeMillis();
        long currentTime = 0L;
        int timeElapsed = 0;
        
        while (true) {
            currentTime = System.currentTimeMillis();
            timeElapsed = (int)(currentTime - lastTime);
            
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
            
            
            repaint();
            serviceRepaints();
            
            
            
            // paused
            if (m_paused) {
                lastTime = System.currentTimeMillis();  // reset time so when unpaused can begin normally
            } else {
                lastTime = currentTime;
            }
        }
    }
    
    public void paint(Graphics g) {
        if (!m_setupComplete)
            return;
        
        // draw background
        if (m_background != -1) {
            g.setColor(m_background);
            g.fillRect(0, 0, m_width, m_height);
        }
        
        
        
        // draw scene
        draw(g);
    }
    
    public abstract void setup();
    public abstract boolean process(int timeElapsed);
    public abstract void draw(Graphics g);
}
