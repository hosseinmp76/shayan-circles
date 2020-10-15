package ir.hosseinmp76.shayancircles.ui.fx;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


import ir.hosseinmp76.shayancircles.tools.AbstractTool;
import ir.hosseinmp76.shayancircles.tools.ClasicCircleOutOfRingTool;
import ir.hosseinmp76.shayancircles.tools.ClasicRingCircleTool;
import ir.hosseinmp76.shayancircles.tools.LongOvalAndCircleTool;
import ir.hosseinmp76.shayancircles.tools.ToolType;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXApplication extends Application {

    static Region root;
    static final int width = 900;
    static final int height = 700;

    public static void startApp() {
	Application.launch();
    }

    AnimationTimer frameRateMeter;
    private final long[] frameTimes = new long[100];

    private int frameTimeIndex = 0;

    private boolean arrayFilled = false;

    MyCanvas myCanvas;

    Label label = new Label();

    Slider slider;

    AbstractTool tool;

    @Override
    public void start(final Stage stage) throws Exception {

	final SplitPane mainSplitePane = new SplitPane();
	mainSplitePane.setDividerPositions(0.2);
	final VBox sidebarVBox = new VBox();
	sidebarVBox.getChildren().add(this.label);
	final TilePane fpr = new TilePane();
	final Label l = new Label("r");
	final TextField rFiled = new TextField("150");
	rFiled.setMaxWidth(50);
	fpr.getChildren().add(l);
	fpr.getChildren().add(rFiled);
	sidebarVBox.getChildren().add(fpr);

	final FlowPane fpR = new FlowPane();
	final Label l2 = new Label("R");
	final TextField rFiled2 = new TextField("251");
	final TextField rFiled3 = new TextField("261");
	fpR.getChildren().add(l2);
	fpR.getChildren().add(rFiled2);
	fpR.getChildren().add(rFiled3);
	sidebarVBox.getChildren().add(fpR);

	this.frameRateMeter = new AnimationTimer() {

	    @Override
	    public void handle(final long now) {

		final long oldFrameTime = FXApplication.this.frameTimes[FXApplication.this.frameTimeIndex];
		FXApplication.this.frameTimes[FXApplication.this.frameTimeIndex] = now;
		FXApplication.this.frameTimeIndex = (FXApplication.this.frameTimeIndex
			+ 1) % FXApplication.this.frameTimes.length;
		if (FXApplication.this.frameTimeIndex == 0) {
		    FXApplication.this.arrayFilled = true;
		}
		if (FXApplication.this.arrayFilled) {
		    final long elapsedNanos = now - oldFrameTime;
		    final long elapsedNanosPerFrame = elapsedNanos
			    / FXApplication.this.frameTimes.length;
		    final double frameRate = 1_000_000_000.0
			    / elapsedNanosPerFrame;
		    FXApplication.this.label.setText(String
			    .format("Current frame rate: %.3f", frameRate));
		}
	    }
	};

	this.frameRateMeter.start();

	final FlowPane fpdis = new FlowPane();
	final Label l3 = new Label("dis");
	final TextField dFiled3 = new TextField("53");
	fpdis.getChildren().add(l3);
	fpdis.getChildren().add(dFiled3);
	sidebarVBox.getChildren().add(fpdis);
	final Button startButt = new Button("start");
	final Button stopButt = new Button("stop");
	final ColorPicker colorPicker = new ColorPicker();

	colorPicker.setOnAction(event -> {
	    FXApplication.this.tool.changeColor(colorPicker.getValue());
	});
	final var myCanvasParentPane = new Pane();

	ComboBox<ToolType> comboBox = new ComboBox();
	comboBox.getItems().add(ToolType.ClasicCircleOutOfRingTool);
	comboBox.getItems().add(ToolType.ClasicRingCircleTool);
	comboBox.getItems().add(ToolType.LongOvalAndCircleTool);

	startButt.setOnMouseClicked(event -> {
	    stopButt.setText("stop");
	    if (FXApplication.this.myCanvas != null) {
		FXApplication.this.myCanvas.start();
		return;
	    }
	    final double r = Double.parseDouble(rFiled.getText());
	    final double R = Double.parseDouble(rFiled2.getText());
	    final double RR = Double.parseDouble(rFiled3.getText());
	    final double dis = Double.parseDouble(dFiled3.getText());

	    final var color = colorPicker.getValue();
	    switch (comboBox.getValue()) {
	    case ClasicRingCircleTool:
		FXApplication.this.tool = new ClasicRingCircleTool(R, RR, r,
			dis, color);
		break;
	    case ClasicCircleOutOfRingTool:
		FXApplication.this.tool = new ClasicCircleOutOfRingTool(R, RR,
			r, dis, color);
		break;
	    case LongOvalAndCircleTool:
		FXApplication.this.tool = new LongOvalAndCircleTool(R, RR, r,
			dis, color);
		break;
	    default:
		break;
	    }

	    final var spped = (int) FXApplication.this.slider.getValue();

	    FXApplication.this.myCanvas = new MyCanvas(
		    FXApplication.width * 8 / 10, FXApplication.height,
		    FXApplication.this.tool, spped);
	    FXApplication.this.myCanvas.start();
	    myCanvasParentPane.getChildren().add(FXApplication.this.myCanvas);

	});

	stopButt.setOnMouseClicked(y -> {
	    if (this.myCanvas != null) {
		if (this.myCanvas.isStoppedDrawing()) {

		    myCanvasParentPane.getChildren().clear();
		    this.myCanvas = null;
		    stopButt.setText("stop");

		} else {
		    this.myCanvas.stop();
		    stopButt.setText("remove");
		}
	    }
	});

	final Button buttonSave = new Button("Save");
	buttonSave.setOnAction(t -> {
	    final FileChooser fileChooser = new FileChooser();

	    // Set extension filter
	    final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
		    "png files (*.png)", "*.png");
	    fileChooser.getExtensionFilters().add(extFilter);

	    // Show save file dialog
	    final File file = fileChooser.showSaveDialog(stage);

	    if (file != null) {
		try {
		    final WritableImage writableImage = new WritableImage(
			    FXApplication.width, FXApplication.height);
		    FXApplication.this.myCanvas.snapshot(null, writableImage);
		    final RenderedImage renderedImage = SwingFXUtils
			    .fromFXImage(writableImage, null);
		    ImageIO.write(renderedImage, "png", file);
		} catch (final IOException ex) {
		}
	    }
	});

	sidebarVBox.getChildren().add(colorPicker);

	sidebarVBox.getChildren().add(comboBox);

	sidebarVBox.getChildren().add(startButt);

	sidebarVBox.getChildren().add(stopButt);
	sidebarVBox.getChildren().add(buttonSave);
	this.slider = new Slider(1, 1000_0, 1000);
//	slider.setMaxHeight(300);
	this.slider.setMinHeight(400);
	this.slider.setOrientation(Orientation.VERTICAL);
	this.slider.setOnMouseDragged(e -> {
	    this.myCanvas.changeSpeed((int) this.slider.getValue());
	});

	sidebarVBox.getChildren().add(this.slider);

	mainSplitePane.getItems().add(sidebarVBox);
	mainSplitePane.getItems().add(myCanvasParentPane);
	final Scene scene = new Scene(mainSplitePane, FXApplication.width,
		FXApplication.height, Color.ANTIQUEWHITE);

	scene.getStylesheets().add(
		FXApplication.class.getResource("styles.css").toExternalForm());

	stage.setTitle("JavaFX and Gradle");
	stage.setScene(scene);
	stage.setOnCloseRequest(x -> System.exit(0));
	stage.show();
	FXApplication.root = mainSplitePane;

    }

}
