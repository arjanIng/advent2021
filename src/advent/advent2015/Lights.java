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

        int[][] lights = new int[1000][1000];
        //for (Boolean[] row : lights) Arrays.fill(row, false);
        //turn on 489,959 through 759,964
        //turn off 12,823 through 102,934
        //toggle 756,965 through 812,992
        
        for (String line : input) {
            String command;
            if (line.startsWith("turn")) {
                command = line.split(" ")[0] + " " + line.split(" ")[1];
            } else {
                command = line.split(" ")[0];
            }
            String opcode = line.substring(command.length() + 1);
            String coords[] = opcode.split(" through ");
            int[] co1 = Arrays.stream(coords[0].split(",")).mapToInt(Integer::parseInt).toArray();
            int[] co2 = Arrays.stream(coords[1].split(",")).mapToInt(Integer::parseInt).toArray();
            
            for (int x = co1[0]; x <= co2[0]; x++) {
                for (int y = co1[1]; y <= co2[1]; y++) {
                    switch (command) {
                        case "turn on" -> lights[x][y]++;
                        case "turn off" -> lights[x][y] = lights[x][y] > 0 ? lights[x][y] - 1 : lights[x][y];
                        case "toggle" -> lights[x][y] += 2;
                    }
                }
            }
        }

        long bright = Arrays.stream(lights).map(line -> Arrays.stream(line).reduce(0, Integer::sum)).reduce(0, Integer::sum);
//        System.out.printf("Part 1: %d%n", numOn);
        System.out.printf("Part 2: %d%n", bright);
    }

    public static void main(String[] args) throws IOException {
        Lights lava = new Lights();
        lava.lights("./data/2015/lights.txt");
    }

}
