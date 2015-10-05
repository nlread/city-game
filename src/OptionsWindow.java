import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Ned Read on 8/18/2014.
 * Window containing the options for the program.
 */
public class OptionsWindow extends JFrame implements ActionListener {
    //region Private Members
    private MainWindow main;
    private JCheckBox displayGroupsBox;
    private JCheckBox useImagesBox;
    private JCheckBox displayGridBox;
    //endregion

    /**
     * Creates and adds the components to the window.
     *
     * @param main MainWindow creating the Options Window
     */
    public OptionsWindow(MainWindow main) {
        super("Options");
        this.main = main;
        getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(3, 1));
        JLabel titleLabel = new JLabel("Options");
        //  JPanel titlePanel = new JPanel();
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        //titlePanel.add(titleLabel);

        displayGroupsBox = new JCheckBox("Display Natural Groupings", false);
        displayGroupsBox.setAlignmentX(LEFT_ALIGNMENT);
        displayGroupsBox.addActionListener(this);

        useImagesBox = new JCheckBox("Use Cell Images", false);
        useImagesBox.setAlignmentX(LEFT_ALIGNMENT);
        useImagesBox.addActionListener(this);


        displayGridBox = new JCheckBox("Display Grid", true);
        displayGridBox.setAlignmentX(LEFT_ALIGNMENT);
        displayGridBox.addActionListener(this);

        optionsPanel.add(displayGroupsBox);
        optionsPanel.add(useImagesBox);
        optionsPanel.add(displayGridBox);

        add(titleLabel);
        add(optionsPanel);
        //   add(displayGroupsBox);
        //   add(useImagesBox);
        //   add(displayGridBox);

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        pack();
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
    }

    /**
     * Handles the options being changed
     *
     * @param e Action Event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //MainWindow gets the value from the options window when painting
        if (e.getSource() == displayGroupsBox)
            main.getBoard().repaint();
        else if (e.getSource() == useImagesBox) {
            Cell[][] grid = main.getBoard().getGrid();
            for (int x = 0; x < grid.length; x++)
                for (int y = 0; y < grid[y].length; y++)
                    grid[x][y].updateImage();
            main.getBoard().repaint();
        } else if (e.getSource() == displayGridBox) {
            main.getBoard().updateAllCells();
            main.getBoard().repaint();
        }
    }

    /**
     * @return Whether or not natural groups should be highlighted
     */
    public boolean shouldDisplayGroups() {
        return displayGroupsBox.isSelected();
    }

    /**
     * @return Whether or not images or simple colors should be used
     */
    public boolean shouldDisplayImages() {
        return useImagesBox.isSelected();
    }

    /**
     * @return Whether or not the borders of each cell should be displayed
     */
    public boolean shouldDisplayGrid() {
        return displayGridBox.isSelected();
    }
}
