import java.awt.*;
import java.util.LinkedList;

/**
 * Created by needa_000 on 8/18/2014.
 */
public class GroupHighlighter
{
    public static final Color[] GROUP_COLORS = {new Color(0xFF6600),Color.BLUE, new Color(0x551A8B),Color.YELLOW};
    private LinkedList<CellGroup> groups;
    public GroupHighlighter()
    {
        groups = new LinkedList<>();
    }
    public void addGroup(int size, int x, int y)
    {
        groups.add(new CellGroup(size,x,y));
    }
    public void clearGroups()
    {
        groups.clear();
    }
    public void paintGroups(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(Cell.BORDER_SIZE));

        int i = 0;
        for (CellGroup group:groups)
        {
            Color c = GROUP_COLORS[i % GROUP_COLORS.length];
            g2.setColor(c);
            g2.drawRect(group.getX() * Cell.WIDTH + (int)(Cell.BORDER_SIZE/2), group.getY() * Cell.HEIGHT + (int)(Cell.BORDER_SIZE/2), group.getSize() * Cell.WIDTH - Cell.BORDER_SIZE, group.getSize() * Cell.HEIGHT - Cell.BORDER_SIZE);
            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 75));
            g2.fillRect(group.getX() * Cell.WIDTH + (int)(Cell.BORDER_SIZE/2), group.getY() * Cell.HEIGHT + (int)(Cell.BORDER_SIZE/2), group.getSize() * Cell.WIDTH - Cell.BORDER_SIZE, group.getSize() * Cell.HEIGHT - Cell.BORDER_SIZE);
            i++;
        }
    }

}
