package frc.robot;

import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Constants.ModuleConstants;

/**
 * This class is used to create the Shuffleboard layout for the robot.
 * It is arranged like the Constants class, with nested classes for each tab.
 * All variables are public static final. To retrieve the value of a dashboard item,
 * simply call it like <b>Dashboard.[tabname].[itemname].get[type]().</b>
 */
public class Dashboard {
    
    public static class DriveTab {
        public static final ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");

        // DRIVING PID CONTROLLER LAYOUT
        public static final ShuffleboardLayout drivePIDLayout = driveTab
            .getLayout("Drive PID", BuiltInLayouts.kList)
            .withSize(2,3)
            .withPosition(0,0) // top left corner
            .withProperties(Map.of("Label position", "HIDDEN")); // hide labels for commands

            // Driving PIDController entries
            public static final GenericEntry drivePEntry = drivePIDLayout
                .add("P", ModuleConstants.kDrivingP)
                .withWidget(BuiltInWidgets.kNumberSlider)
                .withProperties(Map.of("min", 0, "max", 1))
                .getEntry();
            
            public static final GenericEntry driveIEntry = drivePIDLayout
                .add("I", ModuleConstants.kDrivingI)
                .withWidget(BuiltInWidgets.kNumberSlider)
                .withProperties(Map.of("min", 0, "max", 1))
                .getEntry();
            
            public static final GenericEntry driveDEntry = drivePIDLayout
                .add("D", ModuleConstants.kDrivingD)
                .withWidget(BuiltInWidgets.kNumberSlider)
                .withProperties(Map.of("min", 0, "max", 1))
                .getEntry();

        // TURNING PID CONTROLLER LAYOUT
        public static final ShuffleboardLayout turnPIDLayout = driveTab
            .getLayout("Turn PID", BuiltInLayouts.kList)
            .withSize(2,3)
            .withPosition(2,0) // to the right of the drive PID
            .withProperties(Map.of("Label position", "HIDDEN")); // hide labels for commands

            // Turning PIDController entries
            public static final GenericEntry turnPEntry = turnPIDLayout
                .add("P", ModuleConstants.kTurningP)
                .withWidget(BuiltInWidgets.kNumberSlider)
                .withProperties(Map.of("min", 0, "max", 1))
                .getEntry();
            
            public static final GenericEntry turnIEntry = turnPIDLayout
                .add("I", ModuleConstants.kTurningI)
                .withWidget(BuiltInWidgets.kNumberSlider)
                .withProperties(Map.of("min", 0, "max", 1))
                .getEntry();

            public static final GenericEntry turnDEntry = turnPIDLayout
                .add("D", ModuleConstants.kTurningD)
                .withWidget(BuiltInWidgets.kNumberSlider)
                .withProperties(Map.of("min", 0, "max", 1))
                .getEntry();
    }
}
