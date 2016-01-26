package be.bendem.chess.board;

public class BoardPiece {

    public final Piece piece;
    public final Color color;
    public final Coordinate coordinate;

    public BoardPiece(Piece piece, Color color, Coordinate coordinate) {
        this.piece = piece;
        this.color = color;
        this.coordinate = coordinate;
    }

    public Piece getPiece() {
        return piece;
    }

    public Color getColor() {
        return color;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

}
