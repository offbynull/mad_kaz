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
import framework.cydMusicPlayer;
import framework.cydPointInterpolator;
import framework.cydTokenizer;
import java.io.DataInputStream;
import java.util.Vector;
import vm.cydByteArrayInputStream;

public class cydCreditScrollerCanvas extends cydGameCanvas {
    public cydGameManager m_gm;
    
    
    public static final int INDEX_SCROLL_TEXT_BASE              = 100;
    
    public static final char BEGIN_BOUND                        = 'B';
    public static final char MID_BOUND                          = 'M';
    public static final char END_BOUND                          = 'E';
    
    
    public int m_fontHeight;
    public Vector m_unparsedStrings;
    
    public int []m_currentDrawStringWidths = null;
    public char [][]m_currentDrawStrings = null;
    public cydPointInterpolator m_drawPoints = null;
    
    public int m_nextIndex = 0;
    
    public int m_blockHeight;
    public int m_blockWidth;
    public int m_blockStartAtIndex;

    public int m_totalPoints;
    
    
    
    
    public cydCreditScrollerCanvas(cydGameManager gm) {
        super(gm.m_font);
        
        m_gm = gm;
    }

    public void setup() {
        setBackground(0x000000);
        m_gm.m_font.setForegroundOutlineColor(0xFFFFFF);
        
        m_fontHeight = m_gm.m_font.getHeight();
        
        byte []vmData = cydGameManager.getISToByteArray(getClass().getResourceAsStream("/sc_res.script"));
        
        loadCredits(vmData);
        
        if (m_gm.m_musicOn) {
            m_gm.m_player.stopMusic();
            m_gm.m_player.closeMusic();
            m_gm.m_player.newMusic(getClass().getResourceAsStream("/csamp4.mid"), cydMusicPlayer.DESIRED_MIME_TYPE);
            m_gm.m_player.startMusic();
        }
    }
    
    public void stop() {
        if (m_gm.m_musicOn) {
            m_gm.m_player.stopMusic();
            m_gm.m_player.closeMusic();
        }
    }
    
    public void loadCredits(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        m_unparsedStrings = new Vector();
        
        int next = 0;
        
        char []text = null;
        while ((text = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_SCROLL_TEXT_BASE + next)) != null) {
            String []parsedText = cydTokenizer.tokenize(new String(text), ':');
            
            char [][]parsedFinal = new char[parsedText.length][];
            
            for (int i = 0; i < parsedText.length; i++)
                parsedFinal[i] = parsedText[i].toCharArray();
            
            m_unparsedStrings.addElement(parsedFinal);
            next++;
        }
    }

    public boolean process(int timeElapsed) {
        int keyStates = m_gm.m_keyInputSlowDowner.processKeyPress(getKeyStates(), timeElapsed);
        
        if (((keyStates & LEFT_PRESSED) != 0) || ((keyStates & RIGHT_PRESSED) != 0))
            return false;
        
        if (m_drawPoints == null || m_drawPoints.isDone()) {
            if (m_nextIndex == m_unparsedStrings.size()) {
                return false;
            }
            
            
            
            // load here
            m_currentDrawStrings = (char [][])m_unparsedStrings.elementAt(m_nextIndex);

            
            m_totalPoints = Integer.parseInt(new String(m_currentDrawStrings[0]));
            m_blockStartAtIndex = (m_totalPoints * 3) + 1;
            
            
            loadStringBlockExtents();
            loadInterpolationPoints();
                
            
            
            m_nextIndex++;
        }
        
        m_drawPoints.interpolate(timeElapsed);
        
        return true;
    }

    private void loadInterpolationPoints() {
        // load interpolator points and times
        int []frameData = new int[m_totalPoints * 2];
        int []frameTimes = new int[m_totalPoints];
        
        for (int i = 0; i < m_totalPoints; i++) {
            char []xRatioInst = m_currentDrawStrings[1 + (i * 3)];
            char []yRatioInst = m_currentDrawStrings[1 + (i * 3) + 1];
            char []timeInst = m_currentDrawStrings[1 + (i * 3) + 2];
            
            int left = convertRatioToInt(xRatioInst, m_width, m_blockWidth);
            int top = convertRatioToInt(yRatioInst, m_height, m_blockHeight);
            int time = Integer.parseInt(new String(timeInst));
            
            frameData[(i * 2)] = left;
            frameData[(i * 2) + 1] = top;
            frameTimes[i] = time;
        }
        
        
        m_drawPoints = new cydPointInterpolator(2, frameData, frameTimes, 0, true);
    }
    
    private int convertRatioToInt(char[] ratioInst, int multiplier, int bounds) {
        char type = ratioInst[0];
        int ratio = Integer.parseInt(String.valueOf(ratioInst, 1, ratioInst.length - 1), 16);
        
        int result = (int)(((long)ratio * (long)(multiplier << 16)) >> 32);
        
        switch (type) {
            case cydCreditScrollerCanvas.BEGIN_BOUND:
                return result;
            case cydCreditScrollerCanvas.MID_BOUND:
                return result - (bounds / 2);
            case cydCreditScrollerCanvas.END_BOUND:
                return result - bounds;
        }
        
        return 0;
    }

    private void loadStringBlockExtents() {
        // calculate total height
        m_blockHeight = (m_currentDrawStrings.length - m_blockStartAtIndex) * m_fontHeight;
        
        // calculate total width
        
        m_blockWidth = 0;
        
        m_currentDrawStringWidths = new int[m_currentDrawStrings.length];
        
        for (int i = m_blockStartAtIndex; i < m_currentDrawStrings.length; i++) {
            int stringWidth = m_gm.m_font.getWidth(m_currentDrawStrings[i]);
            m_currentDrawStringWidths[i] = stringWidth;
            
            if (stringWidth > m_blockWidth)
                m_blockWidth = stringWidth;
        }
    }

    public void draw() {
        int []points = m_drawPoints.getPoints();
        
        int x = points[0];
        int y = points[1];
        
        for (int i = m_blockStartAtIndex; i < m_currentDrawStrings.length; i++)
            m_gm.m_font.drawString(m_graphics, m_currentDrawStrings[i], x + ((m_blockWidth >> 1) - (m_currentDrawStringWidths[i] >> 1)), y + ((i - m_blockStartAtIndex) * m_fontHeight), -1);
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
