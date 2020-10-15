package ir.hosseinmp76.shayancircles.tools;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javafx.scene.paint.Color;
import lombok.Data;
import lombok.Getter;

@Getter
public abstract class AbstractTool {
    protected ToolState toolState;

    private Color color;

    public AbstractTool(Color color2) {
	this.color = color2;
    }

    public AbstractTool(double traveledDistance, Color color2) {
	this.toolState = new ToolState();
	this.toolState.setTraveledRadius(traveledDistance);
	this.color = color2;
    }

    public abstract Vector2D getPoint();

    public void nextPoint(final double travleRadius) {
	this.toolState.setTraveledRadius(
		this.toolState.getTraveledRadius() + travleRadius);
    }

    public abstract boolean isFinished();

    public void changeColor(Color value) {
	this.color = value;
    }
}

@Data
class ToolState {
    private double traveledRadius = 0;

}