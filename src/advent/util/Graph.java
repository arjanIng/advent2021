package advent.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph<T> {
    private Map<String, Node<T>> graph;
    
    public Graph(Map<String, Node<T>> nodes) {
        this.graph = nodes;
    }

    public Graph<T> load(String filename, Function<String, Stream<Node<T>>> convertLine) throws IOException {
        List<String> input = Files.lines(Paths.get(filename)).collect(Collectors.toList());
        Map<String, Node<T>> graph = input.stream().map(line -> convertLine.apply(line).collect(Collectors.toList()))
                .flatMap(Collection::stream).collect(Collectors.toMap(Node::getId, Function.identity()));
        return new Graph<T>(graph);
    }

    public Stream<Node<T>> stream() {
        Stream.Builder<Node<T>> builder = Stream.builder();
        for (Node<T> node : graph.values()) {
            builder.add(node);
        }
        return builder.build();
    }

    public Stream<List<Node<T>>> allPaths(Node<T> from, Node<T> to, Function<Node, Boolean> canVisit) {
        Stream.Builder<List<Node<T>>> builder = Stream.builder();
        return builder.build();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        //TODO
        return builder.toString();
    }

    private Map<String, Node<T>> getCopyOfNodes() {
        return new HashMap<>(graph);
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
                return (b) -> b.build(graph);
            }

            @Override
            public Set<Characteristics> characteristics() {
                return EnumSet.of(Characteristics.CONCURRENT);
            }
        };
    }

    public Node<T> getOrCreateNode(String id) {
        if (!graph.containsKey(id)) {
            Node<T> node = new Node<T>(id);
            graph.put(id, node);
        }
        return graph.get(id);
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

        public Graph<E> build(Map<String, Node<E>> fromGraph) {
            Map<String, Node<E>> copy =  new HashMap<>(fromGraph);
            for (Node<E> node : list) {
                if (copy.containsKey(node.getId())) copy.put(node.getId(), node);
            }
            return new Graph(copy);
        }
    }


}
