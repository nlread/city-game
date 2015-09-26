import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Scanner;

/**
 * Created by needa_000 on 9/9/2014.
 */
public class DiscreteMath
{
    public static void main(String [] args) throws AWTException, InterruptedException
    {
        while(true)
        {
            System.out.println("Enter two numbers");
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Small:");
            int small = keyboard.nextInt();
            System.out.println("Large:");
            int large = keyboard.nextInt();

            for (int i = 1; i <= small; i++)
            {
                System.out.println(large * i % small);

            }
        }
//        Thread.sleep(3000);
//
//        Robot r = new Robot();
//        while((int)MouseInfo.getPointerInfo().getLocation().getX() > 10)
//        {
//           // if (!r.getPixelColor((int) MouseInfo.getPointerInfo().getLocation().getX(), (int) MouseInfo.getPointerInfo().getLocation().getY()).equals(Color.WHITE))
//          //  {
//                r.mousePress(InputEvent.BUTTON1_MASK);
//                Thread.sleep(100);
//                r.mouseRelease(InputEvent.BUTTON1_MASK);
//                Thread.sleep(750);
//          //  }
//        }

    }
}
