package othello;

import ai.BaseAI;
import ai.RandomAI;
import ai.SquareEvaluationAI;
import util.BoardHelper;
import util.Point;
import util.Stone;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by shiita on 2017/05/09.
 */
public class OthelloForAI {
    private Stone[][] board = new Stone[Othello.BOARD_SIZE][Othello.BOARD_SIZE];
    private Stone nowStone = Stone.White;   // 黒から対局が始まるようにする
    private boolean turn = false;
    private boolean passFlag = false;
    private String result;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<String> results = IntStream.range(0, 100)
                .parallel()
                .mapToObj(i -> new OthelloForAI())
                .map(o -> o.getResult())
                .collect(Collectors.toList());
        try (FileWriter fw = new FileWriter("output.csv", true);
                      BufferedWriter br = new BufferedWriter(fw)) {
            for (String result : results) {
                br.append(result);
                br.newLine();
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        long finish = System.currentTimeMillis();
        System.out.println(finish - start + " ");
    }

    public String getResult() {
        return result;
    }

    private OthelloForAI() {
        initBoard();
        nextTurn();
    }

    private void nextTurn() {
        turn = !turn;
        nowStone = nowStone.getReverse();
        List<Point> hint = BoardHelper.makeHint(nowStone, board);
        if (hint.isEmpty()) {
            if (passFlag) {
                gameOver();
            }
            else {
                passFlag = true;
                nextTurn();
            }
        }
        else {
            passFlag = false;
            BaseAI ai;
            if (turn) {
                ai = new SquareEvaluationAI(board, nowStone, hint);
            }
            else {
                ai = new RandomAI(hint);
            }
//            if (new Random().nextDouble() <= 0.2)
//                ai.randomThink();
//            else
                ai.think();
            BoardHelper.putStone(ai.getRow(), ai.getColumn(), nowStone, board);
            nextTurn();
        }
    }

    private void gameOver() {
        int black = BoardHelper.countStone(Stone.Black, board); int white = BoardHelper.countStone(Stone.White, board);
        result = black + "," + white + "," + (black > white ? 1 : 0);
    }

    private void initBoard() {
        for (int i = 0; i < Othello.BOARD_SIZE; i++)
            for (int j = 0; j < Othello.BOARD_SIZE; j++)
                board[i][j] = Stone.Empty;
        board[3][3] = Stone.Black; board[3][4] = Stone.White;
        board[4][3] = Stone.White; board[4][4] = Stone.Black;
    }
}

