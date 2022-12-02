package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Lights {

    public void lights(String inputFile) throws IOException {
        List<String> input = Files.lines(Paths.get(inputFile))
                .collect(Collectors.toList());

        boolean[][] lights1 = new boolean[1000][1000];
        int[][] lights2 = new int[1000][1000];
        for (String line : input) {
            String command;
            if (line.startsWith("turn")) {
                command = line.split(" ")[0] + " " + line.split(" ")[1];
            } else {
                command = line.split(" ")[0];
            }
            String data = line.substring(command.length() + 1);
            String[] coords = data.split(" through ");
            int[] co1 = Arrays.stream(coords[0].split(",")).mapToInt(Integer::parseInt).toArray();
            int[] co2 = Arrays.stream(coords[1].split(",")).mapToInt(Integer::parseInt).toArray();
            
            for (int x = co1[0]; x <= co2[0]; x++) {
                for (int y = co1[1]; y <= co2[1]; y++) {
                    switch (command) {
                        case "turn on" -> { lights2[x][y]++; lights1[x][y] = true; }
                        case "turn off" -> { lights2[x][y] = lights2[x][y] > 0 ? lights2[x][y] - 1 : lights2[x][y]; lights1[x][y] = false; }
                        case "toggle" -> { lights2[x][y] += 2; lights1[x][y] = !lights1[x][y]; }
                    }
                }
            }
        }

        long numOn = 0;
        for (int x = 0; x < 1000; x++) {
            for (int y = 0; y < 1000; y++) {
                if (lights1[x][y]) numOn++;
            }
        }

        long bright = Arrays.stream(lights2).map(line -> Arrays.stream(line).reduce(0, Integer::sum)).reduce(0, Integer::sum);
        System.out.printf("Part 1: %d%n", numOn);
        System.out.printf("Part 2: %d%n", bright);
    }

    public static void main(String[] args) throws IOException {
        Lights lava = new Lights();
        lava.lights("./data/2015/lights.txt");
    }

}
