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

package bluetooth;
import java.util.*;
import javax.bluetooth.*;

public class cydBTDiscovery implements DiscoveryListener {
    public static final int BT_L2CAP_TYPE        = 0x0100;
    public static final int BT_RFCOMM_TYPE       = 0x0003;
    
    private DiscoveryAgent m_agent;
    private int m_maxServiceSearches = 0;
    private int m_serviceSearchCount;
    private int []m_transactionID;
    private Vector m_recordsList;
    private Vector m_deviceList;
    private UUID []m_uuid;
    
    // async fields
    private boolean m_asyncExecuting;
    private Vector m_asyncResult;

    private boolean m_cancel = false;
    
    public cydBTDiscovery() {
    }
    
    private void addToTransactionTable(int trans) {
        for (int i = 0; i < m_transactionID.length; i++) {
            if (m_transactionID[i] == -1) {
                m_transactionID[i] = trans;
                return;
            }
        }
    }
    
    private void removeFromTransactionTable(int trans) {
        for (int i = 0; i < m_transactionID.length; i++) {
            if (m_transactionID[i] == trans) {
                m_transactionID[i] = -1;
                return;
            }
        }
    }
    
    private void searchServices(RemoteDevice[] devList) {
        for (int i = 0; i < devList.length; i++) {
            try {
                int trans = m_agent.searchServices(null, m_uuid, devList[i], this);
                addToTransactionTable(trans);
            } catch (BluetoothStateException e) { }
            
            synchronized (this) {
                m_serviceSearchCount++;

                if (m_serviceSearchCount == m_maxServiceSearches) {
                    try {
                        this.wait();
                    } catch (Exception e) { }
                }
            }
            
                        
            if (m_cancel)
                return;
        }
        
        
        while (m_serviceSearchCount > 0) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (Exception e) { }
            }
        }
    }
    
    public Vector find(int type, String uuid, boolean accessCached, boolean accessPreknown) {
        m_cancel = false;
        
        return findServers(type, uuid, accessCached, accessPreknown);
    }
    
    private Vector findServers(int type, String uuid, boolean accessCached, boolean accessPreknown) {
        m_recordsList = new Vector();
        m_deviceList = new Vector();
        
        LocalDevice local = null;
        
        try {
            local = LocalDevice.getLocalDevice();
        } catch (BluetoothStateException ex) { return new Vector(); }
        
        m_agent = local.getDiscoveryAgent();
        
        m_maxServiceSearches = Integer.parseInt(LocalDevice.getProperty("bluetooth.sd.trans.max"));
        
        m_transactionID = new int[m_maxServiceSearches];
        
        // Initialize the transaction list
        for (int i = 0; i < m_maxServiceSearches; i++) {
            m_transactionID[i] = -1;
        }
        
        m_uuid = new UUID[] { new UUID(type), new UUID(uuid, false) };
        
        RemoteDevice[] devList = null;
        
        if (accessCached) {
            devList = m_agent.retrieveDevices(DiscoveryAgent.CACHED);
            
            if (m_cancel)
                return m_recordsList;
            
            if (devList != null) {
                searchServices(devList);
                return m_recordsList;
            }
        }
        
        if (accessPreknown) {
            devList = m_agent.retrieveDevices(DiscoveryAgent.PREKNOWN);
            
            if (m_cancel)
                return m_recordsList;
            
            if (devList != null) {
                searchServices(devList);
                return m_recordsList;
            }
        }
        
        try {
            m_agent.startInquiry(DiscoveryAgent.GIAC, this);
            
            synchronized (this) {
                try {
                    this.wait();
                } catch (Exception e) { }
            }
            
            
            if (m_cancel)
                return m_recordsList;
            
        } catch (BluetoothStateException e) { }
        
        if (m_deviceList.size() > 0) {
            devList = new RemoteDevice[m_deviceList.size()];
            m_deviceList.copyInto(devList);
            
            searchServices(devList);
            return m_recordsList;
        }
        
        return m_deviceList;
    }
    
    public static void filterToRFCOMMOnly(Vector v) {
        for (int i = 0; i < v.size(); i++) {
            ServiceRecord sr = (ServiceRecord)v.elementAt(i);
            
            String connURL = sr.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            
            int index= connURL.indexOf(':');
            String protocol= connURL.substring(0, index);
            
            if (!protocol.equals("btspp")) {
                v.removeElementAt(i);
                i--;
            }
        }
    }
    
    public static void filterToL2CAPOnly(Vector v) {
        for (int i = 0; i < v.size(); i++) {
            ServiceRecord sr = (ServiceRecord)v.elementAt(i);
            
            String connURL = sr.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            
            int index= connURL.indexOf(':');
            String protocol= connURL.substring(0, index);
            
            if (!protocol.equals("btl2cap")) {
                v.removeElementAt(i);
                i--;
            }
        }        
    }
    
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        m_deviceList.addElement(btDevice);
    }
    
    public void serviceSearchCompleted(int transID, int respCode) {
        removeFromTransactionTable(transID);
        
        m_serviceSearchCount--;
        
        synchronized (this) {
            this.notifyAll();
        }
    }
    
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        Thread.yield();
        
        for (int i = 0; i < servRecord.length; i++) {
            if (servRecord[i] != null && !m_recordsList.contains(servRecord[i])) {
                String conURL = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                
                m_recordsList.addElement(servRecord[i]);
            }
        }
    }
    
    public void inquiryCompleted(int discType) {
        synchronized (this) {
            try {
                this.notifyAll();
            } catch (Exception e) { }
        }
    }
    
    
    // async methods
    
    public boolean startAsync(final int type, final String uuid) {
        if (m_asyncExecuting)
            return true;
        
        m_cancel = false;
        m_asyncExecuting = true;
        m_asyncResult = null;
        
        Thread t = new Thread(new Runnable() { 
            
            public void run() {
                m_asyncResult = findServers(type, uuid, false, false); 
                m_asyncExecuting = false;
            } 
        });
        t.start();
        
        return true;
    }
    
    public boolean isCompleteAsync() {
        return !m_asyncExecuting;
    }
    
    public Vector getServersAsync() {
        return m_asyncResult;
    }
    
    public void cancel() {
        m_cancel = true;
        
        try {
            m_agent.cancelInquiry(this);
        } catch (Throwable t) {}
    }
}