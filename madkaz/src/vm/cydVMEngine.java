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

package vm;

import framework.cydGraphicsManager;
import framework.cydMusicPlayer;
import framework.cydTokenizer;
import java.io.DataInputStream;
import java.util.Hashtable;
import game.cydGamePlayCanvas;
import java.io.ByteArrayInputStream;
import javax.microedition.lcdui.Display;



/* Changes from original VM:
 *
 *  1: Changed INC operations to directly operate on source stack item, rather than create a new stack item.
 *
 */



public final class cydVMEngine {
    public static int m_randSeed = 0;
    
    private static final String VERSION_DATA = "0.2GAM";
    
    public static int rand() {
        int res = m_randSeed * 214013 + 2531011;
        m_randSeed = res;
        
        return (res >> 16) & 0x7fff;
    }
    
    public static int findEntryPoint(byte []varData) {
        int startOfVMSection = ((varData[12] << 24) | ((varData[13] & 0xFF) << 16) | ((varData[14] &0xFF) << 8) | (varData[15] & 0xFF));
        
        return ((varData[startOfVMSection] << 24) | ((varData[startOfVMSection + 1] & 0xFF) << 16) | ((varData[startOfVMSection + 2] &0xFF) << 8) | (varData[startOfVMSection + 3] & 0xFF));
    }
    
    public static boolean isVMBlock(byte []varData) {
        return ((varData[0] << 24) | ((varData[1] &0xFF) << 16) | ((varData[2] &0xFF) << 8) | (varData[3] & 0xFF)) == 0xCEEEDEEE;
    }
    
