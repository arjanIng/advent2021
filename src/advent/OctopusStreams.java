package advent;

import advent.util.Grid;
import advent.util.Pos;

import java.io.IOException;
import java.util.Stack;

public class OctopusStreams {

    public void octopus(String inputFile) throws IOException {
        Grid<Integer> grid = new Grid<>();
        
        grid = grid.load(inputFile, line -> line.chars()
                .map(Character::getNumericValue).boxed());

        int totalFlashes = 0;
        int turn = 0;

        Stack<Pos<Integer>> flashPositions = new Stack<>();
        while (++turn < Integer.MAX_VALUE) {
            grid = grid.stream().map(p -> p.newVal(p.val() + 1)).collect(grid.collector());
            grid = findFlashes(grid, flashPositions);

            while (!flashPositions.isEmpty()) {
                Pos<Integer> flashPosition = flashPositions.pop();
                totalFlashes++;
                grid = grid.stream()
                        .filter(p -> p.isAround(flashPosition, 1) && p.val() != 0)
                        .map(p -> p.newVal(p.val() + 1)).collect(grid.collector());
                grid = findFlashes(grid, flashPositions);
            }
            if (turn == 100) {
                System.out.printf("Part 1: %d%n", totalFlashes);
            }
            if (grid.stream().filter(p -> p.val() != 0).findAny().isEmpty()) break;
        }
        System.out.printf("Part 2: %d%n", turn);
    }

    private Grid<Integer> findFlashes(Grid<Integer> grid, Stack<Pos<Integer>> flashes) {
        grid.stream().filter(p -> p.val() > 9).forEach(flashes::add);
        return grid.stream().filter(p -> p.val() > 9)
                .map(p -> p.newVal(0)).collect(grid.collector());
    }

    public static void main(String[] args) throws IOException {
        OctopusStreams lava = new OctopusStreams();
        lava.octopus("./data/octopus.txt");
    }

}
