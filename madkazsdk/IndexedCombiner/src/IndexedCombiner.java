/*
    This file is part of Mad Kaz Indexed Combiner.

    Mad Kaz Indexed Combiner is free software; you can redistribute it and/or 
    modify it under the terms of the GNU General Public License as published by 
    the Free Software Foundation; either version 3 of the License, or (at your 
    option) any later version.

    Mad Kaz Indexed Combiner is distributed in the hope that it will be useful, 
    but WITHOUT ANY WARRANTY; without even the implied warranty of 
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
    Public License for more details.

    You should have received a copy of the GNU General Public License along 
    with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IndexedCombiner {
    public static byte []process(LineNumberReader reader) throws Throwable {
        List fileList = getFileList(reader);
        List dataList = getData(fileList);
        byte []headerData = getHeaderData(dataList);
        byte []fullData = getFullData(dataList);
        
        return getFinalData(headerData, fullData);
    }
    
    private static byte[] getFinalData(byte[] headerData, byte[] fullData) throws Throwable {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(headerData.length);
        dos.write(headerData);
        dos.write(fullData);
        
        return baos.toByteArray();
    }

    private static byte []getHeaderData(List dataList) throws Throwable {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        Iterator dataListIt = dataList.iterator();
        
        dos.writeInt(dataList.size());
        
        int offset = 0;
        
        while (dataListIt.hasNext()) {
            List byteArrayList = (List)dataListIt.next();
            Iterator byteArrayListIt = byteArrayList.iterator();
            
            dos.writeInt(byteArrayList.size());
            
            while (byteArrayListIt.hasNext()) {
                byte []data = (byte [])byteArrayListIt.next();
                dos.writeInt(offset);
                dos.writeInt(data.length);
                
                offset += data.length;
            }
        }
        
        return baos.toByteArray();
    }
    
    private static byte []getFullData(List dataList) throws Throwable {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        Iterator dataListIt = dataList.iterator();
        
        while (dataListIt.hasNext()) {
            List byteArrayList = (List)dataListIt.next();
            Iterator byteArrayListIt = byteArrayList.iterator();
            
            while (byteArrayListIt.hasNext()) {
                byte []data = (byte [])byteArrayListIt.next();
                dos.write(data);
            }
        }
        
        return baos.toByteArray();
    }
    
    private static List getData(List fileList) throws Throwable {
        List dataList = new ArrayList();
        Iterator fileListIt = fileList.iterator();
        
        while (fileListIt.hasNext()) {
            List innerDataList = new ArrayList();
            
            List innerFileList = (List)fileListIt.next();
            Iterator innerFileIt = innerFileList.iterator();
            
            while (innerFileIt.hasNext()) {
                File file = (File)innerFileIt.next();
                
                int size = (int)file.length();
                
                FileInputStream fis = new FileInputStream(file);
                
                byte []dataOutput = new byte[size];
                fis.read(dataOutput);
                fis.close();
                
                innerDataList.add(dataOutput);
            }
            
            dataList.add(innerDataList);
        }
        
        return dataList;
    }
    
    private static List getFileList(LineNumberReader reader) throws Throwable {
        String line = null;
        
        ArrayList mainList = new ArrayList();
        ArrayList currentInnerList = new ArrayList();
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            if (line.length() == 0) {
                mainList.add(currentInnerList);
                currentInnerList = new ArrayList();
            } else {
                currentInnerList.add(new File(line));
            }
        }
        
        if (currentInnerList.size() > 0)
            mainList.add(currentInnerList);
        
        return mainList;
    }
    
    public static void main(String[] args) throws Throwable {
        if (args.length != 2) {
            System.out.println("usage: java -jar IndexedCombiner filelist.txt output.dat");
            System.exit(1);
        }
        
        LineNumberReader reader = new LineNumberReader(new FileReader(args[0]));
        
        IndexedCombiner combiner = new IndexedCombiner();
        byte []output = combiner.process(reader);
        
        FileOutputStream fos = new FileOutputStream(args[1]);
        fos.write(output);
        fos.close();
        
        reader.close();
    }
}
