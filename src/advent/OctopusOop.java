package advent;

import advent.util.IntGrid;

import java.io.IOException;
import java.util.Stack;

public class OctopusOop {

    private IntGrid grid;

    public void octopus(String inputFile) throws IOException {

        grid = IntGrid.fromFile(inputFile, line -> line.chars().map(Character::getNumericValue));

        int totalFlashes = 0;
        int turn = 0;

        while (++turn < Integer.MAX_VALUE) {
            Stack<IntGrid.Pos> flashes = new Stack<>();
            grid.forAll(pos -> {
                pos.setVal(pos.getVal() + 1);
                if (pos.getVal() > 9) {
                    pos.setVal(0);
                    flashes.add(pos);
                }
                return pos.getVal();
            });
            while (flashes.size() > 0) {
                IntGrid.Pos flashPos = flashes.pop();
                totalFlashes++;
                grid.forNeighbors(flashPos, pos -> {
                    if (pos.getVal() != 0) pos.setVal(pos.getVal() + 1);
                    if (pos.getVal() > 9) {
                        pos.setVal(0);
                        flashes.add(pos);
                    }
                });
            }
            if (turn == 100) {
                System.out.printf("Part 1: %d%n", totalFlashes);
            }
            if (grid.forAll(IntGrid.Pos::getVal) == 0) break; // all zeros
        }
        System.out.printf("Part 2: %d%n", turn);
    }

    public static void main(String[] args) throws IOException {
        OctopusOop lava = new OctopusOop();
        lava.octopus("./data/octopus.txt");
    }

}
