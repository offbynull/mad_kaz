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

public class cydMenuOutput {
    public static final int HEADER_ITEM_SELECTION_OFFSET = 1;
    public static final int HEADER_HEAD_SPACER = 2;
    public static final int HEADER_FOOT_SPACER = 2;
    public static final int NEXT_TRIANGLE_TIME = 500;
    public static final int TRIANGLE_SIDE = 8;
    public static final int TRIANGLE_SPACER = (HEADER_ITEM_SELECTION_OFFSET << 1) + 2;
    public static final int HEADER_ITEM_SPACER = (HEADER_ITEM_SELECTION_OFFSET << 1) + 1;
    
    public static final int NEXT_SELECTION_TIME = 500;
    
    public static final int []DOWN_TRIANGLE_XY = { 0,0, TRIANGLE_SIDE >> 1,TRIANGLE_SIDE, TRIANGLE_SIDE,0 };
    public static final int []UP_TRIANGLE_XY = { 0,TRIANGLE_SIDE, TRIANGLE_SIDE >> 1,0, TRIANGLE_SIDE,TRIANGLE_SIDE };
    
    public int m_selectionIndex = 0;
    
    public int m_headerX;
    public int m_headerY;
    public char []m_header;
    
    public int []m_itemPositions; //x ,y
    public char [][]m_items;
    
    public int m_x;
    public int m_y;
    public int m_width;
    public int m_height;
    
    public int m_headerSize;
    
    public int m_numberOfItemsOnScreen;
    public int m_topItemNumber;
    
    public int m_elapsedTriangleTime;
    public boolean m_showTriangle = false;
    
    public int m_elapsedSelectionTime;
    public int m_selectionOffset = HEADER_ITEM_SELECTION_OFFSET;
    
    public cydFontLib m_font;
    
    public int m_fontHeight;
    
    public int []m_seperateItemColors;
    public int m_itemColor;
    public int m_headerColor;
    public int m_selectionOutlineColor;
    public int m_triangleColor;
    
    public cydMenuOutput(cydFontLib font, char [][]items, char []header, int x, int y, int width, int height, int itemColor, int headerColor, int selectionOutlineColor, int triangleColor, int []seperateItemColors) {
        reset(font, items, header, x, y, width, height, itemColor, headerColor, selectionOutlineColor, triangleColor, seperateItemColors);
    }
    
    public static int getHeight(cydFontLib font, int itemCount, boolean headerShown) {
        int headerHeight;
        int height;
        int fontHeight = font.getHeight();
        
        if (headerShown) {
            headerHeight = 0;
        } else {
            headerHeight = fontHeight + HEADER_HEAD_SPACER + HEADER_FOOT_SPACER;
        }
        
        return headerHeight + ((TRIANGLE_SIDE + (TRIANGLE_SPACER * 2)) * 2) + ((HEADER_ITEM_SPACER + fontHeight) * itemCount);
    }
    
    public int getSelectionIndex() {
        return m_selectionIndex;
    }
    
    public void setSelectionIndex(int n) {
        if (n > m_topItemNumber + m_numberOfItemsOnScreen)
            m_topItemNumber = n;
        
        m_selectionIndex = n;
    }

    public void reset(cydFontLib font, char [][]items, char []header, int x, int y, int width, int height, int itemColor, int headerColor, int selectionOutlineColor, int triangleColor, int []seperateItemColors) {
        if (itemColor != -1)
            m_itemColor = itemColor;
        
        if (headerColor != -1)
            m_headerColor = headerColor;
        
        if (selectionOutlineColor != -1)
            m_selectionOutlineColor = selectionOutlineColor;
        
        if (triangleColor != -1)
            m_triangleColor = triangleColor;

        if (width != -1)
            m_width = width;
        
        if (height != -1)
            m_height = height;
        
        if (x != -1)
            m_x = x;
        
        if (y != -1)
            m_y = y;
        
        
        if (items != null) {
            m_items = items;
            m_itemPositions = new int[m_items.length<<1];
        }
        
        if (header != null)
            m_header = header;
        
        if (seperateItemColors != null)
            m_seperateItemColors = seperateItemColors;
        
        if (font != null) {
            m_font = font;
            m_fontHeight = m_font.getHeight();
        }
        
        if (m_header == null) {
            m_headerX = 0;
            m_headerY = 0;
            m_headerSize = 0;
        } else {
            m_headerX = (m_width / 2) - (m_font.getWidth(m_header) / 2);
            m_headerY = HEADER_HEAD_SPACER;
            m_headerSize = m_headerY + m_fontHeight + HEADER_FOOT_SPACER;
        }
        
        int len = 0;
        
        if (m_items != null)
            len = m_items.length;
        
        for (int i = 0; i < len; i++) {
            int wordWidth = m_font.getWidth(m_items[i]);
            
            m_itemPositions[(i<<1)] = (m_width / 2) - (wordWidth / 2);
            m_itemPositions[(i<<1)+1] = wordWidth;
        }
        
        m_numberOfItemsOnScreen = (m_height - (m_headerSize + ((TRIANGLE_SIDE + (TRIANGLE_SPACER * 2)) * 2))) / (HEADER_ITEM_SPACER + m_fontHeight);
        
        if (m_numberOfItemsOnScreen == 0)
            m_numberOfItemsOnScreen = 1;
        
        m_topItemNumber = 0;
    }

