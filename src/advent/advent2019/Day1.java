package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {

    public void solve(List<String> input) {
        long fuel = input.stream().mapToLong(Long::parseLong).map(l -> l / 3 - 2).sum();
        System.out.println("Part 1: " + fuel);

        long moreFuel = 0;
        for (String line : input) {
            fuel = Long.parseLong(line);
            long calc = 0;
            long addFuel = fuel / 3 - 2;
            while (addFuel > 0) {
                calc += addFuel;
                addFuel = addFuel / 3 - 2;
            }
            moreFuel += calc;
        }

        System.out.println("Part 2: " + moreFuel);

    }

    public static void main(String[] args) throws IOException {
        Day1 solver = new Day1();
        List<String> lines = Files.lines(Paths.get("./data/2019/day1.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
