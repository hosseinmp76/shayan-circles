package ir.hosseinmp76.shayancircles.tools;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.ArithmeticUtils;

import javafx.scene.paint.Color;

public class ClasicCircleOutOfRingTool extends ClasicRingCircleTool {

    public static Vector2D staticGetPoint(
	    final ClasicCircleOutOfRingTool tool) {
	final var r = tool.getInnerCirleRadios();
	final var R = tool.getOuterCirleBigRadios();
	final double outerAngle = tool.toolState.getTraveledRadius()
		% (2 * Math.PI * tool.getOuterCirleSmallRadios())
		/ tool.getOuterCirleSmallRadios();
	final double innerAngle = tool.toolState.getTraveledRadius()
		% (2 * Math.PI * r) / r;

	final var innerCirleCenter = new Vector2D(
		-1.0 * Math.sin(outerAngle) * (R + r),
		-1.0 * Math.cos(outerAngle) * (R + r));
	final var relativePenLocation = new Vector2D(
		-1 * Math.sin(innerAngle)
			* tool.getPenHoleDistanceToInnerCirleCenter(),
		-1.0 * Math.cos(innerAngle)
			* tool.getPenHoleDistanceToInnerCirleCenter());
	return relativePenLocation.add(innerCirleCenter);
    }

    public ClasicCircleOutOfRingTool(final double outerCirleRadios,
	    final double outerCirleBigRadios, final double innerCirleRadios,
	    final double penHoleDistanceToInnerCirleCenter, final Color color) {
	super(outerCirleRadios, outerCirleBigRadios, innerCirleRadios,
		penHoleDistanceToInnerCirleCenter, color);
	// TODO Auto-generated constructor stub
    }

    @Override
    public boolean isFinished() {
	final var R = this.getOuterCirleBigRadios();
	double lcm = this.getInnerCirleRadios() * R;
	if (this.getInnerCirleRadios() % 1 == 0 && R % 1 == 0) {
	    lcm = ArithmeticUtils.lcm((int) this.getInnerCirleRadios(),
		    (int) R);
	}
	if (2 * Math.PI * lcm
		- this.toolState.getTraveledRadius() < this.epsilon) {
	    return true;
	}
	return false;
    }
}
