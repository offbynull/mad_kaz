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

import imagemanip.cydImageCanvasExpand;
import imagemanip.cydImageColorize;
import imagemanip.cydImageExposure;
import imagemanip.cydImageInvert;
import imagemanip.cydImagePNGModifier;
import imagemanip.cydImageResize;
import imagemanip.cydImageTile;
import java.util.Vector;
import javax.microedition.lcdui.Image;

public class cydGraphicsManager {
    public byte [][]m_imageBuffers;
    public byte [][]m_originalPNGPalettes;
    public int []m_bufferOffsetLengthsPalOffsetPalLength;
    public Image []m_loadedImages;
    public int m_maxLoadable;
    
    public static final int EFFECT_CANVAS_EXPAND            = 0;
    public static final int EFFECT_GRAYSCALE                = 1;
    public static final int EFFECT_OVERLAY_SHADE            = 2;
    public static final int EFFECT_SHADE                    = 3;
    public static final int EFFECT_COLORIZE                 = 4;
    public static final int EFFECT_CONTRAST                 = 5;
    public static final int EFFECT_BRIGHTEN                 = 6;
    public static final int EFFECT_INVERT                   = 7;
    public static final int EFFECT_RESIZE                   = 8;
    public static final int EFFECT_TILE                     = 9;
    
    public static final int PALETTE_EFFECT_SWITCH_PALETTE       = 0;
    public static final int PALETTE_EFFECT_GREEN_COLORIZE       = 1;
    public static final int PALETTE_EFFECT_GREEN_HEAT_COLORIZE  = 2;
    public static final int PALETTE_EFFECT_RED_COLORIZE         = 3;
    public static final int PALETTE_EFFECT_RED_HEAT_COLORIZE    = 4;
    public static final int PALETTE_EFFECT_INVERT_COLORIZE      = 5;
    public static final int PALETTE_EFFECT_NIGHT_TIME_COLORIZE  = 6;
    public static final int PALETTE_EFFECT_DARKEN_COLORIZE      = 7;
    public static final int PALETTE_EFFECT_BRIGHTEN_COLORIZE    = 8;
    public static final int PALETTE_EFFECT_GRAYSCALE_COLORIZE   = 9;
    
    public cydGraphicsManager(int maxLoadable) {
        m_imageBuffers = new byte[maxLoadable][];
        m_originalPNGPalettes = new byte[maxLoadable][];
        m_bufferOffsetLengthsPalOffsetPalLength = new int[maxLoadable<<2]; // * 4
        m_loadedImages = new Image[maxLoadable];
        
        m_maxLoadable = maxLoadable;
    }
    
    public void unloadBuffers(int index) {
        if (index == -1) {
            for (index = 0; index < m_maxLoadable; index++) {
                m_imageBuffers[index] = null;
                m_originalPNGPalettes[index] = null;
            }
            
            m_bufferOffsetLengthsPalOffsetPalLength = null;
        } else {
            m_imageBuffers[index] = null;
            m_originalPNGPalettes[index] = null;
        }
    }
    
    public boolean exists(int index) {
        if (index >= m_maxLoadable)
            return false;
        
        return m_imageBuffers[index] != null;
    }
    
    public void applyPaletteEffect(int index, int effectType, Vector options) {
        if (index == -1) {
            for (index = 0; index < m_maxLoadable; index++) {
                if (exists(index))
                    applyPaletteEffectSingle(index, effectType, options);
            }
        } else {
            applyPaletteEffectSingle(index, effectType, options);
        }
    }
    
