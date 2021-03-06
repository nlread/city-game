import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Created by Ned Read on 7/7/2014.
 * <p/>
 * The panel containing the grid of cells. Handles dragging, highlighting, and adding or replacing a cell.
 */
public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
    //region Constants
    public static final int NUM_COL = 8;
    public static final int NUM_ROW = 5;
    //endregion

    //region Private Members
    private MainWindow main;
    private GroupHighlighter groupHighlighter;
    private BufferedImage backBufferContainer;
    private Graphics2D backBuffer;
    private Cell lastCellHighlighted;

    private Cell[][] grid;

    //region Dragging Private Members
    private boolean isDragging = false;
    private int draggingOffsetX;
    private int draggingOffsetY;
    private Cell beingDragged;
    private Cell swapped;
    private int startingX;
    private int startingY;
    //endregion
    //endregion

    /**
     * Create the board panel. Creates cell grid. Sets all values to the default (Natural)
     *
     * @param main The MainWindow the board is placed in
     */
    public Board(MainWindow main) {
        super();
        this.main = main;
        setSize(NUM_COL * Cell.WIDTH, NUM_ROW * Cell.HEIGHT);
        setPreferredSize(new Dimension(NUM_COL * Cell.WIDTH, NUM_ROW * Cell.HEIGHT));
        setLayout(null);

        groupHighlighter = new GroupHighlighter();

        backBufferContainer = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        backBuffer = (Graphics2D) backBufferContainer.getGraphics();
        grid = new Cell[NUM_COL][NUM_ROW];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y] = new Cell(this, x, y);
                add(grid[x][y]);
            }
        }

        addMouseListener(this);
        addMouseMotionListener(this);
        setVisible(true);
    }

    /**
     * Paints the grid and the cell being dragged. Double Buffered.
     *
     * @param g Graphics from the MainWindow
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        backBuffer.setColor(Color.LIGHT_GRAY);
        backBuffer.fillRect(0, 0, this.getWidth(), this.getHeight());

        drawGrid();

        if (main.getOptionsWindow().shouldDisplayGroups())
            groupHighlighter.paintGroups(backBuffer);
        if (isDragging)
            beingDragged.paint(backBuffer);

        g.drawImage(backBufferContainer, 0, 0, null);
    }

    /**
     * Paint each cell onto the back buffer.
     */
    public void drawGrid() {
        for (int x = 0; x < grid.length; x++)
            for (int y = 0; y < grid[x].length; y++)
                grid[x][y].paint(backBuffer);
    }

    /**
     * Mouse Released Event. Used for handling dragging and changing cell type.
     *
     * @param e Event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (isDragging) {
            isDragging = false;
            beingDragged.setLocation(beingDragged.getGridX() * Cell.WIDTH, beingDragged.getGridY() * Cell.HEIGHT);
            beingDragged.setHighlighted(false);
            beingDragged = null;
            swapped = null;
            grid[startingX][startingY].setHighlighted(false);
            repaint();
            return;
        }

        int x = e.getX() / Cell.WIDTH;
        int y = e.getY() / Cell.HEIGHT;
        if (cordsInGrid(x, y)) {
            int state = main.getToolbar().getState();
            grid[x][y].addOrReplaceStack(state);
            main.getGame().updateStats();
            main.getStackViewer().changeStack(grid[x][y].getStack());
            repaint();
        }
    }

    /**
     * MouseMoved event. Used to handle cell dragging and cell highlighting.
     *
     * @param e Event
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX() / Cell.WIDTH;
        int y = e.getY() / Cell.HEIGHT;

        boolean highlightRepaint = processHighlight(x, y);
        boolean dragRepaint = processDrag(e);

        if (highlightRepaint || dragRepaint)
            repaint();
    }

    /**
     * Handles cell highlighting. Changes the highlighted cell if necessary.
     *
     * @param x Column in grid
     * @param y Row in grid
     * @return Whether or not something changed, indicating the necessity of a repaint.
     */
    public boolean processHighlight(int x, int y) {
        if (isDragging) {
            lastCellHighlighted = null;
            return true;
        }

        if (!cordsInGrid(x, y)) {
            boolean repaint = false;
            if (lastCellHighlighted != null) {
                lastCellHighlighted.setHighlighted(false);
                repaint = true;
            }

            lastCellHighlighted = null;
            return repaint;
        }

        if (lastCellHighlighted == grid[x][y])
            return false;
        if (lastCellHighlighted != null)
            lastCellHighlighted.setHighlighted(false);
        lastCellHighlighted = grid[x][y];
        lastCellHighlighted.setHighlighted(true, Cell.HIGHLIGHT_COLOR);
        main.getStackViewer().changeStack(lastCellHighlighted.getStack());
        return true;
    }

    /**
     * Handles the dragging of a cell. Swaps the cells if drags over a different cell.
     *
     * @param e Event
     * @return Whether or not a repaint is necessary.
     */
    public boolean processDrag(MouseEvent e) {
        if (!isDragging)
            return false;

        int x = e.getX() / Cell.WIDTH;
        int y = e.getY() / Cell.HEIGHT;

        if (!cordsInGrid(x, y))
            return false;

        if (!grid[x][y].equals(beingDragged)) {
            if (swapped != null)
                swapped.setHighlighted(false);

            swapped = grid[x][y];
            swapped.setHighlighted(true, Cell.STARTING_COLOR);

            if (swapped != null)  //If not swapped
            {
                grid[startingX][startingY].setHighlighted(false);
                if (!(x == startingX && y == startingY)) {
                    swapTiles(startingX, startingY, x, y);
                }
            }
            swapTiles(x, y, beingDragged.getGridX(), beingDragged.getGridY());
            main.getGame().updateStats();
        }
        beingDragged.setHighlighted(true, Cell.DRAGGED_COLOR);
        beingDragged.setLocation(e.getX() - draggingOffsetX, e.getY() - draggingOffsetY);
        return true;

    }

    /**
     * Swaps the cells at the given coordinates.
     *
     * @param x  Column of first cell
     * @param y  Row of first cell
     * @param x2 Column of second cell
     * @param y2 Row of second cell
     */
    public void swapTiles(int x, int y, int x2, int y2) {
        Cell temp = grid[x][y];
        grid[x][y] = grid[x2][y2];
        grid[x][y].setGridLocation(x, y);
        grid[x][y].setLocation(x * Cell.WIDTH, y * Cell.HEIGHT);
        grid[x2][y2] = temp;
        grid[x2][y2].setGridLocation(x2, y2);
        grid[x2][y2].setLocation(x2 * Cell.WIDTH, y2 * Cell.HEIGHT);
    }

    /**
     * Used for ensuring coordinates are in the grid
     *
     * @param x Column of cell
     * @param y Row of cell
     * @return Whether the given coordinates are within the grid or not
     */
    public boolean cordsInGrid(int x, int y) {
        if (x >= grid.length || x < 0)
            return false;
        if (y >= grid[x].length || y < 0)
            return false;
        return true;
    }

    /**
     * Used to handle highlighting. Unhighlights last cell when the mouse leaves the board.
     *
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {
        if (lastCellHighlighted != null)
            lastCellHighlighted.setHighlighted(false);
        lastCellHighlighted = null;
        repaint();
    }


    /**
     * Used in handling dragging. Used to begin the drag.
     *
     * @param e Event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (!isDragging && main.getToolbar().getState() == Toolbar.MOVE) {
            isDragging = true;
            beingDragged = grid[e.getX() / Cell.WIDTH][e.getY() / Cell.HEIGHT];
            startingX = beingDragged.getGridX();
            startingY = beingDragged.getGridY();
            draggingOffsetX = e.getX() - beingDragged.getX();
            draggingOffsetY = e.getY() - beingDragged.getY();

            grid[startingX][startingY].setHighlighted(true, Cell.STARTING_COLOR);
        }
    }


    /**
     * Redraw the image on all of the cells.
     */
    public void updateAllCells() {
        for (Cell[] col : grid)
            for (Cell c : col) c.updateImage();
    }

    /**
     * Reset all of the cells back to the default: Only one wild.
     */
    public void reset() {
        for (Cell[] r : grid)
            for (Cell c : r)
                c.replaceStack(Cell.WILD);
    }

    //region Getters
    public Cell[][] getGrid() {
        return grid;
    }

    public GroupHighlighter getGroupHighlighter() {
        return groupHighlighter;
    }

    public MainWindow getMain() {
        return main;
    }
    //endregion

    //region Unused Events
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }
    //endregion
}
