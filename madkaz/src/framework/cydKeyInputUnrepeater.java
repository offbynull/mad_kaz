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

public class cydKeyInputUnrepeater {
    public int m_lastKeyInput;
    
    public cydKeyInputUnrepeater() {
    }
    
    public int processKeyPress(int keyInput) {
        if (keyInput == m_lastKeyInput)
            return 0;
        
        m_lastKeyInput = keyInput;
        return keyInput;
    }
    
    public void reset() {
        m_lastKeyInput = 0;
    }
}
