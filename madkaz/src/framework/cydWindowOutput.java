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

public class cydWindowOutput {
    public cydFontLib m_font;
            
    public char []m_titleText;
    public int m_titleTextColor;
    public int m_titleColor;
    public int m_bgColor;
    public int m_contentColor;
    public int m_outlineColor;
    public int m_x;
    public int m_y;
    public int m_width;
    public int m_height;
    
    public int m_titleX;
    public int m_titleY;
    
    public cydWindowOutput(cydFontLib font, char []titleText, int titleTextColor, int titleColor, int bgColor, int contentColor, int outlineColor, int x, int y, int width, int height, boolean centerTitle) {
        reset(font, titleText, titleTextColor, titleColor, bgColor, contentColor, outlineColor, x, y, width, height, centerTitle);
    }
    
    public int getOutputTop() {
        return m_y + m_font.getHeight() + 15;
    }
    
    public int getOutputBottom() {
        return m_y + m_height - 5;
    }
    
    public int getOutputLeft() {
        return m_x + 7;
    }
    
    public int getOutputRight() {
        return m_x + m_width - 7;
    }
    
    public int getOutputWidth() {
        return getOutputRight() - getOutputLeft();
    }
    
    public int getOutputHeight() {
        return getOutputBottom() - getOutputTop();
    }
    
    public void standardizeHeader() {
//        int dots = m_font.getWidth('.') * 3;
//        boolean tooBig = false;
//        
//        while (m_font.getWidth(m_titleText) + dots > getOutputWidth()) {
//            m_titleText.deleteCharAt(m_titleText.length()-1);
//            tooBig = true;
//        }
//        
//        if (tooBig) {
//            for (int i = 0; i < 3; i++)
//                m_titleText.append('.');
//        }
    }
    
    public void reset(cydFontLib font, char []titleText, int titleTextColor, int titleColor, int bgColor, int contentColor, int outlineColor, int x, int y, int width, int height, boolean centerTitle) {
        if (font != null)
            m_font = font;
        
        if (titleText != null)
            m_titleText = titleText;
        
        if (width != -1)
            m_width = width;
        
        if (height != -1)
            m_height = height;
        
        if (centerTitle && (titleText != null)) {
            m_titleX = (m_width / 2) - (m_font.getWidth(m_titleText) / 2) + 3;
            m_titleY = 6;
        } else {
            m_titleX = 6;
            m_titleY = 6;
        }
        
        if (titleTextColor != -1)
            m_titleTextColor = titleTextColor;
        
        if (titleColor != -1)
            m_titleColor = titleColor;
        
        if (bgColor != -1)
            m_bgColor = bgColor;
        
        if (contentColor != -1)
            m_contentColor = contentColor;
        
        if (outlineColor != -1)
            m_outlineColor = outlineColor;
        
        if (x != -1)
            m_x = x;
        
        if (y != -1)
            m_y = y;
        
        if (titleText != null)
            standardizeHeader();
    }
    
    public void draw(Graphics g) {
        int fontHeight = m_font.getHeight();
        
        g.translate(m_x, m_y);
        
        if (m_bgColor != -2) {
            g.setColor(m_bgColor);
            g.fillRect(0, 0, m_width, m_height);
        }
        
        g.setColor(m_titleColor);
        g.fillRect(4, 4, m_width - 8, fontHeight + 4);
        
        m_font.setForegroundOutlineColor(m_titleTextColor);
        
        if (m_titleText != null)
            m_font.drawString(g, m_titleText, m_titleX, m_titleY, -1);
        
        g.setColor(m_contentColor);
        g.fillRect(4, fontHeight + 12, m_width - 8, m_height - fontHeight - 14);
        
        g.setColor(m_outlineColor);
        g.drawRect(0, 0, m_width, m_height);
        g.drawRect(4, 4, m_width - 8, fontHeight + 4);
        g.drawRect(4, fontHeight + 12, m_width - 8, m_height - fontHeight - 14);
        
        g.translate(-m_x, -m_y);
    }
}
