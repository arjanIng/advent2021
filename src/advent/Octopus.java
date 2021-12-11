package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Octopus {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private int[][] map;

    public void octopus(String inputFile) throws IOException {
        List<String> input = Files.lines(Paths.get(inputFile))
                .collect(Collectors.toList());

        map = input.stream().map(line -> line.chars()
                .map(Character::getNumericValue).toArray())
                .toArray(size -> new int[size][0]);

        int totalFlashes = 0;
        int turn = 0;

        printMap(turn, totalFlashes);
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
        }
        printMap(turn, totalFlashes);
        System.out.printf("Part 1: %d%n", totalFlashes);
        System.out.printf("Part 2: %d%n", turn - 1);
    }

    private void detectFlash(Stack<Flash> flashes, int r, int c) {
        if (map[r][c] > 9) {
            map[r][c] = 0;
            flashes.add(new Flash(r, c));
        }
    }

    private void printMap(int turn, int flashes) {
        System.out.printf("turn: %d, flashes: %d%n", turn, flashes);
        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map.length; c++) {
                if (map[r][c] == 0) {
                    System.out.print(ANSI_BLUE + map[r][c] + ANSI_RESET);
                } else {
                    System.out.print(map[r][c]);
                }
            }
            System.out.println();
        }
        System.out.println();
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
