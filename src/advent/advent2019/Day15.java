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
        droid.setDebugging(false);
        machine.executeThreaded();

        Arrays.stream(grid).forEach(ca -> Arrays.fill(ca, ' '));
        var start = new Pos(0, 0);

        var bestRoute = calcRoutes(start, 1, List.of(start));

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
        assert (oxygenSystem != null);

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
            System.out.printf("after turn %d, rooms: %d", turn, rooms);
            Arrays.stream(grid).map(String::valueOf).forEach(System.out::println);
        }

        System.out.println("Part 2: " + turn);

    }


    char[][] grid = new char[75][75];
    Pos oxygenLocation;

    private List<Pos> calcRoutes(Pos pos, int state, List<Pos> route) {
        if (state == 2) {
            System.out.printf("found route of length %d at pos %d,%d%n", route.size(), pos.x, pos.y);
            Arrays.stream(grid).map(String::valueOf).forEach(System.out::println);
            oxygenLocation = pos;
            return route;
        }
        List<Pos> bestRoute = null;
        char[][] gridCopy = new char[grid.length][grid[0].length];
        for (int y = 0; y < grid.length; y++) gridCopy[y] = Arrays.copyOf(grid[y], grid[y].length);
        gridCopy[pos.y + 40][pos.x + 40] = 'D';
        Arrays.stream(gridCopy).map(String::valueOf).forEach(System.out::println);
        boolean endReached = false;
        for (int dir = 1; dir <= 4; dir++) {
            Pos newPos = pos.move(dir);
            if (route.size() < 2) {
                System.out.printf("Current route: %s%n", newPos);
                Arrays.stream(grid).map(String::valueOf).forEach(System.out::println);
            }
            if (!route.contains(newPos)) {
                int newState = (int) droid.waitForOutput(dir);
                if (newState == 2) { endReached = true; break; }
                grid[newPos.y + 40][newPos.x + 40] = newState == 0 ? '#' : newState == 1 ? '.' : 'O';
                if (newState != 0) {
                    List<Pos> newRoute = new ArrayList<>(route);
                    newRoute.add(newPos);
                    List<Pos> r = calcRoutes(newPos, newState, newRoute);
                    if (bestRoute == null && r != null) {
                        bestRoute = r;
                    } else if (r != null) {
                        if (r.size() < bestRoute.size()) {
                            bestRoute = r;
                        }
                    }

                }
            }
        }
        if (bestRoute == null || endReached) {
            Pos earlier = route.get(route.size() - 2);
            int backDirection = 0;
            if (pos.y > earlier.y) backDirection = 1;
            if (pos.y < earlier.y) backDirection = 2;
            if (pos.x > earlier.x) backDirection = 3;
            if (pos.x < earlier.x) backDirection = 4;

            int backstatus = (int) droid.waitForOutput(backDirection);
            assert (backstatus == 1);
        }

        return bestRoute;
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
