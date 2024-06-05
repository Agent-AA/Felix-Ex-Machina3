package frc.robot.subsystems;

import com.ctre.phoenix.led.CANdle; // CANdle is not supported in Phoenix6, so we have to use the generic library
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.RainbowAnimation;
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
     * Represents an RGB(W) color that the LEDSubsystem can interpret
     */ 
    public class CANdleColor {
        public final int r;
        public final int g;
        public final int b;
        public final int w;

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
    }

    /**
     * Sets the LED strip to a dynamic rainbow pattern
     */
    public void setRainbow() {
        RainbowAnimation rainbowAnim = new RainbowAnimation(1, .5, Constants.OIConstants.kNumLeds);
        candle.animate(rainbowAnim);
    }

    /**
     * Sets the entire LED strip to a solid color
     * @param color the CANdle color to set the LEDs to
     * @throws IllegalArgumentException if any of the arguments are not between 0 and 255
     */
    public void setSolidColor(CANdleColor color) {
        candle.setLEDs(color.r, color.g, color.b);
    }

    /**
     * Sets the LED strip to an alternating pattern of colors
     * @param colorBlockLength the length of each block of colors (e.g., 3 for 3 reds in a row)
     * @param colors the LED colors to alternate between
     */
    public void setAlternatingPattern(int colorBlockLength, CANdleColor ... colors) {
       int colorStart = 0;

       for (CANdleColor color : colors) {
            int blockStart = colorStart;

            while (blockStart <= Constants.OIConstants.kNumLeds - 1) {
                candle.setLEDs(color.r, color.g, color.b, color.w, blockStart, colorBlockLength);
                blockStart += colorBlockLength * colors.length;
            }

            colorStart += colorBlockLength;
       }
    }
}
