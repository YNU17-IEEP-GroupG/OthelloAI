package ai;

import othello.Othello;
import util.Point;
import util.Stone;

import java.util.List;

/**
 * Created by shiita on 2017/05/10.
 */
public class SquareEvaluationAI implements BaseAI {
    private Stone[][] board = new Stone[Othello.BOARD_SIZE][Othello.BOARD_SIZE];
    private Stone stone;
    private List<Point> hint;
    private int row;
    private int column;

    public SquareEvaluationAI(Stone[][] board, Stone stone, List<Point> hint) {
        this.board = board;
        this.stone = stone;
        this.hint = hint;
    }

    @Override
    public void think() {
        Evaluation e = new Evaluation(board);
        Point bestPoint = hint.get(0);
        int bestValue = e.squareEvaluation(bestPoint, stone);
        for (Point p : hint) {
            int value = e.squareEvaluation(p, stone);
            if (value > bestValue) {
                bestPoint = p;
                bestValue = value;
            }
        }
        row = bestPoint.row;
        column = bestPoint.column;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }
}
