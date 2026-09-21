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

public class cydASMOpcodeMnemonic {
    public static final int INTERNAL_BYTE = 0;
    public static final int INTERNAL_SHORT = 1;
    public static final int INTERNAL_INT = 2;
    public static final int INTERNAL_LONG = 3;
    public static final int INTERNAL_STRING = 4;
    
    public static final int INDEX_BYTE = 0;
    public static final int INDEX_SHORT = 1;
    public static final int INDEX_INT = 2;
    public static final int INDEX_LONG = 3;
    public static final int INDEX_OBJECT = 4;
    public static final int INDEX_OPERAND = 5;
    
    private int []m_internalExpectations;
    private int []m_opcodeExpectations;
    
    private String m_longName;
    private String m_shortName;
    private byte []m_numericalOpcode;
    
    public cydASMOpcodeMnemonic(byte []opcode, String longName, String shortName, int []internalExpectations, int []opcodeExpectations) {
        if (longName.equals(shortName))
            throw new RuntimeException("Shortname and longname cannot be the same");
        
        if (!checkName(longName) || !checkName(shortName))
            throw new RuntimeException("No spaces allowed in longname or shortname");
        
        m_longName = longName;
        m_shortName = shortName;
        m_internalExpectations = internalExpectations;
        m_opcodeExpectations = opcodeExpectations;
        m_numericalOpcode = opcode;
    }
    
    private static boolean checkName(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isWhitespace(s.charAt(i)))
                return false;
        }
        
        return true;
    }
    
    public int getInternalExpectationsCount() {
        return m_internalExpectations.length;
    }
    
    public int getOpcodeExpectationsCount() {
        return m_opcodeExpectations.length;
    }
    
    public int getInternalExpectationType(int pos) {
        return m_internalExpectations[pos];
    }
    
    public int getOpcodeExpectationType(int pos) {
        return m_opcodeExpectations[pos];
    }
    
    public String getLongName() {
        return m_longName;
    }
    
    public String getShortName() {
        return m_shortName;
    }
    
    public byte []getOpcode() {
        return m_numericalOpcode;
    }
}
