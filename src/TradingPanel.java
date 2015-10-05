import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created by Ned Read on 8/8/2014.
 * Panel which contains spinners for import and exports of one resource.
 */
public class TradingPanel extends JPanel implements ChangeListener, ActionListener, ComponentListener {

    //region Constants
    public static final Integer TITLE_WIDTH = 59;
    public static final Integer TITLE_HEIGHT = 16;

    public static final int BUY_ONLY = 0;
    public static final int SELL_ONLY = 1;
    public static final int BOTH = 3;
    //endregion

    //region Private Members
    private String resourceName;
    private TradingWindow tradingWindow;

    private JLabel titleLabel;
    private JSpinner importSpinner;
    private JSpinner exportSpinner;
    //endregion

    /**
     * Creates and added the components to the panel
     *
     * @param resourceName  Name of the resource traded
     * @param buySellMode   Trade mode of this resource (Buy only, sell only, both)
     * @param tradingWindow Trading window in which this panel exits.
     */
    public TradingPanel(String resourceName, int buySellMode, TradingWindow tradingWindow) {
        super();
        this.resourceName = resourceName;
        this.tradingWindow = tradingWindow;

        initComponents();
        handleBuySellMode(buySellMode);


        add(titleLabel);
        add(importSpinner);
        add(exportSpinner);
        setMinimumSize(getMinimumSize());
    }

    /**
     * Initializes all of the components in the panel.
     * Does not add them to the panel.
     */
    private void initComponents() {
        titleLabel = new JLabel(resourceName);
        titleLabel.setPreferredSize(new Dimension(TITLE_WIDTH, TITLE_HEIGHT));
        titleLabel.setText(resourceName + ": ");
        titleLabel.addComponentListener(this);

        add(new JSeparator(JSeparator.VERTICAL));

        importSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        importSpinner.addChangeListener(this);
        exportSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        exportSpinner.addChangeListener(this);
    }

    /**
     * Disable the proper spinners given the buySellMode
     *
     * @param buySellMode Buy Sell Mode to match
     */
    private void handleBuySellMode(int buySellMode) {
        if (buySellMode == BUY_ONLY) {
            exportSpinner.setEnabled(false);
            exportSpinner.setVisible(false);
        } else if (buySellMode == SELL_ONLY) {
            importSpinner.setEnabled(false);
            importSpinner.setVisible(false);
        }
    }

    /**
     * Reset the spinners back to 0
     */
    public void reset() {
        importSpinner.setValue(0);
        exportSpinner.setValue(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tradingWindow.valueChanged();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        tradingWindow.valueChanged();
    }


    //region Getters
    public int getImported() {
        return (Integer) importSpinner.getValue();
    }

    public int getExported() {
        return (Integer) exportSpinner.getValue();
    }

    public JSpinner getExportSpinner() {
        return exportSpinner;
    }

    public JSpinner getImportSpinner() {
        return importSpinner;
    }
    //endregion

    //region Unused Events
    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
    //endregion

}
