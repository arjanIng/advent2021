package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Day9 {

    private int data;

    public void solve(List<String> lines) {
        var program = Arrays.stream(lines.get(0).split(",")).mapToLong(Long::parseLong).toArray();

        IntCodeMachine machine = new IntCodeMachine("day9", program);
        machine.getIoDevice().input(1);
        long result = machine.execute().untilHalted().getIoDevice().output();

        System.out.println("Part 1: " + result);

        machine.reset();
        machine.getIoDevice().input(2);
        result = machine.execute().untilHalted().getIoDevice().output();
        System.out.println("Part 2: " + result);
    }

    public static void main(String[] args) throws IOException {
        Day9 solver = new Day9();
        var input = Files.lines(Paths.get("./data/2019/day9.txt")).collect(Collectors.toList());
        solver.solve(input);
    }


}
