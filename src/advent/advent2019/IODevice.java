package advent.advent2019;

public interface IODevice {

    void input(long input);

    long output();

    void reset();

    default void input(long[] inputs) {
        for (long input : inputs) input(input);
    }

}
