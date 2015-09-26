import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by needa_000 on 8/6/2014.
 */
public class MyMenuBar extends JMenuBar implements ActionListener
{
    private MainWindow main;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem startNewGame;
    private JMenuItem deployTradingWindow;
    private JMenuItem deployOptionsWindow;
    private JMenuItem exitProgram;
    public MyMenuBar(MainWindow main)
    {
        super();
        this.main = main;

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        startNewGame = new JMenuItem("New Game");
        deployTradingWindow = new JMenuItem("Open Trading Window");
        deployOptionsWindow = new JMenuItem("Options");
        exitProgram = new JMenuItem("Exit");

        startNewGame.addActionListener(this);
        deployTradingWindow.addActionListener(this);
        deployOptionsWindow.addActionListener(this);
        exitProgram.addActionListener(this);

        fileMenu.add(startNewGame);
        fileMenu.add(deployTradingWindow);
        fileMenu.add(deployOptionsWindow);
        fileMenu.add(exitProgram);
        menuBar.add(fileMenu);

        this.add(menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == startNewGame)
            main.getGame().startNewGame();
        else if(e.getSource() == deployTradingWindow)
            main.deployTradingWindow();
        else if(e.getSource() == deployOptionsWindow)
            main.deployOptionsWindow();
        else if(e.getSource() == exitProgram)
            System.exit(0);

    }
}
