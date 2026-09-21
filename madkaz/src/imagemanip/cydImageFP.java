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

package imagemanip;

public final class cydImageFP {
    public static int castToFP(int intValue) {
        return intValue << 16;
    }
    
    public static int castToInt(int fpValue) {
        return fpValue >> 16;
    }    
    
    public static int addFP(int lhs, int rhs) {
        return lhs + rhs;
    }
    
    public static int subFP(int lhs, int rhs) {
        return lhs - rhs;
    }
    
    public static int mulFP(int lhs, int rhs) {
        return (int)(((long)lhs * (long)rhs) >> 16);
    }
    
    public static int divFP(int lhs, int rhs) {
        long longLhs = ((long)lhs) << 16;
        
        return (int)(longLhs  / (long)rhs);
    }
}
