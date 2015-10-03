/**
 * Created by Ned Read on 8/18/2014.
 *
 * Data container for grouped natural cells. Used in highlighting natural cell group bonuses.
 */
public class CellGroup
{
    //region Private Members
    private int size;
    private int x;
    private int y;
    //endregion

    /**
     * @param size Size of the group one side length of the square
     * @param x Column of the top left corner
     * @param y Row of the top left corner
     */
    public CellGroup(int size, int x, int y)
    {
        this.size = size;
        this.x = x;
        this.y = y;
    }

    //region Getters
    public int getSize()
    {
        return size;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
    //endregion
}
