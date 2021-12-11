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

        public Pos get(int row, int column) {
            if (row < 0 || row > grid.length - 1 || column < 0 || column > grid[row].length - 1 ) return null;
            return new Pos(row, column, grid[row][column]);
        }

        public IntGrid set(int row, int column, int value) {
            int[][] copyOfGrid = getCopyOfGrid();
            copyOfGrid[row][column] = value;
            return new IntGrid(copyOfGrid);
        }

        public Stream<Pos> stream() {
            Stream.Builder<Pos> builder = Stream.builder();
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[r].length; c++) {
                    Pos p = new Pos(r, c, grid[r][c]);
                    builder.add(p);
                }
            }
            return builder.build();
        }

        private int[][] getCopyOfGrid() {
            return Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new);
        }

        public Collector<Pos, ?, IntGrid> collector() {
            return new Collector<Pos, GridBuilder, IntGrid>() {
                @Override
                public Supplier<GridBuilder> supplier() {
                    return GridBuilder::new;
                }

                @Override
                public BiConsumer<GridBuilder, Pos> accumulator() {
                    return GridBuilder::add;
                }

                @Override
                public BinaryOperator<GridBuilder> combiner() {
                    return (left, right) -> { left.addAll(right); return left; };
                }

                @Override
                public Function<GridBuilder, IntGrid> finisher() {
                    return GridBuilder::build;
                }

                @Override
                public Set<Characteristics> characteristics() {
                    return Collections.emptySet();
                }
            };
        }

        private class GridBuilder {
            List<Pos> list;

            private GridBuilder() {
                this.list = new ArrayList<>();
            }

            private void add(Pos pos) {
                list.add(pos);
            }

            private void addAll(GridBuilder other) {
                list.addAll(other.list);
            }

            private IntGrid build() {
                int[][] copyOfGrid = getCopyOfGrid();
                list.forEach(p -> copyOfGrid[p.row()][p.column()] = p.val());
                return new IntGrid(copyOfGrid);
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[r].length; c++) {
                    builder.append(grid[r][c]);
                }
                builder.append(System.lineSeparator());
            }
            return builder.toString();
        }

    }
