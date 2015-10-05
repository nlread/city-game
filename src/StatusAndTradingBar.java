import javax.swing.*;

/**
 * Created by Ned Read on 7/18/2014.
 * Unused panel. Was going to display how many resources the player had and allow them to apply trades.
 */
public class StatusAndTradingBar extends JPanel {
    private MainWindow main;
    private ResourceControlPanel[] controlPanels;

    public StatusAndTradingBar(MainWindow main) {
        super();
        this.main = main;

        controlPanels = new ResourceControlPanel[Cell.TILE_NAMES.length];
        for (int i = 0; i < controlPanels.length; i++) {
            controlPanels[i] = new ResourceControlPanel(this);
            add(controlPanels[i]);
        }

    }

    public MainWindow getMainWindow() {
        return main;
    }
}
