package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Day12 {

    public void solve(List<String> input) {
        String line = input.get(0);
        out.println("Part 1: " + calcTotal(line));
        String line2 = line;
        while (true) {
            String[] parts = line2.split("red", 2);
            if (parts.length == 1) break; // no more reds
            String left = removeBlock(parts[0], -1);
            String right = parts[1];
            if (!left.equals(parts[0])) {
                right = removeBlock(parts[1], 1);
            }
            line2 = left + right;
        }
        out.println("Part 2: " + calcTotal(line2));
    }

    enum Type {OBJECT, ARRAY, VALUE}

    ;

    class Node {
        String value;
        Type type;
        List<Node> children;

        public Node(String value) {
            this.value = value;
            char first = value.charAt(0);
            if (first == '{') {
                type = Type.OBJECT;
            } else if (first == '[') {
                type = Type.ARRAY;
            } else {

                type = Type.VALUE;
            }
        }
    }

    private String removeBlock(String sub, int direction) {
        char[] chars = sub.toCharArray();
        int level = 1;
        int i = direction == -1 ? chars.length - 1 : 0;
        while (level > 0) {
            if (chars[i] == '[' && direction == -1) return sub;
            if (chars[i] == '{') level += direction;
            if (chars[i] == '}') level -= direction;
            i += direction;
        }
        return direction == -1 ? sub.substring(0, i + 2) : sub.substring(i - 1);
    }

    private int calcTotal(String line) {
        int total = 0;
        int num = 0;
        boolean negnum = false;
        for (char c : line.toCharArray()) {
            if (c >= '0' && c <= '9' || c == '-') {
                if (c == '-') {
                    negnum = true;
                } else {
                    num *= 10;
                    if (!negnum) {
                        num += c - '0';
                    } else {
                        num -= c - '0';
                    }
                }
            } else {
                total += num;
                num = 0;
                negnum = false;
            }
        }
        return total;
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
