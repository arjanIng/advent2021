package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day18 {

    public void solve(List<String> lines) {
        Stack<SnailNumber> numbers = new Stack<>();
        List<SnailNumber> combinations = new ArrayList<>();
        for (String line : lines) {
            SnailNumber sn = parse(line);
            numbers.insertElementAt(sn.clone(), 0);
            combinations.add(sn.clone());
        }
        while (numbers.size() > 1) {
            numbers.push(new SnailNumber(numbers.pop(), numbers.pop()));
        }
        System.out.println("Part 1: " + numbers.peek().magnitude());

        int maxm = -1;
        for (SnailNumber n1 : combinations) {
            for (SnailNumber n2 : combinations) {
                if (n1.equals(n2)) continue;
                SnailNumber added1 = new SnailNumber(n1.clone(), n2.clone());
                SnailNumber added2 = new SnailNumber(n2.clone(), n1.clone());
                maxm = Math.max(maxm, added1.magnitude());
                maxm = Math.max(maxm, added2.magnitude());
            }
        }
        System.out.println("Part 2: " + maxm);
    }

    public SnailNumber parse(String s) {
        if (s.length() == 1) {
            return new SnailNumber(Integer.parseInt(s));
        }
        int level = 0;
        int splitAt = -1;
        for (int i = 0; i < s.length() && splitAt == -1; i++) {
            switch (s.charAt(i)) {
                case '[' -> level++;
                case ']' -> level--;
                case ',' -> {
                    if (level == 1) {
                        splitAt = i;
                    }
                }
            }
        }
        return new SnailNumber(parse(s.substring(1, splitAt)), parse(s.substring(splitAt + 1, s.length() - 1)));
    }

    static class SnailNumber {
        SnailNumber left;
        SnailNumber right;
        Integer value;
        SnailNumber parent;

        public SnailNumber(SnailNumber left, SnailNumber right) {
            this.left = left;
            this.right = right;
            left.parent = this;
            right.parent = this;
            reduce();
        }

        public SnailNumber(Integer value) {
            this.value = value;
            this.parent = null;
        }

        public void reduce() {
            boolean running = true;
            while (running) {
                running = false;
                while (explode()) {
                    running = true;
                }
                running = running || split();
            }
        }

        public boolean isDigit() {
            return value != null;
        }

        public SnailNumber clone() {
            if (isDigit()) return new SnailNumber(value);
            return new SnailNumber(left.clone(), right.clone());
        }

        public int level() {
            if (parent == null) return 0;
            return parent.level() + 1;
        }

        public int magnitude() {
            if (isDigit()) return value;
            return 3 * left.magnitude() + 2 * right.magnitude();
        }

        public SnailNumber origin() {
            SnailNumber current = this;
            while (current.parent != null) {
                current = current.parent;
            }
            return current;
        }

        public List<SnailNumber> allDigits() {
            if (isDigit()) return List.of(this);
            List<SnailNumber> all = new ArrayList<>();
            all.addAll(left.allDigits());
            all.addAll(right.allDigits());
            return all;
        }

        public SnailNumber nearestDigit(SnailNumber number, boolean dirLeft) {
            List<SnailNumber> allDigits = origin().allDigits();
            if (!dirLeft) Collections.reverse(allDigits);

            SnailNumber nearestDigit = null;
            for (SnailNumber current : allDigits) {
                if (current.equals(number.left) || current.equals(number.right)) break;
                nearestDigit = current;
            }
            return nearestDigit;
        }

        public boolean explode() {
            if (!isDigit()) {
                if (level() >= 4) {
                    SnailNumber nearestLeft = nearestDigit(this, true);
                    SnailNumber nearestRight = nearestDigit(this, false);
                    if (nearestLeft != null) nearestLeft.value += left.value;
                    if (nearestRight != null) nearestRight.value += right.value;
                    left = null; right = null; value = 0;
                    return true;
                } else {
                    if (left.explode()) return true;
                    if (right.explode()) return true;
                }
            }
            return false;
        }

        public boolean split() {
            if (isDigit()) {
                if (value >= 10) {
                    int n = value / 2;
                    left = new SnailNumber(n);
                    right = new SnailNumber(n + value % 2);
                    left.parent = this; right.parent = this;
                    value = null;
                    return true;
                }
            } else {
                if (left.split()) return true;
                if (right.split()) return true;
            }
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        Day18 solver = new Day18();
        List<String> lines = Files.lines(Paths.get("./data/day18.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
