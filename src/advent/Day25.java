package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day25 {

    public void solve(List<String> input) {
        char[][] map = new char[input.size()][input.get(0).length()];
        for (int r = 0; r < input.size(); r++) {
            String line = input.get(r);
            for (int c = 0; c < input.get(r).length(); c++) {
                map[r][c] = line.charAt(c);
            }
        }

        boolean moving = true;
        int step = 0;
        while (moving) {
            System.out.println("step " + step);
            moving = step(map, '>', new int[]{0, 1});
            moving |= step(map, 'v', new int[]{1, 0});
            step++;
        }
        System.out.println("Part 1: " + step);
    }

    private boolean step(char[][] map, char ch, int[] next) {
        List<Move> toMove = new ArrayList<>();
        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[r].length; c++) {
                if (map[r][c] == ch) {
                    int[] newPos = new int[]{r + next[0], c + next[1]};
                    if (newPos[0] >= map.length) newPos[0] = 0;
                    if (newPos[1] >= map[r].length) newPos[1] = 0;
                    if (map[newPos[0]][newPos[1]] == '.') {
                        toMove.add(new Move(r, c, newPos[0], newPos[1]));
                    }
                }
            }
        }

        for (Move p : toMove) {
            map[p.r2][p.c2] = ch;
            map[p.r][p.c] = '.';
        }
        return !toMove.isEmpty();
    }

    record Move(int r, int c, int r2, int c2) {
    }

    public static void main(String[] args) throws IOException {
        Day25 solver = new Day25();
        List<String> lines = Files.lines(Paths.get("./data/day25.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }
}
