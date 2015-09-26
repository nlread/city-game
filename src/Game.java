import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by needa_000 on 7/7/2014.
 */
public class Game implements MouseListener
{
    private MainWindow main;
    private Board board;
    private StatusBar statusBar;
    private Toolbar toolbar;

    public Game(MainWindow main)
    {
        this.main = main;
        this.board = main.getBoard();
        this.statusBar = main.getStatusBar();
        updateStats();
        this.toolbar = main.getToolbar();
    }


    @Override
    public void mouseReleased(MouseEvent e)
    {
        int x = e.getX() / Cell.WIDTH;
        int y = e.getY() / Cell.HEIGHT;

        if (x >= 10 || x < 0 || y >= 10 || y < 0)
            return;
        int state = main.getToolbar().getState();
        board.getGrid()[x][y].replaceStack(state);
        updateStats();
        board.repaint();
    }

    public void updateStats()
    {
        Cell[][] grid = board.getGrid();

        double wildCredits = 0, farmCredits = 0, energyCredits = 0, houseCredits = 0, businessCredits = 0;
        for (int x = 0; x < grid.length; x++)
            for (int y = 0; y < grid[x].length; y++)
                for (Integer state : grid[x][y].getStack())
                    switch (state)
                    {
                        case (Cell.WILD):
                            wildCredits++;
                            for (Cell c : grid[x][y].getNeighbors())
                                if (c.getStack().contains(Cell.HOUSE))
                                {
                                    wildCredits++;
                                    break;
                                }
                            break;
                        case (Cell.FARM):
                            farmCredits++;
                            break;
                        case (Cell.SOLAR):
                            energyCredits++;
                            break;
                        case (Cell.HOUSE):
                            houseCredits++;
                            farmCredits--;
                            wildCredits -= 2;
                            energyCredits--;
                            if (grid[x][y].getStack().contains(Cell.BUSINESS))
                            {
                                energyCredits += .5;
                                break;
                            }
                            NeighborSearch:
                            for (Cell c : grid[x][y].getNeighbors())
                                for (Integer state2 : c.getStack())
                                    if (state2 == Cell.BUSINESS)
                                    {
                                        energyCredits += .5;
                                        break NeighborSearch;
                                    }
                            break;
                        case (Cell.BUSINESS):
                            houseCredits -= 4;
                            businessCredits++;
                            energyCredits--;
                            break;
                    }
        boolean[][] naturalCountedFor = new boolean[grid.length][grid[0].length];
        GroupHighlighter gh = board.getGroupHighlighter();
        gh.clearGroups();
        for (int i = 4; i > 1; i--)
            for (int x = 0; x <= grid.length - i; x++)
                for (int y = 0; y <= grid[x].length - i; y++)
                {
                    boolean foundOnlyWild = true;
                    CheckOneSquare:
                    for (int x2 = x; x2 < x + i; x2++)
                        for (int y2 = y; y2 < y + i; y2++)
                            if (grid[x2][y2].getStack().getLast() != Cell.WILD || naturalCountedFor[x2][y2])
                            {
                                foundOnlyWild = false;
                                break CheckOneSquare;
                            }
                    if(foundOnlyWild)
                    {
                        for (int x2 = x; x2 < x + i; x2++)
                            for (int y2 = y; y2 < y + i; y2++)
                                naturalCountedFor[x2][y2] = true;
                        if (i == 4)
                            energyCredits += 9;
                        else if (i == 3)
                            energyCredits += 4;
                        else if (i == 2)
                            energyCredits += 1;
                        gh.addGroup(i,x,y);
                    }
                }

        int[] tradingNetChanges = main.getTradingWindow().getNetChange();
        wildCredits += tradingNetChanges[Cell.WILD];
        farmCredits+= tradingNetChanges[Cell.FARM];
        energyCredits += tradingNetChanges[Cell.SOLAR];
        houseCredits += tradingNetChanges[Cell.HOUSE];
        businessCredits += tradingNetChanges[Cell.BUSINESS];
        statusBar.updateStats(new double[]{wildCredits, farmCredits, energyCredits, houseCredits, businessCredits});
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    public void startNewGame()
    {
        main.startOver();
        this.board = main.getBoard();
        this.statusBar = main.getStatusBar();
        this.toolbar = main.getToolbar();
        main.repaint();
    }
}