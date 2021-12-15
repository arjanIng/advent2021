package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Travel {

    public void travel(String inputFile) throws IOException {
        List<String> input = Files.lines(Paths.get(inputFile))
                .collect(Collectors.toList());


        Set<String> cities = new HashSet<>();
        Set<Connection> connections = new HashSet<>();
        

        for (String line : input) {
            String[] parts = line.split(" = ");
            String[] scities = parts[0].split(" to ");
            cities.add(scities[0]);
            cities.add(scities[1]);
            connections.add(new Connection(scities[0], scities[1], Integer.parseInt(parts[1])));
        }

        for (String start : cities) {
            int distance = 0;
            Set<String> visited = new HashSet<>();
            visited.add(start);
            Stack<String> tryNext = new Stack<>();
            tryNext.addAll(cities.stream().filter(s -> !visited.contains(s)).collect(Collectors.toList()));
            String previous = start;
            System.out.printf("%s ", previous);
            while (!tryNext.isEmpty()) {
                String current = tryNext.pop();
                String finalPrevious = previous;
                int dist = connections.stream().filter(con -> con.between(finalPrevious, current)).findFirst().orElseThrow().distance;
                distance += dist;
                System.out.printf("-> %s ", current);
                visited.add(current);
                previous = current;
                tryNext.addAll(cities.stream().filter(s -> !visited.contains(s)).collect(Collectors.toList()));
            }
            System.out.println(distance);
        }


//        System.out.printf("Part 1: %d%n", totalString - totalCode);
//        System.out.printf("Part 2: %d%n", totalEncoded - totalString);
    }

    class City {
        String name;
    }

    class Connection {
        String origin;
        String destination;
        int distance;

        public Connection(String origin, String destination, int distance) {
            this.origin = origin;
            this.destination = destination;
            this.distance = distance;
        }
        
        public boolean between(String c1, String c2) {
            return (origin.equals(c1) && destination.equals(c2)) || (origin.equals(c2) && destination.equals(c1));
        }
    }

    public static void main(String[] args) throws IOException {
        Travel travel = new Travel();
        travel.travel("./data/2015/traveltest.txt");
    }


}
