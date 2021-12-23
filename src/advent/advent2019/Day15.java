package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day15 {
    BlockingIOQueue droid;

    public void solve(List<String> input) {
        long[] program = Arrays.stream(input.get(0).split(",")).mapToLong(Long::parseLong).toArray();

        IntCodeMachine machine = new IntCodeMachine("Repair", program);
        droid = new BlockingIOQueue();
        machine.setIoDevice(droid);
        machine.executeThreaded();

        Arrays.stream(grid).forEach(ca -> Arrays.fill(ca, ' '));
        var start = new Pos(0, 0);

        calcRoutes(start, -1, 1, List.of(start));

        assert bestRoute != null;
        System.out.println("Part 1: " + (bestRoute.size() - 1));
        machine.halt();

        int rooms = 0;
        Pos oxygenSystem = null;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                char c = grid[y][x];
                if (c == '.') rooms++;
                if (c == 'O') oxygenSystem = new Pos(x, y);
            }
        }
        Set<Pos> frontier = new HashSet<>();
        frontier.add(oxygenSystem);
        int turn = 0;
        while (rooms > 0) {
            Set<Pos> remove = new HashSet<>();
            Set<Pos> add = new HashSet<>();
            for (Pos pos : frontier) {
                for (int dir = 1; dir <= 4; dir++) {
                    Pos newPos = pos.move(dir);
                    if (grid[newPos.y][newPos.x] == '.') {
                        // found a room!
                        add.add(newPos);
                        rooms--;
                        grid[newPos.y][newPos.x] = 'O';
                    }
                }
                remove.add(pos);
            }
            frontier.removeAll(remove);
            frontier.addAll(add);
            turn++;
        }

        System.out.println("Part 2: " + turn);
    }


    char[][] grid = new char[60][60];
    Pos oxygenLocation;
    List<Pos> bestRoute;

    private void calcRoutes(Pos pos, int lastdir, int state, List<Pos> route) {
        if (state == 0) return;
        if (state == 2) {
            oxygenLocation = pos;
            if (bestRoute == null) bestRoute = route; else bestRoute = bestRoute.size() < route.size() ? bestRoute : route;
        }
        for (int dir = 1; dir <= 4; dir++) {
            Pos newPos = pos.move(dir);
            if (!route.contains(newPos)) {
                int newState = (int) droid.waitForOutput(dir);
                grid[newPos.y + 30][newPos.x + 30] = newState == 0 ? '#' : newState == 1 ? '.' : 'O';
                List<Pos> newRoute = new ArrayList<>(route);
                newRoute.add(newPos);
                calcRoutes(newPos, dir, newState, newRoute);
            }
        }
        if (route.size() >= 2) {
            int backstatus = (int) droid.waitForOutput((lastdir % 2) + 1 + ((lastdir / 3) * 2));
            assert (backstatus == 1);
        }

    }

    record Pos(int x, int y) {
        public Pos move(int direction) {
            Pos newPos;
            switch (direction) {
                case 1 -> newPos = new Pos(this.x, this.y - 1);
                case 2 -> newPos = new Pos(this.x, this.y + 1);
                case 3 -> newPos = new Pos(this.x - 1, this.y);
                case 4 -> newPos = new Pos(this.x + 1, this.y);
                default -> throw new RuntimeException("Unknown direction");
            }
            return newPos;
        }
    }

    public static void main(String[] args) throws IOException {
        Day15 solver = new Day15();

        String filename = args.length == 0 ? "./data/2019/day15.txt" : args[0];
        List<String> lines = Files.lines(Paths.get(filename)).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }


}
