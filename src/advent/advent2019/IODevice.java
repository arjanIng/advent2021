package advent.advent2019;

import javax.naming.OperationNotSupportedException;

public interface IODevice {

    void input(long input);

    long output();

    void reset();

    void setDebugging(boolean debugging);

    default void input(long[] inputs) {
        for (long input : inputs) input(input);
    }

}
