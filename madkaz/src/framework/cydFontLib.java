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

public class cydFontLib {
    private int m_foregroundOutlineColor;
    private int m_foregroundFillColor;
    private int m_height;
    private byte []m_data = null;
    private short []m_charOffsets = null;
    
    private static final byte MODE_NORM = 0;
    private static final byte MODE_FILL = 1;
    
    public cydFontLib(byte []vfsContent, int offset) {
        if (vfsContent[offset + 0] != 0x56 || vfsContent[offset + 1] != 0x46 || vfsContent[offset + 2] != 0x53 || vfsContent[offset + 3] != 0x30)
            return;
        
        int len = (vfsContent[offset + 4] & 0xFF) << 24;
        len |= (vfsContent[offset + 5] & 0xFF) << 16;
        len |= (vfsContent[offset + 6] & 0xFF) << 8;
        len |= (vfsContent[offset + 7] & 0xFF);
        
        m_data = new byte[len];
        m_charOffsets = new short[96];
        
        m_height = (vfsContent[offset + 8] & 0xFF) << 24;
        m_height |= (vfsContent[offset + 9] & 0xFF) << 16;
        m_height |= (vfsContent[offset + 10] & 0xFF) << 8;
        m_height |= (vfsContent[offset + 11] & 0xFF);
        
        System.arraycopy(vfsContent, offset+12, m_data, 0, len);
        calculateOffsets();
    }
    
    public cydFontLib(cydFontLib orig) {
        m_foregroundOutlineColor = orig.m_foregroundOutlineColor;
        m_foregroundFillColor = orig.m_foregroundFillColor;
        m_height = orig.m_height;
        m_data = new byte[orig.m_data.length];
        m_charOffsets = new short[orig.m_charOffsets.length];
        
        System.arraycopy(orig.m_data, 0, m_data, 0, m_data.length);
        System.arraycopy(orig.m_charOffsets, 0, m_charOffsets, 0, m_charOffsets.length);
    }
    
    public void resize(int fp88RatioX, int fp88RatioY) {
        int len = m_charOffsets.length;
        
        m_height = ((m_height << 8) * fp88RatioY) >> 16;
        
        for (int offset = 0; offset < len; offset++) {
            int dataOffset = m_charOffsets[offset];
            int internalBlockCount = m_data[dataOffset++];
            m_data[dataOffset] = (byte)(((m_data[dataOffset] << 8) * fp88RatioX) >> 16); // width
            
            dataOffset++;
            
            for (int i = 0; i < internalBlockCount; i++) {
                int mode = m_data[dataOffset++];
                int length = m_data[dataOffset++];
                
                for (int j = 0; j < length; j++) {
                    m_data[dataOffset] = (byte)(((m_data[dataOffset] << 8) * fp88RatioX) >> 16);
                    dataOffset++;
                    m_data[dataOffset] = (byte)(((m_data[dataOffset] << 8) * fp88RatioY) >> 16);
                    dataOffset++;
                }
            }
        }
    }
    
    private void calculateOffsets() {
        int counter = 0;
        int symbolCounter = 0;
        int total = m_data.length;
        
        while (counter != total) {
            m_charOffsets[symbolCounter] = (short)counter;
            symbolCounter++;
            
            int blockCount = m_data[counter++];
            int charWidth = m_data[counter++];
            int dataLength = 0;
            
            for (int i = 0; i < blockCount; i++) {
                byte mode = m_data[counter++];
                byte vectorCount = m_data[counter++];
                
                counter += vectorCount << 1;
            }
        }
    }
    
    public void setForegroundOutlineColor(int color) {
        m_foregroundOutlineColor = color;
    }
    
    public void setForegroundFillColor(int color) {
        m_foregroundFillColor = color;
    }
    
    public int getHeight() {
        return m_height;
    }
    
    // calls second drawString
    public int drawString(Graphics g, char []str, int x, int y, int wwWidth) {
        return drawString(g, str, 0, str.length-1, x, y, wwWidth);
    }
    
