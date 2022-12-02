package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Day12 {

    public void solve(List<String> input) {
        String line = input.get(0);

        Node root = new Node(line);
        out.println("Part 1: " + root.sum());
        root.ignoreRed();
        out.println("Part 2: " + root.sum());
    }

    enum Type {OBJECT, ARRAY, ELEMENT, VALUE}

    class Node {
        String name;
        String value;
        Type type;
        boolean ignore;
        List<Node> children;

        public Node(String body) {
            char first = body.charAt(0);
            if (first == '{') {
                type = Type.OBJECT;
                assert(body.charAt(body.length() - 1) == '}');
                this.children = calcChildren(body.substring(1, body.length() - 1));
            } else if (first == '[') {
                type = Type.ARRAY;
                assert(body.charAt(body.length() - 1) == ']');
                this.children = calcChildren(body.substring(1, body.length() - 1));
            } else {
                String[] parts = body.split(":", 2);
                if (parts.length == 2) {
                    this.type = Type.ELEMENT;
                    this.name = parts[0];
                    this.children = List.of(new Node(parts[1]));
                } else {
                    this.type = Type.VALUE;
                    this.value = parts[0];
                }
            }
        }

        private List<Node> calcChildren(String contents) {
            String[] parts = splitLevel(contents);
            List<Node> nodes = new ArrayList<>();
            for (String part: parts) {
                nodes.add(new Node(part));
            }
            return nodes;
        }

        private String[] splitLevel(String input) {
            int level = 0;
            List<String> parts = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (char c : input.toCharArray()) {
                if (c == ',' && level == 0) {
                    parts.add(sb.toString());
                    sb.setLength(0);
                    continue;
                }
                if (c == '[' || c == '{') level++;
                if (c == ']' || c == '}') level--;
                sb.append(c);
            }
            parts.add(sb.toString());
            return parts.toArray(new String[0]);
        }

        public Integer sum() {
            if (this.ignore) return 0;
            int sum = 0;
            if (this.type.equals(Type.VALUE)) {
                try {
                    sum = Integer.parseInt(this.value);
                } catch (NumberFormatException e) {
                    // ignore
                }
            } else {
                for (Node child : children) {
                    sum += child.sum();
                }
            }
            return sum;
        }

        public boolean ignoreRed() {
            if (this.type.equals(Type.VALUE)) {
                if ("\"red\"".equals(this.value)) return true;
                return false;
            } else {
                for (Node child: children) {
                    boolean result = child.ignoreRed();
                    if (this.type == Type.ELEMENT && result == true) return true;
                    if (this.type == Type.OBJECT && result == true) {
                        this.ignore = true;
                    }
                }
            }
            return false;
        }

        public void removeRed() {

        }

        public String toJson() {
            StringBuilder sb = new StringBuilder();
            switch(this.type) {
                case OBJECT -> {
                    sb.append('{');
                    for (Node child: this.children) sb.append(child.toJson()).append(',');
                    sb.setLength(sb.length() - 1);
                    sb.append('}');
                }
                case ARRAY -> {
                    sb.append('[');
                    for (Node child: this.children) sb.append(child.toJson()).append(',');
                    sb.setLength(sb.length() - 1);
                    sb.append(']');
                }
                case ELEMENT -> sb.append(this.name).append(':').append(this.children.get(0).toJson());
                case VALUE -> sb.append(this.value);
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) throws IOException {
        Day12 solver = new Day12();
        List<String> lines = Files.lines(Paths.get("./data/2015/day12.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        System.out.println("Running solver...");
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
