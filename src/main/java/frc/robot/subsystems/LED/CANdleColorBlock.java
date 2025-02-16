package frc.robot.subsystems.LED;

/**
 * Represents an RGB(W) color and its length to display for
 */
public class CANdleColorBlock extends CANdleColor {

    private final int length;

    public CANdleColorBlock(int r, int g, int b, int length) {
        super(r, g, b);
        this.length = length;
    }

    public CANdleColorBlock(int r, int g, int b, int w, int length) {
        super(r, g, b, w);
        this.length = length;
    }

    public CANdleColorBlock(CANdleColor color, int length) {
        super(color.getr(), color.getg(), color.getb(), color.getw());
        this.length = length;
    }

    /**
     * Returns the length (number of LEDs for that color when in a pattern) for the color block
     * 
     * @return the length of the color block
     */
    public int getLength() {
        return length;
    }
}