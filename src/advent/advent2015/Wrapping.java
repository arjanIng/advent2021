package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Wrapping {

    public void wrapping(String inputFile) throws IOException {
        List<String> input = Files.lines(Paths.get(inputFile))
                .collect(Collectors.toList());
        
        long area = 0;
        long ribbon = 0;
        for (String line : input) {
            int[] dim = Arrays.stream(line.split("x")).mapToInt(Integer::parseInt).sorted().toArray();
            int s1 = dim[0] * dim[1]; 
            int s2 = dim[1] * dim[2];
            int s3 = dim[0] * dim[2];
            
            area += s1 * 2 + s2 * 2 + s3 * 2 + s1;
            
            ribbon += dim[0] * 2 + dim[1] * 2 + (dim[0] * dim[1] * dim[2]);
        }
        
        System.out.printf("Part 1: %d%n", area);
        System.out.printf("Part 2: %d%n", ribbon);
    }

    public static void main(String[] args) throws IOException {
        Wrapping lava = new Wrapping();
        lava.wrapping("./data/2015/wrapping.txt");
    }

}
