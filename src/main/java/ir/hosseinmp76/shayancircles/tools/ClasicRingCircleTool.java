package ir.hosseinmp76.shayancircles.tools;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.ArithmeticUtils;

import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class ClasicRingCircleTool extends AbstractTool {

    public static Vector2D staticGetPoint(final ClasicRingCircleTool tool) {
	final double outerAngle = tool.getToolState().getTraveledRadius()
		% (2 * Math.PI * tool.getOuterCirleSmallRadios())
		/ tool.getOuterCirleSmallRadios();
	final double innerAngle = tool.getToolState().getTraveledRadius()
		% (2 * Math.PI * tool.getInnerCirleRadios())
		/ tool.getInnerCirleRadios();

	final var innerCirleCenter = new Vector2D(
		-1.0 * Math.sin(outerAngle) * tool.getInnerCirleRadios(),
		-1.0 * Math.cos(outerAngle) * tool.getInnerCirleRadios());
	final var relativePenLocation = new Vector2D(
		Math.sin(innerAngle)
			* tool.getPenHoleDistanceToInnerCirleCenter(),
		-1.0 * Math.cos(innerAngle)
			* tool.getPenHoleDistanceToInnerCirleCenter());
	return relativePenLocation.add(innerCirleCenter);
    }

    private final double outerCirleSmallRadios;
    private final double outerCirleBigRadios;
    private final double innerCirleRadios;
    private final double penHoleDistanceToInnerCirleCenter;

    protected final double epsilon = 0.00001;

    public ClasicRingCircleTool(final double outerCirleRadios,
	    final double outerCirleBigRadios, final double innerCirleRadios,
	    final double penHoleDistanceToInnerCirleCenter, final Color color) {
	super(color);
	if (penHoleDistanceToInnerCirleCenter > innerCirleRadios)
	    throw new RuntimeException(
		    "penHoleDistanceToInnerCirleCenter > innerCirleRadios");
	this.outerCirleBigRadios = outerCirleBigRadios;
	this.outerCirleSmallRadios = outerCirleRadios;
	this.innerCirleRadios = innerCirleRadios;
	this.penHoleDistanceToInnerCirleCenter = penHoleDistanceToInnerCirleCenter;
	this.toolState = new ToolState();
    }

    @Override
    public Vector2D getPoint() {
	return ClasicRingCircleTool.staticGetPoint(this);
    }

    @Override
    public boolean isFinished() {
	double lcm = this.getInnerCirleRadios()
		* this.getOuterCirleSmallRadios();
	if (this.getInnerCirleRadios() % 1 == 0
		&& this.getOuterCirleSmallRadios() % 1 == 0) {
	    lcm = ArithmeticUtils.lcm((int) this.getInnerCirleRadios(),
		    (int) this.getOuterCirleSmallRadios());
	}
	if (2 * Math.PI * lcm
		- this.getToolState().getTraveledRadius() < this.epsilon) {
	    return true;
	}
	return false;
    }

}
