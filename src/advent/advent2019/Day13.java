package advent.advent2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {
    public void solve(List<String> input) {
        long[] program = Arrays.stream(input.get(0).split(",")).mapToLong(Long::parseLong).toArray();

        IntCodeMachine machine = new IntCodeMachine("arcade", program);
        ArcadeCabinet game = new ArcadeCabinet(44, 23);
        machine.setIoDevice(game);
        machine.execute().untilHalted();
        System.out.println("Part 1: " + game.countElements(2));
        machine.reset();
        machine.poke(0, 2, 0);

        game.setOutputCallback(scr -> {
            int ball = game.ballx();
            int paddle = game.paddlex();

            return Integer.compare(ball, paddle);
        });

        machine.execute().untilHalted();
        System.out.println(game);

        System.out.println("Part 2: " + game.getScore());
    }

    public static void main(String[] args) throws IOException {
        Day13 solver = new Day13();

        System.out.print("\u001B[?25l\u001B[2J"); // hide cursor and erase screen
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.print("\u001B[?25h\u001B[1;24H"), "Shutdown-thread"));


        String filename = "./data/2019/day13.txt";
        if (args.length == 1) filename = args[0];
        List<String> lines = Files.lines(Paths.get(filename)).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        solver.solve(lines);
        System.out.printf("Done after %d millis%n", System.currentTimeMillis() - start);
    }

}