    public int drawString(Graphics g, char []str, int startIndex, int endIndex, int x, int y, int wwWidth) {
        int originalX = x;
        
        byte []data = m_data;
        short []charOffsets = m_charOffsets;
        
        int origHeight = m_height;
        g.setColor(m_foregroundOutlineColor);
        
        for (int i = startIndex; i <= endIndex; i++) {
            if (wwWidth != -1) {
                int width = data[charOffsets[str[i] - 32] + 1];
                
                if (x - originalX + width > wwWidth) {
                    x = originalX;
                    y += origHeight;
                }
            }
            
            char c = str[i];
            
            // inlined drawChar and removed some drawChar functionality
            {
                int offset = c - 32;
                
                if (c >= 93)
                    c = 20;
                
                int dataOffset = charOffsets[offset];
                int internalBlockCount = data[dataOffset++];
                int width = data[dataOffset++];
                
                for (int k = 0; k < internalBlockCount; k++) {
                    int mode = data[dataOffset++];          // fill mode never used. always using line mode.
                    int length = data[dataOffset++];
                    
                    int startPointX = x+data[dataOffset++];
                    int startPointY = y+data[dataOffset++];
                    
                    length--;
                    
                    for (int j = 0; j < length; j++) {
                        int endPointX = x+data[dataOffset++];
                        int endPointY = y+data[dataOffset++];
                        
                        g.drawLine(startPointX, startPointY, endPointX, endPointY);
                        
                        startPointX = endPointX;
                        startPointY = endPointY;
                    }
                }
                
                x += width;
            }
        }
        
        return x;
    }
    
// true = no more left to output
// false = more left to output
    public boolean drawRealWWString(Graphics g, char []str, int startIndex, int termIndex, int x, int y, int width, int maxLineNumbers) {
        g.translate(x, y);
        
        int lineNumber = 0;
        
        byte []data = m_data;
        short []charOffsets = m_charOffsets;
        int origHeight = m_height;
        
        int spaceLeft = width;
        int spaceWidth = data[charOffsets[' ' - 32] + 1];
        
        int len = str.length;
        
        {
            while (startIndex < len) {
                if (str[startIndex] != ' ')
                    break;
                
                startIndex++;
            }
            
            if (startIndex >= len)
                startIndex = -1;
        }
        
        if (startIndex == -1) {
            g.translate(-x, -y);
            return true;
        }
        
        g.setColor(m_foregroundOutlineColor);
        
        while (lineNumber != maxLineNumbers) {
            int endIndex = 0;
            
            {
                int index = startIndex;
                
                while (index < len) {
                    if (str[index] == ' ') {
                        endIndex = index - 1;
                        break;
                    }
                    
                    index++;
                }
                
                if (index >= len)
                    endIndex = len - 1;
            }
            
            int wordWidth = 0;
            
            {
                for (int i=startIndex; i <= endIndex; i++)
                    wordWidth += data[charOffsets[str[i] - 32] + 1];
            }
            
            if (wordWidth > spaceLeft) {
                if (spaceLeft != width) {
                    lineNumber++;
                    spaceLeft = width;
                }
                
                if (lineNumber == maxLineNumbers)
                    break;
                
                if (wordWidth > width) {
                    int currentWidth = 0;
                    
                    while (true) {
                        char c = str[startIndex];
                        
                        if (startIndex > termIndex) {
                            g.translate(-x, -y);
                            return startIndex == termIndex;
                        }
                        
                        int charWidth = data[charOffsets[c - 32] + 1];
                        
                        if (charWidth + currentWidth < width) {
                            int outX = currentWidth;
                            int outY = lineNumber * origHeight;
                            
                            int offset = c - 32;
                            
                            if (c >= 93)
                                c = 20;
                            
                            int dataOffset = charOffsets[offset];
                            int internalBlockCount = data[dataOffset++];
                            dataOffset++;
                            
                            for (int k = 0; k < internalBlockCount; k++) {
                                int mode = data[dataOffset++];          // fill mode never used. always using line mode.
                                int length = data[dataOffset++];
                                
                                int startPointX = outX+data[dataOffset++];
                                int startPointY = outY+data[dataOffset++];
                                
                                length--;
                                
                                for (int j = 0; j < length; j++) {
                                    int endPointX = outX+data[dataOffset++];
                                    int endPointY = outY+data[dataOffset++];
                                    
                                    g.drawLine(startPointX, startPointY, endPointX, endPointY);
                                    
                                    startPointX = endPointX;
                                    startPointY = endPointY;
                                }
                            }
                        } else {
                            break;
                        }
                        
                        startIndex++;
                        currentWidth += charWidth;
                    }
                    
                    lineNumber++;
                    
                    continue;
                } else {
                    while (startIndex <= endIndex) {
                        char c = str[startIndex++];
                        
                        if (startIndex > termIndex) {
                            g.translate(-x, -y);
                            return startIndex == termIndex;
                        }
                        
                        int charWidth = data[charOffsets[c - 32] + 1];
                        
                        {
                            int outX = width - spaceLeft;
                            int outY = lineNumber * origHeight;
                            
                            int offset = c - 32;
                            
                            if (c >= 93)
                                c = 20;
                            
                            int dataOffset = charOffsets[offset];
                            int internalBlockCount = data[dataOffset++];
                            dataOffset++;
                            
                            for (int k = 0; k < internalBlockCount; k++) {
                                int mode = data[dataOffset++];          // fill mode never used. always using line mode.
                                int length = data[dataOffset++];
                                
                                int startPointX = outX+data[dataOffset++];
                                int startPointY = outY+data[dataOffset++];
                                
                                length--;
                                
                                for (int j = 0; j < length; j++) {
                                    int endPointX = outX+data[dataOffset++];
                                    int endPointY = outY+data[dataOffset++];
                                    
                                    g.drawLine(startPointX, startPointY, endPointX, endPointY);
                                    
                                    startPointX = endPointX;
                                    startPointY = endPointY;
                                }
                            }
                        }
                        
                        spaceLeft -= charWidth;
                    }
                    
                    spaceLeft -= spaceWidth;
                }
            } else {
                while (startIndex <= endIndex) {
                    char c = str[startIndex++];
                    
                    if (startIndex > termIndex) {
                        g.translate(-x, -y);
                        return startIndex == termIndex;
                    }
                    
                    int charWidth = data[charOffsets[c - 32] + 1];
                    
                    {
                        int outX = width - spaceLeft;
                        int outY = lineNumber * origHeight;
                        
                        int offset = c - 32;
                        
                        if (c >= 93)
                            c = 20;
                        
                        int dataOffset = charOffsets[offset];
                        int internalBlockCount = data[dataOffset++];
                        dataOffset++;
                        
                        for (int k = 0; k < internalBlockCount; k++) {
                            int mode = data[dataOffset++];          // fill mode never used. always using line mode.
                            int length = data[dataOffset++];
                            
                            int startPointX = outX+data[dataOffset++];
                            int startPointY = outY+data[dataOffset++];
                            
                            length--;
                            
                            for (int j = 0; j < length; j++) {
                                int endPointX = outX+data[dataOffset++];
                                int endPointY = outY+data[dataOffset++];
                                
                                g.drawLine(startPointX, startPointY, endPointX, endPointY);
                                
                                startPointX = endPointX;
                                startPointY = endPointY;
                            }
                        }
                    }
                    
                    spaceLeft -= charWidth;
                }
                
                spaceLeft -= spaceWidth;
            }
            
            {
                startIndex = endIndex + 1;
                
                while (startIndex < len) {
                    if (str[startIndex] != ' ')
                        break;
                    
                    startIndex++;
                }
                
                if (startIndex >= len)
                    startIndex = -1;
            }
            
            if ((startIndex == -1) || (startIndex > termIndex))
                break;
        }
        
        g.translate(-x, -y);
        return startIndex == termIndex || startIndex == -1;
    }
    
