package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Day2 {
    
    public void day2(String filename) throws IOException {
        String input = Files.lines(Paths.get(filename)).collect(Collectors.toList()).get(0);
        
        long[] program = Arrays.stream(input.split(",")).mapToLong(Long::parseLong).toArray();

        IntCodeMachine machine = new IntCodeMachine("day5", program);
        machine.poke(1, 12, 0);
        machine.poke(2, 2, 0);
        machine.execute().untilHalted();
        System.out.println("Part 1: " + machine.peek(0, 0));

        outer:
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                machine.reset();
                machine.poke(1, noun, 0);
                machine.poke(2, verb, 0);
                machine.execute().untilHalted();
                if (machine.peek(0, 0) == 19690720) {
                    System.out.println("Part 2: " + ((100 * noun) + verb));
                    break outer;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Day2 day2 = new Day2();
        day2.day2("./data/2019/day2.txt");
    }


}