    // returns resource offset - 4 (int size)
    public static int getResourceOffset(byte []varData, int id) {
        int startOfResSection = ((varData[8] << 24) | ((varData[9] & 0xFF) << 16) | ((varData[10] &0xFF) << 8) | (varData[11] & 0xFF));
        
        int offset = startOfResSection;
        
        int numOfRes = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
        
        for (int i = 0; i < numOfRes; i++) {
            int foundId = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
            
            if (foundId == id)
                return offset;
            
            int size = ((varData[offset++] << 24) | ((varData[offset++] & 0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
            offset += size;
        }
        
        return -1;
    }
    
    public static void run(cydGamePlayCanvas game, byte []varData, Hashtable globalMap, int offset, int byteMax, int shortMax, int operandMax, int intMax, int longMax, int objectMax, int callMax, int trapMax) {
        run(game, varData, globalMap, offset, new byte[byteMax], new short[shortMax], new short[operandMax], new int[intMax], new long[longMax], new Object[objectMax], new short[callMax * 15], new short[trapMax * 16], new String[trapMax], -1, -1, -1, -1, -1, -1);
    }
    
    public static void run(cydGamePlayCanvas game, byte []varData, Hashtable globalMap, int offset, byte []stackByte, short []stackShort, short []stackOperand, int []stackInt, long []stackLong, Object []stackObject, short []stackCall, short []stackTrapData, String []stackTrapType, int stackByteTop, int stackShortTop, int stackOperandTop, int stackIntTop, int stackLongTop, int stackObjectTop) {
        boolean condition = false;
        
        int stackByteCSTop = 0;
        int stackShortCSTop = 0;
        int stackOperandCSTop = 0;
        int stackIntCSTop = 0;
        int stackLongCSTop = 0;
        int stackObjectCSTop = 0;
        
        int stackCallTop = -1;
        int stackTrapDataTop = -1;
        int stackTrapTypeTop = -1;
        
        cydByteArrayInputStream bais = new cydByteArrayInputStream(varData);
        DataInputStream dis = new DataInputStream(bais);
        
        byte retByte = 0;
        short retShort = 0;
        int retInt = 0;
        long retLong = 0;
        Object retObject = null;
        
        while (true) {
            try {
                while (true) {
                    int opcode = varData[offset++];
                    
                    switch (opcode) {
                        case cydVMOpcodeList.OP_CONST_BYTE: {
                            stackByteTop++;
                            stackByte[stackByteTop + stackByteCSTop] = (byte)varData[offset++];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONST_SHORT: {
                            stackShortTop++;
                            stackShort[stackShortTop + stackShortCSTop] = (short)((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                        }
                        break;
                        case cydVMOpcodeList.OP_CONST_INT: {
                            stackIntTop++;
                            stackInt[stackIntTop + stackIntCSTop] = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                        }
                        break;
                        case cydVMOpcodeList.OP_CONST_LONG: {
                            stackLongTop++;
                            stackLong[stackLongTop + stackLongCSTop] = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                        }
                        break;
                        case cydVMOpcodeList.OP_CONST_OPERAND: {
                            stackOperandTop++;
                            stackOperand[stackOperandTop + stackOperandCSTop] = (short)((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                        }
                        break;
                        case cydVMOpcodeList.OP_CONST_STRING: {
                            stackObjectTop++;
                            bais.setPosition(offset);
                            stackObject[stackObjectTop + stackObjectCSTop] = dis.readUTF();
                            offset = bais.getPosition();
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_NEW_BYTE_ARRAY: {
                            stackObject[(++stackObjectTop) + stackObjectCSTop] = new byte[stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]]];
                        }
                        break;
                        case cydVMOpcodeList.OP_NEW_SHORT_ARRAY: {
                            stackObject[(++stackObjectTop) + stackObjectCSTop] = new short[stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]]];
                        }
                        break;
                        case cydVMOpcodeList.OP_NEW_INT_ARRAY: {
                            stackObject[(++stackObjectTop) + stackObjectCSTop] = new int[stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]]];
                        }
                        break;
                        case cydVMOpcodeList.OP_NEW_LONG_ARRAY: {
                            stackObject[(++stackObjectTop) + stackObjectCSTop] = new long[stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]]];
                        }
                        break;
                        case cydVMOpcodeList.OP_NEW_OBJECT_ARRAY: {
                            stackObject[(++stackObjectTop) + stackObjectCSTop] = new Object[stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]]];
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_POP_BYTE: {
                            stackByteTop--;
                        }
                        break;
                        case cydVMOpcodeList.OP_POP_SHORT: {
                            stackShortTop--;
                        }
                        break;
                        case cydVMOpcodeList.OP_POP_INT: {
                            stackIntTop--;
                        }
                        break;
                        case cydVMOpcodeList.OP_POP_LONG: {
                            stackLongTop--;
                        }
                        break;
                        case cydVMOpcodeList.OP_POP_OPERAND: {
                            stackOperandTop--;
                        }
                        break;
                        case cydVMOpcodeList.OP_POP_OBJECT: {
                            stackObject[stackObjectTop + stackObjectCSTop] = null;
                            stackObjectTop--;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_CONV_BYTE_TO_SHORT: {
                            stackShort[(++stackShortTop) + stackShortCSTop] = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_BYTE_TO_INT: {
                            stackInt[(++stackIntTop) + stackIntCSTop] = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_BYTE_TO_LONG: {
                            stackLong[(++stackLongTop) + stackLongCSTop] = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_BYTE_TO_OPERAND: {
                            stackOperand[(++stackOperandTop) + stackOperandCSTop] = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_SHORT_TO_BYTE: {
                            stackByte[(++stackByteTop) + stackByteCSTop] = (byte)stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_SHORT_TO_INT: {
                            stackInt[(++stackIntTop) + stackIntCSTop] = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_SHORT_TO_LONG: {
                            stackLong[(++stackLongTop) + stackLongCSTop] = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_SHORT_TO_OPERAND: {
                            stackOperand[(++stackOperandTop) + stackOperandCSTop] = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_INT_TO_BYTE: {
                            stackByte[(++stackByteTop) + stackByteCSTop] = (byte)stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_INT_TO_SHORT: {
                            stackShort[(++stackShortTop) + stackShortCSTop] = (short)stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_INT_TO_LONG: {
                            stackLong[(++stackLongTop) + stackLongCSTop] = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_INT_TO_OPERAND: {
                            stackOperand[(++stackOperandTop) + stackOperandCSTop] = (short)stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_LONG_TO_BYTE: {
                            stackByte[(++stackByteTop) + stackByteCSTop] = (byte)stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_LONG_TO_SHORT: {
                            stackShort[(++stackShortTop) + stackShortCSTop] = (short)stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_LONG_TO_INT: {
                            stackInt[(++stackIntTop) + stackIntCSTop] = (int)stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_LONG_TO_OPERAND: {
                            stackOperand[(++stackOperandTop) + stackOperandCSTop] = (short)stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_OPERAND_TO_BYTE: {
                            stackByte[(++stackByteTop) + stackByteCSTop] = (byte)stackOperand[stackOperandCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_OPERAND_TO_SHORT: {
                            stackShort[(++stackShortTop) + stackShortCSTop] = stackOperand[stackOperandCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_OPERAND_TO_INT: {
                            stackInt[(++stackIntTop) + stackIntCSTop] = stackOperand[stackOperandCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONV_OPERAND_TO_LONG: {
                            stackLong[(++stackLongTop) + stackLongCSTop] = stackOperand[stackOperandCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_SUB_ASSIGN_BYTE: {
                            byte []array = (byte [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte dataToPlace = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int index = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            
                            array[index] = dataToPlace;
                        }
                        break;
                        case cydVMOpcodeList.OP_SUB_ASSIGN_SHORT: {
                            short []array = (short [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short dataToPlace = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int index = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            
                            array[index] = dataToPlace;
                        }
                        break;
                        case cydVMOpcodeList.OP_SUB_ASSIGN_INT: {
                            int []array = (int [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int dataToPlace = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int index = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            
                            array[index] = dataToPlace;
                        }
                        break;
                        case cydVMOpcodeList.OP_SUB_ASSIGN_LONG: {
                            long []array = (long [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long dataToPlace = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int index = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            
                            array[index] = dataToPlace;
                        }
                        break;
                        case cydVMOpcodeList.OP_SUB_ASSIGN_OBJECT: {
                            Object []array = (Object [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            Object dataToPlace = stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int index = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            
                            array[index] = dataToPlace;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_SUB_EXTRACT_BYTE: {
                            byte []array = (byte [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int index = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            
                            stackByteTop++;
                            stackByte[stackByteTop + stackByteCSTop] = array[index];
                        }
                        break;
                        case cydVMOpcodeList.OP_SUB_EXTRACT_SHORT: {
                            short []array = (short [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int index = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            
                            stackShortTop++;
                            stackShort[stackShortTop + stackShortCSTop] = array[index];
                        }
                        break;
                        case cydVMOpcodeList.OP_SUB_EXTRACT_INT: {
                            int []array = (int [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int index = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            
                            stackIntTop++;
                            stackInt[stackIntTop + stackIntCSTop] = array[index];
                        }
                        break;
                        case cydVMOpcodeList.OP_SUB_EXTRACT_LONG: {
                            long []array = (long [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int index = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            
                            stackLongTop++;
                            stackLong[stackLongTop + stackLongCSTop] = array[index];
                        }
                        break;
                        case cydVMOpcodeList.OP_SUB_EXTRACT_OBJECT: {
                            Object []array = (Object [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int index = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            
                            stackObjectTop++;
                            stackObject[stackObjectTop + stackObjectCSTop] = array[index];
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_SIZE_OF: {
                            Object operand1 = stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            
                            if (operand1 instanceof byte[])
                                stackInt[(++stackIntTop) + stackIntCSTop] = ((byte[])operand1).length;
                            else if (operand1 instanceof short[])
                                stackInt[(++stackIntTop) + stackIntCSTop] = ((short[])operand1).length;
                            else if (operand1 instanceof int[])
                                stackInt[(++stackIntTop) + stackIntCSTop] = ((int[])operand1).length;
                            else if (operand1 instanceof long[])
                                stackInt[(++stackIntTop) + stackIntCSTop] = ((long[])operand1).length;
                            else if (operand1 instanceof Object[])
                                stackInt[(++stackIntTop) + stackIntCSTop] = ((Object[])operand1).length;
                            else if (operand1 instanceof String)
                                stackInt[(++stackIntTop) + stackIntCSTop] = ((String)operand1).length();
                            else
                                throw new ClassCastException("sizeof operator called on unknown object");
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_OBJECT_PUSH_NULL: {
                            stackObjectTop++;
                            stackObject[stackObjectTop + stackObjectCSTop] = null;
                        }
                        break;
                        case cydVMOpcodeList.OP_OBJECT_EQUALS_NULL: {
                            Object operand1 = stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand1 == null ? true : false);
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_CONTROL_CALL: {
                            int returnAddress = offset + 10;
                            
                            int newStackByteSubAmount = varData[offset++];
                            int newStackShortSubAmount = varData[offset++];
                            int newStackIntSubAmount = varData[offset++];
                            int newStackLongSubAmount = varData[offset++];
                            int newStackObjectSubAmount = varData[offset++];
                            int newStackOperandSubAmount = varData[offset++];
                            
                            stackCall[++stackCallTop] = (short)(returnAddress>>16);
                            stackCall[++stackCallTop] = (short)(returnAddress);
                            stackCall[++stackCallTop] = (short)(stackByteCSTop);
                            stackCall[++stackCallTop] = (short)(stackShortCSTop);
                            stackCall[++stackCallTop] = (short)(stackIntCSTop);
                            stackCall[++stackCallTop] = (short)(stackLongCSTop);
                            stackCall[++stackCallTop] = (short)(stackObjectCSTop);
                            stackCall[++stackCallTop] = (short)(stackOperandCSTop);
                            stackCall[++stackCallTop] = (short)(stackByteTop - newStackByteSubAmount);
                            stackCall[++stackCallTop] = (short)(stackShortTop - newStackShortSubAmount);
                            stackCall[++stackCallTop] = (short)(stackIntTop - newStackIntSubAmount);
                            stackCall[++stackCallTop] = (short)(stackLongTop - newStackLongSubAmount);
                            stackCall[++stackCallTop] = (short)(stackObjectTop - newStackObjectSubAmount);
                            stackCall[++stackCallTop] = (short)(stackOperandTop - newStackOperandSubAmount);
                            stackCall[++stackCallTop] = (short)(condition ? 1 : 0);
                            
                            int callAddress = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            offset = callAddress;
                            
                            stackByteCSTop += stackByteTop + 1 - newStackByteSubAmount;
                            stackShortCSTop += stackShortTop + 1 - newStackShortSubAmount;
                            stackIntCSTop += stackIntTop + 1 - newStackIntSubAmount;
                            stackLongCSTop += stackLongTop + 1 - newStackLongSubAmount;
                            stackObjectCSTop += stackObjectTop + 1 - newStackObjectSubAmount;
                            stackOperandCSTop += stackOperandTop + 1 - newStackOperandSubAmount;
                            
                            stackByteTop = -1 + newStackByteSubAmount;
                            stackShortTop = -1 + newStackShortSubAmount;
                            stackIntTop = -1 + newStackIntSubAmount;
                            stackLongTop = -1 + newStackLongSubAmount;
                            stackObjectTop = -1 + newStackObjectSubAmount;
                            stackOperandTop = -1 + newStackOperandSubAmount;
                        }
                        break;
                        case cydVMOpcodeList.OP_CONTROL_SEMI_DYNAMIC_CALL: {
                            int returnAddress = offset + 6;
                            
                            int newStackByteSubAmount = varData[offset++];
                            int newStackShortSubAmount = varData[offset++];
                            int newStackIntSubAmount = varData[offset++];
                            int newStackLongSubAmount = varData[offset++];
                            int newStackObjectSubAmount = varData[offset++];
                            int newStackOperandSubAmount = varData[offset++];
                            
                            stackCall[++stackCallTop] = (short)(returnAddress>>16);
                            stackCall[++stackCallTop] = (short)(returnAddress);
                            stackCall[++stackCallTop] = (short)(stackByteCSTop);
                            stackCall[++stackCallTop] = (short)(stackShortCSTop);
                            stackCall[++stackCallTop] = (short)(stackIntCSTop);
                            stackCall[++stackCallTop] = (short)(stackLongCSTop);
                            stackCall[++stackCallTop] = (short)(stackObjectCSTop);
                            stackCall[++stackCallTop] = (short)(stackOperandCSTop);
                            stackCall[++stackCallTop] = (short)(stackByteTop - newStackByteSubAmount);
                            stackCall[++stackCallTop] = (short)(stackShortTop - newStackShortSubAmount);
                            stackCall[++stackCallTop] = (short)(stackIntTop - newStackIntSubAmount);
                            stackCall[++stackCallTop] = (short)(stackLongTop - newStackLongSubAmount);
                            stackCall[++stackCallTop] = (short)(stackObjectTop - newStackObjectSubAmount);
                            stackCall[++stackCallTop] = (short)(stackOperandTop - newStackOperandSubAmount);
                            stackCall[++stackCallTop] = (short)(condition ? 1 : 0);
                            
                            int callAddress = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            offset = callAddress;
                            
                            stackByteCSTop += stackByteTop + 1 - newStackByteSubAmount;
                            stackShortCSTop += stackShortTop + 1 - newStackShortSubAmount;
                            stackIntCSTop += stackIntTop + 1 - newStackIntSubAmount;
                            stackLongCSTop += stackLongTop + 1 - newStackLongSubAmount;
                            stackObjectCSTop += stackObjectTop + 1 - newStackObjectSubAmount;
                            stackOperandCSTop += stackOperandTop + 1 - newStackOperandSubAmount;
                            
                            stackByteTop = -1 + newStackByteSubAmount;
                            stackShortTop = -1 + newStackShortSubAmount;
                            stackIntTop = -1 + newStackIntSubAmount;
                            stackLongTop = -1 + newStackLongSubAmount;
                            stackObjectTop = -1 + newStackObjectSubAmount;
                            stackOperandTop = -1 + newStackOperandSubAmount;
                        }
                        break;
                        case cydVMOpcodeList.OP_CONTROL_FULL_DYNAMIC_CALL: {
                            int returnAddress = offset;
                            
                            int callAddress = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]]; // operand 7
                            offset = callAddress;
                            
                            int newStackOperandSubAmount = stackShort[stackShortCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]]; // operand 6
                            int newStackObjectSubAmount = stackShort[stackShortCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]]; // operand 5
                            int newStackLongSubAmount = stackShort[stackShortCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]]; // operand 4
                            int newStackIntSubAmount = stackShort[stackShortCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]]; // operand 3
                            int newStackShortSubAmount = stackShort[stackShortCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]]; // operand 2
                            int newStackByteSubAmount = stackShort[stackShortCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]]; // operand 1
                            
                            stackCall[++stackCallTop] = (short)(returnAddress>>16);
                            stackCall[++stackCallTop] = (short)(returnAddress);
                            stackCall[++stackCallTop] = (short)(stackByteCSTop);
                            stackCall[++stackCallTop] = (short)(stackShortCSTop);
                            stackCall[++stackCallTop] = (short)(stackIntCSTop);
                            stackCall[++stackCallTop] = (short)(stackLongCSTop);
                            stackCall[++stackCallTop] = (short)(stackObjectCSTop);
                            stackCall[++stackCallTop] = (short)(stackOperandCSTop);
                            stackCall[++stackCallTop] = (short)(stackByteTop - newStackByteSubAmount);
                            stackCall[++stackCallTop] = (short)(stackShortTop - newStackShortSubAmount);
                            stackCall[++stackCallTop] = (short)(stackIntTop - newStackIntSubAmount);
                            stackCall[++stackCallTop] = (short)(stackLongTop - newStackLongSubAmount);
                            stackCall[++stackCallTop] = (short)(stackObjectTop - newStackObjectSubAmount);
                            stackCall[++stackCallTop] = (short)(stackOperandTop - newStackOperandSubAmount);
                            stackCall[++stackCallTop] = (short)(condition ? 1 : 0);
                            
                            stackByteCSTop += stackByteTop + 1 - newStackByteSubAmount;
                            stackShortCSTop += stackShortTop + 1 - newStackShortSubAmount;
                            stackIntCSTop += stackIntTop + 1 - newStackIntSubAmount;
                            stackLongCSTop += stackLongTop + 1 - newStackLongSubAmount;
                            stackObjectCSTop += stackObjectTop + 1 - newStackObjectSubAmount;
                            stackOperandCSTop += stackOperandTop + 1 - newStackOperandSubAmount;
                            
                            stackByteTop = -1 + newStackByteSubAmount;
                            stackShortTop = -1 + newStackShortSubAmount;
                            stackIntTop = -1 + newStackIntSubAmount;
                            stackLongTop = -1 + newStackLongSubAmount;
                            stackObjectTop = -1 + newStackObjectSubAmount;
                            stackOperandTop = -1 + newStackOperandSubAmount;
                        }
                        break;
                        case cydVMOpcodeList.OP_CONTROL_RET: {
                            if (stackCallTop == -1) {
                                for (int i = 0; i <= stackObjectTop; i++)
                                    stackObject[i] = null;
                                
                                return;
                            }
                            
                            int objStart = stackObjectCSTop;
                            int objEnd = stackObjectCSTop + stackObjectTop;
                            
                            condition = (stackCall[stackCallTop--] != 0 ? true : false);
                            stackOperandTop = stackCall[stackCallTop--];
                            stackObjectTop = stackCall[stackCallTop--];
                            stackLongTop = stackCall[stackCallTop--];
                            stackIntTop = stackCall[stackCallTop--];
                            stackShortTop = stackCall[stackCallTop--];
                            stackByteTop = stackCall[stackCallTop--];
                            stackOperandCSTop = stackCall[stackCallTop--];
                            stackObjectCSTop = stackCall[stackCallTop--];
                            stackLongCSTop = stackCall[stackCallTop--];
                            stackIntCSTop = stackCall[stackCallTop--];
                            stackShortCSTop = stackCall[stackCallTop--];
                            stackByteCSTop = stackCall[stackCallTop--];
                            
                            int adPart1 = (stackCall[stackCallTop--] & 0xFFFF);
                            int adPart2 = (stackCall[stackCallTop--]<<16);
                            
                            for (int i = objStart; i <= objEnd; i++)
                                stackObject[i] = null;
                            
                            offset = adPart1 | adPart2;
                        }
                        break;
                        case cydVMOpcodeList.OP_CONTROL_JUMP: {
                            offset = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                        }
                        break;
                        case cydVMOpcodeList.OP_CONTROL_DYNAMIC_JUMP: {
                            offset = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONTROL_JUMP_CONDITIONAL: {
                            if (condition)
                                offset = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            else
                                offset += 4;
                        }
                        break;
                        case cydVMOpcodeList.OP_CONTROL_DYNAMIC_JUMP_CONDITIONAL: {
                            if (condition)
                                offset = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                        }
                        break;
                        case cydVMOpcodeList.OP_CONTROL_SET_TRAP: {
                            bais.setPosition(offset);
                            String throwableName = dis.readUTF();
                            offset = bais.getPosition();
                            
                            int trapAddress = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            stackTrapType[++stackTrapTypeTop] = throwableName;
                            
                            stackTrapData[++stackTrapDataTop] = (short)(trapAddress>>16);
                            stackTrapData[++stackTrapDataTop] = (short)(trapAddress);
                            stackTrapData[++stackTrapDataTop] = (short)(stackByteCSTop);
                            stackTrapData[++stackTrapDataTop] = (short)(stackShortCSTop);
                            stackTrapData[++stackTrapDataTop] = (short)(stackIntCSTop);
                            stackTrapData[++stackTrapDataTop] = (short)(stackLongCSTop);
                            stackTrapData[++stackTrapDataTop] = (short)(stackObjectCSTop);
                            stackTrapData[++stackTrapDataTop] = (short)(stackOperandCSTop);
                            stackTrapData[++stackTrapDataTop] = (short)(stackByteTop);
                            stackTrapData[++stackTrapDataTop] = (short)(stackShortTop);
                            stackTrapData[++stackTrapDataTop] = (short)(stackIntTop);
                            stackTrapData[++stackTrapDataTop] = (short)(stackLongTop);
                            stackTrapData[++stackTrapDataTop] = (short)(stackObjectTop);
                            stackTrapData[++stackTrapDataTop] = (short)(stackOperandTop);
                            stackTrapData[++stackTrapDataTop] = (short)(stackCallTop);
                            stackTrapData[++stackTrapDataTop] = (short)(condition ? 1 : 0);
                        }
                        break;
                        case cydVMOpcodeList.OP_CONTROL_THROW_TRAP: {
                            bais.setPosition(offset);
                            String throwableName = dis.readUTF();
                            offset = bais.getPosition();
                            
                            throw (Throwable)Class.forName(throwableName).newInstance();
                        }
                        case cydVMOpcodeList.OP_CONTROL_RELEASE_TRAP: {
                            stackTrapType[stackTrapTypeTop--] = null;
                            stackTrapDataTop -= 16;
                        }
                        break;
                        case cydVMOpcodeList.OP_CONTROL_EXIT:
                            return;
                            
                        case cydVMOpcodeList.OP_SYSTEM_ID_STRING: {
                            stackObjectTop++;
                            stackObject[stackObjectTop + stackObjectCSTop] = VERSION_DATA;
                        }
                        break;
                        case cydVMOpcodeList.OP_SYSTEM_TIME: {
                            stackLongTop++;
                            stackLong[stackLongTop + stackLongCSTop] = System.currentTimeMillis();
                        }
                        break;
                        case cydVMOpcodeList.OP_SYSTEM_OUT: {
                            String operand1 = (String)stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            System.out.println(operand1);
                        }
                        break;
                        case cydVMOpcodeList.OP_SYSTEM_SHARED: {
                            stackObjectTop++;
                            stackObject[stackObjectTop + stackObjectCSTop] = globalMap;
                        }
                        break;
                        case cydVMOpcodeList.OP_SYSTEM_GC: {
                            System.gc();
                            Runtime.getRuntime().gc();
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_COPY_BYTE: {
                            stackByteTop++;
                            stackByte[stackByteTop + stackByteCSTop] = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_COPY_SHORT: {
                            stackShortTop++;
                            stackShort[stackShortTop + stackShortCSTop] = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_COPY_INT: {
                            stackIntTop++;
                            stackInt[stackIntTop + stackIntCSTop] = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_COPY_LONG: {
                            stackLongTop++;
                            stackLong[stackLongTop + stackLongCSTop] = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_COPY_OPERAND: {
                            stackOperandTop++;
                            stackOperand[stackOperandTop + stackOperandCSTop] = stackOperand[stackOperandCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        case cydVMOpcodeList.OP_COPY_OBJECT: {
                            stackObjectTop++;
                            stackObject[stackObjectTop + stackObjectCSTop] = stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_GET_COND: {
                            stackByteTop++;
                            stackByte[stackByteTop + stackByteCSTop] = (byte)(condition ? 1 : 0);
                        }
                        break;
                        case cydVMOpcodeList.OP_SET_COND: {
                            byte data = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            condition = (data == 0 ? false : true);
                        }
                        break;
                        case cydVMOpcodeList.OP_NOT_COND: {
                            condition = !condition;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_MULTI_POP_BYTE: {
                            int amountToRemove = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackByteTop -= amountToRemove;
                        }
                        break;
                        case cydVMOpcodeList.OP_MULTI_POP_SHORT: {
                            int amountToRemove = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackShortTop -= amountToRemove;
                        }
                        break;
                        case cydVMOpcodeList.OP_MULTI_POP_INT: {
                            int amountToRemove = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackIntTop -= amountToRemove;
                        }
                        break;
                        case cydVMOpcodeList.OP_MULTI_POP_LONG: {
                            int amountToRemove = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackLongTop -= amountToRemove;
                        }
                        break;
                        case cydVMOpcodeList.OP_MULTI_POP_OPERAND: {
                            int amountToRemove = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackOperandTop -= amountToRemove;
                        }
                        break;
                        case cydVMOpcodeList.OP_MULTI_POP_OBJECT: {
                            int objEnd = stackObjectCSTop + stackObjectTop;
                            
                            int amountToRemove = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackObjectTop -= amountToRemove;
                            
                            int objStart = stackObjectCSTop + stackObjectTop;
                            
                            if (objStart != objEnd) {
                                for (int i = objStart + 1; i <= objEnd; i++)
                                    stackObject[i] = null;
                            }
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_RES_LENGTH: {
                            int operand1 = stackShort[stackShortCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            int resOffset = getResourceOffset(varData, operand1);
                            int length = ((varData[resOffset++] << 24) | ((varData[resOffset++] &0xFF) << 16) | ((varData[resOffset++] &0xFF) << 8) | (varData[resOffset++] & 0xFF));
                            
                            stackIntTop++;
                            stackInt[stackIntTop + stackIntCSTop] = length;
                        }
                        break;
                        case cydVMOpcodeList.OP_SYSTEM_DIRECT_ACCESS_VM_DATA_FILE: {
                            stackObjectTop++;
                            stackObject[stackObjectTop + stackObjectCSTop] = varData;
                        }
                        break;
                        case cydVMOpcodeList.OP_SYSTEM_DIRECT_LOAD_RES_POSITION: {
                            int operand1 = stackShort[stackShortCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                            int resOffset = getResourceOffset(varData, operand1);
                            int length = ((varData[resOffset++] << 24) | ((varData[resOffset++] &0xFF) << 16) | ((varData[resOffset++] &0xFF) << 8) | (varData[resOffset++] & 0xFF));
                            
                            byte []out = new byte[length];
                            
                            System.arraycopy(varData, resOffset, out, 0, length);
                            
                            stackObjectTop++;
                            stackObject[stackObjectTop + stackObjectCSTop] = out;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_SET_RET: {
                            int type = varData[offset++];
                            
                            switch (type) {
                                case 0:
                                    retByte = stackByte[stackByteCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    break;
                                case 1:
                                    retShort = stackShort[stackShortCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    break;
                                case 2:
                                    retInt = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    break;
                                case 3:
                                    retLong = stackLong[stackLongCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    break;
                                case 4:
                                    retObject = stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    break;
                                default:
                                    throw new RuntimeException("Bad return type");
                            }
                        }
                        break;
                        case cydVMOpcodeList.OP_GET_RET: {
                            int type = varData[offset++];
                            
                            switch (type) {
                                case 0:
                                    stackByteTop++;
                                    stackByte[stackByteTop + stackByteCSTop] = retByte;
                                    break;
                                case 1:
                                    stackShortTop++;
                                    stackShort[stackShortTop + stackShortCSTop] = retShort;
                                    break;
                                case 2:
                                    stackIntTop++;
                                    stackInt[stackIntTop + stackIntCSTop] = retInt;
                                    break;
                                case 3:
                                    stackLongTop++;
                                    stackLong[stackLongTop + stackLongCSTop] = retLong;
                                    break;
                                case 4:
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = retObject;
                                    break;
                                default:
                                    throw new RuntimeException("Bad return type");
                            }
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_ASSIGN_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))] = operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_ASSIGN_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))] = operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_ASSIGN_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))] = operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_ASSIGN_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))] = operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_ASSIGN_OPERAND: {
                            short operand2 = stackOperand[stackOperandCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            stackOperand[stackOperandCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))] = operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_ASSIGN_OBJECT: {
                            Object operand2 = stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))] = operand2;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_EQUALS_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand2 == operand1);
                        }
                        break;
                        case cydVMOpcodeList.OP_EQUALS_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand2 == operand1);
                        }
                        break;
                        case cydVMOpcodeList.OP_EQUALS_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand2 == operand1);
                        }
                        break;
                        case cydVMOpcodeList.OP_EQUALS_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand2 == operand1);
                        }
                        break;
                        case cydVMOpcodeList.OP_EQUALS_OBJECT: {
                            Object operand2 = stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            Object operand1 = stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand2 == operand1);
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_NOT_EQUAL_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand2 != operand1);
                        }
                        break;
                        case cydVMOpcodeList.OP_NOT_EQUAL_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand2 != operand1);
                        }
                        break;
                        case cydVMOpcodeList.OP_NOT_EQUAL_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand2 != operand1);
                        }
                        break;
                        case cydVMOpcodeList.OP_NOT_EQUAL_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand2 != operand1);
                        }
                        break;
                        case cydVMOpcodeList.OP_NOT_EQUAL_OBJECT: {
                            Object operand2 = stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            Object operand1 = stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand2 != operand1);
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_LESS_THAN_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand1 < operand2);
                        }
                        break;
                        case cydVMOpcodeList.OP_LESS_THAN_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand1 < operand2);
                        }
                        break;
                        case cydVMOpcodeList.OP_LESS_THAN_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand1 < operand2);
                        }
                        break;
                        case cydVMOpcodeList.OP_LESS_THAN_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand1 < operand2);
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_GREATER_THAN_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand1 > operand2);
                        }
                        break;
                        case cydVMOpcodeList.OP_GREATER_THAN_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand1 > operand2);
                        }
                        break;
                        case cydVMOpcodeList.OP_GREATER_THAN_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand1 > operand2);
                        }
                        break;
                        case cydVMOpcodeList.OP_GREATER_THAN_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = (operand1 > operand2);
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_ADDITION_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 + operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_ADDITION_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 + operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_ADDITION_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 + operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_ADDITION_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 + operand2;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_SUBTRACT_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 - operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_SUBTRACT_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 - operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_SUBTRACT_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 - operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_SUBTRACT_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 - operand2;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_MULTIPLY_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 * operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_MULTIPLY_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 * operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_MULTIPLY_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 * operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_MULTIPLY_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 * operand2;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_DIVIDE_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 / operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_DIVIDE_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 / operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_DIVIDE_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 / operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_DIVIDE_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 / operand2;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_MODULUS_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 % operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_MODULUS_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 % operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_MODULUS_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 % operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_MODULUS_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 % operand2;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_SHIFT_LEFT_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 << operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_SHIFT_LEFT_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 << operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_SHIFT_LEFT_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 << operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_SHIFT_LEFT_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 << operand2;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_SHIFT_RIGHT_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 >> operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_SHIFT_RIGHT_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 >> operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_SHIFT_RIGHT_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 >> operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_SHIFT_RIGHT_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 >> operand2;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_AND_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 & operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_AND_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 & operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_AND_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 & operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_AND_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 & operand2;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_OR_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 | operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_OR_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 | operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_OR_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 | operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_OR_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 | operand2;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_NOT_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = ~operand1;
                        }
                        break;
                        case cydVMOpcodeList.OP_NOT_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = ~operand1;
                        }
                        break;
                        case cydVMOpcodeList.OP_NOT_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = ~operand1;
                        }
                        break;
                        case cydVMOpcodeList.OP_NOT_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = ~operand1;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_XOR_BYTE: {
                            byte operand2 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 ^ operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_XOR_SHORT: {
                            short operand2 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 ^ operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_XOR_INT: {
                            int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 ^ operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_XOR_LONG: {
                            long operand2 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 ^ operand2;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_PLACE_CONST_BYTE: {
                            int position = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            byte operand2 = varData[offset++];
                            
                            stackByte[stackByteCSTop + position] = operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_PLACE_CONST_SHORT: {
                            int position = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            short operand2 = (short)((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            stackShort[stackShortCSTop + position] = operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_PLACE_CONST_INT: {
                            int position = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[stackIntCSTop + position] = operand2;
                        }
                        break;
                        case cydVMOpcodeList.OP_PLACE_CONST_LONG: {
                            int position = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            long operand2 = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            stackLong[stackLongCSTop + position] = operand2;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_INC_BYTE: {
                            int stackPos = stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackByte[stackPos] += varData[offset++];
                        }
                        break;
                        case cydVMOpcodeList.OP_INC_SHORT: {
                            int stackPos = stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackShort[stackPos] += varData[offset++];
                        }
                        break;
                        case cydVMOpcodeList.OP_INC_INT: {
                            int stackPos = stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackInt[stackPos] += varData[offset++];
                        }
                        break;
                        case cydVMOpcodeList.OP_INC_LONG: {
                            int stackPos = stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackLong[stackPos] += varData[offset++];
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_AND_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 & constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_AND_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 & constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_AND_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 & constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_AND_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 & constItem;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_OR_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 | constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_OR_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 | constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_OR_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 | constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_OR_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 | constItem;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_SHIFT_LEFT_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 << constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_SHIFT_LEFT_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 << constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_SHIFT_LEFT_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 << constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_SHIFT_LEFT_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 << constItem;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_SHIFT_RIGHT_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 >> constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_SHIFT_RIGHT_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 >> constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_SHIFT_RIGHT_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 >> constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_SHIFT_RIGHT_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 >> constItem;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_XOR_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 ^ constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_XOR_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 ^ constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_XOR_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 ^ constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_XOR_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 ^ constItem;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_EQUALS_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            condition = (operand1 == constItem);
                        }
                        break;
                        case cydVMOpcodeList.OP_EQUALS_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            condition = (operand1 == constItem);
                        }
                        break;
                        case cydVMOpcodeList.OP_EQUALS_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            condition = (operand1 == constItem);
                        }
                        break;
                        case cydVMOpcodeList.OP_EQUALS_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            condition = (operand1 == constItem);
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_NOT_EQUAL_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            condition = (operand1 != constItem);
                        }
                        break;
                        case cydVMOpcodeList.OP_NOT_EQUAL_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            condition = (operand1 != constItem);
                        }
                        break;
                        case cydVMOpcodeList.OP_NOT_EQUAL_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            condition = (operand1 != constItem);
                        }
                        break;
                        case cydVMOpcodeList.OP_NOT_EQUAL_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            condition = (operand1 != constItem);
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_LESS_THAN_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            condition = (operand1 < constItem);
                        }
                        break;
                        case cydVMOpcodeList.OP_LESS_THAN_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            condition = (operand1 < constItem);
                        }
                        break;
                        case cydVMOpcodeList.OP_LESS_THAN_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            condition = (operand1 < constItem);
                        }
                        break;
                        case cydVMOpcodeList.OP_LESS_THAN_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            condition = (operand1 < constItem);
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_GREATER_THAN_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            condition = (operand1 > constItem);
                        }
                        break;
                        case cydVMOpcodeList.OP_GREATER_THAN_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            condition = (operand1 > constItem);
                        }
                        break;
                        case cydVMOpcodeList.OP_GREATER_THAN_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            condition = (operand1 > constItem);
                        }
                        break;
                        case cydVMOpcodeList.OP_GREATER_THAN_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            condition = (operand1 > constItem);
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_ADDITION_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 + constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_ADDITION_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 + constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_ADDITION_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 + constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_ADDITION_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 + constItem;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_SUBTRACT_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 - constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_SUBTRACT_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 - constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_SUBTRACT_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 - constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_SUBTRACT_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 - constItem;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_MULTIPLY_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 * constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_MULTIPLY_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 * constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_MULTIPLY_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 * constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_MULTIPLY_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 * constItem;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_DIVIDE_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 / constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_DIVIDE_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 / constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_DIVIDE_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 / constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_DIVIDE_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 / constItem;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_MODULUS_CONST_BYTE: {
                            byte operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = varData[offset++];
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 % constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_MODULUS_CONST_SHORT: {
                            short operand1 = stackShort[stackShortCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 % constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_MODULUS_CONST_INT: {
                            int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            int constItem = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                            
                            stackInt[(++stackIntTop) + stackIntCSTop] = operand1 % constItem;
                        }
                        break;
                        case cydVMOpcodeList.OP_MODULUS_CONST_LONG: {
                            long operand1 = stackLong[stackLongCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            long constItem = (((((long)varData[offset++] & 0xFF) << 56) | (((long)varData[offset++] & 0xFF) << 48) | (((long)varData[offset++] & 0xFF) << 40) | (((long)varData[offset++] & 0xFF) << 32) | (((long)varData[offset++] & 0xFF) << 24) | (((long)varData[offset++] & 0xFF) << 16) | (((long)varData[offset++] & 0xFF) << 8) | ((long)varData[offset++] & 0xFF)));
                            
                            stackLong[(++stackLongTop) + stackLongCSTop] = operand1 % constItem;
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_OBJECT_EQUALS_METHOD: {
                            Object operand2 = stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            Object operand1 = stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                            
                            condition = operand1.equals(operand2);
                        }
                        break;
                        
                        case cydVMOpcodeList.OP_2_CONST_OPERAND: {
                            stackOperandTop++;
                            stackOperand[stackOperandTop + stackOperandCSTop] = (short)((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackOperandTop++;
                            stackOperand[stackOperandTop + stackOperandCSTop] = (short)((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                        }
                        break;
                        case cydVMOpcodeList.OP_3_CONST_OPERAND: {
                            stackOperandTop++;
                            stackOperand[stackOperandTop + stackOperandCSTop] = (short)((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackOperandTop++;
                            stackOperand[stackOperandTop + stackOperandCSTop] = (short)((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackOperandTop++;
                            stackOperand[stackOperandTop + stackOperandCSTop] = (short)((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                        }
                        break;
                        case cydVMOpcodeList.OP_4_CONST_OPERAND: {
                            stackOperandTop++;
                            stackOperand[stackOperandTop + stackOperandCSTop] = (short)((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackOperandTop++;
                            stackOperand[stackOperandTop + stackOperandCSTop] = (short)((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackOperandTop++;
                            stackOperand[stackOperandTop + stackOperandCSTop] = (short)((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                            stackOperandTop++;
                            stackOperand[stackOperandTop + stackOperandCSTop] = (short)((varData[offset++] << 8) | (varData[offset++] & 0xFF));
                        }
                        break;
                        case cydVMOpcodeList.OP_SUB_OP_ONE: {
                            int subopcode = varData[offset++];
                            
                            switch (subopcode) {
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_WHITE_OUT: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_whiteOutColor = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_RAND: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = rand();
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_RAND_MAX: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = rand() % operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_RAND_MIN_MAX: {
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = (rand() % (operand2 - operand1)) + operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_RAND_SEED: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    m_randSeed = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_TIMER: {
                                    int operand4 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];       // repeat_count
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];       // vm_address
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];       // time_ms
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];       // id
                                    
                                    game.m_timers[(operand1 << 2) | 0] = operand2;
                                    game.m_timers[(operand1 << 2) | 1] = 0;
                                    game.m_timers[(operand1 << 2) | 2] = operand3;
                                    game.m_timers[(operand1 << 2) | 3] = operand4;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_RELEASE_TIMER: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_timers[(operand1 << 2) | 2] = cydGamePlayCanvas.VM_NO_ENTRY_POINT;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_GRAVITY: {
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];       // amount
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];       // time  
                                    
                                    game.m_gravTime = operand1;
                                    game.m_gravAmount = operand2;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_CAMERA_Y: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];       // pos
                                    
                                    game.m_currentView = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_SHAKE: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_shakeFactor = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_SLOWDOWN_RATE: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_slowDownFactor = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_STATE: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_gameState = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_BACKGROUND_COLOR: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_background = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_INC_POWER_TIME: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_builtUpInterpolator.m_frameTimeOffsets[0] = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_GRAVITY: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_gravTime;
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_gravAmount;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_CAMERA_Y: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_currentView;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_SHAKE: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_shakeFactor;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_SLOWDOWN_RATE: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_slowDownFactor;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_STATE: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_gameState;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_BACKGROUND_COLOR: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_background;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_INC_POWER_TIME: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_builtUpInterpolator.m_frameTimeOffsets[0];
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_ON_BLOCK_STEP: {
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_onBlockStepAddresses[operand1] = operand2;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_XY_POS: {
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];   //y
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];   //x
                                    
                                    game.m_objectProperties[0] = operand1;
                                    game.m_objectProperties[1] = operand2;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_X_POS: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_objectProperties[0] = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_Y_POS: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_objectProperties[1] = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_X_RATE: {
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];       // amount
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];       // time  
                                    
                                    int oldRate = game.m_objectProperties[6];
                                    
                                    game.m_objectProperties[2] = operand1;
                                    game.m_objectProperties[6] = operand2;
                                    
                                    if (oldRate < operand2)
                                        game.changeSpriteDirection(0);
                                    else if (oldRate > operand2)
                                        game.changeSpriteDirection(0);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_Y_RATE: {
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];       // amount
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];       // time  
                                    
                                    game.m_objectProperties[3] = operand1;
                                    game.m_objectProperties[7] = operand2;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_GRAV_AFFECTED: {
                                    int operand1 = stackByte[stackByteCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_objectAffectedByGrav[0] = operand1 != 0;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_LEFT_BOUND: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];       // time  
                                    
                                    game.m_objectProperties[8] = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_BOUND: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];       // time  
                                    
                                    game.m_objectProperties[9] = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_XY_POS: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[1];
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[0];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_X_POS: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[0];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_Y_POS: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[1];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_X_RATE: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[6];
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[2];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_Y_RATE: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[7];
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[3];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_GRAV_AFFECTED: {
                                    stackByte[(++stackByteTop) + stackByteCSTop] = (byte)(game.m_objectAffectedByGrav[0] == true ? 1 : 0);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_LEFT_BOUND: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[8];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_RIGHT_BOUND: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[9];
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_WHITE_OUT_STATIC: {
                                    game.m_whiteOutColor = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_RAND_MAX_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = rand() % operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_RAND_MIN_MAX_STATIC: {
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = (rand() % (operand2 - operand1)) + operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_RAND_SEED_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    m_randSeed = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_TIMER_STATIC: {
                                    int operand4 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // repeat_count
                                    int operand3 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // vm_address
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // time_ms
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // id
                                    
                                    game.m_timers[(operand1 << 2) | 0] = operand2;
                                    game.m_timers[(operand1 << 2) | 1] = 0;
                                    game.m_timers[(operand1 << 2) | 2] = operand3;
                                    game.m_timers[(operand1 << 2) | 3] = operand4;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_RELEASE_TIMER_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_timers[(operand1 << 2) | 2] = cydGamePlayCanvas.VM_NO_ENTRY_POINT;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_GRAVITY_STATIC: {
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // amount
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // time  
                                    
                                    game.m_gravTime = operand1;
                                    game.m_gravAmount = operand2;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_CAMERA_Y_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // pos
                                    
                                    game.m_currentView = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_SHAKE_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_shakeFactor = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_SLOWDOWN_RATE_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_slowDownFactor = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_STATE_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_gameState = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_BACKGROUND_COLOR_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_background = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_INC_POWER_TIME_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_builtUpInterpolator.m_frameTimeOffsets[0] = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_ON_BLOCK_STEP_STATIC: {
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_onBlockStepAddresses[operand1] = operand2;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_XY_POS_STATIC: {
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_objectProperties[0] = operand1;
                                    game.m_objectProperties[1] = operand2;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_X_POS_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_objectProperties[0] = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_Y_POS_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_objectProperties[1] = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_X_RATE_STATIC: {
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // amount
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // time  
                                    
                                    int oldRate = game.m_objectProperties[6];
                                    
                                    game.m_objectProperties[2] = operand1;
                                    game.m_objectProperties[6] = operand2;
                                    
                                    if (oldRate < operand2)
                                        game.changeSpriteDirection(0);
                                    else if (oldRate > operand2)
                                        game.changeSpriteDirection(0);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_Y_RATE_STATIC: {
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // amount
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // time  
                                    
                                    game.m_objectProperties[3] = operand1;
                                    game.m_objectProperties[7] = operand2;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_GRAV_AFFECTED_STATIC: {
                                    int operand1 = varData[offset++];
                                    
                                    game.m_objectAffectedByGrav[0] = operand1 != 0;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_LEFT_BOUND_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // time  
                                    
                                    game.m_objectProperties[8] = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_BOUND_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // time  
                                    
                                    game.m_objectProperties[9] = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_TOTAL_HEIGHT: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_height;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_WORKING_HEIGHT: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_height - game.m_headerImage.getHeight();
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_WORKING_TOP_Y: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_headerImage.getHeight();
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_ON_LEFT_HIT: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_onLeftHitAddress = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_ON_RIGHT_HIT: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_onRightHitAddress = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_ON_LEFT_HIT_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // time  
                                    
                                    game.m_onLeftHitAddress = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_ON_RIGHT_HIT_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));       // time  
                                    
                                    game.m_onRightHitAddress = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_LEVEL_DATA: {
                                    stackObject[(++stackObjectTop) + stackObjectCSTop] = game.m_realLevelData;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_GAME_FLAGS:{
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_gamePlayFlagsOne;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_GAME_FLAGS:{
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_gamePlayFlagsOne = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_GAME_FLAGS_STATIC:{
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_gamePlayFlagsOne = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_STAND: {
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_objectProperties[10] = operand1;
                                    game.m_objectProperties[11] = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_LEFT_STAND: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_objectProperties[10] = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_STAND: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_objectProperties[11] = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_STAND_STATIC: {
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_objectProperties[10] = operand1;
                                    game.m_objectProperties[11] = operand2;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_LEFT_STAND_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_objectProperties[10] = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_STAND_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_objectProperties[11] = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_LEFT_RIGHT_STAND: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[11];
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[10];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_LEFT_STAND: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[10];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_RIGHT_STAND: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[11];
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_ON_DIE: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_onDieAddress = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_ON_DIE_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_onDieAddress = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_GAME_FLAGS_2:{
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_gamePlayFlagsTwo;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_GAME_FLAGS_2:{
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_gamePlayFlagsTwo = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_GAME_FLAGS_2_STATIC:{
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_gamePlayFlagsTwo = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_CURRENT_FLOOR_NUMBER:{
                                    stackInt[(++stackIntTop) + stackIntCSTop] = (game.m_objectProperties[1] + game.m_objectProperties[19]) / cydGamePlayCanvas.DEFAULT_BLOCK_FRAME_HEIGHT;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_IS_TIMER_ACTIVE: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = (game.m_timers[(operand1 << 2) | 3] != 0 ? 1 : 0);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_IS_TIMER_ACTIVE_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = (game.m_timers[(operand1 << 2) | 3] != 0 ? 1 : 0);
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_BLOCK_REF_PIXEL: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_blockProperties[operand1 << 2];
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_blockProperties[(operand1 << 2) | 1];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_BLOCK_REF_PIXEL: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_blockProperties[operand1 << 2] = operand2;
                                    game.m_blockProperties[(operand1 << 2) | 1] = operand3;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_BLOCK_REF_PIXEL_STATIC: {
                                    int operand3 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_blockProperties[operand1 << 2] = operand2;
                                    game.m_blockProperties[(operand1 << 2) | 1] = operand3;
                                }
                                break;

                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_REF_PIXEL: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[13];
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[14];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_REF_PIXEL: {
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_objectProperties[13] = operand1;
                                    game.m_objectProperties[14] = operand2;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_REF_PIXEL_STATIC: {
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_objectProperties[13] = operand1;
                                    game.m_objectProperties[14] = operand2;
                                }
                                break;

                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_BLOCK_FRAME_SEQ: {
                                    int []operand2 = (int [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_blockSprites[operand1].setFrameSequence(operand2);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_BLOCK_TIME_SEQ: {
                                    int []operand2 = (int [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_blockSprites[operand1].setWaitTimes(operand2);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_FRAME_SEQ: {
                                    int operand4 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];                   // which offset to dump to
                                    int []operand3 = (int [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];   // offsets
                                    int []operand2 = (int [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];   // wait sequences
                                    int []operand1 = (int [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];   // frame sequences
                                    
                                    int framesOffset = operand4;
                                    int waitTimesOffset = operand4 << 1;
                                    
                                    int [][]frames = new int[operand3.length][];
                                    int [][]waitTimes = new int[operand3.length][];
                                    
                                    int pos = 0;
                                    
                                    for (int i = 0; i < operand3.length; i++) {
                                        int endPoint = operand3[i];
                                        
                                        while (pos < endPoint) {
                                            frames[i][endPoint - pos] = operand1[pos];
                                            waitTimes[i][endPoint - pos] = operand2[pos];
                                            
                                            pos++;
                                        }
                                    }
                                    
                                    game.m_objectSpriteSequences[framesOffset] = frames;
                                    game.m_objectSpriteSequences[waitTimesOffset] = waitTimes;
                                    
                                    game.changeSpriteDirection(0);
                                    game.changeSpriteDirection(0);      // hack to make sure sprite turns out ok!
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_ONLY: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    switch (operand1) {
                                        case 0:
                                            game.m_objectProperties[16] = cydGamePlayCanvas.SPRITE_MODE_DIRECTION_LEFT | cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_WALK;       // sprite mode
                                            break;
                                    }
                                    
                                    game.changeSpriteDirection(0);
                                    game.changeSpriteDirection(0);      // hack to make sure sprite turns out ok!
                                    
                                    game.m_objectProperties[17]  = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_POWER: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[12];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_POWER: {
                                    game.m_objectProperties[12] = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_POWER_STATIC: {
                                    game.m_objectProperties[12] =  ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_ON_BLOCK_DEPART: {
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_onBlockDepartAddresses[operand1] = operand2;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_ON_BLOCK_DEPART_STATIC: {
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_onBlockDepartAddresses[operand1] = operand2;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_RET: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_returnCode;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_RET: {
                                    game.m_returnCode = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_RET_STATIC: {
                                    game.m_returnCode = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_LOAD_FROM_JAR: {
                                    String operand1 = (String)stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackObject[(++stackObjectTop) + stackObjectCSTop] = game.loadData(operand1);
                                }
                                break;

                                case cydVMOpcodeList.SUB_OP_ONE_GAME_NEW_MIDI: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    byte []operand1 = (byte [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    if (game.m_gm.m_musicOn)
                                        game.m_gm.m_player.newMusic(new ByteArrayInputStream(operand1, operand2, operand3), cydMusicPlayer.DESIRED_MIME_TYPE);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_START_MIDI: {
                                    if (game.m_gm.m_musicOn)
                                        game.m_gm.m_player.startMusic();
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_STOP_MIDI: {
                                    if (game.m_gm.m_musicOn)
                                        game.m_gm.m_player.stopMusic();
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_CLOSE_MIDI: {
                                    if (game.m_gm.m_musicOn)
                                        game.m_gm.m_player.closeMusic();
                                }
                                break;

                                case cydVMOpcodeList.SUB_OP_ONE_GAME_FLASH_BACKLIGHT: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    Display.getDisplay(game.m_gm.m_gameMidlet).flashBacklight(operand1);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_VIBRATE: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    Display.getDisplay(game.m_gm.m_gameMidlet).vibrate(operand1);
                                }
                                break;

                                case cydVMOpcodeList.SUB_OP_ONE_GAME_FLASH_BACKLIGHT_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    Display.getDisplay(game.m_gm.m_gameMidlet).flashBacklight(operand1);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_VIBRATE_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    Display.getDisplay(game.m_gm.m_gameMidlet).vibrate(operand1);
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_WIDTH: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[18];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_WIDTH_PIXEL: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_objectProperties[18] = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_WIDTH_PIXEL_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_objectProperties[18] = operand1;
                                }
                                break;
    
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_PLAYER_HEIGHT: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_objectProperties[19];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_HEIGHT_PIXEL: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_objectProperties[19] = operand1;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_PLAYER_HEIGHT_PIXEL_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_objectProperties[19] = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_GET_BLOCK_COLL_OFFSET: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_blockProperties[(operand1 << 2) | 2];
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_blockProperties[(operand1 << 2) | 3];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_BLOCK_COLL_OFFSET: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_blockProperties[(operand1 << 2) | 2] = operand2;
                                    game.m_blockProperties[(operand1 << 2) | 3] = operand3;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_BLOCK_COLL_OFFSET_STATIC: {
                                    int operand3 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_blockProperties[(operand1 << 2) | 2] = operand2;
                                    game.m_blockProperties[(operand1 << 2) | 3] = operand3;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SET_LEVEL_DATA: {
                                    int []operand1 = (int [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_realLevelData = operand1;
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_RESIZE_LAYER_ITEMS: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_scene.resizeLayerItems(operand1, operand2, operand3);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_RESIZE_LAYER: {
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_scene.resizeLayer(operand1, operand2);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_RESIZE_DB: {
                                    int operand4 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_scene.resizeDB(operand1, operand2, operand3, operand4);
                                }
                                break;
    
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_LOAD_NEW_INTERPOLATION_POINTS: {
                                    int operand5 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand4 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int []operand3 = (int [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int []operand2 = (int [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_scene.loadNewInterpolationPoints(operand1, operand2, operand3, operand4 != 0, operand5 != 0);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_LOAD_NEW_IMAGE: {
                                    String operand2 = (String)stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    cydGraphicsManager gfxMnger = new cydGraphicsManager(1);
                                    game.loadImage(gfxMnger, cydTokenizer.tokenize(operand2, ':'), 0);
                                    
                                    game.m_scene.loadNewImage(operand1, gfxMnger.getImage(0));
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_LOAD_NEW_SPRITE: {
                                    int operand6 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand5 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand4 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_scene.loadNewSprite(operand1, operand2, operand3, operand4, operand5, operand6);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_LOAD_NEW_SEQUENCE: {
                                    int []operand2 = (int [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_scene.loadNewSequence(operand1, operand2);
                                }
                                break;
    
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_GET_IMAGE_COUNT: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.getImageCount();
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATOR_COUNT: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.getInterpolatorCount();
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_GET_SEQUENCE_COUNT: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.getSequenceCount();
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_GET_SPRITE_COUNT: {
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.getSpriteCount();
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_GET_SPRITE_BLOCK_INDEX: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.getSpriteBlockIndex(operand1);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_GET_SPRITE_BLOCK_INDEX_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.getSpriteBlockIndex(operand1);
                                }
                                break;
    
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_GET_ITEM_LENGTH: {
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.getItemLength(operand1, operand2);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_GET_LAYER_LENGTH: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.getLayerLength(operand1);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_GET_LAYER_PARAM: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.getLayerParam(operand1, operand2, operand3);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_IS_INTERPOLATOR_DONE: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = (game.m_scene.isInterpolatorDone(operand1) ? 1 : 0);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_IS_INTERPOLATOR_DONE_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = (game.m_scene.isInterpolatorDone(operand1) ? 1 : 0);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_IS_LAYER_VISIBLE: {
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = (game.m_scene.isLayerVisible(operand1, operand2) ? 1 : 0);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_IS_LAYER_VISIBLE_STATIC: {
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = (game.m_scene.isLayerVisible(operand1, operand2) ? 1 : 0);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_IS_SPRITE_LINK_VISIBLE: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = (game.m_scene.isSpriteLinkVisible(operand1, operand2, operand3) ? 1 : 0);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_IS_SPRITE_LINK_VISIBLE_STATIC: {
                                    int operand3 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = (game.m_scene.isSpriteLinkVisible(operand1, operand2, operand3) ? 1 : 0);
                                }
                                break;
    
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_SET_LAYER_PARAM: {
                                    int operand4 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_scene.setLayerParam(operand1, operand2, operand3, operand4);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_SET_LAYER_VISIBILITY: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_scene.setLayerVisibility(operand1, operand2, operand3 != 0);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_SET_LAYER_VISIBILITY_STATIC: {
                                    int operand3 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_scene.setLayerVisibility(operand1, operand2, operand3 != 0);
                                }
                                break;
    
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_SWITCH_LAYERS: {
                                    int operand4 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_scene.switchLayers(operand1, operand2, operand3, operand4);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_SWITCH_LAYERS_STATIC: {
                                    int operand4 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand3 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_scene.switchLayers(operand1, operand2, operand3, operand4);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_FLIP_SETS: {
                                    game.m_scene.switchSets(0, 1);
                                }
                                break;
    
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_SWITCH_SPRITE_SEQUENCE: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_scene.switchSpriteSequence(operand1, operand2, operand3);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_SWITCH_SPRITE_SEQUENCE_STATIC: {
                                    int operand3 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_scene.switchSpriteSequence(operand1, operand2, operand3);
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_RESET_INTERPOLATION_POINT: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_scene.resetInterpolator(operand1);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_RESET_INTERPOLATION_POINT_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_scene.resetInterpolator(operand1);
                                }
                                break;
    
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_GET_ITEM: {
                                    int operand4 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.getItem(operand1, operand2, operand3, operand4);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_GET_ITEM_STATIC: {
                                    int operand4 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand3 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.getItem(operand1, operand2, operand3, operand4);
                                }
                                break;
    
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_SET_ITEM: {
                                    int operand5 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand4 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    game.m_scene.setItem(operand1, operand2, operand3, operand4, operand5);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_SET_ITEM_STATIC: {
                                    int operand5 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand4 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand3 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand2 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    game.m_scene.setItem(operand1, operand2, operand3, operand4, operand5);
                                }
                                break;
                                
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATION_POINT: {
                                    int operand1 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.m_interpolators[operand1].m_points[0];
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.m_interpolators[operand1].m_points[1];
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATION_POINT_STATIC: {
                                    int operand1 = ((varData[offset++] << 24) | ((varData[offset++] &0xFF) << 16) | ((varData[offset++] &0xFF) << 8) | (varData[offset++] & 0xFF));
                                    
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.m_interpolators[operand1].m_points[0];
                                    stackInt[(++stackIntTop) + stackIntCSTop] = game.m_scene.m_interpolators[operand1].m_points[1];
                                }
                                break;

                                case cydVMOpcodeList.SUB_OP_ONE_ARRAY_COPY: {
                                    int operand5 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand4 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    Object operand3 = stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    Object operand1 = stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    System.arraycopy(operand1, operand2, operand3, operand4, operand5);
                                }
                                break;
    
                                case cydVMOpcodeList.SUB_OP_ONE_ARRAY_BYTE_TO_SHORT: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    byte []operand1 = (byte [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    short []newArray = new short[operand3];
                                    
                                    for (int i = 0; i < operand3; i++)
                                        newArray[i] = (short)((operand1[operand2++] << 8) | (operand1[operand2++] & 0xFF));
                                    
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = newArray;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_ARRAY_BYTE_TO_INT: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    byte []operand1 = (byte [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    int []newArray = new int[operand3];
                                    
                                    for (int i = 0; i < operand3; i++)
                                        newArray[i] = ((operand1[operand2++] << 24) | ((operand1[operand2++] &0xFF) << 16) | ((operand1[operand2++] &0xFF) << 8) | (operand1[operand2++] & 0xFF));
                                    
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = newArray;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_ARRAY_BYTE_TO_LONG: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    byte []operand1 = (byte [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    long []newArray = new long[operand3];
                                    
                                    for (int i = 0; i < operand3; i++)
                                        newArray[i] = (((((long)operand1[operand2++] & 0xFF) << 56) | (((long)operand1[operand2++] & 0xFF) << 48) | (((long)operand1[operand2++] & 0xFF) << 40) | (((long)operand1[operand2++] & 0xFF) << 32) | (((long)operand1[operand2++] & 0xFF) << 24) | (((long)operand1[operand2++] & 0xFF) << 16) | (((long)operand1[operand2++] & 0xFF) << 8) | ((long)operand1[operand2++] & 0xFF)));
                                    
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = newArray;
                                }
                                break;
    
                                case cydVMOpcodeList.SUB_OP_ONE_ARRAY_SHORT_TO_BYTE: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    short []operand1 = (short [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    byte []newArray = new byte[operand3 << 1];
                                    
                                    for (int i = 0; i < operand3; i++) {
                                        short s = operand1[i];
                                        
                                        newArray[operand2++] = (byte)(s >> 8);
                                        newArray[operand2++] = (byte)(s);
                                    }
                                    
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = newArray;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_ARRAY_INT_TO_BYTE: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int []operand1 = (int [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    byte []newArray = new byte[operand3 << 2];
                                    
                                    for (int i = 0; i < operand3; i++) {
                                        int s = operand1[i];
                                        
                                        newArray[operand2++] = (byte)(s >> 24);
                                        newArray[operand2++] = (byte)(s >> 16);
                                        newArray[operand2++] = (byte)(s >> 8);
                                        newArray[operand2++] = (byte)(s);
                                    }
                                    
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = newArray;
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_ARRAY_LONG_TO_BYTE: {
                                    int operand3 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    int operand2 = stackInt[stackIntCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    long []operand1 = (long [])stackObject[stackObjectCSTop + ((varData[offset++] << 8) | (varData[offset++] & 0xFF))];
                                    
                                    byte []newArray = new byte[operand3 << 3];
                                    
                                    for (int i = 0; i < operand3; i++) {
                                        long s = operand1[i];
                                        
                                        newArray[operand2++] = (byte)(s >> 64);
                                        newArray[operand2++] = (byte)(s >> 48);
                                        newArray[operand2++] = (byte)(s >> 40);
                                        newArray[operand2++] = (byte)(s >> 32);
                                        newArray[operand2++] = (byte)(s >> 24);
                                        newArray[operand2++] = (byte)(s >> 16);
                                        newArray[operand2++] = (byte)(s >> 8);
                                        newArray[operand2++] = (byte)(s);
                                    }
                                    
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = newArray;
                                }
                                break;

                                case cydVMOpcodeList.SUB_OP_ONE_HT_NEW: {
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = new Hashtable();
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_HT_NEW_INITIALCAP: {
                                    int operand1 = stackInt[stackIntCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = new Hashtable(operand1);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_HT_CLEAR: {
                                    Hashtable operand1 = (Hashtable)stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    
                                    operand1.clear();
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_HT_CONTAINS: {
                                    Object operand2 = stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    Hashtable operand1 = (Hashtable)stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    
                                    condition = operand1.contains(operand2);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_HT_CONTAINSKEY: {
                                    Object operand2 = stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    Hashtable operand1 = (Hashtable)stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    
                                    condition = operand1.containsKey(operand2);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_HT_ELEMENTS: {
                                    Hashtable operand1 = (Hashtable)stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = operand1.elements();
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_HT_GET: {
                                    Object operand2 = stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    Hashtable operand1 = (Hashtable)stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = operand1.get(operand2);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_HT_ISEMPTY: {
                                    Hashtable operand1 = (Hashtable)stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    
                                    condition = operand1.isEmpty();
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_HT_KEYS: {
                                    Hashtable operand1 = (Hashtable)stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = operand1.keys();
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_HT_PUT: {
                                    Object operand3 = stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    Object operand2 = stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    Hashtable operand1 = (Hashtable)stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = operand1.put(operand2, operand3);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_HT_REMOVE: {
                                    Object operand2 = stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    Hashtable operand1 = (Hashtable)stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    
                                    stackObjectTop++;
                                    stackObject[stackObjectTop + stackObjectCSTop] = operand1.remove(operand2);
                                }
                                break;
                                case cydVMOpcodeList.SUB_OP_ONE_HT_SIZE: {
                                    Hashtable operand1 = (Hashtable)stackObject[stackObjectCSTop + stackOperand[(stackOperandTop--) + stackOperandCSTop]];
                                    
                                    stackIntTop++;
                                    stackInt[stackIntTop + stackIntCSTop] = operand1.size();
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            } catch (Throwable t) {
                int foundOffset = -1;
                
                for (int i = stackTrapTypeTop; i >= 0; i--) {
                    try {
                        if (Class.forName(stackTrapType[i]).isInstance(t)) {
                            foundOffset = i;
                            break;
                        }
                    } catch (Throwable t1) { }
                }
                
                if (foundOffset == -1) {     // nothing found
                    t.printStackTrace();
                    throw new RuntimeException("VM CAUGHT EXCEPTION -- offset:" + offset + " datlen:" + varData.length + " ex:" + t + " msg:" + t.getMessage());
                }
                
                int topCopy = (16 * (foundOffset + 1)) - 1;
                
                int objEnd = stackObjectCSTop + stackObjectTop;
                
                condition = (stackTrapData[topCopy--] != 0 ? true : false);
                stackCallTop = stackTrapData[topCopy--];
                stackOperandTop = stackTrapData[topCopy--];
                stackObjectTop = stackTrapData[topCopy--];
                stackLongTop = stackTrapData[topCopy--];
                stackIntTop = stackTrapData[topCopy--];
                stackShortTop = stackTrapData[topCopy--];
                stackByteTop = stackTrapData[topCopy--];
                stackOperandCSTop = stackTrapData[topCopy--];
                stackObjectCSTop = stackTrapData[topCopy--];
                stackLongCSTop = stackTrapData[topCopy--];
                stackIntCSTop = stackTrapData[topCopy--];
                stackShortCSTop = stackTrapData[topCopy--];
                stackByteCSTop = stackTrapData[topCopy--];
                
                int adPart1 = (stackTrapData[topCopy--] & 0xFFFF);
                int adPart2 = (stackTrapData[topCopy--]<<16);
                
                int objStart = stackObjectCSTop + stackObjectTop;
                
                if (objStart != objEnd) {
                    for (int i = objStart + 1; i <= objEnd; i++)
                        stackObject[i] = null;
                }
                
                offset = adPart1 | adPart2;
                
                stackObjectTop++;
                stackObject[stackObjectTop + stackObjectCSTop] = t;
            }
        }
    }
}
