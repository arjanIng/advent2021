package advent.advent2021;

import advent.util.IOUtil;

import java.util.List;

import static java.lang.String.format;

public class Day2 {
    
    int pos = 0;
    int aim = 0;
    int depth = 0;
    
    public void subMove() {
        List<String> input = IOUtil.getResourceBody("submove.txt");
        
        for (String line : input) {
            String[] parts = line.split(" ");
            int amount = Integer.parseInt(parts[1]);
            switch(parts[0]) {
                case "forward":
                    pos += amount;
                    depth += amount * aim;
                    break;
                case "up":
                    aim -= amount;
                    break;
                case "down":
                    aim += amount;
                    break;
            }
            System.out.printf("command: %s %s, pos: %d, depth: %d%n", parts[0], parts[1], pos, depth);
        }
        System.out.println(format("pos: %d", pos));
        System.out.println(format("depth: %d", depth));
        System.out.println(format("pos * depth: %d", pos * depth));
        
    }
    
    public static void main(String[] args) {
        Day2 day2 = new Day2();
        day2.subMove();
    } 
    
}
