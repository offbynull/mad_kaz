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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class cydASMProcessor {
    private Map m_parsedResourceMap;
    private Map m_parsedFunctionMap;
    private Set m_parsedExtensionSet;
    
    private Map m_positionResourceMap = new HashMap();
    private Map m_positionFunctionMap = new HashMap();
    
    private byte []m_finalData;
    
    private static final String PREFIX_FUNCTION         = "f:";
    private static final String PREFIX_LABEL            = "l:";
    private static final String PREFIX_VARIABLE         = "v:";
    private static final String PREFIX_HEX              = "hx:";
    private static final String PREFIX_BIN              = "bn:";
    
    private static final String ENTRY_POINT_NAME        = "main";
    private static final int    HEADER_SIZE             = 16;
    
    public cydASMProcessor(Map resMap, Map funcMap, Set extSet) throws IOException {
        m_parsedResourceMap = resMap;
        m_parsedFunctionMap = funcMap;
        m_parsedExtensionSet = extSet;
        
        cydByteArrayOutputStream extensionBaos = extensionDump();
        cydByteArrayOutputStream resourceBaos = resourceDump();
        cydByteArrayOutputStream functionBaos = functionDump(HEADER_SIZE + extensionBaos.size() + resourceBaos.size());
        cydByteArrayOutputStream headerBaos = headerDump(HEADER_SIZE, extensionBaos.size(), resourceBaos.size());
        
        cydByteArrayOutputStream finalDataBaos = new cydByteArrayOutputStream();
        
        System.out.println("Header start offset: " + 0);
        System.out.println("Header size offset: " + headerBaos.size());
        System.out.println("Extension start offset: " + (headerBaos.size()));
        System.out.println("Extension size offset: " + extensionBaos.size());
        System.out.println("Resource start offset: " + (headerBaos.size() + extensionBaos.size()));
        System.out.println("Resource size offset: " + resourceBaos.size());
        System.out.println("Function start offset: " + (resourceBaos.size() + headerBaos.size() + extensionBaos.size()));
        System.out.println("Function size offset: " + functionBaos.size());
        
        finalDataBaos.write(headerBaos.toByteArray());
        finalDataBaos.write(extensionBaos.toByteArray());
        finalDataBaos.write(resourceBaos.toByteArray());
        finalDataBaos.write(functionBaos.toByteArray());
        
        m_finalData = finalDataBaos.toByteArray();
    }
    
    public byte [] getFinalData() {
        return m_finalData;
    }
    
    private cydByteArrayOutputStream headerDump(int extStart, int resStart, int vmDataStart) throws IOException {
        cydByteArrayOutputStream baos = new cydByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(0xCEEEDEEE);
        dos.writeInt(extStart);       // ext start
        dos.writeInt(extStart + resStart);       // res start
        dos.writeInt(extStart + resStart + vmDataStart);    // vm data start (entry point, and then everything else)
        
        return baos;
    }
    
    private cydByteArrayOutputStream extensionDump() throws IOException {
        cydByteArrayOutputStream baos = new cydByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeByte(m_parsedExtensionSet.size());
        
        Iterator i = m_parsedExtensionSet.iterator();
        
        while (i.hasNext()) {
            Integer extId = (Integer)i.next();
            dos.writeByte(extId.intValue());
        }
        
        return baos;
    }
    
    
    private cydByteArrayOutputStream resourceDump() throws IOException {
        cydByteArrayOutputStream baos = new cydByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeShort(m_parsedResourceMap.size());
        
        Iterator i = m_parsedResourceMap.keySet().iterator();
        
        while (i.hasNext()) {
            Short resId = (Short)i.next();
            dos.writeShort(resId.shortValue());
            
            cydASMParser.Resource res = (cydASMParser.Resource)(m_parsedResourceMap.get(resId));
            byte []data = res.getData();
            
            dos.writeInt(data.length);
            dos.write(data);
        }
        
        return baos;
    }
    
    private cydByteArrayOutputStream functionDump(int startPos) throws IOException {
        cydByteArrayOutputStream baos = new cydByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(0xFFFFFFFF);       // entry point
        
        boolean entryPointFound = false;
        int nextPos = startPos + 4;
        Map functionPositions = new HashMap();   // key = label, value = position
        Map functionsUsed = new HashMap();       // key = position, value = label name
        
        Iterator i = m_parsedFunctionMap.keySet().iterator();
        
        while (i.hasNext()) {
            String functionName = (String)i.next();
            
            System.out.println("Function: " + functionName + " = " + (baos.size() + startPos) + " (" + Integer.toHexString(baos.size() + startPos) + ") ");
            
            if (functionName.equals(ENTRY_POINT_NAME)) {
                int entryPointPos = baos.size();
                baos.setPos(0);
                dos.writeInt(entryPointPos + startPos);
                baos.setPos(entryPointPos);
                
                entryPointFound = true;
                System.out.println("Entry point adjusted");
            }
            
            functionPositions.put(functionName, new Integer(baos.size() + startPos));
            
            cydASMParser.Function parsedFunction = (cydASMParser.Function)m_parsedFunctionMap.get(functionName);
            List operationsList = parsedFunction.getOperationsList();
            
            Iterator j = operationsList.iterator();
            
            Map labelPositions = new HashMap();   // key = label, value = position
            Map labelsUsed = new HashMap();       // key = position, value = label name
            
            System.out.println("Start convert");
            
            while (j.hasNext()) {
                cydASMParser.Function.FunctionOp operation = (cydASMParser.Function.FunctionOp)j.next();
                
                System.out.println(operation.getMnemonicUsed().getLongName() + " offset: " + dos.size());
                
                try {
                    
                    if (operation.getLabel() != null)
                        labelPositions.put(operation.getLabel(), new Integer(baos.size() + startPos));
                    
                    String []inputParams = operation.getInputParams();
                    cydASMOpcodeMnemonic opcode = operation.getMnemonicUsed();
                    
                    int paramCount = inputParams.length - 1;    // first element is actual opcode
                    
                    if (paramCount - opcode.getInternalExpectationsCount() < opcode.getOpcodeExpectationsCount())
                        throw new RuntimeException("Not enough parameters for external parameters");
                    
                    for (int c = 0; c < opcode.getOpcodeExpectationsCount(); c++) {
                        String param = inputParams[opcode.getInternalExpectationsCount()+c+1];
                        
                        switch (opcode.getOpcodeExpectationType(c)) {
                            case cydASMOpcodeMnemonic.INDEX_BYTE:
                            case cydASMOpcodeMnemonic.INDEX_SHORT:
                            case cydASMOpcodeMnemonic.INDEX_INT:
                            case cydASMOpcodeMnemonic.INDEX_LONG:
                            case cydASMOpcodeMnemonic.INDEX_OPERAND:
                            case cydASMOpcodeMnemonic.INDEX_OBJECT: {
                                dos.write(cydASMOpcodeList.OP_CONST_OPERAND);
                                
                                if (param.startsWith(PREFIX_BIN)) {
                                    dos.writeShort((short)convertUnsignedBinToLong(param.substring(PREFIX_BIN.length()), 16));
                                } else if (param.startsWith(PREFIX_HEX)) {
                                    dos.writeShort((short)convertUnsignedHexToLong(param.substring(PREFIX_HEX.length()), 4));
                                } else if (param.startsWith(PREFIX_VARIABLE)) {
                                    param = param.substring(PREFIX_VARIABLE.length());
                                    
                                    cydASMParser.Function.FunctionVar var = (cydASMParser.Function.FunctionVar)parsedFunction.getVariableMap().get(param);
                                    dos.writeShort(var.getStackIndex());
                                } else {
                                    dos.writeShort(Short.parseShort(param));
                                }
                                
                                nextPos += 2 + 1;
                            }
                            break;
                            default:
                                throw new RuntimeException("Unknown internal type found");
                        }
                    }
                    
                    paramCount -= opcode.getOpcodeExpectationsCount();
                    
                    if (paramCount < opcode.getInternalExpectationsCount())
                        throw new RuntimeException("Not enough parameters for internal embedded parameters");
                    
                    dos.write(opcode.getOpcode());
                    nextPos += 1;
                    
                    for (int c = 0; c < opcode.getInternalExpectationsCount(); c++) {
                        String param = inputParams[c+1];
                        
                        switch (opcode.getInternalExpectationType(c)) {
                            case cydASMOpcodeMnemonic.INTERNAL_BYTE: {
                                if (param.startsWith(PREFIX_BIN)) {
                                    dos.writeByte((byte)convertUnsignedBinToLong(param.substring(PREFIX_BIN.length()), 8));
                                } else if (param.startsWith(PREFIX_HEX)) {
                                    dos.writeByte((byte)convertUnsignedHexToLong(param.substring(PREFIX_HEX.length()), 2));
                                } else {
                                    dos.writeByte(Byte.parseByte(param));
                                }
                                
                                nextPos += 1;
                            }
                            break;
                            case cydASMOpcodeMnemonic.INTERNAL_SHORT: {
                                if (param.startsWith(PREFIX_BIN)) {
                                    dos.writeShort((short)convertUnsignedBinToLong(param.substring(PREFIX_BIN.length()), 16));
                                } else if (param.startsWith(PREFIX_HEX)) {
                                    dos.writeShort((short)convertUnsignedHexToLong(param.substring(PREFIX_HEX.length()), 4));
                                } else if (param.startsWith(PREFIX_VARIABLE)) {
                                    param = param.substring(PREFIX_VARIABLE.length());
                                    
                                    cydASMParser.Function.FunctionVar var = (cydASMParser.Function.FunctionVar)parsedFunction.getVariableMap().get(param);
                                    
                                    if (var == null)
                                        throw new RuntimeException("Variable " + param + " accessed but not found");
                                    
                                    dos.writeShort(var.getStackIndex());
                                } else {
                                    dos.writeShort(Short.parseShort(param));
                                }
                                
                                nextPos += 2;
                            }
                            break;
                            case cydASMOpcodeMnemonic.INTERNAL_INT: {
                                if (param.startsWith(PREFIX_BIN)) {
                                    dos.writeInt((int)convertUnsignedBinToLong(param.substring(PREFIX_BIN.length()), 32));
                                } else if (param.startsWith(PREFIX_HEX)) {
                                    dos.writeInt((int)convertUnsignedHexToLong(param.substring(PREFIX_HEX.length()), 8));
                                } else if (param.startsWith(PREFIX_LABEL)) {
                                    param = param.substring(PREFIX_LABEL.length());
                                    
                                    labelsUsed.put(new Integer(baos.size() + startPos), param);
                                    
                                    dos.writeInt(0xFFFFFFFF);
                                } else if (param.startsWith(PREFIX_FUNCTION)) {
                                    param = param.substring(PREFIX_FUNCTION.length());
                                    
                                    functionsUsed.put(new Integer(baos.size() + startPos), param);
                                    
                                    dos.writeInt(0xFFFFFFFF);
                                } else {
                                    dos.writeInt(Integer.parseInt(param));
                                }
                                
                                nextPos += 4;
                            }
                            break;
                            case cydASMOpcodeMnemonic.INTERNAL_LONG: {
                                if (param.startsWith(PREFIX_BIN)) {
                                    dos.writeLong((long)convertUnsignedBinToLong(param.substring(PREFIX_BIN.length()), 64));
                                } else if (param.startsWith(PREFIX_HEX)) {
                                    dos.writeLong((long)convertUnsignedHexToLong(param.substring(PREFIX_HEX.length()), 16));
                                } else {
                                    dos.writeLong(Long.parseLong(param));
                                }
                                
                                nextPos += 8;
                            }
                            break;
                            case cydASMOpcodeMnemonic.INTERNAL_STRING: {
                                int initialPos = baos.size();
                                
                                dos.writeUTF(param);
                                
                                nextPos += baos.size() - initialPos;
                            }
                            break;
                            default:
                                throw new RuntimeException("Unknown internal type found");
                        }
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                    throw new RuntimeException(t.toString() + " --- on opcode " + operation.getMnemonicUsed().getLongName());
                }
            }
            
            System.out.println("End convert");
            System.out.println("Adjusting labels for function");
            adjustLabels(startPos, baos, labelPositions, labelsUsed);
        }
        
        System.out.println("Adjusting function calls");
        adjustFunctions(startPos, baos, functionPositions, functionsUsed);
        
        if (!entryPointFound)
            throw new RuntimeException("Entry point function not found");
        
        return baos;
    }
    
    private void adjustLabels(int startPos, cydByteArrayOutputStream baos, Map labelPositionsMap, Map labelsUsedMap) throws IOException {
        DataOutputStream dos = new DataOutputStream(baos);
        
        Set keySet = labelsUsedMap.keySet();
        Iterator i = keySet.iterator();
        
        while (i.hasNext()) {
            Integer posToChange = (Integer)i.next();
            String label = (String)labelsUsedMap.get(posToChange);
            
            if (!labelPositionsMap.containsKey(label))
                throw new RuntimeException("Function for label not found: " + label);
            
            Integer posOfLabel = (Integer)labelPositionsMap.get(label);
            
            int originalBaosPos = baos.size();
            baos.setPos(posToChange - startPos);
            dos.writeInt(posOfLabel);
            baos.setPos(originalBaosPos);
        }
    }
    
    private void adjustFunctions(int startPos, cydByteArrayOutputStream baos, Map functionPositionsMap, Map functionsUsedMap) throws IOException {
        DataOutputStream dos = new DataOutputStream(baos);
        
        Set keySet = functionsUsedMap.keySet();
        Iterator i = keySet.iterator();
        
        while (i.hasNext()) {
            Integer posToChange = (Integer)i.next();
            String label = (String)functionsUsedMap.get(posToChange);
            
            if (!functionPositionsMap.containsKey(label))
                throw new RuntimeException("Function for label not found: " + label);
            
            Integer posOfFunction = (Integer)functionPositionsMap.get(label);
            
            int originalBaosPos = baos.size();
            baos.setPos(posToChange - startPos);
            dos.writeInt(posOfFunction);
            baos.setPos(originalBaosPos);
        }
    }
    
    private long convertUnsignedBinToLong(String s, int maxChars) {
        long res = 0;
        
        s = s.toLowerCase();
        
        if (s.length() > maxChars)
            throw new RuntimeException("Exceeding max number of chars for bin parse");
        
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case '0':
                    res <<= 1;
                    res |= 0x00;
                    break;
                case '1':
                    res <<= 1;
                    res |= 0x01;
                    break;
                default:
                    throw new RuntimeException("Invalid char for bin parse");
            }
        }
        
        return res;
    }
    
    private long convertUnsignedHexToLong(String s, int maxChars) {
        long res = 0;
        
        s = s.toLowerCase();
        
        if (s.length() > maxChars)
            throw new RuntimeException("Exceeding max number of chars for hex parse");
        
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case '0':
                    res <<= 4;
                    res |= 0x00;
                    break;
                case '1':
                    res <<= 4;
                    res |= 0x01;
                    break;
                case '2':
                    res <<= 4;
                    res |= 0x02;
                    break;
                case '3':
                    res <<= 4;
                    res |= 0x03;
                    break;
                case '4':
                    res <<= 4;
                    res |= 0x04;
                    break;
                case '5':
                    res <<= 4;
                    res |= 0x05;
                    break;
                case '6':
                    res <<= 4;
                    res |= 0x06;
                    break;
                case '7':
                    res <<= 4;
                    res |= 0x07;
                    break;
                case '8':
                    res <<= 4;
                    res |= 0x08;
                    break;
                case '9':
                    res <<= 4;
                    res |= 0x09;
                    break;
                case 'a':
                    res <<= 4;
                    res |= 0x0a;
                    break;
                case 'b':
                    res <<= 4;
                    res |= 0x0b;
                    break;
                case 'c':
                    res <<= 4;
                    res |= 0x0c;
                    break;
                case 'd':
                    res <<= 4;
                    res |= 0x0d;
                    break;
                case 'e':
                    res <<= 4;
                    res |= 0x0e;
                    break;
                case 'f':
                    res <<= 4;
                    res |= 0x0f;
                    break;
                default:
                    throw new RuntimeException("Invalid char for hex parse");
            }
        }
        
        return res;
    }
}
