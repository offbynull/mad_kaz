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

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class cydImageCanvasExpand {
    public Image expandImageCanvas(Image inputImage, int newWidth, int newHeight, int fillerPixel, int xPasteOffset, int yPasteOffset) {
        int imageRGB[] = new int[newWidth * newHeight];
        
        int totalSize = newWidth * newHeight;
        
        for (int i = 0; i < totalSize; i++)
            imageRGB[i] = fillerPixel;
        
        Image outputImage = Image.createRGBImage(imageRGB, newWidth, newHeight, true);
        imageRGB = null;        // not needed no more
        
        outputImage.getGraphics().drawImage(inputImage, xPasteOffset, yPasteOffset, Graphics.TOP | Graphics.LEFT);
        
        return Image.createImage(outputImage);
    }
}