    public int moveForwardToWord(char []str, int startIndex) {
        int len = str.length;
        int index = startIndex;
        
        while (index < len) {
            if (str[index] != ' ')
                return index;
            
            index++;
        }
        
        return -1;
    }
    
    public int moveForwardToWordEnd(char []str, int startIndex) {
        int len = str.length;
        int index = startIndex;
        
        boolean found = false;
        
        while (index < len) {
            if (str[index] == ' ')
                return index - 1;
            
            index++;
        }
        
        return len - 1;
    }
    
    public int drawChar(Graphics g, int c, int x, int y) {
        byte []data = m_data;
        short []charOffsets = m_charOffsets;
        
        int offset = c - 32;
        
        if (c >= 93)
            c = 20;
        
        int dataOffset = charOffsets[offset];
        int internalBlockCount = data[dataOffset++];
        int width = data[dataOffset++];
        
        g.setColor(m_foregroundOutlineColor);
        
        for (int i = 0; i < internalBlockCount; i++) {
            int mode = data[dataOffset++];
            int length = data[dataOffset++];
            
            int startPointX = x+data[dataOffset++];
            int startPointY = y+data[dataOffset++];
            
            length--;
            
            for (int j = 0; j < length; j++) {
                int endPointX = x+data[dataOffset++];
                int endPointY = y+data[dataOffset++];
                
                g.drawLine(startPointX, startPointY, endPointX, endPointY);
                
                startPointX = endPointX;
                startPointY = endPointY;
            }
        }
        
        return width;
    }
    
