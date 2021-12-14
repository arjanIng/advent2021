package advent.util;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface Counter<T> extends Map<T, Long> {
    Stream<Map.Entry<T, Long>> stream();

    Long add(Map.Entry<T, Long> entry);

    Counter<T> addAll(Counter<T> entry);

    Long add(T key, Long value);

    Long min();

    Long max();

    static Collector<Entry<String, Long>, Counter<String>, Counter<String>> collector() {
        return new Collector<>() {
            @Override
            public Supplier<Counter<String>> supplier() {
                return HashCounter::new;
            }

            @Override
            public BiConsumer<Counter<String>, Entry<String, Long>> accumulator() {
                return Counter::add;
            }

            @Override
            public BinaryOperator<Counter<String>> combiner() {
                return Counter::addAll;
            }

            @Override
            public Function<Counter<String>, Counter<String>> finisher() {
                return a -> a;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.emptySet();
            }
        };
    }
}
