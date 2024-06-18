package frc.robot.subsystems.LED;

/** 
 * Represents an RGB(W) color that the LEDSubsystem can interpret
 */ 
public class CANdleColor {
    private final int r, g, b, w;

    // if no white color is specified, set it to 0
    public CANdleColor(int r, int g, int b) throws IllegalArgumentException {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            throw new IllegalArgumentException("Arguments must be integers between 0 and 255 (inclusive)");
        }

        this.r = r;
        this.g = g;
        this.b = b;
        this.w = 0;
    }

    public CANdleColor(int r, int g, int b, int w) throws IllegalArgumentException {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255 || w < 0 || w > 255) {
            throw new IllegalArgumentException("Arguments must be integres between 0 and 255 (inclusive)");
        }

        this.r = r;
        this.g = g;
        this.b = b;
        this.w = w;
    }

    /**
     * Returns the red value between 0 and 255 (inclusive) for the color
     * 
     * @return the red value for the color
     */
    public int getr() {
        return r;
    }

    /**
     * Returns the green value between 0 and 255 (inclusive) for the color
     * 
     * @return the green value for the color
     */
    public int getg() {
        return g;
    }

    /**
     * Returns the blue value between 0 and 255 (inclusive) for the color
     * 
     * @return the blue value for the color
     */
    public int getb() {
        return b;
    }

    /**
     * Returns the white value between 0 and 255 (inclusive) for the color
     * 
     * @return the white value for the color
     */
    public int getw() {
        return w;
    }
}