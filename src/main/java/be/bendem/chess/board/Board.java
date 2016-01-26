package be.bendem.chess.board;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Board {

    private final Piece[][] pieces;
    private final Color[][] colors;
    private Color turn;
    private int lastMoveFromX = -1;
    private int lastMoveFromY = -1;
    private int lastMoveToX = -1;
    private int lastMoveToY = -1;

    public Board() {
        pieces = new Piece[8][8];
        colors = new Color[8][8];
        turn = Color.WHITE;

        for(int x = 0; x < 8; ++x) {
            colors[x][0] = colors[x][1] = Color.BLACK;
            colors[x][6] = colors[x][7] = Color.WHITE;
            pieces[x][1] = pieces[x][6] = Piece.PAWN;
        }
        pieces[0][0] = pieces[0][7] = Piece.ROOK;
        pieces[1][0] = pieces[1][7] = Piece.KNIGHT;
        pieces[2][0] = pieces[2][7] = Piece.BISHOP;
        pieces[3][0] = pieces[3][7] = Piece.QUEEN;
        pieces[4][0] = pieces[4][7] = Piece.KING;
        pieces[5][0] = pieces[5][7] = Piece.BISHOP;
        pieces[6][0] = pieces[6][7] = Piece.KNIGHT;
        pieces[7][0] = pieces[7][7] = Piece.ROOK;
    }

    public Board(Piece[][] pieces, Color[][] colors, Color turn) {
        this.pieces = pieces;
        this.colors = colors;
        this.turn = turn;
    }

    public Optional<Piece> get(int x, int y, boolean checkTurn) {
        if(checkTurn && colors[x][y] != turn) {
            return Optional.empty();
        }

        return Optional.ofNullable(pieces[x][y]);
    }

    public Set<BoardPiece> getPieces() {
        HashSet<BoardPiece> boardPieces = new HashSet<>();
        for(int x = 0; x < 8; ++x) {
            for(int y = 0; y < 8; ++y) {
                if(pieces[x][y] == null) continue;
                boardPieces.add(new BoardPiece(pieces[x][y], colors[x][y], new Coordinate(x, y)));
            }
        }
        return boardPieces;
    }

    public Set<Coordinate> getValidMovesFor(int x, int y) {
        // TODO Get all valid moves (call C++ backend?)
        return Collections.emptySet();
    }

    public Optional<Piece> move(int fromX, int fromY, int toX, int toY) {
        Piece piece = pieces[fromX][fromY];
        if(piece == null) {
            throw new IllegalArgumentException("No piece to move");
        }
        if(colors[fromX][fromY] != turn) {
            throw new IllegalArgumentException(String.format("Can only move %s pieces", turn));
        }
        if(colors[toX][toY] == turn) {
            throw new IllegalArgumentException("Can't take own pieces");
        }

        // TODO Check valid move (call backend?)

        Piece taken = pieces[toX][toY];
        pieces[toX][toY] = piece;
        pieces[fromX][fromY] = null;
        colors[toX][toY] = colors[fromX][fromY];
        colors[fromX][fromY] = null;

        lastMoveFromX = fromX;
        lastMoveFromY = fromY;
        lastMoveToX = toX;
        lastMoveToY = toY;

        turn = turn == Color.BLACK ? Color.WHITE : Color.BLACK;

        return Optional.ofNullable(taken);
    }

    public Optional<Coordinate> getLastMoveFrom() {
        return lastMoveFromX == -1 ? Optional.empty() : Optional.of(new Coordinate(lastMoveFromX, lastMoveFromY));
    }

    public Optional<Coordinate> getLastMoveTo() {
        return lastMoveFromX == -1 ? Optional.empty() : Optional.of(new Coordinate(lastMoveToX, lastMoveToY));
    }

}
