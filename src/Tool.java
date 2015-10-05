import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Ned Read on 7/7/2014.
 * Unused class which represented a tool (Place house, place business, place farm , bulldozer, move, etc)
 */
public class Tool extends JPanel {
    private int state;

    private BufferedImage backBufferContainer;
    private Graphics backBuffer;

    public Tool(int state) {
        setMinimumSize(new Dimension(150, 100));
        setSize(150, 100);
        this.state = state;

        backBufferContainer = new BufferedImage(150, 100, BufferedImage.TYPE_INT_RGB);
        backBuffer = backBufferContainer.getGraphics();
    }

    @Override
    public void paintComponent(Graphics g) {
        backBuffer.clearRect(0, 0, getWidth(), getHeight());
        backBuffer.setColor(Cell.COLORS[state]);
        backBuffer.fillRect(0, 0, 150, 100);

        g.drawImage(backBufferContainer, 0, 0, null);
    }
}
