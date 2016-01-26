package be.bendem.chess;

import be.bendem.chess.board.Board;
import be.bendem.chess.board.BoardPiece;
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
        return this;
    }

    public BoardController draw() {
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        drawGrid(ctx);
        drawPieces(ctx);
        return this;
    }

    private void drawGrid(GraphicsContext ctx) {
        double size = getGridSize();
        double square = size / 8;
        boolean black = false;

        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 8; ++j) {
                if(black) {
                    ctx.setFill(Color.DARKGRAY);
                } else {
                    ctx.setFill(Color.WHITE);
                }
                black = !black;
                ctx.fillRect(square * i, square * j, square, square);
            }
            black = !black;
        }
    }

    private void drawPieces(GraphicsContext ctx) {
        if(board == null) {
            Log.w("Skipping board drawing, it is null");
            return;
        }

        for(BoardPiece piece : board.getPieces()) {
            piecesSprite.draw(
                ctx,
                piece.getPiece(),
                piece.getColor(),
                getSquareSize(),
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

    private int getGridSize() {
        return getSquareSize() * 8;
    }

    private int getSquareSize() {
        return (int) Math.min(root.getWidth() - sidebar.getWidth(), root.getHeight()) / 8;
    }
}
