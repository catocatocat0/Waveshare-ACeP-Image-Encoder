package catocatocato.EPaperEncoder;

import catocatocato.EPaperEncoder.datastructures.Palette;
import catocatocato.EPaperEncoder.util.Dither;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;
import java.util.Scanner;
import static catocatocato.EPaperEncoder.util.UtilFunctions.*;

public class EncoderMain {

    private final Scanner CONSOLE;
    private Palette ACEP_PALLET;
    private int[] ACEP_LOOKUP_TABLE;

    public EncoderMain(){
        this.CONSOLE = new Scanner(System.in);
        boolean DEBUG = false;

        if(!DEBUG) {
            onStart();
            mainLoop();
        }else{
            debugRunCode();
        }
    }

    public static void main(String[] args){
        new EncoderMain();
    }

    public void debugRunCode(){
    }

    private void onStart(){
        //creates the palette for the 7 color ACeP
        int[][] acepPaletteRGB = {
                {0,0,0}, //Black
                {255,255,255}, //White
                {0, 255, 0}, //Green
                {0, 0, 255}, //Blue
                {255, 0, 0}, //Red
                {255, 255, 0}, //Yellow
                {255, 128, 0,}, //Orange
                {200 ,150 , 255 } //Lilac
        };

        //creates the lookup table
        ACEP_LOOKUP_TABLE = new int[acepPaletteRGB.length];
        for (int i = 0; i <= acepPaletteRGB.length - 1; i++){
            ACEP_LOOKUP_TABLE[i] = combineRGBA(acepPaletteRGB[i]);
        }

        int[] acepPaletteHex = new int[acepPaletteRGB.length];

        //converts the RGB palette into a hex palette
        for (int i = 0; i <= acepPaletteRGB.length - 1; i++){
            acepPaletteHex[i] = combineRGBA(acepPaletteRGB[i]);
        }

        ACEP_PALLET = new Palette(acepPaletteHex, true);
    }

    private void mainLoop(){

        boolean runFlag = true;
        BufferedImage image;

        System.out.println("Type 'Q' to quit");
        while(runFlag){
            while(true){
                //Prompts user to open file
                File imageLocation;

                do {
                    String userInput = promptStringInput("Enter the file name: ", CONSOLE);
                    if(userInput.equalsIgnoreCase("q")){
                        endProgram();
                    }
                    imageLocation = new File(userInput);
                }while (!imageLocation.exists());

                try{
                    image = ImageIO.read(imageLocation);
                    break;
                }catch (IOException e){
                    System.out.println("Invalid File");
                }
            }

            //dithers the image
            Dither dither = new Dither(image, ACEP_PALLET);
            dither.ditherImage();

            //converts the image to a 8-bit color bytearray
            int w = image.getWidth();
            int h = image.getHeight();
            int[] imgBuffer = new int[w*h];
            int index = 0;
            for(int y = 0; y < h; y++){
                for(int x = 0; x < w; x++){
                    imgBuffer[index] = findIndex(ACEP_LOOKUP_TABLE, convRGBAtoRGB(image.getRGB(x, y)));
                    index++;
                }
            }

            //compresses output to 2 colors per byte
            int[] outputBuffer = new int[w*h/2];
            index = 0;
            for (int i = 0; i <= imgBuffer.length - 1; i+= 2){
                outputBuffer[index] = (imgBuffer[i] << 4) + imgBuffer[i + 1];
                index++;
            }

            //write the byte array to a file
            try{
                if(promptStringInput("Save image as png? (y/n): ", CONSOLE).equalsIgnoreCase("y")){
                    ImageIO.write(image, "png", new File("output.png"));
                }

                FileWriter outputFile = new FileWriter("output.txt");
                outputFile.write("#include \"ImageData.h\"\n\nconst unsigned char img["+outputBuffer.length+"] = {");
                for(int b:outputBuffer){
                    outputFile.write("0x" + String.format("%02X", b) + ", ");
                }
                outputFile.write("};");
                outputFile.close();

            }catch (IOException e){
                e.printStackTrace();
                System.out.println("An Error Occured!");
            }

            //clears memory
            image.flush();
        }
    }


    private void endProgram(){
        System.exit(1);
    }
}
