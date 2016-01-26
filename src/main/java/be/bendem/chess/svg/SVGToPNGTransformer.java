package be.bendem.chess.svg;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class SVGToPNGTransformer {

    public static void main(String... args) throws IOException, TranscoderException {
        if(args.length != 4) {
            System.out.println("Usage: <command> <in.svg> <width> <height> <out.png>");
            return;
        }

        int width = Integer.parseInt(args[1]);
        int height = Integer.parseInt(args[2]);
        Image image = new SVGToPNGTransformer()
            .toImage(Files.newInputStream(Paths.get(args[0])), width, height);

        ImageIO.write(
            SwingFXUtils.fromFXImage(image, new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)),
            "png",
            Files.newOutputStream(Paths.get(args[3]))
        );
    }

    public Image toImage(InputStream inputStream, double width, double height) throws IOException, TranscoderException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        writeTo(inputStream, width, height, os);
        return new Image(new ByteArrayInputStream(os.toByteArray()));
    }

    public OutputStream writeTo(InputStream inputStream, double width, double height, OutputStream os) throws IOException, TranscoderException {
        Transcoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, (float) width);
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, (float) height);

        TranscoderInput input = new TranscoderInput(inputStream);
        TranscoderOutput output = new TranscoderOutput(os);

        transcoder.transcode(input, output);
        return os;
    }

}
