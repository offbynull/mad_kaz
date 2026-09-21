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

public class cydImageResize {
    // Image resize
    private int oldWidth;
    private int oldHeight;
    private int newWidth;
    private int newHeight;
    
    private int oldWidthStepAmountFP;
    private int newWidthStepAmountFP;
    private int oldHeightStepAmountFP;
    private int newHeightStepAmountFP;
    
    private int currentNewYLineFP;
    
    private int scaleType;
    
    private int []oldImageRGB;
    private int []newImageRGB;
    
    public static final int BILINEAR = 0;
    public static final int NORMAL = 1;
    
    private final static int ZEROFP = cydImageFP.castToFP(0);
    private final static int ONEFP = cydImageFP.castToFP(1);
    
    public Image resizeImage(Image inputImage, int newWidth, int newHeight, int type) {
        scaleType = type;
        
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        oldWidth = inputImage.getWidth();
        oldHeight = inputImage.getHeight();
        
        oldImageRGB = new int[inputImage.getWidth() * inputImage.getHeight()];
        newImageRGB = new int[newWidth * newHeight];
        
        inputImage.getRGB(oldImageRGB, 0, inputImage.getWidth(), 0, 0, inputImage.getWidth(), inputImage.getHeight());
        
        oldWidthStepAmountFP = cydImageFP.divFP(cydImageFP.castToFP(1), cydImageFP.castToFP(oldWidth));
        newWidthStepAmountFP = cydImageFP.divFP(cydImageFP.castToFP(1), cydImageFP.castToFP(newWidth));
        oldHeightStepAmountFP = cydImageFP.divFP(cydImageFP.castToFP(1), cydImageFP.castToFP(oldHeight));
        newHeightStepAmountFP = cydImageFP.divFP(cydImageFP.castToFP(1), cydImageFP.castToFP(newHeight));
        
        currentNewYLineFP = cydImageFP.castToFP(0);
        
        int firstY = 0;
        int secondY = 0;
        int result = 0;
        
        for (int y = 0; y < oldHeight; y++) {
            result = fillScan(y);
            
            if (result == 0) {
                firstY = result;
            } else {
                secondY = result;
                fillDown(firstY, secondY);
                firstY = secondY;
            }
        }
        
        oldImageRGB = null;        // clear so we dont run out of mem
        Image outputImage = Image.createRGBImage(newImageRGB, newWidth, newHeight, true);
        newImageRGB = null;     // not needed no more
        
        return Image.createImage(outputImage);
    }
    
    public void fillDown(int firstY, int secondY) {
        int heightCurrentIntervalFP = cydImageFP.castToFP(0);
        
        if (firstY == secondY || firstY + 1 == secondY)
            return;
        
        for (int x = 0; x < newWidth; x++) {
            int startPixel = newImageRGB[(firstY * newWidth) + x];
            int endPixel = newImageRGB[(secondY * newWidth) + x];

            for (int count = firstY; count < secondY; count++) {
                int result;
                
                if (scaleType == NORMAL)
                    result = closestPixelLine(ZEROFP, ONEFP, cydImageFP.divFP(cydImageFP.castToFP(count - firstY), cydImageFP.castToFP(secondY - firstY)), startPixel, endPixel);
                else
                    result = resolvePixelLine(ZEROFP, ONEFP, cydImageFP.divFP(cydImageFP.castToFP(count - firstY), cydImageFP.castToFP(secondY - firstY)), startPixel, endPixel);
                newImageRGB[(count * newWidth) + x] = result;
            }
        }
    }
    
