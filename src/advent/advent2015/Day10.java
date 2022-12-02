package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Day10 {

    public void solve(String input) {
        String current = input;
        for (int i = 0; i < 50; i++) {
            StringBuilder output = new StringBuilder();
            char curchar = current.toCharArray()[0];
            int count = 1;
            for (char c : current.substring(1).toCharArray()) {
                if (c != curchar) {
                    output.append(count).append(curchar);
                    curchar = c;
                    count = 1;
                } else {
                    count++;
                }
            }
            output.append(count).append(curchar);
            current = output.toString();
            if (i == 39) out.println("Part 1: " + current.length());
        }

        out.println("Part 2: " + current.length());
    }


    public static void main(String[] args) throws IOException {
        Day10 solver = new Day10();
        long start = System.currentTimeMillis();
        out.println("Running solver...");
        solver.solve("1113122113");
        out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
