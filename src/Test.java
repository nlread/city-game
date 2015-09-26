import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

/**
 * Created by needa_000 on 8/18/2014.
 */
public class Test
{
    public static void main(String[] args)
    {
        int s = 1;
        try
        {
            s = System.in.read();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println(s);
    }
}
