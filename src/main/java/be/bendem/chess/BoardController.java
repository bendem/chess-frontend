package be.bendem.chess;

import be.bendem.chess.board.Board;
import be.bendem.chess.board.BoardPiece;
import be.bendem.chess.board.Coordinate;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class BoardController implements Initializable {

    private final ChessApp app;
    private final PiecesSprite piecesSprite;
    @FXML private HBox root;
    @FXML private VBox canvasContainer;
    @FXML private VBox sidebar;
    @FXML private Canvas canvas;
    @FXML private Button aiButton;
    @FXML private FlowPane topPieces;
    @FXML private VBox logs;
    @FXML private FlowPane bottomPieces;
    private Board board;
    private MouseMovementHandler mouseMovementHandler;

    public BoardController(ChessApp app) {
        this.app = app;
        this.piecesSprite = app.getPiecesSprite();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.widthProperty().addListener(this::resize);
        root.heightProperty().addListener(this::resize);

        Platform.runLater(() -> resize(null, null, null));
    }

    public BoardController setBoard(Board board) {
        this.board = board;
        board.move(1, 6, 1, 5);
        this.mouseMovementHandler = new MouseMovementHandler(this, board, canvas);

        return this;
    }

    public BoardController draw() {
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        drawGrid(ctx);
        drawBoardContent(ctx);
        return this;
    }

    private void drawGrid(GraphicsContext ctx) {
        double size = getGridSize();
        double square = size / 8;
        boolean black = false;

        for(int x = 0; x < 8; ++x) {
            for(int y = 0; y < 8; ++y) {
                if(black) {
                    ctx.setFill(Color.DARKGRAY);
                } else {
                    ctx.setFill(Color.WHITE);
                }
                black = !black;
                ctx.fillRect(square * x, square * y, square, square);
            }
            black = !black;
        }
    }

    private void drawBoardContent(GraphicsContext ctx) {
        if(board == null) {
            Log.w("Skipping board drawing, it is null");
            return;
        }

        double square = getSquareSize();

        Optional<Coordinate> lastMoveFrom = board.getLastMoveFrom();
        if(lastMoveFrom.isPresent()) {
            ctx.setFill(Color.rgb(150, 200, 150, 0.6));
            ctx.fillRect(square * lastMoveFrom.get().x, square * lastMoveFrom.get().y, square, square);
            Coordinate lastMoveTo = board.getLastMoveTo().get();
            ctx.fillRect(square * lastMoveTo.x, square * lastMoveTo.y, square, square);
        }

        ctx.setFill(Color.TRANSPARENT);

        for(BoardPiece piece : board.getPieces()) {
            piecesSprite.draw(
                ctx,
                piece.getPiece(),
                piece.getColor(),
                square,
                piece.getCoordinate().getX(),
                piece.getCoordinate().getY());
        }
    }

    private void resize(ObservableValue<? extends Number> obs, Number old, Number nw) {
        double size = getGridSize();
        double vMargin = (root.getHeight() - size) / 2;

        canvas.setWidth(size);
        canvas.setHeight(size);
        root.setPadding(new Insets(vMargin));
        draw();
    }

    public double getGridSize() {
        return getSquareSize() * 8;
    }

    public double getSquareSize() {
        return Math.min(root.getWidth() - sidebar.getWidth(), root.getHeight()) / 8;
    }

}
