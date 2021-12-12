package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class MatchSticks {

    public void matchSticks(String inputFile) throws IOException {
        List<String> input = Files.lines(Paths.get(inputFile))
                .collect(Collectors.toList());

        int totalString = 0;
        int totalCode = 0;
        int totalEncoded = 0;
        for (String line : input) {
            int strlen = line.length();

            boolean esc = false;
            int codelen = strlen - 2; // account for the surrounding double quotes
            for (char c : line.toCharArray()) {
                if (esc) {
                    switch (c) {
                        case '\\', '\"' -> codelen -= 1;
                        case 'x' -> codelen -= 3;
                    }
                    esc = false;
                } else {
                    if (c == '\\') esc = true;
                }
            }

            String encoded = line
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"");
            encoded = "\"" + encoded + "\"";

            totalString += strlen;
            totalCode += codelen;
            totalEncoded += encoded.length();
            System.out.printf("%s->%s %d %d %d %n", line, encoded, strlen, codelen, encoded.length());
        }

        System.out.printf("Part 1: %d%n", totalString - totalCode);
        System.out.printf("Part 2: %d%n", totalEncoded - totalString);
    }

    public static void main(String[] args) throws IOException {
        MatchSticks matchSticks = new MatchSticks();
        matchSticks.matchSticks("./data/2015/matchsticks.txt");
    }


}
