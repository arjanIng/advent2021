package advent.advent2021;

import advent.util.Counter;
import advent.util.HashCounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day14 {

    public void octopus(String inputFile) throws IOException {
        List<String> input = Files.lines(Paths.get(inputFile)).collect(Collectors.toList());
        String start = input.get(0);
        
        Map<String, String> rules = new HashMap<>();
        input.subList(2, input.size()).forEach(line -> {
            String[] parts = line.split(" -> ");
            rules.put(parts[0], parts[1]);
        });

        Counter<String> pairCounter = new HashCounter<>();
        for (int i = 0; i < start.length() - 1; i++) {
            String pair = start.substring(i, i + 2);
            pairCounter.add(pair, 1L);
        }
        
        Counter<String> counts = start.chars()
                .mapToObj(c -> new AbstractMap.SimpleEntry<>("" + c, 1L))
                .collect(Counter.collector());

        for (int turn = 0; turn < 40; turn++) {
            pairCounter = pairCounter.stream().map(e -> {
                Map<String, Long> newVals = new HashMap<>();
                char[] pair = e.getKey().toCharArray();
                String c = rules.get(e.getKey());
                counts.put(c, e.getValue());
                newVals.put(e.getKey(), 0L);
                newVals.put(pair[0] + c, e.getValue());
                newVals.put(c + pair[1], e.getValue());
                return newVals;
            }).flatMap(m -> m.entrySet().stream()).collect(Counter.collector());

            if (turn == 9) {
                System.out.printf("Part 1: %d%n", counts.max() - counts.min());
            }
        }
        System.out.printf("Part 2: %d%n", counts.max() - counts.min());
    }

    public static void main(String[] args) throws IOException {
        Day14 lava = new Day14();
        lava.octopus("./data/2021/polymer.txt");
    }

}
