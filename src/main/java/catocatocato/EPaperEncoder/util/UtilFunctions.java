package catocatocato.EPaperEncoder.util;

import java.util.Scanner;

public class UtilFunctions {

    //prompts the user to input a string
    //promptText - the prompt to display to the user
    //CONSOLE - where the user will input
    public static String promptStringInput(String promptText, Scanner CONSOLE){
        System.out.print(promptText);
        return CONSOLE.next();
    }

    //finds the index of an array given a value
    //array - array given
    //value - value you want to search the index of
    //outputs the index number
    public static int findIndex(int[] array, int value) {
        int index = -1;

        for(int i = 0; i <= array.length - 1; i++) {
            if (array[i] == value) {
                index = i;
                break;
            }
        }
        return index;
    }

    //converts 32bit RGBA into R, G and B components as an int[3], index are ordered RGB
    //RGBA - the 32bit RGBA integer
    //return an int[] with separate RGB values
    public static int[] separateRGBA(int RGBA){
        int[] colorChannels = new int[4];

        //red
        colorChannels[0] = (RGBA >> 16) & 0xFF;
        //green
        colorChannels[1] = (RGBA >> 8) & 0xFF;
        //blue
        colorChannels[2] = RGBA & 0xFF;

        return colorChannels;
    }

    //converts an RGB int[] into a 32bit RGB integer
    //returns a 32bit RGB integer
    public static int combineRGBA(int[] RGB){
        int r = (RGB[0] << 16) & 0x00FF0000;
        int g = (RGB[1] << 8) & 0x0000FF00;
        int b = RGB[2] & 0x000000FF;

        return 0xFF000000 | r | g | b;
    }

    //converts an RGBA hex value to an RGB hex value
    public static int convRGBAtoRGB(int RGBA){
        return combineRGBA(separateRGBA(RGBA));
    }
}
