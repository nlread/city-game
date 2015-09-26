import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by needa_000 on 7/6/2014.
 */
public class Cell extends JPanel
{
    public static final String[] TILE_NAMES = {"Natural", "Farm", "Solar", "House", "Business"};
    public static final String[] RESOURCE_NAMES= {"Natural", "Food", "Energy", "Housing", "Money"};
    public static final int WILD = 0;
    public static final int FARM = 1;
    public static final int SOLAR = 2;
    public static final int HOUSE = 3;
    public static final int BUSINESS = 4;

    //public static final Color[] COLORS = {new Color(0x013220), new Color(0x8CC739), new Color(0x20B2AA), new Color(Color.YELLOW.getRGB()), new Color(0x999999)};
    public static final BufferedImage[] IMAGES;
    public static final Color[] COLORS = {new Color(0x013220), new Color(0xCCCC00), new Color(0x20B2AA), new Color(0x8CC739), new Color(0x0F3B5F)}; //Sim City Colors

    public static final Color HIGHLIGHT_COLOR = Color.MAGENTA;
    public static final Color DRAGGED_COLOR = Color.RED;
    public static final Color STARTING_COLOR = new Color(0x00FF00);
    public static final int WIDTH = 125;
    public static final int HEIGHT = 125;
    public static final int LABEL_WIDTH_OFFSET = 5;
    public static final int LABEL_HEIGHT_OFFSET = 25;
    public static final int LABEL_SIZE = 25;
    public static final double STACK_DISPLAY_PERCENT = 9.2 / 10.0;
    public static final Color DEFAULT_BORDER_COLOR = Color.BLACK;
    public static final int BORDER_SIZE = 4;
    public static final Color LABEL_COLOR = Color.BLACK;

