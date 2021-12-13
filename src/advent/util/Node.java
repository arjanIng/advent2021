package advent.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Node<T> {
    private final String id;
    private T value;
    private final Set<Node<T>> connections;

    public Node(String id) {
        this.id = id;
        this.connections = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void addConnection(Node<T> connection) {
        this.connections.add(connection);
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", value=" + value +
                ", connections=" + connections +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
