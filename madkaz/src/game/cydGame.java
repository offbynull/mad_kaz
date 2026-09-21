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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.microedition.midlet.*;

public class cydGame extends MIDlet {
    public cydGameManager m_manager;
    
    public cydGame() {
        m_manager = new cydGameManager(this);
        m_manager.start();
    }
    
    public void startApp() {
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
        m_manager.m_player.stopMusic();
        m_manager.m_player.closeMusic();
        m_manager.m_backlight.stop();
    }
    
    public static byte []getISToByteArray(InputStream is) {
        byte b[] = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        int len = -1;
        
        try {
            while ((len = is.read(b)) != -1) {
                baos.write(b, 0, len);
            }
        } catch (Throwable t) { }
        
        return baos.toByteArray();
    }
}
