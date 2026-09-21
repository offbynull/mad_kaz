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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class cydASMParser {
    private static final int STATE_NORM                                 = 0;
    private static final int STATE_RESOURCE                             = 1;
    private static final int STATE_FUNC_ARGS                            = 2;
    private static final int STATE_FUNC_VARS                            = 3;
    private static final int STATE_FUNC_CONTENTS                        = 4;
    
    private static final String DIRECTIVE_BEGIN_RESOURCES               = "beginreses";
    private static final String DIRECTIVE_END_RESOURCES                 = "endreses";
    private static final String DIRECTIVE_RES_DUMP                      = "resdump";
    private static final String DIRECTIVE_RES_STRING                    = "resstring";
    private static final String DIRECTIVE_RES_OUT                       = "resout";
    private static final String DIRECTIVE_BEGIN_FUNC                    = "beginfunc";
    private static final String DIRECTIVE_END_VARS                      = "endvars";
    private static final String DIRECTIVE_END_ARGS                      = "endargs";
    private static final String DIRECTIVE_END_FUNC                      = "endfunc";
    private static final String DIRECTIVE_EXTENSION_REQUIRED            = "extreq";
    private static final String DIRECTIVE_VAR                           = "var";
    private static final String DIRECTIVE_ARG                           = "arg";
    private static final String DIRECTIVE_LABEL                         = "label";
    
    private static final String TYPE_BYTE                                = "byte";
    private static final String TYPE_SHORT                               = "short";
    private static final String TYPE_INT                                 = "int";
    private static final String TYPE_LONG                                = "long";
    private static final String TYPE_STRING                              = "string";
    
    private static final char LINE_PORTION_COMMENT_DELIM                = '#';
    
    private HashMap m_resourceMap;
    private HashMap m_functionMap;
    private HashSet m_extensionSet;
    
    public cydASMParser() {
        reset();
    }
    
    public void reset() {
        m_resourceMap = new HashMap();
        m_functionMap = new HashMap();
        m_extensionSet = new HashSet();
    }
    
    private boolean findAndRemoveDirective(String []s) {
        if (s.length == 0)
            return false;
        
        if (s[0].isEmpty())
            return false;
        
        boolean res = s[0].charAt(0) == '.';
        
        if (res)
            s[0] = s[0].substring(1);
        
        return res;
    }
    
    private List readString(String s, int startAtIndex) {
        List result = new ArrayList();
        String res = "";
        int offset = startAtIndex + 1;
        
        try {
            while (true) {
                char c = s.charAt(offset++);
                
                if (c == '\\') {
                    char escapeMode = s.charAt(offset++);
                    
                    switch (escapeMode) {
                        case 'n':
                            res += '\n';
                            break;
                        case 't':
                            res += '\t';
                            break;
                        case 'b':
                            res += '\b';
                            break;
                        case 'f':
                            res += '\f';
                            break;
                        case 'r':
                            res += '\r';
                            break;
                        case '\"':
                            res += '\"';
                            break;
                        case '\'':
                            res += '\'';
                            break;
                        case '\\':
                            res += '\\';
                            break;
                        case 'u': {
                            String hex = "";
                            
                            hex += s.charAt(offset++);
                            hex += s.charAt(offset++);
                            hex += s.charAt(offset++);
                            hex += s.charAt(offset++);
                            
                            res += (char)Integer.parseInt(hex, 16);
                        }
                        break;
                    }
                } else {
                    if (c == '\"') {
                        result.add(res);
                        result.add(new Integer(offset - startAtIndex - 2));
                        return result;
                    }
                    
                    res += c;
                }
            }
        } catch (IndexOutOfBoundsException ioobe) {
            throw new RuntimeException("Unexpected string termination");
        }
    }
    
    private String []breakString(String s) {
        List stringList = new ArrayList();
        String currentString = "";
        
        for (int i = 0; i < s.length(); i++) {
            if (Character.isWhitespace(s.charAt(i))) {
                if (!currentString.isEmpty()) {
                    stringList.add(currentString);
                    currentString = "";
                }
            } else if (s.charAt(i) == '\"'  && currentString.isEmpty()) {
                List res = readString(s, i);
                stringList.add(res.get(0));
                i += ((Integer)(res.get(1))).intValue() + 1;
            } else {
                if (s.charAt(i) == LINE_PORTION_COMMENT_DELIM && currentString.isEmpty())
                    break;
                
                currentString += s.charAt(i);
            }
        }
        
        if (!currentString.isEmpty())
            stringList.add(currentString);
        
        return (String[])stringList.toArray(new String[stringList.size()]);
    }
    
    private static byte[] getBytesFromFile(File file) {
        try {
            InputStream is = new FileInputStream(file);

            long length = file.length();

            // Create the byte array to hold the data
            byte[] bytes = new byte[(int)length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                   && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }

            if (offset < bytes.length) {
                throw new RuntimeException("Could not completely read file "+file.getName());
            }

            is.close();
            return bytes;
        } catch (Exception e) { }
        
        throw new RuntimeException("Failed to read file " + file.getAbsolutePath());
    } 
    
    public void parse(List stringList) {
        int state = STATE_NORM;
        
        Iterator i = stringList.iterator();
        Function currentFunc = null;
        String labelForNextOp = null;
        
        cydASMOpcodeSystem opcodeSystem = new cydASMOpcodeSystem();
        
        while (i.hasNext()) {
            String cmdString = (String)i.next();
            
            cmdString = cmdString.trim();
            String []elements = breakString(cmdString);
            
            boolean directive = findAndRemoveDirective(elements);
            
            if (elements.length == 0)
                continue;
            
            switch (state) {
                case STATE_NORM: {
                    if (!directive) {
                        throw new RuntimeException("Expecting assembler directive");
                    } else {
                        if (elements[0].compareTo(DIRECTIVE_BEGIN_RESOURCES) == 0) {
                            if (elements.length != 1)
                                throw new RuntimeException("Unexpected number of parameters for begin resources");
                            
                            state = STATE_RESOURCE;
                        } else if (elements[0].compareTo(DIRECTIVE_BEGIN_FUNC) == 0) {
                            if (elements.length != 2 && elements.length != 8)
                                throw new RuntimeException("Unexpected number of parameters for begin function");
                            
                            if (m_functionMap.containsKey(elements[1]))
                                throw new RuntimeException("Function name already used (" + elements[1] + ")");
                            
                            if (elements.length == 8)
                                currentFunc = new Function(Short.parseShort(elements[2]), Short.parseShort(elements[3]), Short.parseShort(elements[4]), Short.parseShort(elements[5]), Short.parseShort(elements[6]), Short.parseShort(elements[7]));
                            else if (elements.length == 2)
                                currentFunc = new Function((short)0, (short)0, (short)0, (short)0, (short)0, (short)0);
                            
                            m_functionMap.put(elements[1], currentFunc);
                            
                            state = STATE_FUNC_ARGS;
                        } else if (elements[0].compareTo(DIRECTIVE_EXTENSION_REQUIRED) == 0) {
                            if (elements.length != 2)
                                throw new RuntimeException("Unexpected number of parameters for extension required");

                            try {
                                int extReq = Integer.parseInt(elements[1]);
                                
                                if (extReq < 0 || extReq > 255)
                                    throw new RuntimeException("Invalid extension id used");
                                
                                m_extensionSet.add(new Integer(extReq));
                            } catch (NumberFormatException nfe) {
                                throw new RuntimeException("Numeric extension id required");
                            }
                        } else {
                            throw new RuntimeException("Unexpected or unknown directive: " + elements[0]);
                        }
                    }
                }
                break;
                case STATE_RESOURCE: {
                    if (directive) {
                        if (elements[0].compareTo(DIRECTIVE_END_RESOURCES) == 0) {
                            if (elements.length != 1)
                                throw new RuntimeException("Unexpected number of parameters for end resources");
                            
                            state = STATE_NORM;
                        } else if (elements[0].compareTo(DIRECTIVE_RES_DUMP) == 0) {
                            if (elements.length != 3)
                                throw new RuntimeException("Unexpected number of parameters for resource dump");
                            
                            short id = 0;
                            try { id = Short.parseShort(elements[1]); } catch (NumberFormatException nfe) { throw new RuntimeException("Number needed as id for resource"); }
                            String filename = elements[2];
                            
                            if (m_resourceMap.containsKey(new Short(id)))
                                throw new RuntimeException("Resource id " + id + " is already taken;");
                            
                            byte []data = getBytesFromFile(new File(filename));
                            
                            m_resourceMap.put(new Short(id), new Resource(data));
                        } else if (elements[0].compareTo(DIRECTIVE_RES_OUT) == 0) {
                            if (elements.length != 3)
                                throw new RuntimeException("Unexpected number of parameters for resource out");
                            
                            short id = 0;
                            try { id = Short.parseShort(elements[1]); } catch (NumberFormatException nfe) { throw new RuntimeException("Number needed as id for resource"); }
                            String out = elements[2];

                            if (m_resourceMap.containsKey(new Short(id)))
                                throw new RuntimeException("Resource id " + id + " is already taken;");
                            
                            if (out.length() % 2 != 0)
                                throw new RuntimeException("Incorrect number of output characters");
                            
                            int length = out.length()/2;
                            byte []data = new byte[length];
                            
                            for (int j = 0; j < length; j++) {
                                String byteString = out.substring(j*2, (j+1)*2);
                                
                                data[j] = (byte)Integer.parseInt(byteString, 16);
                            }
                            
                            m_resourceMap.put(new Short(id), new Resource(data));
                        } else if (elements[0].compareTo(DIRECTIVE_RES_STRING) == 0) {
                            if (elements.length != 3)
                                throw new RuntimeException("Unexpected number of parameters for resource string");
                            
                            short id = 0;
                            try { id = Short.parseShort(elements[1]); } catch (NumberFormatException nfe) { throw new RuntimeException("Number needed as id for resource"); }
                            String string = elements[2];
                            
                            if (m_resourceMap.containsKey(new Short(id)))
                                throw new RuntimeException("Resource id " + id + " is already taken");
                            
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            DataOutputStream dos = new DataOutputStream(baos);
                            
                            try {
                                dos.writeUTF(string);
                                dos.flush();
                            } catch (Exception e) {
                                throw new RuntimeException("Error converting string to UTF-8 style");
                            }
                            
                            byte []data = baos.toByteArray();
                            m_resourceMap.put(new Short(id), new Resource(data));
                        } else {
                            throw new RuntimeException("Unexpected or unknown directive");
                        }
                    } else {
                        throw new RuntimeException("Expecting assembler directive");
                    }
                }
                break;
                case STATE_FUNC_ARGS: {
                    if (directive) {
                        if (elements[0].compareTo(DIRECTIVE_END_ARGS) == 0) {
                            if (elements.length != 1)
                                throw new RuntimeException("Unexpected number of parameters for end arguements");
                            
                            state = STATE_FUNC_VARS;
                        } else if (elements[0].compareTo(DIRECTIVE_ARG) == 0) {
                            if (elements.length != 3)
                                throw new RuntimeException("Unexpected number of parameters for arguement");
                            
                            if (elements[2].compareTo(TYPE_BYTE) == 0) {
                                currentFunc.putVariable(elements[1], currentFunc.new FunctionVar(Function.FunctionVar.VAR_TYPE_BYTE, elements[2]));
                            } else if (elements[2].compareTo(TYPE_SHORT) == 0) {
                                currentFunc.putVariable(elements[1], currentFunc.new FunctionVar(Function.FunctionVar.VAR_TYPE_SHORT, elements[2]));
                            } else if (elements[2].compareTo(TYPE_INT) == 0) {
                                currentFunc.putVariable(elements[1], currentFunc.new FunctionVar(Function.FunctionVar.VAR_TYPE_INT, elements[2]));
                            } else if (elements[2].compareTo(TYPE_LONG) == 0) {
                                currentFunc.putVariable(elements[1], currentFunc.new FunctionVar(Function.FunctionVar.VAR_TYPE_LONG, elements[2]));
                            } else if (elements[2].compareTo(TYPE_STRING) == 0) {
                                currentFunc.putVariable(elements[1], currentFunc.new FunctionVar(Function.FunctionVar.VAR_TYPE_STRING, elements[2]));
                            }
                        } else {
                            throw new RuntimeException("Unexpected or unknown directive");
                        }
                    } else {
                        throw new RuntimeException("Expecting assembler directive");
                    }
                }
                break;
                case STATE_FUNC_VARS: {
                    if (directive) {
                        if (elements[0].compareTo(DIRECTIVE_END_VARS) == 0) {
                            if (elements.length != 1)
                                throw new RuntimeException("Unexpected number of parameters for end variables");
                            
                            state = STATE_FUNC_CONTENTS;
                        } else if (elements[0].compareTo(DIRECTIVE_VAR) == 0) {
                            if (elements.length != 4)
                                throw new RuntimeException("Unexpected number of parameters for variable");
                            
                            if (elements[2].compareTo(TYPE_BYTE) == 0) {
                                currentFunc.putVariable(elements[1], currentFunc.new FunctionVar(Function.FunctionVar.VAR_TYPE_BYTE, elements[3]));
                                currentFunc.putOperation(null, opcodeSystem.getMnemonic("OP_CONST_BYTE"), new String [] {"OP_CONST_BYTE", elements[3]});
                            } else if (elements[2].compareTo(TYPE_SHORT) == 0) {
                                currentFunc.putVariable(elements[1], currentFunc.new FunctionVar(Function.FunctionVar.VAR_TYPE_SHORT, elements[3]));
                                currentFunc.putOperation(null, opcodeSystem.getMnemonic("OP_CONST_SHORT"), new String [] {"OP_CONST_SHORT", elements[3]});
                            } else if (elements[2].compareTo(TYPE_INT) == 0) {
                                currentFunc.putVariable(elements[1], currentFunc.new FunctionVar(Function.FunctionVar.VAR_TYPE_INT, elements[3]));
                                currentFunc.putOperation(null, opcodeSystem.getMnemonic("OP_CONST_INT"), new String [] {"OP_CONST_INT", elements[3]});
                            } else if (elements[2].compareTo(TYPE_LONG) == 0) {
                                currentFunc.putVariable(elements[1], currentFunc.new FunctionVar(Function.FunctionVar.VAR_TYPE_LONG, elements[3]));
                                currentFunc.putOperation(null, opcodeSystem.getMnemonic("OP_CONST_LONG"), new String [] {"OP_CONST_LONG", elements[3]});
                            } else if (elements[2].compareTo(TYPE_STRING) == 0) {
                                currentFunc.putVariable(elements[1], currentFunc.new FunctionVar(Function.FunctionVar.VAR_TYPE_STRING, elements[3]));
                                currentFunc.putOperation(null, opcodeSystem.getMnemonic("OP_CONST_STRING"), new String [] {"OP_CONST_STRING", elements[3]});
                            } else {
                                throw new RuntimeException("Unknown var type " + elements[2]);
                            }
                        } else {
                            throw new RuntimeException("Unexpected or unknown directive");
                        }
                    } else {
                        throw new RuntimeException("Expecting assembler directive");
                    }
                }
                break;
                case STATE_FUNC_CONTENTS: {
                    if (directive) {
                        if (elements[0].compareTo(DIRECTIVE_END_FUNC) == 0) {
                            if (elements.length != 1)
                                throw new RuntimeException("Unexpected number of parameters for end function");
                            
                            if (labelForNextOp != null)
                                throw new RuntimeException("No operation for label found " + labelForNextOp);
                            
                            currentFunc = null;
                            
                            state = STATE_NORM;
                        } else if (elements[0].compareTo(DIRECTIVE_LABEL) == 0) {
                            if (elements.length != 2)
                                throw new RuntimeException("Unexpected number of parameters for label");
                            
                            if (labelForNextOp != null)
                                throw new RuntimeException("Cannot have more than 1 label for same line");
                            
                            labelForNextOp = elements[1];
                        } else {
                            throw new RuntimeException("Unexpected or unknown directive -- " + elements[0]);
                        }
                    } else {
                        if (elements.length < 1)
                            throw new RuntimeException("Unexpected number of parameters for opcode");
                        
                        cydASMOpcodeMnemonic mnemonic = opcodeSystem.getMnemonic(elements[0]);
                        
                        if (mnemonic == null)
                            throw new RuntimeException("Opcode " + elements[0] + " not found");
                        
                        currentFunc.putOperation(labelForNextOp, mnemonic, elements);
                        
                        if (labelForNextOp != null)
                            labelForNextOp = null;
                    }
                }
                break;
            }
        }
    }
    
    public class Extension {
        private int m_extensionId;
        
        public Extension(int id) {
            m_extensionId = id;
            
            if (m_extensionId > 255 || m_extensionId < 0)
                throw new RuntimeException("Extension id has to be between 0 to 255");
        }
        
        public int getExtensionId() {
            return m_extensionId;
        }
        
        public boolean equals(Object o) {
            if (o instanceof Extension)
                return false;
            
            Extension other = (Extension)o;
            
            return other.m_extensionId == m_extensionId;
        }
    }
    
    public class Resource {
        private byte []m_data;

        public Resource(byte []data) {
            m_data = data;
        }
        
        public byte[] getData() {
            return m_data;
        }
    }
    
    public class Function {
        private short m_startByteIndex = 0;
        private short m_startShortIndex = 0;
        private short m_startIntIndex = 0;
        private short m_startLongIndex = 0;
        private short m_startObjectIndex = 0;
        
        private Map m_variableMap = new HashMap();
        private Map m_labelMap = new HashMap();
        private List m_operationsList = new ArrayList();
        
        public Function(short byteIndex, short shortIndex, short intIndex, short longIndex, short objectIndex, short operandIndex) {
            m_startByteIndex = byteIndex;
            m_startShortIndex = shortIndex;
            m_startIntIndex = intIndex;
            m_startLongIndex = longIndex;
            m_startObjectIndex = objectIndex;
            // operand index not used
        }
        
        public Map getVariableMap() {
            return m_variableMap;
        }
        
        public boolean doesVariableExists(String name) {
            return m_variableMap.containsKey(name);
        }
        
        public void putVariable(String name, FunctionVar var) {
            if (m_variableMap.containsKey(name))
                throw new RuntimeException("Variable already exists");
            
            m_variableMap.put(name, var);
        }
        
        public Map getLabelMap() {
            return m_labelMap;
        }
        
        public boolean doesLabelExists(String name) {
            return m_labelMap.containsKey(name);
        }
        
        public void putLabel(String name, FunctionOp op) {
            if (m_labelMap.containsKey(name))
                throw new RuntimeException("Label already exists");
            
            m_labelMap.put(name, op);
        }
        
        public List getOperationsList() {
            return m_operationsList;
        }
        
        public void putOperation(String label, cydASMOpcodeMnemonic mnemonic, String []params) {
            FunctionOp op = new FunctionOp(mnemonic, params, label);
            
            m_operationsList.add(op);
            
            if (label != null)
                putLabel(label, op);
        }
        
        public int getLine(FunctionOp op) {
            int i = m_operationsList.indexOf(op);
            
            if (i == -1)
                throw new RuntimeException("Operation doesn't exist in function");
            
            return i;
        }
        
        public FunctionOp getOperation(String label) {
            if (m_labelMap.containsKey(label))
                throw new RuntimeException("Label doesn't exist");
            
            return (FunctionOp)m_labelMap.get(label);
        }
        
        public class FunctionOp {
            private cydASMOpcodeMnemonic m_mnemonicUsed;
            private String []m_inputParams;
            private String m_label;
            
            public FunctionOp(cydASMOpcodeMnemonic mnemonic, String []params, String label) {
                m_mnemonicUsed = mnemonic;
                m_inputParams = params;
                m_label = label;
                
                int paramCount = params.length - 1;    // first element is actual opcode
                
                if (paramCount == m_mnemonicUsed.getInternalExpectationsCount())
                    return;
                else if (paramCount == m_mnemonicUsed.getInternalExpectationsCount() + m_mnemonicUsed.getOpcodeExpectationsCount())
                    return;
                else
                    throw new RuntimeException("Incorrect number of parameters entered " + mnemonic.getLongName());
            }

            public cydASMOpcodeMnemonic getMnemonicUsed() {
                return m_mnemonicUsed;
            }

            public String[] getInputParams() {
                return m_inputParams;
            }
            
            public String getLabel() {
                return m_label;
            }
        }
        
        public class FunctionVar {
            public static final int VAR_TYPE_BYTE       = 0;
            public static final int VAR_TYPE_SHORT      = 1;
            public static final int VAR_TYPE_INT        = 2;
            public static final int VAR_TYPE_LONG       = 3;
            public static final int VAR_TYPE_STRING     = 4;
            
            private int m_varType;
            private short m_stackIndex;
            private String m_initialValue;
            
            public FunctionVar(int type, String initialValue) {
                m_varType = type;
                m_initialValue = initialValue;
                
                if (initialValue == null)
                    throw new RuntimeException("Null initial variable value");
                
                switch (m_varType) {
                    case VAR_TYPE_BYTE:
                        m_stackIndex = m_startByteIndex++;
                        //try { Byte.parseByte(m_initialValue); } catch (NumberFormatException nfe) { throw new RuntimeException("Error parsing initial variable value"); }
                        break;
                    case VAR_TYPE_SHORT:
                        m_stackIndex = m_startShortIndex++;
                        //try { Short.parseShort(m_initialValue); } catch (NumberFormatException nfe) { throw new RuntimeException("Error parsing initial variable value"); }
                        break;
                    case VAR_TYPE_INT:
                        m_stackIndex = m_startIntIndex++;
                        //try { Integer.parseInt(m_initialValue); } catch (NumberFormatException nfe) { throw new RuntimeException("Error parsing initial variable value"); }
                        break;
                    case VAR_TYPE_LONG:
                        m_stackIndex = m_startLongIndex++;
                        //try { Long.parseLong(m_initialValue); } catch (NumberFormatException nfe) { throw new RuntimeException("Error parsing initial variable value"); }
                        break;
                    case VAR_TYPE_STRING:
                        m_stackIndex = m_startObjectIndex++;
                        break;
                    default:
                        throw new RuntimeException("Unknown var type passed as function variable");
                }
            }

            public int getVarType() {
                return m_varType;
            }

            public short getStackIndex() {
                return m_stackIndex;
            }
        }
    }

    public Map getResourceMap() {
        return m_resourceMap;
    }

    public Map getFuncMap() {
        return m_functionMap;
    }

    public Set getExtensionSet() {
        return m_extensionSet;
    }
}