    public void draw(Graphics g) {
        g.translate(m_x, m_y);
        
        if (m_header != null) {
            m_font.setForegroundOutlineColor(m_headerColor);
            m_font.drawString(g, m_header, m_headerX, m_headerY, -1);
        }
        
        int len = m_items.length;
        
        int outputCount = 0;
        
        int fontHeight = m_font.getHeight();
        
        int totalItemSpace = m_numberOfItemsOnScreen * (fontHeight + HEADER_ITEM_SPACER);
        int itemYStart = m_headerSize + (TRIANGLE_SIDE + (TRIANGLE_SPACER * 2));
        int itemYEnd = itemYStart + m_height - (m_headerSize + ((TRIANGLE_SIDE + (TRIANGLE_SPACER * 2)) * 2));
        
        int menuYItemStart = m_headerSize + (TRIANGLE_SIDE + (TRIANGLE_SPACER * 2)) + ((itemYEnd - itemYStart) / 2 - (m_numberOfItemsOnScreen * (HEADER_ITEM_SPACER + m_fontHeight)) / 2);
        
        for (int i = m_topItemNumber; i < len; i++) {
            if (m_seperateItemColors != null && m_seperateItemColors[i] != -1)
                m_font.setForegroundOutlineColor(m_seperateItemColors[i]);
            else
                m_font.setForegroundOutlineColor(m_itemColor);
            
            if (outputCount == m_numberOfItemsOnScreen)
                break;
            
            int x = m_itemPositions[(i<<1)];
            int y = menuYItemStart + ((i - m_topItemNumber) * (fontHeight + HEADER_ITEM_SPACER));
            int width = m_itemPositions[(i<<1)+1];
            
            g.translate(x, y);
            
            if (i == m_selectionIndex) {
                g.setColor(m_selectionOutlineColor);
                g.drawRect(-m_selectionOffset, -m_selectionOffset, width + (m_selectionOffset<<1), fontHeight + (m_selectionOffset<<1));
            }
            
            m_font.drawString(g, m_items[i], 0, 0, -1);
            g.translate(-x, -y);
            
            outputCount++;
        }
        
        if (m_showTriangle) {
            g.setColor(m_triangleColor);
            
            if (m_topItemNumber > 0) {
                int x = (m_width >> 1) - (TRIANGLE_SIDE >> 1);
                int y = m_headerSize + TRIANGLE_SPACER;
                g.fillTriangle(x + UP_TRIANGLE_XY[0], y + UP_TRIANGLE_XY[1], x + UP_TRIANGLE_XY[2], y + UP_TRIANGLE_XY[3], x + UP_TRIANGLE_XY[4], y + UP_TRIANGLE_XY[5]);
            }
            
            if (m_topItemNumber + m_numberOfItemsOnScreen < len) {
                int x = (m_width >> 1) - (TRIANGLE_SIDE >> 1);
                int y = m_height - TRIANGLE_SPACER - TRIANGLE_SIDE;
                g.fillTriangle(x + DOWN_TRIANGLE_XY[0], y + DOWN_TRIANGLE_XY[1], x + DOWN_TRIANGLE_XY[2], y + DOWN_TRIANGLE_XY[3], x + DOWN_TRIANGLE_XY[4], y + DOWN_TRIANGLE_XY[5]);
            }
        }
        
        g.translate(-m_x, -m_y);
    }

    public boolean process(int timeElapsed, int keyInput) {
        m_elapsedTriangleTime += timeElapsed;
        m_elapsedSelectionTime += timeElapsed;
        
        if (m_elapsedTriangleTime > NEXT_TRIANGLE_TIME) {
            m_showTriangle = !m_showTriangle;
            m_elapsedTriangleTime = 0;
        }
        
        if (m_elapsedSelectionTime > NEXT_SELECTION_TIME) {
            if (m_selectionOffset == HEADER_ITEM_SELECTION_OFFSET)
                m_selectionOffset <<= 1;
            else
                m_selectionOffset = HEADER_ITEM_SELECTION_OFFSET;
            
            m_elapsedSelectionTime = 0;
        }
        
        if ((keyInput & GameCanvas.FIRE_PRESSED) != 0) {
            return false;
        }
        
        if ((keyInput & GameCanvas.DOWN_PRESSED) != 0) {
            if (m_selectionIndex == m_items.length - 1)
                return true;
            
            m_selectionIndex++;
            
            if (m_selectionIndex == m_topItemNumber + m_numberOfItemsOnScreen)
                m_topItemNumber++;
        } else if ((keyInput & GameCanvas.UP_PRESSED) != 0) {
            if (m_selectionIndex == 0)
                return true;
            
            m_selectionIndex--;
            
            if (m_selectionIndex < m_topItemNumber && m_topItemNumber > 0)
                m_topItemNumber--;
        }
        
        return true;
    }
}
