package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class Day16 {
    List<Integer> containers = new ArrayList<>();
    int numSolutions = 0;
    int minContainers = Integer.MAX_VALUE;
    int numMinSolutions = 0;

    public void solve(List<String> input) {
        for (String line : input) {
            containers.add(Integer.parseInt(line));
        }

        permutate(150, Collections.emptyList(), new int[containers.size()], 0);;

        out.println("Part 1: " + numSolutions);
        out.println("Part 2: " + numMinSolutions);
    }

    public void permutate(int eggnog, List<Integer> history, int[] index, int level) {
        if (eggnog == 0) {
            numSolutions++;
            if (history.size() < minContainers) {
                minContainers = history.size();
                numMinSolutions = 0;
            }
            if (history.size() == minContainers) {
                numMinSolutions++;
            }
        } else {
            int start = level == 0 ? 0 : index[level - 1] + 1;
            for (int i = start; i < containers.size(); i++) {
                List<Integer> newHistory = new ArrayList<>(history);
                newHistory.add(containers.get(i));
                int[] newIndex = Arrays.copyOf(index, index.length);
                newIndex[level] = i;

                permutate(eggnog - containers.get(i), newHistory, newIndex, level + 1);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Day16 solver = new Day16();
        List<String> lines = Files.lines(Paths.get("./data/2015/day17.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        System.out.println("Running solver...");
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
