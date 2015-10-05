import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created by needa_000 on 8/8/2014.
 */
public class TradingWindow extends JFrame implements ComponentListener {
    public static final String IMPORT_TEXT = "In";
    public static final String EXPORT_TEXT = "Out";
    private TradingPanel[] tradingPanels;
    private JLabel importLabel;
    private JLabel exportLabel;
    private MainWindow main;

    public TradingWindow(MainWindow main) {
        super();
        this.main = main;
        setTitle("Trading Window");
        addComponentListener(this);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        setResizable(false);

        initTitle();
        initTradingPanels();

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        pack();
        setMinimumSize(getMinimumSize());
    }

    private void initTitle() {
        JPanel titlePanel = new JPanel();
        importLabel = new JLabel(IMPORT_TEXT);
        exportLabel = new JLabel(EXPORT_TEXT);
        titlePanel.add(importLabel);
        titlePanel.add(exportLabel);
        add(titlePanel);
    }

    private void initTradingPanels() {
        tradingPanels = new TradingPanel[Cell.RESOURCE_NAMES.length + 1];
        for (int i = 0; i < Cell.RESOURCE_NAMES.length; i++) {
            tradingPanels[i] = new TradingPanel((Cell.RESOURCE_NAMES[i]), TradingPanel.BOTH, this);
            add(tradingPanels[i]);
        }
        tradingPanels[tradingPanels.length - 1] = new TradingPanel("Exxonia", TradingPanel.BUY_ONLY, this);
        add(tradingPanels[tradingPanels.length - 1]);
    }

    public void valueChanged() {
        main.getGame().updateStats();
    }

    public int[] getImports() {
        int[] imports = new int[tradingPanels.length];
        for (int i = 0; i < tradingPanels.length; i++) {
            imports[i] = tradingPanels[i].getImported();
        }
        return imports;
    }

    public int[] getExports() {
        int[] exports = new int[tradingPanels.length];
        for (int i = 0; i < tradingPanels.length; i++) {
            exports[i] = tradingPanels[i].getExported();
        }
        return exports;
    }

    public int[] getNetChange() {
        int[] netChanges = new int[tradingPanels.length];
        for (int i = 0; i < tradingPanels.length; i++) {
            netChanges[i] = tradingPanels[i].getImported() - tradingPanels[i].getExported();
        }
        return netChanges;
    }

    public void reset() {
        for (TradingPanel tp : tradingPanels)
            tp.reset();
    }

    @Override
    public void componentShown(ComponentEvent e) {
        importLabel.setLocation(tradingPanels[0].getImportSpinner().getX(), importLabel.getY());
        exportLabel.setLocation(tradingPanels[0].getExportSpinner().getX(), importLabel.getY());
    }

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
