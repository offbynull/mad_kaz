/*
    Mad Kaz Assembler  Copyright 2007 CodeYield Development, Inc.  inquiries@codeyielddevelopment.com

    This file is part of Mad Kaz Assembler.

    Mad Kaz Assembler is free software; you can redistribute it and/or modify 
    it under the terms of the GNU General Public License as published by the 
    Free Software Foundation; either version 3 of the License, or (at your 
    option) any later version.

    Mad Kaz Assembler is distributed in the hope that it will be useful, but 
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
    or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
    more details.

    You should have received a copy of the GNU General Public License along 
    with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.HashMap;

public class cydNoKeyOverwriteHashMap extends HashMap {
    public Object put(Object key, Object value) {
        Object retValue;
        
        if (containsKey(key))
            throw new RuntimeException(key + " already exists in map");
        
        retValue = super.put(key, value);
        return retValue;
    }

}
