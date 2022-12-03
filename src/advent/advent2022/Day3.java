package advent.advent2022;

import advent.util.BiMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Day3 {

    public void solve(List<String> lines) {
        int sumpriority = 0;
        for (String line : lines) {
            int split = line.length() / 2;
            char[] left = line.substring(0, split).toCharArray();
            char[] right = line.substring(split).toCharArray();

            endloop:
            for (int i = 0; i < left.length; i++) {
                for (int j = 0; j < right.length; j++) {
                    if (left[i] == right[j]) {
                        int priority = 0;
                        if (Character.isLowerCase(left[i])) {
                            priority = 1 + left[i] - 'a';
                        } else {
                            priority = 27 + left[i] - 'A';
                        }
                        sumpriority += priority;
                        break endloop;
                    }
                }
            }
        }
        out.println("Part 1: " + sumpriority);
    }

    public static void main(String[] args) throws IOException {
        Day3 solver = new Day3();
        List<String> lines = Files.lines(Paths.get("./data/2022/day3.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        out.println("Running solver...");
        solver.solve(lines);
        out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
