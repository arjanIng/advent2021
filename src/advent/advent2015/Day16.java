package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Day16 {
    Map<String, Map<String, Integer>> sues = new HashMap<>();

    public void solve(List<String> input) {
        for (String line : input) {
            String[] parts = line.split(":" , 2);
            String name = parts[0];

            Map<String, Integer> properties = new HashMap<>();

            String[] fields = parts[1].split(", ");
            for (String field : fields) {
                String[] namevalue = field.split(": ");
                properties.put(namevalue[0].trim(), Integer.parseInt(namevalue[1]));
            }
            sues.put(name, properties);
        }

        for (String name : sues.keySet()) {
            Map<String, Integer> properties = sues.get(name);
            boolean correct = true;
            for (Map.Entry<String, Integer> prop: properties.entrySet()) {
                switch(prop.getKey()) {
                    case "children" -> { if (prop.getValue() != 3) correct = false; }
                    case "cats" -> { if (prop.getValue() <= 7) correct = false; }
                    case "samoyeds" -> { if (prop.getValue() != 2) correct = false; }
                    case "pomeranians" -> { if (prop.getValue() >= 3) correct = false; }
                    case "akitas" -> { if (prop.getValue() != 0) correct = false; }
                    case "vizslas" -> { if (prop.getValue() != 0) correct = false; }
                    case "goldfish" -> { if (prop.getValue() >= 5) correct = false; }
                    case "trees" -> { if (prop.getValue() <= 3) correct = false; }
                    case "cars" -> { if (prop.getValue() != 2) correct = false; }
                    case "perfumes" -> { if (prop.getValue() != 1) correct = false; }
                }
            }
            if (correct) out.println("Match found: " + name);
        }

        out.println("Part 1: " + sues);
    }

    public static void main(String[] args) throws IOException {
        Day16 solver = new Day16();
        List<String> lines = Files.lines(Paths.get("./data/2015/day16.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        System.out.println("Running solver...");
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
