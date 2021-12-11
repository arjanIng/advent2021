package advent.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class IntGrid {
    private final int[][] grid;

    private IntGrid(int[][] input) {
        this.grid = input;
    }

    public static IntGrid fromFile(String filename, Function<String, IntStream> lineToArray) throws IOException {
        List<String> input = Files.lines(Paths.get(filename)).collect(Collectors.toList());
        int[][] grid = input.stream().map(line -> lineToArray.apply(line).toArray())
                .toArray(size -> new int[size][0]);
        return new IntGrid(grid);
    }

    public IntGrid forAll(Function<Pos, Integer> function) {
        int[][] result = copyOfGrid();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                Pos p = new Pos(r, c, grid[r][c]);
                result[r][c] = function.apply(p);
            }
        }
        return new IntGrid(result);
    }

    public IntGrid forNeighbors(Pos pos, Function<Pos, Integer> function) {
        int[][] result = copyOfGrid();
        for (int r = Math.max(pos.getRow() - 1, 0); r < Math.min(pos.getRow() + 2, grid.length); r++) {
            for (int c = Math.max(pos.getColumn() - 1, 0); c < Math.min(pos.getColumn() + 2, grid[r].length); c++) {
                Pos p = new Pos(r, c, grid[r][c]);
                result[r][c] = function.apply(p);
            }
        }
        return new IntGrid(result);
    }

    public int inspect(Function<Pos, Integer> function) {
        int result = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                Pos p = new Pos(r, c, grid[r][c]);
                result += function.apply(p);
            }
        }
        return result;
    }

    private int[][] copyOfGrid() {
        return Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new);
    }

    public void visualize() {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                System.out.print(grid[r][c]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static class Pos {
        private final int row;
        private final int column;
        private final int val;

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public int getVal() {
            return val;
        }

        public Pos newVal(int newVal) {
            return new Pos(row, column, newVal);
        }

        public Pos(int row, int column, int val) {
            this.row = row;
            this.column = column;
            this.val = val;
        }

        @Override
        public String toString() {
            return "Pos{" +
                    "row=" + row +
                    ", column=" + column +
                    ", val=" + val +
                    '}';
        }
    }
}
