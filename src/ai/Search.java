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
        int alpha = -(1 << 30);
        int beta =    1 << 30;

        List<Point> hint = BoardHelper.makeHint(stone, board);
        for (Point p : hint) {
            List<Point> pList = BoardHelper.putStone(p.row, p.column, stone, board);
            int value = alphaBeta(depth - 1, evaluationCode, stone.getReverse(), board, alpha, beta);
            BoardHelper.undo(p.row, p.column, stone, pList, board);
            if (value > alpha) {
                bestPoint = p;
                alpha = value;
            }
        }
        return bestPoint;
    }

    private int alphaBeta(int depth, int evaluationCode, Stone stone, Stone[][] board, int alpha, int beta) {
        if (depth == 0) {
            return evaluate(evaluationCode, targetStone, board);
        }

        int value;
        if (depth % 2 == 0) alpha = -(1 << 29);
        else                beta  =   1 << 29;

        List<Point> hint = BoardHelper.makeHint(stone, board);
        // 読みの中で勝敗が決まった場合
        if (hint.isEmpty()) {
            return depth % 2 == 0 ? alpha : beta;
        }
        for (Point p : hint) {
            List<Point> pList = BoardHelper.putStone(p.row, p.column, stone, board);
            value = alphaBeta(depth - 1, evaluationCode, stone.getReverse(), board, alpha, beta);
            BoardHelper.undo(p.row, p.column, stone, pList, board);
            if (depth % 2 == 0 && value > alpha) {
                alpha = value;
                if (alpha > beta) return alpha;
            }
            else if (depth % 2 == 1 && value < beta) {
                beta = value;
                if (alpha > beta) return beta;
            }
        }
        return depth % 2 == 0 ? alpha : beta;
    }

    private int evaluate(int evaluationCode, Stone stone, Stone[][] board) {
        int value = 0;
        switch (evaluationCode) {
            case Evaluation.SQUARE_EVALUATION:
                value = Evaluation.squareEvaluation(stone, board);
                break;
        }
        return value;
    }
}
