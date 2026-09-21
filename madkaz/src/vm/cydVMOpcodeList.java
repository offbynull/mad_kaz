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

public final class cydVMOpcodeList {
    public static final byte OP_CONST_BYTE                               = 0;    // FOLLOWED UP BY 1 BYTE, res = new byte (followup)
    public static final byte OP_CONST_SHORT                              = 1;    // FOLLOWED UP BY 2 BYTE, res = new short (followup)
    public static final byte OP_CONST_INT                                = 2;    // FOLLOWED UP BY 4 BYTE, res = new int (followup)
    public static final byte OP_CONST_LONG                               = 3;    // FOLLOWED UP BY 8 BYTE, res = new long (followup)
    public static final byte OP_CONST_OPERAND                            = 4;    // FOLLOWED UP BY 2 BYTE, res = new operand (followup)
    public static final byte OP_CONST_STRING                             = 5;    // FOLLOWED UP BY n BYTE, res = new string (utf8 dump)
    
    public static final byte OP_NEW_BYTE_ARRAY                           = 6;    // op1 = size of array (int index), res = new byte array
    public static final byte OP_NEW_SHORT_ARRAY                          = 7;    // op1 = size of array (int index), res = new short array
    public static final byte OP_NEW_INT_ARRAY                            = 8;    // op1 = size of array (int index), res = new int array
    public static final byte OP_NEW_LONG_ARRAY                           = 9;    // op1 = size of array (int index), res = new long array
    public static final byte OP_NEW_OBJECT_ARRAY                         = 10;   // op1 = size of array (int index), res = new object array
    
    public static final byte OP_POP_BYTE                                 = 11;   // res = remove most recent byte
    public static final byte OP_POP_SHORT                                = 12;   // res = remove most recent short
    public static final byte OP_POP_INT                                  = 13;   // res = remove most recent int
    public static final byte OP_POP_LONG                                 = 14;   // res = remove most recent long
    public static final byte OP_POP_OPERAND                              = 15;   // res = remove most recent operand
    public static final byte OP_POP_OBJECT                               = 16;   // res = remove most recent object
    
    public static final byte OP_CONV_BYTE_TO_SHORT                       = 17;   // FOLLOWED UP BY 2 BYTE (byte index), res = new short
    public static final byte OP_CONV_BYTE_TO_INT                         = 18;   // FOLLOWED UP BY 2 BYTE (byte index), res = new int
    public static final byte OP_CONV_BYTE_TO_LONG                        = 19;   // FOLLOWED UP BY 2 BYTE (byte index), res = new long
    public static final byte OP_CONV_BYTE_TO_OPERAND                     = 20;   // FOLLOWED UP BY 2 BYTE (byte index), res = new operand
    public static final byte OP_CONV_SHORT_TO_BYTE                       = 21;   // FOLLOWED UP BY 2 BYTE (short index), res = new byte
    public static final byte OP_CONV_SHORT_TO_INT                        = 22;   // FOLLOWED UP BY 2 BYTE (short index), res = new int
    public static final byte OP_CONV_SHORT_TO_LONG                       = 23;   // FOLLOWED UP BY 2 BYTE (short index), res = new long
    public static final byte OP_CONV_SHORT_TO_OPERAND                    = 24;   // FOLLOWED UP BY 2 BYTE (short index), res = new operand
    public static final byte OP_CONV_INT_TO_BYTE                         = 25;   // FOLLOWED UP BY 2 BYTE (int index), res = new byte
    public static final byte OP_CONV_INT_TO_SHORT                        = 26;   // FOLLOWED UP BY 2 BYTE (int index), res = new short
    public static final byte OP_CONV_INT_TO_LONG                         = 27;   // FOLLOWED UP BY 2 BYTE (int index), res = new long
    public static final byte OP_CONV_INT_TO_OPERAND                      = 28;   // FOLLOWED UP BY 2 BYTE (int index), res = new operand
    public static final byte OP_CONV_LONG_TO_BYTE                        = 29;   // FOLLOWED UP BY 2 BYTE (long index), res = new byte
    public static final byte OP_CONV_LONG_TO_SHORT                       = 30;   // FOLLOWED UP BY 2 BYTE (long index), res = new short
    public static final byte OP_CONV_LONG_TO_INT                         = 31;   // FOLLOWED UP BY 2 BYTE (long index), res = new int
    public static final byte OP_CONV_LONG_TO_OPERAND                     = 32;   // FOLLOWED UP BY 2 BYTE (long index), res = new operand
    public static final byte OP_CONV_OPERAND_TO_BYTE                     = 33;   // FOLLOWED UP BY 2 BYTE (operand index), res = new byte
    public static final byte OP_CONV_OPERAND_TO_SHORT                    = 34;   // FOLLOWED UP BY 2 BYTE (operand index), res = new short
    public static final byte OP_CONV_OPERAND_TO_INT                      = 35;   // FOLLOWED UP BY 2 BYTE (operand index), res = new int
    public static final byte OP_CONV_OPERAND_TO_LONG                     = 36;   // FOLLOWED UP BY 2 BYTE (operand index), res = new long
    
