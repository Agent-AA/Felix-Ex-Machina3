package frc.robot.subsystems.LED;

/**
 * Represents a pattern of CANdleColorBlocks and a frameDelay in seconds if it is
 * to be used in an animation.
 */
public class CANdlePattern {
    
    private final double frameDelay;
    private final CANdleColorBlock[] colorBlocks;

    public CANdlePattern(CANdleColorBlock ... colorBlocks) {
        this.colorBlocks = colorBlocks;
        frameDelay = 0.0;
    }

    public CANdlePattern(double frameDelaySeconds, CANdleColorBlock ... colorBlocks) {
        this.colorBlocks = colorBlocks;
        frameDelay = frameDelaySeconds;
    }

    /**
     * Returns the frame delay in seconds
     * 
     * @return the frame delay (s)
     */
    public double getFrameDelay() {
        return frameDelay;
    }

    /**
     * Returns the entire pattern of colorblocks
     * 
     * @return the pattern of colorblocks as an array
     */
    public CANdleColorBlock[] getPattern() {
        return colorBlocks;
    }

    /**
     * Returns the color block at the specified index
     * @param index
     * @return a color block
     */
    public CANdleColorBlock getBlock(int index) {
        return colorBlocks[index];
    }
}
