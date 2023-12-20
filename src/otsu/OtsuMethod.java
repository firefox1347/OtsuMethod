package otsu;

/**
 *
 * @author firefox1347
 */
public class OtsuMethod {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String file = "lake_256x256.pgm";
        PGM image = new PGM(file);
        PGM OtsuImage = image.otsu();
        PGM entropyVersion = image.entropy();
        OtsuImage.exportImageToFile("Otsu"+file);
        entropyVersion.exportImageToFile("entropy_"+file);
    }
    
}
