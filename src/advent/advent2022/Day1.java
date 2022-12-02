package advent.advent2022;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {

    public void solve(List<String> lines) {
        List<Integer> elfcalories = new ArrayList<>();
        elfcalories.add(0);
        lines.forEach(l -> {
            if (l.isEmpty()) {
                elfcalories.add(0);
            } else {
                elfcalories.set(elfcalories.size() - 1, elfcalories.get(elfcalories.size() - 1) + Integer.parseInt(l));
            }
        });

        System.out.println("part 1: " + elfcalories.stream().max(Comparator.comparingInt(a -> a)).orElseThrow());

        List<Integer> sorted = elfcalories.stream().sorted(Comparator.comparingInt(a -> -a)).toList();

        System.out.println("part 2: " + (sorted.get(0) + sorted.get(1) + sorted.get(2)));
    }


    public static void main(String[] args) throws IOException {
        Day1 solver = new Day1();
        List<String> lines = Files.lines(Paths.get("./data/2022/day1.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        System.out.println("Running solver...");
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
