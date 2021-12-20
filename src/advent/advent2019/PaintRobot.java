package advent.advent2019;

import java.util.HashMap;
import java.util.Map;

public class PaintRobot implements IODevice {
    private Pos pos;
    private Map<Pos, Long> painted;
    private Dir direction;
    private boolean moving;

    @Override
    public void input(long input) {
        if (!moving) { // painting
            painted.put(pos, input);
            moving = true;
        } else {
            direction = input == 0 ? direction.previous() : direction.next();
            pos = direction.move(pos);
            moving = false;
        }
    }

    public Map<Pos, Long> getPainted() {
        return painted;
    }

    @Override
    public long output() {
        return painted.containsKey(pos) ? painted.get(pos) : 0;
    }

    @Override
    public void reset() {
        pos = new Pos(0, 0);
        painted = new HashMap<>();
        direction = Dir.UP;
        moving = false;
    }

    enum Dir {
        UP, RIGHT, DOWN, LEFT;

        private static final Dir[] vals = values();

        public Dir next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }

        public Dir previous() {
            return vals[(this.ordinal() - 1) < 0 ? vals.length - 1 : this.ordinal() - 1];
        }

        public Pos move(Pos from) {
            switch (this.ordinal()) {
                case 0 -> {
                    return new Pos(from.x, from.y - 1);
                }
                case 1 -> {
                    return new Pos(from.x + 1, from.y);
                }
                case 2 -> {
                    return new Pos(from.x, from.y + 1);
                }
                case 3 -> {
                    return new Pos(from.x - 1, from.y);
                }
                default -> throw new RuntimeException("Can't move?");
            }
        }
    }

    record Pos(int x, int y) {
    }
}