    public int getWidth(char []str) {
        return getWidth(str, 0, str.length - 1);
    }
    
    public int getWidth(char []str, int startIndex, int endIndex) {
        byte []data = m_data;
        short []charOffsets = m_charOffsets;
        
        int len = str.length;
        int width = 0;
        
        for (int i=startIndex; i <= endIndex; i++)
            width += data[charOffsets[str[i] - 32] + 1];
        
        return width;
    }
    
    public int getWWHeight(char []str, int maxWidth) {
        byte []data = m_data;
        short []charOffsets = m_charOffsets;
        
        int len = str.length;
        int width = 0;
        int height = m_height;
        int origHeight = m_height;
        boolean contentAdded = false;
        
        for (int i=0; i < len; i++) {
            contentAdded = true;
            int charWidth = data[charOffsets[str[i] - 32] + 1];
            width += charWidth;
            
            if (width > maxWidth) {
                width = charWidth;
                height += origHeight;
                contentAdded = false;
            }
        }
        
        if (!contentAdded)
            height -= origHeight;
        
        return height;
    }
    
    public int getRealWWVisibleLength(char []str, int startIndex, int termIndex, int width, int maxLineNumbers) {
        int lineNumber = 0;
        
        byte []data = m_data;
        short []charOffsets = m_charOffsets;
        int origHeight = m_height;
        
        int originalStartIndex = startIndex;
        int spaceLeft = width;
        int spaceWidth = data[charOffsets[' ' - 32] + 1];
        
        int len = str.length;
        
        {
            while (startIndex < len) {
                if (str[startIndex] != ' ')
                    break;
                
                startIndex++;
            }
            
            if (startIndex >= len)
                startIndex = -1;
        }
        
        if (startIndex == -1) {
            return 0;
        }
        
        while (lineNumber != maxLineNumbers) {
            int endIndex = 0;
            
            {
                int index = startIndex;
                
                while (index < len) {
                    if (str[index] == ' ') {
                        endIndex = index - 1;
                        break;
                    }
                    
                    index++;
                }
                
                if (index >= len)
                    endIndex = len - 1;
            }
            
            int wordWidth = 0;
            
            {
                for (int i=startIndex; i <= endIndex; i++)
                    wordWidth += data[charOffsets[str[i] - 32] + 1];
            }
            
            if (wordWidth > spaceLeft) {
                if (spaceLeft != width) {
                    lineNumber++;
                    spaceLeft = width;
                }
                
                if (lineNumber == maxLineNumbers)
                    break;
                
                if (wordWidth > width) {
                    int currentWidth = 0;
                    
                    while (true) {
                        char c = str[startIndex];
                        
                        if (startIndex > termIndex)
                            return startIndex;
                        
                        int charWidth = data[charOffsets[c - 32] + 1];
                        
                        if (charWidth + currentWidth >= width)
                            break;
                        
                        startIndex++;
                        currentWidth += charWidth;
                    }
                    
                    lineNumber++;
                    
                    continue;
                } else {
                    while (startIndex <= endIndex) {
                        char c = str[startIndex++];
                        
                        if (startIndex > termIndex)
                            return startIndex;
                        
                        int charWidth = data[charOffsets[c - 32] + 1];
                        
                        spaceLeft -= charWidth;
                    }
                    
                    spaceLeft -= spaceWidth;
                }
            } else {
                while (startIndex <= endIndex) {
                    char c = str[startIndex++];
                        
                    if (startIndex > termIndex)
                        return startIndex;
                    
                    int charWidth = data[charOffsets[c - 32] + 1];
                    
                    spaceLeft -= charWidth;
                }
                
                spaceLeft -= spaceWidth;
            }
            
            int oldStartIndex = startIndex;
            
            {
                startIndex = endIndex + 1;
                
                while (startIndex < len) {
                    if (str[startIndex] != ' ')
                        break;
                    
                    startIndex++;
                }
                
                if (startIndex >= len)
                    startIndex = -1;
            }
            
            if ((startIndex == -1) || (startIndex > termIndex)) {
                startIndex = oldStartIndex;
                break;
            }
        }
        
        return startIndex - originalStartIndex;
    }
    
