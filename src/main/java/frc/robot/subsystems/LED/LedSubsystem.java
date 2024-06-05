package frc.robot.subsystems.LED;

import com.ctre.phoenix.led.CANdle; // CANdle is not supported in Phoenix6, so we have to use the generic library
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.CANdle.LEDStripType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * The LedSubsystem controls the LEDs on the robot.
 */
public class LedSubsystem extends SubsystemBase {

    // Our LED strip
    private final CANdle candle = new CANdle(Constants.OIConstants.kCANdleId);
    private final CANdleConfiguration config;

    public LedSubsystem() {
        // Set the default configuration
        config = new CANdleConfiguration();
        config.stripType = LEDStripType.RGB;
        config.brightnessScalar = .5;
        candle.configAllSettings(config);

        // Set default color (color for disabled)
        setSolidColor(new CANdleColor(255, 0, 0)); // red
    }

    /**
     * Sets a portion of the LED strip to a colorblock. Although the CANdle class natively 
     * supports setting LEDs, this method wraps the CANdle's native .setLEDs() method 
     * so that it works with this subsystem's unique colorblock schema.
     * @param colorBlock the desired colorblock
     * @param startIdx the start index for the LED
     */
    public void setColor(CANdleColorBlock colorBlock, int startIdx) {
        candle.setLEDs(colorBlock.r, colorBlock.g, colorBlock.b, colorBlock.w, startIdx, colorBlock.length);
    }

    /**
     * Sets a portion of the LED strip to a colorblock. This method version
     * (as opposed to the above .setColor() method) is used with a primitive
     * CANdleColor and the length is inputted as an argument.
     * @param color
     * @param startIdx
     * @param length
     */
    public void setColor(CANdleColor color, int startIdx, int length) {
        candle.setLEDs(color.r, color.g, color.b, color.w, startIdx, length);

    }

    /**
     * Sets the entire LED strip to a solid color. Functions as a wrapper like
     * the .setColor() method does.
     * @param color the CANdle color to set the LEDs to
     */
    public void setSolidColor(CANdleColor color) {
        candle.setLEDs(color.r, color.g, color.b);
    }

    /**
     * Sets the LED strip to an equally alternating pattern of colors
     * @param colorBlockLength the length of each block of colors (e.g., 3 for 3 reds in a row)
     * @param colors the CANdle colors to alternate between
     */
    public void setPattern(int colorBlockLength, CANdleColor ... colors) {
       int colorStartAddress = 0;

       for (CANdleColor color : colors) {
            int blockStartAddress = colorStartAddress;

            while (blockStartAddress <= Constants.OIConstants.kNumLeds - 1) {
                setColor(color, blockStartAddress, colorBlockLength);
                blockStartAddress += colorBlockLength * colors.length;
            }

            colorStartAddress += colorBlockLength;
       }
    }

    /**
     * Sets the LED strip to a pattern of colors as specified by the input.
     * The pattern will repeat if it doesn't fully cover the LEDs.
     * @param ...colorBlocks the colorBlocks to set the pattern to.
     */
    public void setPattern(CANdleColorBlock ... colorBlocks) {
        int blockStartAddress = 0;

        while (blockStartAddress <= Constants.OIConstants.kNumLeds - 1) {
            for (CANdleColorBlock colorBlock: colorBlocks) {
                setColor(colorBlock, blockStartAddress);
                blockStartAddress += colorBlock.length;
            }
        }
    }
}
