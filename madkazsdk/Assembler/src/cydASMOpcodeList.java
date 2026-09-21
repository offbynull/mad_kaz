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

public class cydASMOpcodeList {
    public static final byte []OP_CONST_BYTE                               = new byte [] {0};    // FOLLOWED UP BY 1 BYTE, res = new byte (followup)
    public static final byte []OP_CONST_SHORT                              = new byte [] {1};    // FOLLOWED UP BY 2 BYTE, res = new short (followup)
    public static final byte []OP_CONST_INT                                = new byte [] {2};    // FOLLOWED UP BY 4 BYTE, res = new int (followup)
    public static final byte []OP_CONST_LONG                               = new byte [] {3};    // FOLLOWED UP BY 8 BYTE, res = new long (followup)
    public static final byte []OP_CONST_OPERAND                            = new byte [] {4};    // FOLLOWED UP BY 2 BYTE, res = new operand (followup)
    public static final byte []OP_CONST_STRING                             = new byte [] {5};    // FOLLOWED UP BY n BYTE, res = new string (utf8 dump)
    
    public static final byte []OP_NEW_BYTE_ARRAY                           = new byte [] {6};    // op1 = size of array (int index), res = new byte array
    public static final byte []OP_NEW_SHORT_ARRAY                          = new byte [] {7};    // op1 = size of array (int index), res = new short array
    public static final byte []OP_NEW_INT_ARRAY                            = new byte [] {8};    // op1 = size of array (int index), res = new int array
    public static final byte []OP_NEW_LONG_ARRAY                           = new byte [] {9};    // op1 = size of array (int index), res = new long array
    public static final byte []OP_NEW_OBJECT_ARRAY                         = new byte [] {10};   // op1 = size of array (int index), res = new object array
    
    public static final byte []OP_POP_BYTE                                 = new byte [] {11};   // res = remove most recent byte
    public static final byte []OP_POP_SHORT                                = new byte [] {12};   // res = remove most recent short
    public static final byte []OP_POP_INT                                  = new byte [] {13};   // res = remove most recent int
    public static final byte []OP_POP_LONG                                 = new byte [] {14};   // res = remove most recent long
    public static final byte []OP_POP_OPERAND                              = new byte [] {15};   // res = remove most recent operand
    public static final byte []OP_POP_OBJECT                               = new byte [] {16};   // res = remove most recent object
    
    public static final byte []OP_CONV_BYTE_TO_SHORT                       = new byte [] {17};   // FOLLOWED UP BY 2 BYTE (byte index), res = new short
    public static final byte []OP_CONV_BYTE_TO_INT                         = new byte [] {18};   // FOLLOWED UP BY 2 BYTE (byte index), res = new int
    public static final byte []OP_CONV_BYTE_TO_LONG                        = new byte [] {19};   // FOLLOWED UP BY 2 BYTE (byte index), res = new long
    public static final byte []OP_CONV_BYTE_TO_OPERAND                     = new byte [] {20};   // FOLLOWED UP BY 2 BYTE (byte index), res = new operand
    public static final byte []OP_CONV_SHORT_TO_BYTE                       = new byte [] {21};   // FOLLOWED UP BY 2 BYTE (short index), res = new byte
    public static final byte []OP_CONV_SHORT_TO_INT                        = new byte [] {22};   // FOLLOWED UP BY 2 BYTE (short index), res = new int
    public static final byte []OP_CONV_SHORT_TO_LONG                       = new byte [] {23};   // FOLLOWED UP BY 2 BYTE (short index), res = new long
    public static final byte []OP_CONV_SHORT_TO_OPERAND                    = new byte [] {24};   // FOLLOWED UP BY 2 BYTE (short index), res = new operand
    public static final byte []OP_CONV_INT_TO_BYTE                         = new byte [] {25};   // FOLLOWED UP BY 2 BYTE (int index), res = new byte
    public static final byte []OP_CONV_INT_TO_SHORT                        = new byte [] {26};   // FOLLOWED UP BY 2 BYTE (int index), res = new short
    public static final byte []OP_CONV_INT_TO_LONG                         = new byte [] {27};   // FOLLOWED UP BY 2 BYTE (int index), res = new long
    public static final byte []OP_CONV_INT_TO_OPERAND                      = new byte [] {28};   // FOLLOWED UP BY 2 BYTE (int index), res = new operand
    public static final byte []OP_CONV_LONG_TO_BYTE                        = new byte [] {29};   // FOLLOWED UP BY 2 BYTE (long index), res = new byte
    public static final byte []OP_CONV_LONG_TO_SHORT                       = new byte [] {30};   // FOLLOWED UP BY 2 BYTE (long index), res = new short
    public static final byte []OP_CONV_LONG_TO_INT                         = new byte [] {31};   // FOLLOWED UP BY 2 BYTE (long index), res = new int
    public static final byte []OP_CONV_LONG_TO_OPERAND                     = new byte [] {32};   // FOLLOWED UP BY 2 BYTE (long index), res = new operand
    public static final byte []OP_CONV_OPERAND_TO_BYTE                     = new byte [] {33};   // FOLLOWED UP BY 2 BYTE (operand index), res = new byte
    public static final byte []OP_CONV_OPERAND_TO_SHORT                    = new byte [] {34};   // FOLLOWED UP BY 2 BYTE (operand index), res = new short
    public static final byte []OP_CONV_OPERAND_TO_INT                      = new byte [] {35};   // FOLLOWED UP BY 2 BYTE (operand index), res = new int
    public static final byte []OP_CONV_OPERAND_TO_LONG                     = new byte [] {36};   // FOLLOWED UP BY 2 BYTE (operand index), res = new long
    
    public static final byte []OP_SUB_ASSIGN_BYTE                          = new byte [] {37};   // FOLLOWED UP BY 4 BYTE (object index, byte index), op1 = index of array (int index)
    public static final byte []OP_SUB_ASSIGN_SHORT                         = new byte [] {38};   // FOLLOWED UP BY 4 BYTE (object index, short index), op1 = index of array (int index)
    public static final byte []OP_SUB_ASSIGN_INT                           = new byte [] {39};   // FOLLOWED UP BY 4 BYTE (object index, int index), op1 = index of array (int index)
    public static final byte []OP_SUB_ASSIGN_LONG                          = new byte [] {40};   // FOLLOWED UP BY 4 BYTE (object index, long index), op1 = index of array (int index)
    public static final byte []OP_SUB_ASSIGN_OBJECT                        = new byte [] {41};   // FOLLOWED UP BY 4 BYTE (object index, object index), op1 = index of array (int index)
    
    public static final byte []OP_SUB_EXTRACT_BYTE                         = new byte [] {42};   // FOLLOWED UP BY 4 BYTE (object index), op1 = index of array (int index), res = copy of byte in array
    public static final byte []OP_SUB_EXTRACT_SHORT                        = new byte [] {43};   // FOLLOWED UP BY 4 BYTE (object index), op1 = index of array (int index), res = copy of short in array
    public static final byte []OP_SUB_EXTRACT_INT                          = new byte [] {44};   // FOLLOWED UP BY 4 BYTE (object index), op1 = index of array (int index), res = copy of int in array
    public static final byte []OP_SUB_EXTRACT_LONG                         = new byte [] {45};   // FOLLOWED UP BY 4 BYTE (object index), op1 = index of array (int index), res = copy of long in array
    public static final byte []OP_SUB_EXTRACT_OBJECT                       = new byte [] {46};   // FOLLOWED UP BY 4 BYTE (object index), op1 = index of array (int index), res = copy of object in array
    
    public static final byte []OP_SIZE_OF                                  = new byte [] {47};  // op1 = value 1 (object index), res = new int
    
    public static final byte []OP_OBJECT_PUSH_NULL                         = new byte [] {48};  // res = new object pushed onto object stack containing null
    public static final byte []OP_OBJECT_EQUALS_NULL                       = new byte [] {49};  // FOLLOWED UP BY 2 BYTES (object index), res = cond changed: 0 for false, 1 for true
    
    public static final byte []OP_CONTROL_CALL                             = new byte [] {50};  // FOLLOWED UP BY 9 BYTES. 1 byte for how much to subtract off of each stack (byte/short/int/long/object). 4 bytes address
    public static final byte []OP_CONTROL_SEMI_DYNAMIC_CALL                = new byte [] {51};  // FOLLOWED UP BY 5 BYTES. 1 byte for how much to subtract off of each stack (byte/short/int/long/object). op1 = address (int index)
    public static final byte []OP_CONTROL_FULL_DYNAMIC_CALL                = new byte [] {52};  // op1 = address (int index), op 2-6 = how much to subtract off of each stack (byte index * 5)
    public static final byte []OP_CONTROL_RET                              = new byte [] {53};  // res = reset stack tops
    public static final byte []OP_CONTROL_JUMP                             = new byte [] {54};  // FOLLOWED UP BY 4 BYTES representing address. res = pc changed to op1
    public static final byte []OP_CONTROL_DYNAMIC_JUMP                     = new byte [] {55};  // op1 = position (int index), res = pc changed to op1
    public static final byte []OP_CONTROL_JUMP_CONDITIONAL                 = new byte [] {56};  // FOLLOWED UP BY 4 BYTES representing address. res = pc changed to op1 if cond is false
    public static final byte []OP_CONTROL_DYNAMIC_JUMP_CONDITIONAL         = new byte [] {57};  // op1 = position (int index) res = pc changed to op1 if cond is false
    public static final byte []OP_CONTROL_SET_TRAP                         = new byte [] {58};  // FOLLOWED UP BY n+4 BYTE representing trap string being set, catch position (int index)
    public static final byte []OP_CONTROL_THROW_TRAP                       = new byte [] {59}; // FOLLOWED UP BY n BYTE representing trap being thrown
    public static final byte []OP_CONTROL_RELEASE_TRAP                     = new byte [] {60};
    public static final byte []OP_CONTROL_EXIT                             = new byte [] {61};
    
    public static final byte []OP_SYSTEM_ID_STRING                         = new byte [] {62}; // res = new string giving you subsystem id
    public static final byte []OP_SYSTEM_TIME                              = new byte [] {63}; // res = time in ms
    public static final byte []OP_SYSTEM_OUT                               = new byte [] {64}; // op1 = string to output (object index)
    public static final byte []OP_SYSTEM_SHARED                            = new byte [] {65}; // res = hashmap
    public static final byte []OP_SYSTEM_INTERRUPT_CALL                    = new byte [] {66}; // FOLLOWED UP BY 2 BYTES. 1 byte for which interrupt system to call, 1 byte for the id of the call
    public static final byte []OP_SYSTEM_DYNAMIC_INTERRUPT_CALL            = new byte [] {67}; // op1 = interrupt system (byte index), op2 = id of the interrupt to call
    public static final byte []OP_SYSTEM_INTERRUPT_AVAILABLE               = new byte [] {68}; // FOLLOWED UP BY 1 BYTE for which interrupt system to call, res = cond changed: 0 for false, 1 for true
    public static final byte []OP_SYSTEM_DYNAMIC_INTERRUPT_AVAILABLE       = new byte [] {69}; // op1 = interrupt system (byte index), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_SYSTEM_GC                                = new byte [] {70};
    
    public static final byte []OP_COPY_BYTE                                = new byte [] {71}; // FOLLOWED UP BY 2 BYTE, res = new byte (followup)
    public static final byte []OP_COPY_SHORT                               = new byte [] {72}; // FOLLOWED UP BY 2 BYTE, res = new short (followup)
    public static final byte []OP_COPY_INT                                 = new byte [] {73}; // FOLLOWED UP BY 2 BYTE, res = new int (followup)
    public static final byte []OP_COPY_LONG                                = new byte [] {74}; // FOLLOWED UP BY 2 BYTE, res = new long (followup)
    public static final byte []OP_COPY_OPERAND                             = new byte [] {75}; // FOLLOWED UP BY 2 BYTE, res = new operand (followup)
    public static final byte []OP_COPY_OBJECT                              = new byte [] {76}; // FOLLOWED UP BY 2 BYTE, res = new object (followup)
    
    public static final byte []OP_GET_COND                                 = new byte [] {77}; // res = condition into byte
    public static final byte []OP_SET_COND                                 = new byte [] {78}; // FOLLOWED UP BY 2 BYTE (byte index to put into cond)
    public static final byte []OP_NOT_COND                                 = new byte [] {80}; // res = condition inverted
    
    public static final byte []OP_MULTI_POP_BYTE                           = new byte [] {81}; // FOLLOWED UP BY 2 BYTE (amount to remove), res = remove op1 bytes
    public static final byte []OP_MULTI_POP_SHORT                          = new byte [] {82}; // FOLLOWED UP BY 2 BYTE (amount to remove), res = remove op1 shorts
    public static final byte []OP_MULTI_POP_INT                            = new byte [] {83};  // FOLLOWED UP BY 2 BYTE (amount to remove), res = remove op1 ints
    public static final byte []OP_MULTI_POP_LONG                           = new byte [] {84};  // FOLLOWED UP BY 2 BYTE (amount to remove), res = remove op1 longs
    public static final byte []OP_MULTI_POP_OPERAND                        = new byte [] {85};  // FOLLOWED UP BY 2 BYTE (amount to remove), res = remove op1 operands
    public static final byte []OP_MULTI_POP_OBJECT                         = new byte [] {86};  // FOLLOWED UP BY 2 BYTE (amount to remove), res = remove op1 objects
    
    public static final byte []OP_RES_LENGTH                               = new byte [] {87};  // op1 = id to copy (short index), res = size of res as int
    public static final byte []OP_SYSTEM_DIRECT_ACCESS_VM_DATA_FILE        = new byte [] {88};  // res = actual data byte array of vm put onto object stack
    public static final byte []OP_SYSTEM_DIRECT_LOAD_RES_POSITION          = new byte [] {89};  // op1 = id to copy (short index), res = pos of res in data file
    
    public static final byte []OP_SET_RET                                  = new byte [] {90};  // FOLLOWED UP BY 1 BYTE (0=byte,1=short,2=int,3=long,4=obj), op1 = value (any index)
    public static final byte []OP_GET_RET                                  = new byte [] {91};  // FOLLOWED UP BY 1 BYTE (0=byte,1=short,2=int,3=long,4=obj), res = new stack item on either byte, short, int, long, or obj
    
    public static final byte []OP_ASSIGN_BYTE                              = new byte [] {92};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new byte
    public static final byte []OP_ASSIGN_SHORT                             = new byte [] {93};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new short
    public static final byte []OP_ASSIGN_INT                               = new byte [] {94};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_ASSIGN_LONG                              = new byte [] {95};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    public static final byte []OP_ASSIGN_OPERAND                           = new byte [] {96};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new operand
    public static final byte []OP_ASSIGN_OBJECT                            = new byte [] {97};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new object
    
    public static final byte []OP_EQUALS_BYTE                              = new byte [] {98};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_EQUALS_SHORT                             = new byte [] {99};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_EQUALS_INT                               = new byte [] {100};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_EQUALS_LONG                              = new byte [] {101};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_EQUALS_OBJECT                            = new byte [] {102};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    
    public static final byte []OP_NOT_EQUAL_BYTE                           = new byte [] {103};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_NOT_EQUAL_SHORT                          = new byte [] {104};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_NOT_EQUAL_INT                            = new byte [] {105};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_NOT_EQUAL_LONG                           = new byte [] {106};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_NOT_EQUAL_OBJECT                         = new byte [] {107};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    
    public static final byte []OP_LESS_THAN_BYTE                           = new byte [] {108};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_LESS_THAN_SHORT                          = new byte [] {109};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_LESS_THAN_INT                            = new byte [] {110};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_LESS_THAN_LONG                           = new byte [] {111};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    
    public static final byte []OP_GREATER_THAN_BYTE                        = new byte [] {112};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_GREATER_THAN_SHORT                       = new byte [] {113};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_GREATER_THAN_INT                         = new byte [] {114};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    public static final byte []OP_GREATER_THAN_LONG                        = new byte [] {115};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = cond changed: 0 for false, 1 for true
    
