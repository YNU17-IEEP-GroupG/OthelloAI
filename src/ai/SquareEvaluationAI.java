package ai;

import othello.Othello;
import util.BoardHelper;
import util.Point;
import util.Stone;

import java.util.List;
import java.util.Random;

/**
 * Created by shiita on 2017/05/10.
 */
public class SquareEvaluationAI implements BaseAI {
    private Stone[][] board = new Stone[Othello.BOARD_SIZE][Othello.BOARD_SIZE];
    private Stone stone;
    private int depth;
    private List<Point> hint;
    private int row;
    private int column;
    private int countStart;

    public SquareEvaluationAI(Stone[][] board, Stone stone, int depth, int countStart, List<Point> hint) {
        this.board = board;
        this.stone = stone;
        this.hint = hint;
        this.depth = depth;
        this.countStart = countStart;
        row = hint.get(0).row;
        column = hint.get(0).column;
    }

    @Override
    public void think() {
        Search s = new Search(stone);
        Point point;
        if (BoardHelper.countStone(Stone.Empty, board) > countStart)
            point = s.minMax(depth, Evaluation.EvaluationType.SQUARE, stone, board);
        else
            point = s.untilEnd(Evaluation.EvaluationType.COUNT, stone, board);
        row = point.row;
        column = point.column;
    }

    @Override
    public void randomThink() {
        Point point = hint.get(new Random().nextInt(hint.size()));
        row = point.row;
        column = point.column;
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
