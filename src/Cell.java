import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Ned Read on 7/6/2014.
 * Represents one cell on the board. Has methods for adding and removing to/from the stack.
 * Generates the image for the cell, handling the border, image/color, and the stack label.
 */
public class Cell extends JPanel {
    //region Constants
    public static final String[] TILE_NAMES = {"Natural", "Farm", "Solar", "House", "Business"};
    public static final String[] RESOURCE_NAMES = {"Natural", "Food", "Energy", "Housing", "Money"};
    public static final int WILD = 0;
    public static final int FARM = 1;
    public static final int SOLAR = 2;
    public static final int HOUSE = 3;
    public static final int BUSINESS = 4;


    public static final BufferedImage[] IMAGES;
    //public static final Color[] COLORS = {new Color(0x013220), new Color(0x8CC739), new Color(0x20B2AA), new Color(Color.YELLOW.getRGB()), new Color(0x999999)}; //Grey's Colors
    public static final Color[] COLORS = {new Color(0x013220), new Color(0xCCCC00), new Color(0x20B2AA), new Color(0x8CC739), new Color(0x0F3B5F)}; //Sim City Colors

    public static final Font LABEL_FONT;
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


    static {
        IMAGES = new BufferedImage[TILE_NAMES.length];
        for (int i = 0; i < IMAGES.length; i++) {
            BufferedImage image = DataLoader.loadImage(TILE_NAMES[i].toLowerCase() + ".jpg");
            IMAGES[i] = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            IMAGES[i].getGraphics().drawImage(image.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH), 0, 0, null);
        }

