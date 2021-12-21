package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day21 {

    long[] wins = new long[] { 0, 0};
    Map<Integer, Integer> rolls;


    public void solve(List<String> input) {
        int[] pawns = input.stream().map(l -> l.split(":")[1].trim()).mapToInt(Integer::parseInt).toArray();

        part1(Arrays.copyOf(pawns, 2));

        this.rolls = diceRolls(new ArrayDeque<>());
        countAllWins(pawns, new int[] {0, 0}, 0, 1);
        System.out.println("Part 2: " + Math.max(wins[0], wins[1]));
    }

    private void countAllWins(final int[] pawns, final int[] scores, final int p, final long occurs) {
        for (var i = 0; i < 2; i++) {
            if (scores[i] >= 21) {
                this.wins[i] += occurs;
                return;
            }
        }
        this.rolls.forEach((die, roll) -> {
            int[] ss = Arrays.copyOf(scores, 2);
            int[] ps = Arrays.copyOf(pawns, 2);

            ps[p] += die;
            ps[p] %= 10;
            if (ps[p] == 0) ps[p] = 10;
            ss[p] += ps[p];
            countAllWins(ps, ss, p == 0 ? 1 : 0, occurs * roll);
        });
    }

    private Map<Integer, Integer> diceRolls(Deque<Integer> dice) {
        Map<Integer, Integer> rolls = new HashMap<>();
        if (dice.size() == 3) {
            rolls.put(dice.stream().reduce(Integer::sum).orElseThrow(), 1);
            return rolls;
        }
        for (int die = 1; die <= 3; die++) {
            dice.push(die);
            var result = diceRolls(dice);
            dice.pop();
            result.forEach((k, v) -> {
                if (!rolls.containsKey(k)) {
                    rolls.put(k, v);
                } else {
                    rolls.put(k, rolls.get(k) + v);
                }
            });
        }
        return rolls;
    }

    private void part1(int[] pawns) {
        int die = 1;
        int rolls = 0;
        int[] scores = new int[2];
        int player;
        int winner;

        finish:
        while (true) {
            for (player = 0; player < 2; player++) {
                for (int i = 0; i < 3; i++) {
                    pawns[player] += die % 10;
                    if (pawns[player] > 10) pawns[player] = pawns[player] - 10;
                    die++;
                    if (die > 100) die = 1;
                    rolls++;
                }
                scores[player] += pawns[player];
                if (scores[player] >= 1000) {
                    winner = player;
                    break finish;
                }
            }
        }
        System.out.println("Part 1: " + (scores[(winner + 1) % 2] * rolls));
    }


    public static void main(String[] args) throws IOException {
        Day21 solver = new Day21();
        List<String> lines = Files.lines(Paths.get("./data/day21.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
