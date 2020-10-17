package ir.hosseinmp76.shayancircles;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ir.hosseinmp76.shayancircles.ui.fx.MyCanvas;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Utills {
    public static void savePNG(int width, int height, MyCanvas canvas, File file) {
	try {
	    final WritableImage writableImage = new WritableImage(width,
		    height);
	    SnapshotParameters sp = new SnapshotParameters();
	    sp.setFill(Color.TRANSPARENT);

	    canvas.snapshot(sp, writableImage);
	    final RenderedImage renderedImage = SwingFXUtils
		    .fromFXImage(writableImage, null);
	    ImageIO.write(renderedImage, "png", file);
	} catch (final IOException ex) {
	}
    }
}
