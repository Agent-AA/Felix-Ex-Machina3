package frc.robot;

import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Constants.*;

/**
 * This class is used to create the Shuffleboard layout for the robot.
 * It is arranged like the Constants class, with nested classes for each tab.
 * All variables are public static final. To retrieve the value of a dashboard item,
 * simply call it like <b>Dashboard.[tabname].[itemname].get[type]().</b>
 */
public class Dashboard {
    
    public static class MainTab {
        public static final ShuffleboardTab mainTab = Shuffleboard.getTab("Main");

        // MAX SPEED CONTROLLER
        public static final GenericEntry maxSpeedEntry = mainTab
            .add("Max Speed", DriveConstants.kMaxSpeedMetersPerSecond)
            .withSize(4,1)
            .withPosition(0,0) // top left corner
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 10))
            .getEntry();

        // DRIVING PID CONTROLLER LAYOUT
        public static final ShuffleboardLayout drivePIDLayout = mainTab
            .getLayout("Drive PID", BuiltInLayouts.kList)
            .withSize(2,3)
            .withPosition(0,1) // top left corner, below max drive speed
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
        public static final ShuffleboardLayout turnPIDLayout = mainTab
            .getLayout("Turn PID", BuiltInLayouts.kList)
            .withSize(2,3)
            .withPosition(2,1) // to the right of the drive PID and under max speed controller
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
        
        // Shooting speed entry
        public static final GenericEntry shootingSpeedEntry = mainTab
            .add("Shooting Speed", ShootingConstants.kDefaultShootSpeed)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 1))
            .withPosition(5,0)
            .withSize(2,1)
            .getEntry();

        // Climbing speed entry
        public static final GenericEntry climbingSpeedEntry = mainTab
            .add("Climbing Speed", ClimbConstants.kDefaultClimbSpeed)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 1))
            .withPosition(5,1)
            .withSize(2, 1)
            .getEntry();

        // RGB color scheme layout
        public static final ShuffleboardLayout colorPickerLayout = mainTab
            .getLayout("RGBW Color Adjuster", BuiltInLayouts.kList)
            .withSize(2,4)
            .withPosition(7,0)
            .withProperties(Map.of("Label Position", "HIDDEN"));

            public static final GenericEntry rEntry = colorPickerLayout
            .add("R", LedConstants.kElectricBlue.getr())
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withSize(2,1)
            .withProperties(Map.of("min",0,"max",255, "block increment", 1))
            .getEntry();

            public static final GenericEntry gEntry = colorPickerLayout
            .add("G", LedConstants.kElectricBlue.getg())
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withSize(2,1)
            .withProperties(Map.of("min",0,"max",255, "block increment", 1))
            .getEntry();

            public static final GenericEntry bEntry = colorPickerLayout
            .add("B", LedConstants.kElectricBlue.getb())
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withSize(2,1)
            .withProperties(Map.of("min",0,"max",255, "block increment", 1))
            .getEntry();

            public static final GenericEntry wEntry = colorPickerLayout
            .add("W", LedConstants.kElectricBlue.getw())
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withSize(2,1)
            .withProperties(Map.of("min",0,"max",255, "block increment", 1))
            .getEntry();
    }
}
