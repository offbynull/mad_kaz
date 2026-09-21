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

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

public class cydMainStorageSystem {
    public static final int RMS_CONFIG_SETTINGS_SAVED                           = 0;
    public static final int RMS_CONFIG_INDEX_MUSIC                              = 1;
    public static final int RMS_CONFIG_INDEX_BILINEAR_SCALING                   = 2;
    public static final int RMS_CONFIG_ZONE_NAX_MODE_BASE                       = 3;
    public static final int RMS_CONFIG_LENGTH                                   = 64;
    
    public static final String STORAGE_NAME                                     = "gdat";
    
    public RecordStore m_recordStore;
    public byte []m_data;
    
    public cydMainStorageSystem() {
        reload();
    }
    
    public boolean isSettingsSaved() {
        return m_data[cydMainStorageSystem.RMS_CONFIG_SETTINGS_SAVED] != 0;
    }
    
    public boolean isMusicOn() {
        return m_data[cydMainStorageSystem.RMS_CONFIG_INDEX_MUSIC] != 0;
    }
    
    public boolean isBilinearScalingOn() {
        return m_data[cydMainStorageSystem.RMS_CONFIG_INDEX_BILINEAR_SCALING] != 0;
    }
    
    public int getMaxMode(int zoneNumber) {
        return m_data[cydMainStorageSystem.RMS_CONFIG_ZONE_NAX_MODE_BASE + zoneNumber];
    }
    
    public void setSettingsSaved(boolean b) {
        m_data[cydMainStorageSystem.RMS_CONFIG_SETTINGS_SAVED] = (byte)(b ? 1 : 0);
        forceSave();
    }
    
    public void setMusicOn(boolean b) {
        m_data[cydMainStorageSystem.RMS_CONFIG_INDEX_MUSIC] = (byte)(b ? 1 : 0);
        forceSave();
    }
    
    public void setBilinearScalingOn(boolean b) {
        m_data[cydMainStorageSystem.RMS_CONFIG_INDEX_BILINEAR_SCALING] = (byte)(b ? 1 : 0);
        forceSave();
    }
    
    public void setMaxMode(int zoneNumber, int mode) {
        m_data[cydMainStorageSystem.RMS_CONFIG_ZONE_NAX_MODE_BASE + zoneNumber] = (byte)mode;
        forceSave();
    }
    
    public void incMaxMode(int zoneNumber, int incAmount) {
        m_data[cydMainStorageSystem.RMS_CONFIG_ZONE_NAX_MODE_BASE + zoneNumber] += incAmount;
        forceSave();
    }
    
    public void clearAllMaxModes() {
        for (int i = cydMainStorageSystem.RMS_CONFIG_ZONE_NAX_MODE_BASE; i < cydMainStorageSystem.RMS_CONFIG_LENGTH; i++)
            m_data[i] = 0;
        
        forceSave();
    }
    
    public void setAllMaxModes(int maxMode) {
        for (int i = cydMainStorageSystem.RMS_CONFIG_ZONE_NAX_MODE_BASE; i < cydMainStorageSystem.RMS_CONFIG_LENGTH; i++)
            m_data[i] = (byte)maxMode;
        
        forceSave();
    }
    
    public void reload() {
        try {
            RecordStore rs = RecordStore.openRecordStore(cydMainStorageSystem.STORAGE_NAME, true);

            // If no records exists, make record
            if (rs.getNumRecords() == 0) {
                rs.addRecord(new byte[cydMainStorageSystem.RMS_CONFIG_LENGTH], 0, cydMainStorageSystem.RMS_CONFIG_LENGTH);
                rs.closeRecordStore();

                rs = RecordStore.openRecordStore(cydMainStorageSystem.STORAGE_NAME, true);
            }

            RecordEnumeration re = rs.enumerateRecords(null, null, false);

            if (re.hasNextElement())
                m_data = re.nextRecord();

            re.destroy();
            rs.closeRecordStore();
        } catch (Throwable t) {
            throw new RuntimeException(t.toString());
        }
    }
    
    public void forceSave() {
        try {
            
            // Delete old record
            
            RecordStore rs = RecordStore.openRecordStore(cydMainStorageSystem.STORAGE_NAME, true);

            RecordEnumeration re = rs.enumerateRecords(null, null, false);

            if (re.hasNextElement()) {
                int recordId = re.nextRecordId();
                rs.deleteRecord(recordId);
            }

            re.destroy();
            rs.closeRecordStore();
            
            
            // Write record
            
            rs = RecordStore.openRecordStore(cydMainStorageSystem.STORAGE_NAME, true);
            rs.addRecord(m_data, 0, cydMainStorageSystem.RMS_CONFIG_LENGTH);
            rs.closeRecordStore();
        } catch (Throwable t) {
            throw new RuntimeException(t.toString());
        }
    }
}
