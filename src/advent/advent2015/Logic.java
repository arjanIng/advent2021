package advent.advent2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Logic {

    Map<String, Wire> wires = new HashMap<>();
    List<Component> inputs = new ArrayList<>();
    Set<Component> history;

    public void logic(String inputFile) throws IOException {
        List<String> inputData = Files.lines(Paths.get(inputFile))
                .collect(Collectors.toList());
        
        for (String line : inputData) {
            String[] parts = line.split(" -> ");
            String opcode = parts[0];
            String outputWireName = parts[1];
            String[] iparts = opcode.split(" ");
            Component newComponent;
            if (iparts.length > 1) {
                if (iparts.length == 2) { // NOT gate
                    SingleInputGate gate = new SingleInputGate();
                    gate.input = getComponent(iparts[1], gate, false);
                    gate.function = a -> (char) ~a;
                    newComponent = gate;
                } else if (iparts[1].length() < 4) { // AND / OR gate
                    DualInputGate gate = new DualInputGate();
                    gate.input1 = getComponent(iparts[0], gate, false);
                    gate.input2 = getComponent(iparts[2], gate, false);
                    
                    switch(iparts[1]) {
                        case "AND" -> gate.function = (a, b) -> (char) (a & b);
                        case "OR" -> gate.function = (a, b) -> (char) (a | b);
                    }
                    newComponent = gate;
                } else {
                    SingleInputGate gate = new SingleInputGate();
                    gate.input = getComponent(iparts[0], gate, false);
                    char shift = (char) Integer.parseInt(iparts[2]);
                    switch(iparts[1]) {
                        case "LSHIFT" -> gate.function = a -> (char) (a << shift);
                        case "RSHIFT" -> gate.function = a -> (char) (a >> shift);
                    }
                    newComponent = gate;
                }
            } else {
                try {
                    InSignal inSignal = new InSignal();
                    inSignal.setSignal((char) Integer.parseInt(iparts[0]));
                    inputs.add(inSignal);
                    newComponent = inSignal;
                } catch (NumberFormatException e) {
                    // if the parse fails, we are dealing with a wire to wire
                    Component inputWire = getComponent(iparts[0], null, false);
                    newComponent = inputWire;
                }
            }
            
            Component outputWire = getComponent(outputWireName, newComponent, true);
            newComponent.addOutput(outputWire);
        }

        System.out.printf("Inputs %d%n", inputs.size());
        for (Component input : inputs) {
            System.out.printf("***** Propagating %s%n", input);
            history = new HashSet<>();
            input.propagate();
        }
        
        for (Wire w : this.wires.values().stream().sorted(Comparator.comparing(a -> a.name)).collect(Collectors.toList())) {
            System.out.printf("%s: %d%n", w.name, (long) w.signal);
        }
//        System.out.printf("Part 1: %d%n", numOn);
//        System.out.printf("Part 2: %d%n", bright);
    }
    
    private Component getComponent(String name, Component component, boolean input) {
        Component result;
        try {
            int val = Integer.parseInt(name);
            InSignal inSignal = new InSignal();
            inSignal.signal = (char) val;
            inSignal.output = component;
            inputs.add(inSignal);
            result = inSignal;
        } catch (NumberFormatException e) {
            if (!wires.containsKey(name)) {
                Wire wire = new Wire();
                wire.name = name;
                wires.put(name, wire);
            }
            Wire wire = wires.get(name);
            if (component != null) {
                if (input) {
                    wire.input = component;
                } else {
                    wire.addOutput(component);
                }
            }
            result = wire;
        }
        return result;
    }
    
    interface Component {
        char getSignal();
        boolean hasSignal();
        void propagate();
        void addOutput(Component output);
    }
    
    class Wire implements Component {
        String name;
        char signal;
        Component input;
        List<Component> outputs = new ArrayList<>();
        boolean hasSignal;

        @Override
        public char getSignal() {
            return signal;
        }

        @Override
        public boolean hasSignal() {
            return hasSignal;
        }

        @Override
        public void propagate() {
            if (input.hasSignal() && !history.contains(this)) {
                this.signal = input.getSignal();
                this.hasSignal = true;
                history.add(this);
                System.out.printf("In wire %s, signal %d%n", name, (int) signal);
                for (Component output : outputs) {
                    output.propagate();
                }
            }
        }
        
        @Override
        public void addOutput(Component output) {
            this.outputs.add(output);
        }
    }
    
    class InSignal implements Component {
        char signal;
        Component output;

        @Override
        public char getSignal() {
            return signal;
        }

        @Override
        public boolean hasSignal() {
            return true;
        }

        public void setSignal(char signal) {
            this.signal = signal;
        }

        @Override
        public void propagate() {
            output.propagate();
        }

        @Override
        public void addOutput(Component output) {
            this.output = output;
        }
    }
    
    class DualInputGate implements Component {
        char signal;
        Component input1;
        Component input2;
        Component output;
        BiFunction<Character, Character, Character> function;
        boolean hasSignal;

        @Override
        public char getSignal() {
            return signal;
        }

        @Override
        public boolean hasSignal() {
            return hasSignal;
        }

        @Override
        public void propagate() {
            if (input1.hasSignal() && input2.hasSignal()) {
                this.signal = function.apply(input1.getSignal(), input2.getSignal());
                this.hasSignal = true;
                history.add(this);
                output.propagate();
            }
        }

        @Override
        public void addOutput(Component output) {
            this.output = output;
        }
    }

    class SingleInputGate implements Component {
        char signal;
        Component input;
        Component output;
        Function<Character, Character> function;
        boolean hasSignal;

        @Override
        public char getSignal() {
            return signal;
        }

        @Override
        public boolean hasSignal() {
            return hasSignal;
        }

        @Override
        public void propagate() {
            if (input.hasSignal()) {
                this.hasSignal = true;
                this.signal = function.apply(input.getSignal());
                output.propagate();
            }
        }

        @Override
        public void addOutput(Component output) {
            this.output = output;
        }
    }

    public static void main(String[] args) throws IOException {
        Logic lava = new Logic();
        lava.logic("./data/2015/logic.txt");
    }

}