    public static final byte []OP_ADDITION_BYTE                            = new byte [] {116};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_ADDITION_SHORT                           = new byte [] {117};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_ADDITION_INT                             = new byte [] {118};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_ADDITION_LONG                            = new byte [] {119};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte []OP_SUBTRACT_BYTE                            = new byte [] {120};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_SUBTRACT_SHORT                           = new byte [] {121};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_SUBTRACT_INT                             = new byte [] {122};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_SUBTRACT_LONG                            = new byte [] {123};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte []OP_MULTIPLY_BYTE                            = new byte [] {124};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_MULTIPLY_SHORT                           = new byte [] {125};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_MULTIPLY_INT                             = new byte [] {126};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_MULTIPLY_LONG                            = new byte [] {127};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte []OP_DIVIDE_BYTE                              = new byte [] {-128};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_DIVIDE_SHORT                             = new byte [] {-127};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_DIVIDE_INT                               = new byte [] {-126};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_DIVIDE_LONG                              = new byte [] {-125};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte []OP_MODULUS_BYTE                             = new byte [] {-124};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_MODULUS_SHORT                            = new byte [] {-123};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_MODULUS_INT                              = new byte [] {-122};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_MODULUS_LONG                             = new byte [] {-121};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte []OP_SHIFT_LEFT_BYTE                          = new byte [] {-120};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_SHIFT_LEFT_SHORT                         = new byte [] {-119};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_SHIFT_LEFT_INT                           = new byte [] {-118};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_SHIFT_LEFT_LONG                          = new byte [] {-117};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte []OP_SHIFT_RIGHT_BYTE                         = new byte [] {-116};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_SHIFT_RIGHT_SHORT                        = new byte [] {-115};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_SHIFT_RIGHT_INT                          = new byte [] {-114};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_SHIFT_RIGHT_LONG                         = new byte [] {-113};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte []OP_AND_BYTE                                 = new byte [] {-112};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_AND_SHORT                                = new byte [] {-111};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_AND_INT                                  = new byte [] {-110};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_AND_LONG                                 = new byte [] {-109};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte []OP_OR_BYTE                                  = new byte [] {-108};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_OR_SHORT                                 = new byte [] {-107};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_OR_INT                                   = new byte [] {-106};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_OR_LONG                                  = new byte [] {-105};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte []OP_NOT_BYTE                                 = new byte [] {-104};  // FOLLOWED UP BY 2 BYTE (2bytes=index1), res = new int
    public static final byte []OP_NOT_SHORT                                = new byte [] {-103};  // FOLLOWED UP BY 2 BYTE (2bytes=index1), res = new int
    public static final byte []OP_NOT_INT                                  = new byte [] {-102};  // FOLLOWED UP BY 2 BYTE (2bytes=index1), res = new int
    public static final byte []OP_NOT_LONG                                 = new byte [] {-101};  // FOLLOWED UP BY 2 BYTE (2bytes=index1), res = new long
    
