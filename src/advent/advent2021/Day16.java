package advent.advent2021;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Day16 {

    List<BiFunction<Long, Long, Long>> operations = new ArrayList<>();

    public void day16(String filename) throws IOException {
        String input = Files.lines(Paths.get(filename)).collect(Collectors.toList()).get(0);
        
        operations.add(Long::sum);
        operations.add((a, b) -> a * b);
        operations.add((a, b) -> a < b ? a : b);
        operations.add((a, b) -> a > b ? a : b);
        operations.add((a, b) -> null);
        operations.add((a, b) -> a > b ? 1L : 0L);
        operations.add((a, b) -> a < b ? 1L : 0L);
        operations.add((a, b) -> a.equals(b) ? 1L : 0L);

        StringBuilder sbuilder = new StringBuilder(new BigInteger(input, 16).toString(2));
        if (input.startsWith("0")) {
            sbuilder.insert(0, "0000");
        }
        if (sbuilder.length() % 4 != 0) {
            int add = (sbuilder.length() / 4 + 1) * 4 - sbuilder.length();
            for (int i = 0; i < add; i++) sbuilder.insert(0, "0");
        }

        long result = calculate(sbuilder.toString());
        System.out.printf("Part 1: %d%n", versionSum);
        System.out.printf("Part 2: %d%n", result);
    }

    int versionSum = 0;
    int pos = 0; // not thread safe!
    public long calculate(String sbits) {
        int version = Integer.parseInt(sbits.substring(pos, pos + 3), 2);
        int typeId = Integer.parseInt(sbits.substring(pos + 3, pos + 6), 2);
        versionSum += version;
        pos += 6;
        if (typeId == 4) {
            boolean last = false;
            StringBuilder data = new StringBuilder();
            while (!last) {
                String part = sbits.substring(pos, pos + 5);
                if (part.startsWith("0")) last = true;
                data.append(part.substring(1));
                pos += 5;
            }
            return Long.parseLong(data.toString(), 2);
        } else {
            int lengthTypeId = Integer.parseInt(sbits.substring(pos, pos + 1), 2);
            int l = lengthTypeId == 0 ? 15 : 11;
            int length = Integer.parseInt(sbits.substring(pos + 1, pos + 1 + l), 2);
            pos += l + 1;
            List<Long> results = new ArrayList<>();
            if (lengthTypeId == 0) {
                while (length > 0) {
                    int start = pos;
                    results.add(calculate(sbits));
                    length -= (pos - start);
                }
            } else {
                for (int i = 0; i < length; i++) {
                    results.add(calculate(sbits));
                }
            }
            return results.stream().reduce((a, b) -> operations.get(typeId).apply(a, b)).orElseThrow();
        }
    }

    public static void main(String[] args) throws IOException {
        Day16 day16 = new Day16();
        day16.day16("./data/2021/day16.txt");
    }

}
