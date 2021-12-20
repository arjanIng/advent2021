package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Day7 {

    private int data;

    public void solve(List<String> lines) throws IOException {
        var program = Arrays.stream(lines.get(0).split(",")).mapToInt(Integer::parseInt).toArray();

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
                            var result = IntCode.execute(program, new int[]{a, 0}, false);
                            result = IntCode.execute(program, new int[]{b, result}, false);
                            result = IntCode.execute(program, new int[]{c, result}, false);
                            result = IntCode.execute(program, new int[]{d, result}, false);
                            result = IntCode.execute(program, new int[]{e, result}, false);

                            //System.out.printf("Setting %d, %d, %d, %d, %d, result %d%n", a, b, c, d, e, result);

                            maxResult = Math.max(maxResult, result);
                        }
                    }
                }
            }
        }
        System.out.println("Part 1: " + maxResult);

        var input = Files.lines(Paths.get("./data/2019/day7.txt")).collect(Collectors.toList());
        var program2 = Arrays.stream(input.get(0).split(",")).mapToLong(Long::parseLong).toArray();

        List<IntCodeMachine> machines = new ArrayList<>();
        List<Amplifier> amplifiers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Amplifier amp = new Amplifier();
            amplifiers.add(amp);
            var machine = new IntCodeMachine("amp-" + i, program2);
            machine.setIoDevice(amp);
            machine.setDebugging(false);
            machines.add(machine);
        }
        maxResult = -1;
        for (var a = 5; a < 10; a++) {
            for (var b = 5; b < 10; b++) {
                if (b == a) continue;
                for (var c = 5; c < 10; c++) {
                    if (c == a || c == b) continue;
                    for (var d = 5; d < 10; d++) {
                        if (d == a || d == b || d == c) continue;
                        for (var e = 5; e < 10; e++) {
                            if (e == a || e ==b || e == c || e == d) continue;

                            machines.forEach(IntCodeMachine::reset);

                            amplifiers.get(0).input(a);
                            amplifiers.get(1).input(b);
                            amplifiers.get(2).input(c);
                            amplifiers.get(3).input(d);
                            amplifiers.get(4).input(e);

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
                            maxResult = Math.max(maxResult, result);
                        }
                    }
                }
            }
        }
        System.out.println("Part 2: " + maxResult);

    }

    public static void main(String[] args) throws IOException {
        Day7 solver = new Day7();
        var input = Files.lines(Paths.get("./data/2019/day7.txt")).collect(Collectors.toList());
        solver.solve(input);
    }


}
