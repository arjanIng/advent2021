package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Octopus {

    private int[][] map;

    public void octopus(String inputFile) throws IOException {
        List<String> input = Files.lines(Paths.get(inputFile))
                .collect(Collectors.toList());

        map = input.stream().map(line -> line.chars()
                .map(Character::getNumericValue).toArray())
                .toArray(size -> new int[size][0]);

        int totalFlashes = 0;
        int turn = 0;

        while (++turn != Integer.MAX_VALUE) {
            Stack<Flash> flashes = new Stack<>();
            boolean allzeros = true;
            for (int r = 0; r < map.length; r++) {
                for (int c = 0; c < map[r].length; c++) {
                    if (map[r][c] != 0) allzeros = false;
                    map[r][c]++;
                    detectFlash(flashes, r, c);
                }
            }
            if (allzeros) break;
            while (flashes.size() > 0) {
                Flash f = flashes.pop();
                totalFlashes++;
                for (int rr = Math.max(f.r - 1, 0); rr < Math.min(f.r + 2, map.length); rr++) {
                    for (int cc = Math.max(f.c - 1, 0); cc < Math.min(f.c + 2, map[rr].length); cc++) {
                        if (map[rr][cc] != 0) map[rr][cc]++;
                        detectFlash(flashes, rr, cc);
                    }
                }
            }
            if (turn == 100) {
                System.out.printf("Part 1: %d%n", totalFlashes);
            }
        }
        System.out.printf("Part 2: %d%n", turn - 1);
    }

    private void detectFlash(Stack<Flash> flashes, int r, int c) {
        if (map[r][c] > 9) {
            map[r][c] = 0;
            flashes.add(new Flash(r, c));
        }
    }

    class Flash {
        int r;
        int c;

        public Flash(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    public static void main(String[] args) throws IOException {
        Octopus lava = new Octopus();
        lava.octopus("./data/octopus.txt");
    }

}
