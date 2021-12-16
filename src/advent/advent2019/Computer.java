package advent.advent2019;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Computer {
    private static final int ADD = 1;
    private static final int MUL = 2;
    private static final int INPUT = 3;
    private static final int OUTPUT = 4;
    private static final int HALT = 99;
    
    private static final Map<Integer, Instruction> INSTRUCTIONS = new HashMap<>();
    static {
        INSTRUCTIONS.put(ADD, new Instruction("ADD", ADD, 3));
        INSTRUCTIONS.put(MUL, new Instruction("MUL", MUL, 3));
        INSTRUCTIONS.put(INPUT, new Instruction("INPUT", INPUT, 1));
        INSTRUCTIONS.put(OUTPUT, new Instruction("OUTPUT", OUTPUT, 1));
        INSTRUCTIONS.put(HALT, new Instruction("HALT", HALT, 0));
    }

    private static int[] program;
    
    public static int executeIntCode(int[] code) {
        executeIntCode(code, 0);
        return program[0];
    }
    
    public static int executeIntCode(int[] code, int input) {
        program = Arrays.copyOf(code, code.length);
        int output = -1;
        int pc = 0;
        while (true) {
            int opcode = program[pc];
            if (opcode == HALT) {
                break;
            }
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
            System.out.printf("%s %s [%s]%n", ins.name, flags, Arrays.stream(params).mapToObj(String::valueOf).collect(Collectors.joining(",")));
            switch (ins.operation) {
                case ADD -> program[params[2]] = val(params[0], modes[0]) + val(params[1], modes[1]);
                case MUL -> program[params[2]] = val(params[0], modes[0]) * val(params[1], modes[1]);
                case INPUT -> program[params[0]] = input;
                case OUTPUT -> output = val(params[0], modes[0]);
                default -> throw new RuntimeException("Unknown opcode");
            }
            pc += ins.numParams + 1;
        }
        return output;
    }

    private static int val(int param, int mode) {
        return mode == 0 ? program[param] : param;
    }

    static class Instruction {
        String name;
        int operation;
        int numParams;

        public Instruction(String name, int operation, int numParams) {
            this.name = name;
            this.operation = operation;
            this.numParams = numParams;
        }
    }

}
