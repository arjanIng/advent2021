package advent.advent2019;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class IntCode {
    private static final int ADD = 1;
    private static final int MUL = 2;
    private static final int INPUT = 3;
    private static final int OUTPUT = 4;
    private static final int BEQ = 5;
    private static final int BNE = 6;
    private static final int SLT = 7;
    private static final int SEQ = 8;
    private static final int HALT = 99;
    
    private static final Map<Integer, Instruction> INSTRUCTIONS = new HashMap<>();
    static {
        INSTRUCTIONS.put(ADD, new Instruction("ADD", ADD, 3));
        INSTRUCTIONS.put(MUL, new Instruction("MUL", MUL, 3));
        INSTRUCTIONS.put(INPUT, new Instruction("INPUT", INPUT, 1));
        INSTRUCTIONS.put(OUTPUT, new Instruction("OUTPUT", OUTPUT, 1));
        INSTRUCTIONS.put(BEQ, new Instruction("BEQ", BEQ, 2));
        INSTRUCTIONS.put(BNE, new Instruction("BNE", BNE, 2));
        INSTRUCTIONS.put(SLT, new Instruction("SLT", SLT, 3));
        INSTRUCTIONS.put(SEQ, new Instruction("SEQ", SEQ, 3));
        INSTRUCTIONS.put(HALT, new Instruction("HALT", HALT, 0));
    }

    private static int[] program;
    
    public static int execute(int[] code) {
        execute(code, 0);
        return program[0];
    }

    public static int execute(int[] code, int input) {
        return execute(code, input, false);
    }

    public static int execute(int[] code, int input, boolean debugging) {
        return execute(code, new int[] { input }, debugging);
    }

    public static int execute(int[] code, int[] input, boolean debugging) {
        program = Arrays.copyOf(code, code.length);
        int output = -1;
        int inpc = 0;
        int pc = 0;
        boolean running = true;
        while (running) {
            int opcode = program[pc];
            Instruction ins = INSTRUCTIONS.get(opcode % 100);
            int[] params = new int[ins.numParams];
            int[] modes = new int[ins.numParams];
            StringBuilder flags = new StringBuilder(String.valueOf(opcode));

            int pad = 5 - flags.length();
            for (int i = 0; i < pad; i++) flags.insert(0, "0");
            flags.setLength(3);
            
            for (int i = 0; i < ins.numParams; i++) {
                params[i] = program[pc + i + 1];
                modes[i] = flags.charAt(2 - i) == '1' ? 1 : 0;
            }
            if (debugging) System.out.printf("%04d %02d %-6s %s [%s] -> ", pc, ins.operation, ins.name, flags, Arrays.stream(params).mapToObj(String::valueOf).collect(Collectors.joining(",")));
            int[] state = Arrays.copyOf(program, program.length);
            int pcstate = pc;
            switch (ins.operation) {
                case ADD -> program[params[2]] = val(params[0], modes[0]) + val(params[1], modes[1]);
                case MUL -> program[params[2]] = val(params[0], modes[0]) * val(params[1], modes[1]);
                case INPUT -> program[params[0]] = input[inpc++];
                case OUTPUT -> { output = val(params[0], modes[0]); if (debugging) System.out.print("OUTPUT: " + output); }
                case BEQ -> { if (val(params[0], modes[0]) > 0) pc = val(params[1], modes[1]); }
                case BNE -> { if (val(params[0], modes[0]) == 0) pc = val(params[1], modes[1]); }
                case SLT -> program[params[2]] = (val(params[0], modes[0]) < val(params[1], modes[1])) ? 1 : 0;
                case SEQ -> program[params[2]] = (val(params[0], modes[0]) == val(params[1], modes[1])) ? 1 : 0;
                case HALT -> { running = false; if (debugging) System.out.println("bye"); }
                default -> throw new RuntimeException("Unknown opcode");
            }
            if (debugging) {
                for (int i = 0; i < program.length; i++) {
                    if (state[i] != program[i]) System.out.printf("State of %d has changed from %d to %d.", i, state[i], program[i]);
                }
                if (pcstate != pc) System.out.printf("Program counter has changed from %d to %d.", pcstate, pc);
                System.out.println();
            }
            if (pcstate == pc) pc += ins.numParams + 1;
        }
        return output;
    }

    private static int val(int param, int mode) {
        return mode == 0 ? program[param] : param;
    }

    static record Instruction(String name, int operation, int numParams) {
    }

}
