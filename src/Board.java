import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Created by needa_000 on 7/7/2014.
 */
public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener
{
    public static final int NUM_COL = 8;
    public static final int NUM_ROW = 5;
    private MainWindow main;
    private GroupHighlighter groupHighlighter;
    private BufferedImage backBufferContainer;
    private Graphics2D backBuffer;

    private Cell[][] grid;

    public Board(MainWindow main)
    {
        super();
        this.main = main;
        setSize(NUM_COL * Cell.WIDTH, NUM_ROW * Cell.HEIGHT);
        setPreferredSize(new Dimension(NUM_COL * Cell.WIDTH, NUM_ROW * Cell.HEIGHT));
        setLayout(null);

        groupHighlighter = new GroupHighlighter();

        backBufferContainer = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        backBuffer = (Graphics2D) backBufferContainer.getGraphics();
        System.out.println(backBuffer.getClass());
        grid = new Cell[NUM_COL][NUM_ROW];
        for (int x = 0; x < grid.length; x++)
        {
            for (int y = 0; y < grid[x].length; y++)
            {
                grid[x][y] = new Cell(this, x, y);
                add(grid[x][y]);
            }
        }

        addMouseListener(this);
        addMouseMotionListener(this);
        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        long currentTime = System.currentTimeMillis();
        backBuffer.setColor(Color.LIGHT_GRAY);
        backBuffer.fillRect(0, 0, this.getWidth(), this.getHeight());

        drawGrid();

        if (main.getOptionsWindow().shouldDisplayGroups())
            groupHighlighter.paintGroups(backBuffer);
        if (isDragging)
            beingDragged.paint(backBuffer);

        g.drawImage(backBufferContainer, 0, 0, null);
        System.out.println("Time to paint board: " + (System.currentTimeMillis() - currentTime));

    }

    public void drawGrid()
    {
        for (int x = 0; x < grid.length; x++)
            for (int y = 0; y < grid[x].length; y++)
                grid[x][y].paint(backBuffer);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (isDragging)
        {
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
        if (cordsInGrid(x, y))
        {
            int state = main.getToolbar().getState();
            grid[x][y].addOrReplaceStack(state);
            main.getGame().updateStats();
            main.getStackViewer().changeStack(grid[x][y].getStack());
            repaint();
        }
    }


    private Cell lastCellHighlighted;

    @Override
    public void mouseMoved(MouseEvent e)
    {
        int x = e.getX() / Cell.WIDTH;
        int y = e.getY() / Cell.HEIGHT;

        boolean highlightRepaint = processHighlight(x, y);
        boolean dragRepaint = processDrag(e);

        if (highlightRepaint || dragRepaint)
            repaint();
    }

    public boolean processHighlight(int x, int y)
    {
        if (isDragging)
        {
            lastCellHighlighted = null;
            return true;
        }

        if (!cordsInGrid(x, y))
        {
            boolean repaint = false;
            if (lastCellHighlighted != null)
            {
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

    public boolean processDrag(MouseEvent e)
    {
        if (!isDragging)
            return false;

        int x = e.getX() / Cell.WIDTH;
        int y = e.getY() / Cell.HEIGHT;
        if (!cordsInGrid(x, y))
            return false;
        if (!grid[x][y].equals(beingDragged))
        {
            if (swapped != null)
                swapped.setHighlighted(false);

            swappedX = x;
            swappedY = y;
            swapped = grid[x][y];
            swapped.setHighlighted(true, Cell.STARTING_COLOR);

            if (swapped != null)  //If not swapped
            {
                grid[startingX][startingY].setHighlighted(false);
                if (!(x == startingX && y == startingY))
                {
                    swapTiles(startingX, startingY, x, y);
                    //grid[startingX][startingY].setHighlighted(true, Cell.STARTING_COLOR);
                }
            }
            swapTiles(x, y, beingDragged.getGridX(), beingDragged.getGridY());
            main.getGame().updateStats();
        }
        beingDragged.setHighlighted(true, Cell.DRAGGED_COLOR);
        beingDragged.setLocation(e.getX() - draggingOffsetX, e.getY() - draggingOffsetY);
        return true;

    }

    public void swapTiles(int x, int y, int x2, int y2)
    {
        Cell temp = grid[x][y];
        grid[x][y] = grid[x2][y2];
        grid[x][y].setGridLocation(x, y);
        grid[x][y].setLocation(x * Cell.WIDTH, y * Cell.HEIGHT);
        grid[x2][y2] = temp;
        grid[x2][y2].setGridLocation(x2, y2);
        grid[x2][y2].setLocation(x2 * Cell.WIDTH, y2 * Cell.HEIGHT);
    }

    public boolean cordsInGrid(int x, int y)
    {
        if (x >= grid.length || x < 0)
            return false;
        if (y >= grid[x].length || y < 0)
            return false;
        return true;
    }


    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        if (lastCellHighlighted != null)
            lastCellHighlighted.setHighlighted(false);
        lastCellHighlighted = null;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (!isDragging && main.getToolbar().getState() == Toolbar.MOVE)
        {
            isDragging = true;
            beingDragged = grid[e.getX() / Cell.WIDTH][e.getY() / Cell.HEIGHT];
            startingX = beingDragged.getGridX();
            startingY = beingDragged.getGridY();
            draggingOffsetX = e.getX() - beingDragged.getX();
            draggingOffsetY = e.getY() - beingDragged.getY();

            grid[startingX][startingY].setHighlighted(true, Cell.STARTING_COLOR);
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
    }

    public static void pause(long millis)
    {
        try
        {
            Thread.sleep(millis);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private boolean isDragging = false;
    private int draggingOffsetX;
    private int draggingOffsetY;
    private Cell beingDragged;
    private Cell swapped;
    private int swappedX;
    private int swappedY;
    private int startingX;
    private int startingY;


    public Cell[][] getGrid()
    {
        return grid;
    }


    public void reset()
    {
        for (Cell[] r : grid)
            for (Cell c : r)
                c.replaceStack(Cell.WILD);
    }

    public GroupHighlighter getGroupHighlighter()
    {
        return groupHighlighter;
    }

    public MainWindow getMain()
    {
        return main;
    }

    public void updateAllCells()
    {
        for (Cell[] col : grid)
            for (Cell c : col) c.updateImage();
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
    }
}
