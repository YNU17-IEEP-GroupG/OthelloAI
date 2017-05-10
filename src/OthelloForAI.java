import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by shiita on 2017/05/09.
 */
public class OthelloForAI {
    private static final int BOARD_SIZE = 8;
    private Stone[][] board = new Stone[BOARD_SIZE][BOARD_SIZE];
    private Stone nowStone = Stone.Black;
    private boolean turn = true;
    private boolean passFlag = false;
    private String result;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<String> results = IntStream.range(0,1000000)
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

    private List<Point> makeHint(Stone stone) {
        List<Point> hint = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (!selectDirections(i, j, stone).isEmpty())
                    hint.add(new Point(i, j));
        return hint;
    }

    private void putStone(int r, int c, Stone stone) {
        EnumSet<Direction> directions = selectDirections(r, c, stone);
        if (directions.isEmpty()) return;
        board[r][c] = stone;
        reverseStone(r, c, stone, directions);
    }

    private EnumSet<Direction> selectDirections(int r, int c, Stone stone) {
        EnumSet<Direction> directions = EnumSet.noneOf(Direction.class);
        if (board[r][c] != Stone.Empty) return directions;
        EnumSet.allOf(Direction.class)
                .stream()
                .filter(d -> checkLine(r, c, stone, d))
                .forEach(directions::add);
        return directions;
    }

    private boolean checkLine (int r, int c, Stone stone, Direction direction) {
        int dr = direction.getDR(); int dc = direction.getDC();
        int i = r + dr;             int j = c + dc;

        while (0 <= i && i < BOARD_SIZE && 0 <= j && j < BOARD_SIZE) {
            if      (board[i][j] == Stone.Empty) break;
            else if (board[i][j] == stone){
                if (Math.abs(r - i) > 1 || Math.abs(c - j) > 1) return true;
                else break;
            }
            i += dr; j += dc;
        }
        return false;
    }

    private void nextTurn() {
        turn = !turn;
        nowStone = nowStone.getReverse();
        List<Point> hint = makeHint(nowStone);
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
            if (turn) {
                RandomAI ai = new RandomAI(hint);
                putStone(ai.getRow(), ai.getColumn(), nowStone);
                nextTurn();
            }
            else {
                RandomAI ai = new RandomAI(hint);
                putStone(ai.getRow(), ai.getColumn(), nowStone);
                nextTurn();
            }
        }
    }

    private void reverseStone(int r, int c, Stone stone, EnumSet<Direction> directions) {
        directions.forEach(d -> reverseLine(r, c, stone, d));
    }

    private void reverseLine(int r, int c, Stone stone, Direction direction) {
        int dr = direction.getDR(); int dc = direction.getDC();
        int i = r + dr;             int j = c + dc;
        Stone reverse = stone.getReverse();

        while (0 <= i && i < BOARD_SIZE && 0 <= j && j < BOARD_SIZE) {
            if (board[i][j] == reverse) {
                board[i][j] = stone;
            }
            else {
                break;
            }
            i += dr; j += dc;
        }
    }

    private void gameOver() {
        int black = countStone(Stone.Black); int white = countStone(Stone.White);
        result = black + "," + white + "," + (black > white ? 1 : 0);
    }

    private int countStone(Stone stone) {
        return (int)Arrays.stream(board)
                .mapToLong(ss -> Arrays.stream(ss)
                        .filter(s -> s == stone)
                        .count())
                .sum();
    }

    private void initBoard() {
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                board[i][j] = Stone.Empty;
        board[3][3] = Stone.Black; board[3][4] = Stone.White;
        board[4][3] = Stone.White; board[4][4] = Stone.Black;
    }

    private void printBoard() {
        for (Stone[] stones : board) {
            for (Stone stone : stones) System.out.print(stone + " ");
            System.out.println();
        }
        System.out.println();
    }
}

