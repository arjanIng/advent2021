package advent.util;

import java.util.Objects;

public record Pos(int row, int column, int val) {
    public Pos newVal(int newVal) {
        return new Pos(row, column, newVal);
    }

    public Pos add(int add) {
        return new Pos(row, column, val + add);
    }

    public boolean isAround(Pos p, int distance) {
        return !this.equals(p) && Math.abs(row - p.row) <= distance && Math.abs(column - p.column) <= distance;
    }

    public boolean isAdjacent(Pos p, int distance) {
        return !this.equals(p) &&
                (column == p.column && Math.abs(row - p.row) <= distance) ||
                (row == p.row && Math.abs(column - p.column) <= distance);
    }

    @Override
    public String toString() {
        return "Pos{" +
                "row=" + row +
                ", column=" + column +
                ", val=" + val +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos pos = (Pos) o;
        return row == pos.row && column == pos.column && val == pos.val;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, val);
    }
}
