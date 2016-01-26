package be.bendem.chess.board;

public class Coordinate {

    public final int x;
    public final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return (x << 8) & y;
    }
}
