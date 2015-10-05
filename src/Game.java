import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

/**
 * Created by Ned Read on 7/7/2014.
 * Main game logic.
 */
public class Game implements MouseListener {
    //region Private Members
    private MainWindow main;
    private Board board;
    private StatusBar statusBar;
    private Toolbar toolbar;
    //endregion

    /**
     * @param main The main window launching the game
     */
    public Game(MainWindow main) {
        this.main = main;
        this.board = main.getBoard();
        this.statusBar = main.getStatusBar();
        updateStats();
        this.toolbar = main.getToolbar();
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        //Convert to Row/Column
        int x = e.getX() / Cell.WIDTH;
        int y = e.getY() / Cell.HEIGHT;

        //Outside of the board
        if (x >= 10 || x < 0 || y >= 10 || y < 0)
            return;

        int state = main.getToolbar().getState();
        board.getGrid()[x][y].replaceStack(state);
        updateStats();
        board.repaint();
    }

    /**
     * Re-calculates the resources and updates the status bar.
     * Contains main resource logic.
     */
    public void updateStats() {
        Cell[][] grid = board.getGrid();

        double wildCredits = 0, farmCredits = 0, energyCredits = 0, houseCredits = 0, businessCredits = 0;
        double[] resourceChanges = new double[5];
        Arrays.fill(resourceChanges, 0);
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                for (Integer state : cell.getStack())
                    switch (state) {
                        case (Cell.WILD):
                            handleWildCell(cell, resourceChanges);
                            break;
                        case (Cell.FARM):
                            handleFarmCell(cell, resourceChanges);
                            break;
                        case (Cell.SOLAR):
                            handleSolarCell(cell, resourceChanges);
                            break;
                        case (Cell.HOUSE):
                            handleHouseCell(cell, resourceChanges);
                            break;
                        case (Cell.BUSINESS):
                            handleBusinessCell(cell, resourceChanges);
                            break;
                    }
            }
        }
        handleGroupedNaturalCells(grid, resourceChanges);

        int[] tradingNetChanges = main.getTradingWindow().getNetChange();
        resourceChanges[Cell.WILD] += tradingNetChanges[Cell.WILD];
        resourceChanges[Cell.FARM] += tradingNetChanges[Cell.FARM];
        resourceChanges[Cell.SOLAR] += tradingNetChanges[Cell.SOLAR];
        resourceChanges[Cell.HOUSE] += tradingNetChanges[Cell.HOUSE];
        resourceChanges[Cell.BUSINESS] += tradingNetChanges[Cell.BUSINESS];
        statusBar.updateStats(resourceChanges);
    }



    /**
     * Calculate resource change for wild cell on board.
     * +1 Wild. +1 Wild if neighboring cell contains a house.
     * @param cell Cell in grid
     * @param resourceChanges Array containing net changes
     */
    public void handleWildCell(Cell cell, double[] resourceChanges) {
        resourceChanges[Cell.WILD]++;
        for (Cell c : cell.getNeighbors())
            if (c.getStack().contains(Cell.HOUSE)) {
                resourceChanges[Cell.WILD]++;
                return;
            }
    }

    /**
     * Calculate resource change for farm cell on board.
     * +1 Farm
     * @param cell Cell in the grid
     * @param resourceChanges Array containing net changes
     */
    public void handleFarmCell(Cell cell, double[] resourceChanges) {
        resourceChanges[Cell.FARM]++;
    }

    /**
     * Calculate resource change for a solar cell on board.
     * +1 Solar
     * @param cell Cell in the grid
     * @param resourceChanges Array containing net changes
     */
    public void handleSolarCell(Cell cell, double[] resourceChanges) {
        resourceChanges[Cell.SOLAR]++;
    }

    /**
     * Calculate resource change for a house cell on board.
     * +1 House. -1 Farm. -2 Wild. -1 Solar. +.5 Solar if next to business.
     * @param cell
     * @param resourceChanges
     */
    public void handleHouseCell(Cell cell, double[] resourceChanges) {
        resourceChanges[Cell.HOUSE]++;
        resourceChanges[Cell.FARM]--;
        resourceChanges[Cell.WILD] -= 2;
        resourceChanges[Cell.SOLAR]--;
        //Check rest of stack for businesses
        if (cell.getStack().contains(Cell.BUSINESS)) {
            resourceChanges[Cell.SOLAR] += .5;
            return;
        }
        //Check neighboring stacks for businesses
        for (Cell c : cell.getNeighbors())
            if (c.getStack().contains(Cell.BUSINESS)) {
                resourceChanges[Cell.SOLAR] += .5;
                return;
            }
    }

    /**
     * Calculates change in resources for a business cell in the board.
     * +1 Business. -4 House. -1 Solar.
     * @param cell Cell in the board
     * @param resourceChanges Array containing net changes.
     */
    private void handleBusinessCell(Cell cell, double[] resourceChanges) {
        resourceChanges[Cell.BUSINESS]++;
        resourceChanges[Cell.HOUSE] -= 4;
        resourceChanges[Cell.SOLAR]--;
    }

    /**
     * Calculates change in resources for groups of natural cells.
     * Also handles resetting the group highlighter.
     * @param grid Grid of cells
     * @param resourceChanges Array of resource changes.
     */
    private void handleGroupedNaturalCells(Cell[][] grid, double[] resourceChanges) {
        boolean[][] naturalCountedFor = new boolean[grid.length][grid[0].length];
        GroupHighlighter gh = board.getGroupHighlighter();
        gh.clearGroups();
        //For each possible size of the group biggest to smallest (4 to 1)
        for (int i = 4; i > 1; i--) {
            //Check for a group at each cell in the grid.
            for (int x = 0; x <= grid.length - i; x++) {
                for (int y = 0; y <= grid[x].length - i; y++) {
                    boolean foundOnlyWild = true;
                    CheckOneSquare:
                    //Validate group of current length at current cell
                    for (int x2 = x; x2 < x + i; x2++) {
                        for (int y2 = y; y2 < y + i; y2++) {
                            //If cell is not wild or is part of another group, this square isn't a group.
                            if (grid[x2][y2].getStack().getLast() != Cell.WILD || naturalCountedFor[x2][y2]) {
                                foundOnlyWild = false;
                                break CheckOneSquare;
                            }
                        }
                    }
                    if (foundOnlyWild) {
                        //Flag each cell so that it cannot be used in another group
                        for (int x2 = x; x2 < x + i; x2++)
                            for (int y2 = y; y2 < y + i; y2++)
                                naturalCountedFor[x2][y2] = true;
                        //Add proper resource amount
                        if (i == 4)
                            resourceChanges[Cell.SOLAR] += 9;
                        else if (i == 3)
                            resourceChanges[Cell.SOLAR] += 4;
                        else if (i == 2)
                            resourceChanges[Cell.SOLAR] += 1;
                        //Add found group to group highlighter
                        gh.addGroup(i, x, y);
                    }
                }
            }
        }
    }

    /**
     * Reset the game.
     */
    public void startNewGame() {
        main.startOver();
        this.board = main.getBoard();
        this.statusBar = main.getStatusBar();
        this.toolbar = main.getToolbar();
        main.repaint();
    }



    //region Unused Events
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    //endregion
}