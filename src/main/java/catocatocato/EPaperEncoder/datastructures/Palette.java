package catocatocato.EPaperEncoder.datastructures;

import static catocatocato.EPaperEncoder.util.UtilFunctions.*;

public class Palette {

    private final int[] palette;
    private final boolean ACeP;

    //uploads a palette into the class. Palette is required to be in hex form.
    //ACeP determines if we are using the color palette for the Waveshare ACeP e-paper
    public Palette(int[] palette, boolean ACeP){
        this.palette = palette;
        this.ACeP = ACeP;
    }

    //returns the palette in hex form
    public int[] getHexPalette(){
        return palette;
    }

    //returns the palette in RGB form
    public int[][] getRGBPalette(){

        int[][] rgbPalette = new int[palette.length][3];
        for(int i = 0; i <= palette.length - 1; i++){
            rgbPalette[i] = separateRGBA(palette[i]);
        }

        return rgbPalette;
    }

    //finds the closest color in the palette to a hex color
    //rgbHex - the color you want to match
    //returns the closest matching color from the palette
    public int findClosestColor(int rgbHex){
        int[][] rgbPalette = getRGBPalette();
        int[] rgbSelector = separateRGBA(rgbHex);
        int[] closestColor = new int[3];
        double bestColorDistance = Integer.MAX_VALUE;

        //calculates the closest color
        for(int i = 0; i <= rgbPalette.length - 1; i++){

            int r = rgbPalette[i][0] - rgbSelector[0];
            int g = rgbPalette[i][1] - rgbSelector[1];
            int b = rgbPalette[i][2] - rgbSelector[2];

            double colorDistance =
                    Math.pow(r,2) +
                    Math.pow(g,2) +
                    Math.pow(b,2);

            //below are parameters to model the color output based off of an RGBHSV Triangle (only usable for ACeP mode)
            //the higher the adj numbers are, the less pronounced the color
            if(ACeP) {
                //this model has been adjusted to 'normalize' the colors
                double adjBlack = 1.05;
                double adjWhite = 1.15;
                double adjRed = 1.15;
                double adjGreen = 0.95;
                double adjBlue = 1;
                double adjYellow = 1;
                double adjOrange = 1.66;
                switch (i) {
                    case 0: //black
                        colorDistance = colorDistance * adjBlack;
                        break;
                    case 1: //white
                        colorDistance = colorDistance * adjWhite;
                        break;
                    case 2: //red
                        colorDistance = colorDistance * adjRed;
                        break;
                    case 3: //green
                        colorDistance = colorDistance * adjGreen;
                        break;
                    case 4: //blue
                        colorDistance = colorDistance * adjBlue;
                        break;
                    case 5: //Yellow
                        colorDistance = colorDistance * adjYellow;
                        break;
                    case 6: //Orange
                        colorDistance = colorDistance * adjOrange;
                        break;
                }
            }

            if (colorDistance < bestColorDistance) {
                bestColorDistance = colorDistance;
                closestColor = rgbPalette[i];
            }
        }
        return combineRGBA(closestColor);
    }
}
