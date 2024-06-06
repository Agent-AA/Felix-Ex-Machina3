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

    public double getFrameDelay() {
        return frameDelay;
    }

    public CANdleColorBlock[] getPattern() {
        return colorBlocks;
    }

    public CANdleColorBlock getBlock(int index) {
        return colorBlocks[index];
    }
}
