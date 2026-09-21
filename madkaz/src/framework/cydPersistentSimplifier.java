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

package framework;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

public final class cydPersistentSimplifier {
    private cydPersistentSimplifier() {
    }
    
    public static final boolean remove(String name) {
        try {
            RecordStore.deleteRecordStore(name);
        } catch (Throwable t) {
            return false;
        }
        
        return true;        
    }
    
    public static final boolean store(byte []b, String name) {
        try {
            RecordStore rs = RecordStore.openRecordStore(name, true);
            rs.addRecord(b, 0, b.length);
            rs.closeRecordStore();
        } catch (Throwable t) {
            return false;
        }
        
        return true;
    }
    
    public static final byte [] get(String name) {
        byte [] ret = null;
        try {
            RecordStore rs = RecordStore.openRecordStore(name, false);
            RecordEnumeration re = rs.enumerateRecords(null, null, false);

            if (re.hasNextElement())
                ret = re.nextRecord();
            
            re.destroy();
            rs.closeRecordStore();
        } catch (Throwable t) { }
        
        return ret;
    }
    
    public static final boolean exists(String name) {
        boolean b = false;
        try {
            RecordStore rs = RecordStore.openRecordStore(name, false);
            b = rs.getNumRecords() != 0;
            rs.closeRecordStore();
        } catch (Throwable t) { }
        
        return b;
    }
}
