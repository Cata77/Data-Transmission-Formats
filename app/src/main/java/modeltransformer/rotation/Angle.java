package modeltransformer.rotation;

public class Angle {
    private final double angleX;
    private final double angleY;
    private final double angleZ;

    public Angle(double angleX, double angleY, double angleZ) {
        this.angleX = angleX;
        this.angleY = angleY;
        this.angleZ = angleZ;
    }

    public double getAngleX() {
        return angleX;
    }

    public double getAngleY() {
        return angleY;
    }

    public double getAngleZ() {
        return angleZ;
    }
}