    public void applyPaletteEffectSingle(int index, int effectType, Vector options) {
        int plteOffset = m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 2];
        int plteCount = m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 3];
        int plteDataOffset = plteOffset + 8;
        
        switch (effectType) {
            case cydGraphicsManager.PALETTE_EFFECT_SWITCH_PALETTE: {
                cydImagePNGModifier.changePalette(m_imageBuffers[index], plteDataOffset, plteCount, (byte [])options.elementAt(0), ((Integer)options.elementAt(1)).intValue());
            }
            break;
            case cydGraphicsManager.PALETTE_EFFECT_GREEN_COLORIZE: {
                cydImagePNGModifier.greenColorize(m_imageBuffers[index], plteDataOffset, plteCount);
            }
            break;
            case cydGraphicsManager.PALETTE_EFFECT_GREEN_HEAT_COLORIZE: {
                cydImagePNGModifier.greenHeatColorize(m_imageBuffers[index], plteDataOffset, plteCount);
            }
            break;
            case cydGraphicsManager.PALETTE_EFFECT_RED_COLORIZE: {
                cydImagePNGModifier.redColorize(m_imageBuffers[index], plteDataOffset, plteCount);
            }
            break;
            case cydGraphicsManager.PALETTE_EFFECT_RED_HEAT_COLORIZE: {
                cydImagePNGModifier.redHeatColorize(m_imageBuffers[index], plteDataOffset, plteCount);
            }
            break;
            case cydGraphicsManager.PALETTE_EFFECT_INVERT_COLORIZE: {
                cydImagePNGModifier.invertColorize(m_imageBuffers[index], plteDataOffset, plteCount);
            }
            break;
            case cydGraphicsManager.PALETTE_EFFECT_NIGHT_TIME_COLORIZE: {
                cydImagePNGModifier.nightTimeColorize(m_imageBuffers[index], plteDataOffset, plteCount);
            }
            break;
            case cydGraphicsManager.PALETTE_EFFECT_DARKEN_COLORIZE: {
                cydImagePNGModifier.darkenColorize(m_imageBuffers[index], plteDataOffset, plteCount, ((Integer)options.elementAt(1)).intValue());
            }
            break;
            case cydGraphicsManager.PALETTE_EFFECT_BRIGHTEN_COLORIZE: {
                cydImagePNGModifier.brightenColorize(m_imageBuffers[index], plteDataOffset, plteCount, ((Integer)options.elementAt(1)).intValue());
            }
            break;
            case cydGraphicsManager.PALETTE_EFFECT_GRAYSCALE_COLORIZE: {
                cydImagePNGModifier.grayscaleColorize(m_imageBuffers[index], plteDataOffset, plteCount);
            }
            break;
        }
        
        cydImagePNGModifier.recalculateChunkCRC(m_imageBuffers[index], plteOffset);
    }
    
    public void applyEffect(int index, int effectType, Object options) {
        if (index == -1) {
            for (index = 0; index < m_maxLoadable; index++) {
                if (exists(index)) {
                    Image i = applyEffectSingle(index, effectType, options);
                    m_loadedImages[index] = null;
                    System.gc();
                    m_loadedImages[index] = i;
                }
            }
        } else {
            Image i = applyEffectSingle(index, effectType, options);
            m_loadedImages[index] = null;
            System.gc();
            m_loadedImages[index] = i;
        }
    }
    
    public Image applyEffectSingle(int index, int effectType, Object options) {
        Image ret = null;
        
        switch (effectType) {
            case cydGraphicsManager.EFFECT_CANVAS_EXPAND: {
                int []data = (int [])options;
                ret = new cydImageCanvasExpand().expandImageCanvas(m_loadedImages[index], data[0], data[1], data[2], data[3], data[4]);
            }
            break;
            case cydGraphicsManager.EFFECT_GRAYSCALE: {
                cydImageColorize colorizer = new cydImageColorize();
                
                colorizer.populateGrayscale();
                ret = colorizer.colorizeImage(m_loadedImages[index]);
            }
            break;
            case cydGraphicsManager.EFFECT_OVERLAY_SHADE: {
                int []data = (int [])options;
                
                cydImageColorize colorizer = new cydImageColorize();
                
                colorizer.populateOverlayShade(data[0], data[1] != 0);
                ret = colorizer.colorizeImage(m_loadedImages[index]);
            }
            break;
            case cydGraphicsManager.EFFECT_SHADE: {
                int []data = (int [])options;
                
                cydImageColorize colorizer = new cydImageColorize();
                
                colorizer.populateShade(data[0], data[1] != 0);
                ret = colorizer.colorizeImage(m_loadedImages[index]);
            }
            break;
            case cydGraphicsManager.EFFECT_COLORIZE: {
                int []data = (int [])options;
                
                cydImageColorize colorizer = new cydImageColorize();
                
                colorizer.colorMap = data;
                ret = colorizer.colorizeImage(m_loadedImages[index]);
            }
            break;
            case cydGraphicsManager.EFFECT_CONTRAST: {
                int []data = (int [])options;
                
                cydImageExposure exposure = new cydImageExposure();
                ret = exposure.contrastImage(m_loadedImages[index], data[0], data[1]);
            }
            break;
            case cydGraphicsManager.EFFECT_BRIGHTEN: {
                int []data = (int [])options;
                
                cydImageExposure exposure = new cydImageExposure();
                ret = exposure.brightenImage(m_loadedImages[index], data[0], data[1]);
            }
            break;
            case cydGraphicsManager.EFFECT_INVERT: {
                cydImageInvert invert = new cydImageInvert();
                ret = invert.invertImage(m_loadedImages[index]);
            }
            break;
            case cydGraphicsManager.EFFECT_RESIZE: {
                int []data = (int [])options;
                
                cydImageResize resize = new cydImageResize();
                ret = resize.resizeImage(m_loadedImages[index], data[0], data[1], data[2]);
            }
            break;
            case cydGraphicsManager.EFFECT_TILE: {
                int []data = (int [])options;
                
                cydImageTile tile = new cydImageTile();
                ret = tile.tileImage(m_loadedImages[index], data[0], data[1]);
            }
        }
        
        return ret;
    }
    
    public Image getImage(int index) {
        return m_loadedImages[index];
    }
    
    public Image []getLoadedImageArray() {
        return m_loadedImages;
    }
    
    public void loadImage(int index) {
        if (index == -1) {
            for (index = 0; index < m_maxLoadable; index++) {
                if (exists(index)) {
                    m_loadedImages[index] = null;
                    System.gc();
                    m_loadedImages[index] = Image.createImage(m_imageBuffers[index], m_bufferOffsetLengthsPalOffsetPalLength[index<<2], m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 1]);
                }
            }
        } else {
            m_loadedImages[index] = null;
            System.gc();
            m_loadedImages[index] = Image.createImage(m_imageBuffers[index], m_bufferOffsetLengthsPalOffsetPalLength[index<<2], m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 1]);
        }
    }
    
    public void removeImage(int index) {
        if (index == -1) {
            for (index = 0; index < m_maxLoadable; index++) {
                m_imageBuffers[index] = null;
                m_originalPNGPalettes[index] = null;
                m_loadedImages[index] = null;
                System.gc();
            }
        } else {
            m_imageBuffers[index] = null;
            m_originalPNGPalettes[index] = null;
            m_loadedImages[index] = null;
            System.gc();
        }
    }
    
    public void removeLoadedImage(int index) {
        if (index == -1) {
            for (index = 0; index < m_maxLoadable; index++) {
                m_loadedImages[index] = null;
                System.gc();
            }
        } else {
            m_loadedImages[index] = null;
            System.gc();
        }
    }
    
    public void copyImage(int fromIndex, int toIndex, boolean copyBuffers, boolean copyLoaded) {
        if (!copyBuffers) {
            m_imageBuffers[toIndex] = m_imageBuffers[fromIndex];
            m_originalPNGPalettes[toIndex] = m_originalPNGPalettes[fromIndex];
        } else {
            int length = m_bufferOffsetLengthsPalOffsetPalLength[(fromIndex<<2) + 1];
            int offset = m_bufferOffsetLengthsPalOffsetPalLength[fromIndex<<2];
            
            if (exists(toIndex)) {
                m_imageBuffers[toIndex] = new byte[length];
                System.arraycopy(m_imageBuffers[fromIndex], offset, m_imageBuffers[toIndex], 0, length);
                
                int pngPaletteLength = m_originalPNGPalettes[fromIndex].length;
                m_originalPNGPalettes[toIndex] = new byte[pngPaletteLength];
                System.arraycopy(m_originalPNGPalettes[fromIndex], 0, m_originalPNGPalettes[toIndex], 0, pngPaletteLength);
            }
        }
        
        if (copyLoaded) {
            m_loadedImages[toIndex] = null;
            System.gc();
            m_loadedImages[toIndex] = m_loadedImages[fromIndex];
        }
        
        m_bufferOffsetLengthsPalOffsetPalLength[toIndex<<2]  = m_bufferOffsetLengthsPalOffsetPalLength[fromIndex<<2];
        m_bufferOffsetLengthsPalOffsetPalLength[(toIndex<<2) + 1]  = m_bufferOffsetLengthsPalOffsetPalLength[(fromIndex<<2) + 1];
        m_bufferOffsetLengthsPalOffsetPalLength[(toIndex<<2) + 2]  = m_bufferOffsetLengthsPalOffsetPalLength[(fromIndex<<2) + 2];
        m_bufferOffsetLengthsPalOffsetPalLength[(toIndex<<2) + 3]  = m_bufferOffsetLengthsPalOffsetPalLength[(fromIndex<<2) + 3];
    }
    
    public int addImage(int index, byte []buffer, boolean copy, int offset, int length) {
        if (!copy) {
            m_imageBuffers[index] = buffer;
        } else {
            m_imageBuffers[index] = new byte[length];
            System.arraycopy(buffer, offset, m_imageBuffers[index], 0, length);
            
            offset = 0;
        }
        
        long chunkInfo = cydImagePNGModifier.locateChunk(m_imageBuffers[index], offset, 0x504C5445);
        
        int chunkOffset = (int)chunkInfo;
        int chunkLength = (int)(chunkInfo >> 32);
        
        m_bufferOffsetLengthsPalOffsetPalLength[index<<2]  = offset;
        m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 1]  = length;
        m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 2]  = chunkOffset;
        m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 3]  = chunkLength;
        m_loadedImages[index] = null;
        System.gc();
        
        return index;
    }
    
    public void savePalette(int index) {
        if (index == -1) {
            for (index = 0; index < m_maxLoadable; index++) {
                if (exists(index)) {
                    int chunkOffset = m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 2];
                    int chunkLength = m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 3];
                    
                    m_originalPNGPalettes[index] = new byte[chunkLength];
                    System.arraycopy(m_imageBuffers[index], chunkOffset + 8, m_originalPNGPalettes[index], 0, chunkLength);
                }
            }
        } else {
            int chunkOffset = m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 2];
            int chunkLength = m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 3];
            
            m_originalPNGPalettes[index] = new byte[chunkLength];
            System.arraycopy(m_imageBuffers[index], chunkOffset + 8, m_originalPNGPalettes[index], 0, chunkLength);
        }
    }
    
    public void restorePalette(int index) {
        if (index == -1) {
            for (index = 0; index < m_maxLoadable; index++) {
                if (exists(index)) {
                    int chunkOffset = m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 2];
                    int chunkLength = m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 3];
                    
                    byte []imageData = m_imageBuffers[index];
                    byte []pngPalette = m_originalPNGPalettes[index];
                    
                    System.arraycopy(m_originalPNGPalettes[index], 0, m_imageBuffers[index], chunkOffset + 8, chunkLength);
                    
                    cydImagePNGModifier.recalculateChunkCRC(m_imageBuffers[index], chunkOffset);
                }
            }
        } else {
            int chunkOffset = m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 2];
            int chunkLength = m_bufferOffsetLengthsPalOffsetPalLength[(index<<2) + 3];
            
            byte []imageData = m_imageBuffers[index];
            byte []pngPalette = m_originalPNGPalettes[index];
            
            System.arraycopy(m_originalPNGPalettes[index], 0, m_imageBuffers[index], chunkOffset + 8, chunkLength);
            
            cydImagePNGModifier.recalculateChunkCRC(m_imageBuffers[index], chunkOffset);
        }
    }
}
