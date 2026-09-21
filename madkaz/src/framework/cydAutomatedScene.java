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

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class cydAutomatedScene {
    public static final int SPRITE_INVISIBLE_FLAG                     = 0x80000000;
    
    public static final int LAYER_TYPE_MASK                           = 7;      // bbb
    public static final int LAYER_TYPE_NONE                           = 0;      // 000     ; param 3 = is visible
    public static final int LAYER_TYPE_STATIC                         = 1;      // 001     ; param 0 = type, param 1 = x trans point, param 2 = y trans point, param 3 = is visible
    public static final int LAYER_TYPE_SCROLL                         = 2;      // 010     ; param 0 = type, param 1 = x trans point, param 2 = y trans point, param 3 = is visible, param 4 = x scroll point, param 5 = y scroll point, param 6 = x scroll rate (1 pixel in ? ms), param 7 = y scroll rate (1 pixel in ? ms), param 8 = elapsedTime for x, param 9 = elapsed time for y
    public static final int LAYER_TYPE_LINKED_PARALLAX                = 3;      // 011     ; param 0 = type, param 1 = x trans point, param 2 = y trans point, param 3 = is visible, param 4 = slow down rate (divide by amount), param 5 = y cam trans point
    public static final int LAYER_TYPE_LINKED_PARALLAX_LOOP           = 4;      // 100     ; param 0 = type, param 1 = x trans point, param 2 = y trans point, param 3 = is visible, param 4 = slow down rate (divide by amount), param 5 = y cam trans point, param 6 = y length, param 7 = y repeat count
    
    public int [][]m_props;                                                     // set -> layerprops
    public int [][][]m_set;                                                     // set -> layers -> items (sprit link, interpolator link, x offset, y offset)
    public int [][][]m_sequences;                                               // sprite and waittime sequences for spritedb
    public Image []m_images;                                                    // imagedb
    public cydAdvancedSprite []m_sprites;                                       // spritedb
    public cydPointInterpolator []m_interpolators;                              // interpolatordb
    
    public cydAutomatedScene() {
        m_props = new int[0][0];
        m_set = new int[0][0][0];
        m_sequences = new int[0][][];
        m_images = new Image[0];
        m_sprites = new cydAdvancedSprite[0];
        m_interpolators = new cydPointInterpolator[0];
    }
    
    public void setLayerVisibility(int setIndex, int layerIndex, boolean v) {
        setLayerParam(setIndex, layerIndex, 3, (v ? 1 : 0));
    }
    
    public boolean isLayerVisible(int setIndex, int layerIndex) {
        return getLayerParam(setIndex, layerIndex, 3) != 0;
    }
    
    public boolean isSpriteLinkVisible(int setIndex, int layerIndex, int itemIndex) {
        return (m_set[setIndex][layerIndex][(itemIndex << 2) | 0 ] & cydAutomatedScene.SPRITE_INVISIBLE_FLAG) != 0;
    }
    
    public void setLayerParam(int setIndex, int layerIndex, int paramIndex, int param) {
        m_props[setIndex][(layerIndex << 4) | paramIndex] = param;
    }
    
    public int getLayerParam(int setIndex, int layerIndex, int paramIndex) {
        return m_props[setIndex][(layerIndex << 4) | paramIndex];
    }
    
    public void setNoneLayer(int setIndex, int layerIndex) {
        setLayerParam(setIndex, layerIndex, 0, cydAutomatedScene.LAYER_TYPE_NONE);
        setLayerParam(setIndex, layerIndex, 3, 1);
    }
    
    public void setStaticLayer(int setIndex, int layerIndex, int transX, int transY) {
        setLayerParam(setIndex, layerIndex, 0, cydAutomatedScene.LAYER_TYPE_STATIC);
        setLayerParam(setIndex, layerIndex, 1, transX);
        setLayerParam(setIndex, layerIndex, 2, transY);
        setLayerParam(setIndex, layerIndex, 3, 1);
    }
    
    public void setScrollLayer(int setIndex, int layerIndex, int transX, int transY, int xRate, int yRate) {
        setLayerParam(setIndex, layerIndex, 0, cydAutomatedScene.LAYER_TYPE_SCROLL);
        setLayerParam(setIndex, layerIndex, 1, transX);
        setLayerParam(setIndex, layerIndex, 2, transY);
        setLayerParam(setIndex, layerIndex, 3, 1);
        setLayerParam(setIndex, layerIndex, 4, 0);
        setLayerParam(setIndex, layerIndex, 5, 0);
        setLayerParam(setIndex, layerIndex, 6, xRate);
        setLayerParam(setIndex, layerIndex, 7, yRate);
        setLayerParam(setIndex, layerIndex, 8, 0);
        setLayerParam(setIndex, layerIndex, 9, 0);
    }
    
    public void setLinkedParallax(int setIndex, int layerIndex, int transX, int transY, int slowDownRate) {
        setLayerParam(setIndex, layerIndex, 0, cydAutomatedScene.LAYER_TYPE_LINKED_PARALLAX);
        setLayerParam(setIndex, layerIndex, 1, transX);
        setLayerParam(setIndex, layerIndex, 2, transY);
        setLayerParam(setIndex, layerIndex, 3, 1);
        setLayerParam(setIndex, layerIndex, 4, slowDownRate);
        setLayerParam(setIndex, layerIndex, 5, 0);
    }
    
    public void setLinkedParallaxLoop(int setIndex, int layerIndex, int transX, int transY, int slowDownRate, int loopLength, int repeatCount) {
        setLayerParam(setIndex, layerIndex, 0, cydAutomatedScene.LAYER_TYPE_LINKED_PARALLAX_LOOP);
        setLayerParam(setIndex, layerIndex, 1, transX);
        setLayerParam(setIndex, layerIndex, 2, transY);
        setLayerParam(setIndex, layerIndex, 3, 1);
        setLayerParam(setIndex, layerIndex, 4, slowDownRate);
        setLayerParam(setIndex, layerIndex, 5, 0);
        setLayerParam(setIndex, layerIndex, 6, loopLength);
        setLayerParam(setIndex, layerIndex, 7, repeatCount);
    }
    
    public boolean isInterpolatorDone(int interpolatorIndex) {
        return m_interpolators[interpolatorIndex].isDone();
    }
    
    public int getSpriteBlockIndex(int spriteIndex) {
        return m_sprites[spriteIndex].m_blockIndex;
    }
    
    public void loadNewImage(int imageIndex, Image image) {
        m_images[imageIndex] = image;
    }
    
    public void loadNewSprite(int spriteIndex, int imageIndex, int frameWidth, int frameHeight, int frameSequenceIndex, int waitSequenceIndex) {
        m_sprites[spriteIndex] = new cydAdvancedSprite(m_images[imageIndex], frameWidth, frameHeight, m_sequences[frameSequenceIndex], m_sequences[waitSequenceIndex]);
    }
    
    public void loadNewInterpolationPoints(int interpolatorIndex, int []xyData, int []timeData, boolean dontRepeat, boolean smoothIntegration) {
        cydPointInterpolator oldInterpolator = m_interpolators[interpolatorIndex];
        
        if (smoothIntegration && (oldInterpolator != null) && (xyData.length >= 2)) {
            xyData[0] = oldInterpolator.getPoints()[0];
            xyData[1] = oldInterpolator.getPoints()[1];
        }
        
        m_interpolators[interpolatorIndex] = new cydPointInterpolator(2, xyData, timeData, 0, dontRepeat);
    }
    
    public void loadNewSequence(int sequenceIndex, int []data) {
        int [][]seq = new int[data[0]][];
        
        int index = 0;
        int pos = 1;
        while (pos < data.length) {
            int count = data[pos];
            seq[index] = new int[count];
            
            pos++;
            
            for (int i = 0; i < count; i++) {
                seq[index][i] = data[pos];
                pos++;
            }
            
            index++;
        }
        
        m_sequences[sequenceIndex] = seq;
    }
    
    public void switchSpriteSequence(int spriteIndex, int frameSequenceIndex, int waitSequenceIndex) {
        m_sprites[spriteIndex].setFrameSequence(m_sequences[frameSequenceIndex], m_sequences[waitSequenceIndex]);
    }
    
    private int min(int a, int b) {
        return (a < b ? a : b);
    }
    
    public void resizeDB(int spriteCount, int interpolatorCount, int sequenceCount, int imageCount) {
        if (imageCount != -1) {
            Image []newImages = new Image[imageCount];
            System.arraycopy(m_images, 0, newImages, 0, min(imageCount, m_images.length));
            m_images = newImages;
        }
        
        if (sequenceCount != -1) {
            int [][][]newSequences = new int[sequenceCount][][];
            System.arraycopy(m_sequences, 0, newSequences, 0, min(sequenceCount, m_sequences.length));
            m_sequences = newSequences;
        }
        
        if (spriteCount != -1) {
            cydAdvancedSprite []newSprites = new cydAdvancedSprite[spriteCount];
            System.arraycopy(m_sprites, 0, newSprites, 0, min(spriteCount, m_sprites.length));
            m_sprites = newSprites;
        }
        
        if (interpolatorCount != -1) {
            cydPointInterpolator []newInterpolators = new cydPointInterpolator[interpolatorCount];
            System.arraycopy(m_interpolators, 0, newInterpolators, 0, min(interpolatorCount, m_interpolators.length));
            m_interpolators = newInterpolators;
        }
    }
    
    public int getSetLength() {
        return m_set.length;
    }
    
    public int getLayerLength(int set) {
        return m_set[set].length;
    }
    
    public int getItemLength(int set, int layer) {
        return m_set[set][layer].length >> 2;
    }
    
    public int getImageCount() {
        return m_images.length;
    }
    
    public int getSpriteCount() {
        return m_sprites.length;
    }
    
    public int getInterpolatorCount() {
        return m_interpolators.length;
    }
    
    public int getSequenceCount() {
        return m_sequences.length;
    }
    
    public void resizeSet(int setCount) {
        if (setCount != -1) {
            int oldSetCount = m_set.length;
            
            int [][][]newSet = new int[setCount][][];
            System.arraycopy(m_set, 0, newSet, 0, min(setCount, m_set.length));
            m_set = newSet;
            
            for (int i = oldSetCount; i < setCount; i++)
                m_set[i] = new int[0][0];
            
            int [][]newProps = new int[setCount << 4][];
            System.arraycopy(m_props, 0, newProps, 0, min(setCount << 4, m_props.length));
            m_props = newProps;
            
            for (int i = oldSetCount; i < setCount; i++)
                m_props[i] = new int[0];
        }
    }
    
    public void resizeLayer(int setIndex, int layerCount) {
        if (layerCount != -1) {
            int [][]newLayers = new int[layerCount][0];
            System.arraycopy(m_set[setIndex], 0, newLayers, 0, min(layerCount, m_set[setIndex].length));
            m_set[setIndex] = newLayers;
            
            int []newLayerProps = new int[layerCount << 4];
            System.arraycopy(m_props[setIndex], 0, newLayerProps, 0, min(layerCount << 4, m_props[setIndex].length));
            m_props[setIndex] = newLayerProps;
        }
    }
    
    public void resizeLayerItems(int setIndex, int layerIndex, int itemCount) {
        int []newItems = new int[itemCount << 2];
        System.arraycopy(m_set[setIndex][layerIndex], 0, newItems, 0, min(itemCount << 2, m_set[setIndex][layerIndex].length));
        m_set[setIndex][layerIndex] = newItems;
    }
    
    public void switchSets(int srcSet, int dstSet) {
        {
            int [][]dst = m_set[dstSet];
            m_set[dstSet] = m_set[srcSet];
            m_set[srcSet] = dst;
        }
        
        {
            int []dst = m_props[dstSet];
            m_props[dstSet] = m_props[srcSet];
            m_props[srcSet] = dst;
        }
    }
    
    public void switchLayers(int srcSetIndex, int dstSetIndex, int srcLayerIndex, int dstLayerIndex) {
        int []dst = m_set[dstSetIndex][dstLayerIndex];
        m_set[dstSetIndex][dstLayerIndex] = m_set[srcSetIndex][srcLayerIndex];
        m_set[srcSetIndex][srcLayerIndex] = dst;
    }
    
    public void setSpriteLinkVisible(int setIndex, int layerIndex, int itemIndex, boolean visible) {
        if (visible)
            m_set[setIndex][layerIndex][itemIndex << 2] |= cydAutomatedScene.SPRITE_INVISIBLE_FLAG;
        else
            m_set[setIndex][layerIndex][itemIndex << 2] &= ~cydAutomatedScene.SPRITE_INVISIBLE_FLAG;
    }
    
    public void setItem(int setIndex, int layerIndex, int itemIndex, int itemOffset, int data) {
        m_set[setIndex][layerIndex][(itemIndex << 2) | itemOffset] = data;
    }
    
    public int getItem(int setIndex, int layerIndex, int itemIndex, int itemOffset) {
        return m_set[setIndex][layerIndex][(itemIndex << 2) | itemOffset];
    }
    
    public void resetInterpolator(int interpolatorIndex) {
        m_interpolators[interpolatorIndex].restart();
    }
    
    public void process(int timeElapsed, int yCamPos) {
        cydAdvancedSprite []sprites = m_sprites;
        cydPointInterpolator []interpolators = m_interpolators;
        
        int spriteCount = sprites.length;
        
        for (int i = 0; i < spriteCount; i++)
            sprites[i].play(timeElapsed);
        
        int interpolatorCount = interpolators.length;
        
        for (int i = 0; i < interpolatorCount; i++)
            interpolators[i].interpolate(timeElapsed);
        
        int [][][]sets = m_set;
        int [][]setProps = m_props;
        
        int setCount = sets.length;
        
        for (int k = 0; k < setCount; k++) {
            int [][]layers = sets[k];
            int []layerProps = setProps[k];
            
            int layerCount = layers.length;
            
            for (int i = 0; i < layerCount; i++) {
                int layerPropsIndex = i << 4;
                
                int layerType = layerProps[layerPropsIndex] & cydAutomatedScene.LAYER_TYPE_MASK;
                
                switch (layerType) {
                    case cydAutomatedScene.LAYER_TYPE_NONE: {
                    }
                    break;
                    case cydAutomatedScene.LAYER_TYPE_STATIC: {
                    }
                    break;
                    case cydAutomatedScene.LAYER_TYPE_SCROLL: {
                        int xTimeRate = layerProps[layerPropsIndex | 6];
                        int yTimeRate = layerProps[layerPropsIndex | 7];
                        
                        if (xTimeRate > 0) {
                            int xElapsedTime = layerProps[layerPropsIndex | 8] + timeElapsed;
                            int xIterationAmount = xElapsedTime / xTimeRate;
                            layerProps[layerPropsIndex | 8] = xElapsedTime % xTimeRate;
                            layerProps[layerPropsIndex | 4] += xIterationAmount;
                        }
                        
                        if (yTimeRate > 0) {
                            int yElapsedTime = layerProps[layerPropsIndex | 9] + timeElapsed;
                            int yIterationAmount = yElapsedTime / yTimeRate;
                            layerProps[layerPropsIndex | 9] = yElapsedTime % yTimeRate;
                            layerProps[layerPropsIndex | 5] += yIterationAmount;
                        }
                    }
                    break;
                    case cydAutomatedScene.LAYER_TYPE_LINKED_PARALLAX: {
                        int slowDownRate = layerProps[layerPropsIndex | 4];
                        
                        layerProps[layerPropsIndex | 5] = yCamPos / slowDownRate;
                    }
                    break;
                    case cydAutomatedScene.LAYER_TYPE_LINKED_PARALLAX_LOOP: {
                        int slowDownRate = layerProps[layerPropsIndex | 4];
                        int yLength = layerProps[layerPropsIndex | 6];
                        
                        int slowDownPoint = yCamPos / slowDownRate;
                        int yTransCamPoint = slowDownPoint % yLength;
                        
                        if (yTransCamPoint < 0)
                            yTransCamPoint = yLength + yTransCamPoint;
                        
                        layerProps[layerPropsIndex | 5] = yTransCamPoint;
                    }
                    break;
                }
            }
        }
    }
    
    public void drawLayers(Graphics g, int set) {
        cydAdvancedSprite []sprites = m_sprites;
        cydPointInterpolator []interpolators = m_interpolators;
        
        int [][]layers = m_set[set];
        int []layerProps = m_props[set];
        
        int layerCount = layers.length;
        
        for (int i = 0; i < layerCount; i++) {
            int []layer = layers[i];
            
            
            int layerPropsIndex = i << 4;
            
            
            if (layerProps[layerPropsIndex | 3] == 0)
                continue;
            
            
            int layerType = layerProps[layerPropsIndex] & cydAutomatedScene.LAYER_TYPE_MASK;
            
            int layerXTrans = 0;
            int layerYTrans = 0;
            
            switch (layerType) {
                case cydAutomatedScene.LAYER_TYPE_STATIC: {
                    layerXTrans = layerProps[layerPropsIndex | 1];
                    layerYTrans = layerProps[layerPropsIndex | 2];
                    
                    g.translate(layerXTrans, layerYTrans);
                }
                break;
                case cydAutomatedScene.LAYER_TYPE_SCROLL: {
                    layerXTrans = layerProps[layerPropsIndex | 1] + layerProps[layerPropsIndex | 4];
                    layerYTrans = layerProps[layerPropsIndex | 2] + layerProps[layerPropsIndex | 5];
                    
                    g.translate(layerXTrans, layerYTrans);
                }
                break;
                case cydAutomatedScene.LAYER_TYPE_LINKED_PARALLAX:
                case cydAutomatedScene.LAYER_TYPE_LINKED_PARALLAX_LOOP: {
                    layerXTrans = layerProps[layerPropsIndex | 1];
                    layerYTrans = layerProps[layerPropsIndex | 2] - (layerProps[layerPropsIndex | 5]);
                    
                    g.translate(layerXTrans, layerYTrans);
                }
                break;
            }
            
            
            
            int layerElementCount = layer.length;
            
            
            
            
            switch (layerType) {
                case cydAutomatedScene.LAYER_TYPE_NONE: {
                    for (int j = 0; j < layerElementCount; j += 4) {
                        int spriteLink = layer[j | 0];
                        
                        if ((spriteLink & cydAutomatedScene.SPRITE_INVISIBLE_FLAG) != 0)
                            continue;
                        
                        int interpolatorLink = layer[j | 1];
                        int xOffset = layer[j | 2];
                        int yOffset = layer[j | 3];
                        
                        int []interpolatorPoints = interpolators[interpolatorLink].m_points;
                        cydAdvancedSprite sprite = sprites[spriteLink];
                        
                        sprite.setPosition(interpolatorPoints[0] + xOffset, interpolatorPoints[1] + yOffset);
                        sprite.paint(g);
                    }
                }
                break;
                case cydAutomatedScene.LAYER_TYPE_STATIC:
                case cydAutomatedScene.LAYER_TYPE_SCROLL:
                case cydAutomatedScene.LAYER_TYPE_LINKED_PARALLAX: {
                    for (int j = 0; j < layerElementCount; j += 4) {
                        int spriteLink = layer[j | 0];
                        
                        if ((spriteLink & cydAutomatedScene.SPRITE_INVISIBLE_FLAG) != 0)
                            continue;
                        
                        int interpolatorLink = layer[j | 1];
                        int xOffset = layer[j | 2];
                        int yOffset = layer[j | 3];
                        
                        int []interpolatorPoints = interpolators[interpolatorLink].m_points;
                        cydAdvancedSprite sprite = sprites[spriteLink];
                        
                        sprite.setPosition(interpolatorPoints[0] + xOffset, interpolatorPoints[1] + yOffset);
                        sprite.paint(g);
                    }
                    
                    g.translate(-layerXTrans, -layerYTrans);
                }
                break;
                case cydAutomatedScene.LAYER_TYPE_LINKED_PARALLAX_LOOP: {
                    int loopLength = layerProps[layerPropsIndex | 6];
                    int repeatCount = layerProps[layerPropsIndex | 7];
                    
                    for (int k = 0; k < repeatCount; k++) {
                        for (int j = 0; j < layerElementCount; j += 4) {
                            int spriteLink = layer[j | 0];
                            
                            if ((spriteLink & cydAutomatedScene.SPRITE_INVISIBLE_FLAG) != 0)
                                continue;
                            
                            int interpolatorLink = layer[j | 1];
                            int xOffset = layer[j | 2];
                            int yOffset = layer[j | 3];
                            
                            int []interpolatorPoints = interpolators[interpolatorLink].m_points;
                            cydAdvancedSprite sprite = sprites[spriteLink];
                            
                            sprite.setPosition(interpolatorPoints[0] + xOffset, interpolatorPoints[1] + yOffset);
                            sprite.paint(g);
                        }
                        
                        g.translate(0, loopLength);
                    }
                    
                    g.translate(-layerXTrans, (-layerYTrans) - (repeatCount * loopLength));
                }
                break;
            }
        }
    }
}