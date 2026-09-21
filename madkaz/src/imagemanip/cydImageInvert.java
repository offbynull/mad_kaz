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

public class cydImageInvert {
    public Image invertImage(Image inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        
        int []imageRGB = new int[width * height];
        
        inputImage.getRGB(imageRGB, 0, inputImage.getWidth(), 0, 0, inputImage.getWidth(), inputImage.getHeight());
        
        for (int y = 0; y < height; y++) {
            int currentOffset = y * width;
            
            for (int x = 0; x < width; x++) {
                int color = imageRGB[currentOffset + x];
                
                imageRGB[currentOffset + x] = 0xFF000000 | (~color);
            }
        }
        
        Image outputImage = Image.createRGBImage(imageRGB, width, height, true);
        imageRGB = null;        // not needed no more
        
        return outputImage;
    }
}
