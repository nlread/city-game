import javax.swing.*;

/**
 * Created by needa_000 on 7/18/2014.
 */
public class StatusAndTradingBar extends JPanel
{
    private MainWindow main;
    private ResourceControlPanel[] controlPanels;
    public StatusAndTradingBar(MainWindow main)
    {
        super();
        this.main = main;

        controlPanels = new ResourceControlPanel[Cell.TILE_NAMES.length];
        for (int i = 0; i < controlPanels.length; i++)
        {
            controlPanels[i] = new ResourceControlPanel(this);
            add(controlPanels[i]);
        }

    }

    public MainWindow getMainWindow()
    {   return main;    }
}
