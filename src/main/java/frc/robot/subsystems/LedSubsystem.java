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

    // Its configuration settings
    private final CANdleConfiguration config = new CANdleConfiguration();

    public LedSubsystem() {
        // Set the default configuration
        config.stripType = LEDStripType.RGB;
        config.brightnessScalar = .5; // dim leds to half brightness
        candle.configAllSettings(config);

        setRainbow(); // set to rainbow pattern
    }

    /**
     * Sets the LED strip to a dynamic rainbow pattern
     */
    public void setRainbow() {
        RainbowAnimation rainbowAnim = new RainbowAnimation(1, .5, Constants.OIConstants.kNumLeds);
        candle.animate(rainbowAnim);
    }
}
