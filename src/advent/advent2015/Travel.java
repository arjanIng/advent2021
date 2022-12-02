package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Travel {
    Map<String, Integer> distance = new HashMap<>();

    public void travel(String inputFile) throws IOException {
        List<String> input = Files.lines(Paths.get(inputFile))
                .collect(Collectors.toList());
        Set<String> cities = new HashSet<>();

        for (String line : input) {
            String[] parts = line.split(" = ");
            String[] scities = parts[0].split(" to ");
            cities.add(scities[0]);
            cities.add(scities[1]);
            distance.put(key(scities[0], scities[1]), Integer.parseInt(parts[1]));
        }

        System.out.printf("Part 1: %d%n", calcDist(null, cities, false));
        System.out.printf("Part 2: %d%n", calcDist(null, cities, true));
    }

    private int calcDist(String from, Set<String> todo, boolean longest) {
        if (todo.size() == 1) {
            return distance(from, todo.stream().findFirst().orElseThrow());
        } else {
            int minmax = longest ? 0 : Integer.MAX_VALUE;
            for (String city : todo) {
                int traveled = from != null ? distance(from, city) : 0;
                int dist = traveled + calcDist(city, todo.stream().filter(c -> !c.equals(city)).collect(Collectors.toSet()), longest);
                if (!longest && dist < minmax) minmax = dist;
                if (longest && dist > minmax) minmax = dist;
            }
            return minmax;
        }
    }

    private int distance(String a, String b) {
        return distance.get(key(a, b));
    }

    private String key(String a, String b) {
        return a.hashCode() > b.hashCode() ? a + b : b + a;
    }

    public static void main(String[] args) throws IOException {
        Travel travel = new Travel();
        travel.travel("./data/2015/travel.txt");
    }


}
