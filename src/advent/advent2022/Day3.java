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
        for (String line : lines) {
            

        }
        out.println("Part 1: " + 0);
    }


    public static void main(String[] args) throws IOException {
        Day3 solver = new Day3();
        List<String> lines = Files.lines(Paths.get("./data/2022/day3test.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        out.println("Running solver...");
        solver.solve(lines);
        out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
