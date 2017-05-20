package ai;


import util.BoardHelper;
import util.Point;
import util.Stone;

import java.util.List;

/**
 * Created by shiita on 2017/05/17.
 * 常に多く石を取れるように行動する
 */
public class AlphaAI extends BaseAI {
    private final int[] countStart = { 0, 4, 8 };
    private final int[] depth = { 2, 2, 4 };
    private final int[] limit = { 4, 10, 20 };

    @Override
    public void think() {
        Search s = new Search(stone);
        Point point;
        if (BoardHelper.countStone(Stone.Empty, board) > countStart[difficulty])
            point = s.beamSearch(Evaluation.EvaluationType.COUNT, board, limit[difficulty], depth[difficulty]);
        else
            point = s.untilEnd(Evaluation.EvaluationType.COUNT, stone, board);
        row = point.getRow();
        column = point.getColumn();
    }

    public AlphaAI(List<Point> hint, Stone stone, Stone[][] board, int difficulty) {
        super(hint, stone, board, difficulty);
    }
}
