package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Day2 {
    
    public void day2(String filename) throws IOException {
        String input = Files.lines(Paths.get(filename)).collect(Collectors.toList()).get(0);
        
        int[] program = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
        
        program[1] = 12;
        program[2] = 2;

        int result = IntCode.execute(program);
        System.out.println("Part 1: " + result);
        
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                program[1] = noun;
                program[2] = verb;
                result = IntCode.execute(program);
                if (result == 19690720) {
                    System.out.println("Part 2: " + ((100 * noun) + verb));
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Day2 day2 = new Day2();
        day2.day2("./data/2019/day2.txt");
    }


}
