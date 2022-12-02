package advent.advent2022;

import advent.util.BiMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Day2 {
    private static Map<String, String> COLUMN_A = new HashMap<>();
    private static Map<String, String> COLUMN_B = new HashMap<>();
    private static Map<String, Integer> SHAPE_SCORE = new HashMap<>();
    private static BiMap<String, String> WIN_CONDITION = new BiMap<>();

    static {
        COLUMN_A.put("A", "R");
        COLUMN_A.put("B", "P");
        COLUMN_A.put("C", "S");

        COLUMN_B.put("X", "R");
        COLUMN_B.put("Y", "P");
        COLUMN_B.put("Z", "S");

        SHAPE_SCORE.put("R", 1);
        SHAPE_SCORE.put("P", 2);
        SHAPE_SCORE.put("S", 3);

        WIN_CONDITION.put("R", "P");
        WIN_CONDITION.put("P", "S");
        WIN_CONDITION.put("S", "R");
    }

    public void solve(List<String> lines) {
        int total = 0;
        int total2 = 0;
        for (String line : lines) {
            String[] parts = line.split(" ");
            String challenge = COLUMN_A.get(parts[0]);
            String response = COLUMN_B.get(parts[1]);

            total += calcScore(challenge, response);

            if ("X".equals(parts[1])) response = WIN_CONDITION.reverse().get(challenge);
            if ("Y".equals(parts[1])) response = challenge;
            if ("Z".equals(parts[1])) response = WIN_CONDITION.get(challenge);

            total2 += calcScore(challenge, response);
        }
        out.println("Part 1: " + total);
        out.println("Part 2: " + total2);

    }

    private static int calcScore(String challenge, String response) {
        int score = SHAPE_SCORE.get(response);
        if (challenge.equals(response)) score += 3;
        if (response.equals(WIN_CONDITION.get(challenge))) score += 6;
        return score;
    }


    public static void main(String[] args) throws IOException {
        Day2 solver = new Day2();
        List<String> lines = Files.lines(Paths.get("./data/2022/day2.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        out.println("Running solver...");
        solver.solve(lines);
        out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
