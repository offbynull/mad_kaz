/*
    Hershey To VFS Converter   Copyright 2007 CodeYield Development, Inc.  inquiries@codeyielddevelopment.com
 
    This file is part of Hershey To VFS Converter.
 
    Hershey To VFS Converter is free software; you can redistribute it and/or 
    modify it under the terms of the GNU General Public License as published by 
    the Free Software Foundation; either version 3 of the License, or (at your 
    option) any later version.

    Hershey To VFS Converter is distributed in the hope that it will be useful, 
    but WITHOUT ANY WARRANTY; without even the implied warranty of 
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
    Public License for more details.

    You should have received a copy of the GNU General Public License along 
    with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws Throwable {
        if (args.length < 4) {
            System.out.println("Conv Ex: java -jar cydHersheyFontConverter CONV coordMapFile asciiConversionMap.hmp 10 output.vfs");
            System.out.println("Resize Ex (size in FP8/8): java -jar cydHersheyFontConverter RESIZE 000000a0 10 input.vfs output.vfs");
            return;
        }
        
        if (args[0].equals("CONV")) {
            System.out.println("Reading hershey data...");
            String []fontData = readHersheyData(args[1]);
            System.out.println("Reading font mapping...");
            int []fontMap = readFontMapData(args[2]);
            System.out.println("Converting hershey data to VFS data...");
            byte [][]fontEngineData = new HersheyToVFSConverter().convert(fontData, Integer.parseInt(args[3]));
            System.out.println("Creating VFS file...");
            writeVFSFile(args[3], fontEngineData, fontMap, Integer.parseInt(args[3]));
        } else if (args[0].equals("RESIZE")) {
            int fp88Ratio = (int)Long.parseLong(args[1], 16);
            
            System.out.println("Reading VFS data...");
            byte [][]fontEngineData = readVFSFile(args[3]);
            
            System.out.println("Resizing VFS data");
            fontEngineData = resize(fontEngineData, fp88Ratio);
            
            int []fakeMapData = new int [fontEngineData.length];
            
            for (int i = 0; i < fakeMapData.length; i++)
                fakeMapData[i] = i;
            
            writeVFSFile(args[4], fontEngineData, fakeMapData, Integer.parseInt(args[2]));
        }
    }
    
    public static byte [][]resize(byte [][]fontEngineData, int fp88Ratio) throws Throwable {
        ArrayList binData = new ArrayList();
        
        for (int i = 0; i < fontEngineData.length; i++) {
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            
            int blockCount = fontEngineData[i][0];
            int charWidth = fontEngineData[i][1];
            
            dos.write(blockCount);
            dos.write(((charWidth<<8)*fp88Ratio) >> 16);
            
            int counter = 2;
            
            for (int j = 0; j < blockCount; j++) {
                ByteArrayOutputStream blockBaos = new ByteArrayOutputStream();
                
                int mode = fontEngineData[i][counter++];
                int vectorCount = fontEngineData[i][counter++];

                int oldX = fontEngineData[i][counter++];
                int oldY = fontEngineData[i][counter++];
                
                blockBaos.write(((oldX<<8)*fp88Ratio) >> 16);
                blockBaos.write(((oldY<<8)*fp88Ratio) >> 16);
                
                int newVectorCount = 1;
                
                for (int k = 1; k < vectorCount; k++) {
                    int x = ((fontEngineData[i][counter++]<<8)*fp88Ratio) >> 16;
                    int y = ((fontEngineData[i][counter++]<<8)*fp88Ratio) >> 16;
                    
                    if (oldX == x && oldY == y)
                        continue;
                    
                    blockBaos.write(x);
                    blockBaos.write(y);
                    
                    oldX = x;
                    oldY = y;
                    
                    newVectorCount++;
                }
                
                if (newVectorCount % 2 != 0) {
                    blockBaos.write(oldX);
                    blockBaos.write(oldY);
                    
                    newVectorCount++;
                }
                
                dos.write(mode);
                dos.write(newVectorCount);
                dos.write(blockBaos.toByteArray());
            }
            
            binData.add(baos.toByteArray());
        }
        
        return (byte [][])binData.toArray(new byte[binData.size()][]);
    }
    
    public static byte [][]readVFSFile(String filename) throws Throwable {
        byte [][]ret = null;
        
        try {
            FileInputStream fis = new FileInputStream(filename);
            DataInputStream dis = new DataInputStream(fis);
            
            if (dis.readInt() != HersheyToVFSConverter.VFS_HEADER)
                throw new RuntimeException("Incorrect file header");
            
            int len = dis.readInt();
            int height = dis.readInt();
            
            ArrayList binData = new ArrayList();

            {
                int counter = 12;
                int symbolCounter = 0;
                int total = (int)new File(filename).length();

                while (counter != total) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    symbolCounter++;

                    int blockCount = dis.readByte();
                    int charWidth = dis.readByte();
                    
                    counter += 2;
                    
                    int dataLength = 0;
                    
                    baos.write(blockCount);
                    baos.write(charWidth);

                    for (int i = 0; i < blockCount; i++) {
                        byte mode = dis.readByte();
                        byte vectorCount = dis.readByte();
                        
                        counter += 2;

                        baos.write(mode);
                        baos.write(vectorCount);
                        
                        byte []buf = new byte[vectorCount << 1];
                        dis.read(buf);
                        baos.write(buf);
                        
                        counter += vectorCount << 1;
                    }
                    
                    binData.add(baos.toByteArray());
                }
            }
            
            dis.close();
            
            ret = (byte [][])binData.toArray(new byte[binData.size()][]);
        } catch (Exception e) {
            System.out.println("Error reading VFS file " + filename);
            e.printStackTrace();
            System.exit(1);
        }
        
        return ret;
    }
    
    public static void writeVFSFile(String filename, byte[][]binData, int[]map, int height) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            DataOutputStream dos = new DataOutputStream(fos);
            
            dos.writeInt(HersheyToVFSConverter.VFS_HEADER);
            
            int len = 0;
            for (int i = 0; i < map.length; i++)
                len += binData[map[i]].length;
            
            dos.writeInt(len);
            dos.writeInt(height);
            
            for (int i = 0; i < map.length; i++)
                dos.write(binData[map[i]]);
            
            dos.close();
        } catch (Exception e) {
            System.out.println("Error writing VFS file " + filename);
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static String[] readHersheyData(String filename) {
        List output = new ArrayList();
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String str;
            
            while ((str = in.readLine()) != null)
                output.add(str);
            
            in.close();
        } catch (IOException e) {
            System.out.println("Failed to read " + filename);
            System.exit(1);
        }
        
        return (String[])output.toArray(new String[output.size()]);
    }
    
    public static int[] readFontMapData(String filename) {
        List numberList = new ArrayList();
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String str;
            
            while ((str = in.readLine()) != null) {
                StringTokenizer strtok = new StringTokenizer(str, " ");
                
                while (strtok.hasMoreTokens())
                    numberList.add(strtok.nextToken());
            }
            
            in.close();
        } catch (IOException e) {
            System.out.println("Failed to read " + filename);
            System.exit(1);
        }
        
        
        int elementCount = 0;
        int []fontMap = null;
        Iterator i = null;
        
        // calculate number of elements needed
        i = numberList.iterator();
        
        while (i.hasNext()) {
            StringTokenizer strtok = new StringTokenizer((String)i.next(), "-");
            
            int tokenCount = strtok.countTokens(); 
            if (tokenCount == 1) {
                elementCount++;
            } else if (tokenCount == 2) {
                int startNum = Integer.parseInt(strtok.nextToken());
                int endNum = Integer.parseInt(strtok.nextToken());
                
                elementCount += endNum - startNum + 1;
            } else {
                System.out.println("Error parsing map file");
                System.exit(1);                
            }
        }
        
        // populate map
        fontMap = new int[elementCount];
        i = numberList.iterator();
        int elementCounter = 0;
        
        while (i.hasNext()) {
            StringTokenizer strtok = new StringTokenizer((String)i.next(), "-");
            
            int tokenCount = strtok.countTokens(); 
            if (tokenCount == 1) {
                fontMap[elementCounter++] = Integer.parseInt(strtok.nextToken());
            } else if (tokenCount == 2) {
                int startNum = Integer.parseInt(strtok.nextToken());
                int endNum = Integer.parseInt(strtok.nextToken());
                
                int count = endNum - startNum;
                
                for (int j = 0; j <= count; j++)
                    fontMap[elementCounter + j] = startNum + j;
                
                elementCounter += count + 1;
            } else {
                System.out.println("Error parsing map file");
                System.exit(1);                
            }
        }
        
        return fontMap;
    }
}
