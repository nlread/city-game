import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by needa_000 on 8/18/2014.
 */
public class OptionsWindow extends JFrame implements ActionListener
{
    private MainWindow main;
    private JCheckBox displayGroupsBox;
    private JCheckBox useImagesBox;
    private JCheckBox displayGridBox;
    public OptionsWindow(MainWindow main)
    {
        super("Options");
        this.main =  main;
        getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(3,1));
        JLabel titleLabel = new JLabel("Options");
      //  JPanel titlePanel = new JPanel();
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        //titlePanel.add(titleLabel);

        displayGroupsBox = new JCheckBox("Display Natural Groupings",false);
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

    public boolean shouldDisplayGroups()
    {
        return displayGroupsBox.isSelected();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == displayGroupsBox)
            main.getBoard().repaint();
        else if(e.getSource() == useImagesBox)
        {
            Cell[][] grid = main.getBoard().getGrid();
            for(int x = 0; x< grid.length; x++)
                for (int y = 0; y< grid[y].length; y++)
                    grid[x][y].updateImage();
            main.getBoard().repaint();
        }
        else if(e.getSource() == displayGridBox)
        {
            main.getBoard().updateAllCells();
            main.getBoard().repaint();
        }
    }

    public boolean shouldDisplayImages()
    {
        return useImagesBox.isSelected();
    }
    public boolean shouldDisplayGrid() { return displayGridBox.isSelected();}
}
