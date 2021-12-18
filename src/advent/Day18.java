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
        List<SnailNumber> numberList = new ArrayList<>();
        Stack<SnailNumber> numbers = new Stack<>();
        for (String line : lines) {
            numbers.add(parse(line));
            numberList.add(numbers.peek().clone());
        }
        Collections.reverse(numbers);

        while (numbers.size() > 1) {
            SnailNumber n1 = numbers.pop();
            SnailNumber n2 = numbers.pop();
            SnailNumber added = add(n1, n2);
            numbers.push(added);
        }
        System.out.println("Part 1: " + numbers.peek().magnitude());
        
        int maxm = -1;
        for (SnailNumber n1 : numberList) {
            for (SnailNumber n2 : numberList) {
                if (!n1.equals(n2)) {
                    maxm = Math.max(maxm, add(n1.clone(), n2.clone()).magnitude());
                    maxm = Math.max(maxm, add(n2.clone(), n1.clone()).magnitude());
                }
            }
        }
        System.out.println("Part 2: " + maxm);
    }

    private SnailNumber add(SnailNumber n1, SnailNumber n2) {
        SnailNumber added = new SnailNumber(n1, n2);
        reduce(added);
        return added;
    }

    public void reduce(SnailNumber sn) {
        boolean running = true;
        while (running) {
            boolean hasExploded = sn.explode();
            boolean exploding = hasExploded;
            while(exploding) {
                exploding = sn.explode();
            }
            boolean hasSplit = sn.split();
            running = hasExploded || hasSplit;
        }
    }

    public SnailNumber parse(String s) {
        if (s.length() == 1) {
            return new SnailNumber(Integer.parseInt(s));
        }
        int level = 0;
        int commaAt = -1;
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case '[' -> level++;
                case ']' -> level--;
                case ',' -> {
                    if (level == 1) {
                        commaAt = i;
                    }
                }
            }
        }
        return new SnailNumber(parse(s.substring(1, commaAt)), parse(s.substring(commaAt + 1, s.length() - 1)));
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
        }

        public SnailNumber(Integer value) {
            this.value = value;
            this.parent = null;
        }

        public int level() {
            if (parent == null) return 0;
            return parent.level() + 1;
        }

        public int magnitude() {
            if (value != null) return value;
            return 3 * left.magnitude() + 2 * right.magnitude();
        }

        public SnailNumber origin() {
            SnailNumber current = this;
            while (current.parent != null) {
                current = current.parent;
            }
            return current;
        }

        public List<SnailNumber> flatMap() {
            if (this.value != null) return List.of(this);
            List<SnailNumber> all = new ArrayList<>();
            all.addAll(this.left.flatMap());
            all.addAll(this.right.flatMap());
            return all;
        }

        public SnailNumber clone() {
            if (value != null) return new SnailNumber(value);
            return new SnailNumber(left.clone(), right.clone());
        }

        public SnailNumber searchDigit(SnailNumber exclude, boolean dirLeft) {
            List<SnailNumber> map = this.origin().flatMap();
            if (!dirLeft) {
                Collections.reverse(map);
            }
            SnailNumber foundDigit = null;
            for (SnailNumber current : map) {
                if (current.equals(exclude.left) || current.equals(exclude.right)) break;
                if (current.value != null) {
                    foundDigit = current;
                }
            }
            return foundDigit;
        }

        public boolean explode() {
            if (this.left != null && this.right != null) {
                if (level() >= 4) {
                    SnailNumber leftDigit = this.parent.searchDigit(this, true);
                    SnailNumber rightDigit = this.parent.searchDigit(this, false);
                    if (leftDigit != null) leftDigit.value = leftDigit.value + this.left.value;
                    if (rightDigit != null) rightDigit.value = rightDigit.value + this.right.value;
                    this.left = null;
                    this.right = null;
                    this.value = 0;
                    return true;
                } else {
                    if (left.explode()) return true;
                    if (right.explode()) return true;
                }
            }
            return false;
        }

        public boolean split() {
            if (left != null && right != null) {
                if (left.split()) return true;
                if (right.split()) return true;
            } else {
                if (value >= 10) {
                    int n = this.value / 2;
                    left = new SnailNumber(n);
                    right = new SnailNumber(n + value % 2);
                    left.parent = this;
                    right.parent = this;
                    value = null;
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            if (value != null) return value.toString();
            return "[" + left + "," + right + "]";
        }
    }

    public static void main(String[] args) throws IOException {
        Day18 day17 = new Day18();
        List<String> lines = Files.lines(Paths.get("./data/day18.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        day17.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
