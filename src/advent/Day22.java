package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day22 {

    public void solve(List<String> input) {
        List<Cube> cubes = new ArrayList<>();
        input.forEach(l -> {
            String[] parts = l.split(" ");
            int[] coords = Arrays.stream(parts[1].split(","))
                    .map(trio -> trio.split("=")[1])
                    .flatMap(duo -> Arrays.stream(duo.split("\\.\\.")))
                    .mapToInt(Integer::parseInt).toArray();
            cubes.add(new Cube(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5], parts[0].equals("on")));
        });

        Set<Point> points = new HashSet<>();
        cubes.stream().filter(i -> i.x1 >= -50 && i.x1 <= 50).forEach(cube -> {
            Set<Point> change = cube.toPoints();
            if (cube.on) points.addAll(change); else points.removeAll(change);
        });

        System.out.println("Part 1: " + points.size());

        List<Cube> placed = new ArrayList<>();
        for (Cube c : cubes) {
            List<Cube> todo = new ArrayList<>();
            if (c.on) todo.add(c);
            for (Cube p : placed) {
                Optional<Cube> inter = p.intersect(c, !p.on);
                inter.ifPresent(todo::add);
            }
            placed.addAll(todo);
        }

        System.out.println("Part 2: " + placed.stream().mapToLong(Cube::volume).sum());

    }

    record Point(int x, int y, int z) {
    }

    record Cube(int x1, int x2, int y1, int y2, int z1, int z2, boolean on) {

        public long volume() {
            return (x2 - x1 + 1L) * (y2 - y1 + 1L) * (z2 - z1 + 1L) * (on ? 1 : -1);
        }

        public Optional<Cube> intersect(Cube c, boolean on) {
            if (x1 > c.x2 || x2 < c.x1 || y1 > c.y2 ||
                    y2 < c.y1 || z1 > c.z2 || z2 < c.z1) return Optional.empty();
            
            return Optional.of(new Cube(
                    Math.max(x1, c.x1), Math.min(x2, c.x2),
                    Math.max(y1, c.y1), Math.min(y2, c.y2),
                    Math.max(z1, c.z1), Math.min(z2, c.z2), on));
        }

        public Set<Point> toPoints() {
            Set<Point> points = new HashSet<>();
            for (int z = z1; z <= z2; z++) {
                for (int y = y1; y <= y2; y++) {
                    for (int x = x1; x <= x2; x++) {
                        points.add(new Point(x, y, z));
                    }
                }
            }
            return points;
        }
    }

    public static void main(String[] args) throws IOException {
        Day22 solver = new Day22();
        List<String> lines = Files.lines(Paths.get("./data/day22.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
