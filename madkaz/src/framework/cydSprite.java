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

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class cydSprite extends Sprite {
    public int []m_frameWaitTimes;
    public int m_lastTime = 0;
    public int m_index = 0;

    public cydSprite(cydSprite other) {
        super(other);
        
        m_frameWaitTimes = new int[other.m_frameWaitTimes.length];
        System.arraycopy(other.m_frameWaitTimes, 0, m_frameWaitTimes, 0, m_frameWaitTimes.length);
        
        m_lastTime = other.m_lastTime;
        m_index = other.m_index;
    }
    
    public cydSprite(Image image, int []sequence, int []waitTimes) {
        super(image);
        
        // sequence = new int[0];
        
        setFrameSequence(sequence, waitTimes);
    }
    
    public cydSprite(Image image, int frameWidth, int frameHeight, int []sequence, int []waitTimes) {
        super(image, frameWidth, frameHeight);
        
        if (sequence == null) {
            int frameCount = image.getWidth() / frameWidth * image.getHeight() / frameHeight; 
            sequence = new int[frameCount];
            
            for (int i = 0; i < frameCount; i++)
                sequence[i] = i;
        }
        
        setFrameSequence(sequence, waitTimes);
    }
    
    public void setFrameSequence(int []sequence, int []waitTimes) {
        super.setFrameSequence(sequence);
        m_frameWaitTimes = waitTimes;
        
        //super.setFrame(0); // uncommenting this line causes bug on mot
        m_index = 0;
        m_lastTime = 0;
    }
    
    public void setFrameSequence(int []sequence) {
        super.setFrameSequence(sequence);
        
        m_index = 0;
        m_lastTime = 0;
    }
    
    public void setWaitTimes(int []waitTimes) {
        m_frameWaitTimes = waitTimes;
        
        m_lastTime = 0;
    }
    
    public void play(int timeDifference) {
        if (m_frameWaitTimes == null) {
            nextFrame();
            return;
        }
        
        int waitTime = m_frameWaitTimes[m_index];
        m_lastTime += timeDifference;
        
        if (m_lastTime > waitTime) {
            nextFrame();
            
            m_index++;
            
            if (m_index >= m_frameWaitTimes.length)
                m_index = 0;
            
            m_lastTime = 0;
        }
    }
}
