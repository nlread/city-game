import javax.swing.*;
import java.awt.*;

/**
 * Created by Ned Read on 7/18/2014.
 * Unused, was going to contain spinners for trading, now in trading window.
 */
public class ResourceControlPanel extends JPanel
{
    private JLabel nameLabel;
    private JLabel amountLabel;
    private JSpinner tradingSpinner;

    private StatusAndTradingBar sAndTBar;

    private int amount;

    public ResourceControlPanel(StatusAndTradingBar sAndTBar)
    {
        super();
        this.sAndTBar = sAndTBar;

        nameLabel = new JLabel("Name");

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel,BoxLayout.PAGE_AXIS));
        amountLabel = new JLabel("" + 0);
        tradingSpinner = new JSpinner(new SpinnerNumberModel(0,-999,999,1));
        tradingSpinner.setPreferredSize(new Dimension(sAndTBar.getMainWindow().getBoard().getWidth()/15,30));
        tradingSpinner.setFont(new Font("Times New Roman",Font.PLAIN,20));
        statusPanel.add(amountLabel);
        statusPanel.add(tradingSpinner);

        add(nameLabel);
        add(statusPanel);
    }



}