    public static final byte OP_SUB_ASSIGN_BYTE                          = 37;   // FOLLOWED UP BY 4 BYTE (object index, byte index), op1 = index of array (int index)
    public static final byte OP_SUB_ASSIGN_SHORT                         = 38;   // FOLLOWED UP BY 4 BYTE (object index, short index), op1 = index of array (int index)
    public static final byte OP_SUB_ASSIGN_INT                           = 39;   // FOLLOWED UP BY 4 BYTE (object index, int index), op1 = index of array (int index)
    public static final byte OP_SUB_ASSIGN_LONG                          = 40;   // FOLLOWED UP BY 4 BYTE (object index, long index), op1 = index of array (int index)
    public static final byte OP_SUB_ASSIGN_OBJECT                        = 41;   // FOLLOWED UP BY 4 BYTE (object index, object index), op1 = index of array (int index)
    
    public static final byte OP_SUB_EXTRACT_BYTE                         = 42;   // FOLLOWED UP BY 4 BYTE (object index), op1 = index of array (int index), res = copy of byte in array
    public static final byte OP_SUB_EXTRACT_SHORT                        = 43;   // FOLLOWED UP BY 4 BYTE (object index), op1 = index of array (int index), res = copy of short in array
    public static final byte OP_SUB_EXTRACT_INT                          = 44;   // FOLLOWED UP BY 4 BYTE (object index), op1 = index of array (int index), res = copy of int in array
    public static final byte OP_SUB_EXTRACT_LONG                         = 45;   // FOLLOWED UP BY 4 BYTE (object index), op1 = index of array (int index), res = copy of long in array
    public static final byte OP_SUB_EXTRACT_OBJECT                       = 46;   // FOLLOWED UP BY 4 BYTE (object index), op1 = index of array (int index), res = copy of object in array
    
    public static final byte OP_SIZE_OF                                  = 47;  // op1 = value 1 (object index), res = new int
    
    public static final byte OP_OBJECT_PUSH_NULL                         = 48;  // res = new object pushed onto object stack containing null
    public static final byte OP_OBJECT_EQUALS_NULL                       = 49;  // FOLLOWED UP BY 2 BYTES (object index), res = cond changed: 0 for false, 1 for true
    
    public static final byte OP_CONTROL_CALL                             = 50;  // FOLLOWED UP BY 9 BYTES. 1 byte for how much to subtract off of each stack (byte/short/int/long/object). 4 bytes address
    public static final byte OP_CONTROL_SEMI_DYNAMIC_CALL                = 51;  // FOLLOWED UP BY 5 BYTES. 1 byte for how much to subtract off of each stack (byte/short/int/long/object). op1 = address (int index)
    public static final byte OP_CONTROL_FULL_DYNAMIC_CALL                = 52;  // op1 = address (int index), op 2-6 = how much to subtract off of each stack (byte index * 5)
    public static final byte OP_CONTROL_RET                              = 53;  // res = reset stack tops
    public static final byte OP_CONTROL_JUMP                             = 54;  // FOLLOWED UP BY 4 BYTES representing address. res = pc changed to op1
    public static final byte OP_CONTROL_DYNAMIC_JUMP                     = 55;  // op1 = position (int index), res = pc changed to op1
    public static final byte OP_CONTROL_JUMP_CONDITIONAL                 = 56;  // FOLLOWED UP BY 4 BYTES representing address. res = pc changed to op1 if cond is false
    public static final byte OP_CONTROL_DYNAMIC_JUMP_CONDITIONAL         = 57;  // op1 = position (int index) res = pc changed to op1 if cond is false
    public static final byte OP_CONTROL_SET_TRAP                         = 58;  // FOLLOWED UP BY n+4 BYTE representing trap string being set, catch position (int index)
    public static final byte OP_CONTROL_THROW_TRAP                       = 59; // FOLLOWED UP BY n BYTE representing trap being thrown
    public static final byte OP_CONTROL_RELEASE_TRAP                     = 60;
    public static final byte OP_CONTROL_EXIT                             = 61;
    
    public static final byte OP_SYSTEM_ID_STRING                         = 62; // res = new string giving you subsystem id
    public static final byte OP_SYSTEM_TIME                              = 63; // res = time in ms
    public static final byte OP_SYSTEM_OUT                               = 64; // op1 = string to output (object index)
    public static final byte OP_SYSTEM_SHARED                            = 65; // res = hashmap
    public static final byte OP_SYSTEM_GC                                = 70;
    
    public static final byte OP_COPY_BYTE                                = 71; // FOLLOWED UP BY 2 BYTE, res = new byte (followup)
    public static final byte OP_COPY_SHORT                               = 72; // FOLLOWED UP BY 2 BYTE, res = new short (followup)
    public static final byte OP_COPY_INT                                 = 73; // FOLLOWED UP BY 2 BYTE, res = new int (followup)
    public static final byte OP_COPY_LONG                                = 74; // FOLLOWED UP BY 2 BYTE, res = new long (followup)
    public static final byte OP_COPY_OPERAND                             = 75; // FOLLOWED UP BY 2 BYTE, res = new operand (followup)
    public static final byte OP_COPY_OBJECT                              = 76; // FOLLOWED UP BY 2 BYTE, res = new object (followup)
    
