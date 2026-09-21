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
import framework.cydCanvas;
import framework.cydFontLib;
import framework.cydWindowOutput;
import javax.microedition.lcdui.Graphics;

public class cydErrorCanvas extends cydCanvas {
    public static final char []m_title = "Error".toCharArray();
    
    public char []m_errorMsg;
    public cydGameManager m_gm;
    public cydWindowOutput m_windowOutput;
    public boolean m_done = false;
    
    public cydErrorCanvas(cydGameManager gm, cydFontLib font, String errorMsg) {
        super(font, true);
        
        m_errorMsg = ("A critical error has occured. Please e-mail this message to bugs@cydev.mobi: " + errorMsg + " -- Press any key").toCharArray();
        m_gm = gm;
    }

    public void setup() {
        m_windowOutput = new cydWindowOutput(m_gm.m_font, m_title, 0x000000, 0xFF0000, 0xA0A0A0, 0xFFFFFF, 0x000000, 10, 10, getWidth() - 20, getHeight() - 20, true);
    }

    public void draw(Graphics g) {
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        m_windowOutput.draw(g);
        m_font.setForegroundOutlineColor(0x000000);
        m_font.drawRealWWString(g, m_errorMsg, 0, m_errorMsg.length, m_windowOutput.getOutputLeft(), m_windowOutput.getOutputTop(), m_windowOutput.getOutputWidth(), m_windowOutput.getOutputHeight() / m_font.getHeight());
    }

    public boolean process(int timeElapsed) {
        return !m_done;
    }

    protected void keyReleased(int keyCode) {
        m_done = true;
    }
}
