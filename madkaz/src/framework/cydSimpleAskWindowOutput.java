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

public class cydSimpleAskWindowOutput {
    public cydWindowOutput m_windowOutput;
    public cydTextOutput m_textOutput;
    public cydMenuOutput m_menuOutput;
    public cydFontLib m_font;
    
    public int m_width;
    public int m_height;
    public int m_x;
    public int m_y;
    
    public int m_titleColor;
    public char []m_title;
    public char []m_output;
    public char [][]m_menuItems;
    
    public int m_descX;
    public int m_descY;
    public int m_descWidth;
    public int m_descHeight;
    
    public int m_menuX;
    public int m_menuY;
    public int m_menuWidth;
    public int m_menuHeight;
    
    public cydSimpleAskWindowOutput(cydFontLib font, char []title, char []output, char [][]menuItems, int []itemColors, int x, int y, int width, int height, int titleColor) {
        reset(font, title, output, menuItems, itemColors, x, y, width, height, titleColor);
    }

    public void reset(cydFontLib font, char []title, char []output, char [][]menuItems, int []itemColors, int x, int y, int width, int height, int titleColor) {
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
        
        if (output != null)
            m_output = output;
        
        if (menuItems != null)
            m_menuItems = menuItems;
        
        m_windowOutput = new cydWindowOutput(m_font, m_title, 0x000000, m_titleColor, 0xA0A0A0, 0xC0C0C0, 0x000000, m_x, m_y, m_width, m_height, true);
        
        m_descX = m_windowOutput.getOutputLeft();
        m_descY = m_windowOutput.getOutputTop();
        m_descWidth = m_windowOutput.getOutputWidth();
        m_descHeight = m_windowOutput.getOutputHeight() / 5 * 3;
        
        m_menuX = m_windowOutput.getOutputLeft();
        m_menuY = m_descY + m_descHeight + 3;
        m_menuWidth = m_windowOutput.getOutputWidth();
        m_menuHeight = m_windowOutput.getOutputHeight() - m_descHeight - 3;

        if (m_textOutput == null)
            m_textOutput = new cydTextOutput(m_font, m_descX + 1, m_descY + 1, m_descWidth - 2, cydTextOutput.getNumberOfLinesDisplayable(m_font, m_descHeight - 2), m_titleColor, m_titleColor, 20);
        else
            m_textOutput.reset(m_descX + 1, m_descY + 1, m_descWidth - 2, cydTextOutput.getNumberOfLinesDisplayable(m_font, m_descHeight - 2), m_titleColor, m_titleColor, 20);            
        
        if (output != null) {     // if output is null, no new text is being posted!
            m_textOutput.setBuffer(m_output);
            m_textOutput.renew();
        }
        
        m_menuOutput = new cydMenuOutput(m_font, m_menuItems, null, m_menuX + 1, m_menuY + 1, m_menuWidth - 2, m_menuHeight - 2, 0x000000, 0x000000, 0x000000, 0x000000, itemColors);
    }
    
    // -1 = means nothing was selected
    // else = the item that was selected
    public int process(int timeElapsed, int keyStates) {
        m_textOutput.process(timeElapsed);

        if (m_textOutput.isDone()) {
            m_menuOutput.process(timeElapsed, keyStates);

            if ((keyStates & GameCanvas.FIRE_PRESSED) != 0)
                return m_menuOutput.getSelectionIndex();
        } else if (m_textOutput.isWaiting() && keyStates != 0) {
            m_textOutput.moveDown();
        }
        
        return -1;
    }
    
    public void draw(Graphics g) {
        m_windowOutput.draw(g);
        
        g.setColor(0x000000);
        g.drawRect(m_descX, m_descY, m_descWidth, m_descHeight);
        g.setColor(0x000000);
        g.drawRect(m_menuX, m_menuY, m_menuWidth, m_menuHeight);
        
        m_textOutput.draw(g);
        
       if (m_textOutput.isDone())
           m_menuOutput.draw(g);
    }
}
