package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day21 {

    public void solve(List<String> input) {
        int die = 1;
        int rolls = 0;
        int[] pawns = new int[2];
        int[] scores = new int[2];
        int player = 0;
        int winner;
        for (String line : input) {
            String[] parts = line.split(":");
            pawns[player] = Integer.parseInt(parts[1].trim());
            player++;
        }

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

        for (String line : input) {
            String[] parts = line.split(":");
            pawns[player] = Integer.parseInt(parts[1].trim());
            scores[player] = 0;
            player++;
        }
        var diceRolls = diceRolls();
        countAllWins(0, diceRolls, pawns[0], pawns[1], 0, 0, 0, 1);
        System.out.println("Part 2: " + Math.max(win0, win1));
    }

    long win0 = 0;
    long win1 = 0;

    private void countAllWins(final int step, Map<Integer, Integer> rolls, final int pawn0, final int pawn1,
                              final int score0, final int score1, final int player, final long numOccurs) {
        if (score0 >= 21) {
            win0 += numOccurs;
            return;
        }
        if (score1 >= 21) {
            win1 += numOccurs;
            return;
        }
        rolls.forEach((die, num) -> {
            int newScore0 = score0, newScore1 = score1, newPawn0 = pawn0, newPawn1 = pawn1;
            if (player == 0) {
                newPawn0 += die;
                if (newPawn0 > 10) newPawn0 = newPawn0 - 10;
                newScore0 += newPawn0;
            } else {
                newPawn1 += die;
                if (newPawn1 > 10) newPawn1 = newPawn1 - 10;
                newScore1 += newPawn1;
            }
            countAllWins(step + 1, rolls, newPawn0, newPawn1, newScore0, newScore1,
                    player == 0 ? 1 : 0, numOccurs * num);
        });
    }

    private Map<Integer, Integer> diceRolls() {
        Map<Integer, Integer> rolls = new HashMap<>();
        for (int die1 = 1; die1 <= 3; die1++) {
            for (int die2 = 1; die2 <= 3; die2++) {
                for (int die3 = 1; die3 <= 3; die3++) {
                    rolls.merge(die1 + die2 + die3, 1, Integer::sum);
                }
            }
        }
        return rolls;
    }

    public static void main(String[] args) throws IOException {
        Day21 solver = new Day21();
        List<String> lines = Files.lines(Paths.get("./data/day21.txt")).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
