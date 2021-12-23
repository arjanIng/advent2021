package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day11 {

    public void solve(List<String> lines) {
        var program = Arrays.stream(lines.get(0).split(",")).mapToLong(Long::parseLong).toArray();

        var robot = new PaintRobot();

        IntCodeMachine machine = new IntCodeMachine("Paintrobot", program);
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

    static class PaintRobot implements IODevice {
        private Pos pos;
        private Map<Pos, Long> painted;
        private Dir direction;
        private boolean moving;

        @Override
        public void input(long input) {
            if (!moving) { // painting
                painted.put(pos, input);
                moving = true;
            } else {
                direction = input == 0 ? direction.previous() : direction.next();
                pos = direction.move(pos);
                moving = false;
            }
        }

        @Override
        public long output() {
            return painted.containsKey(pos) ? painted.get(pos) : 0;
        }

        @Override
        public void reset() {
            pos = new Pos(0, 0);
            painted = new HashMap<>();
            direction = Dir.UP;
            moving = false;
        }

        @Override
        public void setDebugging(boolean debugging) {

        }

        public Map<Pos, Long> getPainted() {
            return painted;
        }

        enum Dir {
            UP, RIGHT, DOWN, LEFT;

            private static final Dir[] vals = values();

            public Dir next() {
                return vals[(this.ordinal() + 1) % vals.length];
            }

            public Dir previous() {
                return vals[(this.ordinal() - 1) < 0 ? vals.length - 1 : this.ordinal() - 1];
            }

            public Pos move(Pos p) {
                return switch (this.ordinal()) {
                    case 0 -> new Pos(p.x, p.y - 1);
                    case 1 -> new Pos(p.x + 1, p.y);
                    case 2 -> new Pos(p.x, p.y + 1);
                    case 3 -> new Pos(p.x - 1, p.y);
                    default -> throw new RuntimeException("Can't move?");
                };
            }
        }

        record Pos(int x, int y) {
        }
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
