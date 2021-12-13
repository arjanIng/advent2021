package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Folding {

    public void folding(String inputFile) throws IOException {
        List<String> input = Files.lines(Paths.get(inputFile)).collect(Collectors.toList());

        List<Integer[]> dots = new ArrayList<>();
        List<String[]> folds = new ArrayList<>();
        boolean parsingDots = true;
        for (String line : input) {
            if (line.equals("")) {
                parsingDots = false;
                continue;
            }
            if (parsingDots) {
                String[] parts = line.split(",");
                Integer[] coords = new Integer[2];
                coords[0] = Integer.parseInt(parts[0]);
                coords[1] = Integer.parseInt(parts[1]);
                dots.add(new Integer[] {coords[0], coords[1]});
            } else {
                String[] parts = line.split("=");
                folds.add(parts);
            }
        }

        for (String[] fold : folds) {
            int along = Integer.parseInt(fold[1]);
            int axis = fold[0].equals("fold along x") ? 0 : 1;
            dots.stream().filter(d -> d[axis] > along).forEach(d -> d[axis] = along - (d[axis] - along));
            dots = dots.stream().distinct().collect(Collectors.toList());
        }

        System.out.printf("Part 1: %d%n", dots.size());
        System.out.printf("Part 2: %n");
        printMap(dots);
    }

    private void printMap(List<Integer[]> dots) {
        int maxX = dots.stream().map(a -> a[0]).max(Integer::compareTo).orElseThrow() + 1;
        int maxY = dots.stream().map(a -> a[1]).max(Integer::compareTo).orElseThrow() + 1;
        boolean[][] map = new boolean[maxY][maxX];
        for (Integer[] dot : dots) {
            map[dot[1]][dot[0]] = true;
        }

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                System.out.print(map[y][x] ? "█" : " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        Folding folding = new Folding();
        folding.folding("./data/folding.txt");
    }

}
