package advent;

import advent.util.IntGrid;

import java.io.IOException;
import java.util.Stack;

public class OctopusOop {

    public void octopus(String inputFile) throws IOException {
        IntGrid grid = IntGrid.fromFile(inputFile, line -> line.chars().map(Character::getNumericValue));

        int totalFlashes = 0;
        int turn = 0;

        Stack<IntGrid.Pos> flashes = new Stack<>();
        while (++turn < Integer.MAX_VALUE) {
            grid = grid.forAll(pos -> {
                int val = pos.getVal() + 1;
                if (val > 9) {
                    flashes.add(pos);
                    return 0;
                }
                return val;
            });
            while (flashes.size() > 0) {
                IntGrid.Pos flashPos = flashes.pop();
                totalFlashes++;
                grid = grid.forNeighbors(flashPos, pos -> {
                    int val = pos.getVal();
                    if (val != 0) val++;
                    if (val > 9) {
                        flashes.add(pos);
                        return 0;
                    }
                    return val;
                });
            }
            if (turn == 100) {
                System.out.printf("Part 1: %d%n", totalFlashes);
            }
            if (grid.inspect(IntGrid.Pos::getVal) == 0) break; // all zeros
        }
        System.out.printf("Part 2: %d%n", turn);
    }

    public static void main(String[] args) throws IOException {
        OctopusOop lava = new OctopusOop();
        lava.octopus("./data/octopus.txt");
    }

}
