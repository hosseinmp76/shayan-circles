package ir.hosseinmp76.shayancircles.tools;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.ArithmeticUtils;

import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class LongOvalAndCircleTool extends AbstractTool {

    public static Vector2D staticGetPoint(final LongOvalAndCircleTool tool) {
	var ovarCircumference = 2
		* (Math.PI * tool.ovalHeadCircleRadis + tool.ovalWidth);
	double locationOnOval = tool.toolState.getTraveledRadius()
		% (ovarCircumference);
	Vector2D innerCirleCenter;
	if (locationOnOval < tool.ovalWidth) {
	    innerCirleCenter = new Vector2D(locationOnOval - tool.ovalWidth / 2,
		    -1.0 * (tool.ovalHeadCircleRadis + tool.circleRadios));
	} else if (locationOnOval < tool.ovalWidth
		+ Math.PI * tool.ovalHeadCircleRadis) {
	    locationOnOval -= tool.ovalWidth;
	    var alpha = locationOnOval / tool.ovalHeadCircleRadis;
	    var x = Math.sin(alpha)
		    * (tool.circleRadios + tool.ovalHeadCircleRadis);
	    var y = -1 * Math.cos(alpha)
		    * (tool.circleRadios + tool.ovalHeadCircleRadis);
	    innerCirleCenter = new Vector2D(x + tool.ovalWidth / 2, y);
	} else if (locationOnOval < 2 * tool.ovalWidth
		+ Math.PI * tool.ovalHeadCircleRadis) {
	    locationOnOval -= (tool.ovalHeadCircleRadis * Math.PI
		    + tool.ovalWidth);
	    innerCirleCenter = new Vector2D(tool.ovalWidth / 2 - locationOnOval,
		    tool.ovalHeadCircleRadis + tool.circleRadios);
	} else {
	    locationOnOval -= (2 * tool.ovalWidth
		    + Math.PI * tool.ovalHeadCircleRadis);
	    var alpha = locationOnOval / tool.ovalHeadCircleRadis;
	    var x = -1 * Math.sin(alpha)
		    * (tool.circleRadios + tool.ovalHeadCircleRadis);
	    var y = Math.cos(alpha)
		    * (tool.circleRadios + tool.ovalHeadCircleRadis);
	    innerCirleCenter = new Vector2D(x - tool.ovalWidth / 2, y);
	}

	var innerAngle = (tool.toolState.getTraveledRadius()
		% (2 * Math.PI * tool.circleRadios)) / tool.circleRadios;

	final var relativePenLocation = new Vector2D(
		Math.sin(innerAngle)
			* tool.getPenHoleDistanceToInnerCirleCenter(),
		-1.0 * Math.cos(innerAngle)
			* tool.getPenHoleDistanceToInnerCirleCenter());
	var res = relativePenLocation.add(innerCirleCenter);
	return res;
    }

    private final double ovalHeadCircleRadis;
    private final double ovalWidth;
    private final double circleRadios;
    private final double penHoleDistanceToInnerCirleCenter;

    protected final double epsilon = 0.00001;

    public LongOvalAndCircleTool(double ovalHeadCircleRadis, double ovalWidth,
	    double circleRadios, double penHoleDistanceToInnerCirleCenter,
	    Color color2) {
	super(0, color2);
	this.ovalHeadCircleRadis = ovalHeadCircleRadis;
	this.ovalWidth = ovalWidth;
	this.circleRadios = circleRadios;
	this.penHoleDistanceToInnerCirleCenter = penHoleDistanceToInnerCirleCenter;
    }

    @Override
    public Vector2D getPoint() {
	return LongOvalAndCircleTool.staticGetPoint(this);
    }

    @Override
    public boolean isFinished() {
	var a = circleRadios;
	var b = ovalHeadCircleRadis + ovalWidth;
	double lcm = a * b;
	if (a % 1 == 0 && b % 1 == 0) {
	    lcm = ArithmeticUtils.lcm((int) a, (int) b);
	}
	if (2 * Math.PI * lcm
		- this.toolState.getTraveledRadius() < this.epsilon) {
	    return true;
	}
	return false;
    }

}