        LABEL_FONT = new Font("Verdana", Font.BOLD, LABEL_SIZE);
    }
    //endregion

    //region Private Members
    private BufferedImage onScreenImage;
    private JLabel stackLabel;
    private Board board;
    private LinkedList<Integer> stack;
    private int gridX;
    private int gridY;
    private boolean isHighlighted;
    private Color highlightColor;
    //endregion

    /**
     * Creates a cell with the default state (Natural) at the given coordinates.
     *
     * @param board Board the cell is created in
     * @param gridX Column in board
     * @param gridY Row in board
     */
    public Cell(Board board, int gridX, int gridY) {
        this(board, Cell.WILD, gridX, gridY);
    }

    /**
     * Creates a cell with the given state at the given coordinates.
     *
     * @param board Board the cell is created in
     * @param state Beginning state for the cell.
     * @param gridX Column in board
     * @param gridY Row in board
     */
    public Cell(Board board, int state, int gridX, int gridY) {
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

    /**
     * Replace the cell's stack with the given state
     *
     * @param newState The state to replace the stack with
     */
    public void replaceStack(int newState) {
        if (newState < 0 || newState > 4) {
            System.out.println("Invalid State");
            return;
        }
        stack.clear();
        stack.add(newState);
        updateImage();
    }

    /**
     * Re-draw the image displaced on the board for this cell
     */
    public void updateImage() {
        Graphics2D g = (Graphics2D) onScreenImage.getGraphics();

        if (board.getMain().getOptionsWindow().shouldDisplayImages())
            drawCellImage(g);
        else
            drawCellColor(g);

        drawCellBorder(g);
        drawCellStackLabel(g);
    }


    /**
     * Re-draws the cell boarder.
     */
    public void drawCellBorder() {
        Graphics2D g = (Graphics2D) onScreenImage.getGraphics();
        drawCellBorder(g);
    }

    /**
     * Draws the cell boarder on the given graphics object.
     * Uses the appropriate color if highlighted.
     *
     * @param g Graphic object to draw the boarder on
     */
    private void drawCellBorder(Graphics2D g) {
        g.setStroke(new BasicStroke(BORDER_SIZE));
        if (isHighlighted)
            g.setColor(highlightColor);
        else {
            if (!board.getMain().getOptionsWindow().shouldDisplayGrid())
                return;
            g.setColor(DEFAULT_BORDER_COLOR);
        }
        g.drawRect((int) (BORDER_SIZE / 2), (int) (BORDER_SIZE / 2), WIDTH - BORDER_SIZE, HEIGHT - BORDER_SIZE);
    }

    /**
     * Draws a rectangle with the cells color on the given Graphic object
     * Used when the the application is not displaying cell images
     *
     * @param g Graphic object to draw the rectangle on
     */
    private void drawCellColor(Graphics2D g) {
        g.setColor(COLORS[stack.getFirst()]);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        if (stack.size() >= 2) {
            int rectWidth = (int) (1.0 * WIDTH * STACK_DISPLAY_PERCENT);
            int rectHeight = (int) (1.0 * HEIGHT * STACK_DISPLAY_PERCENT);
            g.setColor(COLORS[stack.getLast()]);
            g.fillRect(0, 0, rectWidth, rectHeight);

        }
    }

    /**
     * Draws the cell images on the given graphic object
     * Used when the the application is displaying cell images
     *
     * @param g Graphic object to draw the image on
     */
    private void drawCellImage(Graphics2D g) {
        g.drawImage(IMAGES[stack.getLast()], 0, 0, null);
    }

    /**
     * Draws the stack label on the given graphic object.
     * Only draws a label if the stack is greater than or equal to 2.
     * Draws "X<Stack Height>"
     *
     * @param g
     */
    private void drawCellStackLabel(Graphics2D g) {
        if (stack.size() >= 2) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(LABEL_FONT);
            g.setColor(LABEL_COLOR);
            g.drawString("X" + stack.size(), LABEL_WIDTH_OFFSET, LABEL_HEIGHT_OFFSET);

        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(onScreenImage, getX(), getY(), null);
    }

    /**
     * Remove the top from the stack. Does nothing if the stack only contains one element
     * Updates the image accordingly.
     */
    public void removeTop() {
        if (stack.size() == 0) {
            System.err.println("Stack empty!!");
            return;
        } else if (stack.size() == 1) {
            System.err.println("Stack only contains one element!!");
            return;
        }
        stack.remove(stack.size() - 1);
        updateImage();
    }


    /**
     * Adds the current state to the stack or replaces the stack with the current state, depending on the stack
     * and the state being added. Houses and Businesses can be stacked and capped with a Farm or Solar. Does not
     * replace a capped stack if one tried to add another farm or solar or house or business to it. Bulldozer removed
     * one element from the stack, and replaces the final element with a natural area.
     *
     * @param state State to add or replace
     */
    public void addOrReplaceStack(int state) {
        if (state == Toolbar.MOVE)
            return;
        if (state == WILD) //Natural Area's cannot be stacked
        {
            replaceStack(state);
            return;
        }
        if (state == HOUSE || state == BUSINESS) {
            if (stack.getFirst() == FARM || stack.getFirst() == SOLAR || stack.getFirst() == WILD) //House and Business cannot go on top of Farm, Solar, or Natural
            {
                replaceStack(state);
                return;
            }
            if (stack.getLast() == FARM || stack.getLast() == SOLAR) //Business or House Capped with Farm or Solar: Don't replace
                return;
            addToStack(state);
            return;

        }
        if (state == FARM || state == SOLAR) {
            if (stack.getLast() == BUSINESS || stack.getLast() == HOUSE) //Farm and Solar can be placed on top of Business or House
            {
                addToStack(state);
                return;
            }
            if (stack.getFirst() == BUSINESS || stack.getFirst() == HOUSE) //Don't replace a stack which is already capped with Farm or Solar
                return;
            replaceStack(state);
            return;
        }
        if (state == Toolbar.BULLDOZER) //Delete on from stack or replace with Natural
        {
            if (stack.size() == 1)
                replaceStack(Cell.WILD);
            else
                removeTop();
            return;
        }
        addToStack(state);
    }

    /**
     * Add the given state to the stack. Does not check if the state is valid or can be placed on the current
     * stack
     *
     * @param state State to add to the stack.
     */
    public void addToStack(int state) {
        stack.add(state);
        updateImage();
    }

    //region Getters

    /**
     * The image that should be displayed onscreen for the cell
     *
     * @return
     */
    public BufferedImage getOnScreenImage() {
        return onScreenImage;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public LinkedList<Integer> getStack() {
        return stack;
    }

    public ArrayList<Cell> getNeighbors() {
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


    //endregion

    //region Setters
    public void setHighlighted(boolean isHighlighted, Color highlightColor) {
        this.isHighlighted = isHighlighted;
        this.highlightColor = highlightColor;
        drawCellBorder();
    }


    public void setHighlighted(boolean isHighlighted) {
        setHighlighted(isHighlighted, this.highlightColor == null ? HIGHLIGHT_COLOR : this.highlightColor);
    }

    public void setGridLocation(int newGridX, int newGridY) {
        gridX = newGridX;
        gridY = newGridY;
    }
    //endregion
}