    public int getRealWWLineCount(char []str, int startIndex, int termIndex, int width) {
        int lineNumber = 0;
        
        byte []data = m_data;
        short []charOffsets = m_charOffsets;
        
        int spaceLeft = width;
        int spaceWidth = data[charOffsets[' ' - 32] + 1];
        
        int len = str.length;
        
        {
            while (startIndex < len) {
                if (str[startIndex] != ' ')
                    break;
                
                startIndex++;
            }
            
            if (startIndex >= len)
                startIndex = -1;
        }
        
        if (startIndex == -1)
            return 0;
        
        while (startIndex <= termIndex) {
            int endIndex = 0;
            
            {
                int index = startIndex;
                
                while (index < len) {
                    if (str[index] == ' ') {
                        endIndex = index - 1;
                        break;
                    }
                    
                    index++;
                }
                
                if (index >= len)
                    endIndex = len - 1;
            }
            
            int wordWidth = 0;
            
            {
                for (int i=startIndex; i <= endIndex; i++)
                    wordWidth += data[charOffsets[str[i] - 32] + 1];
            }
            
            if (wordWidth > spaceLeft) {
                if (spaceLeft != width) {
                    lineNumber++;
                    spaceLeft = width;
                }
                
                if (wordWidth > width) {
                    int currentWidth = 0;
                    
                    while (true) {
                        char c = str[startIndex];
                        
                        if (startIndex > termIndex)
                            return lineNumber;
                        
                        int charWidth = data[charOffsets[c - 32] + 1];;
                        
                        if (charWidth + currentWidth >= width)
                            break;
                        
                        startIndex++;
                        currentWidth += charWidth;
                    }
                    
                    lineNumber++;
                    
                    continue;
                } else {
                    while (startIndex <= endIndex) {
                        char c = str[startIndex++];
                        
                        if (startIndex > termIndex)
                            return lineNumber;
                        
                        int charWidth = data[charOffsets[c - 32] + 1];;
                        
                        spaceLeft -= charWidth;
                    }
                    
                    spaceLeft -= spaceWidth;
                }
            } else {
                while (startIndex <= endIndex) {
                    char c = str[startIndex++];
                    
                    if (startIndex > termIndex)
                        return lineNumber;
                    
                    int charWidth = data[charOffsets[c - 32] + 1];
                    
                    spaceLeft -= charWidth;
                }
                
                spaceLeft -= spaceWidth;
            }
            
            {
                startIndex = endIndex + 1;
                
                while (startIndex < len) {
                    if (str[startIndex] != ' ')
                        break;
                    
                    startIndex++;
                }
                
                if (startIndex >= len)
                    startIndex = -1;
            }
            
            if ((startIndex == -1) || (startIndex > termIndex))
                break;
        }
        
        return lineNumber;
    }
    
    public int getWidth(char c) {
        return m_data[m_charOffsets[c - 32] + 1];
    }
}
