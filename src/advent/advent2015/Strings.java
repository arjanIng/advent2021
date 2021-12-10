package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Strings {

    public void strings(String inputFile) throws IOException {
        List<String> input = Files.lines(Paths.get(inputFile))
                .collect(Collectors.toList());
        
        char[] vowels = "aeiou".toCharArray();
        String[] naughty = new String[] { "ab", "cd", "pq", "xy" };
        
        int nice1 = 0;
        int nice2 = 0;
        for (String line : input) {
            boolean nice;
            int ivowels = 0, idoubles = 0;
            char previous = '!';
            for (char c : line.toCharArray()) {
                for (char v : vowels) {
                    if (c == v) ivowels++;
                }
                if (c == previous) idoubles++;
                previous = c;
            }
            nice = (ivowels >= 3 && idoubles >= 1);
            
            for (String s : naughty) {
                if (line.contains(s)) {
                    nice = false;
                    break;
                }
            }
            if (nice) nice1++;
            
            boolean hasRepeatingPair = false;
            boolean hasPalindrome = false;
            
            char[] chars = line.toCharArray();
            for (int i = 0; i < line.length() - 2; i++) {
                if (chars[i] == chars[i + 2]) hasPalindrome = true;
                for (int j = i + 2; j < line.length() - 1; j++) {
                    if (chars[j] == chars[i] && chars[j + 1] == chars[i + 1]) hasRepeatingPair = true;
                }
            }
            
            if (hasRepeatingPair && hasPalindrome) nice2++;
        }
        
        System.out.printf("Part 1: %d%n", nice1);
        System.out.printf("Part 2: %d%n", nice2);
    }

    public static void main(String[] args) throws IOException {
        Strings lava = new Strings();
        lava.strings("./data/2015/strings.txt");
    }

}
