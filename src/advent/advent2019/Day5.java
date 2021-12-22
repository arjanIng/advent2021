package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Day5 {
    
    public void day5(String filename) throws IOException {
        String input = Files.lines(Paths.get(filename)).collect(Collectors.toList()).get(0);
        long[] program = Arrays.stream(input.split(",")).mapToLong(Long::parseLong).toArray();

        IntCodeMachine machine = new IntCodeMachine("TEST", program);
        machine.getIoDevice().input(1);
        machine.execute().untilHalted();
        System.out.println("Part 1: " + ((BasicIODevice) machine.getIoDevice()).lastOutput());

        machine.reset();
        machine.getIoDevice().input(5);
        machine.execute().untilHalted();
        System.out.println("Part 2: " + ((BasicIODevice) machine.getIoDevice()).lastOutput());

    }

    public static void main(String[] args) throws IOException {
        Day5 day5 = new Day5();
        day5.day5("./data/2019/day5.txt");
    }


}
