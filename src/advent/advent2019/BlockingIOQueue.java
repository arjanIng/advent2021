package advent.advent2019;

import java.util.ArrayDeque;
import java.util.Deque;

public class BlockingIOQueue implements IODevice {
    final Deque<Long> inputQueue;
    final Deque<Long> outputQueue;
    boolean debugging = false;

    public BlockingIOQueue() {
        this.inputQueue = new ArrayDeque<>();
        this.outputQueue = new ArrayDeque<>();
    }

    @Override
    public synchronized void input(long input) {
        if (debugging) System.out.printf("%s has sent the input %d%n", Thread.currentThread().getName(), input);
        while (!inputQueue.isEmpty()) {
            waitForNotify();
        }
        inputQueue.addLast(input);
        notifyAll();
    }

    @Override
    public synchronized long output() {
        if (debugging) System.out.printf("%s has requested output%n", Thread.currentThread().getName());
        while (outputQueue.isEmpty()) {
            waitForNotify();
        }
        notifyAll();
        if (debugging) System.out.printf("Sending output to %s: %d %n", Thread.currentThread().getName(), outputQueue.peek());
        return outputQueue.pop();
    }

    public synchronized long waitForOutput(long direction) {
        outputQueue.addLast(direction);
        notifyAll();
        while (inputQueue.isEmpty()) {
            waitForNotify();
        }
        return inputQueue.pop();
    }

    private void waitForNotify() {
        try {
            wait();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void reset() {
        inputQueue.clear();
        outputQueue.clear();
    }

    @Override
    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
    }
}

