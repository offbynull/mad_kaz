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

package imagemanip;

import java.util.Random;
import javax.microedition.lcdui.Image;

public class cydImageExposure {
    public static final int DARKEN = 0;
    public static final int BRIGHTEN = 1;
    
    public static final int CONTRAST = 0;
    public static final int SOFTEN = 1;
    public static final int BW_STYLE = 0x80000000;
    public static final int COLOR_STYLE = 0x00000000;
    
    public final int STYLE_MASK = 0x80000000;
    public final int TYPE_MASK = 0x00000001;
    
    // if have time add in flag for halflife2 lost coast hdr sampling style to get average tone? probably
    // need to use FPMath lib to get distance from center (sqrt pythag). would probably make calc 2x longer
    //
    // removed contrasting/softening to average tone and started calculating tones for each individual component.
    // provides for color contrast but not as intense contrasting?
    public Image contrastImage(Image inputImage, int degree, int flag) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        
        int type = flag & TYPE_MASK;
        int style = flag & STYLE_MASK;
        
        int []imageRGB = new int[width * height];
        inputImage.getRGB(imageRGB, 0, inputImage.getWidth(), 0, 0, inputImage.getWidth(), inputImage.getHeight());
        
        int averageRedTone = 0;
        int averageGreenTone = 0;
        int averageBlueTone = 0;

        // calc avg tone
        if (style == COLOR_STYLE) {
            for (int y = 0; y < height; y++) {
                int currentOffset = y * width;

                for (int x = 0; x < width; x++) {
                    int color = imageRGB[currentOffset + x];

                    int alpha = (color & 0xFF000000);
                    int red = (color & 0x00FF0000) >> 16;
                    int green = (color & 0x0000FF00) >> 8;
                    int blue = (color & 0x000000FF);

                    if (alpha != 0xFF000000)
                        continue;

                    averageRedTone += red;
                    averageGreenTone += green;
                    averageBlueTone += blue;
                }
            }
            
            averageRedTone /= imageRGB.length;
            averageGreenTone /= imageRGB.length;
            averageBlueTone /= imageRGB.length;
        } else if (style == BW_STYLE) {
            //calc avg tone
            int averageTone = 0;
            
            for (int y = 0; y < height; y++) {
                int currentOffset = y * width;

                for (int x = 0; x < width; x++) {
                    int color = imageRGB[currentOffset + x];

                    int alpha = (color & 0xFF000000);
                    int red = (color & 0x00FF0000) >> 16;
                    int green = (color & 0x0000FF00) >> 8;
                    int blue = (color & 0x000000FF);

                    if (alpha != 0xFF000000)
                        continue;

                    averageTone = (red + green + blue) / 3;
                }
            }
            
            averageTone /= imageRGB.length;
            
            averageRedTone = averageTone;
            averageGreenTone = averageTone;
            averageBlueTone = averageTone;
        }
        
        int degreeFP = cydImageFP.divFP(cydImageFP.castToFP(255 - degree), cydImageFP.castToFP(255));
        int diffDegreeFP = cydImageFP.divFP(cydImageFP.castToFP(degree), cydImageFP.castToFP(255));
        
