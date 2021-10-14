import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class Ball {
    private double rx, ry;
    private double vx, vy;
    private final double radius;

    public Ball(double radius) {
        this.radius = radius;
        rx = StdRandom.uniform(radius, 1.0-radius);
        ry = StdRandom.uniform(radius, 1.0-radius);
        vx = StdRandom.uniform(-0.02,0.02);
        vy = StdRandom.uniform(-0.02,0.02);
    }

    public void move(double dt) {
        if ((rx + vx * dt < radius) || (rx + vx * dt > 1.0 - radius))
            vx = -vx;
        if ((ry + vy * dt < radius) || (ry + vy * dt > 1.0 - radius))
            vy = -vy;
        rx += vx * dt;
        ry += vy * dt;
    }

    public void draw() {
        StdDraw.filledCircle(rx, ry, radius);
    }
}
