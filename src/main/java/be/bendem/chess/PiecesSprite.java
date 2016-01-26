package be.bendem.chess;

import be.bendem.chess.board.Color;
import be.bendem.chess.board.Piece;
import be.bendem.chess.svg.SVGToPNGTransformer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.apache.batik.transcoder.TranscoderException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class PiecesSprite {

    private static final int PIECE_SIZE = 45;
    private static final int SPRITE_WIDTH = 270;
    private static final int SPRITE_HEIGHT = 90;

    private final SVGToPNGTransformer transformer;
    private final byte[] spriteContent;
    private Image cache;
    private int cacheSize;

    public PiecesSprite(Path file) throws IOException {
        this(Files.newInputStream(file));
    }

    public PiecesSprite(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[512];
        int read;
        while((read = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }

        transformer = new SVGToPNGTransformer();
        spriteContent = outputStream.toByteArray();
    }

    public GraphicsContext draw(GraphicsContext ctx, Piece piece, Color color, int size, int x, int y) {
        if(cache == null || cacheSize != size) {
            updateCache(size);
        }

        ctx.drawImage(cache,
            pieceToX(piece) * size,
            colorToY(color) * size,
            size,
            size,
            x * size,
            y * size,
            size,
            size);

        return ctx;
    }

    private void updateCache(int size) {
        float ratio = (float) size / PIECE_SIZE;
        try {
            cache = transformer.toImage(new ByteArrayInputStream(spriteContent), SPRITE_WIDTH * ratio, SPRITE_HEIGHT * ratio);
        } catch(IOException | TranscoderException e) {
            throw new RuntimeException(e);
        }
        cacheSize = size;
    }

    private int pieceToX(Piece piece) {
        switch(piece) {
            case KING:   return 0;
            case QUEEN:  return 1;
            case BISHOP: return 2;
            case KNIGHT: return 3;
            case ROOK:   return 4;
            case PAWN:   return 5;
        }
        throw new AssertionError();
    }

    private int colorToY(Color color) {
        switch(color) {
            case WHITE: return 0;
            case BLACK: return 1;
        }
        throw new AssertionError();
    }

}
