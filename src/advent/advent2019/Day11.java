package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day11 {

    public void solve(List<String> lines) {
        var program = Arrays.stream(lines.get(0).split(",")).mapToLong(Long::parseLong).toArray();

        var robot = new PaintRobot();

        IntCodeMachine machine = new IntCodeMachine("paintrobot", program);
        machine.setIoDevice(robot);

        machine.execute().untilHalted();
        System.out.println("Part 1: " + robot.getPainted().size());

        machine.reset();
        robot.getPainted().put(new PaintRobot.Pos(0, 0), 1L);
        machine.execute().untilHalted();

        Map<PaintRobot.Pos, Long> dots = robot.getPainted();
        int minx = dots.keySet().stream().mapToInt(PaintRobot.Pos::x).min().orElseThrow();
        int maxx = dots.keySet().stream().mapToInt(PaintRobot.Pos::x).max().orElseThrow();
        int miny = dots.keySet().stream().mapToInt(PaintRobot.Pos::y).min().orElseThrow();
        int maxy = dots.keySet().stream().mapToInt(PaintRobot.Pos::y).max().orElseThrow();

        long[][] map = new long[Math.abs(miny) + maxy + 1][Math.abs(minx) + maxx + 1];
        dots.forEach((p, c) -> map[p.y()][p.x()] = c);

        System.out.println("Part 2:");
        Arrays.stream(map).map(l -> Arrays.stream(l).mapToObj(i -> i == 0 ? "." : "#")
                .collect(Collectors.joining())).forEach(System.out::println);

    }

    public static void main(String[] args) throws IOException {
        Day11 solver = new Day11();
        List<String> lines = Files.lines(Paths.get("./data/2019/day11.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        System.out.println("Running solver...");
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }


}
