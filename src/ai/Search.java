package ai;

import util.BoardHelper;
import util.Point;
import util.Stone;

import java.util.List;

/**
 * Created by shiita on 2017/05/13.
 */
public class Search {
    private Stone targetStone;

    public Search(Stone targetStone) {
        this.targetStone = targetStone;
    }

    // depthは2の倍数で2以上ある必要がある
    public Point minMax(int depth, int evaluationCode, Stone stone, Stone[][] board) {
        if (depth % 2 == 1 || depth < 2) throw new RuntimeException();
        Point bestPoint = new Point(0,0);
        int bestValue = -(1 << 30);

        List<Point> hint = BoardHelper.makeHint(stone, board);
        for (Point p : hint) {
            List<Point> pList = BoardHelper.putStone(p.row, p.column, stone, board);
            int value = mm(depth - 1, evaluationCode, stone.getReverse(), board);
            BoardHelper.undo(p.row, p.column, stone, pList, board);
//            System.out.println("last (r, c) = (" + p.row + ", " +p.column + ")  value = " + value);
            if (value > bestValue) {
                bestPoint = p;
                bestValue = value;
            }
        }
        return bestPoint;
    }

    private int mm(int depth, int evaluationCode, Stone stone, Stone[][] board) {
        if (depth == 0) {
            return evaluate(evaluationCode, targetStone, board);
        }

        int bestValue;
        int tmpValue;
        if (depth % 2 == 0) bestValue = -(1 << 29);
        else                bestValue =   1 << 29;

        int i = 1;
        List<Point> hint = BoardHelper.makeHint(stone, board);
        // 読みの中で勝敗が決まった場合
        if (hint.isEmpty()) {
            return bestValue;
        }
        for (Point p : hint) {
            List<Point> pList = BoardHelper.putStone(p.row, p.column, stone, board);
            tmpValue = mm(depth - 1, evaluationCode, stone.getReverse(), board);
//            System.out.println("" + i++ + "(r, c) = (" + p.row + ", " +p.column + ")  value = " + tmpValue);
            BoardHelper.undo(p.row, p.column, stone, pList, board);
            if ((depth % 2 == 0 && tmpValue > bestValue) || (depth % 2 == 1 && tmpValue < bestValue)) {
                bestValue = tmpValue;
            }
        }
//        System.out.println("bestvalue = " + bestValue);

        return bestValue;
    }

    private int evaluate(int evaluationCode, Stone stone, Stone[][] board) {
        int value = 0;
        switch (evaluationCode) {
            case Evaluation.SQUARE_EVALUATION:
                value = Evaluation.squareEvaluation(stone, board);
//                System.out.println("value = " + value);
                break;
        }
        return value;
    }
}
