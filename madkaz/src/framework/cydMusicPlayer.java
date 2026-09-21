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

import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

public class cydMusicPlayer implements PlayerListener {
    protected Player m_noise;
    protected boolean m_playing;

    public final static String DESIRED_MIME_TYPE = "audio/midi";
    
    public cydMusicPlayer() {}
    
    public boolean newMusic(InputStream is, String mimetype) {
        try {
            m_noise = Manager.createPlayer(is, mimetype);
            
            m_noise.realize();
            m_noise.prefetch();
            m_noise.setLoopCount(-1);
        } catch (Throwable ex) {
            return false;
        }
        
        return true;
    }
    
    public void stopMusic() {
        try {
            m_noise.stop();
            m_playing = false;
        } catch (Throwable t) {}
    }
    
    public void startMusic() {
        try {
            m_noise.start();
            m_playing = true;
        } catch (Throwable t) {}
    }
    
    public void toggleMusic() {
        try {
            if (m_playing)
                stopMusic();
            else
                startMusic();
        } catch (Throwable t) {}
    }
    
    public void closeMusic() {
        try {
            m_noise.deallocate();
            m_noise.close();
        } catch (Throwable t) {}        
    }

    public void playerUpdate(Player player, String string, Object object) {
    }
}
