package modeltransformer.transformer;

public class Triangle {
    private Point point1;
    private Point point2;
    private Point point3;
    private double zAverage;

    public Triangle() {
        this.point1 = null;
        this.point2 = null;
        this.point3 = null;
        this.zAverage = 0;
    }

    public Point getPoint1() {
        return point1;
    }

    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }

    public Point getPoint3() {
        return point3;
    }

    public void setPoint3(Point point3) {
        this.point3 = point3;
    }

    public double getzAverage() {
        return zAverage;
    }

    public void setzAverage(double zAverage) {
        this.zAverage = zAverage;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "point1=" + point1 +
                ", point2=" + point2 +
                ", point3=" + point3 +
                ", zAverage=" + zAverage +
                '}';
    }
}
