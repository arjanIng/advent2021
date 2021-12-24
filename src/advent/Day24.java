package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day24 {

    List<Instruction> instructions = new ArrayList<>();

    public void solve(List<String> input) {
        input.forEach(line -> {
            String[] parts = line.split(" ");
            Instruction ins;
            if (parts.length == 2) {
                ins = new Instruction(parts[0], parts[1], null);
            } else {
                ins = new Instruction(parts[0], parts[1], parts[2]);
            }
            instructions.add(ins);
        });

        Pair[] pairs = new Pair[14];
        int s = 0;
        for (int i = 0; i < 14; i++) {
            pairs[s] = new Pair(s, Integer.parseInt(instructions.get(5 + i * 18).param2()),
                    Integer.parseInt(instructions.get(15 + i * 18).param2()));
            s++;
        }

        Stack<Pair> stack = new Stack<>();
        stack.push(pairs[0]);
        int i = 1;
        int[] numbers = new int[14];
        int[] smallnumbers = new int[14];
        while (!stack.isEmpty()) {
            if (pairs[i].value >= 0) {
                stack.push(pairs[i]);
            } else {
                Pair popped = stack.pop();
                Pair comparewith = pairs[i];
                int diff = popped.offset() + comparewith.value();
                if (diff < 0) {
                    numbers[popped.index] = 9;
                    numbers[comparewith.index] = 9 + diff;
                } else {
                    numbers[comparewith.index] = 9;
                    numbers[popped.index] = 9 - diff;
                }

                if (diff < 0) {
                    smallnumbers[comparewith.index] = 1;
                    smallnumbers[popped.index] = 1 - diff;
                } else {
                    smallnumbers[popped.index] = 1;
                    smallnumbers[comparewith.index] = 1 + diff;
                }
            }
            i++;
        }
        String answer = Arrays.stream(numbers).mapToObj(String::valueOf).collect(Collectors.joining());
        System.out.println("Part 1: " + answer);

        answer = Arrays.stream(smallnumbers).mapToObj(String::valueOf).collect(Collectors.joining());
        System.out.println("Part 2: " + answer);
    }

    record Pair(int index, int value, int offset) {
    }

    record Instruction(String name, String param1, String param2) {
    }

    public static void main(String[] args) throws IOException {
        Day24 solver = new Day24();
        List<String> lines = Files.lines(Paths.get("./data/day24.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }
}