    public static final byte []OP_XOR_BYTE                                 = new byte [] {-100};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_XOR_SHORT                                = new byte [] {-99};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_XOR_INT                                  = new byte [] {-98};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new int
    public static final byte []OP_XOR_LONG                                 = new byte [] {-97};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=index2), res = new long
    
    public static final byte []OP_PLACE_CONST_BYTE                         = new byte [] {-96};  // FOLLOWED UP BY 3 BYTE (2bytes=index1, 1bytes=constant), res = new byte
    public static final byte []OP_PLACE_CONST_SHORT                        = new byte [] {-95};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=constant), res = new short
    public static final byte []OP_PLACE_CONST_INT                          = new byte [] {-94};  // FOLLOWED UP BY 6 BYTE (2bytes=index1, 4bytes=constant), res = new int
    public static final byte []OP_PLACE_CONST_LONG                         = new byte [] {-93};  // FOLLOWED UP BY 10 BYTE (2bytes=index1, 8bytes=constant), res = new long
    
    public static final byte []OP_INC_BYTE                                 = new byte [] {-92};  // FOLLOWED UP BY 3 BYTE (2bytes=index, 1bytes=signed constant to add), res = new byte
    public static final byte []OP_INC_SHORT                                = new byte [] {-91};  // FOLLOWED UP BY 3 BYTE (2bytes=index, 1bytes=signed constant to add), res = new short
    public static final byte []OP_INC_INT                                  = new byte [] {-90};  // FOLLOWED UP BY 3 BYTE (2bytes=index, 1bytes=signed constant to add), res = new int
    public static final byte []OP_INC_LONG                                 = new byte [] {-89};  // FOLLOWED UP BY 3 BYTE (2bytes=index, 1bytes=signed constant to add), res = new long

    public static final byte []OP_AND_CONST_BYTE                           = new byte [] {-88};  // FOLLOWED UP BY 3 BYTE (2bytes=index1, 1bytes=constant), res = new byte
    public static final byte []OP_AND_CONST_SHORT                          = new byte [] {-87};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=constant), res = new short
    public static final byte []OP_AND_CONST_INT                            = new byte [] {-86};  // FOLLOWED UP BY 6 BYTE (2bytes=index1, 4bytes=constant), res = new int
    public static final byte []OP_AND_CONST_LONG                           = new byte [] {-85};  // FOLLOWED UP BY 10 BYTE (2bytes=index1, 8bytes=constant), res = new long
    
    public static final byte []OP_OR_CONST_BYTE                            = new byte [] {-84};  // FOLLOWED UP BY 3 BYTE (2bytes=index1, 1bytes=constant), res = new byte
    public static final byte []OP_OR_CONST_SHORT                           = new byte [] {-83};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=constant), res = new short
    public static final byte []OP_OR_CONST_INT                             = new byte [] {-82};  // FOLLOWED UP BY 6 BYTE (2bytes=index1, 4bytes=constant), res = new int
    public static final byte []OP_OR_CONST_LONG                            = new byte [] {-81};  // FOLLOWED UP BY 10 BYTE (2bytes=index1, 8bytes=constant), res = new long
    
    public static final byte []OP_SHIFT_LEFT_CONST_BYTE                    = new byte [] {-80};  // FOLLOWED UP BY 3 BYTE (2bytes=index1, 1bytes=constant), res = new byte
    public static final byte []OP_SHIFT_LEFT_CONST_SHORT                   = new byte [] {-79};  // FOLLOWED UP BY 4 BYTE (2bytes=index1, 2bytes=constant), res = new short
    public static final byte []OP_SHIFT_LEFT_CONST_INT                     = new byte [] {-78};  // FOLLOWED UP BY 6 BYTE (2bytes=index1, 4bytes=constant), res = new int
    public static final byte []OP_SHIFT_LEFT_CONST_LONG                    = new byte [] {-77};   // FOLLOWED UP BY 10 BYTE (2bytes=index1, 8bytes=constant), res = new long
    
    public static final byte []OP_SHIFT_RIGHT_CONST_BYTE                   = new byte [] {-76};   // FOLLOWED UP BY 6 BYTE (2bytes=index1, 4bytes=constant), res = new int
    public static final byte []OP_SHIFT_RIGHT_CONST_SHORT                  = new byte [] {-75};   // FOLLOWED UP BY 10 BYTE (2bytes=index1, 8bytes=constant), res = new long    
    public static final byte []OP_SHIFT_RIGHT_CONST_INT                    = new byte [] {-74};   // FOLLOWED UP BY 6 BYTE (2bytes=index1, 4bytes=constant), res = new int
    public static final byte []OP_SHIFT_RIGHT_CONST_LONG                   = new byte [] {-73};   // FOLLOWED UP BY 10 BYTE (2bytes=index1, 8bytes=constant), res = new long
    
    public static final byte []OP_XOR_CONST_BYTE                           = new byte [] {-72};
    public static final byte []OP_XOR_CONST_SHORT                          = new byte [] {-71};
    public static final byte []OP_XOR_CONST_INT                            = new byte [] {-70};
    public static final byte []OP_XOR_CONST_LONG                           = new byte [] {-69};
    
    public static final byte []OP_EQUALS_CONST_BYTE                        = new byte [] {-68};
    public static final byte []OP_EQUALS_CONST_SHORT                       = new byte [] {-67};
    public static final byte []OP_EQUALS_CONST_INT                         = new byte [] {-66};
    public static final byte []OP_EQUALS_CONST_LONG                        = new byte [] {-65};
    
    public static final byte []OP_NOT_EQUAL_CONST_BYTE                     = new byte [] {-64};
    public static final byte []OP_NOT_EQUAL_CONST_SHORT                    = new byte [] {-63};
    public static final byte []OP_NOT_EQUAL_CONST_INT                      = new byte [] {-62};
    public static final byte []OP_NOT_EQUAL_CONST_LONG                     = new byte [] {-61};
    
    public static final byte []OP_LESS_THAN_CONST_BYTE                     = new byte [] {-60};
    public static final byte []OP_LESS_THAN_CONST_SHORT                    = new byte [] {-59};
    public static final byte []OP_LESS_THAN_CONST_INT                      = new byte [] {-58};
    public static final byte []OP_LESS_THAN_CONST_LONG                     = new byte [] {-57};
    
    public static final byte []OP_GREATER_THAN_CONST_BYTE                  = new byte [] {-56};
    public static final byte []OP_GREATER_THAN_CONST_SHORT                 = new byte [] {-55};
    public static final byte []OP_GREATER_THAN_CONST_INT                   = new byte [] {-54};
    public static final byte []OP_GREATER_THAN_CONST_LONG                  = new byte [] {-53};
    
    public static final byte []OP_ADDITION_CONST_BYTE                      = new byte [] {-52};
    public static final byte []OP_ADDITION_CONST_SHORT                     = new byte [] {-51};
    public static final byte []OP_ADDITION_CONST_INT                       = new byte [] {-50};
    public static final byte []OP_ADDITION_CONST_LONG                      = new byte [] {-49};
    
    public static final byte []OP_SUBTRACT_CONST_BYTE                      = new byte [] {-48};
    public static final byte []OP_SUBTRACT_CONST_SHORT                     = new byte [] {-47};
    public static final byte []OP_SUBTRACT_CONST_INT                       = new byte [] {-46};
    public static final byte []OP_SUBTRACT_CONST_LONG                      = new byte [] {-45};
    
    public static final byte []OP_MULTIPLY_CONST_BYTE                      = new byte [] {-44};
    public static final byte []OP_MULTIPLY_CONST_SHORT                     = new byte [] {-43};
    public static final byte []OP_MULTIPLY_CONST_INT                       = new byte [] {-42};
    public static final byte []OP_MULTIPLY_CONST_LONG                      = new byte [] {-41};
    
    public static final byte []OP_DIVIDE_CONST_BYTE                        = new byte [] {-40};
    public static final byte []OP_DIVIDE_CONST_SHORT                       = new byte [] {-39};
    public static final byte []OP_DIVIDE_CONST_INT                         = new byte [] {-38};
    public static final byte []OP_DIVIDE_CONST_LONG                        = new byte [] {-37};
    
    public static final byte []OP_MODULUS_CONST_BYTE                       = new byte [] {-36};
    public static final byte []OP_MODULUS_CONST_SHORT                      = new byte [] {-35};
    public static final byte []OP_MODULUS_CONST_INT                        = new byte [] {-34};
    public static final byte []OP_MODULUS_CONST_LONG                       = new byte [] {-32};
    
    public static final byte []OP_OBJECT_EQUALS_METHOD                     = new byte [] {-31};   // FOLLOWED UP BY 4 BYTE (2bytes=objectindex1, 2bytes=objectindex1), res = new byte [] {condition flag changed
    
    public static final byte []OP_2_CONST_OPERAND                          = new byte [] {-30};   // FOLLOWED UP BY 4 BYTE, res = new byte [] {2 new operand (followup)
    public static final byte []OP_3_CONST_OPERAND                          = new byte [] {-29};   // FOLLOWED UP BY 6 BYTE, res = new byte [] {3 new operand (followup)
    public static final byte []OP_4_CONST_OPERAND                          = new byte [] {-28};   // FOLLOWED UP BY 8 BYTE, res = new byte [] {4 new operand (followup)
    
    public static final byte []SUB_OP_ONE_GAME_WHITE_OUT                   = new byte [] {-27, 4};
    
    public static final byte []SUB_OP_ONE_GAME_RAND                        = new byte [] {-27, 5};
    public static final byte []SUB_OP_ONE_GAME_RAND_MAX                    = new byte [] {-27, 6};
    public static final byte []SUB_OP_ONE_GAME_RAND_MIN_MAX                = new byte [] {-27, 7};
    public static final byte []SUB_OP_ONE_GAME_RAND_SEED                   = new byte [] {-27, 8};
    
    public static final byte []SUB_OP_ONE_GAME_SET_TIMER                   = new byte [] {-27, 9};
    public static final byte []SUB_OP_ONE_GAME_RELEASE_TIMER               = new byte [] {-27, 10};
    
    public static final byte []SUB_OP_ONE_GAME_SET_GRAVITY                 = new byte [] {-27, 12};
    public static final byte []SUB_OP_ONE_GAME_SET_CAMERA_Y                = new byte [] {-27, 13};
    public static final byte []SUB_OP_ONE_GAME_SET_SHAKE                   = new byte [] {-27, 14};
    public static final byte []SUB_OP_ONE_GAME_SET_SLOWDOWN_RATE           = new byte [] {-27, 15};
    public static final byte []SUB_OP_ONE_GAME_SET_STATE                   = new byte [] {-27, 16};
    public static final byte []SUB_OP_ONE_GAME_SET_BACKGROUND_COLOR        = new byte [] {-27, 17};
    public static final byte []SUB_OP_ONE_GAME_SET_INC_POWER_TIME          = new byte [] {-27, 18};
    
    public static final byte []SUB_OP_ONE_GAME_GET_GRAVITY                 = new byte [] {-27, 19};
    public static final byte []SUB_OP_ONE_GAME_GET_CAMERA_Y                = new byte [] {-27, 20};
    public static final byte []SUB_OP_ONE_GAME_GET_SHAKE                   = new byte [] {-27, 21};
    public static final byte []SUB_OP_ONE_GAME_GET_SLOWDOWN_RATE           = new byte [] {-27, 22};
    public static final byte []SUB_OP_ONE_GAME_GET_STATE                   = new byte [] {-27, 23};
    public static final byte []SUB_OP_ONE_GAME_GET_BACKGROUND_COLOR        = new byte [] {-27, 24};
    public static final byte []SUB_OP_ONE_GAME_GET_INC_POWER_TIME          = new byte [] {-27, 25};
    
    public static final byte []SUB_OP_ONE_GAME_ON_BLOCK_STEP               = new byte [] {-27, 26};         // when stepping on a certain block type
    
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_XY_POS           = new byte [] {-27, 32};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_X_POS            = new byte [] {-27, 33};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_Y_POS            = new byte [] {-27, 34};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_X_RATE           = new byte [] {-27, 35};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_Y_RATE           = new byte [] {-27, 36};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_GRAV_AFFECTED    = new byte [] {-27, 37};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_LEFT_BOUND       = new byte [] {-27, 38};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_BOUND      = new byte [] {-27, 39};
    
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_XY_POS           = new byte [] {-27, 40};
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_X_POS            = new byte [] {-27, 41};
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_Y_POS            = new byte [] {-27, 42};
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_X_RATE           = new byte [] {-27, 43};
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_Y_RATE           = new byte [] {-27, 44};
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_GRAV_AFFECTED    = new byte [] {-27, 45};
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_LEFT_BOUND       = new byte [] {-27, 46};
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_RIGHT_BOUND      = new byte [] {-27, 47};
    
    public static final byte []SUB_OP_ONE_GAME_WHITE_OUT_STATIC            = new byte [] {-27, 51};
    
    public static final byte []SUB_OP_ONE_GAME_RAND_MAX_STATIC             = new byte [] {-27, 52};
    public static final byte []SUB_OP_ONE_GAME_RAND_MIN_MAX_STATIC         = new byte [] {-27, 53};
    public static final byte []SUB_OP_ONE_GAME_RAND_SEED_STATIC            = new byte [] {-27, 54};
    
    public static final byte []SUB_OP_ONE_GAME_SET_TIMER_STATIC            = new byte [] {-27, 55};
    public static final byte []SUB_OP_ONE_GAME_RELEASE_TIMER_STATIC        = new byte [] {-27, 56};
    
    public static final byte []SUB_OP_ONE_GAME_SET_GRAVITY_STATIC          = new byte [] {-27, 57};
    public static final byte []SUB_OP_ONE_GAME_SET_CAMERA_Y_STATIC         = new byte [] {-27, 58};
    public static final byte []SUB_OP_ONE_GAME_SET_SHAKE_STATIC            = new byte [] {-27, 59};
    public static final byte []SUB_OP_ONE_GAME_SET_SLOWDOWN_RATE_STATIC    = new byte [] {-27, 60};
    public static final byte []SUB_OP_ONE_GAME_SET_STATE_STATIC            = new byte [] {-27, 61};
    public static final byte []SUB_OP_ONE_GAME_SET_BACKGROUND_COLOR_STATIC = new byte [] {-27, 62};
    public static final byte []SUB_OP_ONE_GAME_SET_INC_POWER_TIME_STATIC   = new byte [] {-27, 63};
    
    public static final byte []SUB_OP_ONE_GAME_ON_BLOCK_STEP_STATIC        = new byte [] {-27, 64};         // when stepping on a certain block type
    
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_XY_POS_STATIC    = new byte [] {-27, 68};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_X_POS_STATIC     = new byte [] {-27, 69};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_Y_POS_STATIC     = new byte [] {-27, 70};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_X_RATE_STATIC    = new byte [] {-27, 71};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_Y_RATE_STATIC    = new byte [] {-27, 72};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_GRAV_AFFECTED_STATIC = new byte [] {-27, 73};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_LEFT_BOUND_STATIC = new byte [] {-27, 74};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_BOUND_STATIC = new byte [] {-27, 75};
    
    public static final byte []SUB_OP_ONE_GAME_GET_TOTAL_HEIGHT            = new byte [] {-27, 76};
    public static final byte []SUB_OP_ONE_GAME_GET_WORKING_HEIGHT          = new byte [] {-27, 77};
    public static final byte []SUB_OP_ONE_GAME_GET_WORKING_TOP_Y           = new byte [] {-27, 78};
    
    public static final byte []SUB_OP_ONE_GAME_ON_LEFT_HIT                 = new byte [] {-27, 79};
    public static final byte []SUB_OP_ONE_GAME_ON_RIGHT_HIT                = new byte [] {-27, 80};
    public static final byte []SUB_OP_ONE_GAME_ON_LEFT_HIT_STATIC          = new byte [] {-27, 81};
    public static final byte []SUB_OP_ONE_GAME_ON_RIGHT_HIT_STATIC         = new byte [] {-27, 82};
    
    public static final byte []SUB_OP_ONE_GAME_GET_LEVEL_DATA              = new byte [] {-27, 83};
    public static final byte []SUB_OP_ONE_GAME_GET_GAME_FLAGS              = new byte [] {-27, 84};
    public static final byte []SUB_OP_ONE_GAME_SET_GAME_FLAGS              = new byte [] {-27, 85};
    public static final byte []SUB_OP_ONE_GAME_SET_GAME_FLAGS_STATIC       = new byte [] {-27, 86};
    
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_STAND = new byte [] {-27, 87};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_LEFT_STAND       = new byte [] {-27, 88};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_STAND      = new byte [] {-27, 89};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_STAND_STATIC = new byte [] {-27, 90};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_LEFT_STAND_STATIC       = new byte [] {-27, 91};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_STAND_STATIC      = new byte [] {-27, 92};
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_LEFT_RIGHT_STAND = new byte [] {-27, 93};
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_LEFT_STAND       = new byte [] {-27, 94};
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_RIGHT_STAND      = new byte [] {-27, 95};
    
    public static final byte []SUB_OP_ONE_GAME_ON_DIE                      = new byte [] {-27, 96};
    public static final byte []SUB_OP_ONE_GAME_ON_DIE_STATIC               = new byte [] {-27, 97};
    
    public static final byte []SUB_OP_ONE_GAME_GET_GAME_FLAGS_2            = new byte [] {-27, 98};
    public static final byte []SUB_OP_ONE_GAME_SET_GAME_FLAGS_2            = new byte [] {-27, 99};
    public static final byte []SUB_OP_ONE_GAME_SET_GAME_FLAGS_2_STATIC     = new byte [] {-27, 100};
    
    public static final byte []SUB_OP_ONE_GAME_GET_CURRENT_FLOOR_NUMBER    = new byte [] {-27, 101};
    
    public static final byte []SUB_OP_ONE_GAME_IS_TIMER_ACTIVE             = new byte [] {-27, 102};
    public static final byte []SUB_OP_ONE_GAME_IS_TIMER_ACTIVE_STATIC      = new byte [] {-27, 103};
    
    public static final byte []SUB_OP_ONE_GAME_GET_BLOCK_REF_PIXEL         = new byte [] {-27, 104};
    public static final byte []SUB_OP_ONE_GAME_SET_BLOCK_REF_PIXEL         = new byte [] {-27, 105};
    public static final byte []SUB_OP_ONE_GAME_SET_BLOCK_REF_PIXEL_STATIC  = new byte [] {-27, 106};
    
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_REF_PIXEL        = new byte [] {-27, 107};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_REF_PIXEL        = new byte [] {-27, 108};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_REF_PIXEL_STATIC = new byte [] {-27, 109};
    
    public static final byte []SUB_OP_ONE_GAME_SET_BLOCK_FRAME_SEQ         = new byte [] {-27, 110};
    public static final byte []SUB_OP_ONE_GAME_SET_BLOCK_TIME_SEQ          = new byte [] {-27, 111};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_FRAME_SEQ        = new byte [] {-27, 112};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_ONLY  = new byte [] {-27, 113};
    
    public static final byte []SUB_OP_ONE_GAME_GET_POWER                   = new byte [] {-27, 114};
    public static final byte []SUB_OP_ONE_GAME_SET_POWER                   = new byte [] {-27, 115};
    public static final byte []SUB_OP_ONE_GAME_SET_POWER_STATIC            = new byte [] {-27, 116};
    
    public static final byte []SUB_OP_ONE_GAME_ON_BLOCK_DEPART             = new byte [] {-27, 117};
    public static final byte []SUB_OP_ONE_GAME_ON_BLOCK_DEPART_STATIC      = new byte [] {-27, 118};
    
    public static final byte []SUB_OP_ONE_GAME_GET_RET                     = new byte [] {-27, 119};
    public static final byte []SUB_OP_ONE_GAME_SET_RET                     = new byte [] {-27, 120};
    public static final byte []SUB_OP_ONE_GAME_SET_RET_STATIC              = new byte [] {-27, 121};
    
    public static final byte []SUB_OP_ONE_GAME_LOAD_FROM_JAR               = new byte [] {-27, 122};
    
    public static final byte []SUB_OP_ONE_GAME_NEW_MIDI                    = new byte [] {-27, 123};
    public static final byte []SUB_OP_ONE_GAME_START_MIDI                  = new byte [] {-27, 124};
    public static final byte []SUB_OP_ONE_GAME_STOP_MIDI                   = new byte [] {-27, 125};
    public static final byte []SUB_OP_ONE_GAME_CLOSE_MIDI                  = new byte [] {-27, 126};
    
    public static final byte []SUB_OP_ONE_GAME_FLASH_BACKLIGHT             = new byte [] {-27, 127};
    public static final byte []SUB_OP_ONE_GAME_VIBRATE                     = new byte [] {-27, -128};
    
    public static final byte []SUB_OP_ONE_GAME_FLASH_BACKLIGHT_STATIC      = new byte [] {-27, -127};
    public static final byte []SUB_OP_ONE_GAME_VIBRATE_STATIC              = new byte [] {-27, -126};
    
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_WIDTH              = new byte [] {-27, -125};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_WIDTH_PIXEL        = new byte [] {-27, -124};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_WIDTH_PIXEL_STATIC = new byte [] {-27, -123};
    
    public static final byte []SUB_OP_ONE_GAME_GET_PLAYER_HEIGHT              = new byte [] {-27, -122};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_HEIGHT_PIXEL        = new byte [] {-27, -121};
    public static final byte []SUB_OP_ONE_GAME_SET_PLAYER_HEIGHT_PIXEL_STATIC = new byte [] {-27, -120};
    
    public static final byte []SUB_OP_ONE_GAME_GET_BLOCK_COLL_OFFSET         = new byte [] {-27, -119};
    public static final byte []SUB_OP_ONE_GAME_SET_BLOCK_COLL_OFFSET         = new byte [] {-27, -118};
    public static final byte []SUB_OP_ONE_GAME_SET_BLOCK_COLL_OFFSET_STATIC  = new byte [] {-27, -117};
    
    public static final byte []SUB_OP_ONE_GAME_SET_LEVEL_DATA              = new byte [] {-27, -116};
    
    public static final byte []SUB_OP_ONE_GAME_SCENE_RESIZE_LAYER_ITEMS                   = new byte [] {-27, -115};
    public static final byte []SUB_OP_ONE_GAME_SCENE_RESIZE_LAYER                         = new byte [] {-27, -114};
    public static final byte []SUB_OP_ONE_GAME_SCENE_RESIZE_DB                            = new byte [] {-27, -113};
    
    public static final byte []SUB_OP_ONE_GAME_SCENE_LOAD_NEW_INTERPOLATION_POINTS        = new byte [] {-27, -112};
    public static final byte []SUB_OP_ONE_GAME_SCENE_LOAD_NEW_IMAGE                       = new byte [] {-27, -111};
    public static final byte []SUB_OP_ONE_GAME_SCENE_LOAD_NEW_SPRITE                      = new byte [] {-27, -110};
    public static final byte []SUB_OP_ONE_GAME_SCENE_LOAD_NEW_SEQUENCE                    = new byte [] {-27, -109};
    
    public static final byte []SUB_OP_ONE_GAME_SCENE_GET_IMAGE_COUNT                      = new byte [] {-27, -108};
    public static final byte []SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATOR_COUNT               = new byte [] {-27, -107};
    public static final byte []SUB_OP_ONE_GAME_SCENE_GET_SEQUENCE_COUNT                   = new byte [] {-27, -106};
    public static final byte []SUB_OP_ONE_GAME_SCENE_GET_SPRITE_COUNT                     = new byte [] {-27, -105};
    public static final byte []SUB_OP_ONE_GAME_SCENE_GET_SPRITE_BLOCK_INDEX               = new byte [] {-27, -104};
    public static final byte []SUB_OP_ONE_GAME_SCENE_GET_SPRITE_BLOCK_INDEX_STATIC        = new byte [] {-27, -103};
    
    public static final byte []SUB_OP_ONE_GAME_SCENE_GET_ITEM_LENGTH                      = new byte [] {-27, -102};
    public static final byte []SUB_OP_ONE_GAME_SCENE_GET_LAYER_LENGTH                     = new byte [] {-27, -101};
    public static final byte []SUB_OP_ONE_GAME_SCENE_GET_LAYER_PARAM                      = new byte [] {-27, -100};
    public static final byte []SUB_OP_ONE_GAME_SCENE_IS_INTERPOLATOR_DONE                 = new byte [] {-27, -99};
    public static final byte []SUB_OP_ONE_GAME_SCENE_IS_INTERPOLATOR_DONE_STATIC          = new byte [] {-27, -98};
    public static final byte []SUB_OP_ONE_GAME_SCENE_IS_LAYER_VISIBLE                     = new byte [] {-27, -97};
    public static final byte []SUB_OP_ONE_GAME_SCENE_IS_LAYER_VISIBLE_STATIC              = new byte [] {-27, -96};
    public static final byte []SUB_OP_ONE_GAME_SCENE_IS_SPRITE_LINK_VISIBLE               = new byte [] {-27, -95};
    public static final byte []SUB_OP_ONE_GAME_SCENE_IS_SPRITE_LINK_VISIBLE_STATIC        = new byte [] {-27, -94};
    
    public static final byte []SUB_OP_ONE_GAME_SCENE_SET_LAYER_PARAM                      = new byte [] {-27, -93};
    public static final byte []SUB_OP_ONE_GAME_SCENE_SET_LAYER_VISIBILITY                 = new byte [] {-27, -92};
    public static final byte []SUB_OP_ONE_GAME_SCENE_SET_LAYER_VISIBILITY_STATIC          = new byte [] {-27, -91};
    
    public static final byte []SUB_OP_ONE_GAME_SCENE_SWITCH_LAYERS                        = new byte [] {-27, -90};
    public static final byte []SUB_OP_ONE_GAME_SCENE_SWITCH_LAYERS_STATIC                 = new byte [] {-27, -89};
    public static final byte []SUB_OP_ONE_GAME_SCENE_FLIP_SETS                            = new byte [] {-27, -88};
    
    public static final byte []SUB_OP_ONE_GAME_SCENE_SWITCH_SPRITE_SEQUENCE               = new byte [] {-27, -87};
    public static final byte []SUB_OP_ONE_GAME_SCENE_SWITCH_SPRITE_SEQUENCE_STATIC        = new byte [] {-27, -86};
    
    public static final byte []SUB_OP_ONE_GAME_SCENE_RESET_INTERPOLATION_POINT            = new byte [] {-27, -85};
    public static final byte []SUB_OP_ONE_GAME_SCENE_RESET_INTERPOLATION_POINT_STATIC     = new byte [] {-27, -84};
    
    public static final byte []SUB_OP_ONE_GAME_SCENE_GET_ITEM                             = new byte [] {-27, -83};
    public static final byte []SUB_OP_ONE_GAME_SCENE_GET_ITEM_STATIC                      = new byte [] {-27, -82};
    
    public static final byte []SUB_OP_ONE_GAME_SCENE_SET_ITEM                             = new byte [] {-27, -81};
    public static final byte []SUB_OP_ONE_GAME_SCENE_SET_ITEM_STATIC                      = new byte [] {-27, -80};
    
    public static final byte []SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATION_POINT              = new byte [] {-27, -79};
    public static final byte []SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATION_POINT_STATIC       = new byte [] {-27, -78};
    
    public static final byte []SUB_OP_ONE_ARRAY_COPY                                      = new byte [] {-27, -77};
    
    public static final byte []SUB_OP_ONE_ARRAY_BYTE_TO_SHORT                             = new byte [] {-27, -76};
    public static final byte []SUB_OP_ONE_ARRAY_BYTE_TO_INT                               = new byte [] {-27, -75};
    public static final byte []SUB_OP_ONE_ARRAY_BYTE_TO_LONG                              = new byte [] {-27, -74};
    
    public static final byte []SUB_OP_ONE_ARRAY_SHORT_TO_BYTE                             = new byte [] {-27, -73};
    public static final byte []SUB_OP_ONE_ARRAY_INT_TO_BYTE                               = new byte [] {-27, -72};
    public static final byte []SUB_OP_ONE_ARRAY_LONG_TO_BYTE                              = new byte [] {-27, -71};
    
    public static final byte []SUB_OP_ONE_HT_NEW                                          = new byte [] {-27, -70};
    public static final byte []SUB_OP_ONE_HT_NEW_INITIALCAP                               = new byte [] {-27, -69};
    public static final byte []SUB_OP_ONE_HT_CLEAR                                        = new byte [] {-27, -68};
    public static final byte []SUB_OP_ONE_HT_CONTAINS                                     = new byte [] {-27, -67};
    public static final byte []SUB_OP_ONE_HT_CONTAINSKEY                                  = new byte [] {-27, -66};
    public static final byte []SUB_OP_ONE_HT_ELEMENTS                                     = new byte [] {-27, -65};
    public static final byte []SUB_OP_ONE_HT_GET                                          = new byte [] {-27, -64};
    public static final byte []SUB_OP_ONE_HT_ISEMPTY                                      = new byte [] {-27, -63};
    public static final byte []SUB_OP_ONE_HT_KEYS                                         = new byte [] {-27, -62};
    public static final byte []SUB_OP_ONE_HT_PUT                                          = new byte [] {-27, -61};
    public static final byte []SUB_OP_ONE_HT_REMOVE                                       = new byte [] {-27, -60};
    public static final byte []SUB_OP_ONE_HT_SIZE                                         = new byte [] {-27, -59};
    
    
    
    
    
    
    public static final cydASMOpcodeMnemonic MNEMONIC_CONST_BYTE                          = new cydASMOpcodeMnemonic(OP_CONST_BYTE,                         "OP_CONST_BYTE",                        "constb",       new int[] {cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONST_SHORT                         = new cydASMOpcodeMnemonic(OP_CONST_SHORT,                        "OP_CONST_SHORT",                       "consts",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONST_INT                           = new cydASMOpcodeMnemonic(OP_CONST_INT,                          "OP_CONST_INT",                         "consti",       new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONST_LONG                          = new cydASMOpcodeMnemonic(OP_CONST_LONG,                         "OP_CONST_LONG",                        "constl",       new int[] {cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONST_OPERAND                       = new cydASMOpcodeMnemonic(OP_CONST_OPERAND,                      "OP_CONST_OPERAND",                     "consto",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONST_STRING                        = new cydASMOpcodeMnemonic(OP_CONST_STRING,                       "OP_CONST_STRING",                      "conststr",     new int[] {cydASMOpcodeMnemonic.INTERNAL_STRING}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_NEW_BYTE_ARRAY                      = new cydASMOpcodeMnemonic(OP_NEW_BYTE_ARRAY,                     "OP_NEW_BYTE_ARRAY",                    "nab",          new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_NEW_SHORT_ARRAY                     = new cydASMOpcodeMnemonic(OP_NEW_SHORT_ARRAY,                    "OP_NEW_SHORT_ARRAY",                   "nas",          new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_NEW_INT_ARRAY                       = new cydASMOpcodeMnemonic(OP_NEW_INT_ARRAY,                      "OP_NEW_INT_ARRAY",                     "nai",          new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_NEW_LONG_ARRAY                      = new cydASMOpcodeMnemonic(OP_NEW_LONG_ARRAY,                     "OP_NEW_LONG_ARRAY",                    "nal",          new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_NEW_OBJECT_ARRAY                    = new cydASMOpcodeMnemonic(OP_NEW_OBJECT_ARRAY,                   "OP_NEW_OBJECT_ARRAY",                  "naobj",        new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_POP_BYTE                            = new cydASMOpcodeMnemonic(OP_POP_BYTE,                           "OP_POP_BYTE",                          "popb",         new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_POP_SHORT                           = new cydASMOpcodeMnemonic(OP_POP_SHORT,                          "OP_POP_SHORT",                         "pops",         new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_POP_INT                             = new cydASMOpcodeMnemonic(OP_POP_INT,                            "OP_POP_INT",                           "popi",         new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_POP_LONG                            = new cydASMOpcodeMnemonic(OP_POP_LONG,                           "OP_POP_LONG",                          "popl",         new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_POP_OPERAND                         = new cydASMOpcodeMnemonic(OP_POP_OPERAND,                        "OP_POP_OPERAND",                       "popo",         new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_POP_OBJECT                          = new cydASMOpcodeMnemonic(OP_POP_OBJECT,                         "OP_POP_OBJECT",                        "popobj",       new int[] {}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_BYTE_TO_SHORT                  = new cydASMOpcodeMnemonic(OP_CONV_BYTE_TO_SHORT,                 "OP_CONV_BYTE_TO_SHORT",                "b2s",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_BYTE_TO_INT                    = new cydASMOpcodeMnemonic(OP_CONV_BYTE_TO_INT,                   "OP_CONV_BYTE_TO_INT",                  "b2i",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_BYTE_TO_LONG                   = new cydASMOpcodeMnemonic(OP_CONV_BYTE_TO_LONG,                  "OP_CONV_BYTE_TO_LONG",                 "b2l",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_BYTE_TO_OPERAND                = new cydASMOpcodeMnemonic(OP_CONV_BYTE_TO_OPERAND,               "OP_CONV_BYTE_TO_OPERAND",              "b2o",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_SHORT_TO_BYTE                  = new cydASMOpcodeMnemonic(OP_CONV_SHORT_TO_BYTE,                 "OP_CONV_SHORT_TO_BYTE",                "s2b",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_SHORT_TO_INT                   = new cydASMOpcodeMnemonic(OP_CONV_SHORT_TO_INT,                  "OP_CONV_SHORT_TO_INT",                 "s2i",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_SHORT_TO_LONG                  = new cydASMOpcodeMnemonic(OP_CONV_SHORT_TO_LONG,                 "OP_CONV_SHORT_TO_LONG",                "s2l",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_SHORT_TO_OPERAND               = new cydASMOpcodeMnemonic(OP_CONV_SHORT_TO_OPERAND,              "OP_CONV_SHORT_TO_OPERAND",             "s2o",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_INT_TO_BYTE                    = new cydASMOpcodeMnemonic(OP_CONV_INT_TO_BYTE,                   "OP_CONV_INT_TO_BYTE",                  "i2b",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_INT_TO_SHORT                   = new cydASMOpcodeMnemonic(OP_CONV_INT_TO_SHORT,                  "OP_CONV_INT_TO_SHORT",                 "i2s",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_INT_TO_LONG                    = new cydASMOpcodeMnemonic(OP_CONV_INT_TO_LONG,                   "OP_CONV_INT_TO_LONG",                  "i2l",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_INT_TO_OPERAND                 = new cydASMOpcodeMnemonic(OP_CONV_INT_TO_OPERAND,                "OP_CONV_INT_TO_OPERAND",               "i2o",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_LONG_TO_BYTE                   = new cydASMOpcodeMnemonic(OP_CONV_LONG_TO_BYTE,                  "OP_CONV_LONG_TO_BYTE",                 "l2b",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_LONG_TO_SHORT                  = new cydASMOpcodeMnemonic(OP_CONV_LONG_TO_SHORT,                 "OP_CONV_LONG_TO_SHORT",                "l2s",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_LONG_TO_INT                    = new cydASMOpcodeMnemonic(OP_CONV_LONG_TO_INT,                   "OP_CONV_LONG_TO_INT",                  "l2i",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_LONG_TO_OPERAND                = new cydASMOpcodeMnemonic(OP_CONV_LONG_TO_OPERAND,               "OP_CONV_LONG_TO_OPERAND",              "l2o",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_OPERAND_TO_BYTE                = new cydASMOpcodeMnemonic(OP_CONV_OPERAND_TO_BYTE,               "OP_CONV_OPERAND_TO_BYTE",              "o2b",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_OPERAND_TO_SHORT               = new cydASMOpcodeMnemonic(OP_CONV_OPERAND_TO_SHORT,              "OP_CONV_OPERAND_TO_SHORT",             "o2s",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_OPERAND_TO_INT                 = new cydASMOpcodeMnemonic(OP_CONV_OPERAND_TO_INT,                "OP_CONV_OPERAND_TO_INT",               "o2i",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONV_OPERAND_TO_LONG                = new cydASMOpcodeMnemonic(OP_CONV_OPERAND_TO_LONG,               "OP_CONV_OPERAND_TO_LONG",              "o2l",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_SUB_ASSIGN_BYTE                     = new cydASMOpcodeMnemonic(OP_SUB_ASSIGN_BYTE,                    "OP_SUB_ASSIGN_BYTE",                   "subassignb",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUB_ASSIGN_SHORT                    = new cydASMOpcodeMnemonic(OP_SUB_ASSIGN_SHORT,                   "OP_SUB_ASSIGN_SHORT",                  "subassigns",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUB_ASSIGN_INT                      = new cydASMOpcodeMnemonic(OP_SUB_ASSIGN_INT,                     "OP_SUB_ASSIGN_INT",                    "subassigni",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUB_ASSIGN_LONG                     = new cydASMOpcodeMnemonic(OP_SUB_ASSIGN_LONG,                    "OP_SUB_ASSIGN_LONG",                   "subassignl",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUB_ASSIGN_OBJECT                   = new cydASMOpcodeMnemonic(OP_SUB_ASSIGN_OBJECT,                  "OP_SUB_ASSIGN_OBJECT",                 "subassignobj", new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_SUB_EXTRACT_BYTE                    = new cydASMOpcodeMnemonic(OP_SUB_EXTRACT_BYTE,                   "OP_SUB_EXTRACT_BYTE",                  "extractb",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUB_EXTRACT_SHORT                   = new cydASMOpcodeMnemonic(OP_SUB_EXTRACT_SHORT,                  "OP_SUB_EXTRACT_SHORT",                 "extracts",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUB_EXTRACT_INT                     = new cydASMOpcodeMnemonic(OP_SUB_EXTRACT_INT,                    "OP_SUB_EXTRACT_INT",                   "extracti",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUB_EXTRACT_LONG                    = new cydASMOpcodeMnemonic(OP_SUB_EXTRACT_LONG,                   "OP_SUB_EXTRACT_LONG",                  "extractl",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUB_EXTRACT_OBJECT                  = new cydASMOpcodeMnemonic(OP_SUB_EXTRACT_OBJECT,                 "OP_SUB_EXTRACT_OBJECT",                "extractobj",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_SIZE_OF                             = new cydASMOpcodeMnemonic(OP_SIZE_OF,                            "OP_SIZE_OF",                           "sizeof",       new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_OBJECT});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_OBJECT_PUSH_NULL                    = new cydASMOpcodeMnemonic(OP_OBJECT_PUSH_NULL,                   "OP_OBJECT_PUSH_NULL",                  "pushnull",     new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_OBJECT_EQUALS_NULL                  = new cydASMOpcodeMnemonic(OP_OBJECT_EQUALS_NULL,                 "OP_OBJECT_EQUALS_NULL",                "eqnull",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_CONTROL_CALL                        = new cydASMOpcodeMnemonic(OP_CONTROL_CALL,                       "OP_CONTROL_CALL",                      "call",         new int[] {cydASMOpcodeMnemonic.INTERNAL_BYTE, cydASMOpcodeMnemonic.INTERNAL_BYTE, cydASMOpcodeMnemonic.INTERNAL_BYTE, cydASMOpcodeMnemonic.INTERNAL_BYTE, cydASMOpcodeMnemonic.INTERNAL_BYTE, cydASMOpcodeMnemonic.INTERNAL_BYTE, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONTROL_SEMI_DYNAMIC_CALL           = new cydASMOpcodeMnemonic(OP_CONTROL_SEMI_DYNAMIC_CALL,          "OP_CONTROL_SEMI_DYNAMIC_CALL",         "semidyncall",  new int[] {cydASMOpcodeMnemonic.INTERNAL_BYTE, cydASMOpcodeMnemonic.INTERNAL_BYTE, cydASMOpcodeMnemonic.INTERNAL_BYTE, cydASMOpcodeMnemonic.INTERNAL_BYTE, cydASMOpcodeMnemonic.INTERNAL_BYTE, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONTROL_FULL_DYNAMIC_CALL           = new cydASMOpcodeMnemonic(OP_CONTROL_FULL_DYNAMIC_CALL,          "OP_CONTROL_FULL_DYNAMIC_CALL",         "dyncall",      new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_BYTE, cydASMOpcodeMnemonic.INDEX_BYTE, cydASMOpcodeMnemonic.INDEX_BYTE, cydASMOpcodeMnemonic.INDEX_BYTE, cydASMOpcodeMnemonic.INDEX_BYTE, cydASMOpcodeMnemonic.INDEX_BYTE, cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONTROL_RET                         = new cydASMOpcodeMnemonic(OP_CONTROL_RET,                        "OP_CONTROL_RET",                       "ret",          new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONTROL_JUMP                        = new cydASMOpcodeMnemonic(OP_CONTROL_JUMP,                       "OP_CONTROL_JUMP",                      "jmp",          new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONTROL_DYNAMIC_JUMP                = new cydASMOpcodeMnemonic(OP_CONTROL_DYNAMIC_JUMP,               "OP_CONTROL_DYNAMIC_JUMP",              "dynjmp",       new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONTROL_JUMP_CONDITIONAL            = new cydASMOpcodeMnemonic(OP_CONTROL_JUMP_CONDITIONAL,           "OP_CONTROL_JUMP_CONDITIONAL",          "jmpc",         new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONTROL_DYNAMIC_JUMP_CONDITIONAL    = new cydASMOpcodeMnemonic(OP_CONTROL_DYNAMIC_JUMP_CONDITIONAL,   "OP_CONTROL_DYNAMIC_JUMP_CONDITIONAL",  "dynjmpc",      new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONTROL_SET_TRAP                    = new cydASMOpcodeMnemonic(OP_CONTROL_SET_TRAP,                   "OP_CONTROL_SET_TRAP",                  "sett",         new int[] {cydASMOpcodeMnemonic.INTERNAL_STRING, cydASMOpcodeMnemonic.INDEX_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONTROL_THROW_TRAP                  = new cydASMOpcodeMnemonic(OP_CONTROL_THROW_TRAP,                 "OP_CONTROL_THROW_TRAP",                "throwt",       new int[] {cydASMOpcodeMnemonic.INTERNAL_STRING}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONTROL_RELEASE_TRAP                = new cydASMOpcodeMnemonic(OP_CONTROL_RELEASE_TRAP,               "OP_CONTROL_RELEASE_TRAP",              "releaset",     new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_CONTROL_EXIT                        = new cydASMOpcodeMnemonic(OP_CONTROL_EXIT,                       "OP_CONTROL_EXIT",                      "exit",         new int[] {}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_SYSTEM_ID_STRING                    = new cydASMOpcodeMnemonic(OP_SYSTEM_ID_STRING,                   "OP_SYSTEM_ID_STRING",                  "idstring",     new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SYSTEM_TIME                         = new cydASMOpcodeMnemonic(OP_SYSTEM_TIME,                        "OP_SYSTEM_TIME",                       "timems",       new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SYSTEM_OUT                          = new cydASMOpcodeMnemonic(OP_SYSTEM_OUT,                         "OP_SYSTEM_OUT",                        "out",          new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_OBJECT});
    public static final cydASMOpcodeMnemonic MNEMONIC_SYSTEM_SHARED                       = new cydASMOpcodeMnemonic(OP_SYSTEM_SHARED,                      "OP_SYSTEM_SHARED",                     "shared",       new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SYSTEM_GC                           = new cydASMOpcodeMnemonic(OP_SYSTEM_GC,                          "OP_SYSTEM_GC",                         "gc",           new int[] {}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_COPY_BYTE                           = new cydASMOpcodeMnemonic(OP_COPY_BYTE,                          "OP_COPY_BYTE",                         "copyb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_COPY_SHORT                          = new cydASMOpcodeMnemonic(OP_COPY_SHORT,                         "OP_COPY_SHORT",                        "copys",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_COPY_INT                            = new cydASMOpcodeMnemonic(OP_COPY_INT,                           "OP_COPY_INT",                          "copyi",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_COPY_LONG                           = new cydASMOpcodeMnemonic(OP_COPY_LONG,                          "OP_COPY_LONG",                         "copyl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_COPY_OPERAND                        = new cydASMOpcodeMnemonic(OP_COPY_OPERAND,                       "OP_COPY_OPERAND",                      "copyo",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_COPY_OBJECT                         = new cydASMOpcodeMnemonic(OP_COPY_OBJECT,                        "OP_COPY_OBJECT",                       "copyobj",      new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});

    public static final cydASMOpcodeMnemonic MNEMONIC_GET_COND                            = new cydASMOpcodeMnemonic(OP_GET_COND,                           "OP_GET_COND",                          "getc",         new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SET_COND                            = new cydASMOpcodeMnemonic(OP_SET_COND,                           "OP_SET_COND",                          "setc",         new int[] {cydASMOpcodeMnemonic.INDEX_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_COND                            = new cydASMOpcodeMnemonic(OP_NOT_COND,                           "OP_NOT_COND",                          "notc",         new int[] {}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTI_POP_BYTE                      = new cydASMOpcodeMnemonic(OP_MULTI_POP_BYTE,                     "OP_MULTI_POP_BYTE",                    "mulpopb",      new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTI_POP_SHORT                     = new cydASMOpcodeMnemonic(OP_MULTI_POP_SHORT,                    "OP_MULTI_POP_SHORT",                   "mulpops",      new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTI_POP_INT                       = new cydASMOpcodeMnemonic(OP_MULTI_POP_INT,                      "OP_MULTI_POP_INT",                     "mulpopi",      new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTI_POP_LONG                      = new cydASMOpcodeMnemonic(OP_MULTI_POP_LONG,                     "OP_MULTI_POP_LONG",                    "mulpopl",      new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTI_POP_OPERAND                   = new cydASMOpcodeMnemonic(OP_MULTI_POP_OPERAND,                  "OP_MULTI_POP_OPERAND",                 "mulpopo",      new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTI_POP_OBJECT                    = new cydASMOpcodeMnemonic(OP_MULTI_POP_OBJECT,                   "OP_MULTI_POP_OBJECT",                  "mulpopobj",    new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_RES_LENGTH                          = new cydASMOpcodeMnemonic(OP_RES_LENGTH,                         "OP_RES_LENGTH",                        "reslen",       new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_SHORT});
    public static final cydASMOpcodeMnemonic MNEMONIC_SYSTEM_DIRECT_ACCESS_VM_DATA_FILE   = new cydASMOpcodeMnemonic(OP_SYSTEM_DIRECT_ACCESS_VM_DATA_FILE,  "OP_SYSTEM_DIRECT_ACCESS_VM_DATA_FILE", "davmdat",      new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SYSTEM_DIRECT_LOAD_RES_POSITION     = new cydASMOpcodeMnemonic(OP_SYSTEM_DIRECT_LOAD_RES_POSITION,    "OP_SYSTEM_DIRECT_LOAD_RES_POSITION",   "dlrp",         new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_SHORT});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_SET_RET                             = new cydASMOpcodeMnemonic(OP_SET_RET,                            "OP_SET_RET",                           "setret",       new int[] {cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {cydASMOpcodeMnemonic.INDEX_BYTE});
    public static final cydASMOpcodeMnemonic MNEMONIC_GET_RET                             = new cydASMOpcodeMnemonic(OP_GET_RET,                            "OP_GET_RET",                           "getret",       new int[] {cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_ASSIGN_BYTE                         = new cydASMOpcodeMnemonic(OP_ASSIGN_BYTE,                        "OP_ASSIGN_BYTE",                       "assignb",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ASSIGN_SHORT                        = new cydASMOpcodeMnemonic(OP_ASSIGN_SHORT,                       "OP_ASSIGN_SHORT",                      "assigns",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ASSIGN_INT                          = new cydASMOpcodeMnemonic(OP_ASSIGN_INT,                         "OP_ASSIGN_INT",                        "assigni",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ASSIGN_LONG                         = new cydASMOpcodeMnemonic(OP_ASSIGN_LONG,                        "OP_ASSIGN_LONG",                       "assignl",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ASSIGN_OPERAND                      = new cydASMOpcodeMnemonic(OP_ASSIGN_OPERAND,                     "OP_ASSIGN_OPERAND",                    "assigno",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ASSIGN_OBJECT                       = new cydASMOpcodeMnemonic(OP_ASSIGN_OBJECT,                      "OP_ASSIGN_OBJECT",                     "assignobj",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_EQUALS_BYTE                         = new cydASMOpcodeMnemonic(OP_EQUALS_BYTE,                        "OP_EQUALS_BYTE",                       "eqb",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_EQUALS_SHORT                        = new cydASMOpcodeMnemonic(OP_EQUALS_SHORT,                       "OP_EQUALS_SHORT",                      "eqs",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_EQUALS_INT                          = new cydASMOpcodeMnemonic(OP_EQUALS_INT,                         "OP_EQUALS_INT",                        "eqi",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_EQUALS_LONG                         = new cydASMOpcodeMnemonic(OP_EQUALS_LONG,                        "OP_EQUALS_LONG",                       "eql",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_EQUALS_OBJECT                       = new cydASMOpcodeMnemonic(OP_EQUALS_OBJECT,                      "OP_EQUALS_OBJECT",                     "eqobj",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_EQUAL_BYTE                      = new cydASMOpcodeMnemonic(OP_NOT_EQUAL_BYTE,                     "OP_NOT_EQUAL_BYTE",                    "neqb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_EQUAL_SHORT                     = new cydASMOpcodeMnemonic(OP_NOT_EQUAL_SHORT,                    "OP_NOT_EQUAL_SHORT",                   "neqs",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_EQUAL_INT                       = new cydASMOpcodeMnemonic(OP_NOT_EQUAL_INT,                      "OP_NOT_EQUAL_INT",                     "neqi",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_EQUAL_LONG                      = new cydASMOpcodeMnemonic(OP_NOT_EQUAL_LONG,                     "OP_NOT_EQUAL_LONG",                    "neql",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_EQUAL_OBJECT                    = new cydASMOpcodeMnemonic(OP_NOT_EQUAL_OBJECT,                   "OP_NOT_EQUAL_OBJECT",                  "neqobj",      new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_LESS_THAN_BYTE                      = new cydASMOpcodeMnemonic(OP_LESS_THAN_BYTE,                     "OP_LESS_THAN_BYTE",                    "lstb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_LESS_THAN_SHORT                     = new cydASMOpcodeMnemonic(OP_LESS_THAN_SHORT,                    "OP_LESS_THAN_SHORT",                   "lsts",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_LESS_THAN_INT                       = new cydASMOpcodeMnemonic(OP_LESS_THAN_INT,                      "OP_LESS_THAN_INT",                     "lsti",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_LESS_THAN_LONG                      = new cydASMOpcodeMnemonic(OP_LESS_THAN_LONG,                     "OP_LESS_THAN_LONG",                    "lstl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GREATER_THAN_BYTE                   = new cydASMOpcodeMnemonic(OP_GREATER_THAN_BYTE,                  "OP_GREATER_THAN_BYTE",                 "grtb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GREATER_THAN_SHORT                  = new cydASMOpcodeMnemonic(OP_GREATER_THAN_SHORT,                 "OP_GREATER_THAN_SHORT",                "grts",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GREATER_THAN_INT                    = new cydASMOpcodeMnemonic(OP_GREATER_THAN_INT,                   "OP_GREATER_THAN_INT",                  "grti",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GREATER_THAN_LONG                   = new cydASMOpcodeMnemonic(OP_GREATER_THAN_LONG,                  "OP_GREATER_THAN_LONG",                 "grtl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_ADDITION_BYTE                       = new cydASMOpcodeMnemonic(OP_ADDITION_BYTE,                      "OP_ADDITION_BYTE",                     "addb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ADDITION_SHORT                      = new cydASMOpcodeMnemonic(OP_ADDITION_SHORT,                     "OP_ADDITION_SHORT",                    "adds",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ADDITION_INT                        = new cydASMOpcodeMnemonic(OP_ADDITION_INT,                       "OP_ADDITION_INT",                      "addi",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ADDITION_LONG                       = new cydASMOpcodeMnemonic(OP_ADDITION_LONG,                      "OP_ADDITION_LONG",                     "addl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_SUBTRACT_BYTE                       = new cydASMOpcodeMnemonic(OP_SUBTRACT_BYTE,                      "OP_SUBTRACT_BYTE",                     "subb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUBTRACT_SHORT                      = new cydASMOpcodeMnemonic(OP_SUBTRACT_SHORT,                     "OP_SUBTRACT_SHORT",                    "subs",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUBTRACT_INT                        = new cydASMOpcodeMnemonic(OP_SUBTRACT_INT,                       "OP_SUBTRACT_INT",                      "subi",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUBTRACT_LONG                       = new cydASMOpcodeMnemonic(OP_SUBTRACT_LONG,                      "OP_SUBTRACT_LONG",                     "subl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTIPLY_BYTE                       = new cydASMOpcodeMnemonic(OP_MULTIPLY_BYTE,                      "OP_MULTIPLY_BYTE",                     "mulb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTIPLY_SHORT                      = new cydASMOpcodeMnemonic(OP_MULTIPLY_SHORT,                     "OP_MULTIPLY_SHORT",                    "muls",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTIPLY_INT                        = new cydASMOpcodeMnemonic(OP_MULTIPLY_INT,                       "OP_MULTIPLY_INT",                      "muli",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTIPLY_LONG                       = new cydASMOpcodeMnemonic(OP_MULTIPLY_LONG,                      "OP_MULTIPLY_LONG",                     "mull",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_DIVIDE_BYTE                         = new cydASMOpcodeMnemonic(OP_DIVIDE_BYTE,                        "OP_DIVIDE_BYTE",                       "divb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_DIVIDE_SHORT                        = new cydASMOpcodeMnemonic(OP_DIVIDE_SHORT,                       "OP_DIVIDE_SHORT",                      "divs",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_DIVIDE_INT                          = new cydASMOpcodeMnemonic(OP_DIVIDE_INT,                         "OP_DIVIDE_INT",                        "divi",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_DIVIDE_LONG                         = new cydASMOpcodeMnemonic(OP_DIVIDE_LONG,                        "OP_DIVIDE_LONG",                       "divl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_MODULUS_BYTE                        = new cydASMOpcodeMnemonic(OP_MODULUS_BYTE,                       "OP_MODULUS_BYTE",                      "modb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MODULUS_SHORT                       = new cydASMOpcodeMnemonic(OP_MODULUS_SHORT,                      "OP_MODULUS_SHORT",                     "mods",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MODULUS_INT                         = new cydASMOpcodeMnemonic(OP_MODULUS_INT,                        "OP_MODULUS_INT",                       "modi",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MODULUS_LONG                        = new cydASMOpcodeMnemonic(OP_MODULUS_LONG,                       "OP_MODULUS_LONG",                      "modl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_LEFT_BYTE                     = new cydASMOpcodeMnemonic(OP_SHIFT_LEFT_BYTE,                    "OP_SHIFT_LEFT_BYTE",                   "shlb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_LEFT_SHORT                    = new cydASMOpcodeMnemonic(OP_SHIFT_LEFT_SHORT,                   "OP_SHIFT_LEFT_SHORT",                  "shls",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_LEFT_INT                      = new cydASMOpcodeMnemonic(OP_SHIFT_LEFT_INT,                     "OP_SHIFT_LEFT_INT",                    "shli",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_LEFT_LONG                     = new cydASMOpcodeMnemonic(OP_SHIFT_LEFT_LONG,                    "OP_SHIFT_LEFT_LONG",                   "shll",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_RIGHT_BYTE                    = new cydASMOpcodeMnemonic(OP_SHIFT_RIGHT_BYTE,                   "OP_SHIFT_RIGHT_BYTE",                  "shrb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_RIGHT_SHORT                   = new cydASMOpcodeMnemonic(OP_SHIFT_RIGHT_SHORT,                  "OP_SHIFT_RIGHT_SHORT",                 "shrs",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_RIGHT_INT                     = new cydASMOpcodeMnemonic(OP_SHIFT_RIGHT_INT,                    "OP_SHIFT_RIGHT_INT",                   "shri",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_RIGHT_LONG                    = new cydASMOpcodeMnemonic(OP_SHIFT_RIGHT_LONG,                   "OP_SHIFT_RIGHT_LONG",                  "shrl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_AND_BYTE                            = new cydASMOpcodeMnemonic(OP_AND_BYTE,                           "OP_AND_BYTE",                          "andb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_AND_SHORT                           = new cydASMOpcodeMnemonic(OP_AND_SHORT,                          "OP_AND_SHORT",                         "ands",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_AND_INT                             = new cydASMOpcodeMnemonic(OP_AND_INT,                            "OP_AND_INT",                           "andi",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_AND_LONG                            = new cydASMOpcodeMnemonic(OP_AND_LONG,                           "OP_AND_LONG",                          "andl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_OR_BYTE                             = new cydASMOpcodeMnemonic(OP_OR_BYTE,                            "OP_OR_BYTE",                           "orb",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_OR_SHORT                            = new cydASMOpcodeMnemonic(OP_OR_SHORT,                           "OP_OR_SHORT",                          "ors",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_OR_INT                              = new cydASMOpcodeMnemonic(OP_OR_INT,                             "OP_OR_INT",                            "ori",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_OR_LONG                             = new cydASMOpcodeMnemonic(OP_OR_LONG,                            "OP_OR_LONG",                           "orl",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_BYTE                            = new cydASMOpcodeMnemonic(OP_NOT_BYTE,                           "OP_NOT_BYTE",                          "notb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_SHORT                           = new cydASMOpcodeMnemonic(OP_NOT_SHORT,                          "OP_NOT_SHORT",                         "nots",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_INT                             = new cydASMOpcodeMnemonic(OP_NOT_INT,                            "OP_NOT_INT",                           "noti",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_LONG                            = new cydASMOpcodeMnemonic(OP_NOT_LONG,                           "OP_NOT_LONG",                          "notl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_XOR_BYTE                            = new cydASMOpcodeMnemonic(OP_XOR_BYTE,                           "OP_XOR_BYTE",                          "xorb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_XOR_SHORT                           = new cydASMOpcodeMnemonic(OP_XOR_SHORT,                          "OP_XOR_SHORT",                         "xors",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_XOR_INT                             = new cydASMOpcodeMnemonic(OP_XOR_INT,                            "OP_XOR_INT",                           "xori",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_XOR_LONG                            = new cydASMOpcodeMnemonic(OP_XOR_LONG,                           "OP_XOR_LONG",                          "xorl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});

    public static final cydASMOpcodeMnemonic MNEMONIC_PLACE_CONST_BYTE                    = new cydASMOpcodeMnemonic(OP_PLACE_CONST_BYTE,                   "OP_PLACE_CONST_BYTE",                  "placecb",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_PLACE_CONST_SHORT                   = new cydASMOpcodeMnemonic(OP_PLACE_CONST_SHORT,                  "OP_PLACE_CONST_SHORT",                 "placecs",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_PLACE_CONST_INT                     = new cydASMOpcodeMnemonic(OP_PLACE_CONST_INT,                    "OP_PLACE_CONST_INT",                   "placeci",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_PLACE_CONST_LONG                    = new cydASMOpcodeMnemonic(OP_PLACE_CONST_LONG,                   "OP_PLACE_CONST_LONG",                  "placecl",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});

    public static final cydASMOpcodeMnemonic MNEMONIC_INC_BYTE                            = new cydASMOpcodeMnemonic(OP_INC_BYTE,                           "OP_INC_BYTE",                          "incb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_INC_SHORT                           = new cydASMOpcodeMnemonic(OP_INC_SHORT,                          "OP_INC_SHORT",                         "incs",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_INC_INT                             = new cydASMOpcodeMnemonic(OP_INC_INT,                            "OP_INC_INT",                           "inci",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_INC_LONG                            = new cydASMOpcodeMnemonic(OP_INC_LONG,                           "OP_INC_LONG",                          "incl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});

    public static final cydASMOpcodeMnemonic MNEMONIC_AND_CONST_BYTE                      = new cydASMOpcodeMnemonic(OP_AND_CONST_BYTE,                     "OP_AND_CONST_BYTE",                    "andcb",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_AND_CONST_SHORT                     = new cydASMOpcodeMnemonic(OP_AND_CONST_SHORT,                    "OP_AND_CONST_SHORT",                   "andcs",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_AND_CONST_INT                       = new cydASMOpcodeMnemonic(OP_AND_CONST_INT,                      "OP_AND_CONST_INT",                     "andci",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_AND_CONST_LONG                      = new cydASMOpcodeMnemonic(OP_AND_CONST_LONG,                     "OP_AND_CONST_LONG",                    "andcl",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});

    public static final cydASMOpcodeMnemonic MNEMONIC_OR_CONST_BYTE                       = new cydASMOpcodeMnemonic(OP_OR_CONST_BYTE,                      "OP_OR_CONST_BYTE",                     "orcb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_OR_CONST_SHORT                      = new cydASMOpcodeMnemonic(OP_OR_CONST_SHORT,                     "OP_OR_CONST_SHORT",                    "orcs",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_OR_CONST_INT                        = new cydASMOpcodeMnemonic(OP_OR_CONST_INT,                       "OP_OR_CONST_INT",                      "orci",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_OR_CONST_LONG                       = new cydASMOpcodeMnemonic(OP_OR_CONST_LONG,                      "OP_OR_CONST_LONG",                     "orcl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});

    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_LEFT_CONST_BYTE               = new cydASMOpcodeMnemonic(OP_SHIFT_LEFT_CONST_BYTE,              "OP_SHIFT_LEFT_CONST_BYTE",             "shlcb",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_LEFT_CONST_SHORT              = new cydASMOpcodeMnemonic(OP_SHIFT_LEFT_CONST_SHORT,             "OP_SHIFT_LEFT_CONST_SHORT",            "shlcs",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_LEFT_CONST_INT                = new cydASMOpcodeMnemonic(OP_SHIFT_LEFT_CONST_INT,               "OP_SHIFT_LEFT_CONST_INT",              "shlci",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_LEFT_CONST_LONG               = new cydASMOpcodeMnemonic(OP_SHIFT_LEFT_CONST_LONG,              "OP_SHIFT_LEFT_CONST_LONG",             "shlcl",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});

    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_RIGHT_CONST_BYTE              = new cydASMOpcodeMnemonic(OP_SHIFT_RIGHT_CONST_BYTE,             "OP_SHIFT_RIGHT_CONST_BYTE",            "shrcb",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_RIGHT_CONST_SHORT             = new cydASMOpcodeMnemonic(OP_SHIFT_RIGHT_CONST_SHORT,            "OP_SHIFT_RIGHT_CONST_SHORT",           "shrcs",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_RIGHT_CONST_INT               = new cydASMOpcodeMnemonic(OP_SHIFT_RIGHT_CONST_INT,              "OP_SHIFT_RIGHT_CONST_INT",             "shrci",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SHIFT_RIGHT_CONST_LONG              = new cydASMOpcodeMnemonic(OP_SHIFT_RIGHT_CONST_LONG,             "OP_SHIFT_RIGHT_CONST_LONG",            "shrcl",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_XOR_CONST_BYTE                      = new cydASMOpcodeMnemonic(OP_XOR_CONST_BYTE,                     "OP_XOR_CONST_BYTE",                    "xorcb",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_XOR_CONST_SHORT                     = new cydASMOpcodeMnemonic(OP_XOR_CONST_SHORT,                    "OP_XOR_CONST_SHORT",                   "xorcs",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_XOR_CONST_INT                       = new cydASMOpcodeMnemonic(OP_XOR_CONST_INT,                      "OP_XOR_CONST_INT",                     "xorci",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_XOR_CONST_LONG                      = new cydASMOpcodeMnemonic(OP_XOR_CONST_LONG,                     "OP_XOR_CONST_LONG",                    "xorcl",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_EQUALS_CONST_BYTE                   = new cydASMOpcodeMnemonic(OP_EQUALS_CONST_BYTE,                  "OP_EQUALS_CONST_BYTE",                 "eqcb",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_EQUALS_CONST_SHORT                  = new cydASMOpcodeMnemonic(OP_EQUALS_CONST_SHORT,                 "OP_EQUALS_CONST_SHORT",                "eqcs",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_EQUALS_CONST_INT                    = new cydASMOpcodeMnemonic(OP_EQUALS_CONST_INT,                   "OP_EQUALS_CONST_INT",                  "eqci",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_EQUALS_CONST_LONG                   = new cydASMOpcodeMnemonic(OP_EQUALS_CONST_LONG,                  "OP_EQUALS_CONST_LONG",                 "eqcl",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_EQUAL_CONST_BYTE                = new cydASMOpcodeMnemonic(OP_NOT_EQUAL_CONST_BYTE,               "OP_NOT_EQUAL_CONST_BYTE",              "neqcb",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_EQUAL_CONST_SHORT               = new cydASMOpcodeMnemonic(OP_NOT_EQUAL_CONST_SHORT,              "OP_NOT_EQUAL_CONST_SHORT",             "neqcs",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_EQUAL_CONST_INT                 = new cydASMOpcodeMnemonic(OP_NOT_EQUAL_CONST_INT,                "OP_NOT_EQUAL_CONST_INT",               "neqci",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_NOT_EQUAL_CONST_LONG                = new cydASMOpcodeMnemonic(OP_NOT_EQUAL_CONST_LONG,               "OP_NOT_EQUAL_CONST_LONG",              "neqcl",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_LESS_THAN_CONST_BYTE                = new cydASMOpcodeMnemonic(OP_LESS_THAN_CONST_BYTE,               "OP_LESS_THAN_CONST_BYTE",              "lstcb",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_LESS_THAN_CONST_SHORT               = new cydASMOpcodeMnemonic(OP_LESS_THAN_CONST_SHORT,              "OP_LESS_THAN_CONST_SHORT",             "lstcs",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_LESS_THAN_CONST_INT                 = new cydASMOpcodeMnemonic(OP_LESS_THAN_CONST_INT,                "OP_LESS_THAN_CONST_INT",               "lstci",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_LESS_THAN_CONST_LONG                = new cydASMOpcodeMnemonic(OP_LESS_THAN_CONST_LONG,               "OP_LESS_THAN_CONST_LONG",              "lstcl",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GREATER_THAN_CONST_BYTE             = new cydASMOpcodeMnemonic(OP_GREATER_THAN_CONST_BYTE,            "OP_GREATER_THAN_CONST_BYTE",           "grtcb",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GREATER_THAN_CONST_SHORT            = new cydASMOpcodeMnemonic(OP_GREATER_THAN_CONST_SHORT,           "OP_GREATER_THAN_CONST_SHORT",          "grtcs",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GREATER_THAN_CONST_INT              = new cydASMOpcodeMnemonic(OP_GREATER_THAN_CONST_INT,             "OP_GREATER_THAN_CONST_INT",            "grtci",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GREATER_THAN_CONST_LONG             = new cydASMOpcodeMnemonic(OP_GREATER_THAN_CONST_LONG,            "OP_GREATER_THAN_CONST_LONG",           "grtcl",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_ADDITION_CONST_BYTE                 = new cydASMOpcodeMnemonic(OP_ADDITION_CONST_BYTE,                "OP_ADDITION_CONST_BYTE",               "addcb",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ADDITION_CONST_SHORT                = new cydASMOpcodeMnemonic(OP_ADDITION_CONST_SHORT,               "OP_ADDITION_CONST_SHORT",              "addcs",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ADDITION_CONST_INT                  = new cydASMOpcodeMnemonic(OP_ADDITION_CONST_INT,                 "OP_ADDITION_CONST_INT",                "addci",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ADDITION_CONST_LONG                 = new cydASMOpcodeMnemonic(OP_ADDITION_CONST_LONG,                "OP_ADDITION_CONST_LONG",               "addcl",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_SUBTRACT_CONST_BYTE                 = new cydASMOpcodeMnemonic(OP_SUBTRACT_CONST_BYTE,                "OP_SUBTRACT_CONST_BYTE",               "subcb",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUBTRACT_CONST_SHORT                = new cydASMOpcodeMnemonic(OP_SUBTRACT_CONST_SHORT,               "OP_SUBTRACT_CONST_SHORT",              "subcs",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUBTRACT_CONST_INT                  = new cydASMOpcodeMnemonic(OP_SUBTRACT_CONST_INT,                 "OP_SUBTRACT_CONST_INT",                "subci",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_SUBTRACT_CONST_LONG                 = new cydASMOpcodeMnemonic(OP_SUBTRACT_CONST_LONG,                "OP_SUBTRACT_CONST_LONG",               "subcl",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTIPLY_CONST_BYTE                 = new cydASMOpcodeMnemonic(OP_MULTIPLY_CONST_BYTE,                "OP_MULTIPLY_CONST_BYTE",               "mulcb",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTIPLY_CONST_SHORT                = new cydASMOpcodeMnemonic(OP_MULTIPLY_CONST_SHORT,               "OP_MULTIPLY_CONST_SHORT",              "mulcs",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTIPLY_CONST_INT                  = new cydASMOpcodeMnemonic(OP_MULTIPLY_CONST_INT,                 "OP_MULTIPLY_CONST_INT",                "mulci",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MULTIPLY_CONST_LONG                 = new cydASMOpcodeMnemonic(OP_MULTIPLY_CONST_LONG,                "OP_MULTIPLY_CONST_LONG",               "mulcl",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_DIVIDE_CONST_BYTE                   = new cydASMOpcodeMnemonic(OP_DIVIDE_CONST_BYTE,                  "OP_DIVIDE_CONST_BYTE",                 "divcb",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_DIVIDE_CONST_SHORT                  = new cydASMOpcodeMnemonic(OP_DIVIDE_CONST_SHORT,                 "OP_DIVIDE_CONST_SHORT",                "divcs",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_DIVIDE_CONST_INT                    = new cydASMOpcodeMnemonic(OP_DIVIDE_CONST_INT,                   "OP_DIVIDE_CONST_INT",                  "divci",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_DIVIDE_CONST_LONG                   = new cydASMOpcodeMnemonic(OP_DIVIDE_CONST_LONG,                  "OP_DIVIDE_CONST_LONG",                 "divcl",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_MODULUS_CONST_BYTE                  = new cydASMOpcodeMnemonic(OP_MODULUS_CONST_BYTE,                 "OP_MODULUS_CONST_BYTE",                "modcb",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MODULUS_CONST_SHORT                 = new cydASMOpcodeMnemonic(OP_MODULUS_CONST_SHORT,                "OP_MODULUS_CONST_SHORT",               "modcs",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MODULUS_CONST_INT                   = new cydASMOpcodeMnemonic(OP_MODULUS_CONST_INT,                  "OP_MODULUS_CONST_INT",                 "modci",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_MODULUS_CONST_LONG                  = new cydASMOpcodeMnemonic(OP_MODULUS_CONST_LONG,                 "OP_MODULUS_CONST_LONG",                "modcl",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_LONG}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_OBJECT_EQUALS_METHOD                = new cydASMOpcodeMnemonic(OP_OBJECT_EQUALS_METHOD,               "OP_OBJECT_EQUALS_METHOD",              "objeqmeth",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_2_CONST_OPERAND                     = new cydASMOpcodeMnemonic(OP_2_CONST_OPERAND,                    "OP_2_CONST_OPERAND",                   "const2o",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_3_CONST_OPERAND                     = new cydASMOpcodeMnemonic(OP_3_CONST_OPERAND,                    "OP_3_CONST_OPERAND",                   "const3o",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_4_CONST_OPERAND                     = new cydASMOpcodeMnemonic(OP_4_CONST_OPERAND,                    "OP_4_CONST_OPERAND",                   "const4o",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_WHITE_OUT                      = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_WHITE_OUT,                "SUB_OP_ONE_GAME_WHITE_OUT",                "gm_whiteout",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_RAND                           = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_RAND,                     "SUB_OP_ONE_GAME_RAND",                     "gm_rnd",        new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_RAND_MAX                       = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_RAND_MAX,                 "SUB_OP_ONE_GAME_RAND_MAX",                 "gm_rndmin",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_RAND_MIN_MAX                   = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_RAND_MIN_MAX,             "SUB_OP_ONE_GAME_RAND_MIN_MAX",             "gm_rndminmax",  new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_RAND_SEED                      = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_RAND_SEED,                "SUB_OP_ONE_GAME_RAND_SEED",                "gm_rndseed",    new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_TIMER                      = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_TIMER,                "SUB_OP_ONE_GAME_SET_TIMER",                "gm_timer",      new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_RELEASE_TIMER                  = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_RELEASE_TIMER,            "SUB_OP_ONE_GAME_RELEASE_TIMER",            "gm_endtimer",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_GRAVITY                    = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_GRAVITY,              "SUB_OP_ONE_GAME_SET_GRAVITY",              "gm_set_grav",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_CAMERA_Y                   = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_CAMERA_Y,             "SUB_OP_ONE_GAME_SET_CAMERA_Y",             "gm_set_camy",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_SHAKE                      = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_SHAKE,                "SUB_OP_ONE_GAME_SET_SHAKE",                "gm_set_shake",  new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_SLOWDOWN_RATE              = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_SLOWDOWN_RATE,        "SUB_OP_ONE_GAME_SET_SLOWDOWN_RATE",        "gm_set_sdrate", new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_STATE                      = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_STATE,                "SUB_OP_ONE_GAME_SET_STATE",                "gm_set_state",  new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_BACKGROUND_COLOR           = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_BACKGROUND_COLOR,     "SUB_OP_ONE_GAME_SET_BACKGROUND_COLOR",     "gm_set_bgclr",  new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_INC_POWER_TIME             = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_INC_POWER_TIME,       "SUB_OP_ONE_GAME_SET_INC_POWER_TIME",       "gm_set_incpwr", new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_GRAVITY                    = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_GRAVITY,              "SUB_OP_ONE_GAME_GET_GRAVITY",              "gm_get_grav",   new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_CAMERA_Y                   = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_CAMERA_Y,             "SUB_OP_ONE_GAME_GET_CAMERA_Y",             "gm_get_camy",   new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_SHAKE                      = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_SHAKE,                "SUB_OP_ONE_GAME_GET_SHAKE",                "gm_get_shake",  new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_SLOWDOWN_RATE              = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_SLOWDOWN_RATE,        "SUB_OP_ONE_GAME_GET_SLOWDOWN_RATE",        "gm_get_sdrate", new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_STATE                      = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_STATE,                "SUB_OP_ONE_GAME_GET_STATE",                "gm_get_state",  new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_BACKGROUND_COLOR           = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_BACKGROUND_COLOR,     "SUB_OP_ONE_GAME_GET_BACKGROUND_COLOR",     "gm_get_bgclr",  new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_INC_POWER_TIME             = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_INC_POWER_TIME,       "SUB_OP_ONE_GAME_GET_INC_POWER_TIME",       "gm_get_incpwr", new int[] {}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_ON_BLOCK_STEP                  = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_ON_BLOCK_STEP,            "SUB_OP_ONE_GAME_ON_BLOCK_STEP",             "gm_on_block",  new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_XY_POS              = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_XY_POS,        "SUB_OP_ONE_GAME_SET_PLAYER_XY_POS",         "gm_set_xy",    new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_X_POS               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_X_POS,         "SUB_OP_ONE_GAME_SET_PLAYER_X_POS",          "gm_set_x",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_Y_POS               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_Y_POS,         "SUB_OP_ONE_GAME_SET_PLAYER_Y_POS",          "gm_set_y",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_X_RATE              = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_X_RATE,        "SUB_OP_ONE_GAME_SET_PLAYER_X_RATE",         "gm_set_xrate", new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_Y_RATE              = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_Y_RATE,        "SUB_OP_ONE_GAME_SET_PLAYER_Y_RATE",         "gm_set_yrate", new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_GRAV_AFFECTED       = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_GRAV_AFFECTED, "SUB_OP_ONE_GAME_SET_PLAYER_GRAV_AFFECTED",  "gm_set_ga",    new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_LEFT_BOUND          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_LEFT_BOUND,    "SUB_OP_ONE_GAME_SET_PLAYER_LEFT_BOUND",     "gm_set_lb",    new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_RIGHT_BOUND         = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_BOUND,   "SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_BOUND",    "gm_set_rb",    new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_XY_POS              = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_XY_POS,        "SUB_OP_ONE_GAME_GET_PLAYER_XY_POS",         "gm_get_xy",    new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_X_POS               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_X_POS,         "SUB_OP_ONE_GAME_GET_PLAYER_X_POS",          "gm_get_x",     new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_Y_POS               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_Y_POS,         "SUB_OP_ONE_GAME_GET_PLAYER_Y_POS",          "gm_get_y",     new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_X_RATE              = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_X_RATE,        "SUB_OP_ONE_GAME_GET_PLAYER_X_RATE",         "gm_get_xrate", new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_Y_RATE              = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_Y_RATE,        "SUB_OP_ONE_GAME_GET_PLAYER_Y_RATE",         "gm_get_yrate", new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_GRAV_AFFECTED       = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_GRAV_AFFECTED, "SUB_OP_ONE_GAME_GET_PLAYER_GRAV_AFFECTED",  "gm_get_ga",    new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_LEFT_BOUND          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_LEFT_BOUND,    "SUB_OP_ONE_GAME_GET_PLAYER_LEFT_BOUND",     "gm_get_lb",    new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_RIGHT_BOUND         = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_RIGHT_BOUND,   "SUB_OP_ONE_GAME_GET_PLAYER_RIGHT_BOUND",    "gm_get_rb",    new int[] {}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_WHITE_OUT_STATIC               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_WHITE_OUT_STATIC,                "SUB_OP_ONE_GAME_WHITE_OUT_STATIC",                 "gm_whiteout_stat",   new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_RAND_MAX_STATIC                = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_RAND_MAX_STATIC,                 "SUB_OP_ONE_GAME_RAND_MAX_STATIC",                  "gm_rndmin_stat",     new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_RAND_MIN_MAX_STATIC            = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_RAND_MIN_MAX_STATIC,             "SUB_OP_ONE_GAME_RAND_MIN_MAX_STATIC",              "gm_rndminmax_stat",  new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_RAND_SEED_STATIC               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_RAND_SEED_STATIC,                "SUB_OP_ONE_GAME_RAND_SEED_STATIC",                 "gm_rndseed_stat",    new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_TIMER_STATIC               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_TIMER_STATIC,                "SUB_OP_ONE_GAME_SET_TIMER_STATIC",                 "gm_timer_stat",      new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_RELEASE_TIMER_STATIC           = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_RELEASE_TIMER_STATIC,            "SUB_OP_ONE_GAME_RELEASE_TIMER_STATIC",             "gm_endtimer_stat",   new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_GRAVITY_STATIC             = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_GRAVITY_STATIC,              "SUB_OP_ONE_GAME_SET_GRAVITY_STATIC",               "gm_set_grav_stat",   new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_CAMERA_Y_STATIC            = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_CAMERA_Y_STATIC,             "SUB_OP_ONE_GAME_SET_CAMERA_Y_STATIC",              "gm_set_camy_stat",   new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_SHAKE_STATIC               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_SHAKE_STATIC,                "SUB_OP_ONE_GAME_SET_SHAKE_STATIC",                 "gm_set_shake_stat",  new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_SLOWDOWN_RATE_STATIC       = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_SLOWDOWN_RATE_STATIC,        "SUB_OP_ONE_GAME_SET_SLOWDOWN_RATE_STATIC",         "gm_set_sdrate_stat", new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_STATE_STATIC               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_STATE_STATIC,                "SUB_OP_ONE_GAME_SET_STATE_STATIC",                 "gm_set_state_stat",  new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_BACKGROUND_COLOR_STATIC    = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_BACKGROUND_COLOR_STATIC,     "SUB_OP_ONE_GAME_SET_BACKGROUND_COLOR_STATIC",      "gm_set_bgclr_stat",  new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_INC_POWER_TIME_STATIC      = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_INC_POWER_TIME_STATIC,       "SUB_OP_ONE_GAME_SET_INC_POWER_TIME_STATIC",        "gm_set_incpwr_stat", new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_ON_BLOCK_STEP_STATIC           = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_ON_BLOCK_STEP_STATIC,            "SUB_OP_ONE_GAME_ON_BLOCK_STEP_STATIC",             "gm_on_block_stat",   new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_XY_POS_STATIC       = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_XY_POS_STATIC,        "SUB_OP_ONE_GAME_SET_PLAYER_XY_POS_STATIC",         "gm_set_xy_stat",     new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_X_POS_STATIC        = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_X_POS_STATIC,         "SUB_OP_ONE_GAME_SET_PLAYER_X_POS_STATIC",          "gm_set_x_stat",      new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_Y_POS_STATIC        = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_Y_POS_STATIC,         "SUB_OP_ONE_GAME_SET_PLAYER_Y_POS_STATIC",          "gm_set_y_stat",      new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_X_RATE_STATIC       = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_X_RATE_STATIC,        "SUB_OP_ONE_GAME_SET_PLAYER_X_RATE_STATIC",         "gm_set_xrate_stat",  new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_Y_RATE_STATIC       = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_Y_RATE_STATIC,        "SUB_OP_ONE_GAME_SET_PLAYER_Y_RATE_STATIC",         "gm_set_yrate_stat",  new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_GRAV_AFFECTED_STATIC= new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_GRAV_AFFECTED_STATIC, "SUB_OP_ONE_GAME_SET_PLAYER_GRAV_AFFECTED_STATIC",  "gm_set_ga_stat",     new int[] {cydASMOpcodeMnemonic.INTERNAL_BYTE}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_LEFT_BOUND_STATIC   = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_LEFT_BOUND_STATIC,    "SUB_OP_ONE_GAME_SET_PLAYER_LEFT_BOUND_STATIC",     "gm_set_lb_stat",     new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_RIGHT_BOUND_STATIC  = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_BOUND_STATIC,   "SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_BOUND_STATIC",    "gm_set_rb_stat",     new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_TOTAL_HEIGHT               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_TOTAL_HEIGHT,                "SUB_OP_ONE_GAME_GET_TOTAL_HEIGHT",                 "gm_get_th",          new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_WORKING_HEIGHT             = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_WORKING_HEIGHT,              "SUB_OP_ONE_GAME_GET_WORKING_HEIGHT",               "gm_get_wh",          new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_WORKING_TOP_Y              = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_WORKING_TOP_Y,               "SUB_OP_ONE_GAME_GET_WORKING_TOP_Y",                "gm_get_wtopy",       new int[] {}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_ON_LEFT_HIT                    = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_ON_LEFT_HIT,                     "SUB_OP_ONE_GAME_ON_LEFT_HIT",                      "gm_on_lhit",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_ON_RIGHT_HIT                   = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_ON_RIGHT_HIT,                    "SUB_OP_ONE_GAME_ON_RIGHT_HIT",                     "gm_on_rhit",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_ON_LEFT_HIT_STATIC             = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_ON_LEFT_HIT_STATIC,              "SUB_OP_ONE_GAME_ON_LEFT_HIT_STATIC",               "gm_on_lhit_stat",    new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_ON_RIGHT_HIT_STATIC            = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_ON_RIGHT_HIT_STATIC,             "SUB_OP_ONE_GAME_ON_RIGHT_HIT_STATIC",              "gm_on_rhit_stat",    new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_LEVEL_DATA                 = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_LEVEL_DATA,                  "SUB_OP_ONE_GAME_GET_LEVEL_DATA",                   "gm_gld",             new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_GAME_FLAGS                 = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_GAME_FLAGS,                  "SUB_OP_ONE_GAME_GET_GAME_FLAGS",                   "gm_get_gf",          new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_GAME_FLAGS                 = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_GAME_FLAGS,                  "SUB_OP_ONE_GAME_SET_GAME_FLAGS",                   "gm_set_gf",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_GAME_FLAGS_STATIC          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_GAME_FLAGS_STATIC,           "SUB_OP_ONE_GAME_SET_GAME_FLAGS_STATIC",            "gm_set_gf_stat",     new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_LEFT_RIGHT_STAND    = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_STAND,     "SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_STAND",      "gm_set_lrs",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_LEFT_STAND          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_LEFT_STAND,           "SUB_OP_ONE_GAME_SET_PLAYER_LEFT_STAND",            "gm_set_ls",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_RIGHT_STAND         = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_STAND,          "SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_STAND",           "gm_set_rs",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_LEFT_RIGHT_STAND_STATIC = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_STAND_STATIC, "SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_STAND_STATIC", "gm_set_lrs_stat", new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_LEFT_STAND_STATIC   = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_LEFT_STAND_STATIC,    "SUB_OP_ONE_GAME_SET_PLAYER_LEFT_STAND_STATIC",     "gm_set_ls_stat",     new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_RIGHT_STAND_STATIC  = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_STAND_STATIC,   "SUB_OP_ONE_GAME_SET_PLAYER_RIGHT_STAND_STATIC",    "gm_set_rs_stat",     new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_LEFT_RIGHT_STAND    = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_LEFT_RIGHT_STAND,     "SUB_OP_ONE_GAME_GET_PLAYER_LEFT_RIGHT_STAND",      "gm_get_lrs",         new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_LEFT_STAND          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_LEFT_STAND,           "SUB_OP_ONE_GAME_GET_PLAYER_LEFT_STAND",            "gm_get_ls",          new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_RIGHT_STAND         = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_RIGHT_STAND,          "SUB_OP_ONE_GAME_GET_PLAYER_RIGHT_STAND",           "gm_get_rs",          new int[] {}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_ON_DIE                         = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_ON_DIE,                          "SUB_OP_ONE_GAME_ON_DIE",                           "gm_on_die",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_ON_DIE_STATIC                  = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_ON_DIE_STATIC,                   "SUB_OP_ONE_GAME_ON_DIE_STATIC",                    "gm_on_die_stat",     new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_GAME_FLAGS_2               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_GAME_FLAGS_2,                "SUB_OP_ONE_GAME_GET_GAME_FLAGS_2",                 "gm_get_gf2",         new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_GAME_FLAGS_2               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_GAME_FLAGS_2,                "SUB_OP_ONE_GAME_SET_GAME_FLAGS_2",                 "gm_set_gf2",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_GAME_FLAGS_2_STATIC        = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_GAME_FLAGS_2_STATIC,         "SUB_OP_ONE_GAME_SET_GAME_FLAGS_2_STATIC",          "gm_set_gf2_stat",    new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_CURRENT_FLOOR_NUMBER       = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_CURRENT_FLOOR_NUMBER,        "SUB_OP_ONE_GAME_GET_CURRENT_FLOOR_NUMBER",         "gm_get_floornum",    new int[] {}, new int[] {});

    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_GAME_IS_TIMER_ACTIVE       = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_IS_TIMER_ACTIVE,                 "SUB_OP_ONE_GAME_IS_TIMER_ACTIVE",                  "gm_is_ta",           new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_GAME_IS_TIMER_ACTIVE_STATIC= new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_IS_TIMER_ACTIVE_STATIC,          "SUB_OP_ONE_GAME_IS_TIMER_ACTIVE_STATIC",           "gm_is_ta_stat",      new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_BLOCK_REF_PIXEL            = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_BLOCK_REF_PIXEL,             "SUB_OP_ONE_GAME_GET_BLOCK_REF_PIXEL",              "gm_get_b_rp",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_BLOCK_REF_PIXEL            = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_BLOCK_REF_PIXEL,             "SUB_OP_ONE_GAME_SET_BLOCK_REF_PIXEL",              "gm_set_b_rp",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_BLOCK_REF_PIXEL_STATIC     = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_BLOCK_REF_PIXEL_STATIC,      "SUB_OP_ONE_GAME_SET_BLOCK_REF_PIXEL_STATIC",       "gm_set_b_rp_stat",   new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_REF_PIXEL           = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_REF_PIXEL,            "SUB_OP_ONE_GAME_GET_PLAYER_REF_PIXEL",             "gm_get_p_rp",        new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_REF_PIXEL           = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_REF_PIXEL,            "SUB_OP_ONE_GAME_SET_PLAYER_REF_PIXEL",             "gm_set_p_rp",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_REF_PIXEL_STATIC    = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_REF_PIXEL_STATIC,     "SUB_OP_ONE_GAME_SET_PLAYER_REF_PIXEL_STATIC",      "gm_set_p_rp_stat",   new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_BLOCK_FRAME_SEQ            = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_BLOCK_FRAME_SEQ,             "SUB_OP_ONE_GAME_SET_BLOCK_FRAME_SEQ",              "gm_set_b_fs",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_BLOCK_TIME_SEQ             = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_BLOCK_TIME_SEQ,              "SUB_OP_ONE_GAME_SET_BLOCK_TIME_SEQ",               "gm_set_b_ts",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_FRAME_SEQ           = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_FRAME_SEQ,            "SUB_OP_ONE_GAME_SET_PLAYER_FRAME_SEQ",             "gm_set_p_fs",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_LEFT_RIGHT_ONLY     = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_ONLY,      "SUB_OP_ONE_GAME_SET_PLAYER_LEFT_RIGHT_ONLY",       "gm_set_lr_only",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});    
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_POWER                      = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_POWER,                       "SUB_OP_ONE_GAME_GET_POWER",                        "gm_get_pow",         new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_POWER                      = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_POWER,                       "SUB_OP_ONE_GAME_SET_POWER",                        "gm_set_pow",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_POWER_STATIC               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_POWER_STATIC,                "SUB_OP_ONE_GAME_SET_POWER_STATIC",                 "gm_set_pow_stat",    new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});

    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_ON_BLOCK_DEPART                = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_ON_BLOCK_DEPART,                 "SUB_OP_ONE_GAME_ON_BLOCK_DEPART",                  "gm_on_depart",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_ON_BLOCK_DEPART_STATIC         = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_ON_BLOCK_DEPART_STATIC,          "SUB_OP_ONE_GAME_ON_BLOCK_DEPART_STATIC",           "gm_on_depart_stat",  new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_RET                        = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_RET,                         "SUB_OP_ONE_GAME_GET_RET",                          "gm_get_ret",         new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_RET                        = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_RET,                         "SUB_OP_ONE_GAME_SET_RET",                          "gm_set_ret",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_RET_STATIC                 = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_RET_STATIC,                  "SUB_OP_ONE_GAME_SET_RET_STATIC",                   "gm_set_ret_stat",    new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_LOAD_FROM_JAR                  = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_LOAD_FROM_JAR,                   "SUB_OP_ONE_GAME_LOAD_FROM_JAR",                    "gm_load_from_jar",   new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_NEW_MIDI                       = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_NEW_MIDI,                        "SUB_OP_ONE_GAME_NEW_MIDI",                         "gm_new_midi",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_START_MIDI                     = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_START_MIDI,                      "SUB_OP_ONE_GAME_START_MIDI",                       "gm_start_midi",      new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_STOP_MIDI                      = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_STOP_MIDI,                       "SUB_OP_ONE_GAME_STOP_MIDI",                        "gm_stop_midi",       new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_CLOSE_MIDI                     = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_CLOSE_MIDI,                      "SUB_OP_ONE_GAME_CLOSE_MIDI",                       "gm_close_midi",      new int[] {}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_FLASH_BACKLIGHT                = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_FLASH_BACKLIGHT,                 "SUB_OP_ONE_GAME_FLASH_BACKLIGHT",                  "gm_fb",              new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_VIBRATE                        = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_VIBRATE,                         "SUB_OP_ONE_GAME_VIBRATE",                          "gm_vib",             new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_FLASH_BACKLIGHT_STATIC         = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_FLASH_BACKLIGHT_STATIC,          "SUB_OP_ONE_GAME_FLASH_BACKLIGHT_STATIC",           "gm_fb_stat",         new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_VIBRATE_STATIC                 = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_VIBRATE_STATIC,                  "SUB_OP_ONE_GAME_VIBRATE_STATIC",                   "gm_vib_stat",        new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    

    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_WIDTH               = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_WIDTH,                "SUB_OP_ONE_GAME_GET_PLAYER_WIDTH",                 "gm_get_pwidth",      new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_WIDTH_PIXEL         = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_WIDTH_PIXEL,          "SUB_OP_ONE_GAME_SET_PLAYER_WIDTH_PIXEL",           "gm_set_pwidth",      new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_WIDTH_PIXEL_STATIC  = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_WIDTH_PIXEL_STATIC,   "SUB_OP_ONE_GAME_SET_PLAYER_WIDTH_PIXEL_STATIC",    "gm_get_pwidth_stat", new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});

    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_PLAYER_HEIGHT              = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_PLAYER_HEIGHT,               "SUB_OP_ONE_GAME_GET_PLAYER_HEIGHT",                "gm_get_pheight",     new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_HEIGHT_PIXEL        = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_HEIGHT_PIXEL,         "SUB_OP_ONE_GAME_SET_PLAYER_HEIGHT_PIXEL",          "gm_set_pheight",     new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_PLAYER_HEIGHT_PIXEL_STATIC = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_PLAYER_HEIGHT_PIXEL_STATIC,  "SUB_OP_ONE_GAME_SET_PLAYER_HEIGHT_PIXEL_STATIC",   "gm_set_pheight_stat",new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_GET_BLOCK_COLL_OFFSET          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_GET_BLOCK_COLL_OFFSET,           "SUB_OP_ONE_GAME_GET_BLOCK_COLL_OFFSET",            "gm_get_b_co",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_BLOCK_COLL_OFFSET          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_BLOCK_COLL_OFFSET,           "SUB_OP_ONE_GAME_SET_BLOCK_COLL_OFFSET",            "gm_set_b_co",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_BLOCK_COLL_OFFSET_STATIC   = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_BLOCK_COLL_OFFSET_STATIC,    "SUB_OP_ONE_GAME_SET_BLOCK_COLL_OFFSET_STATIC",     "gm_set_b_co_stat",   new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});

    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SET_LEVEL_DATA                 = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SET_LEVEL_DATA,                  "SUB_OP_ONE_GAME_SET_LEVEL_DATA",                   "gm_sld",             new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    

    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_RESIZE_LAYER_ITEMS                       = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_RESIZE_LAYER_ITEMS,                 "SUB_OP_ONE_GAME_SCENE_RESIZE_LAYER_ITEMS",                 "gm_s_rli",            new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_RESIZE_LAYER                             = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_RESIZE_LAYER,                       "SUB_OP_ONE_GAME_SCENE_RESIZE_LAYER",                       "gm_s_rl",             new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_RESIZE_DB                                = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_RESIZE_DB,                          "SUB_OP_ONE_GAME_SCENE_RESIZE_DB",                          "gm_s_rdb",            new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_LOAD_NEW_INTERPOLATION_POINTS            = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_LOAD_NEW_INTERPOLATION_POINTS,      "SUB_OP_ONE_GAME_SCENE_LOAD_NEW_INTERPOLATION_POINTS",      "gm_s_nip",            new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_LOAD_NEW_IMAGE                           = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_LOAD_NEW_IMAGE,                     "SUB_OP_ONE_GAME_SCENE_LOAD_NEW_IMAGE",                     "gm_s_ni",             new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_LOAD_NEW_SPRITE                          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_LOAD_NEW_SPRITE,                    "SUB_OP_ONE_GAME_SCENE_LOAD_NEW_SPRITE",                    "gm_s_ns",             new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_LOAD_NEW_SEQUENCE                        = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_LOAD_NEW_SEQUENCE,                  "SUB_OP_ONE_GAME_SCENE_LOAD_NEW_SEQUENCE",                  "gm_s_nseq",           new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_GET_IMAGE_COUNT                          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_GET_IMAGE_COUNT,                    "SUB_OP_ONE_GAME_SCENE_GET_IMAGE_COUNT",                    "gm_s_get_imgc",       new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_GET_INTERPOLATOR_COUNT                   = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATOR_COUNT,             "SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATOR_COUNT",             "gm_s_get_intc",       new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_GET_SEQUENCE_COUNT                       = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_GET_SEQUENCE_COUNT,                 "SUB_OP_ONE_GAME_SCENE_GET_SEQUENCE_COUNT",                 "gm_s_get_setc",       new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_GET_SPRITE_COUNT                         = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_GET_SPRITE_COUNT,                   "SUB_OP_ONE_GAME_SCENE_GET_SPRITE_COUNT",                   "gm_s_get_sc",         new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_GET_SPRITE_BLOCK_INDEX                   = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_GET_SPRITE_BLOCK_INDEX,             "SUB_OP_ONE_GAME_SCENE_GET_SPRITE_BLOCK_INDEX",             "gm_s_get_sbc",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_GET_SPRITE_BLOCK_INDEX_STATIC            = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_GET_SPRITE_BLOCK_INDEX_STATIC,      "SUB_OP_ONE_GAME_SCENE_GET_SPRITE_BLOCK_INDEX_STATIC",      "gm_s_get_sbc_stat",   new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_GET_ITEM_LENGTH                          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_GET_ITEM_LENGTH,                    "SUB_OP_ONE_GAME_SCENE_GET_ITEM_LENGTH",                    "gm_s_get_il",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_GET_LAYER_LENGTH                         = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_GET_LAYER_LENGTH,                   "SUB_OP_ONE_GAME_SCENE_GET_LAYER_LENGTH",                   "gm_s_get_llen",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_GET_LAYER_PARAM                          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_GET_LAYER_PARAM,                    "SUB_OP_ONE_GAME_SCENE_GET_LAYER_PARAM",                    "gm_s_get_lparm",      new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_IS_INTERPOLATOR_DONE                     = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_IS_INTERPOLATOR_DONE,               "SUB_OP_ONE_GAME_SCENE_IS_INTERPOLATOR_DONE",               "gm_s_is_id",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_IS_INTERPOLATOR_DONE_STATIC              = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_IS_INTERPOLATOR_DONE_STATIC,        "SUB_OP_ONE_GAME_SCENE_IS_INTERPOLATOR_DONE_STATIC",        "gm_s_is_id_stat",     new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_IS_LAYER_VISIBLE                         = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_IS_LAYER_VISIBLE,                   "SUB_OP_ONE_GAME_SCENE_IS_LAYER_VISIBLE",                   "gm_s_is_lv",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_IS_LAYER_VISIBLE_STATIC                  = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_IS_LAYER_VISIBLE_STATIC,            "SUB_OP_ONE_GAME_SCENE_IS_LAYER_VISIBLE_STATIC",            "gm_s_is_lv_stat",     new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_IS_SPRITE_LINK_VISIBLE                   = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_IS_SPRITE_LINK_VISIBLE,             "SUB_OP_ONE_GAME_SCENE_IS_SPRITE_LINK_VISIBLE",             "gm_s_is_slv",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_IS_SPRITE_LINK_VISIBLE_STATIC            = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_IS_SPRITE_LINK_VISIBLE_STATIC,      "SUB_OP_ONE_GAME_SCENE_IS_SPRITE_LINK_VISIBLE_STATIC",      "gm_s_is_slv_stat",    new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_SET_LAYER_PARAM                          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_SET_LAYER_PARAM,                    "SUB_OP_ONE_GAME_SCENE_SET_LAYER_PARAM",                    "gm_s_set_lparm",      new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_SET_LAYER_VISIBILITY                     = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_SET_LAYER_VISIBILITY,               "SUB_OP_ONE_GAME_SCENE_SET_LAYER_VISIBILITY",               "gm_s_set_lv",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_SET_LAYER_VISIBILITY_STATIC              = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_SET_LAYER_VISIBILITY_STATIC,        "SUB_OP_ONE_GAME_SCENE_SET_LAYER_VISIBILITY_STATIC",        "gm_s_set_lv_stat",    new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_SWITCH_LAYERS                            = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_SWITCH_LAYERS,                      "SUB_OP_ONE_GAME_SCENE_SWITCH_LAYERS",                      "gm_s_swl",            new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_SWITCH_LAYERS_STATIC                     = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_SWITCH_LAYERS_STATIC,               "SUB_OP_ONE_GAME_SCENE_SWITCH_LAYERS_STATIC",               "gm_s_swl_stat",       new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_FLIP_SETS                                = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_FLIP_SETS,                          "SUB_OP_ONE_GAME_SCENE_FLIP_SETS",                          "gm_s_fs",             new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_SWITCH_SPRITE_SEQUENCE                   = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_SWITCH_SPRITE_SEQUENCE,             "SUB_OP_ONE_GAME_SCENE_SWITCH_SPRITE_SEQUENCE",             "gm_s_ssseq",          new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_SWITCH_SPRITE_SEQUENCE_STATIC            = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_SWITCH_SPRITE_SEQUENCE_STATIC,      "SUB_OP_ONE_GAME_SCENE_SWITCH_SPRITE_SEQUENCE_STATIC",      "gm_s_ssseq_stat",     new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_RESET_INTERPOLATION_POINT                = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_RESET_INTERPOLATION_POINT,          "SUB_OP_ONE_GAME_SCENE_RESET_INTERPOLATION_POINT",          "gm_s_rst_ip",         new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_RESET_INTERPOLATION_POINT_STATIC         = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_RESET_INTERPOLATION_POINT_STATIC,   "SUB_OP_ONE_GAME_SCENE_RESET_INTERPOLATION_POINT_STATIC",   "gm_s_rst_ip_stat",    new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_GET_ITEM                                 = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_GET_ITEM,                           "SUB_OP_ONE_GAME_SCENE_GET_ITEM",                           "gm_s_get_item",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_GET_ITEM_STATIC                          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_GET_ITEM_STATIC,                    "SUB_OP_ONE_GAME_SCENE_GET_ITEM_STATIC",                    "gm_s_get_item_stat",  new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});
    
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_SET_ITEM                                 = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_SET_ITEM,                           "SUB_OP_ONE_GAME_SCENE_SET_ITEM",                           "gm_s_set_item",       new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_SET_ITEM_STATIC                          = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_SET_ITEM_STATIC,                    "SUB_OP_ONE_GAME_SCENE_SET_ITEM_STATIC",                    "gm_s_set_item_stat",  new int[] {cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT, cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});

    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_GET_INTERPOLATION_POINT                  = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATION_POINT,            "SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATION_POINT",            "gm_g_int_pnt",        new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_GAME_SCENE_GET_INTERPOLATION_POINT_STATIC           = new cydASMOpcodeMnemonic(SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATION_POINT_STATIC,     "SUB_OP_ONE_GAME_SCENE_GET_INTERPOLATION_POINT_STATIC",     "gm_g_int_pnt_stat",   new int[] {cydASMOpcodeMnemonic.INTERNAL_INT}, new int[] {});

    public static final cydASMOpcodeMnemonic MNEMONIC_ARRAY_COPY                          = new cydASMOpcodeMnemonic(SUB_OP_ONE_ARRAY_COPY,                    "SUB_OP_ONE_ARRAY_COPY",                    "arr_copy",             new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ARRAY_BYTE_TO_SHORT                 = new cydASMOpcodeMnemonic(SUB_OP_ONE_ARRAY_BYTE_TO_SHORT,           "SUB_OP_ONE_ARRAY_BYTE_TO_SHORT",           "arr_b2s",              new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ARRAY_BYTE_TO_INT                   = new cydASMOpcodeMnemonic(SUB_OP_ONE_ARRAY_BYTE_TO_INT,             "SUB_OP_ONE_ARRAY_BYTE_TO_INT",             "arr_b2i",              new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ARRAY_BYTE_TO_LONG                  = new cydASMOpcodeMnemonic(SUB_OP_ONE_ARRAY_BYTE_TO_LONG,            "SUB_OP_ONE_ARRAY_BYTE_TO_LONG",            "arr_b2l",              new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ARRAY_SHORT_TO_BYTE                 = new cydASMOpcodeMnemonic(SUB_OP_ONE_ARRAY_SHORT_TO_BYTE,           "SUB_OP_ONE_ARRAY_SHORT_TO_BYTE",           "arr_s2b",              new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ARRAY_INT_TO_BYTE                   = new cydASMOpcodeMnemonic(SUB_OP_ONE_ARRAY_INT_TO_BYTE,             "SUB_OP_ONE_ARRAY_INT_TO_BYTE",             "arr_i2b",              new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_ARRAY_LONG_TO_BYTE                  = new cydASMOpcodeMnemonic(SUB_OP_ONE_ARRAY_LONG_TO_BYTE,            "SUB_OP_ONE_ARRAY_LONG_TO_BYTE",            "arr_l2b",              new int[] {cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT, cydASMOpcodeMnemonic.INTERNAL_SHORT}, new int[] {});



    
    public static final cydASMOpcodeMnemonic MNEMONIC_HT_NEW                              = new cydASMOpcodeMnemonic(SUB_OP_ONE_HT_NEW,                      "SUB_OP_ONE_HT_NEW",                      "ht-new1",       new int[] {}, new int[] {});
    public static final cydASMOpcodeMnemonic MNEMONIC_HT_NEW_INITIALCAP                   = new cydASMOpcodeMnemonic(SUB_OP_ONE_HT_NEW_INITIALCAP,           "SUB_OP_ONE_HT_NEW_INITIALCAP",           "ht-new2",       new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_INT});
    public static final cydASMOpcodeMnemonic MNEMONIC_HT_CLEAR                            = new cydASMOpcodeMnemonic(SUB_OP_ONE_HT_CLEAR,                    "SUB_OP_ONE_HT_CLEAR",                    "ht-clear",      new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_OBJECT});
    public static final cydASMOpcodeMnemonic MNEMONIC_HT_CONTAINS                         = new cydASMOpcodeMnemonic(SUB_OP_ONE_HT_CONTAINS,                 "SUB_OP_ONE_HT_CONTAINS",                 "ht-contains",   new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_OBJECT, cydASMOpcodeMnemonic.INDEX_OBJECT});
    public static final cydASMOpcodeMnemonic MNEMONIC_HT_CONTAINSKEY                      = new cydASMOpcodeMnemonic(SUB_OP_ONE_HT_CONTAINSKEY,              "SUB_OP_ONE_HT_CONTAINSKEY",              "ht-containskey",new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_OBJECT, cydASMOpcodeMnemonic.INDEX_OBJECT});
    public static final cydASMOpcodeMnemonic MNEMONIC_HT_ELEMENTS                         = new cydASMOpcodeMnemonic(SUB_OP_ONE_HT_ELEMENTS,                 "SUB_OP_ONE_HT_ELEMENTS",                 "ht-elems",      new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_OBJECT});
    public static final cydASMOpcodeMnemonic MNEMONIC_HT_GET                              = new cydASMOpcodeMnemonic(SUB_OP_ONE_HT_GET,                      "SUB_OP_ONE_HT_GET",                      "ht-get",        new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_OBJECT, cydASMOpcodeMnemonic.INDEX_OBJECT});
    public static final cydASMOpcodeMnemonic MNEMONIC_HT_ISEMPTY                          = new cydASMOpcodeMnemonic(SUB_OP_ONE_HT_ISEMPTY,                  "SUB_OP_ONE_HT_ISEMPTY",                  "ht-isempty",    new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_OBJECT});
    public static final cydASMOpcodeMnemonic MNEMONIC_HT_KEYS                             = new cydASMOpcodeMnemonic(SUB_OP_ONE_HT_KEYS,                     "SUB_OP_ONE_HT_KEYS",                     "ht-keys",       new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_OBJECT});
    public static final cydASMOpcodeMnemonic MNEMONIC_HT_PUT                              = new cydASMOpcodeMnemonic(SUB_OP_ONE_HT_PUT,                      "SUB_OP_ONE_HT_PUT",                      "ht-put",        new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_OBJECT, cydASMOpcodeMnemonic.INDEX_OBJECT, cydASMOpcodeMnemonic.INDEX_OBJECT});
    public static final cydASMOpcodeMnemonic MNEMONIC_HT_REMOVE                           = new cydASMOpcodeMnemonic(SUB_OP_ONE_HT_REMOVE,                   "SUB_OP_ONE_HT_REMOVE",                   "ht-remove",     new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_OBJECT, cydASMOpcodeMnemonic.INDEX_OBJECT});
    public static final cydASMOpcodeMnemonic MNEMONIC_HT_SIZE                             = new cydASMOpcodeMnemonic(SUB_OP_ONE_HT_SIZE,                     "SUB_OP_ONE_HT_SIZE",                     "ht-size",       new int[] {}, new int[] {cydASMOpcodeMnemonic.INDEX_OBJECT});
}