    public int fillScan(int y) {
        int widthCurrentIntervalFP = cydImageFP.castToFP(0);
        int currentNewXPos = 0;
        currentNewYLineFP = cydImageFP.divFP(cydImageFP.castToFP(y), cydImageFP.castToFP(oldHeight));
        int currentNewYPos = cydImageFP.castToInt(cydImageFP.mulFP(currentNewYLineFP, cydImageFP.castToFP(newHeight)));

        while (currentNewXPos < newWidth) {
            int originalPixelLocationInPixels = cydImageFP.castToInt(cydImageFP.mulFP(widthCurrentIntervalFP, cydImageFP.castToFP(oldWidth)));
            int nextPixelLocationInPixels = originalPixelLocationInPixels + 1;

            if (nextPixelLocationInPixels < oldWidth) {
                int originalPixel = oldImageRGB[oldWidth * y + originalPixelLocationInPixels];
                int nextPixel = oldImageRGB[oldWidth * y + nextPixelLocationInPixels];

                int originalPixelLocationInLineFP = cydImageFP.divFP(cydImageFP.castToFP(originalPixelLocationInPixels), cydImageFP.castToFP(oldWidth));
                int nextPixelLocationInLineFP = cydImageFP.divFP(cydImageFP.castToFP(nextPixelLocationInPixels), cydImageFP.castToFP(oldWidth));
                
                int resultingPixel;
                
                if (scaleType == NORMAL)
                    resultingPixel = closestPixelLine(originalPixelLocationInLineFP, nextPixelLocationInLineFP, widthCurrentIntervalFP, originalPixel, nextPixel);
                else
                    resultingPixel = resolvePixelLine(originalPixelLocationInLineFP, nextPixelLocationInLineFP, widthCurrentIntervalFP, originalPixel, nextPixel);

                newImageRGB[(currentNewYPos * newWidth) + currentNewXPos] = resultingPixel;
            } else {
                newImageRGB[(currentNewYPos * newWidth) + currentNewXPos] = oldImageRGB[oldWidth * y + originalPixelLocationInPixels];
            }

            widthCurrentIntervalFP = cydImageFP.addFP(widthCurrentIntervalFP, newWidthStepAmountFP);
            currentNewXPos++;
        }
        
        return currentNewYPos;
    }
    
    public static int closestPixelLine(int lineBeginFP, int lineEndFP, int positionOnLineFP, int leftHandPixel, int rightHandPixel) {
        int diffLeftFP = cydImageFP.subFP(positionOnLineFP, lineBeginFP);
        int diffRightFP = cydImageFP.subFP(lineEndFP, positionOnLineFP);
        
        if (diffLeftFP < diffRightFP)
            return leftHandPixel;
        else
            return rightHandPixel;
    }
    
    public static int resolvePixelLine(int lineBeginFP, int lineEndFP, int positionOnLineFP, int leftHandPixel, int rightHandPixel) {
        int result = 0;
        
        int startAlpha = (leftHandPixel & 0xFF000000) >> 24;
        int startRed = (leftHandPixel & 0x00FF0000) >> 16;
        int startGreen = (leftHandPixel & 0x0000FF00) >> 8;
        int startBlue = (leftHandPixel & 0x000000FF);
        
        int endAlpha = (leftHandPixel & 0xFF000000) >> 24;
        int endRed = (rightHandPixel & 0x00FF0000) >> 16;
        int endGreen = (rightHandPixel & 0x0000FF00) >> 8;
        int endBlue = (rightHandPixel & 0x000000FF);
        
        lineEndFP = cydImageFP.subFP(lineEndFP, lineBeginFP);
        positionOnLineFP = cydImageFP.subFP(positionOnLineFP, lineBeginFP);
        
        int finalPositionFP = cydImageFP.divFP(positionOnLineFP, lineEndFP);
        
        int resultAlpha = resolveComponent(startAlpha, endAlpha, finalPositionFP);
        int resultRed = resolveComponent(startRed, endRed, finalPositionFP);
        int resultGreen = resolveComponent(startGreen, endGreen, finalPositionFP);
        int resultBlue = resolveComponent(startBlue, endBlue, finalPositionFP);
        
        return resultAlpha << 24 | resultRed << 16 | resultGreen << 8 | resultBlue;
    }
    
    public static int resolveComponent(int componentBeginDegree, int componentEndDegree, int positionFP) {
        int maxDist = componentEndDegree - componentBeginDegree;
        
        return componentBeginDegree + cydImageFP.castToInt(cydImageFP.mulFP(cydImageFP.castToFP(maxDist), positionFP));
    }
}
