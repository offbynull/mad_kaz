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

package game;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Hashtable;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStoreException;

public class cydLevelStorageSystem implements RecordComparator {
    public static final String STORAGE_NAME                 = "dlevs";
    public static final int LEVEL_MAGIC_NUM                 = 0xDECADE69;
    
    
    public static final int COMPARE_MODE_SHORT_NAME         = 0;
    public static final int COMPARE_MODE_STORED_TIME        = 1;
    
    
    public static final String KEY_CREATED_TIME             = "CREATED_TIME";
    public static final String KEY_STORED_TIME              = "STORED_TIME";
    public static final String KEY_SHORT_NAME               = "SHORT_NAME";
    public static final String KEY_LONG_NAME                = "LONG_NAME";
    public static final String KEY_AUTHOR_NAME              = "AUTHOR_NAME";
    public static final String KEY_COMMENTS                 = "COMMENTS";
    public static final String KEY_VM_DATA                  = "VM_DATA";
    
    
    public int m_compMode = cydLevelStorageSystem.COMPARE_MODE_SHORT_NAME;
    
    public int []m_recordIDs;
    public char [][]m_shortNames;
    public RecordStore m_recordStore;
    
    public cydLevelStorageSystem() {
    }
    
    public boolean open() {
        try {
            m_recordStore = RecordStore.openRecordStore(cydLevelStorageSystem.STORAGE_NAME, true);
        } catch (Throwable t) {
            return false;
        }
        
        return true;
    }
    
    public void close() {
        try {
            m_recordStore.closeRecordStore();
        } catch (Throwable t) { }
    }
    
    public boolean loadHeaderData(){
        try {
            RecordEnumeration recEnum = m_recordStore.enumerateRecords(null, this, false);

            int numberOfItems = recEnum.numRecords();
            m_recordIDs = new int[numberOfItems];
            m_shortNames = new char[numberOfItems][];

            int recCounter = 0;
            while (recEnum.hasNextElement()) {
                m_recordIDs[recCounter] = recEnum.nextRecordId();
                recCounter++;
            }
            
            recEnum.destroy();
            
            
            
            for (int i = 0; i < numberOfItems; i++)
                m_shortNames[i] = ((String)levelEntryToHash(m_recordStore.getRecord(m_recordIDs[i])).get(cydLevelStorageSystem.KEY_SHORT_NAME)).toCharArray();
        } catch (Throwable t) {
            return false;
        }
        
        return true;
    }
    
    public byte []getLevelData(int recordID) {
        try {
            return m_recordStore.getRecord(recordID);
        } catch (Throwable t) { }
        
        return null;
    }
    
    public boolean deleteLevelData(int recordID) {
        try {
            m_recordStore.deleteRecord(recordID);
            
            return true;
        } catch (Throwable t) { }
        
        return false;
    }
    
    public byte []getLevelDataFromIndex(int index) {
        try {
            return m_recordStore.getRecord(m_recordIDs[index]);
        } catch (Throwable t) { }
        
        return null;
    }
    
    public Hashtable getLevelHash(int index) {
        try {
            return levelEntryToHash(m_recordStore.getRecord(m_recordIDs[index]));
        } catch (Throwable t) { }
        
        return null;
    }
    
    public char [][]getLevelShortNames() {
        return m_shortNames;
    }
    
    public int []getRecordIDs() {
        return m_recordIDs;
    }
    
    public boolean addLevel(byte []data) {
        try {
            long storedTime = System.currentTimeMillis();
            
            data[12] = (byte)(storedTime >>> 56);
            data[13] = (byte)(storedTime >>> 48);
            data[14] = (byte)(storedTime >>> 40);
            data[15] = (byte)(storedTime >>> 32);
            data[16] = (byte)(storedTime >>> 24);
            data[17] = (byte)(storedTime >>> 16);
            data[18] = (byte)(storedTime >>>  8);
            data[19] = (byte)(storedTime >>>  0);
            
            m_recordStore.addRecord(data, 0, data.length);
        } catch (Throwable t) {
            return false;
        }
        
        return true;
    }

    // fucked items go down to the end
    public int compare(byte[] recOne, byte[] recTwo) {
        Hashtable htOne = levelEntryToHash(recOne);
        Hashtable htTwo = levelEntryToHash(recTwo);
        
        if (htOne == null)
            return RecordComparator.FOLLOWS;
        
        if (htTwo == null)
            return RecordComparator.PRECEDES;
        
        int retCode = 0;
        
        switch (m_compMode) {
            case cydLevelStorageSystem.COMPARE_MODE_SHORT_NAME: {
                String recOneVal = (String)htOne.get(KEY_SHORT_NAME);
                String recTwoVal = (String)htOne.get(KEY_SHORT_NAME);
                
                int val = recOneVal.compareTo(recTwoVal);
                
                if (val > 0)
                    return RecordComparator.PRECEDES;
                else if (val < 0)
                    return RecordComparator.FOLLOWS;
                else if (val == 0)
                    return RecordComparator.EQUIVALENT;
            }
            break;
            case cydLevelStorageSystem.COMPARE_MODE_STORED_TIME: {
                long recOneVal = ((Long)htOne.get(KEY_STORED_TIME)).longValue();
                long recTwoVal = ((Long)htTwo.get(KEY_STORED_TIME)).longValue();
                
                if (recOneVal > recTwoVal)
                    return RecordComparator.PRECEDES;
                else if (recOneVal < recTwoVal)
                    return RecordComparator.FOLLOWS;
                else if (recOneVal == recTwoVal)
                    return RecordComparator.EQUIVALENT;
            }
            break;
        }
        
        return RecordComparator.FOLLOWS;
    }
    
    public Hashtable levelEntryToHash(byte []b) {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        Hashtable ret = new Hashtable();
        
        try {
            int magicNum = dis.readInt();

            ret.put(cydLevelStorageSystem.KEY_CREATED_TIME, new Long(dis.readLong()));
            ret.put(cydLevelStorageSystem.KEY_STORED_TIME, new Long(dis.readLong()));
            
            ret.put(cydLevelStorageSystem.KEY_SHORT_NAME, dis.readUTF());
            ret.put(cydLevelStorageSystem.KEY_LONG_NAME, dis.readUTF());
            
            ret.put(cydLevelStorageSystem.KEY_AUTHOR_NAME, dis.readUTF());
            
            ret.put(cydLevelStorageSystem.KEY_COMMENTS, dis.readUTF());
            
            int dataLength = dis.readInt();
            
            byte []vmDat = new byte[dataLength];
            
            int readAmount = dis.read(vmDat);
            
            if (magicNum != cydLevelStorageSystem.LEVEL_MAGIC_NUM || readAmount != dataLength)
                return null;
            
            ret.put(cydLevelStorageSystem.KEY_VM_DATA, vmDat);
        } catch (Throwable t) {
            return null;
        }
        
        return ret;
    }
}
