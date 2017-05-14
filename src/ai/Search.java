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

    public Point untilEnd(Evaluation.EvaluationType type, Stone stone, Stone[][] board) {
        Point bestPoint = new Point(0,0);
        Triple bestTriple = new Triple(-1, 0, 0);
        Triple triple;
        List<Point> hint = BoardHelper.makeHint(stone, board);
        for (Point p : hint) {
            List<Point> pList = BoardHelper.putStone(p.row, p.column, stone, board);
            triple = uEnd(type, stone.getReverse(), board);
            BoardHelper.undo(p.row, p.column, stone, pList, board);
            if (triple.winPer > bestTriple.winPer) {
                bestTriple.winPer = triple.winPer;
                bestTriple.value = triple.value;
                bestPoint = p;
            }
            else if (triple.winPer == bestTriple.winPer && triple.value > bestTriple.value) {
                bestTriple.value = triple.value;
                bestPoint = p;
            }
        }
//        System.out.println("勝率" + (bestTriple.winPer * 100) +"%, 最低石差" + bestTriple.value + "個");
        return bestPoint;
    }

    private Triple uEnd(Evaluation.EvaluationType type, Stone stone, Stone[][] board) {
        List<Point> hint = BoardHelper.makeHint(stone, board);
        if (hint.isEmpty()) {
            List<Point> hint2 = BoardHelper.makeHint(stone.getReverse(), board);
            if (hint2.isEmpty()) {
                int value = evaluate(type, targetStone, board);
                return new Triple(value > 0 ? 1 : 0, 1, value);
            }
            else {
                return uEnd(type, stone.getReverse(), board);
            }
        }

        double winPer = 0;
        int count = 0;
        int value = stone == targetStone ? -(1 << 29) : (1 << 29);
        for (Point p : hint) {
            List<Point> pList = BoardHelper.putStone(p.row, p.column, stone, board);
            Triple triple = uEnd(type, stone.getReverse(), board);
            BoardHelper.undo(p.row, p.column, stone, pList, board);

            // 目的の石の場合には最も良いものを選択
            if (stone == targetStone) {
                if (triple.winPer > winPer) {
                    winPer = triple.winPer;
                    value = triple.value;
                }
                else if (triple.winPer == winPer) {
                    value = Math.max(triple.value, value);
                }
            }
            // そうでなければ、全体の確率を考える
            else {
                winPer += triple.winPer * triple.count;
                value = Math.min(triple.value, value);
            }
            count += triple.count;
        }
        if (stone != targetStone) {
            winPer /= count;
        }
        return new Triple(winPer, count, value);
    }

    // depthは2の倍数で2以上ある必要がある
    public Point minMax(int depth, Evaluation.EvaluationType type, Stone stone, Stone[][] board) {
        if (depth % 2 == 1 || depth < 2) throw new RuntimeException();
        Point bestPoint = new Point(0,0);
        int alpha = -(1 << 30);
        int beta =    1 << 30;

        List<Point> hint = BoardHelper.makeHint(stone, board);
        for (Point p : hint) {
            List<Point> pList = BoardHelper.putStone(p.row, p.column, stone, board);
            int value = alphaBeta(depth - 1, type, stone.getReverse(), board, alpha, beta);
            BoardHelper.undo(p.row, p.column, stone, pList, board);
            if (value > alpha) {
                bestPoint = p;
                alpha = value;
            }
        }
        return bestPoint;
    }

    private int alphaBeta(int depth, Evaluation.EvaluationType type, Stone stone, Stone[][] board, int alpha, int beta) {
        if (depth == 0) {
            return evaluate(type, targetStone, board);
        }

        int value;
        if (stone == targetStone) alpha = -(1 << 29);
        else                      beta  =   1 << 29;

        List<Point> hint = BoardHelper.makeHint(stone, board);
        // 読みの中で勝敗が決まった場合
        if (hint.isEmpty()) {
            return stone == targetStone ? alpha : beta;
        }
        for (Point p : hint) {
            List<Point> pList = BoardHelper.putStone(p.row, p.column, stone, board);
            value = alphaBeta(depth - 1, type, stone.getReverse(), board, alpha, beta);
            BoardHelper.undo(p.row, p.column, stone, pList, board);
            if (stone == targetStone && value > alpha) {
                alpha = value;
                if (alpha > beta) return alpha;
            }
            else if (stone != targetStone && value < beta) {
                beta = value;
                if (alpha > beta) return beta;
            }
        }
        return stone == targetStone ? alpha : beta;
    }

    private int evaluate(Evaluation.EvaluationType type, Stone stone, Stone[][] board) {
        int value = 0;
        switch (type) {
            case SQUARE:
                value = Evaluation.squareEvaluation(stone, board);
                break;
            case COUNT:
                value = Evaluation.countEvaluation(stone, board);
                break;
        }
        return value;
    }

    private class Triple {
        public double winPer;
        public int count;
        public int value;
        public Triple(double winPer, int count, int value) {
            this.winPer = winPer; this.count = count; this.value = value;
        }
    }
}
