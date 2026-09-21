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

public class cydPointInterpolator {
    public int []m_points;                  // -32,768 to 32,767
    public int []m_frameData;               // -32,768 to 32,767
    public int []m_frameTimeOffsets;        // -32,768 to 32,767
    public int m_frameCount;
    public int m_frameOn;
    public int m_timeElapsedOnCurrentFrame; // -32,768 to 32,767
    public boolean m_dontRepeat;
    public boolean m_done;
    
    public cydPointInterpolator(int pointCount, int []frameData, int []frameTimeOffsets, int frameOn, boolean dontRepeat) {
        m_frameData = frameData;
        m_frameTimeOffsets = frameTimeOffsets;
        
        m_points = new int[pointCount];
        
        m_frameCount = frameData.length / pointCount;
        m_frameOn = 0;
        
        System.arraycopy(frameData, m_frameOn * pointCount, m_points, 0, pointCount);
        
        m_dontRepeat = dontRepeat;
    }
    
    public void forceToFrame(int n) {
        if (n >= m_frameCount)
            return;
        
        m_timeElapsedOnCurrentFrame = 0;
        
        System.arraycopy(m_frameData, n * m_points.length, m_points, 0, m_points.length);
    }
    
    // interpolation function optimized to use localvariables. benchmark app shows local var access times are faster.
    public void interpolate(int timeElapsed) {
        if (m_done)
            return;
        
        int timeElapsedOnCurrentFrame = m_timeElapsedOnCurrentFrame;
        int []frameTimeOffsets = m_frameTimeOffsets;
        int frameOn = m_frameOn;
        int frameCount = m_frameCount;
        boolean dontRepeat = m_dontRepeat;
        
        timeElapsedOnCurrentFrame += timeElapsed;
        
        int lastDrawnFrame = frameOn;
        
        while (timeElapsedOnCurrentFrame >= frameTimeOffsets[frameOn]) {
            frameOn++;
            timeElapsedOnCurrentFrame -= frameTimeOffsets[frameOn-1];
            
            if (frameOn == frameCount) {
                frameOn = 0;
                
                if (dontRepeat) {
                    forceToFrame(frameCount-1);
                    m_done = true;
                    
                    m_timeElapsedOnCurrentFrame = timeElapsedOnCurrentFrame;
                    m_frameOn = frameOn;
                    
                    return;
                }
            }
        }
        
        int ratio = ((timeElapsedOnCurrentFrame << 16) / frameTimeOffsets[frameOn]);
        
        int []points = m_points;
        
        int len = points.length;
        
        int startSet = frameOn;
        int endSet = frameOn + 1;
        if (endSet >= frameCount) {
            if (dontRepeat) {
                forceToFrame(frameCount-1);
                m_done = true;
                
                m_timeElapsedOnCurrentFrame = timeElapsedOnCurrentFrame;
                m_frameOn = frameOn;
                
                return;
            }
            
            endSet = 0;
        }
        
        int []frameData = m_frameData;
        
        for (int i = 0; i < len; i++) {
            int startPoint = frameData[startSet*len + i];
            int endPoint = frameData[endSet*len + i];        
            
            int diffPoint = (endPoint - startPoint) << 16;
            
            points[i] = startPoint + (int)(((long)diffPoint * (long)ratio) >> 32);
        }
        
        
        
        m_timeElapsedOnCurrentFrame = timeElapsedOnCurrentFrame;
        m_frameOn = frameOn;
    }
    
    public int []getPoints() {
        return m_points;
    }
    
    public boolean isDone() {
        return m_done;
    }
    
    public boolean isDontRepeat() {
        return m_dontRepeat;
    }
    
    public void setDontRepeat(boolean b) {
        m_dontRepeat = b;
        m_done = b;
    }
    
    public void resetDone() {
        m_done = false;
    }
    
    public void restart() {
        m_done = false;
        forceToFrame(0);
    }
    
    public int getCurrentFrame() {
        return m_frameOn;
    }
}
