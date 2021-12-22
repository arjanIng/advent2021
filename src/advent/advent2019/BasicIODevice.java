package advent.advent2019;

public class BasicIODevice implements IODevice {
    long[] data = new long[256];
    int idp = 0;
    int odp = 0;

    @Override
    public void input(long input) {
        data[idp++] = input;
        if (idp >= data.length) idp = 0;
    }

    @Override
    public long output() {
        long val = data[odp++];
        if (odp >= data.length) odp = 0;
        return val;
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
}
