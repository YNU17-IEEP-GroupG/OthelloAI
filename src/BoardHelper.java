import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by shiita on 2017/05/10.
 * ボードの状態を変化させないものをOthelloForAIクラスから分離
 */
public class BoardHelper {
    public static List<Point> makeHint(Stone stone, Stone[][] board) {
        List<Point> hint = new ArrayList<>();
        for (int i = 0; i < OthelloForAI.BOARD_SIZE; i++)
            for (int j = 0; j < OthelloForAI.BOARD_SIZE; j++)
                if (!selectDirections(i, j, stone, board).isEmpty())
                    hint.add(new Point(i, j));
        return hint;
    }

    public static EnumSet<Direction> selectDirections(int r, int c, Stone stone, Stone[][] board) {
        EnumSet<Direction> directions = EnumSet.noneOf(Direction.class);
        if (board[r][c] != Stone.Empty) return directions;
        EnumSet.allOf(Direction.class)
                .stream()
                .filter(d -> checkLine(r, c, stone, d, board))
                .forEach(directions::add);
        return directions;
    }

    public static boolean checkLine (int r, int c, Stone stone, Direction direction, Stone[][] board) {
        int dr = direction.getDR(); int dc = direction.getDC();
        int i = r + dr;             int j = c + dc;

        while (0 <= i && i < OthelloForAI.BOARD_SIZE && 0 <= j && j < OthelloForAI.BOARD_SIZE) {
            if      (board[i][j] == Stone.Empty) break;
            else if (board[i][j] == stone){
                if (Math.abs(r - i) > 1 || Math.abs(c - j) > 1) return true;
                else break;
            }
            i += dr; j += dc;
        }
        return false;
    }
}
