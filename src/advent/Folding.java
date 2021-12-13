package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Folding {

    public void folding(String inputFile) throws IOException {
        List<String> input = Files.lines(Paths.get(inputFile))
                .collect(Collectors.toList());

        List<Dot> dots = new ArrayList<>();
        List<String[]> folds = new ArrayList<>();
        int maxX = 0;
        int maxY = 0;
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
                dots.add(new Dot(coords[0], coords[1]));
            } else {
                // fold along y=7
                String[] parts = line.split("=");
                folds.add(parts);
            }
        }

        for (String[] fold : folds) {
            System.out.printf("size: %d%n", dots.size());
            int along = Integer.parseInt(fold[1]);
            if (fold[0].equals("fold along y")) {
                dots.stream().filter(d -> d.y > along).forEach(d -> d.y = along - (d.y - along));
            }
            if (fold[0].equals("fold along x")) {
                dots.stream().filter(d -> d.x > along).forEach(d -> d.x = along - (d.x - along));
            }
            dots = new ArrayList<>(new HashSet<>(dots));
        }
        

        System.out.printf("Part 1: %d%n", dots.size());
        System.out.printf("Part 2: %n");
        printMap(dots);
    }

    private void printMap(List<Dot> dots) {
        int maxX = dots.stream().map(a -> a.x).max(Integer::compareTo).get() + 1;
        int maxY = dots.stream().map(a -> a.y).max(Integer::compareTo).get() + 1;
        boolean[][] map = new boolean[maxY][maxX];
        for (Dot dot : dots) {
            map[dot.y][dot.x] = true;
        }

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                System.out.print(map[y][x] ? "#" : ".");
            }
            System.out.println();
        }
        System.out.println("---");
    }


    class Dot {
        int x;
        int y;

        public Dot(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Dot dot = (Dot) o;
            return x == dot.x &&
                    y == dot.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static void main(String[] args) throws IOException {
        Folding folding = new Folding();
        folding.folding("./data/folding.txt");
    }

}
