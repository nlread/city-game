import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by needa_000 on 8/18/2014.
 */
public class DataLoader
{
    public static final String IMAGE_FOLDER_LOCATION = "images/";
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
