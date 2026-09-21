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
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class cydImageHeaderMenuWindowOutput {
    public cydWindowOutput m_windowOutput;
    public cydMenuOutput m_menuOutput;
    public cydFontLib m_font;
    
    public int m_width;
    public int m_height;
    public int m_x;
    public int m_y;
    
    public int m_titleColor;
    
    public int m_boundsX;
    public int m_boundsY;
    public int m_boundsWidth;
    public int m_boundsHeight;

    public int m_headerImageX;
    public int m_headerImageY;
    public int m_headerImageWidth;
    public int m_headerImageHeight;
    
    char []m_title;
    char []m_innerTitle;
    char [][] m_menuItems;
    Image m_headerImage;
    
    public cydImageHeaderMenuWindowOutput(cydFontLib font, char []title, char []innerTitle, char [][]menuItems, int []itemColors, Image headerImage, int x, int y, int width, int height, int titleColor) {
        reset(font, title, innerTitle, menuItems, itemColors, headerImage, x, y, width, height, titleColor);
    }
    
    public void reset(cydFontLib font, char []title, char []innerTitle, char [][]menuItems, int []itemColors, Image headerImage, int x, int y, int width, int height, int titleColor) {
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
        
        if (innerTitle != null)
            m_innerTitle = innerTitle;
        
        if (menuItems != null)
            m_menuItems = menuItems;
        
        m_windowOutput = new cydWindowOutput(m_font, m_title, 0x000000, m_titleColor, 0xA0A0A0, 0xC0C0C0, 0x000000, m_x, m_y, m_width, m_height, true);
        
        if (headerImage != null) {
            m_headerImage = headerImage;
            
            m_headerImageX = m_windowOutput.getOutputLeft() + (m_windowOutput.getOutputWidth() / 2) - (m_headerImage.getWidth() / 2);
            m_headerImageY = m_windowOutput.getOutputTop() + 2;
            m_headerImageWidth = m_headerImage.getWidth();
            m_headerImageHeight = m_headerImage.getHeight() + 4;
        }
        
        m_boundsX = m_windowOutput.getOutputLeft();
        m_boundsY = m_windowOutput.getOutputTop();
        m_boundsWidth = m_windowOutput.getOutputWidth();
        m_boundsHeight = m_windowOutput.getOutputHeight();
        
        m_menuOutput = new cydMenuOutput(m_font, m_menuItems, m_innerTitle, m_boundsX + 1, m_boundsY + 1 + m_headerImageHeight, m_boundsWidth - 2, m_boundsHeight - 2 - m_headerImageHeight, 0x000000, 0x000000, 0x000000, 0x000000, itemColors);
    }
    
    public int process(int timeElapsed, int keyStates) {
        m_menuOutput.process(timeElapsed, keyStates);
        
        if ((keyStates & GameCanvas.FIRE_PRESSED) != 0)
            return m_menuOutput.getSelectionIndex();
        
        return -1;
    }
    
    public void draw(Graphics g) {
        g.setColor(0x000000);
        g.drawRect(m_boundsX, m_boundsY, m_boundsWidth, m_boundsHeight);
        
        m_windowOutput.draw(g);
        
        if (m_headerImage != null)
            g.drawImage(m_headerImage, m_headerImageX, m_headerImageY, 0);
        
        m_menuOutput.draw(g);
    }
}
