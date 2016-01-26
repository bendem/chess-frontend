package be.bendem.chess;

import be.bendem.chess.board.Board;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

public class MouseMovementHandler {

    private final BoardController boardController;
    private final Board board;
    private final Canvas canvas;
    private int fromX = -1;
    private int fromY = -1;

    public MouseMovementHandler(BoardController boardController, Board board, Canvas canvas) {
        this.boardController = boardController;
        this.board = board;
        this.canvas = canvas;

        canvas.setOnMousePressed(this::pressed);
        canvas.setOnMouseMoved(this::moved);
        canvas.setOnMouseReleased(this::released);
    }

    private void pressed(MouseEvent e) {
        fromX = toGridX(e);
        fromY = toGridY(e);

        // TODO That should display all legal moves
        Log.d("Clicked on %d, %d", fromX, fromY);
    }

    private void moved(MouseEvent e) {
        if(fromX < 0) {
            return;
        }
        // TODO Animation
    }

    private void released(MouseEvent e) {
        if(fromX < 0) {
            return;
        }
        // That should check if legal move
        try {
            board.move(fromX, fromY, toGridX(e), toGridY(e));
            boardController.draw();
        } catch(IllegalArgumentException exc) {
            Log.w("Invalid move from %d,%d to %d,%d", exc, fromX, fromY, toGridX(e), toGridY(e));
        }
        fromX = -1;
        fromY = -1;
    }

    private int toGridX(MouseEvent e) {
        return (int) (e.getX() / boardController.getSquareSize());
    }

    private int toGridY(MouseEvent e) {
        return (int) (e.getY() / boardController.getSquareSize());
    }

}
