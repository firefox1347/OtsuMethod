package otsu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author firefox1347
 */
public class PGM {
    private int height;
    private int width;
    private int maxIntensity;
    private int[][] image;
    private String imageName;
    private int[] histogram;
    private double[] CDF;
    
    public PGM(String name, int h, int w){
        imageName = name;
        height = h;
        width = w;
        image = new int[height][width];
    }
    public PGM(String filename){
        imageName = filename;
        read(filename);
    }
  
    public boolean read(String filename) {
      try {
        File f = new File(filename);
        Scanner reader = new Scanner(f);
        String header = reader.nextLine();
        if (header == null || header.length() < 2 || header.charAt(0) != 'P' || header.charAt(1) != '2') {
          throw new Exception("Wrong PGM header!");
        }

        do { // skip lines (if any) start with '#'
          header = reader.nextLine();
        } while (header.charAt(0) == '#');

        Scanner readStr = new Scanner(header);  // get w, h from last line scanned instead of file
        width = readStr.nextInt();
        height = readStr.nextInt();
        maxIntensity = reader.nextInt();  // get the rest from file

        System.out.println("Reading PGM image of size " + width + "x" + height);
        // Write your code here
        image = new int[height][width];
        for(int i = 0 ; i < height ; i++){
            for(int j = 0 ; j < width ; j++){
                image[i][j] = reader.nextInt();
            }
        }


        // End of your code        
      }catch(Exception e){
        width = -1;
        height = -1;
      }
      return true;
    }
    
    public PGM otsu(){
        PGM OtsuImage = new PGM("OtsuImage", height, width);
        //step 1: Compute the histogram
        histogram = new int[256];
        for(int i = 0 ; i < height ; i++){
            for(int j = 0 ; j < width ; j++){
                histogram[image[i][j]]++;
            }
        }
        //end of step 1
        for(int i = 0 ; i < 256; i++){System.out.println(histogram[i]+"\n");} //debug for step 1
        //step 2: Compute the CDF
        CDF = new double[256];
        CDF[0] = histogram[0] / (height*width);
        for(int i = 1 ; i < 256 ; i++){
            CDF[i] = CDF[i-1] + (double)histogram[i] / (height*width);
        }
        //end of step 2
        System.out.println(CDF[255]);//debug for step 2
        //step 3
        
        //end of step 3
        //step 4 & step5
        int Topt = 0;
        double Varopt = 0;
        for(int T = 0 ; T < 256 ; T++){
            double P[] = new double[2];
            P[0] = CDF[T];
            P[1] = 1- CDF[T];
            int sum1 = 0;
            for(int i = 0 ; i < T ; i++){
                sum1 = sum1 + i*histogram[i];
            }
            int sum2 = 0;
            for(int i = T ; i < 256 ; i++){
                sum2 = sum2 + i*histogram[i];
            }
            double var = P[0]*P[1]*Math.pow((sum1/(P[0] * width* height ) - sum2 / (P[1] *width *height)), 2) ;
            System.out.println(T+"= "+var);// debug for step 4 & 5
            if (var > Varopt){
                Varopt = var;
                Topt = T;
            }
        }
        System.out.println("Threshold T is "+ Topt);
        //end of step 4 and step 5
        
        //assign 0 or 255 according to the intensity value compared to the threshold value
        for(int i = 0 ; i < height ; i++){
            for(int j = 0 ; j < width ; j++){
                if(image[i][j] < Topt){
                    OtsuImage.image[i][j] = 0;
                }else{
                    OtsuImage.image[i][j] = 255;
                }
            }
        }
        return OtsuImage;
    }
    
    public void exportImageToFile(String filename){
        try{
            PrintStream output = new PrintStream(filename);
            output.println("P2");
            output.println(width + " " + height);
            output.println("255");
            for(int i = 0 ; i < height ; i++){
                for(int j = 0 ; j < width ; j++){
                    output.print(image[i][j]+" ");
                }
                output.println();
            }
        }catch(Exception e){
            System.err.println("err");
        }
        
    }
}
