package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day7 {

    public void solve(List<String> lines) throws IOException {
        var program = Arrays.stream(lines.get(0).split(",")).mapToLong(Long::parseLong).toArray();

        IntCodeMachine machine1 = new IntCodeMachine("day7-part1", program);
        machine1.setDebugging(false);

        long maxResult = allCombinations(new ArrayDeque<>(), ia -> {
            long result = 0;
            for (int i = 0; i < 5; i++){
                machine1.reset();
                machine1.getIoDevice().input(new long[] {ia[i], result} );
                result = machine1.execute().untilHalted().getIoDevice().output();
            }
            return result;
        });
        System.out.println("Part 1: " + maxResult);

        var input = Files.lines(Paths.get("./data/2019/day7.txt")).collect(Collectors.toList());
        var program2 = Arrays.stream(input.get(0).split(",")).mapToLong(Long::parseLong).toArray();

        List<IntCodeMachine> machines = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            var machine = new IntCodeMachine("amp-" + i, program2);
            machines.add(machine);
        }

        maxResult = allCombinations(new ArrayDeque<>(), ia -> {
            machines.forEach(IntCodeMachine::reset);
            for (int i = 0; i < 5; i++) machines.get(i).getIoDevice().input(ia[i] + 5);
            boolean running = true;
            long result = 0;
            while (running) {
                for (var machine : machines) {
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

    private long allCombinations(Deque<Integer> values, Function<Integer[], Long> function) {
        long maxResult = -1;
        if (values.size() == 5) {
            return function.apply(values.toArray(Integer[]::new));
        }
        for (var a = 0; a < 5; a++) {
            values.push(a);
            maxResult = Math.max(maxResult, allCombinations(values, function));
            values.pop();
        }
        return maxResult;
    }

    public static void main(String[] args) throws IOException {
        Day7 solver = new Day7();
        var input = Files.lines(Paths.get("./data/2019/day7.txt")).collect(Collectors.toList());
        solver.solve(input);
    }

}
