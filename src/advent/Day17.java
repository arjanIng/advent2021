package advent;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Day17 {

    public void day17(String filename) throws IOException {
        String input = Files.lines(Paths.get(filename)).collect(Collectors.toList()).get(0);
        
        String[] targetxy = input.split(", ");
        String[] targetxx = targetxy[0].split("=");
        String[] targetx1x2 = targetxx[1].split("\\.\\.");
        int targetx1 = Integer.parseInt(targetx1x2[0]);
        int targetx2 = Integer.parseInt(targetx1x2[1]);
        String[] targetyy = targetxy[1].split("=");
        String[] targety1y2 = targetyy[1].split("\\.\\.");
        int targety1 = Integer.parseInt(targety1y2[0]);
        int targety2 = Integer.parseInt(targety1y2[1]);
        
        // max y velocity is the distance between the ship and the lowest possible value for y in the target area
        int maxvy = -targety1 - 1;
        // calc highest point with this velocity
        int maxHeight = 0;
        for (int y = maxvy; y > 0; y--) {
            maxHeight = maxHeight + y;
        }
        System.out.printf("Part 1: %d%n", maxHeight);

        int hits = 0;
        
        for (int vy = targety1; vy <= maxvy; vy++) {
            for (int vx = 1; vx <= targetx2; vx++) {
                int ivx = vx; int ivy = vy;
                int x = 0; int y = 0;
                while (x < targetx2 && y > targety1) {
                    x = x + ivx;
                    y = y + ivy;
                    if (x >= targetx1 && x <= targetx2 && y >= targety1 && y <= targety2) {
                        hits = hits + 1;
                        break;
                    }
                    ivx = ivx - 1; if (ivx < 0) ivx = 0;
                    ivy = ivy - 1;
                }
            }
        }
        System.out.printf("Part 2: %d%n", hits);
    }

    public static void main(String[] args) throws IOException {
        Day17 day17 = new Day17();
        day17.day17("./data/day17.txt");
    }

}
