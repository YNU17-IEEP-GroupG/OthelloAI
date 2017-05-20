package util;

import othello.Othello;

/**
 * Created by shiita on 2017/04/29.
 */
public class Point {
    public int row = 0;
    public int column = 0;

    public Point(int row, int column) throws PointException {
        if (0 <= row && row <= Othello.BOARD_SIZE)
            this.row = row;
        else
            throw new PointException("Pointの行の値が範囲外です。");
        if (0 <= column && column <= Othello.BOARD_SIZE)
            this.column = column;
        else
            throw new PointException("Pointの列の値が範囲外です。");
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return String.format("(row, column) = (%d, %d)", row, column);
    }

    private class PointException extends RuntimeException {
        public PointException(String message) {
            super(message);
        }
    }
}