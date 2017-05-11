import java.util.List;

/**
 * Created by shiita on 2017/05/10.
 */
public class Evaluation {
    private Stone[][] board = new Stone[OthelloForAI.BOARD_SIZE][OthelloForAI.BOARD_SIZE];
    private static final int[][] square =
            {{ 30, -12,  0, -1, -1,  0, -12,  30},
             {-12, -15, -3, -3, -3, -3, -15, -12},
             {  0,  -3,  0, -1, -1,  0,  -3,   0},
             { -1,  -3, -1, -1, -1, -1,  -3,  -1},
             { -1,  -3, -1, -1, -1, -1,  -3,  -1},
             {  0,  -3,  0, -1, -1,  0,  -3,   0},
             {-12, -15, -3, -3, -3, -3, -15, -12},
             { 30, -12,  0, -1, -1,  0, -12,  30},};

    public Evaluation(Stone[][] board) {
        this.board = board;
    }

    // pのマスに置いた時の自分と相手の局面の評価の差分を返す
    public int squareEvaluation(Point point, Stone stone) {
        int black = 0;
        int white = 0;
        List<Point> pList = BoardHelper.putStone(point.row, point.column, stone, board);
        for (int i = 0; i < OthelloForAI.BOARD_SIZE; i++) {
            for (int j = 0; j < OthelloForAI.BOARD_SIZE; j++) {
                if (board[i][j] == Stone.Black)
                    black += square[i][j];
                else if (board[i][j] == Stone.White)
                    white += square[i][j];
            }
        }

        // 置いた石と返した石を元に戻す。新しい盤を作るよりも戻す処理の方が速い
        Stone reverse = stone.getReverse();
        pList.stream()
                .forEach(p -> board[p.row][p.column] = reverse);
        board[point.row][point.column] = Stone.Empty;

        return stone == Stone.Black ? black - white : white - black;
    }
}
