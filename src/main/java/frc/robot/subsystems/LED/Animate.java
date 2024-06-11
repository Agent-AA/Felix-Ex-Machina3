package frc.robot.subsystems.LED;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * The Animate command animates a series of patterns onto the robot's LEDs.
 * The command does not self-terminate. It must be terminated by 
 */
public class Animate extends Command {
    
    private final CANdlePattern[] patterns;
    private final LedSubsystem robotLEDs;
    private int index = 0;
    private double lastChange = 0.0;

    public Animate(LedSubsystem robotLEDs, CANdlePattern... patterns) throws IllegalArgumentException {

        if (patterns.length == 1) throw new IllegalArgumentException(
            "Vararg pattern must have at least 2 elements. Use LedSubsystem.setPattern() for a single element."
            );

        this.patterns = patterns;
        this.robotLEDs = robotLEDs;

        addRequirements(robotLEDs);
    }

    @Override
    public void initialize() {

        // Set the first pattern
        robotLEDs.setPattern(patterns[0]);

        // Timestamp the change
        lastChange = System.currentTimeMillis();
    }

    @Override
    public void execute() {

        // If the pattern has been displayed for its requested duration,
        if (System.currentTimeMillis() - lastChange >= patterns[index].getFrameDelay() * 1000) {

            // Move to the next pattern index
            index = (index + 1) % patterns.length;

            // Set the pattern
            robotLEDs.setPattern(patterns[index]);

            // Timestamp the change
            lastChange = System.currentTimeMillis();
        }
    }

}
