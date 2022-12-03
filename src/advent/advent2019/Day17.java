package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day17 {

    String commands = """
            A,A,B,C,B,C,B,C,C,A
            L,10,R,8,R,8
            L,10,L,12,R,8,R,10
            R,10,L,12,R,10
            """;

    public void solve(List<String> input) {
        long[] program = Arrays.stream(input.get(0).split(",")).mapToLong(Long::parseLong).toArray();

        IntCodeMachine machine = new IntCodeMachine("ASCII", program);
        StringBuilder sb = new StringBuilder();

        while (!machine.isHalted()) {
            machine.execute();
            long output = machine.getIoDevice().output();
            sb.append((char) output);
        }

        List<String> scaffolds = Arrays.stream(sb.toString().split("\n")).toList();

        int sum = 0;
        for (int r = 1; r < scaffolds.size() - 1; r++) {
            String line = scaffolds.get(r);
            for (int c = 1; c < line.length() - 1; c++) {
                if (line.charAt(c) == '#' && line.charAt(c - 1) == '#' && line.charAt(c + 1) == '#'
                        && scaffolds.get(r - 1).charAt(c) == '#' && scaffolds.get(r + 1).charAt(c) == '#') {
                    sum += r * c;
                }
            }
        }
        System.out.println("Part 1: " + sum);

        machine.reset();
        machine.poke(0, 2, 0);
        BlockingIOQueue io = new BlockingIOQueue();
        machine.setIoDevice(io);
        for (int i = 0; i < commands.length(); i++) {
            io.input(commands.charAt(i));
        }
        machine.execute().untilHalted();

        String output = io.queue.stream().map(l -> String.valueOf((char) l.longValue())).collect(Collectors.joining());

        System.out.println("Part 2: " + io.queue.getLast());
    }


    public static void main(String[] args) throws IOException {
        Day17 solver = new Day17();

        String filename = args.length == 0 ? "./data/2019/day17.txt.txt" : args[0];
        List<String> lines = Files.lines(Paths.get(filename)).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }


}
