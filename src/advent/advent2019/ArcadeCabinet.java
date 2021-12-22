package advent.advent2019;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.function.Function;

public class ArcadeCabinet implements IODevice {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    int[][] grid;
    int joystick;
    int ballx;
    int paddlex;
    int score;

    Function<ArcadeCabinet, Integer> outputCallback;

    public ArcadeCabinet(int sizex, int sizey) {
        grid = new int[sizey][sizex];
        joystick = 0;
        ballx = -1;
        paddlex = -1;
    }

    Deque<Integer> inputQueue = new ArrayDeque<>();

    @Override
    public void input(long input) {
        inputQueue.addLast((int) input);

        if (inputQueue.size() >= 3) {
            int x = inputQueue.pop();
            int y = inputQueue.pop();
            int c = inputQueue.pop();
            if (x >= 0) {
                draw(x, y, c);
                if (c == 4) ballx = x;
                if (c == 3) paddlex = x;
            } else {
                if (x == -1 && y == 0) {
                    score = c;
                    drawScore(score);
                }
            }
        }
    }

    public void draw(int x, int y, int c) {
        grid[y][x] = c;
        System.out.print("\u001b[" + (y + 2) + ";" + (x + 1) + "H" + charAt(x, y));
    }

    public void drawScore(int score) {
        System.out.print("\u001b[1;10H" + ANSI_GREEN + "score: " + score + ANSI_RESET);
    }

    public void setOutputCallback(Function<ArcadeCabinet, Integer> inputFunction) {
        this.outputCallback = inputFunction;
    }

    public int getScore() {
        return score;
    }

    @Override
    public long output() {
        return outputCallback.apply(this);
    }

    @Override
    public void reset() {
        for (int[] line : grid) Arrays.fill(line, 0);
    }

    public int ballx() {
        return ballx;
    }

    public int paddlex() {
        return paddlex;
    }



    public int countElements(int i) {
        int count = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == i) count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ANSI_BLUE + "Score: " + score + ANSI_WHITE).append('\n');
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                String s = charAt(x, y);
                sb.append(s);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private String charAt(int x, int y) {
        String s = "";
        switch (grid[y][x]) {
            case 0 -> s = " ";
            case 1 -> s = "\u2588";
            case 2 -> s = ANSI_RED + "\u2591" + ANSI_WHITE;
            case 3 -> s = ANSI_YELLOW + "\u2583" + ANSI_WHITE;
            case 4 -> s = ANSI_GREEN + "\u233d" + ANSI_WHITE;
            default -> throw new RuntimeException("Unknown symbol");
        }
        return s;
    }


}
