package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day7 {

    public void solve(List<String> lines) throws IOException {
        var program = Arrays.stream(lines.get(0).split(",")).mapToLong(Long::parseLong).toArray();

        IntCodeMachine part1 = new IntCodeMachine("day7-part1", program);
        part1.setDebugging(false);

        long maxResult = allCombinations(0, ia -> {
            long result = 0;
            for (int i = 0; i < 5; i++){
                part1.reset();
                part1.getIoDevice().input(new long[]{ia[i], result});
                result = part1.execute().untilHalted().getIoDevice().output();
            }
            return result;
        });
        System.out.println("Part 1: " + maxResult);

        var input = Files.lines(Paths.get("./data/2019/day7.txt")).collect(Collectors.toList());
        var program2 = Arrays.stream(input.get(0).split(",")).mapToLong(Long::parseLong).toArray();

        List<IntCodeMachine> machines = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            var machine = new IntCodeMachine("amp-" + i, program2);
            machine.setDebugging(true);
            machines.add(machine);
        }

        maxResult = allCombinations(5, ia -> {
            machines.forEach(IntCodeMachine::reset);
            for (int i = 0; i < 5; i++) {
                machines.get(i).getIoDevice().input(ia[i] + 5);
            }
            boolean running = true;
            long result = 0;
            while (running) {
                for (var machine : machines) {
                    machine.setDebugging(false);
                    machine.getIoDevice().input(result);
                    machine.execute();
                    result = machine.getIoDevice().output();

                    if (machine.isHalted()) running = false;
                }
            }
            return result;
        });
        System.out.println("Part 2: " + maxResult);
    }

    private long allCombinations(int offset, Function<Integer[], Long> function) {
        long maxResult = -1;
        for (var a = 0; a < 5; a++) {
            for (var b = 0; b < 5; b++) {
                if (b == a) continue;
                for (var c = 0; c < 5; c++) {
                    if (c == a || c == b) continue;
                    for (var d = 0; d < 5; d++) {
                        if (d == a || d == b || d == c) continue;
                        for (var e = 0; e < 5; e++) {
                            if (e == a || e == b || e == c || e == d) continue;
                            maxResult = Math.max(maxResult, function.apply(new Integer[] {a, b, c, d, e}));
                        }
                    }
                }
            }
        }
        return maxResult;
    }

    public static void main(String[] args) throws IOException {
        Day7 solver = new Day7();
        var input = Files.lines(Paths.get("./data/2019/day7.txt")).collect(Collectors.toList());
        solver.solve(input);
    }

}
