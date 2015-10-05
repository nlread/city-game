import javax.swing.*;
import java.awt.*;

/**
 * Created by Ned Read on 7/18/2014.
 * Unused class
 */
public class TradingBar extends JPanel {
    private MainWindow main;
    private JSpinner[] tradingSpinners;

    public TradingBar(MainWindow main) {
        this.main = main;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        tradingSpinners = new JSpinner[Cell.TILE_NAMES.length];
        for (int i = 0; i < tradingSpinners.length; i++) {
            tradingSpinners[i] = new JSpinner();
            tradingSpinners[i].setPreferredSize(new Dimension(Cell.WIDTH, Cell.HEIGHT));
            add(tradingSpinners[i]);
        }
    }
}
