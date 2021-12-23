package advent.advent2019;

public class BasicIODevice implements IODevice {
    long[] data = new long[256];
    int idp = 0;
    int odp = 0;
    boolean debugging = false;

    @Override
    public void input(long input) {
        if (debugging) System.out.printf("%s: received input %d", Thread.currentThread().getName(), input);
        data[idp++] = input;
        if (idp >= data.length) idp = 0;
    }

    @Override
    public long output() {
        long output = data[odp++];
        if (odp >= data.length) odp = 0;
        if (debugging) System.out.printf("%s: sending output %d", Thread.currentThread().getName(), output);
        return output;
    }

    public long lastOutput() {
        return data[idp - 1];
    }

    @Override
    public void reset() {
        data = new long[256];
        idp = 0;
        odp = 0;
    }

    @Override
    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
    }
}
