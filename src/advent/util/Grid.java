package advent.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grid<T> {
    private final Object[][] grid;

    public Grid(Object[][] input) {
        this.grid = input;
    }

    public Grid() {
        this.grid = new Object[0][0];
    }

    public Pos<T> get(int row, int column) {
        if (row < 0 || row > grid.length - 1 || column < 0 || column > grid[row].length - 1) return null;
        return new Pos<>(row, column, elementAt(row, column));
    }

    public Pos<T> get(Pos<T> pos) {
        return get(pos.row(), pos.column());
    }

    public int sizeR() {
        return grid[0].length;
    }
    
    public int sizeC() {
        return grid.length;
    }

    public Grid<T> load(String filename, Function<String, Stream<T>> convertLine) throws IOException {
        List<String> input = Files.lines(Paths.get(filename)).collect(Collectors.toList());
        Object[][] grid = input.stream().map(line -> convertLine.apply(line).toArray())
                .toArray(size -> new Object[size][0]);
        return new Grid(grid);
    }

    public Grid<T> set(int row, int column, T value) {
        Object[][] copy = getCopyOfGrid();
        copy[row][column] = value;
        return new Grid<>(copy);
    }

    public Grid<T> set(Pos<T> pos) {
        Object[][] copy = getCopyOfGrid();
        copy[pos.row][pos.column] = pos.val;
        return new Grid<>(copy);
    }

    public Stream<Pos<T>> stream() {
        Stream.Builder<Pos<T>> builder = Stream.builder();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                Pos<T> p = new Pos<T>(r, c, elementAt(r, c));
                builder.add(p);
            }
        }
        return builder.build();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Object[] objects : grid) {
            for (Object object : objects) {
                builder.append(object + ",");
            }
            builder.setLength(builder.length() - 1);
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    private T elementAt(int r, int c) {
        return (T) grid[r][c];
    }

    private Object[][] getCopyOfGrid() {
        return Arrays.stream(grid).map(l -> Arrays.copyOf(l, l.length)).toArray(Object[][]::new);
    }

    public <E> Collector<E, ?, Grid<T>> collector() {
        return new Collector<E, Builder<T>, Grid<T>>() {
            @Override
            public Supplier<Builder<T>> supplier() {
                return Builder::new;
            }

            @Override
            @SuppressWarnings("unchecked")
            public BiConsumer<Builder<T>, E> accumulator() {
                return (b, e) -> {
                    assert(e instanceof Pos);
                    b.add((Pos<T>) e);
                };
            }

            @Override
            public BinaryOperator<Builder<T>> combiner() {
                return Builder::addAll;
            }

            @Override
            public Function<Builder<T>, Grid<T>> finisher() {
                return (b) -> b.build(grid);
            }

            @Override
            public Set<Characteristics> characteristics() {
                return EnumSet.of(Characteristics.CONCURRENT);
            }
        };
    }

    public static class Builder<E> {
        private final List<Pos<E>> list;

        public Builder() {
            list = new ArrayList<>();
        }

        public Builder<E> add(Pos<E> element) {
            list.add(element);
            return this;
        }

        public Builder<E> add(Pos<E>... elements) {
            list.addAll(Arrays.asList(elements));
            return this;
        }

        public Builder<E> addAll(Grid.Builder<E> builder) {
            list.addAll(builder.list);
            return this;
        }

        public Grid<E> build(Object[][] fromGrid) {
            Object[][] copy =  Arrays.stream(fromGrid).map(l -> Arrays.copyOf(l, l.length)).toArray(Object[][]::new);
            for (Pos<E> pos: list) {
                copy[pos.row][pos.column] = pos.val;
            }
            return new Grid<>(copy);
        }
    }

}
