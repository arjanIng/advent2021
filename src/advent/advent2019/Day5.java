package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Day5 {
    
    public void day5(String filename) throws IOException {
        String input = Files.lines(Paths.get(filename)).collect(Collectors.toList()).get(0);
        int[] program = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();

        int result = Computer.executeIntCode(program, 1);
        
        System.out.println("Part 1: " + result);
    }

    public static void main(String[] args) throws IOException {
        Day5 day5 = new Day5();
        day5.day5("./data/2019/day5.txt");
    }


}
