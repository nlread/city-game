import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Ned Read on 8/6/2014.
 * Main menu bar for the game. Has file menu with new game, open trading window, options, and exit.
 */
public class MainMenuBar extends JMenuBar implements ActionListener {
    //region Private Members
    private MainWindow main;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem startNewGame;
    private JMenuItem deployTradingWindow;
    private JMenuItem deployOptionsWindow;
    private JMenuItem exitProgram;
    //endregion

    public MainMenuBar(MainWindow main) {
        super();
        this.main = main;

        //region Set up MenuBar and MenuItems
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        startNewGame = new JMenuItem("New Game");
        deployTradingWindow = new JMenuItem("Open Trading Window");
        deployOptionsWindow = new JMenuItem("Options");
        exitProgram = new JMenuItem("Exit");
        //endregion

        //region Add ActionListeners
        startNewGame.addActionListener(this);
        deployTradingWindow.addActionListener(this);
        deployOptionsWindow.addActionListener(this);
        exitProgram.addActionListener(this);
        //endregion


        //region Add items to MenuBar
        fileMenu.add(startNewGame);
        fileMenu.add(deployTradingWindow);
        fileMenu.add(deployOptionsWindow);
        fileMenu.add(exitProgram);
        menuBar.add(fileMenu);
        this.add(menuBar);
        //endregion

    }

    /**
     * Preform the task for the given button
     *
     * @param e Event of the action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startNewGame)
            main.getGame().startNewGame();
        else if (e.getSource() == deployTradingWindow)
            main.deployTradingWindow();
        else if (e.getSource() == deployOptionsWindow)
            main.deployOptionsWindow();
        else if (e.getSource() == exitProgram)
            System.exit(0);

    }
}
