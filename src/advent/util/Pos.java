package advent.util;

import java.util.Objects;

public class Pos<T> {
    final int row;
    final int column;
    final T val;

    public Pos(int row, int column, T val) {
        this.row = row;
        this.column = column;
        this.val = val;
    }
    
    public int row() {
        return row;
    }
    
    public int column() {
        return column;
    }
    
    public T val() {
        return val;
    }

    public Pos newVal(int newVal) {
        return new Pos(row, column, newVal);
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
        return row == pos.row && column == pos.column && val.equals(pos.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, val);
    }

}