    static
    {
        IMAGES = new BufferedImage[TILE_NAMES.length];
        for (int i = 0; i < IMAGES.length; i++)
        {
            BufferedImage image = DataLoader.loadImage(TILE_NAMES[i].toLowerCase() + ".jpg");
            IMAGES[i] = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
            IMAGES[i].getGraphics().drawImage(image.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH), 0, 0, null);
        }
    }



    private BufferedImage onScreenImage;
    private JLabel stackLabel;
    private Board board;
    private LinkedList<Integer> stack;
    private int gridX;
    private int gridY;
    private boolean isHighlighted;
    private Color highlightColor;

    public Cell(Board board, int x, int y)
    {
        this(board, Cell.WILD, x, y);
    }

    public Cell(Board board, int state, int gridX, int gridY)
    {
        this.board = board;
        this.gridX = gridX;
        this.gridY = gridY;
        setLocation(new Point(gridX * WIDTH, gridY * HEIGHT));
        stack = new LinkedList<>();
        stack.add(state);
        onScreenImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        updateImage();
        setToolTipText("THIS IS TEXT " + Math.random());
    }

    public void replaceStack(int newState)
    {
        if (newState < 0 || newState > 4)
        {
            System.out.println("Invalid State");
            return;
        }
        stack.clear();
        stack.add(newState);
        updateImage();
    }

    public void updateImage()
    {
        Graphics2D g = (Graphics2D) onScreenImage.getGraphics();

        if(board.getMain().getOptionsWindow().shouldDisplayImages())
            drawCellImage(g);
        else
            drawCellColor(g);
        drawCellBorder(g);
        drawCellStackLabel(g);
    }


    public void updateBorder()
    {
        Graphics2D g = (Graphics2D) onScreenImage.getGraphics();
        drawCellBorder(g);
    }

    private void drawCellColor(Graphics2D g)
    {
        g.setColor(COLORS[stack.getFirst()]);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        if (stack.size() >= 2)
        {
            int rectWidth = (int) (1.0 * WIDTH * STACK_DISPLAY_PERCENT);
            int rectHeight = (int) (1.0 * HEIGHT * STACK_DISPLAY_PERCENT);
            g.setColor(COLORS[stack.getLast()]);
            g.fillRect(0, 0,rectWidth, rectHeight);

        }
    }

    private void drawCellImage(Graphics2D g)
    {
        g.drawImage(IMAGES[stack.getLast()],0,0,null);
    }

    private void drawCellBorder(Graphics2D g)
    {
        g.setStroke(new BasicStroke(BORDER_SIZE));
        if (isHighlighted)
            g.setColor(highlightColor);
        else
        {
            if (!board.getMain().getOptionsWindow().shouldDisplayGrid())
                return;
            g.setColor(DEFAULT_BORDER_COLOR);
        }
        g.drawRect((int)(BORDER_SIZE/2), (int)(BORDER_SIZE/2), WIDTH - BORDER_SIZE, HEIGHT - BORDER_SIZE);
    }

    private void drawCellStackLabel(Graphics2D g)
    {
        if (stack.size() >= 2)
        {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(new Font("Verdana", Font.BOLD, LABEL_SIZE));
            g.setColor(LABEL_COLOR);
            g.drawString("X" + stack.size(), LABEL_WIDTH_OFFSET, LABEL_HEIGHT_OFFSET);

        }
    }

    public BufferedImage getOnScreenImage()
    {
        return onScreenImage;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.drawImage(onScreenImage, getX(), getY(), null);
    }

    public int getGridX()
    {
        return gridX;
    }

    public int getGridY()
    {
        return gridY;
    }

    public boolean isHighlighted()
    {
        return isHighlighted;
    }

    public void setHighlighted(boolean isHighlighted)
    {
        setHighlighted(isHighlighted, this.highlightColor == null ? HIGHLIGHT_COLOR : this.highlightColor);
    }

    public void setHighlighted(boolean isHighlighted, Color highlightColor)
    {
        this.isHighlighted = isHighlighted;
        this.highlightColor = highlightColor;
        updateBorder();
    }

    public LinkedList<Integer> getStack()
    {
        return stack;
    }

    public ArrayList<Cell> getNeighbors()
    {
        ArrayList<Cell> neighbors = new ArrayList<>();
        Cell[][] grid = board.getGrid();
        if (gridX < grid.length - 1)
            neighbors.add(grid[gridX + 1][gridY]);
        if (gridX > 0)
            neighbors.add(grid[gridX - 1][gridY]);
        if (gridY < grid[gridX].length - 1)
            neighbors.add(grid[gridX][gridY + 1]);
        if (gridY > 0)
            neighbors.add(grid[gridX][gridY - 1]);
        return neighbors;
    }

    public void addToStack(int state)
    {
        stack.add(state);
        updateImage();
    }

    public void removeTop()
    {
        if (stack.size() == 0)
        {
            System.err.println("Stack empty!!");
            return;
        } else if (stack.size() == 1)
        {
            System.err.println("Stack only contains one element!!");
            return;
        }
        stack.remove(stack.size() - 1);
        updateImage();
    }


    public JLabel getLabel()
    {
        return stackLabel;
    }

    public void addOrReplaceStack(int state)
    {
        if(state == Toolbar.MOVE)
            return;
        if(state == WILD)
        {
            replaceStack(state);
            return;
        }
        if (state == HOUSE || state == BUSINESS)
        {
            if(stack.getFirst() == FARM || stack.getFirst() == SOLAR || stack.getFirst() == WILD)
            {
                replaceStack(state);
                return;
            }
            if(stack.getLast() == FARM || stack.getLast() == SOLAR)
                return;
            addToStack(state);
            return;

        }
        if (state == FARM || state == SOLAR)
        {
            if(stack.getLast() == BUSINESS || stack.getLast() == HOUSE)
            {
                addToStack(state);
                return;
            }
            if(stack.getFirst() == BUSINESS || stack.getFirst() == HOUSE)
                return;
            replaceStack(state);
            return;
        }
        if (state == Toolbar.BULLDOZER)
        {
            if (stack.size() == 1)
                replaceStack(Cell.WILD);
            else
                removeTop();
            return;
        }
        addToStack(state);
    }

    public void setGridLocation(int newGridX, int newGridY)
    {
        gridX = newGridX;
        gridY = newGridY;
    }
}
