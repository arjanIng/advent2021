package advent.advent2019;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IntCodeMachine {
    private static final int ADD = 1;
    private static final int MUL = 2;
    private static final int INPUT = 3;
    private static final int OUTPUT = 4;
    private static final int JNZ = 5;
    private static final int JZ = 6;
    private static final int SLT = 7;
    private static final int SEQ = 8;
    private static final int RELBASE = 9;
    private static final int HALT = 99;

    private static final int ONE_KB = 1024;
    private static final int ONE_MB = ONE_KB * ONE_KB;

    private static final Map<Integer, Instruction> INSTRUCTIONS = new HashMap<>();

    static {
        INSTRUCTIONS.put(ADD, new Instruction("ADD", ADD, 3));
        INSTRUCTIONS.put(MUL, new Instruction("MUL", MUL, 3));
        INSTRUCTIONS.put(INPUT, new Instruction("INPUT", INPUT, 1));
        INSTRUCTIONS.put(OUTPUT, new Instruction("OUTPUT", OUTPUT, 1));
        INSTRUCTIONS.put(JNZ, new Instruction("JNZ", JNZ, 2));
        INSTRUCTIONS.put(JZ, new Instruction("JZ", JZ, 2));
        INSTRUCTIONS.put(SLT, new Instruction("SLT", SLT, 3));
        INSTRUCTIONS.put(SEQ, new Instruction("SEQ", SEQ, 3));
        INSTRUCTIONS.put(RELBASE, new Instruction("RELBASE", RELBASE, 1));
        INSTRUCTIONS.put(HALT, new Instruction("HALT", HALT, 0));
    }

    private final String name;
    private final long[] code;
    private IODevice ioDevice;
    private ExecutorService executor;

    private boolean debugging;
    private long[] mem;
    private int pc = 0;
    private int relbase = 0;
    private boolean halted = false;

    public IntCodeMachine(String name, long[] code) {
        this.name = name;
        this.code = code;
        this.ioDevice = new BasicIODevice();
        reset();
    }

    public void executeThreaded() {
        executor = Executors.newSingleThreadExecutor();
        executor.execute(this::execute);
    }

    public IntCodeMachine execute() {
        Thread.currentThread().setName("IntCodeMachine[" + name + "]");
        while (!halted) {
            int opcode = (int) mem[pc];
            if (!INSTRUCTIONS.containsKey(opcode % 100)) {
                System.out.printf("%s: Unknown opcode: %d at address %d%n", name, opcode, pc);
                this.halted = true;
                return this;
            }
            Instruction ins = INSTRUCTIONS.get(opcode % 100);
            long[] params = new long[ins.numParams];
            int[] modes = new int[ins.numParams];
            StringBuilder flags = new StringBuilder(String.valueOf(opcode));

            int pad = 5 - flags.length();
            for (int i = 0; i < pad; i++) flags.insert(0, "0");
            flags.setLength(3);

            for (int i = 0; i < ins.numParams; i++) {
                params[i] = mem[pc + i + 1];
                modes[i] = Integer.parseInt(String.valueOf(flags.charAt(2 - i)));
            }
            if (debugging)
                System.out.printf("%s: %04d %02d %-7s %s [%s] -> ", Thread.currentThread().getName(), pc, ins.operation, ins.name, flags,
                        Arrays.stream(params).mapToObj(String::valueOf).collect(Collectors.joining(",")));
            long[] state = Arrays.copyOf(mem, mem.length);
            int pcstate = pc;
            int relbasestate = relbase;
            switch (ins.operation) {
                case ADD -> poke(params[2], peek(params[0], modes[0]) + peek(params[1], modes[1]), modes[2]);
                case MUL -> poke(params[2], peek(params[0], modes[0]) * peek(params[1], modes[1]), modes[2]);
                case INPUT -> {
                    long input = ioDevice.output();
                    if (debugging) System.out.printf("Storing input of %d to %d. ", input, params[0]);
                    poke(params[0], input, modes[0]);
                }
                case OUTPUT -> {
                    long output = peek(params[0], modes[0]);
                    if (debugging) System.out.printf("OUTPUT: %s.", output);
                    ioDevice.input(output);
                    pc += ins.numParams + 1;
                    if (executor == null) return this;
                }
                case JNZ -> {
                    if (peek(params[0], modes[0]) != 0) pc = (int) peek(params[1], modes[1]);
                }
                case JZ -> {
                    if (peek(params[0], modes[0]) == 0) pc = (int) peek(params[1], modes[1]);
                }
                case SLT -> poke(params[2], (peek(params[0], modes[0]) < peek(params[1], modes[1])) ? 1 : 0, modes[2]);
                case SEQ -> poke(params[2], (peek(params[0], modes[0]) == peek(params[1], modes[1])) ? 1 : 0, modes[2]);
                case RELBASE -> this.relbase += peek(params[0], modes[0]);
                case HALT -> {
                    halted = true;
                    if (debugging) System.out.println("bye");
                }
                default -> throw new RuntimeException("Unknown opcode");
            }
            if (debugging) {
                for (int i = 0; i < mem.length; i++) {
                    if (state[i] != mem[i])
                        System.out.printf("State of %d has changed from %d to %d. ", i, state[i], mem[i]);
                }
                if (relbasestate != relbase)
                    System.out.printf("Relative base has changed from %d to %d. ", relbasestate, relbase);
                if (pcstate != pc) System.out.printf("Program counter has changed from %d to %d. ", pcstate, pc);
                System.out.println();
            }
            if (pcstate == pc) pc += ins.numParams + 1;
        }
        return this;
    }

    public void poke(long addr, long value, int mode) {
        switch (mode) {
            case 0, 1 -> mem[(int) addr] = value;
            case 2 -> mem[(int) (relbase + addr)] = value;
            default -> throw new RuntimeException("Unknown address mode");
        }
    }

    public long peek(long param, int mode) {
        switch (mode) {
            case 0 -> {
                return mem[(int) param];
            }
            case 1 -> {
                return param;
            }
            case 2 -> {
                return mem[(int) (relbase + param)];
            }
            default -> throw new RuntimeException("Unknown address mode");
        }
    }

    public IntCodeMachine untilHalted() {
        while (!halted) execute();
        return this;
    }

    public IODevice getIoDevice() {
        return ioDevice;
    }

    public void setIoDevice(IODevice ioDevice) {
        this.ioDevice = ioDevice;
        this.ioDevice.reset();
    }

    public boolean isHalted() {
        return halted;
    }

    public void halt() {
        halted = true;
        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
    }

    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
    }

    public void reset() {
        mem = Arrays.copyOf(code, ONE_MB);
        pc = 0;
        relbase = 0;
        halted = false;
        ioDevice.reset();
    }

    static record Instruction(String name, int operation, int numParams) {
    }

}
