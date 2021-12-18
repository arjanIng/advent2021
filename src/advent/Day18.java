package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day18 {

    public void solve(List<String> lines) {
        Stack<SnailNumber> sums = new Stack<>();
        List<SnailNumber> combinations = new ArrayList<>();
        for (String line : lines) {
            SnailNumber sn = parse(line);
            sums.insertElementAt(sn.clone(), 0);
            combinations.add(sn.clone());
        }
        while (sums.size() > 1) {
            sums.push(sums.pop().add(sums.pop()));
        }
        System.out.println("Part 1: " + sums.peek().magnitude());

        int maxm = -1;
        for (SnailNumber n1 : combinations) {
            for (SnailNumber n2 : combinations) {
                if (n1.equals(n2)) continue;
                maxm = Math.max(maxm, n1.clone().add(n2.clone()).magnitude());
                maxm = Math.max(maxm, n2.clone().add(n1.clone()).magnitude());
            }
        }
        System.out.println("Part 2: " + maxm);
    }

    private SnailNumber parse(String s) {
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
        private SnailNumber left;
        private SnailNumber right;
        private Integer value;
        private SnailNumber parent;

        public SnailNumber(SnailNumber left, SnailNumber right) {
            this.left = left;
            this.right = right;
            left.parent = this;
            right.parent = this;
        }

        public SnailNumber(Integer value) {
            this.value = value;
        }

        public SnailNumber add(SnailNumber toAdd) {
            SnailNumber sn = new SnailNumber(this, toAdd);
            sn.reduce();
            return sn;
        }

        public SnailNumber clone() {
            if (isDigit()) return new SnailNumber(value);
            return new SnailNumber(left.clone(), right.clone());
        }

        public int magnitude() {
            if (isDigit()) return value;
            return 3 * left.magnitude() + 2 * right.magnitude();
        }

        private void reduce() {
            boolean running = true;
            while (running) {
                running = false;
                while (explode()) {
                    running = true;
                }
                running |= split();
            }
        }

        private boolean isDigit() {
            return value != null;
        }

        private int level() {
            if (parent == null) return 0;
            return parent.level() + 1;
        }

        private SnailNumber origin() {
            SnailNumber current = this;
            while (current.parent != null) current = current.parent;
            return current;
        }

        private List<SnailNumber> allDigits() {
            if (isDigit()) return List.of(this);
            List<SnailNumber> all = new ArrayList<>();
            all.addAll(left.allDigits());
            all.addAll(right.allDigits());
            return all;
        }

        private SnailNumber protect(List<SnailNumber> list, int i) {
            return (i < 0 || i >= list.size()) ? null : list.get(i);
        }

        private SnailNumber nearestDigit(SnailNumber number, boolean dirLeft) {
            List<SnailNumber> digits = origin().allDigits();
            return protect(digits, digits.indexOf(number.left) + (dirLeft ? -1 : 2));
        }

        private boolean explode() {
            if (!isDigit()) {
                if (level() == 4) {
                    SnailNumber nearestLeft = nearestDigit(this, true);
                    SnailNumber nearestRight = nearestDigit(this, false);
                    if (nearestLeft != null) nearestLeft.value += left.value;
                    if (nearestRight != null) nearestRight.value += right.value;
                    left = null; right = null;
                    value = 0;
                    return true;
                } else {
                    return left.explode() || right.explode();
                }
            }
            return false;
        }

        private boolean split() {
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
                return left.split() || right.split();
            }
            return false;
        }

        @Override
        public String toString() {
            if (isDigit()) return value.toString();
            return "[" + left + "," + right + "]";
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
