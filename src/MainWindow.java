import javax.swing.*;
import java.awt.*;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;


/**
 * Created by needa_000 on 7/6/2014.
 */
public class MainWindow extends JFrame
{
    private Game game;

    public static void main(String [] args)
    {
        MainWindow window = new MainWindow();
        window.pack();
    }

    private Board board;
    private Toolbar toolbar;
    private StatusBar statusBar;
    private TradingWindow tradingWindow;
    private OptionsWindow optionsWindow;

    private StackViewer stackViewer;

    public MainWindow()
    {
        super();
        setJMenuBar(new MyMenuBar(this));

       initGame();
        addComponents();


        this.game = new Game(this);

        setTitle("Sustainability Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
    }

    public void startOver()
    {
        board.reset();
        tradingWindow.reset();
        game.updateStats();
    }

    private void initGame()
    {
        toolbar = new Toolbar();
        statusBar = new StatusBar(this);
        stackViewer = new StackViewer(this);
        tradingWindow = new TradingWindow(this);
        optionsWindow = new OptionsWindow(this);

        board = new Board(this);
        addKeyListener(board);
        repaint();
    }

    private void addComponents()
    {
        setLayout(new BorderLayout());
        add(board, BorderLayout.CENTER);
        add(toolbar, BorderLayout.NORTH);
        add(statusBar, BorderLayout.SOUTH);
        add(stackViewer, BorderLayout.EAST);
        repaint();
    }



    public static void pause(long millis)
    {
        try
        {
            Thread.sleep(millis);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public Toolbar getToolbar()
    {
        return toolbar;
    }

    public Board getBoard()
    {
        return board;
    }

    public StatusBar getStatusBar()
    {
        return statusBar;
    }

    public StackViewer getStackViewer(){ return stackViewer;}

    public Game getGame()
    {
        return game;
    }

    public void deployTradingWindow()
    {
        tradingWindow.setVisible(true);
    }

    public TradingWindow getTradingWindow()
    {
        return tradingWindow;
    }

    public OptionsWindow getOptionsWindow()
    {
        return optionsWindow;
    }

    public void deployOptionsWindow()
    {
        optionsWindow.setVisible(true);
    }
}
