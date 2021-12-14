package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Polymer {

    public void octopus(String inputFile) throws IOException {
        List<String> input = Files.lines(Paths.get(inputFile)).collect(Collectors.toList());
        String polymer = input.get(0);
        
        Map<String, String> translation = new HashMap<>();
        Map<String, Long> pairs = new HashMap<>();
        Map<String, Long> finalPairs = pairs;
        input.subList(2, input.size()).forEach(line -> {
            String[] parts = line.split(" -> ");
            translation.put(parts[0], parts[1]);
            finalPairs.put(parts[0], 0L);
        });
        
        for (int i = 0; i < polymer.length() - 1; i++) {
            String pair = polymer.substring(i, i + 2);
            pairs.put(pair, pairs.get(pair) + 1);
        }
        
        Map<String, Long> counts = new HashMap<>();

        for (String type : translation.values().stream().distinct().collect(Collectors.toList())) {
            long count = polymer.chars().filter(c -> c == type.charAt(0)).count();
            counts.put(type, count);
        }
        
        for (int turn = 0; turn < 40; turn++) {
            Map<String, Long> newPairs = new HashMap<>();
            translation.keySet().forEach(key -> newPairs.put(key, 0L));
            pairs.forEach((pair, value) -> {
                if (value > 0) {
                    String insert = translation.get(pair);
                    counts.put(insert, counts.get(insert) + value);
                    String[] addPairs = new String[]{pair.charAt(0) + insert, insert + pair.charAt(1)};
                    newPairs.put(addPairs[0], value + newPairs.get(addPairs[0]));
                    newPairs.put(addPairs[1], value + newPairs.get(addPairs[1]));
                }
            });
            pairs = newPairs;

            if (turn == 9) {
                long lowest = counts.values().stream().min(Comparator.naturalOrder()).orElseThrow();
                long highest =counts.values().stream().max(Comparator.naturalOrder()).orElseThrow();
                System.out.printf("Part 1: %d%n", highest - lowest);
            }
        }
        long lowest = counts.values().stream().min(Comparator.naturalOrder()).orElseThrow();
        long highest =counts.values().stream().max(Comparator.naturalOrder()).orElseThrow();
        
        System.out.printf("Part 2: %d%n", highest - lowest);
    }

    public static void main(String[] args) throws IOException {
        Polymer lava = new Polymer();
        lava.octopus("./data/polymer.txt");
    }

}
