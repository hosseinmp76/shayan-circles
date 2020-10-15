package ir.hosseinmp76.shayancircles.ui.fx;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import ir.hosseinmp76.shayancircles.tools.AbstractTool;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MyCanvas extends Canvas {
    AbstractTool tool;
    int width;
    int height;
    private final Vector2D centerOfCanvas;
    int perSecond;
    final double travle = 1;

    Timer timer = new Timer();
    int repeatPerTick = 1;

    MyCanvas(final int widght, final int height, final AbstractTool tool,
	    final int perSecond) {
	super(widght, height);
	this.width = widght;
	this.height = height;
	this.tool = tool;
	this.perSecond = perSecond;
	this.centerOfCanvas = new Vector2D(widght / 2, height / 2);
	var gc = this.getGraphicsContext2D();
	gc.fillOval(0, 0, widght, height);

    }

    boolean stoppedDrawing = true;

    public boolean isStoppedDrawing() {
	return stoppedDrawing;

    }

    public void start() {
	if (!isStoppedDrawing() || this.tool.isFinished())
	    return;
	this.timer = new Timer();
	TimerTask task;

	task = new TimerTask() {
	    @Override
	    public void run() {
		MyCanvas.this.handle();
	    }

	};
	repeatPerTick = Math.max(this.perSecond / 1000, 1);
	this.timer.scheduleAtFixedRate(task, 0,
		Math.max(1000 / this.perSecond, 1));
	this.stoppedDrawing = false;
    }

    public void stop() {

	this.timer.cancel();
	this.stoppedDrawing = true;
    }

    protected void handle() {
	final GraphicsContext graphics_context = this.getGraphicsContext2D();
	Platform.runLater(() -> {
	    for (int i = 0; i < repeatPerTick; i++) {

		graphics_context.setFill(this.tool.getColor());
		final var point = this.tool.getPoint();
		final var x = this.centerOfCanvas.getX() + point.getX();
		final var y = this.centerOfCanvas.getY() - point.getY();
		graphics_context.fillRect(x, y, 2, 2);
		this.tool.nextPoint(this.travle);
	    }
	});

	if (this.tool.isFinished()) {
	    this.timer.cancel();
	    System.out.println("fff");
	}
    }

    public void changeSpeed(int value) {
	// TODO Auto-generated method stub
	stop();
	this.perSecond = value;
	start();

    }
}
