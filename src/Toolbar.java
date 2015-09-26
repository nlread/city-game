import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by needa_000 on 7/7/2014.
 */
public class Toolbar extends JPanel implements ActionListener
{
    public static final int MOVE = 10;
    public static final String MOVE_NAME = "Move";
    public static final int BULLDOZER = 11;
    public static final String BULLDOZER_NAME = "Bulldozer";
    private JPanel buttonPanel;
    private JButton[] buttons;
    private JLabel infoLabel;
    private JPanel infoPanel;

    private int state;

    public Toolbar()
    {
        super();

        state = 0;

        setLayout(new FlowLayout(FlowLayout.LEFT));

        buttonPanel = new JPanel();
        buttons = new JButton[Cell.TILE_NAMES.length + 2];

        for (int i = 0; i < Cell.TILE_NAMES.length; i++)
            buttons[i] = new JButton(Cell.TILE_NAMES[i]);
        buttons[Cell.TILE_NAMES.length] = new JButton("Move");
        buttons[Cell.TILE_NAMES.length + 1] = new JButton("Bulldozer");

        for(JButton b:buttons)
        {
            b.setContentAreaFilled(false);
            b.addActionListener(this);
        }
        buttons[Cell.WILD].setEnabled(false);

        for (int i = 0; i < buttons.length - 2; i++)
            buttonPanel.add(buttons[i]);
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPanel.add(buttons[buttons.length-2]);
        buttonPanel.add(buttons[buttons.length-1]);

        add(buttonPanel);

        infoPanel = new JPanel();
        infoLabel = new JLabel("ERROR 0");
        infoPanel.add(infoLabel);
        updateInfoLabel();
        add(infoPanel);
    }

    public void updateInfoLabel()
    {
        String info = "ERROR 1";
        switch(state)
        {
            case(Cell.WILD):
                info = "No requirement";
                break;
            case(Cell.FARM):
                info = "No requirement";
                break;
            case(Cell.SOLAR):
                info = "No requirement";
                break;
            case(Cell.HOUSE):
                info = "1 Energy  1 Farm  2 Natural Areas";
                break;
            case(Cell.BUSINESS):
                info = "1 Energy  4 Houses";
                break;
            case(MOVE):
                info = "Allows cells to be dragged";
                break;
            case(BULLDOZER):
                info = "Destroys the top of a stack";
                break;
        }
        infoLabel.setText(info);
        repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(!e.getSource().getClass().equals(JButton.class))
            return;
        JButton b = (JButton) e.getSource();
        if(b.getText().equals(Cell.TILE_NAMES[Cell.WILD]))
            state = Cell.WILD;
        else if(b.getText().equals(Cell.TILE_NAMES[Cell.WILD]))
            state = Cell.FARM;
        else if(b.getText().equals(Cell.TILE_NAMES[Cell.FARM]))
            state = Cell.FARM;
        else if(b.getText().equals(Cell.TILE_NAMES[Cell.SOLAR]))
            state = Cell.SOLAR;
        else if(b.getText().equals(Cell.TILE_NAMES[Cell.HOUSE]))
            state = Cell.HOUSE;
        else if(b.getText().equals(Cell.TILE_NAMES[Cell.BUSINESS]))
            state = Cell.BUSINESS;
        else if(b.getText().equals(Cell.TILE_NAMES[Cell.WILD]))
            state = Cell.WILD;
        else if(b.getText().equals(Toolbar.MOVE_NAME))
            state = Toolbar.MOVE;
        else
            state = Toolbar.BULLDOZER;
        updateInfoLabel();


        for(JButton b2:buttons)
               b2.setEnabled(true);

        b.setEnabled(false);
    }

    public int getState()
    {
        return state;
    }

}
