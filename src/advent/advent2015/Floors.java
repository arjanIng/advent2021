package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Floors {

    public void floors(String inputFile) throws IOException {
        String input = Files.lines(Paths.get(inputFile))
                .collect(Collectors.joining());
        int floor = 0;
        int firstBasement = -1;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            floor += c == '(' ? 1 : -1;
            if (floor < 0 && firstBasement == -1) firstBasement = i + 1;
        }
        System.out.printf("Part 1: %d%n", floor);
        System.out.printf("Part 2: %d%n", firstBasement);
    }

    public static void main(String[] args) throws IOException {
        Floors lava = new Floors();
        lava.floors("./data/2015/floors.txt");
    }

}
