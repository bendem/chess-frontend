package be.bendem.chess;

import be.bendem.chess.board.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private final ChessApp app;
    @FXML private Button continueGame;
    @FXML private Button newGame;
    @FXML private Button loadGame;
    @FXML private Button config;

    public HomeController(ChessApp app) {
        this.app = app;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        continueGame.setOnAction(e -> {
            Log.d("Going back to the board");
            app.<BoardController>setScene("Board.fxml").draw();
        });

        newGame.setOnAction(e -> {
            Log.d("Loading new game");
            continueGame.setDisable(false);
            app.<BoardController>setScene("Board.fxml").setBoard(new Board()).draw();
        });

        loadGame.setOnAction(e -> {
            Log.d("Asking for a game to load");

            continueGame.setDisable(false);
            // TODO Show a scene with a textarea to paste the saved game or a file chooser
            // TODO Load from text (json?) then setBoard
            app.<BoardController>setScene("Board.fxml").setBoard(new Board()).draw();
        });

        config.setOnAction(e -> {
            Log.d("NYI");
            // TODO Find out what there is to configure
        });
    }

}