    public static final byte OP_GET_COND                                 = 77; // res = condition into byte
    public static final byte OP_SET_COND                                 = 78; // FOLLOWED UP BY 2 BYTE (byte index to put into cond)
    public static final byte OP_NOT_COND                                 = 80; // res = condition inverted
    
    public static final byte OP_MULTI_POP_BYTE                           = 81; // FOLLOWED UP BY 2 BYTE (amount to remove), res = remove op1 bytes
    public static final byte OP_MULTI_POP_SHORT                          = 82; // FOLLOWED UP BY 2 BYTE (amount to remove), res = remove op1 shorts
    public static final byte OP_MULTI_POP_INT                            = 83;  // FOLLOWED UP BY 2 BYTE (amount to remove), res = remove op1 ints
    public static final byte OP_MULTI_POP_LONG                           = 84;  // FOLLOWED UP BY 2 BYTE (amount to remove), res = remove op1 longs
    public static final byte OP_MULTI_POP_OPERAND                        = 85;  // FOLLOWED UP BY 2 BYTE (amount to remove), res = remove op1 operands
    public static final byte OP_MULTI_POP_OBJECT                         = 86;  // FOLLOWED UP BY 2 BYTE (amount to remove), res = remove op1 objects
    
    public static final byte OP_RES_LENGTH                               = 87;  // op1 = id to copy (short index), res = size of res as int
    public static final byte OP_SYSTEM_DIRECT_ACCESS_VM_DATA_FILE        = 88;  // res = actual data byte array of vm put onto object stack
    public static final byte OP_SYSTEM_DIRECT_LOAD_RES_POSITION          = 89;  // op1 = id to copy (short index), res = pos of res in data file
    
    public static final byte OP_SET_RET                                  = 90;  // FOLLOWED UP BY 1 BYTE (0=byte,1=short,2=int,3=long,4=obj), op1 = value (any index)
    public static final byte OP_GET_RET                                  = 91;  // FOLLOWED UP BY 1 BYTE (0=byte,1=short,2=int,3=long,4=obj), res = new stack item on either byte, short, int, long, or obj
    
    public static final byte OP_ASSIGN_BYTE                              = 92;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new byte
    public static final byte OP_ASSIGN_SHORT                             = 93;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new short
    public static final byte OP_ASSIGN_INT                               = 94;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_ASSIGN_LONG                              = 95;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    public static final byte OP_ASSIGN_OPERAND                           = 96;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new operand
    public static final byte OP_ASSIGN_OBJECT                            = 97;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new object
    
    public static final byte OP_EQUALS_BYTE                              = 98;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_EQUALS_SHORT                             = 99;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_EQUALS_INT                               = 100;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_EQUALS_LONG                              = 101;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_EQUALS_OBJECT                            = 102;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    
    public static final byte OP_NOT_EQUAL_BYTE                           = 103;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_NOT_EQUAL_SHORT                          = 104;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_NOT_EQUAL_INT                            = 105;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_NOT_EQUAL_LONG                           = 106;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_NOT_EQUAL_OBJECT                         = 107;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    
    public static final byte OP_LESS_THAN_BYTE                           = 108;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_LESS_THAN_SHORT                          = 109;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_LESS_THAN_INT                            = 110;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_LESS_THAN_LONG                           = 111;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    
    public static final byte OP_GREATER_THAN_BYTE                        = 112;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_GREATER_THAN_SHORT                       = 113;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_GREATER_THAN_INT                         = 114;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte OP_GREATER_THAN_LONG                        = 115;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    
    public static final byte OP_ADDITION_BYTE                            = 116;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_ADDITION_SHORT                           = 117;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_ADDITION_INT                             = 118;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_ADDITION_LONG                            = 119;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte OP_SUBTRACT_BYTE                            = 120;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_SUBTRACT_SHORT                           = 121;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_SUBTRACT_INT                             = 122;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_SUBTRACT_LONG                            = 123;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte OP_MULTIPLY_BYTE                            = 124;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_MULTIPLY_SHORT                           = 125;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_MULTIPLY_INT                             = 126;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_MULTIPLY_LONG                            = 127;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte OP_DIVIDE_BYTE                              = -128;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_DIVIDE_SHORT                             = -127;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_DIVIDE_INT                               = -126;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_DIVIDE_LONG                              = -125;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte OP_MODULUS_BYTE                             = -124;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_MODULUS_SHORT                            = -123;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_MODULUS_INT                              = -122;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_MODULUS_LONG                             = -121;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte OP_SHIFT_LEFT_BYTE                          = -120;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_SHIFT_LEFT_SHORT                         = -119;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_SHIFT_LEFT_INT                           = -118;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_SHIFT_LEFT_LONG                          = -117;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte OP_SHIFT_RIGHT_BYTE                         = -116;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_SHIFT_RIGHT_SHORT                        = -115;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_SHIFT_RIGHT_INT                          = -114;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_SHIFT_RIGHT_LONG                         = -113;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte OP_AND_BYTE                                 = -112;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_AND_SHORT                                = -111;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_AND_INT                                  = -110;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_AND_LONG                                 = -109;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte OP_OR_BYTE                                  = -108;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_OR_SHORT                                 = -107;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_OR_INT                                   = -106;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_OR_LONG                                  = -105;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte OP_NOT_BYTE                                 = -104;  // FOLLOWED UP BY 4 BYTE (2bytes=index1), res = new int
    public static final byte OP_NOT_SHORT                                = -103;  // FOLLOWED UP BY 4 BYTE (2bytes=index1), res = new int
    public static final byte OP_NOT_INT                                  = -102;  // FOLLOWED UP BY 4 BYTE (2bytes=index1), res = new int
    public static final byte OP_NOT_LONG                                 = -101;  // FOLLOWED UP BY 4 BYTE (2bytes=index1), res = new long
    
