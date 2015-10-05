import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Created by Ned Read on 7/17/2014.
 * Panel which displays what buildings are in a stack.
 * Draws a square with the corresponding color for each of the buildings and border in a stack representing the
 * order in which they were added. This will display the cell which is currently hovered over. Scales the size of
 * the squares up to a point, but usually up to 62 will be visible at any given point which is more than enough for
 * the game.
 */
public class StackViewer extends JPanel {
    //region Constants
    public static final int MIN_HEIGHT = 10;
    public static final int MAX_HEIGHT = 40;
    public static final int WIDTH = 65;
    public static final int OVERHEAD = 0;
    public static final int PADDING = 5;
    //endregion

    //region Private Members
    private MainWindow main;

    private LinkedList<Integer> stack;
    //endregion

    /**
     * Sets the size and background
     *
     * @param main The MainWindow in which the StackViewer is added
     */
    public StackViewer(MainWindow main) {
        super();
        this.main = main;
        setPreferredSize(new Dimension(WIDTH + PADDING * 2, getHeight()));
        setBackground(main.getBackground());
    }

    /**
     * Set the stack to be displayed.
     *
     * @param newStack The new stack to be displayed. Same representation as the stack in the Cell class.
     */
    public void changeStack(LinkedList<Integer> newStack) {
        this.stack = newStack;
        repaint();
    }

    /**
     * Paint the rectangles representing each building in the stack onto the panel
     *
     * @param g Graphics to draw the rectangles on.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (stack == null)
            return;

        int panelHeight = main.getBoard().getHeight();
        //Max tiles to be shown, depends on window height
        int maxTiles = (panelHeight - OVERHEAD) / MIN_HEIGHT;

        int height;
        if (stack.size() >= maxTiles)
            height = MIN_HEIGHT;
        else {
            height = panelHeight / stack.size();
            if (height > MAX_HEIGHT)
                height = MAX_HEIGHT;
        }

        BufferedImage backBufferContainer = new BufferedImage(WIDTH + PADDING * 2, panelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics backBuffer = backBufferContainer.getGraphics();

        backBuffer.setColor(getBackground());
        backBuffer.fillRect(0, 0, getWidth(), getHeight());

        //Draw each rectangle
        int count = 1;
        for (Integer i : stack) {
            backBuffer.setColor(Cell.COLORS[i]);
            backBuffer.fillRect(PADDING, panelHeight - height * count - 1, WIDTH, height);
            backBuffer.setColor(Color.BLACK);
            backBuffer.drawRect(PADDING, panelHeight - height * count - 1, WIDTH, height);
            count++;
        }
        g.drawImage(backBufferContainer, 0, 0, null);
    }
}
