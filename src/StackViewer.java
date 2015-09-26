import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Created by needa_000 on 7/17/2014.
 */
public class StackViewer extends JPanel
{
    public static final int MIN_HEIGHT = 10;
    public static final int MAX_HEIGHT = 40;
    public static final int WIDTH = 65;
    public static final int OVERHEAD = 0;
    public static final int PADDING = 5;

    private MainWindow main;

    private LinkedList<Integer> stack;
    public StackViewer(MainWindow main)
    {
        super();
        this.main = main;
        setPreferredSize(new Dimension(WIDTH + PADDING * 2, getHeight()));
        setBackground(main.getBackground());
    }

    public void changeStack(LinkedList<Integer> newStack)
    {
        this.stack = newStack;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if(stack == null)
            return;

        int panelHeight = main.getBoard().getHeight();
        int maxTiles = (panelHeight - OVERHEAD) / MIN_HEIGHT;

        int height;
        if(stack.size() >= maxTiles)
            height = MIN_HEIGHT;
        else
        {
            height = panelHeight / stack.size();
            if (height > MAX_HEIGHT)
                height = MAX_HEIGHT;
        }

        BufferedImage backBufferContainer = new BufferedImage(WIDTH + PADDING*2,panelHeight,BufferedImage.TYPE_INT_RGB);
        Graphics backBuffer = backBufferContainer.getGraphics();

        backBuffer.setColor(getBackground());
        backBuffer.fillRect(0,0,getWidth(),getHeight());

        int count = 1;
        for(Integer i:stack)
        {
            backBuffer.setColor(Cell.COLORS[i]);
            backBuffer.fillRect(PADDING,panelHeight - height * count - 1,WIDTH,height);
            backBuffer.setColor(Color.BLACK);
            backBuffer.drawRect(PADDING,panelHeight - height * count - 1,WIDTH,height);
            count++;
        }
        g.drawImage(backBufferContainer,0,0,null);
    }
}
