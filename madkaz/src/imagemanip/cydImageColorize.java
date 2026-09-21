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

import javax.microedition.lcdui.Image;

public class cydImageColorize {
    public int []colorMap = new int[256];
    
    public void populateGrayscale() {
        for (int i = 0; i < 256; i++)
            colorMap[i] = (i << 16) | (i << 8) | i;
    }
    
    public void populateOverlayShade(int shade, boolean invert) {
        int shadeRed = (shade & 0x00FF0000) >> 16;
        int shadeGreen = (shade & 0x0000FF00) >> 8;
        int shadeBlue = shade & 0x000000FF;
        
        int shadeRedFP = cydImageFP.castToFP(shadeRed);
        int shadeGreenFP = cydImageFP.castToFP(shadeGreen);
        int shadeBlueFP = cydImageFP.castToFP(shadeBlue);
        
        if (!invert) {
            for (int i = 0; i < 256; i++) {
                int degreeFP = cydImageFP.divFP(cydImageFP.castToFP(i), 255);
                
                int redPortion = cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, shadeRedFP)) << 16;
                int greenPortion = cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, shadeGreenFP)) << 8;
                int bluePortion = cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, shadeBlueFP));
                
                colorMap[i] = redPortion | greenPortion | bluePortion;
            }
        } else {
            for (int i = 0; i < 256; i++) {
                int degreeFP = cydImageFP.divFP(cydImageFP.castToFP(255 - i), 255);
                
                int redPortion = cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, shadeRedFP)) << 16;
                int greenPortion = cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, shadeGreenFP)) << 8;
                int bluePortion = cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, shadeBlueFP));
                
                colorMap[i] = redPortion | greenPortion | bluePortion;
            }
        } 
    }
    
    public void populateShade(int shade, boolean invert) {
        int shadeRed = (shade & 0x00FF0000) >> 16;
        int shadeGreen = (shade & 0x0000FF00) >> 8;
        int shadeBlue = shade & 0x000000FF;
        
        int diffShadeRed = 0xFF - shadeRed;
        int diffShadeGreen = 0xFF - shadeGreen;
        int diffShadeBlue = 0xFF - shadeBlue;
        
        int diffShadeRedFP = cydImageFP.castToFP(diffShadeRed);
        int diffShadeGreenFP = cydImageFP.castToFP(diffShadeGreen);
        int diffShadeBlueFP = cydImageFP.castToFP(diffShadeBlue);
        
        if (!invert) {
            for (int i = 0; i < 256; i++) {
                int degreeFP = cydImageFP.divFP(cydImageFP.castToFP(i), 255);

                int redPortion = (shadeRed + cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, diffShadeRedFP))) << 16;
                int greenPortion = (shadeGreen + cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, diffShadeGreenFP))) << 8;
                int bluePortion = shadeBlue + cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, diffShadeBlueFP));

                colorMap[i] = redPortion | greenPortion | bluePortion;
            }
        } else {
            for (int i = 0; i < 256; i++) {
                int degreeFP = cydImageFP.divFP(cydImageFP.castToFP(255 - i), 255);

                int redPortion = (shadeRed + cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, diffShadeRedFP))) << 16;
                int greenPortion = (shadeGreen + cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, diffShadeGreenFP))) << 8;
                int bluePortion = shadeBlue + cydImageFP.castToInt(cydImageFP.mulFP(degreeFP, diffShadeBlueFP));

                colorMap[i] = redPortion | greenPortion | bluePortion;
            }
        }
    }
    
    public Image colorizeImage(Image inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        
        int []imageRGB = new int[width * height];
        
        inputImage.getRGB(imageRGB, 0, inputImage.getWidth(), 0, 0, inputImage.getWidth(), inputImage.getHeight());
        
        for (int y = 0; y < height; y++) {
            int currentOffset = y * width;

            for (int x = 0; x < width; x++) {
                int color = imageRGB[currentOffset + x];

                int alpha = (color & 0xFF000000);
                int red = (color & 0x00FF0000) >> 16;
                int green = (color & 0x0000FF00) >> 8;
                int blue = (color & 0x000000FF);

                int average = (red + green + blue) / 3;

                imageRGB[currentOffset + x] = alpha | colorMap[average];
            }
        }
        
        Image outputImage = Image.createRGBImage(imageRGB, width, height, true);
        imageRGB = null;        // not needed no more
        
        return Image.createImage(outputImage);
    }
}
