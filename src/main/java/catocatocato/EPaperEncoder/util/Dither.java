package catocatocato.EPaperEncoder.util;

import catocatocato.EPaperEncoder.datastructures.Palette;
import static catocatocato.EPaperEncoder.util.UtilFunctions.*;
import java.awt.image.BufferedImage;

public class Dither {

    private final Palette PALETTE;
    private final BufferedImage IMAGE;

    public Dither(BufferedImage IMAGE, Palette PALETTE){
        this.IMAGE = IMAGE;
        this.PALETTE = PALETTE;
    }

    //dithers the image fed into the constructor
    public void ditherImage(){
        //prepares a hex array for output
        int[] output = new int[IMAGE.getWidth() * IMAGE.getHeight()];
        int index = 0;

        //applies the dithering algorithm
        int w = IMAGE.getWidth();
        int h = IMAGE.getHeight();

        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                //select the current and new RGB
                int imgRGB = IMAGE.getRGB(x, y);
                int newRGB = PALETTE.findClosestColor(imgRGB);
                IMAGE.setRGB(x, y, newRGB);

                //converts the hex color values into RGB values
                int[] imgRGBPalette = separateRGBA(imgRGB);
                int[] newRGBPalette = separateRGBA(newRGB);

                //calculates the errors
                int errR = imgRGBPalette[0] - newRGBPalette[0];
                int errG = imgRGBPalette[1] - newRGBPalette[1];
                int errB = imgRGBPalette[2] - newRGBPalette[2];

                //applies the error
                //Floyd-Steinberg Dithering
                /*
                if (x + 1 < w){
                    int update = calculateColor(IMAGE.getRGB(x + 1, y), errR, errG, errB, 7, 16F);
                    IMAGE.setRGB(x + 1, y, update);
                }
                if (y + 1 < h) {
                    int update = calculateColor(IMAGE.getRGB(x , y + 1), errR, errG, errB, 5, 16F);
                    IMAGE.setRGB(x, y + 1, update);
                }
                if (x - 1 >= 0 && y + 1 < h) {
                    int update = calculateColor(IMAGE.getRGB(x - 1, y + 1), errR, errG, errB, 3, 16F);
                    IMAGE.setRGB(x - 1, y + 1, update);
                }
                if (y + 1 < h && x + 1 < w) {
                    int update = calculateColor(IMAGE.getRGB(x + 1, y + 1), errR, errG, errB, 1, 16F);
                    IMAGE.setRGB(x + 1, y + 1, update);
                }
                 */

                //Burkes Dithering
                if (x + 1 < w){
                    int update = calculateColor(IMAGE.getRGB(x + 1, y), errR, errG, errB, 8, 32F);
                    IMAGE.setRGB(x + 1, y, update);
                }
                if (x + 2 < w){
                    int update = calculateColor(IMAGE.getRGB(x + 2, y), errR, errG, errB, 4, 32F);
                    IMAGE.setRGB(x + 2, y, update);
                }
                if (y + 1 < h) {
                    int update = calculateColor(IMAGE.getRGB(x , y + 1), errR, errG, errB, 8, 32F);
                    IMAGE.setRGB(x, y + 1, update);
                }
                if (x - 1 >= 0 && y + 1 < h) {
                    int update = calculateColor(IMAGE.getRGB(x - 1, y + 1), errR, errG, errB, 4, 32F);
                    IMAGE.setRGB(x - 1, y + 1, update);
                }
                if (x - 2 >= 0 && y + 1 < h) {
                    int update = calculateColor(IMAGE.getRGB(x - 2, y + 1), errR, errG, errB, 2, 32F);
                    IMAGE.setRGB(x - 2, y + 1, update);
                }
                if (y + 1 < h && x + 1 < w) {
                    int update = calculateColor(IMAGE.getRGB(x + 1, y + 1), errR, errG, errB, 4, 32F);
                    IMAGE.setRGB(x + 1, y + 1, update);
                }
                if (y + 1 < h && x + 2 < w) {
                    int update = calculateColor(IMAGE.getRGB(x + 2, y + 1), errR, errG, errB, 2, 32F);
                    IMAGE.setRGB(x + 2, y + 1, update);
                }

            }
        }
    }

    public int calculateColor(int selRGB, int errR, int errG, int errB, int dither, float base){
        int[] selRGBArray = separateRGBA(selRGB);

        selRGBArray[0] += errR * (dither / base);
        selRGBArray[1] += errG * (dither / base);
        selRGBArray[2] += errB * (dither / base);

        if (selRGBArray[0] < 0) {
            selRGBArray[0] = 0;
        } else if (selRGBArray[0] > 255) {
            selRGBArray[0] = 255;
        }
        if (selRGBArray[1] < 0) {
            selRGBArray[1] = 0;
        } else if (selRGBArray[1] > 255) {
            selRGBArray[1] = 255;
        }
        if (selRGBArray[2] < 0) {
            selRGBArray[2] = 0;
        } else if (selRGBArray[2] > 255) {
            selRGBArray[2] = 255;
        }

        return combineRGBA(selRGBArray);
    }
}
