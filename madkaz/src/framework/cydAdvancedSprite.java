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

public class cydAdvancedSprite extends Sprite {
    public int [][]m_blockWaitTimes;
    public int [][]m_blockSequences;
    public int m_blockLength;
    public int m_blockIndex;
    
    public int []m_currentFrameWaitTimes;
    public int []m_currentFrameSequences;
    
    public int m_currentFrameLength;
    
    public int m_lastTime = 0;
    public int m_index = 0;
    
    public cydAdvancedSprite(Image image, int frameWidth, int frameHeight, int [][]sequence, int [][]waitTimes) {
        super(image, frameWidth, frameHeight);
        
        setFrameSequence(sequence, waitTimes);
    }
    
    public boolean setFrameSequence(int [][]sequence, int [][]waitTimes) {
        m_blockIndex = 0;
        
        if (waitTimes.length != sequence.length)
            return false;
        
        int len = sequence.length;
        
        for (int i = 0; i < len; i++) {
            if (waitTimes[i].length != sequence[i].length)
                return false;
        }
        
        m_blockLength = len;
        
        m_blockWaitTimes = waitTimes;
        m_blockSequences = sequence;
        
        m_blockIndex = -1;
        
        nextBlock();
        
        return true;
    }
    
    public boolean nextBlock() {
        int nextBlock = m_blockIndex + 1;
        
        if (nextBlock == m_blockLength) {
            m_index = 0;
            
            return false;
        }
        
        int []waitTimes = m_blockWaitTimes[nextBlock];
        int []sequences = m_blockSequences[nextBlock];
        
        m_blockIndex = nextBlock;
        
        switchToSequence(sequences, waitTimes);
        
        return true;
    }
    
    private void switchToSequence(int []sequence, int []waitTimes) {
        super.setFrameSequence(sequence);
        m_currentFrameSequences = sequence;
        m_currentFrameWaitTimes = waitTimes;
        
        //super.setFrame(0); // uncommenting this line causes bug on mot
        m_index = 0;
        m_lastTime = 0;
        m_currentFrameLength = sequence.length;
    }
    
    public void play(int timeDifference) {
        int waitTime = m_currentFrameWaitTimes[m_index];
        m_lastTime += timeDifference;
        
        if (m_lastTime > waitTime) {
            m_index++;
            
            //System.out.println(m_lastTime);
            
            if (m_index >= m_currentFrameLength) {
                if (!nextBlock())
                    nextFrame();
            } else {
                nextFrame();
            }
            
            m_lastTime = 0;
        }
    }
}
