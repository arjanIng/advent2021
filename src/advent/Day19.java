package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day19 {

    public void solve(List<String> input) {

        List<Scanner> scanners = new ArrayList<>();
        Scanner current = null;
        for (String line : input) {
            if (line.isEmpty()) continue;
            if (line.startsWith("--- scanner")) {
                current = new Scanner(line.split("---")[1], new ArrayList<>());
                scanners.add(current);
            } else {
                String[] dists = line.split(",");
                Pos signal = new Pos(Integer.parseInt(dists[0]), Integer.parseInt(dists[1]), Integer.parseInt(dists[2]));
                assert current != null;
                current.signals.add(signal);
            }
        }

        Scanner zero = scanners.remove(0);

        List<Scanner> distances = new ArrayList<>();
        while(!scanners.isEmpty()) {
            outer:
            for (Scanner scanner : scanners) {
                System.out.println("Scanners: " + scanners.size());
                List<Scanner> rotations = scanner.getRotations();
                for (Scanner rot : rotations) {
                    HashMap<Pos, Integer> numEqualDiffs = new HashMap<>();

                    for (Pos p1 : zero.signals) {
                        for (Pos p2 : rot.signals) {
                            Pos diff = p1.subtract(p2);
                            if (numEqualDiffs.containsKey(diff)) {
                                numEqualDiffs.put(diff, numEqualDiffs.get(diff) + 1);
                            } else {
                                numEqualDiffs.put(diff, 1);
                            }
                        }
                    }

                    List<Map.Entry<Pos, Integer>> trans = numEqualDiffs.entrySet().stream().filter(i -> i.getValue() >= 12).collect(Collectors.toList());
                    if (trans.size() == 1) {
                        // found orientation
                        zero.add(rot, trans.get(0).getKey());
                        scanner.pos = trans.get(0).getKey();
                        distances.add(scanner);
                        scanners.remove(scanner);
                        break outer;
                    }
                }
            }
        }

        System.out.println("Part 1: " + zero.signals.size());

        int maxDist = -1;
        for (int i = 0; i < distances.size() - 1; i++) {
            for (int j = i; j < distances.size(); j++) {
                Scanner s1 = distances.get(i);
                Scanner s2 = distances.get(j);
                maxDist = Math.max(maxDist, s1.pos.distance(s2.pos));
            }
        }

        System.out.println("Part 2: " + maxDist);

    }


    static Pos roll(Pos c) {
        return new Pos(c.x, c.z, -c.y);
    }

    static Pos turn(Pos c) {
        return new Pos(-c.y, c.x, c.z);
    }

    // https://stackoverflow.com/questions/16452383/how-to-get-all-24-rotations-of-a-3-dimensional-array
    static List<Pos> sequence(Pos pos) {
        List<Pos> r = new ArrayList<>();
        for (int c = 0; c < 2; c++) {
            for (int s = 0; s < 3; s++) {
                pos = roll(pos);
                r.add(pos);
                for (int i = 0; i < 3; i++) {
                    pos = turn(pos);
                    r.add(pos);
                }
            }
            pos = roll(turn(roll(pos)));
        }
        return r;
    }

    static class Pos implements Comparable<Pos> {
        int x;
        int y;
        int z;

        public Pos(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        int[] asArray() {
            return new int[]{x, y, z};
        }

        Pos subtract(Pos other) {
            return new Pos(x - other.x, y - other.y, z - other.z);
        }

        public Pos sum(Pos other) {
            return new Pos(x + other.x, y + other.y, z + other.z);
        }

        @Override
        public int compareTo(Pos o) {
            return Arrays.compare(asArray(), o.asArray());
        }

        public int distance(Pos other) {
            int dx = Math.abs(x - other.x);
            int dy = Math.abs(y - other.y);
            int dz = Math.abs(z - other.z);
            return dx + dy + dz;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pos pos = (Pos) o;
            return x == pos.x && y == pos.y && z == pos.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    static class Scanner {
        String name;
        Pos pos;
        List<Pos> signals;

        public Scanner(String name, List<Pos> signals) {
            this.name = name;
            this.signals = signals;
            pos = new Pos(0, 0, 0);
        }

        public void add(Scanner s, Pos trans) {
            for (Pos p : s.signals) {
                Pos q = p.sum(trans);
                if (!signals.contains(q)) {
                    signals.add(q);
                }
            }
        }

        public List<Scanner> getRotations() {
            List<List<Pos>> result = new ArrayList<>(24);
            for (int i = 0; i < 24; i++) result.add(new ArrayList<>(signals.size()));
            for (Pos c : signals) {
                var x = sequence(c);
                for (int i = 0; i < 24; i++) result.get(i).add(x.get(i));
            }
            return result.stream().map(s -> new Scanner(name + "-rot", s)).collect(Collectors.toList());
        }

    }

    public static void main(String[] args) throws IOException {
        Day19 solver = new Day19();
        List<String> lines = Files.lines(Paths.get("./data/day19.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
