package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Day13 {
    Map<String, Map<String, Integer>> personScore = new HashMap<>();

    public void solve(List<String> input) {
        for (String line : input) {
            String[] parts = line.split(" ");
            String name = parts[0];
            personScore.putIfAbsent(name, new HashMap<>());
            int score = Integer.parseInt(parts[3]);
            if (parts[2].equals("lose")) score = -score;
            String partner = parts[10].substring(0, parts[10].length() - 1);
            personScore.get(name).put(partner, score);
        }

        String first = personScore.keySet().stream().findFirst().orElseThrow();
        out.println("Part 1: " + optimalSeating(first, first, personScore.keySet().stream().filter(p -> !p.equals(first)).collect(Collectors.toSet())));

        personScore.put("me", new HashMap<>());
        for (String person: personScore.keySet()) {
            personScore.get(person).put("me", 0);
            personScore.get("me").put(person, 0);
        }
        out.println("Part 2: " + optimalSeating(first, first, personScore.keySet().stream().filter(p -> !p.equals(first)).collect(Collectors.toSet())));
    }

    public int optimalSeating(String first, String from, Collection<String> todo) {
        if (todo.size() == 1) {
            String last = todo.stream().findFirst().orElseThrow();
            return personScore.get(from).get(last) +
                    personScore.get(last).get(from) +
                    personScore.get(last).get(first) +
                    personScore.get(first).get(last);
        } else {
            int max = 0;
            for (String person : todo) {
                int result = personScore.get(from).get(person) + personScore.get(person).get(from);
                result += optimalSeating(first, person, todo.stream().filter(p -> !p.equals(person)).collect(Collectors.toSet()));
                if (result > max) max = result;
            }
            return max;
        }
    }


    public static void main(String[] args) throws IOException {
        Day13 solver = new Day13();
        List<String> lines = Files.lines(Paths.get("./data/2015/day13.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        System.out.println("Running solver...");
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
