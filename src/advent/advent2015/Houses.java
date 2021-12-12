package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Houses {

    public void houses(String inputFile) throws IOException {
        String input = Files.lines(Paths.get(inputFile))
                .collect(Collectors.joining());

        int[] santa = new int[] {0, 0};
        int[] robosanta = new int[] {0, 0};

        System.out.printf("Part 1: %d%n", deliver(input, santa));
        santa = new int[] {0, 0};
        System.out.printf("Part 2: %d%n", deliver(input, santa, robosanta));
    }

    private int deliver(String input, int[]... santas) {
        Set<String> delivered = new HashSet<>();
        delivered.add("0-0");
        for (int i = 0; i < input.length(); i += santas.length) {
            for (int j = 0; j < santas.length; j++) {
                moveAndDeliver(santas[j], input.charAt(j + i), delivered);
            }
        }
        return delivered.size();
    }

    private void moveAndDeliver(int[] pos, char c, Set<String> delivered) {
        switch (c) {
            case '<' -> pos[0]--;
            case '>' -> pos[0]++;
            case '^' -> pos[1]--;
            case 'v' -> pos[1]++;
            default -> throw new RuntimeException("error");
        }
        String key = pos[0] + "-" + pos[1];
        delivered.add(key);
    }

    public static void main(String[] args) throws IOException {
        Houses lava = new Houses();
        lava.houses("./data/2015/houses.txt");
    }

}
