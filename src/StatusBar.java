import javax.swing.*;
import java.awt.*;

/**
 * Created by Ned Read on 7/8/2014.
 * Panel displaying the amount of each resource the player has.
 * Positive amounts are shown in green, negative amounts are shown in red, and nuetral amoutns are shown in black.
 */
public class StatusBar extends JPanel {
    //region Constants
    public static final Color NEUTRAL_COLOR = Color.BLACK;
    public static final Color POSITIVE_COLOR = new Color(0x266A2E);
    public static final Color NEGATIVE_COLOR = Color.RED;
    //endregion

    //region Private Members
    private MainWindow main;
    private int[] counts;
    private JLabel[] labels;
    private JLabel[] numbers;
    //endregion

    /**
     * Create each of the labels and add them to the panel
     *
     * @param main The MainWindow in which the status bar is placed.
     */
    public StatusBar(MainWindow main) {
        this.main = main;
        counts = new int[Cell.RESOURCE_NAMES.length];
        labels = new JLabel[Cell.RESOURCE_NAMES.length];
        numbers = new JLabel[Cell.RESOURCE_NAMES.length];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel(Cell.RESOURCE_NAMES[i] + " : ");
            add(labels[i]);
            numbers[i] = new JLabel();
            add(numbers[i]);
        }
    }
/*
    public void updateStats()
    {
        for (int i = 0; i < counts.length; i++)
            counts[i] = 0;

        Cell[][] grid = main.getBoard().getGrid();
        for(int x = 0; x < grid.length; x++)
            for(int y = 0; y < grid[x].length; y++)
                counts[grid[x][y].getStack().get(0)]++;

        for (int i = 0; i < labels.length; i++)
            labels[i].setText(Cell.TILE_NAMES[i] + ": " + counts[i]);
    }
    */

    /**
     * Update the resource counts to match the given.
     * Set the labels to display the appropriate color.
     *
     * @param credits New resource counts
     */
    public void updateStats(double[] credits) {
        if (credits.length != numbers.length) {
            System.err.println("Non-matiching arrays");
            return;
        }
        for (int i = 0; i < numbers.length; i++) {
            if (credits[i] == 0)
                numbers[i].setForeground(NEUTRAL_COLOR);
            else if (credits[i] > 0)
                numbers[i].setForeground(POSITIVE_COLOR);
            else
                numbers[i].setForeground(NEGATIVE_COLOR);
            numbers[i].setText("" + credits[i]);
        }
    }
}
