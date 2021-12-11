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

        public void visualize() {
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[r].length; c++) {
                    System.out.print(grid[r][c]);
                }
                System.out.println();
            }
            System.out.println();
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
                int[][] copyOfGrid = Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new);
                list.forEach(p -> copyOfGrid[p.getRow()][p.getColumn()] = p.getVal());
                return new IntGrid(copyOfGrid);
            }
        }

        public record Pos(int row, int column, int val) {

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

            public Pos add(int add) {
                return new Pos(row, column, val + add);
            }

            public boolean isNeighborOf(Pos p) {
                return Math.abs(row - p.row) <= 1 && Math.abs(column - p.column) <= 1;
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