        //contrast pixels
        if (type == CONTRAST) {
            for (int y = 0; y < height; y++) {
                int currentOffset = y * width;

                for (int x = 0; x < width; x++) {
                    int color = imageRGB[currentOffset + x];

                    int alpha = (color & 0xFF000000);
                    int red = (color & 0x00FF0000) >> 16;
                    int green = (color & 0x0000FF00) >> 8;
                    int blue = (color & 0x000000FF);

                    int finalRed = 0;
                    int finalGreen = 0;
                    int finalBlue = 0;
                    
                    if (red < averageRedTone) {
                        int redFP = cydImageFP.castToFP(red);
                        finalRed = cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, redFP)) << 16;
                    } else {
                        int redDiff = 0xFF - red;
                        int redDiffFP = cydImageFP.castToFP(redDiff);
                        finalRed = (red + cydImageFP.castToInt(cydImageFP.mulFP(diffDegreeFP, redDiffFP))) << 16;
                    }
                    
                    if (green < averageGreenTone) {
                        int greenFP = cydImageFP.castToFP(green);
                        finalGreen = cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, greenFP)) << 8;
                    } else {
                        int greenDiff = 0xFF - green;
                        int greenDiffFP = cydImageFP.castToFP(greenDiff);
                        finalGreen = (green + cydImageFP.castToInt(cydImageFP.mulFP(diffDegreeFP, greenDiffFP))) << 8;
                    }
                    
                    if (blue < averageBlueTone) {
                        int blueFP = cydImageFP.castToFP(blue);
                        finalBlue = cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, blueFP));
                    } else {
                        int blueDiff = 0xFF - blue;
                        int blueDiffFP = cydImageFP.castToFP(blueDiff);
                        finalBlue = blue + cydImageFP.castToInt(cydImageFP.mulFP(diffDegreeFP, blueDiffFP));
                    }
                    
                    imageRGB[currentOffset + x] = alpha | finalRed | finalGreen | finalBlue;
                }
            }
        } else if (type == SOFTEN) {
            for (int y = 0; y < height; y++) {
                int currentOffset = y * width;

                for (int x = 0; x < width; x++) {
                    int color = imageRGB[currentOffset + x];

                    int alpha = (color & 0xFF000000);
                    int red = (color & 0x00FF0000) >> 16;
                    int green = (color & 0x0000FF00) >> 8;
                    int blue = (color & 0x000000FF);

                    int finalRed = 0;
                    int finalGreen = 0;
                    int finalBlue = 0;
                    
                    if (red < averageRedTone) {
                        int redDiff = averageRedTone - red;
                        int redDiffFP = cydImageFP.castToFP(redDiff);
                        finalRed = (red + cydImageFP.castToInt(cydImageFP.mulFP(redDiffFP, diffDegreeFP))) << 16;
                    } else {
                        int redDiff = red - averageRedTone;
                        int redDiffFP = cydImageFP.castToFP(redDiff);
                        finalRed = (averageRedTone + cydImageFP.castToInt(cydImageFP.mulFP(redDiffFP, degreeFP))) << 16;
                    }
                    
                    if (green < averageGreenTone) {
                        int greenDiff = averageGreenTone - green;
                        int greenDiffFP = cydImageFP.castToFP(greenDiff);
                        finalGreen = (green + cydImageFP.castToInt(cydImageFP.mulFP(greenDiffFP, diffDegreeFP))) << 8;
                    } else {
                        int greenDiff = green - averageGreenTone;
                        int greenDiffFP = cydImageFP.castToFP(greenDiff);
                        finalGreen = (averageGreenTone + cydImageFP.castToInt(cydImageFP.mulFP(greenDiffFP, degreeFP))) << 8;
                    }
                    
                    if (blue < averageBlueTone) {
                        int blueDiff = averageBlueTone - blue;
                        int blueDiffFP = cydImageFP.castToFP(blueDiff);
                        finalBlue = (blue + cydImageFP.castToInt(cydImageFP.mulFP(blueDiffFP, diffDegreeFP)));
                    } else {
                        int blueDiff = blue - averageBlueTone;
                        int blueDiffFP = cydImageFP.castToFP(blueDiff);
                        finalBlue = (averageBlueTone + cydImageFP.castToInt(cydImageFP.mulFP(blueDiffFP, degreeFP)));
                    }
                    
                    imageRGB[currentOffset + x] = alpha | finalRed | finalGreen | finalBlue;
                }
            }
        }
        
        Image outputImage = Image.createRGBImage(imageRGB, width, height, true);
        imageRGB = null;        // not needed no more
        
        return Image.createImage(outputImage);
    }
    
    public Image brightenImage(Image inputImage, int degree, int flag) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        
        if (flag == DARKEN)
            degree = 255 - degree;
        
        int degreeFP = cydImageFP.divFP(cydImageFP.castToFP(degree), cydImageFP.castToFP(255));
        
        int []imageRGB = new int[width * height];
        inputImage.getRGB(imageRGB, 0, inputImage.getWidth(), 0, 0, inputImage.getWidth(), inputImage.getHeight());
        
        if (flag == DARKEN) {
            for (int y = 0; y < height; y++) {
                int currentOffset = y * width;

                for (int x = 0; x < width; x++) {
                    int color = imageRGB[currentOffset + x];

                    int alpha = (color & 0xFF000000);
                    int red = (color & 0x00FF0000) >> 16;
                    int green = (color & 0x0000FF00) >> 8;
                    int blue = (color & 0x000000FF);

                    int redFP = cydImageFP.castToFP(red);
                    int greenFP = cydImageFP.castToFP(green);
                    int blueFP = cydImageFP.castToFP(blue);

                    int finalRed = cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, redFP)) << 16;
                    int finalGreen = cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, greenFP)) << 8;
                    int finalBlue = cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, blueFP));

                    imageRGB[currentOffset + x] = alpha | finalRed | finalGreen | finalBlue;
                }
            }
        } else if (flag == BRIGHTEN) {
            for (int y = 0; y < height; y++) {
                int currentOffset = y * width;

                for (int x = 0; x < width; x++) {
                    int color = imageRGB[currentOffset + x];

                    int alpha = (color & 0xFF000000);
                    int red = (color & 0x00FF0000) >> 16;
                    int green = (color & 0x0000FF00) >> 8;
                    int blue = (color & 0x000000FF);

                    int redDiff = 0xFF - red;
                    int greenDiff = 0xFF - green;
                    int blueDiff = 0xFF - blue;

                    int redDiffFP = cydImageFP.castToFP(redDiff);
                    int greenDiffFP = cydImageFP.castToFP(greenDiff);
                    int blueDiffFP = cydImageFP.castToFP(blueDiff);

                    int finalRed = (red + cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, redDiffFP))) << 16;
                    int finalGreen = green + (cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, greenDiffFP))) << 8;
                    int finalBlue = blue + cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, blueDiffFP));

                    imageRGB[currentOffset + x] = alpha | finalRed | finalGreen | finalBlue;
                }
            }
        }
        
        Image outputImage = Image.createRGBImage(imageRGB, width, height, true);
        imageRGB = null;        // not needed no more
        
        return Image.createImage(outputImage);
    }
}
