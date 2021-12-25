package advent.advent2021;

import advent.util.Counter;
import advent.util.HashCounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class Day6 {
    
    public void lanterns(String fileName) throws IOException {
        List<String> input = Files.lines(Paths.get(fileName)).collect(Collectors.toList());

        Counter<Integer> ageCounter = new HashCounter<>();
        for (String num : input.get(0).split(",")) {
            ageCounter.add(Integer.parseInt(num), 1L);
        }
        
        for (int day = 1; day <= 256; day++) {
            long numBorn = ageCounter.getOrDefault(0, 0L);
            for (int i = 0; i < 9; i++) {
                long val = ageCounter.getOrDefault(i + 1, 0L);
                ageCounter.put(i, val);
            }
            ageCounter.add(6, numBorn);
            ageCounter.put(8, numBorn);
            if (day == 79) {
                System.out.printf("Part 1: %d%n", ageCounter.sum());
            }
        }
        System.out.printf("Part 2: %d%n", ageCounter.sum());
    }

    public static void main(String[] args) throws IOException {
        Day6 day6 = new Day6();
        day6.lanterns("./data/2021/lantern.txt");
    }


}
