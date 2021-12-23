package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Solved by hand
 */
public class Day23 {

    public void solve(List<String> input) {


    }

    public static void main(String[] args) throws IOException {
        Day23 solver = new Day23();
        List<String> lines = Files.lines(Paths.get("./data/day23test.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
