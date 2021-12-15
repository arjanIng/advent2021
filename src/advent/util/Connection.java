package advent.util;

public class Connection<N, T> {
    private final Node<N> from;
    private final Node<N> to;
    private final T weight;

    public Connection(Node<N> from, Node<N> to, T weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Node<N> getFrom() {
        return from;
    }

    public Node<N> getTo() {
        return to;
    }

    public T getWeight() {
        return weight;
    }
}
