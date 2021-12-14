package advent.util;

import java.util.HashMap;
import java.util.stream.Stream;

public class HashCounter<T> extends HashMap<T, Long> implements Counter<T> {
    public HashCounter() {
        super();
    }

    @Override
    public Stream<Entry<T, Long>> stream() {
        return entrySet().stream();
    }

    @Override
    public Long add(Entry<T, Long> entry) {
        return add(entry.getKey(), entry.getValue());
    }

    @Override
    public Counter<T> addAll(Counter<T> other) {
        other.forEach(this::add);
        return this;
    }

    @Override
    public Long add(T key, Long value) {
        Long previous = get(key);
        super.put(key, getOrDefault(key, 0L) + value);
        return previous;
    }

    @Override
    public Long min() {
        return values().stream().min(Long::compareTo).orElseThrow();
    }

    @Override
    public Long max() {
        return values().stream().max(Long::compareTo).orElseThrow();
    }

}
