package advent;

import advent.util.IntGrid;

import java.io.IOException;
import java.util.Stack;

public class OctopusStreams {

    public void octopus(String inputFile) throws IOException {
        IntGrid grid = IntGrid.fromFile(inputFile, line -> line.chars()
                .map(Character::getNumericValue));

        int totalFlashes = 0;
        int turn = 0;

        Stack<IntGrid.Pos> flashes = new Stack<>();
        while (++turn < Integer.MAX_VALUE) {
            grid = grid.stream().map(p -> p.add(1)).collect(grid.collector());
            grid = findFlashes(grid, flashes);

            while (!flashes.isEmpty()) {
                IntGrid.Pos flash = flashes.pop();
                totalFlashes++;
                grid = grid.stream()
                        .filter(p -> p.isNeighborOf(flash) && p.getVal() != 0)
                        .map(p -> p.add(1)).collect(grid.collector());
                grid = findFlashes(grid, flashes);
            }
            if (turn == 100) {
                System.out.printf("Part 1: %d%n", totalFlashes);
            }
            if (grid.stream().filter(p -> p.getVal() != 0).findAny().isEmpty()) break;
        }
        System.out.printf("Part 2: %d%n", turn);
    }

    private IntGrid findFlashes(IntGrid grid, Stack<IntGrid.Pos> flashes) {
        grid.stream().filter(p -> p.getVal() > 9).forEach(flashes::add);
        return grid.stream().filter(p -> p.getVal() > 9)
                .map(p -> p.newVal(0)).collect(grid.collector());
    }

    public static void main(String[] args) throws IOException {
        OctopusStreams lava = new OctopusStreams();
        lava.octopus("./data/octopus.txt");
    }

}
