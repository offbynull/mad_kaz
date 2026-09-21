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

public class cydImageTile {
    public Image tileImage(Image inputImage, int tileXCount, int tileYCount) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        Image outputImage = Image.createImage(inputImage.getWidth() * tileXCount, inputImage.getHeight() * tileYCount);
        
        Graphics g = outputImage.getGraphics();
        
        for (int x = 0; x < tileXCount; x++) {
            for (int y = 0; y < tileYCount; y++) {
                g.drawImage(inputImage, x * width, y * height, 0);
            }
        }
        
        return Image.createImage(outputImage);
    }
}
