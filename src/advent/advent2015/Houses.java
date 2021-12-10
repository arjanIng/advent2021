package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Houses {

    private Map<String, Integer> delivered = new HashMap<>();
    
    public void houses(String inputFile) throws IOException {
        String input = Files.lines(Paths.get(inputFile))
                .collect(Collectors.joining());

        int[] santa = new int[] {0, 0};
        int[] robosanta = new int[] {0, 0};
        delivered.put("0-0", 2);

        for (int i = 0; i < input.length(); i += 2) {
            moveAndDeliver(santa, input.charAt(i));
            moveAndDeliver(robosanta, input.charAt(i + 1));
        }
        //System.out.printf("Part 1: %d%n", delivered.size());
        System.out.printf("Part 2: %d%n", delivered.size());
    }
    
    private void moveAndDeliver(int[] pos, char c) {
        switch (c) {
            case '<' -> pos[0]--;
            case '>' -> pos[0]++;
            case '^' -> pos[1]--;
            case 'v' -> pos[1]++;
            default -> throw new RuntimeException("error");
        }
        String key = pos[0] + "-" + pos[1];
        if (delivered.containsKey(key)) {
            delivered.put(key, delivered.get(key) + 1);
        } else {
            delivered.put(key, 1);
        }
    }

    public static void main(String[] args) throws IOException {
        Houses lava = new Houses();
        lava.houses("./data/2015/houses.txt");
    }

}
