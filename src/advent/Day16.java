package advent;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Day16 {

    int versionSum = 0;
    List<BiFunction<Long, Long, Long>> operations = new ArrayList<>();

    public void day16(String filename) throws IOException {
        String input = Files.lines(Paths.get(filename)).collect(Collectors.toList()).get(0);
        
        operations.add(Long::sum);
        operations.add((a, b) -> a * b);
        operations.add((a, b) -> a < b ? a : b);
        operations.add((a, b) -> a > b ? a : b);
        operations.add((a, b) -> a);
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

        long[] result = calculate(sbuilder.toString(), 0);

        System.out.printf("Part 1: %d%n", versionSum);
        System.out.printf("Part 2: %d%n", result[1]);
    }

    public long[] calculate(String sbits, int start) {
        int pos = start;
        int version = Integer.parseInt(sbits.substring(pos, pos + 3), 2);
        int typeId = Integer.parseInt(sbits.substring(pos + 3, pos + 6), 2);
        versionSum += version;
        pos += 6;
        if (typeId == 4) {
            boolean ending = false;
            StringBuilder data = new StringBuilder();
            while (!ending) {
                String part = sbits.substring(pos, pos + 5);
                if (part.startsWith("0")) ending = true;
                data.append(part.substring(1));
                pos += 5;
            }
            long idata = Long.parseLong(data.toString(), 2); 
            return new long[]{pos - start, idata};
        } else {
            int lengthTypeId = Integer.parseInt(sbits.substring(pos, pos + 1), 2);
            int l = lengthTypeId == 0 ? 15 : 11;
            int length = Integer.parseInt(sbits.substring(pos + 1, pos + 1 + l), 2);
            pos += l + 1;
            List<Long> results = new ArrayList<>();
            if (lengthTypeId == 0) {
                while (length > 0) {
                    long[] r = calculate(sbits, pos);
                    pos += r[0];
                    length -= r[0];
                    results.add(r[1]);
                }
            } else {
                for (int i = 0; i < length; i++) {
                    long[] r = calculate(sbits, pos);
                    pos += r[0];
                    results.add(r[1]);
                }
            }
            Long result;
            if (typeId < 4) {
                result = results.stream().reduce((a, b) -> operations.get(typeId).apply(a, b)).orElseThrow();
            } else {
                result = operations.get(typeId).apply(results.get(0), results.get(1));
            }
            return new long[] {pos - start, result};
        }
    }

    public static void main(String[] args) throws IOException {
        Day16 day16 = new Day16();
        day16.day16("./data/day16.txt");
    }

}
