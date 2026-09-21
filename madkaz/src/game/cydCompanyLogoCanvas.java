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
import framework.cydGameCanvas;
import framework.cydPointInterpolator;
import java.io.DataInputStream;
import java.util.Random;
import vm.cydByteArrayInputStream;

public class cydCompanyLogoCanvas extends cydGameCanvas {
    public static final int INDEX_INTRO_TEXT_ONE                = 0;
    public static final int INDEX_INTRO_TEXT_TWO                = 1;
    
    public int m_urlDisplayX1 = 0;
    public int m_urlDisplayX2 = 0;
    public int m_urlDisplayStartY = 0;
    
    public int m_blockSideLength;
    public int m_blockSpacerLength;
    public int m_blockStartX;
    public int m_blockStartY;
    
    public Random m_random = new Random();
    
    public int m_fontHeight = 0;
    
    public int m_timeElapsedUnused = 0;
    
    public int []m_color = new int[26];
    public int m_nextIndex = 0;
    
    public cydPointInterpolator m_doorOpener;
    public cydPointInterpolator m_bombDrop;
    
    public char []m_introTextOne;
    public char []m_introTextTwo;
    
    public int m_width = 0;
    public int m_height = 0;
    
    public cydCompanyLogoCanvas(cydFontLib font) {
        super(font);
    }
    
