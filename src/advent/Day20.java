package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day20 {

    public void solve(List<String> input) {
        String decoder = input.get(0);
        List<String> image = input.subList(2, input.size());

        char inf = '.';
        for (int turn = 0; turn < 50; turn++) {
            List<String> newImage = new ArrayList<>();
            for (int y = -1; y < image.size() + 1; y++) {
                StringBuilder newRow = new StringBuilder();
                for (int x = -1; x < image.get(0).length() + 1; x++) {
                    int index = 0;
                    int bitmask = 0B100000000;
                    for (int yy = y - 1; yy <= y + 1; yy++) {
                        for (int xx = x - 1; xx <= x + 1; xx++) {
                            char c = isOutside(image, yy, xx) ? inf : image.get(yy).charAt(xx);
                            index |= c == '#' ? bitmask : 0;
                            bitmask >>= 1;
                        }
                    }
                    newRow.append(decoder.charAt(index));
                }
                newImage.add(newRow.toString());
            }
            inf = decoder.charAt(inf == '.' ? 0B000000000 : 0B111111111);
            image = newImage;
            if (turn == 1) {
                System.out.println("Part 1: " + count(image));
            }
        }
        System.out.println("Part 2: " + count(image));
    }

    private boolean isOutside(List<String> image, int yy, int xx) {
        return yy < 0 || yy >= image.size() || xx < 0 || xx >= image.get(0).length();
    }

    private long count(List<String> image) {
        return image.stream().map(line -> line.chars().filter(c -> c == '#').count()).reduce(0L, Long::sum);
    }

    public static void main(String[] args) throws IOException {
        Day20 solver = new Day20();
        List<String> lines = Files.lines(Paths.get("./data/day20.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
