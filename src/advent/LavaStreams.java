package advent;

import advent.util.IntGrid;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LavaStreams {

    public void lava(String inputFile) throws IOException {
        IntGrid grid = IntGrid.fromFile(inputFile, line -> line.chars()
                .map(Character::getNumericValue));

        AtomicInteger risk = new AtomicInteger();
        List<Integer> sizes = new ArrayList<>();

        grid.stream().forEach(pos -> {
            int lowestNeighbor = grid.stream().filter(n -> n.isNear(pos, 1))
                    .map(IntGrid.Pos::getVal).min(Integer::compareTo).orElse(Integer.MAX_VALUE);
            if (pos.getVal() < lowestNeighbor) {
                risk.addAndGet(pos.getVal() + 1);
                Stack<IntGrid.Pos> toVisit = new Stack<>();
                sizes.add(basinSize(pos, new HashSet<>(), grid));
            }
        });
        sizes.sort(Collections.reverseOrder());
        
        System.out.printf("Part 1: %d%n", risk.get());
        System.out.printf("Part 2: %d%n",
                sizes.get(0) * sizes.get(1) * sizes.get(2));
    }

    private int basinSize(IntGrid.Pos pos, Set<IntGrid.Pos> visited, IntGrid grid) {
        if (pos == null || visited.contains(pos) || pos.getVal() == 9) return 0;
        visited.add(pos);
        return grid.stream().filter(p -> p.isAdjacent(pos, 1))
                .map(p -> basinSize(p, visited, grid))
                .reduce(1, Integer::sum);
    }

    public static void main(String[] args) throws IOException {
        LavaStreams lava = new LavaStreams();
        lava.lava("./data/lava.txt");
    }

}
