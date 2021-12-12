package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.addAll;

/**
 * UGLY
 */
public class Caves {
    Map<String, Cave> caves = new HashMap<>();

    public void caves(String fileName) throws IOException {
        List<String> input = Files.lines(Paths.get(fileName)).collect(Collectors.toList());

        for (String line : input) {
            String[] parts = line.split("-");
            Cave cave1 = findOrCreateCave(parts[0]);
            Cave cave2 = findOrCreateCave(parts[1]);
            cave1.connections.add(cave2);
            cave2.connections.add(cave1);
        }

        Cave start = findOrCreateCave("start");
        Cave end = findOrCreateCave("end");

        Set<List<Cave>> routes = new HashSet<>();
        List<Cave> smallCaves = caves.values().stream().filter(c -> !Character.isUpperCase(c.name.toCharArray()[0]) && !c.equals(start) && !c.equals(end)).toList();
        for (Cave visitTwice : smallCaves) {
            routes.addAll(traverse(start, end, new ArrayList<>(), new HashSet<>(), visitTwice, 0));
        }

        for (List<Cave> route : routes) {
            for (Cave cave : route) {
                if (!cave.equals(end)) {
                    System.out.print(cave.name + " -> ");
                } else {
                    System.out.print("end");
                }
            }
            System.out.println();
        }

        System.out.printf("Part 1: %d%n", routes.size());
    }

    private Set<List<Cave>> traverse(Cave current, Cave end, List<Cave> route, Set<Cave> visited, Cave visitTwice, int visits) {
        Set<List<Cave>> routes = new HashSet<>();
        if (current.equals(end)) {
            route.add(current);
            routes.add(route);
            return routes;
        }
        Set<Cave> newVisited = new HashSet<>(visited);
        if (!Character.isUpperCase(current.name.toCharArray()[0])) {
            newVisited.add(current);
        }
        for (Cave cave : current.connections.stream().filter(c -> !newVisited.contains(c) || c.equals(visitTwice) && visits < 2).toList()) {
            List<Cave> newRoute = new ArrayList<>(route);
            newRoute.add(current);
            if (current.equals(visitTwice)) {
                routes.addAll(traverse(cave, end, newRoute, newVisited, visitTwice, visits + 1));
            } else {
                routes.addAll(traverse(cave, end, newRoute, newVisited, visitTwice, visits));
            }
        }
        return routes;
    }

    private Cave findOrCreateCave(String name) {
        if (!caves.containsKey(name)) {
            Cave cave = new Cave();
            cave.name = name;
            caves.put(name, cave);
        }
        return caves.get(name);
    }

    static class Cave {
        String name;
        Set<Cave> connections = new HashSet<>();

        @Override
        public String toString() {
            return "Cave{" +
                    "name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cave cave = (Cave) o;
            return Objects.equals(name, cave.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
    
    public static void main(String[] args) throws IOException {
        Caves caves = new Caves();
        caves.caves("./data/caves.txt");
    }

}