    public void setup() {
        m_width = getWidth();
        m_height = getHeight();
        
        setBackground(0x000000);
        
        byte []vmData = cydGameManager.getISToByteArray(getClass().getResourceAsStream("/splash_res.script"));
        
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        m_introTextOne = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_INTRO_TEXT_ONE);
        m_introTextTwo = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_INTRO_TEXT_TWO);
        
        m_fontHeight = m_font.getHeight();
        m_font.setForegroundFillColor(0xFFFFFF);
        m_font.setForegroundOutlineColor(0xFFFFFF);
        
        m_urlDisplayStartY = m_height - 10 - (m_fontHeight * 2);
        
        m_urlDisplayX1 = (m_width >> 1) - (m_font.getWidth(m_introTextOne) >> 1);
        m_urlDisplayX2 = (m_width >> 1) - (m_font.getWidth(m_introTextTwo) >> 1);
        
        m_blockSideLength = 10;
        m_blockSpacerLength = 3;
        
        m_blockStartX = (m_width / 2) - 71;
        m_blockStartY = (m_height / 2) - 39;
        
        int []points = new int[] {m_blockStartX + 13, m_blockStartY + 10, m_blockStartX + 23, m_blockStartY + 10,
                                  m_blockStartX + 13, m_blockStartY + 10, m_blockStartX + 18, m_blockStartY + 18,
                                  m_blockStartX + 13, m_blockStartY + 10, m_blockStartX + 13, m_blockStartY + 20,
                                  m_blockStartX + 13, m_blockStartY + 10, m_blockStartX + 12, m_blockStartY + 19,
                                  m_blockStartX + 13, m_blockStartY + 10, m_blockStartX + 14, m_blockStartY + 21,
                                  m_blockStartX + 13, m_blockStartY + 10, m_blockStartX + 13, m_blockStartY + 20};
        
        int []times = new int[] {500, 400, 300, 600, 700, 800};
        
        m_doorOpener = new cydPointInterpolator(4, points, times, 0, true);
        
        int []bombPoints = new int [] {m_blockStartX + 16, m_blockStartY,
                                       m_blockStartX + 16, m_blockStartY + (m_blockSideLength*5) + (m_blockSpacerLength*5) - 6,
                                       m_blockStartX + 16, m_blockStartY + 28,
                                       m_blockStartX + 16, m_blockStartY + 20,
                                       m_blockStartX + 16, m_blockStartY + 28,
                                       m_blockStartX + 16, m_blockStartY + (m_blockSideLength*5) + (m_blockSpacerLength*5) - 6,
                                       m_blockStartX + 16, m_blockStartY + 48,
                                       m_blockStartX + 16, m_blockStartY + 45,
                                       m_blockStartX + 16, m_blockStartY + 48,
                                       m_blockStartX + 16, m_blockStartY + (m_blockSideLength*5) + (m_blockSpacerLength*5) - 6};
        
        int []bombTimes = new int [] {800, 800, 800, 800, 800, 800, 700, 700, 700, 1000};
        
        m_bombDrop = new cydPointInterpolator(2, bombPoints, bombTimes, 0, true);
    }
    
    public void draw() {
        if (m_nextIndex == 26) {
            m_graphics.setColor(255);
            
            int []data = m_doorOpener.getPoints();
            
            m_graphics.drawLine(data[0], data[1], data[2], data[3]);
            
            if (m_doorOpener.isDone()) {
                m_graphics.setColor(0x7f7f7f);
                int []bombData = m_bombDrop.getPoints();
                m_graphics.fillRoundRect(bombData[0], bombData[1], 6, 6, 6, 6);
            }
        }
        
        for (int i = 0; i < 26; i++) {
            m_graphics.setColor(m_color[i]);
            
            switch (i) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                    m_graphics.fillRect(m_blockStartX + ((m_blockSideLength * i) + (m_blockSpacerLength * i)), m_blockStartY, m_blockSideLength, m_blockSideLength);
                    break;
                    
                case 5:
                case 6:
                    m_graphics.fillRect(m_blockStartX + ((m_blockSideLength * (i - 3)) + (m_blockSpacerLength * (i - 3))), m_blockStartY + m_blockSideLength + (m_blockSpacerLength), m_blockSideLength, m_blockSideLength);
                    break;
                    
                case 7:
                case 8:
                case 9:
                    m_graphics.fillRect(m_blockStartX + ((m_blockSideLength * (i - 3)) + (m_blockSpacerLength * (i - 3))) + (m_blockSideLength>>1), m_blockStartY + m_blockSideLength + (m_blockSpacerLength), m_blockSideLength, m_blockSideLength);
                    break;
                    
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                    m_graphics.fillRect(m_blockStartX + ((m_blockSideLength * (i - 6)) + (m_blockSpacerLength * (i - 6))) + (m_blockSideLength>>1), m_blockStartY + (m_blockSideLength*2) + (m_blockSpacerLength*2), m_blockSideLength, m_blockSideLength);
                    break;
                    
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                    m_graphics.fillRect(m_blockStartX + ((m_blockSideLength * (i - 12)) + (m_blockSpacerLength * (i - 12))) + (m_blockSideLength>>1), m_blockStartY + (m_blockSideLength*3) + (m_blockSpacerLength*3), m_blockSideLength, m_blockSideLength);
                    break;
                    
                case 21:
                case 22:
                case 23:
                    m_graphics.fillRect(m_blockStartX + ((m_blockSideLength * (i - 19)) + (m_blockSpacerLength * (i - 19))), m_blockStartY + (m_blockSideLength*4) + (m_blockSpacerLength*4), m_blockSideLength, m_blockSideLength);
                    break;
                    
                case 24:
                case 25:
                    m_graphics.fillRect(m_blockStartX + ((m_blockSideLength * (i - 24)) + (m_blockSpacerLength * (i - 24))), m_blockStartY + (m_blockSideLength*5) + (m_blockSpacerLength*5), m_blockSideLength, m_blockSideLength);
                    break;
            }
        }
        
        m_font.drawString(m_graphics, m_introTextOne, m_urlDisplayX1, m_urlDisplayStartY, -1);
        m_font.drawString(m_graphics, m_introTextTwo, m_urlDisplayX2, m_urlDisplayStartY + m_fontHeight + 1, -1);
    }
    
    public boolean process(int timeElapsed) {
        timeElapsed *= 3;
        
        if (m_nextIndex != 26) {
            m_color[m_nextIndex] += (timeElapsed);
            
            if (m_color[m_nextIndex] > 255) {
                m_color[m_nextIndex] = 255;
                m_nextIndex++;
            }
        } else {
            m_doorOpener.interpolate(timeElapsed);
            
            if (m_doorOpener.isDone()) {
                m_bombDrop.interpolate(timeElapsed);
                
                if (m_bombDrop.isDone()) {
                    m_timeElapsedUnused += timeElapsed;
                    
                    if (m_timeElapsedUnused >= 1700)
                        return false;
                }
            }
        }
        
        return true;
    }
}
