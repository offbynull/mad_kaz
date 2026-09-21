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

// http://www.programmersheaven.com/download/15441/download.aspx
//                    A Fast Algorithm for Rotating Bitmaps
//
//                                      by
//                                  Karl Lager

public class cydImageRotate {
    public cydImageRotate() {
    }
    
    // phythag to get max length and width
    // not done yet, needs to be translated to fit in bounds!
    public Image rotate(Image inputImage, int angle, int []fp1616SineTable, int newWidth, int newHeight) {
        int []oldImageRGB = new int[inputImage.getWidth() * inputImage.getHeight()];
        inputImage.getRGB(oldImageRGB, 0, inputImage.getWidth(), 0, 0, inputImage.getWidth(), inputImage.getHeight());
        
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();

        int []newImageRGB = new int[newWidth * newHeight];        // rotate points to get

        int sinAngle = (angle < 0 ? (angle % 360) + 360 : angle % 360);
        int cosAngle = (angle < 0 ? ((angle - 90) % 360) + 360 : (angle + 90) % 360);
        
        int cos = fp1616SineTable[cosAngle];
        int sin = fp1616SineTable[sinAngle];
        int newX;
        int newY;
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                newX = cydImageFP.castToInt(cydImageFP.addFP(cydImageFP.mulFP(cydImageFP.castToFP(x), cos), cydImageFP.mulFP(cydImageFP.castToFP(y), sin)));
                newY = cydImageFP.castToInt(cydImageFP.subFP(cydImageFP.mulFP(cydImageFP.castToFP(y), cos), cydImageFP.mulFP(cydImageFP.castToFP(x), sin)));
  
                if (newY >= 0 && newX >= 0 && newY < newHeight && newX < newWidth)
                    newImageRGB[(newY * newWidth) + newX] = oldImageRGB[(y * width) + x];
            }
        }
        
        return Image.createImage(Image.createRGBImage(newImageRGB, newWidth, newHeight, true));
    }
}
