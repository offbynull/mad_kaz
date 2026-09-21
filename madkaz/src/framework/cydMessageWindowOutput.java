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

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class cydMessageWindowOutput {
    public static final int TRIANGLE_SIDE = 8;
    public static final int TRIANGLE_SPACER = 1;
    public static final int NEXT_TRIANGLE_TIME = 500;
    
    public static final int []DOWN_TRIANGLE_XY = { 0,0, TRIANGLE_SIDE >> 1,TRIANGLE_SIDE, TRIANGLE_SIDE,0 };
    public static final int []UP_TRIANGLE_XY = { 0,TRIANGLE_SIDE, TRIANGLE_SIDE >> 1,0, TRIANGLE_SIDE,TRIANGLE_SIDE };
    public static final int []LEFT_TRIANGLE_XY = { TRIANGLE_SIDE,0, 0,TRIANGLE_SIDE >> 1, TRIANGLE_SIDE,TRIANGLE_SIDE };
    public static final int []RIGHT_TRIANGLE_XY = { 0,0, 0,TRIANGLE_SIDE, TRIANGLE_SIDE,TRIANGLE_SIDE >> 1 };
    
    public int m_upTriangleX;
    public int m_upTriangleY;
    public int m_downTriangleX;
    public int m_downTriangleY;
    public int m_leftTriangleX;
    public int m_leftTriangleY;
    public int m_rightTriangleX;
    public int m_rightTriangleY;
    
    public cydWindowOutput m_windowOutput;
    
    public cydFontLib m_font;
    public char []m_leftCommand;
    public char []m_rightCommand;
    public char []m_title;
    
    public int m_titleColor;
    
    public int m_x;
    public int m_y;
    public int m_width;
    public int m_height;
    
    public int m_outputX;
    public int m_outputY;
    public int m_outputWidth;
    public int m_outputHeight;
    
    public int m_elapsedTriangleTime;
    public boolean m_showTriangle = false;
    
    public Vector m_windowData = new Vector();
    public Vector m_windowDataHeight = new Vector();
    public int m_windowDataTotal = 0;
    public int m_windowDataOffset = 0;
    
    public int m_canvasWidth;
    public int m_canvasHeight;
    
    public cydMessageWindowOutput(cydFontLib font, char []title, char []leftCommand, char []rightCommand, int x, int y, int width, int height, int titleColor, int canvasWidth, int canvasHeight) {
        reset(font, title, leftCommand, rightCommand, x, y, width, height, titleColor, canvasWidth, canvasHeight);
    }
    
    public void reset(cydFontLib font, char []title, char []leftCommand, char []rightCommand, int x, int y, int width, int height, int titleColor, int canvasWidth, int canvasHeight) {
        if (canvasWidth != -1)
            m_canvasWidth = canvasWidth;
        
        if (canvasHeight != -1)
            m_canvasHeight = canvasHeight;
        
        if (width != -1)
            m_width = width;
        
        if (height != -1)
            m_height = height;
        
        if (x != -1)
            m_x = x;
        
        if (y != -1)
            m_y = y;
        
        if (font != null)
            m_font = font;
        
        if (title != null)
            m_title = title;
        
        if (titleColor != -1)
            m_titleColor = titleColor;
        
        if (leftCommand != null)
            m_leftCommand = leftCommand;
        
        if (rightCommand != null)
            m_rightCommand = rightCommand;
        
        m_windowOutput = new cydWindowOutput(m_font, m_title, 0x000000, m_titleColor, 0xA0A0A0, 0xC0C0C0, 0x000000, m_x, m_y, m_width, m_height, true);
        
        int triangleBlock = TRIANGLE_SIDE + (TRIANGLE_SPACER * 2);
        
        m_outputX = m_windowOutput.getOutputLeft();
        m_outputY = m_windowOutput.getOutputTop() + triangleBlock;
        m_outputWidth = m_windowOutput.getOutputWidth();
        m_outputHeight = m_windowOutput.getOutputHeight() - (triangleBlock * 4);
        
        m_upTriangleX = (m_width / 2) - (TRIANGLE_SIDE / 2) + x;
        m_upTriangleY = m_windowOutput.getOutputTop() + TRIANGLE_SPACER;
        
        m_downTriangleX = (m_width / 2) - (TRIANGLE_SIDE / 2) + x;
        m_downTriangleY = m_outputY + m_outputHeight + TRIANGLE_SPACER + 1;
        
        m_leftTriangleX = m_outputX + TRIANGLE_SPACER;
        m_leftTriangleY = m_outputY + m_outputHeight + TRIANGLE_SPACER + (triangleBlock*2);
        
        m_rightTriangleX = m_outputX + m_outputWidth - TRIANGLE_SIDE - TRIANGLE_SPACER;
        m_rightTriangleY = m_outputY + m_outputHeight + TRIANGLE_SPACER + (triangleBlock*2);
    }
    
    public void addItem(char []b) {
        int height = m_font.getRealWWLineCount(b, 0, b.length, m_outputWidth) * m_font.getHeight() + m_font.getHeight();
        
        m_windowData.addElement(b);
        m_windowDataHeight.addElement(new Integer(height));
        
        m_windowDataTotal += height;
    }
    
    public void addItem(Image i) {
        int height = i.getHeight();
        
        m_windowData.addElement(i);
        m_windowDataHeight.addElement(new Integer(height));
        
        m_windowDataTotal += height;
    }
    
    public void removeAllItems() {
        m_windowData.removeAllElements();
        m_windowDataHeight.removeAllElements();
        
        m_windowDataTotal = 0;
        m_windowDataOffset = 0;
    }

    public void forceDown() {
        if (m_windowDataOffset + m_outputHeight > m_windowDataTotal)
            return;
        
        m_windowDataOffset = m_windowDataTotal - m_outputHeight;
    }
    
    public void moveDown() {
        if (m_windowDataOffset + m_outputHeight > m_windowDataTotal)
            return;
        
        m_windowDataOffset += 10;
        
        if (m_windowDataOffset + m_outputHeight > m_windowDataTotal)
            m_windowDataOffset = m_windowDataTotal - m_outputHeight;
    }
    
    public void moveUp() {
        m_windowDataOffset -= 10;
        
        if (m_windowDataOffset < 0)
            m_windowDataOffset = 0;
    }
    
    public void draw(Graphics g) {
        m_windowOutput.draw(g);
        
        g.setColor(0x000000);
        g.drawRect(m_outputX, m_outputY, m_outputWidth, m_outputHeight);
        
        g.setClip(m_outputX, m_outputY, m_outputWidth, m_outputHeight);
        
        g.translate(m_outputX, -m_windowDataOffset + m_outputY);
        
        int x = 0;
        int y = 0;
        
        for (int i = 0; i < m_windowData.size(); i++) {
            Object o = m_windowData.elementAt(i);
            
            if (o instanceof char []) {
                char []strBuf = (char [])o;
                
                int height = ((Integer)m_windowDataHeight.elementAt(i)).intValue();
                
                if (y + height >= m_windowDataOffset && y < m_windowDataOffset + m_outputHeight)
                    m_font.drawRealWWString(g, strBuf, 0, strBuf.length, 0, y, m_outputWidth, -1);
                
                y += height;
            } else if (o instanceof Image) {
                Image img = (Image)o;
                
                int height = ((Integer)m_windowDataHeight.elementAt(i)).intValue();
                
                if (y + height >= m_windowDataOffset && y < m_windowDataOffset + m_outputHeight)
                    g.drawImage(img, (m_outputWidth >> 1) - (img.getWidth() >> 1), y, 0);
                
                y += height;
            }
        }
        
        g.translate(-m_outputX, m_windowDataOffset - m_outputY);
        
        g.setClip(0, 0, m_canvasWidth, m_canvasHeight);
        
        int triangleBlock = TRIANGLE_SIDE + (TRIANGLE_SPACER * 2);
        
        if (m_showTriangle) {
            g.setColor(0x000000);
            
            if (m_windowDataOffset > 0) {
                g.fillTriangle(UP_TRIANGLE_XY[0]+m_upTriangleX,UP_TRIANGLE_XY[1]+m_upTriangleY,
                        UP_TRIANGLE_XY[2]+m_upTriangleX,UP_TRIANGLE_XY[3]+m_upTriangleY,
                        UP_TRIANGLE_XY[4]+m_upTriangleX,UP_TRIANGLE_XY[5]+m_upTriangleY);
            }
            
            if (m_windowDataOffset + m_outputHeight < m_windowDataTotal) {
                g.fillTriangle(DOWN_TRIANGLE_XY[0]+m_downTriangleX,DOWN_TRIANGLE_XY[1]+m_downTriangleY,
                        DOWN_TRIANGLE_XY[2]+m_downTriangleX,DOWN_TRIANGLE_XY[3]+m_downTriangleY,
                        DOWN_TRIANGLE_XY[4]+m_downTriangleX,DOWN_TRIANGLE_XY[5]+m_downTriangleY);
            }
            
            if (m_leftCommand.length > 0) {
                g.fillTriangle(LEFT_TRIANGLE_XY[0]+m_leftTriangleX,LEFT_TRIANGLE_XY[1]+m_leftTriangleY,
                        LEFT_TRIANGLE_XY[2]+m_leftTriangleX,LEFT_TRIANGLE_XY[3]+m_leftTriangleY,
                        LEFT_TRIANGLE_XY[4]+m_leftTriangleX,LEFT_TRIANGLE_XY[5]+m_leftTriangleY);
                
                m_font.drawString(g, m_leftCommand, m_leftTriangleX + TRIANGLE_SPACER + TRIANGLE_SIDE, m_leftTriangleY + (TRIANGLE_SIDE >> 1) - (m_font.getHeight() >> 1) , -1);
            }
            
            if (m_rightCommand.length > 0) {
                g.fillTriangle(RIGHT_TRIANGLE_XY[0]+m_rightTriangleX,RIGHT_TRIANGLE_XY[1]+m_rightTriangleY,
                        RIGHT_TRIANGLE_XY[2]+m_rightTriangleX,RIGHT_TRIANGLE_XY[3]+m_rightTriangleY,
                        RIGHT_TRIANGLE_XY[4]+m_rightTriangleX,RIGHT_TRIANGLE_XY[5]+m_rightTriangleY);
                
                m_font.drawString(g, m_rightCommand, m_rightTriangleX - TRIANGLE_SPACER - m_font.getWidth(m_rightCommand), m_rightTriangleY + (TRIANGLE_SIDE >> 1) - (m_font.getHeight() >> 1) , -1);
            }
        }
    }
    
    public int process(int timeElapsed, int keyStates) {
        m_elapsedTriangleTime += timeElapsed;
        
        if (m_elapsedTriangleTime > NEXT_TRIANGLE_TIME) {
            m_showTriangle = !m_showTriangle;
            m_elapsedTriangleTime = 0;
        }
        
        if ((keyStates & GameCanvas.DOWN_PRESSED) != 0)
            moveDown();
        else if ((keyStates & GameCanvas.UP_PRESSED) != 0)
            moveUp();
        else if ((keyStates & GameCanvas.LEFT_PRESSED) != 0 && m_leftCommand.length > 0)
            return 1;
        else if ((keyStates & GameCanvas.RIGHT_PRESSED) != 0 && m_rightCommand.length > 0)
            return 2;
        
        return -1;
    }
}
