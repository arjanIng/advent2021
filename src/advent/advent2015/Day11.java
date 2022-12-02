package advent.advent2015;

import java.io.IOException;

import static java.lang.System.out;

public class Day11 {

    public void solve(String input) {
        String part1 = findNextPw(input);
        out.println("Part 1: " + part1);
        out.println("Part 2: " + findNextPw(nextTry(part1)));
    }

    private String findNextPw(String input) {
        String current = input;

        while (true) {
            if (isValid(current)) break;
            current = nextTry(current);
        }
        return current;
    }

    private String nextTry(String current) {
        char[] newpw = current.toCharArray();
        for (int i = 0; i < newpw.length; i++) {
            if (newpw[i] == 'i' || newpw[i] == 'o' || newpw[i] == 'l') {
                newpw[i] += 1;
                for (int j = i + 1; j < newpw.length; j++) {
                    newpw[j] = 'a';
                }
            }
        }
        int i = newpw.length - 1;
        while (true) {
            newpw[i] += 1;
            if (newpw[i] > 'z') {
                newpw[i] = 'a';
                i--;
            } else {
                break;
            }
        }
        current = new String(newpw);
        return current;
    }

    private boolean isValid(String pw) {
        return passesRule2(pw) && passesRule1(pw) && passesRule3(pw);
    }

    private boolean passesRule1(String pw) {
        char last = pw.toCharArray()[0];
        int count = 0;
        for (char c : pw.substring(1).toCharArray()) {
            if (c - last == 1) {
                count++;
                if (count == 2) {
                    return true;
                }
            } else {
                count = 0;
            }
            last = c;
        }
        return false;
    }

    private boolean passesRule2(String pw) {
        return !pw.contains("i") && !pw.contains("o") && !pw.contains("l");
    }

    private boolean passesRule3(String pw) {
        char firstPair = 0x0;
        char last = pw.toCharArray()[0];
        for (char c : pw.substring(1).toCharArray()) {
            if (last == c) {
                if (c != firstPair) {
                    if (firstPair == 0x0) {
                        firstPair = c;
                    } else {
                        return true;
                    }
                }
            }
            last = c;
        }
        return false;
    }




    public static void main(String[] args) throws IOException {
        Day11 solver = new Day11();
        long start = System.currentTimeMillis();
        out.println("Running solver...");

        solver.solve("hepxcrrq");
        out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
