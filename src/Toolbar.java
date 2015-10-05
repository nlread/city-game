import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Ned Read on 7/7/2014.
 * Panel containing the buttons for switching the active tool (Place House, Place Farm, Bulldozer, Move, etc)
 * Buttons are set to inactive when selected to indicate to the player which tool is selected (Button becomes greyed
 * out when set to inactive). A short description of each tool is displayed. Move and Bulldozer are descriptions of
 * what the tool does while the placement tools show the requirement to place the selected object (ie 4 houses and 1
 * energy for a business).
 */
public class Toolbar extends JPanel implements ActionListener {
    //region Constants
    public static final int MOVE = 10;
    public static final String MOVE_NAME = "Move";
    public static final int BULLDOZER = 11;
    public static final String BULLDOZER_NAME = "Bulldozer";

    public static final String WILD_INFO = "No requirement";
    public static final String FARM_INFO = "No requirement";
    public static final String SOLAR_INFO = "No requirement";
    public static final String HOUSE_INFO = "1 Energy  1 Farm  2 Natural Areas";
    public static final String BUSINESS_INFO = "1 Energy  4 Houses";
    public static final String MOVE_INFO = "Allows cells to be dragged";
    public static final String BULLDOZER_INFO = "Destroys the top of a stack";
    //endregion

    //region Private Members
    private JPanel buttonPanel;
    private JButton[] buttons;
    private JLabel infoLabel;
    private JPanel infoPanel;

    private int state;
    //endregion

    /**
     * Create each of the buttons with the appropriate text. Bulldozer and Move text are defined in this class
     * while the placement tools text match that in the Cell class. A gap is added between the placement
     * and move/bulldozer tools.
     */
    public Toolbar() {
        super();

        state = 0;

        setLayout(new FlowLayout(FlowLayout.LEFT));

        buttonPanel = new JPanel();
        buttons = new JButton[Cell.TILE_NAMES.length + 2];

        //Create the buttons with appropriate text
        for (int i = 0; i < Cell.TILE_NAMES.length; i++)
            buttons[i] = new JButton(Cell.TILE_NAMES[i]);
        buttons[Cell.TILE_NAMES.length] = new JButton(MOVE_NAME);
        buttons[Cell.TILE_NAMES.length + 1] = new JButton(BULLDOZER_NAME);

        //Add action listeners and minor appearance details
        for (JButton b : buttons) {
            b.setContentAreaFilled(false);
            b.addActionListener(this);
        }
        buttons[Cell.WILD].setEnabled(false);

        //Add buttons to the panel
        for (int i = 0; i < buttons.length - 2; i++)
            buttonPanel.add(buttons[i]);
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPanel.add(buttons[buttons.length - 2]);
        buttonPanel.add(buttons[buttons.length - 1]);

        add(buttonPanel);

        //Add the information panel. Error 0 displayed be default. Should be changed right away when updateInfoLablel
        //called. 0 means error from the beginning.
        infoPanel = new JPanel();
        infoLabel = new JLabel("ERROR 0");
        infoPanel.add(infoLabel);
        updateInfoLabel();
        add(infoPanel);
    }

    /**
     * Updates what should be displayed on the info panel. Displays ERROR 1 when the state does not have corresponding
     * information. 1 means error later in program not at the beginning.
     */
    public void updateInfoLabel() {
        String info = "ERROR 1";
        switch (state) {
            case (Cell.WILD):
                info = WILD_INFO;
                break;
            case (Cell.FARM):
                info = FARM_INFO;
                break;
            case (Cell.SOLAR):
                info = SOLAR_INFO;
                break;
            case (Cell.HOUSE):
                info = HOUSE_INFO;
                break;
            case (Cell.BUSINESS):
                info = BUSINESS_INFO;
                break;
            case (MOVE):
                info = MOVE_INFO;
                break;
            case (BULLDOZER):
                info = BULLDOZER_INFO;
                break;
        }
        infoLabel.setText(info);
        repaint();
    }

    /**
     * Handle each of the button presses. Set the button to be inactive and update the state and displayed info
     *
     * @param e Event from the button
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!e.getSource().getClass().equals(JButton.class))
            return;
        JButton b = (JButton) e.getSource();
        String text = b.getText();
        if (text.equals(Cell.TILE_NAMES[Cell.WILD]))
            state = Cell.WILD;
        else if (text.equals(Cell.TILE_NAMES[Cell.WILD]))
            state = Cell.FARM;
        else if (text.equals(Cell.TILE_NAMES[Cell.FARM]))
            state = Cell.FARM;
        else if (text.equals(Cell.TILE_NAMES[Cell.SOLAR]))
            state = Cell.SOLAR;
        else if (text.equals(Cell.TILE_NAMES[Cell.HOUSE]))
            state = Cell.HOUSE;
        else if (text.equals(Cell.TILE_NAMES[Cell.BUSINESS]))
            state = Cell.BUSINESS;
        else if (text.equals(Cell.TILE_NAMES[Cell.WILD]))
            state = Cell.WILD;
        else if (text.equals(Toolbar.MOVE_NAME))
            state = Toolbar.MOVE;
        else
            state = Toolbar.BULLDOZER;
        updateInfoLabel();

        for (JButton b2 : buttons)
            b2.setEnabled(true);

        b.setEnabled(false);
    }

    //region Getters
    public int getState() {
        return state;
    }
    //endregion
}
