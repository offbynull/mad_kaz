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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class cydIndexedCombinedReader {
    public byte []m_indexedCombinedDat;
    
    public cydIndexedCombinedReader(byte []indexedCombinedDat) {
        m_indexedCombinedDat = indexedCombinedDat;
    }
    
    public byte []get(int mainIndex, int subIndex) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(m_indexedCombinedDat);
            DataInputStream dis = new DataInputStream(bais);

            int headerLength = dis.readInt();
            int mainCount = dis.readInt();
            
            if (mainIndex >= mainCount)
                return null;
            
            for (int i = 0; i < mainIndex; i++) {
                int entryCount = dis.readInt();
                dis.skipBytes(entryCount * 8);
            }
                
            int entryCount = dis.readInt();
            
            if (subIndex >= entryCount)
                return null;
            
            dis.skipBytes(subIndex * 8);
            
            int offset = dis.readInt();
            int length = dis.readInt();
            
            byte []res = new byte[length];
            
            System.arraycopy(m_indexedCombinedDat, 4 + headerLength + offset, res, 0, length);
            
            return res;
        } catch (Throwable t) { }
        
        return null;
    }
}
