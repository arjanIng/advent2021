package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Travel {

    public void travel(String inputFile) throws IOException {
        List<String> input = Files.lines(Paths.get(inputFile))
                .collect(Collectors.toList());

        Set<City> cities = new HashSet<>();
        //Map<City, City> Connection =
        for (String line : input) {
            String[] parts = line.split(" = ");

        }

//        System.out.printf("Part 1: %d%n", totalString - totalCode);
//        System.out.printf("Part 2: %d%n", totalEncoded - totalString);
    }

    class City {
        String name;
        boolean visited;
    }

    class Connection {
        String origin;
        String destination;
        int distance;
    }

    public static void main(String[] args) throws IOException {
        Travel travel = new Travel();
        travel.travel("./data/2015/traveltest.txt");
    }


}
