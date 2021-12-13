package advent.util;

import java.io.IOException;
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

public class Graph<T> {
    private List<Node<T>> nodes;
    
    public Graph(List<Node<T>> nodes) {
        this.nodes = nodes;
    }

    public Graph<T> load(String filename, Function<String, Stream<T>> convertLine) throws IOException {
        List<String> input = Files.lines(Paths.get(filename)).collect(Collectors.toList());
        //TODO
//        Object[][] grid = input.stream().map(line -> convertLine.apply(line).toArray())
//                .toArray(size -> new Object[size][0]);
        return new Graph(null);
    }

    public Stream<Node<T>> stream() {
        Stream.Builder<Node<T>> builder = Stream.builder();
        for (Node<T> node : nodes) {
            builder.add(node);
        }
        return builder.build();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        //TODO
        return builder.toString();
    }

    private List<Node<T>> getCopyOfNodes() {
        return new ArrayList<Node<T>>(nodes);
    }

    public <E> Collector<E, ?, Graph<T>> collector() {
        return new Collector<E, Graph.Builder<T>, Graph<T>>() {
            @Override
            public Supplier<Graph.Builder<T>> supplier() {
                return Graph.Builder::new;
            }

            @Override
            @SuppressWarnings("unchecked")
            public BiConsumer<Graph.Builder<T>, E> accumulator() {
                return (b, e) -> {
                    assert(e instanceof Node);
                    b.add((Node<T>) e);
                };
            }

            @Override
            public BinaryOperator<Graph.Builder<T>> combiner() {
                return Graph.Builder::addAll;
            }

            @Override
            public Function<Graph.Builder<T>, Graph<T>> finisher() {
                return (b) -> b.build(nodes);
            }

            @Override
            public Set<Characteristics> characteristics() {
                return EnumSet.of(Characteristics.CONCURRENT);
            }
        };
    }

    public static class Builder<E> {
        private final List<Node<E>> list;

        public Builder() {
            list = new ArrayList<>();
        }

        public Graph.Builder<E> add(Node<E> element) {
            list.add(element);
            return this;
        }

        public Graph.Builder<E> add(Node<E>... elements) {
            list.addAll(Arrays.asList(elements));
            return this;
        }

        public Graph.Builder<E> addAll(Graph.Builder<E> builder) {
            list.addAll(builder.list);
            return this;
        }

        public Graph<E> build(List<Node<E>> fromGraph) {
            List<Node<E>> copy =  new ArrayList(fromGraph);
            for (Node<E> node : list) {
                if (copy.contains(node)) copy.add(copy.indexOf(node), node);
            }
            return new Graph(copy);
        }
    }


}
