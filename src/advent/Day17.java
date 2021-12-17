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

    public void day17(List<String> lines) {
        String input = lines.get(0);
        
        String[] targetxy = input.split(", ");
        String[] targetxx = targetxy[0].split("=");
        String[] targetx1x2 = targetxx[1].split("\\.\\.");
        int targetx1 = Integer.parseInt(targetx1x2[0]);
        int targetx2 = Integer.parseInt(targetx1x2[1]);
        String[] targetyy = targetxy[1].split("=");
        String[] targety1y2 = targetyy[1].split("\\.\\.");
        int targety1 = Integer.parseInt(targety1y2[0]);
        int targety2 = Integer.parseInt(targety1y2[1]);
        
        int maxvy = -targety1 - 1;
        int maxHeight = 0;
        int hits = 0;
        for (int vy = targety1; vy <= maxvy; vy++) {
            for (int vx = 1; vx <= targetx2; vx++) {
                int ivx = vx; int ivy = vy;
                int x = 0; int y = 0;
                int shotHeight = 0;
                while (x < targetx2 && y > targety1) {
                    x = x + ivx;
                    y = y + ivy;
                    if (y > shotHeight) {
                        shotHeight = y;
                    }
                    if (x >= targetx1 && x <= targetx2 && y >= targety1 && y <= targety2) {
                        hits++;
                        if (shotHeight > maxHeight) {
                            maxHeight = shotHeight;
                        }
                        break;
                    }
                    ivx = ivx - 1; if (ivx < 0) ivx = 0;
                    ivy = ivy - 1;
                }
            }
        }
        System.out.printf("Part 1: %d%n", maxHeight);
        System.out.printf("Part 2: %d%n", hits);
    }

    public static void main(String[] args) throws IOException {
        Day17 day17 = new Day17();
        List<String> lines = Files.lines(Paths.get("./data/day17alter.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        day17.day17(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
