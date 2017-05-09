import java.util.List;
import java.util.Random;

/**
 * Created by shiita on 2017/04/29.
 */
public class RandomAI {
    private List<Point> hint;
    private int row;
    private int column;

    public RandomAI(List<Point> hint) {
        this.hint = hint;
        row = hint.get(0).row;
        column = hint.get(0).column;
        think();
    }

    private void think() {
        Point point = hint.get(new Random().nextInt(hint.size()));
        row = point.row;
        column = point.column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