    public static final byte OP_XOR_BYTE                                 = -100;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_XOR_SHORT                                = -99;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_XOR_INT                                  = -98;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte OP_XOR_LONG                                 = -97;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte OP_PLACE_CONST_BYTE                         = -96;  // FOLLOWED UP BY 3 BYTE (2bytes=index1, 1bytes=constant), res = new byte
    public static final byte OP_PLACE_CONST_SHORT                        = -95;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=constant), res = new short
    public static final byte OP_PLACE_CONST_INT                          = -94;  // FOLLOWED UP BY 6 BYTE (2bytes=index1, 4bytes=constant), res = new int
    public static final byte OP_PLACE_CONST_LONG                         = -93;  // FOLLOWED UP BY 10 BYTE (2bytes=index1, 8bytes=constant), res = new long
    
    public static final byte OP_INC_BYTE                                 = -92;  // FOLLOWED UP BY 3 BYTE (2bytes=index, 1bytes=signed constant to add), res = new byte
    public static final byte OP_INC_SHORT                                = -91;  // FOLLOWED UP BY 3 BYTE (2bytes=index, 1bytes=signed constant to add), res = new short
    public static final byte OP_INC_INT                                  = -90;  // FOLLOWED UP BY 3 BYTE (2bytes=index, 1bytes=signed constant to add), res = new int
    public static final byte OP_INC_LONG                                 = -89;  // FOLLOWED UP BY 3 BYTE (2bytes=index, 1bytes=signed constant to add), res = new long

    public static final byte OP_AND_CONST_BYTE                           = -88;  // FOLLOWED UP BY 3 BYTE (2bytes=index1, 1bytes=constant), res = new int
    public static final byte OP_AND_CONST_SHORT                          = -87;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=constant), res = new int
    public static final byte OP_AND_CONST_INT                            = -86;  // FOLLOWED UP BY 6 BYTE (2bytes=index1, 4bytes=constant), res = new int
    public static final byte OP_AND_CONST_LONG                           = -85;  // FOLLOWED UP BY 10 BYTE (2bytes=index1, 8bytes=constant), res = new long
    
    public static final byte OP_OR_CONST_BYTE                            = -84;  // FOLLOWED UP BY 3 BYTE (2bytes=index1, 1bytes=constant), res = new int
    public static final byte OP_OR_CONST_SHORT                           = -83;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=constant), res = new int
    public static final byte OP_OR_CONST_INT                             = -82;  // FOLLOWED UP BY 6 BYTE (2bytes=index1, 4bytes=constant), res = new int
    public static final byte OP_OR_CONST_LONG                            = -81;  // FOLLOWED UP BY 10 BYTE (2bytes=index1, 8bytes=constant), res = new long
    
    public static final byte OP_SHIFT_LEFT_CONST_BYTE                    = -80;  // FOLLOWED UP BY 3 BYTE (2bytes=index1, 1bytes=constant), res = new int
    public static final byte OP_SHIFT_LEFT_CONST_SHORT                   = -79;  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=constant), res = new int
    public static final byte OP_SHIFT_LEFT_CONST_INT                     = -78;  // FOLLOWED UP BY 6 BYTE (2bytes=index1, 4bytes=constant), res = new int
    public static final byte OP_SHIFT_LEFT_CONST_LONG                    = -77;   // FOLLOWED UP BY 10 BYTE (2bytes=index1, 8bytes=constant), res = new long
    
    public static final byte OP_SHIFT_RIGHT_CONST_BYTE                   = -76;   // FOLLOWED UP BY 3 BYTE (2bytes=index1, 1bytes=constant), res = new int
    public static final byte OP_SHIFT_RIGHT_CONST_SHORT                  = -75;   // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=constant), res = new int    
    public static final byte OP_SHIFT_RIGHT_CONST_INT                    = -74;   // FOLLOWED UP BY 6 BYTE (2bytes=index1, 4bytes=constant), res = new int
    public static final byte OP_SHIFT_RIGHT_CONST_LONG                   = -73;   // FOLLOWED UP BY 10 BYTE (2bytes=index1, 8bytes=constant), res = new long
    
    public static final byte OP_XOR_CONST_BYTE                           = -72;
    public static final byte OP_XOR_CONST_SHORT                          = -71;
    public static final byte OP_XOR_CONST_INT                            = -70;
    public static final byte OP_XOR_CONST_LONG                           = -69;
    
    public static final byte OP_EQUALS_CONST_BYTE                        = -68;
    public static final byte OP_EQUALS_CONST_SHORT                       = -67;
    public static final byte OP_EQUALS_CONST_INT                         = -66;
    public static final byte OP_EQUALS_CONST_LONG                        = -65;
    
    public static final byte OP_NOT_EQUAL_CONST_BYTE                     = -64;
    public static final byte OP_NOT_EQUAL_CONST_SHORT                    = -63;
    public static final byte OP_NOT_EQUAL_CONST_INT                      = -62;
    public static final byte OP_NOT_EQUAL_CONST_LONG                     = -61;
    
    public static final byte OP_LESS_THAN_CONST_BYTE                     = -60;
    public static final byte OP_LESS_THAN_CONST_SHORT                    = -59;
    public static final byte OP_LESS_THAN_CONST_INT                      = -58;
    public static final byte OP_LESS_THAN_CONST_LONG                     = -57;
    
    public static final byte OP_GREATER_THAN_CONST_BYTE                  = -56;
    public static final byte OP_GREATER_THAN_CONST_SHORT                 = -55;
    public static final byte OP_GREATER_THAN_CONST_INT                   = -54;
    public static final byte OP_GREATER_THAN_CONST_LONG                  = -53;
    
    public static final byte OP_ADDITION_CONST_BYTE                      = -52;
    public static final byte OP_ADDITION_CONST_SHORT                     = -51;
    public static final byte OP_ADDITION_CONST_INT                       = -50;
    public static final byte OP_ADDITION_CONST_LONG                      = -49;
    
    public static final byte OP_SUBTRACT_CONST_BYTE                      = -48;
    public static final byte OP_SUBTRACT_CONST_SHORT                     = -47;
    public static final byte OP_SUBTRACT_CONST_INT                       = -46;
    public static final byte OP_SUBTRACT_CONST_LONG                      = -45;
    
    public static final byte OP_MULTIPLY_CONST_BYTE                      = -44;
    public static final byte OP_MULTIPLY_CONST_SHORT                     = -43;
    public static final byte OP_MULTIPLY_CONST_INT                       = -42;
    public static final byte OP_MULTIPLY_CONST_LONG                      = -41;
    
    public static final byte OP_DIVIDE_CONST_BYTE                        = -40;
    public static final byte OP_DIVIDE_CONST_SHORT                       = -39;
    public static final byte OP_DIVIDE_CONST_INT                         = -38;
    public static final byte OP_DIVIDE_CONST_LONG                        = -37;
    
    public static final byte OP_MODULUS_CONST_BYTE                       = -36;
    public static final byte OP_MODULUS_CONST_SHORT                      = -35;
    public static final byte OP_MODULUS_CONST_INT                        = -34;
    public static final byte OP_MODULUS_CONST_LONG                       = -32;
    
    public static final byte OP_OBJECT_EQUALS_METHOD                     = -31;   // FOLLOWED UP BY 4 BYTE (2bytes=objectindex1, 2bytes=objectindex1), res = condition flag changed
    
    public static final byte OP_2_CONST_OPERAND                          = -30;   // FOLLOWED UP BY 4 BYTE, res = 2 new operand (followup)
    public static final byte OP_3_CONST_OPERAND                          = -29;   // FOLLOWED UP BY 6 BYTE, res = 3 new operand (followup)
    public static final byte OP_4_CONST_OPERAND                          = -28;   // FOLLOWED UP BY 8 BYTE, res = 4 new operand (followup)

    public static final byte OP_SUB_OP_ONE                               = -27;
    
    public static final byte SUB_OP_ONE_GAME_WHITE_OUT                   = 4;
    
    public static final byte SUB_OP_ONE_GAME_RAND                        = 5;
    public static final byte SUB_OP_ONE_GAME_RAND_MAX                    = 6;
    public static final byte SUB_OP_ONE_GAME_RAND_MIN_MAX                = 7;
    public static final byte SUB_OP_ONE_GAME_RAND_SEED                   = 8;
    
    public static final byte SUB_OP_ONE_GAME_SET_TIMER                   = 9;
    public static final byte SUB_OP_ONE_GAME_RELEASE_TIMER               = 10;
    
    public static final byte SUB_OP_ONE_GAME_SET_GRAVITY                 = 12;
    public static final byte SUB_OP_ONE_GAME_SET_CAMERA_Y                = 13;
    public static final byte SUB_OP_ONE_GAME_SET_SHAKE                   = 14;
    public static final byte SUB_OP_ONE_GAME_SET_SLOWDOWN_RATE           = 15;
    public static final byte SUB_OP_ONE_GAME_SET_STATE                   = 16;
    public static final byte SUB_OP_ONE_GAME_SET_BACKGROUND_COLOR        = 17;
    public static final byte SUB_OP_ONE_GAME_SET_INC_POWER_TIME          = 18;
    
    public static final byte SUB_OP_ONE_GAME_GET_GRAVITY                 = 19;
    public static final byte SUB_OP_ONE_GAME_GET_CAMERA_Y                = 20;
    public static final byte SUB_OP_ONE_GAME_GET_SHAKE                   = 21;
    public static final byte SUB_OP_ONE_GAME_GET_SLOWDOWN_RATE           = 22;
    public static final byte SUB_OP_ONE_GAME_GET_STATE                   = 23;
    public static final byte SUB_OP_ONE_GAME_GET_BACKGROUND_COLOR        = 24;
    public static final byte SUB_OP_ONE_GAME_GET_INC_POWER_TIME          = 25;
    
    public static final byte SUB_OP_ONE_GAME_ON_BLOCK_STEP               = 26;
    
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_XY_POS           = 32;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_X_POS            = 33;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_Y_POS            = 34;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_X_RATE           = 35;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_Y_RATE           = 36;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_GRAV_AFFECTED    = 37;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_LEFT_BOUND       = 38;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_BOUND      = 39;
    
    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_XY_POS           = 40;
    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_X_POS            = 41;
    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_Y_POS            = 42;
    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_X_RATE           = 43;
    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_Y_RATE           = 44;
    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_GRAV_AFFECTED    = 45;
    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_LEFT_BOUND       = 46;
    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_RIGHT_BOUND      = 47;
    
    public static final byte SUB_OP_ONE_GAME_WHITE_OUT_STATIC            = 51;
    
    public static final byte SUB_OP_ONE_GAME_RAND_MAX_STATIC             = 52;
    public static final byte SUB_OP_ONE_GAME_RAND_MIN_MAX_STATIC         = 53;
    public static final byte SUB_OP_ONE_GAME_RAND_SEED_STATIC            = 54;
    
    public static final byte SUB_OP_ONE_GAME_SET_TIMER_STATIC            = 55;
    public static final byte SUB_OP_ONE_GAME_RELEASE_TIMER_STATIC        = 56;
    
    public static final byte SUB_OP_ONE_GAME_SET_GRAVITY_STATIC          = 57;
    public static final byte SUB_OP_ONE_GAME_SET_CAMERA_Y_STATIC         = 58;
    public static final byte SUB_OP_ONE_GAME_SET_SHAKE_STATIC            = 59;
    public static final byte SUB_OP_ONE_GAME_SET_SLOWDOWN_RATE_STATIC    = 60;
    public static final byte SUB_OP_ONE_GAME_SET_STATE_STATIC            = 61;
    public static final byte SUB_OP_ONE_GAME_SET_BACKGROUND_COLOR_STATIC = 62;
    public static final byte SUB_OP_ONE_GAME_SET_INC_POWER_TIME_STATIC   = 63;
    
    public static final byte SUB_OP_ONE_GAME_ON_BLOCK_STEP_STATIC        = 64;
    
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_XY_POS_STATIC    = 68;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_X_POS_STATIC     = 69;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_Y_POS_STATIC     = 70;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_X_RATE_STATIC    = 71;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_Y_RATE_STATIC    = 72;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_GRAV_AFFECTED_STATIC = 73;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_LEFT_BOUND_STATIC = 74;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_BOUND_STATIC = 75;
    
    public static final byte SUB_OP_ONE_GAME_GET_TOTAL_HEIGHT            = 76;
    public static final byte SUB_OP_ONE_GAME_GET_WORKING_HEIGHT          = 77;
    public static final byte SUB_OP_ONE_GAME_GET_WORKING_TOP_Y           = 78;
    
    public static final byte SUB_OP_ONE_GAME_ON_LEFT_HIT                 = 79;
    public static final byte SUB_OP_ONE_GAME_ON_RIGHT_HIT                = 80;
    public static final byte SUB_OP_ONE_GAME_ON_LEFT_HIT_STATIC          = 81;
    public static final byte SUB_OP_ONE_GAME_ON_RIGHT_HIT_STATIC         = 82;
    
    public static final byte SUB_OP_ONE_GAME_GET_LEVEL_DATA              = 83;
    public static final byte SUB_OP_ONE_GAME_GET_GAME_FLAGS              = 84;
    public static final byte SUB_OP_ONE_GAME_SET_GAME_FLAGS              = 85;
    public static final byte SUB_OP_ONE_GAME_SET_GAME_FLAGS_STATIC       = 86;
    
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_STAND = 87;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_LEFT_STAND       = 88;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_STAND      = 89;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_STAND_STATIC = 90;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_LEFT_STAND_STATIC       = 91;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_STAND_STATIC      = 92;
    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_LEFT_RIGHT_STAND = 93;
    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_LEFT_STAND       = 94;
    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_RIGHT_STAND      = 95;
    
    public static final byte SUB_OP_ONE_GAME_ON_DIE                      = 96;
    public static final byte SUB_OP_ONE_GAME_ON_DIE_STATIC               = 97;
    
    public static final byte SUB_OP_ONE_GAME_GET_GAME_FLAGS_2            = 98;
    public static final byte SUB_OP_ONE_GAME_SET_GAME_FLAGS_2            = 99;
    public static final byte SUB_OP_ONE_GAME_SET_GAME_FLAGS_2_STATIC     = 100;
    
    public static final byte SUB_OP_ONE_GAME_GET_CURRENT_FLOOR_NUMBER    = 101;
    
    public static final byte SUB_OP_ONE_GAME_IS_TIMER_ACTIVE             = 102;
    public static final byte SUB_OP_ONE_GAME_IS_TIMER_ACTIVE_STATIC      = 103;
    
    public static final byte SUB_OP_ONE_GAME_GET_BLOCK_REF_PIXEL         = 104;
    public static final byte SUB_OP_ONE_GAME_SET_BLOCK_REF_PIXEL         = 105;
    public static final byte SUB_OP_ONE_GAME_SET_BLOCK_REF_PIXEL_STATIC  = 106;
    
    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_REF_PIXEL        = 107;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_REF_PIXEL        = 108;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_REF_PIXEL_STATIC = 109;
    
    public static final byte SUB_OP_ONE_GAME_SET_BLOCK_FRAME_SEQ         = 110;
    public static final byte SUB_OP_ONE_GAME_SET_BLOCK_TIME_SEQ          = 111;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_FRAME_SEQ        = 112;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_ONLY  = 113;
    
    public static final byte SUB_OP_ONE_GAME_GET_POWER                   = 114;
    public static final byte SUB_OP_ONE_GAME_SET_POWER                   = 115;
    public static final byte SUB_OP_ONE_GAME_SET_POWER_STATIC            = 116;
    
    public static final byte SUB_OP_ONE_GAME_ON_BLOCK_DEPART             = 117;
    public static final byte SUB_OP_ONE_GAME_ON_BLOCK_DEPART_STATIC      = 118;
    
    public static final byte SUB_OP_ONE_GAME_GET_RET                     = 119;
    public static final byte SUB_OP_ONE_GAME_SET_RET                     = 120;
    public static final byte SUB_OP_ONE_GAME_SET_RET_STATIC              = 121;
    
    public static final byte SUB_OP_ONE_GAME_LOAD_FROM_JAR               = 122;
    
    public static final byte SUB_OP_ONE_GAME_NEW_MIDI                    = 123;
    public static final byte SUB_OP_ONE_GAME_START_MIDI                  = 124;
    public static final byte SUB_OP_ONE_GAME_STOP_MIDI                   = 125;
    public static final byte SUB_OP_ONE_GAME_CLOSE_MIDI                  = 126;
    
    public static final byte SUB_OP_ONE_GAME_FLASH_BACKLIGHT             = 127;
    public static final byte SUB_OP_ONE_GAME_VIBRATE                     = -128;

    public static final byte SUB_OP_ONE_GAME_FLASH_BACKLIGHT_STATIC      = -127;
    public static final byte SUB_OP_ONE_GAME_VIBRATE_STATIC              = -126;

    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_WIDTH              = -125;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_WIDTH_PIXEL        = -124;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_WIDTH_PIXEL_STATIC = -123;
    
    public static final byte SUB_OP_ONE_GAME_GET_PLAYER_HEIGHT              = -122;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_HEIGHT_PIXEL        = -121;
    public static final byte SUB_OP_ONE_GAME_SET_PLAYER_HEIGHT_PIXEL_STATIC = -120;
    
    public static final byte SUB_OP_ONE_GAME_GET_BLOCK_COLL_OFFSET         = -119;
    public static final byte SUB_OP_ONE_GAME_SET_BLOCK_COLL_OFFSET         = -118;
    public static final byte SUB_OP_ONE_GAME_SET_BLOCK_COLL_OFFSET_STATIC  = -117;
    
    public static final byte SUB_OP_ONE_GAME_SET_LEVEL_DATA              = -116;
    
    public static final byte SUB_OP_ONE_GAME_SCENE_RESIZE_LAYER_ITEMS                   = -115;
    public static final byte SUB_OP_ONE_GAME_SCENE_RESIZE_LAYER                         = -114;
    public static final byte SUB_OP_ONE_GAME_SCENE_RESIZE_DB                            = -113;
    
    public static final byte SUB_OP_ONE_GAME_SCENE_LOAD_NEW_INTERPOLATION_POINTS        = -112;
    public static final byte SUB_OP_ONE_GAME_SCENE_LOAD_NEW_IMAGE                       = -111;
    public static final byte SUB_OP_ONE_GAME_SCENE_LOAD_NEW_SPRITE                      = -110;
    public static final byte SUB_OP_ONE_GAME_SCENE_LOAD_NEW_SEQUENCE                    = -109;
    
    public static final byte SUB_OP_ONE_GAME_SCENE_GET_IMAGE_COUNT                      = -108;
    public static final byte SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATOR_COUNT               = -107;
    public static final byte SUB_OP_ONE_GAME_SCENE_GET_SEQUENCE_COUNT                   = -106;
    public static final byte SUB_OP_ONE_GAME_SCENE_GET_SPRITE_COUNT                     = -105;
    public static final byte SUB_OP_ONE_GAME_SCENE_GET_SPRITE_BLOCK_INDEX               = -104;
    public static final byte SUB_OP_ONE_GAME_SCENE_GET_SPRITE_BLOCK_INDEX_STATIC        = -103;
    
    public static final byte SUB_OP_ONE_GAME_SCENE_GET_ITEM_LENGTH                      = -102;
    public static final byte SUB_OP_ONE_GAME_SCENE_GET_LAYER_LENGTH                     = -101;
    public static final byte SUB_OP_ONE_GAME_SCENE_GET_LAYER_PARAM                      = -100;
    public static final byte SUB_OP_ONE_GAME_SCENE_IS_INTERPOLATOR_DONE                 = -99;
    public static final byte SUB_OP_ONE_GAME_SCENE_IS_INTERPOLATOR_DONE_STATIC          = -98;
    public static final byte SUB_OP_ONE_GAME_SCENE_IS_LAYER_VISIBLE                     = -97;
    public static final byte SUB_OP_ONE_GAME_SCENE_IS_LAYER_VISIBLE_STATIC              = -96;
    public static final byte SUB_OP_ONE_GAME_SCENE_IS_SPRITE_LINK_VISIBLE               = -95;
    public static final byte SUB_OP_ONE_GAME_SCENE_IS_SPRITE_LINK_VISIBLE_STATIC        = -94;
    
    public static final byte SUB_OP_ONE_GAME_SCENE_SET_LAYER_PARAM                      = -93;
    public static final byte SUB_OP_ONE_GAME_SCENE_SET_LAYER_VISIBILITY                 = -92;
    public static final byte SUB_OP_ONE_GAME_SCENE_SET_LAYER_VISIBILITY_STATIC          = -91;
    
    public static final byte SUB_OP_ONE_GAME_SCENE_SWITCH_LAYERS                        = -90;
    public static final byte SUB_OP_ONE_GAME_SCENE_SWITCH_LAYERS_STATIC                 = -89;
    public static final byte SUB_OP_ONE_GAME_SCENE_FLIP_SETS                            = -88;
    
    public static final byte SUB_OP_ONE_GAME_SCENE_SWITCH_SPRITE_SEQUENCE               = -87;
    public static final byte SUB_OP_ONE_GAME_SCENE_SWITCH_SPRITE_SEQUENCE_STATIC        = -86;
    
    public static final byte SUB_OP_ONE_GAME_SCENE_RESET_INTERPOLATION_POINT            = -85;
    public static final byte SUB_OP_ONE_GAME_SCENE_RESET_INTERPOLATION_POINT_STATIC     = -84;
    
    public static final byte SUB_OP_ONE_GAME_SCENE_GET_ITEM                             = -83;
    public static final byte SUB_OP_ONE_GAME_SCENE_GET_ITEM_STATIC                      = -82;
    
    public static final byte SUB_OP_ONE_GAME_SCENE_SET_ITEM                             = -81;
    public static final byte SUB_OP_ONE_GAME_SCENE_SET_ITEM_STATIC                      = -80;
    
    public static final byte SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATION_POINT              = -79;
    public static final byte SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATION_POINT_STATIC       = -78;
    
    public static final byte SUB_OP_ONE_ARRAY_COPY                                      = -77;
    
    public static final byte SUB_OP_ONE_ARRAY_BYTE_TO_SHORT                             = -76;
    public static final byte SUB_OP_ONE_ARRAY_BYTE_TO_INT                               = -75;
    public static final byte SUB_OP_ONE_ARRAY_BYTE_TO_LONG                              = -74;
    
    public static final byte SUB_OP_ONE_ARRAY_SHORT_TO_BYTE                             = -73;
    public static final byte SUB_OP_ONE_ARRAY_INT_TO_BYTE                               = -72;
    public static final byte SUB_OP_ONE_ARRAY_LONG_TO_BYTE                              = -71;
    
    public static final byte SUB_OP_ONE_HT_NEW                                          = -70;
    public static final byte SUB_OP_ONE_HT_NEW_INITIALCAP                               = -69;
    public static final byte SUB_OP_ONE_HT_CLEAR                                        = -68;
    public static final byte SUB_OP_ONE_HT_CONTAINS                                     = -67;
    public static final byte SUB_OP_ONE_HT_CONTAINSKEY                                  = -66;
    public static final byte SUB_OP_ONE_HT_ELEMENTS                                     = -65;
    public static final byte SUB_OP_ONE_HT_GET                                          = -64;
    public static final byte SUB_OP_ONE_HT_ISEMPTY                                      = -63;
    public static final byte SUB_OP_ONE_HT_KEYS                                         = -62;
    public static final byte SUB_OP_ONE_HT_PUT                                          = -61;
    public static final byte SUB_OP_ONE_HT_REMOVE                                       = -60;
    public static final byte SUB_OP_ONE_HT_SIZE                                         = -59;
}