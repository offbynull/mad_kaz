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

public class cydKeyInputSlowDowner {
    public static final int MAX_TIME = 500;
    
    public int m_timeElapsed;
    public int m_lastKeyInput;
    
    public int processKeyPress(int keyInput, int timeSinceLastInvokation) {
        if (keyInput != m_lastKeyInput) {
            m_lastKeyInput = keyInput;
            m_timeElapsed = 0;
                        
            return keyInput;
        }
        
        m_timeElapsed += timeSinceLastInvokation;
        
        if (m_timeElapsed > MAX_TIME) {
            m_timeElapsed = 0;
            
            return m_lastKeyInput;
        }
        
        return 0;
    }
    
    public void reset() {
        m_timeElapsed = 0;
        m_lastKeyInput = 0;
    }
}
