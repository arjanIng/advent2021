package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Day15 {
    Map<String, List<Integer>> ingredients = new HashMap<>();
    int maxScore = 0;
    int cal500 = 0;

    public void solve(List<String> input) {
        for (String line : input) {
            String[] parts = line.split(" ");
            String name = strip(parts[0]);
            List<Integer> stats = new ArrayList<>();
            stats.add(Integer.parseInt(strip(parts[2])));
            stats.add(Integer.parseInt(strip(parts[4])));
            stats.add(Integer.parseInt(strip(parts[6])));
            stats.add(Integer.parseInt(strip(parts[8])));
            stats.add(Integer.parseInt(parts[10]));
            ingredients.put(name, stats);
        }

        ratio(0, new int[ingredients.size()]);
        out.println("Part 1: " + maxScore);
        out.println("Part 2: " + cal500);
    }

    private void ratio(int level, int[] teaspoons) {
        if (level == ingredients.size() - 1) {
            teaspoons[ingredients.size() - 1] = calcLeft(ingredients.size(), teaspoons);
            int score = 1;
            int calories = 0;
            for (int s = 0; s < 4; s++) {
                int sum = 0;
                int i = 0;
                for (String ingredient: ingredients.keySet()) {
                    List<Integer> stats = ingredients.get(ingredient);
                    sum += stats.get(s) * teaspoons[i];
                    if (s == 0) calories += stats.get(4) * teaspoons[i];
                    i++;
                }
                if (sum < 0) sum = 0;
                score *= sum;
            }
            if (score > maxScore) maxScore = score;
            if (calories == 500) {
                if (score > cal500) cal500 = score;
            }
        } else {
            int left = calcLeft(level, teaspoons);
            for (int i = 0; i <= left; i++) {
                int[] newTeaspoons = Arrays.copyOf(teaspoons, ingredients.size());
                newTeaspoons[level] = i;
                ratio(level + 1, newTeaspoons);
            }
        }
    }

    private static int calcLeft(int level, int[] teaspoons) {
        int left = 100;
        for (int i = 0; i < level; i++) {
            left -= teaspoons[i];
        }
        return left;
    }

    private String strip(String input) { return input.substring(0, input.length() - 1); }

    public static void main(String[] args) throws IOException {
        Day15 solver = new Day15();
        List<String> lines = Files.lines(Paths.get("./data/2015/day15.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        System.out.println("Running solver...");
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
