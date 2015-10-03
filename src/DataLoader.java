import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Ned Read on 8/18/2014.
 *
 * Wrapper class used for loading data from the file system.
 * So far only used for loading image.
 */
public class DataLoader
{
    public static final String IMAGE_FOLDER_LOCATION = "images/";

    /**
     * Load the given image name from the default image folder.
     * Does not do any error handling.
     * @param fileName Name of the image file
     * @return Image from image file. Null if unable to load image.
     */
    public static BufferedImage loadImage(String fileName)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File(IMAGE_FOLDER_LOCATION + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to load image " + fileName);
        }
        return image;
    }
}
