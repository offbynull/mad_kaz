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

public class cydTextOutput {
    public static final int NEXT_CHAR_TIME = 200;
    
    public static final int NEXT_TRIANGLE_TIME = 200;
    public static final int TRIANGLE_SIDE = 8;
    public static final int TRIANGLE_SPACER = 2;
    
    public static final int []NEXT_TRIANGLE_XY = { 0,0, TRIANGLE_SIDE >> 1,TRIANGLE_SIDE, TRIANGLE_SIDE,0 };
    
    public int m_elapsedTime;
    public boolean m_showTriangle;
    public int m_triangleColor;
    
    public cydFontLib m_font;
    public char []m_totalTextBlock;
    
    public int m_x;
    public int m_y;
    public int m_width;
    public int m_numberOfLinesToShow;
    public int m_triangleStartX;
    public int m_triangleStartY;
    
    public int m_textColor;
    
    public int m_startIndex;
    public int m_endIndex;
    public int m_nextPageIndex;
    public boolean m_reachedEndOfBuffer;
    
    public int m_charStartIndex;
    
    public int m_charSwitchTime = NEXT_CHAR_TIME;
    
    public static int getTotalHeight(cydFontLib font, int numberOfLinesToShow) {
        return (font.getHeight() * numberOfLinesToShow) + TRIANGLE_SIDE + TRIANGLE_SPACER;
    }
    
    public static int getNumberOfLinesDisplayable(cydFontLib font, int height) {
        return (height - TRIANGLE_SIDE + TRIANGLE_SPACER) / font.getHeight();
    }
    
    public cydTextOutput(cydFontLib font, int x, int y, int width, int numberOfLinesToShow, int color, int triangleColor, int charSwitchTime) {
        m_font = font;
        reset(x, y, width, numberOfLinesToShow, color, triangleColor, charSwitchTime);
    }
    
    public void setBuffer(char []buf) {
        m_totalTextBlock = buf;
        m_nextPageIndex = m_font.getRealWWVisibleLength(buf, 0, buf.length - 1, m_width, m_numberOfLinesToShow);
    }
    
    public void reset(int x, int y, int width, int numberOfLinesToShow, int color, int triangleColor, int charSwitchTime) {
        if (x != -1)
            m_x = x;
        
        if (y != -1)
            m_y = y;
        
        if (width != -1)
            m_width = width;
        
        if (numberOfLinesToShow != -1)
            m_numberOfLinesToShow = numberOfLinesToShow;
        
        if (color != -1)
            m_textColor = color;
        
        if (triangleColor != -1)
            m_triangleColor = triangleColor;
        
        m_triangleStartX = m_x + ((m_width/2) - (TRIANGLE_SIDE/2));
        m_triangleStartY = m_y + (m_font.getHeight() * numberOfLinesToShow) + TRIANGLE_SPACER;
        
        if (charSwitchTime != -1)
            m_charSwitchTime = charSwitchTime;
    }
    
    public void draw(Graphics g) {
        m_font.setForegroundFillColor(m_textColor);
        m_font.setForegroundOutlineColor(m_textColor);
        
        m_reachedEndOfBuffer = m_font.drawRealWWString(g, m_totalTextBlock, m_startIndex, m_endIndex, m_x, m_y, m_width, m_numberOfLinesToShow);
        
        if (m_showTriangle && (m_endIndex < m_totalTextBlock.length)) {
            g.setColor(m_triangleColor);
            
            g.fillTriangle(NEXT_TRIANGLE_XY[0] + m_triangleStartX, NEXT_TRIANGLE_XY[1] + m_triangleStartY, 
                           NEXT_TRIANGLE_XY[2] + m_triangleStartX, NEXT_TRIANGLE_XY[3] + m_triangleStartY,
                           NEXT_TRIANGLE_XY[4] + m_triangleStartX, NEXT_TRIANGLE_XY[5] + m_triangleStartY);
        }
    }
    
    public void process(int timeElapsed) {
        if (!m_reachedEndOfBuffer) {
            m_showTriangle = false;
            
            m_elapsedTime += timeElapsed;
            
            while (m_elapsedTime > m_charSwitchTime && m_endIndex < m_nextPageIndex) {
                m_endIndex++; 
                m_elapsedTime -= m_charSwitchTime;
            }
        } else {
            m_elapsedTime += timeElapsed;
            
            if (m_elapsedTime > NEXT_TRIANGLE_TIME) {
                m_showTriangle = !m_showTriangle;
                
                m_elapsedTime = 0;
            }
        }
    }
    
    public void renew() {
        m_startIndex = 0;
        m_endIndex = 0;
        
        m_nextPageIndex = m_font.getRealWWVisibleLength(m_totalTextBlock, m_startIndex, m_totalTextBlock.length - 1, m_width, m_numberOfLinesToShow);
    }
    
    public boolean isDone() {
        return m_endIndex >= m_totalTextBlock.length;
    }
    
    public boolean moveDown() {
        int newStart = m_endIndex;
        
        if (newStart >= m_totalTextBlock.length)
            return false;
        
        m_startIndex = newStart;
        
        m_nextPageIndex = m_font.getRealWWVisibleLength(m_totalTextBlock, m_startIndex, m_totalTextBlock.length - 1, m_width, m_numberOfLinesToShow) + m_startIndex;
        
        return true;
    }
    
    public boolean isWaiting() {
        return m_reachedEndOfBuffer;
    }
}
