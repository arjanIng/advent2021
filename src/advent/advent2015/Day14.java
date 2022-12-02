package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Day14 {
    Map<String, List<Integer>> reindeer = new HashMap<>();

    public void solve(List<String> input) {
        for (String line : input) {
            String[] parts = line.split(" ");
            String name = parts[0];
            List<Integer> stats = new ArrayList<>();
            stats.add(Integer.parseInt(parts[3]));  // 0: km/s
            stats.add(Integer.parseInt(parts[6]));  // 1: duration
            stats.add(Integer.parseInt(parts[13])); // 2: rest
            stats.add(-stats.get(2)); // 3: flying until
            stats.add(0); // 4: km traveled
            stats.add(0); // 5: points scored
            reindeer.put(name, stats);
        }

        race(2503);
        int max = 0;
        for (String name : reindeer.keySet()) {
            if (reindeer.get(name).get(4) > max) max = reindeer.get(name).get(4);
        }
        out.println("Part 1: " + max);
        int pointmax = 0;
        for (String name : reindeer.keySet()) {
            if (reindeer.get(name).get(5) > pointmax) pointmax = reindeer.get(name).get(5);
        }
        out.println("Part 2: " + pointmax);
    }

    public void race(int time) {
        for (int t = 1; t <= time; t++) {
            for (String name : reindeer.keySet()) {
                List<Integer> stats = reindeer.get(name);
                if (stats.get(3) == -stats.get(2)) stats.set(3, stats.get(1));
                if (stats.get(3) > 0) stats.set(4, stats.get(4) + stats.get(0));

                stats.set(3, stats.get(3) - 1);

                out.printf("%d. Reindeer %s: %d km (%d), points: %d%n", t, name, stats.get(4), stats.get(3), stats.get(5));
            }
            List<String> leads = calcLeads();
            out.println("leads: " + leads);
            for (String lead: leads) reindeer.get(lead).set(5, reindeer.get(lead).get(5) + 1);
        }
    }

    private List<String> calcLeads() {
        int max = 0;
        List<String> leads = new ArrayList<>();
        for (String name : reindeer.keySet()) {
            List<Integer> stats = reindeer.get(name);
            if (stats.get(4) > max) max = stats.get(4);
        }
        for (String name : reindeer.keySet()) {
            List<Integer> stats = reindeer.get(name);
            if (stats.get(4) == max) leads.add(name);
        }

        return leads;
    }

    public static void main(String[] args) throws IOException {
        Day14 solver = new Day14();
        List<String> lines = Files.lines(Paths.get("./data/2015/day14.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        System.out.println("Running solver...");
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
