package be.bendem.chess;

import javafx.stage.Stage;

import java.io.IOException;

public class ChessApp extends BaseApplication {

    public static void main(String... args) {
        launch(args);
    }

    private final PiecesSprite piecesSprite;

    public ChessApp() {
        try {
            piecesSprite = new PiecesSprite(getResourceStream("pieces.svg"));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Log.d("Application starting");

        open("Home.fxml", "Chess", stage);
    }

    public PiecesSprite getPiecesSprite() {
        return piecesSprite;
    }
}
