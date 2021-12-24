package advent.advent2019;

import java.util.ArrayDeque;
import java.util.Deque;

public class BlockingIOQueue implements IODevice {
    final Deque<Long> queue;
    boolean debugging = false;

    public BlockingIOQueue() {
        this.queue = new ArrayDeque<>();
    }

    @Override
    public synchronized void input(long input) {
        if (debugging) System.out.printf("%s has sent the input %d%n", Thread.currentThread().getName(), input);
        queue.add(input);
        notifyAll();
    }

    @Override
    public synchronized long output() {
        if (debugging) System.out.printf("%s has requested output%n", Thread.currentThread().getName());
        while (queue.isEmpty()) {
            waitForNotify();
        }
        notifyAll();
        if (debugging) System.out.printf("Sending output to %s: %d %n", Thread.currentThread().getName(), queue.peek());
        return queue.remove();
    }

    public synchronized long waitForOutput(long direction) {
        queue.addLast(direction);
        notifyAll();
        while (queue.isEmpty()) {
            waitForNotify();
        }
        return queue.remove();
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
        queue.clear();
    }

    @Override
    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
    }
}

