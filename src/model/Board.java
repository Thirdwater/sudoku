package model;

import model.Cell;

import java.util.Arrays;

public class Board {

    private Cell[][] cells;

    public Board () {
        cells = new Cell[9][9];
        emptyBoard();
    }

    public void emptyBoard () {
        for (int i = 0; i < 9; i++) {
            Arrays.fill(cells[i], new Cell());
        }
    }

    public void resetProgress () {
        for (Cell[] row: cells) {
            for (Cell cell: row) {
                cell.resetProgress();
            }
        }
    }

    private int[] getRow (int i) {
        assert 1 <= i && i <= 9;
        int[] row = new int[9];
        for (int j = 0; j < 9; j++) {
            row[j] = this.cells[i - 1][j].getNumber();
        }
        return row;
    }

    private int[] getColumn (int j) {
        assert 1 <= j && j <= 9;
        int[] column = new int[9];
        for (int i = 0; i < 9; i++) {
            column[i] = this.cells[i][j - 1].getNumber();
        }
        return column;
    }

    private int[] getSubgrid (int k) {
        assert 1 <= k && k <= 9;
        int[] subgrid = new int[9];
        int baseRow = 3 * ((k - 1) / 3);
        int baseColumn = (k - 1) % 3;
        for (int i = 0; i < 9; i++) {
            int shiftRow = 3 * (i / 3);
            int shiftColumn = i % 3;
            subgrid[i] = this.cells[baseRow + shiftRow][baseColumn + shiftColumn].getNumber();
        }
        return subgrid;
    }

    public void setPuzzle (int[][] puzzle) {
        if (isValidPuzzle(puzzle) && isSolvablePuzzle(puzzle)) {
            emptyBoard();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int fixedNum = puzzle[i][j];
                    cells[i][j].setFixed(fixedNum);
                }
            }
        }
    }

    public boolean isComplete () {

        return false;
    }

    private boolean isValidPuzzle (int[][] puzzle) {
        if (puzzle.length != 9) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            if (puzzle[i].length != 9) {
                return false;
            }
            for (int j = 0; j < 9; j++) {
                int num = puzzle[i][j];
                if (!(0 <= num && num <= 9)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param puzzle assumed isValidPuzzle
     * @return
     */
    private boolean isSolvablePuzzle (int[][] puzzle) {
        return true;
    }

}
