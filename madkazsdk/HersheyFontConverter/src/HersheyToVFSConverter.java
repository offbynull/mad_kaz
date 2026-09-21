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

import java.io.ByteArrayOutputStream;

public class HersheyToVFSConverter {
    public static final int VFS_HEADER = 0x56465330;
    
    protected static final byte MODE_NORM = 0;
    protected static final byte MODE_FILL = 1;
    
    public byte[][] convert(String []input, int height) throws Throwable {
        byte [][]output = new byte[4000][];
        
        for (int i = 0; i < input.length; i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int symbolNum = Integer.parseInt(input[i].substring(0, 5).trim());
            
            int verticeCount = Integer.parseInt(input[i].substring(5, 8).trim());
            
            if (verticeCount > 255) {
                System.out.println("Too many vertices for symbol " + symbolNum + ". Skipping symbol conversion.");
                continue;
            }
            
            int leftPos = input[i].charAt(8) - 82;
            int rightPos = input[i].charAt(9) - 82;
            int charWidth = rightPos - leftPos;
            int blockCount = 0;

            ByteArrayOutputStream dataBlock = new ByteArrayOutputStream();
            ByteArrayOutputStream internalBlock = new ByteArrayOutputStream();

            for (int j = 1; j < verticeCount; j++) {
                if (input[i].charAt((j<<1)+8) == ' ' && ((j != verticeCount - 1)) && (input[i].charAt((j<<1)+9) == 'R')) {
                    byte []block = internalBlock.toByteArray();
                    dataBlock.write(HersheyToVFSConverter.MODE_NORM);
                    dataBlock.write(block.length >> 1);
                    dataBlock.write(block);
                    blockCount++;
                    internalBlock = new ByteArrayOutputStream();
                    
                    continue;
                }
                
                internalBlock.write((byte)(input[i].charAt((j<<1)+8) - 82 - leftPos));
                internalBlock.write((byte)(input[i].charAt((j<<1)+9) - 82) + (height >> 1));
            }
            
            byte []block = internalBlock.toByteArray();
            if (block.length > 0) {
                dataBlock.write(HersheyToVFSConverter.MODE_NORM);
                dataBlock.write(block.length >> 1);
                dataBlock.write(block);
                blockCount++;
            }
            
            baos.write(blockCount);
            baos.write(charWidth);
            baos.write(dataBlock.toByteArray());
            
            output[symbolNum] = baos.toByteArray();
        }
        
        return output;
    }
}
