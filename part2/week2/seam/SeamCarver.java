import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private static final double BORDER_ENERGY = 1000D;

    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        checkNull(picture);
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        checkX(x);
        checkY(y);
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return BORDER_ENERGY;
        Color upColor = picture.get(x, y - 1), downColor = picture.get(x, y + 1),
                leftColor = picture.get(x - 1, y), rightColor = picture.get(x + 1, y);
        double rx = rightColor.getRed() - leftColor.getRed(), gx = rightColor.getGreen() - leftColor.getGreen(),
                bx = rightColor.getBlue() - leftColor.getBlue(), ry = upColor.getRed() - downColor.getRed(),
                gy = upColor.getGreen() - downColor.getGreen(), by = upColor.getBlue() - downColor.getBlue();
        double xGradSquare = rx * rx + gx * gx + bx * bx, yGradSquare = ry * ry + gy * gy + by * by;
        return Math.sqrt(xGradSquare + yGradSquare);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] ans = new int[width()];
        if (height() <= 2) {
            for (int x = 0; x < width(); ++x)
                ans[x] = 0;
            return ans;
        }
        double[][] dp = new double[width()][height()];
        for (int y = 0; y < height(); ++y)
            dp[0][y] = BORDER_ENERGY;

        for (int x = 1; x < width(); ++x) {
            for (int y = 1; y < height() - 1; ++y)
                dp[x][y] = Math.min(Math.min(dp[x - 1][y - 1], dp[x - 1][y]), dp[x - 1][y + 1]) + energy(x, y);
            dp[x][0] = Math.min(dp[x - 1][0], dp[x - 1][1]) + BORDER_ENERGY;
            dp[x][height() - 1] = Math.min(dp[x - 1][height() - 1], dp[x - 1][height() - 2]) + BORDER_ENERGY;
        }

        double min = dp[width() - 1][1];
        int minY = 1;
        for (int y = 2; y < height() - 1; ++y) {
            if (min > dp[width() - 1][y]) {
                min = dp[width() - 1][y];
                minY = y;
            }
        }

        ans[width() - 1] = minY;

        for (int x = width() - 2; x >= 0; --x) {
            min = Double.POSITIVE_INFINITY;
            for (int y = ans[x + 1] - 1; y <= ans[x + 1] + 1; ++y) {
                if (min > dp[x][y]) {
                    min = dp[x][y];
                    minY = y;
                }
                ans[x] = minY;
            }
        }
        return ans;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] ans = new int[height()];
        if (width() <= 2) {
            for (int y = 0; y < height(); ++y)
                ans[y] = 0;
            return ans;
        }
        double[][] dp = new double[width()][height()];
        for (int x = 0; x < width(); ++x)
            dp[x][0] = BORDER_ENERGY;

        for (int y = 1; y < height(); ++y) {
            for (int x = 1; x < width() - 1; ++x)
                dp[x][y] = Math.min(Math.min(dp[x - 1][y - 1], dp[x][y - 1]), dp[x + 1][y - 1]) + energy(x, y);
            dp[0][y] = Math.min(dp[0][y - 1], dp[1][y - 1]) + BORDER_ENERGY;
            dp[width() - 1][y] = Math.min(dp[width() - 1][y - 1], dp[width() - 2][y - 1]) + BORDER_ENERGY;
        }

        double min = dp[1][height() - 1];
        int minX = 1;
        for (int x = 2; x < width() - 1; ++x) {
            if (min > dp[x][height() - 1]) {
                min = dp[x][height() - 1];
                minX = x;
            }
        }

        ans[height() - 1] = minX;

        for (int y = height() - 2; y >= 0; --y) {
            min = Double.POSITIVE_INFINITY;
            for (int x = ans[y + 1] - 1; x <= ans[y + 1] + 1; ++x) {
                if (min > dp[x][y]) {
                    min = dp[x][y];
                    minX = x;
                }
                ans[y] = minX;
            }
        }
        return ans;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (height() <= 1)
            throw new IllegalArgumentException();
        checkHorizontalSeam(seam);
        Picture nextPicture = new Picture(width(), height() - 1);
        for (int x = 0; x < seam.length; ++x) {
            for (int y = 0; y < seam[x]; ++y)
                nextPicture.set(x, y, picture.get(x, y));
            for (int y = seam[x] + 1; y < height(); ++y)
                nextPicture.set(x, y - 1, picture.get(x, y));
        }
        picture = nextPicture;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (width() <= 1)
            throw new IllegalArgumentException();
        checkVerticalSeam(seam);
        Picture nextPicture = new Picture(width() - 1, height());
        for (int y = 0; y < seam.length; ++y) {
            for (int x = 0; x < seam[y]; ++x)
                nextPicture.set(x, y, picture.get(x, y));
            for (int x = seam[y] + 1; x < width(); ++x)
                nextPicture.set(x - 1, y, picture.get(x, y));
        }
        picture = nextPicture;
    }

    private void checkHorizontalSeam(int[] seam) {
        checkNull(seam);
        if (seam.length != picture.width())
            throw new IllegalArgumentException();
        checkY(seam[0]);
        for (int i = 1; i < seam.length; ++i) {
            checkY(seam[i]);
            checkLeftAdjacent(seam, i);
        }
    }

    private void checkVerticalSeam(int[] seam) {
        checkNull(seam);
        if (seam.length != picture.height())
            throw new IllegalArgumentException();
        checkX(seam[0]);
        for (int i = 1; i < seam.length; ++i) {
            checkX(seam[i]);
            checkLeftAdjacent(seam, i);
        }
    }

    private void checkLeftAdjacent(int[] seam, int i) {
        switch (seam[i] - seam[i - 1]) {
            case -1:
            case 0:
            case 1:
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void checkX(int x) {
        if (x < 0 || x >= picture.width())
            throw new IllegalArgumentException();
    }

    private void checkY(int y) {
        if (y < 0 || y >= picture.height())
            throw new IllegalArgumentException();
    }

    private void checkNull(Object object) {
        if (object == null)
            throw new IllegalArgumentException();
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver carver = new SeamCarver(picture);
        carver.findHorizontalSeam();
        carver.findVerticalSeam();
    }
}
