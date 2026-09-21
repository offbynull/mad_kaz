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

public final class cydTokenizer {
    public static int countTokens(String str, char delim) {
        int count = 0;
        int pos = 0;
        boolean found = false;
        
        while ((pos = str.indexOf(delim, pos)) != -1) {
            pos++;
            count++;
            
            found = true;
        }
        
        if (found)
            count++;
        
        return count;
    }
    
    public static String []tokenize(String str, char delim) {
        String []ret = new String[countTokens(str, delim)];

        int count = 0;
        int pos = 0;
        int newPos = 0;
        boolean found = false;
        
        while ((newPos = str.indexOf(delim, pos)) != -1) {
            ret[count] = str.substring(pos, newPos);
            
            pos = newPos + 1;
            count++;
            
            found = true;
        }
        
        if (found)
            ret[count] = str.substring(pos, str.length());
        
        return ret;
    }
}


