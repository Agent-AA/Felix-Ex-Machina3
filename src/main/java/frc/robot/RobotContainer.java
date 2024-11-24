// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.ClimbingSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShootingSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.Drive.DriveSubsystem;
import frc.robot.subsystems.LED.Animate;
import frc.robot.subsystems.LED.CANdleColor;
import frc.robot.subsystems.LED.LedSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import java.util.List;

/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  public static final DriveSubsystem m_robotDrive = new DriveSubsystem();
  protected static final LedSubsystem m_robotLEDs = new LedSubsystem(); // protected so it can be used by robot.java
  private static final ShootingSubsystem m_robotShooter = new ShootingSubsystem();
  private static final IntakeSubsystem m_robotIntake = new IntakeSubsystem();
  private static final ClimbingSubsystem m_robotClimbers = new ClimbingSubsystem();
  public static final VisionSubsystem m_robotVision = new VisionSubsystem();

  // The driver's controller
  XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings and other triggers
    configureButtonBindings();
    configureOtherTriggers();

    // Configure default commands
    m_robotDrive.setDefaultCommand(
        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        new RunCommand(
            () -> m_robotDrive.drive(
                -MathUtil.applyDeadband(m_driverController.getLeftY(), OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(m_driverController.getLeftX(), OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(m_driverController.getRightX(), OIConstants.kDriveDeadband),
                true, true),
            m_robotDrive));

    // Defaults for shooter, intake, and climbers are to do nothing
    m_robotIntake.setDefaultCommand(
    new RunCommand(
        () -> m_robotIntake.deactivateIntake(),
        m_robotIntake)
    );
    m_robotShooter.setDefaultCommand(
        new RunCommand(
            () -> m_robotShooter.deactivateShooter(),
            m_robotShooter
        )
    );
    m_robotClimbers.setDefaultCommand(
        new RunCommand(
            () -> m_robotClimbers.stopClimbers(),
            m_robotClimbers
        )
    );

    m_robotLEDs.setDefaultCommand(
        new RunCommand(
            () -> m_robotLEDs.setSolidColor(new CANdleColor((int) Dashboard.MainTab.rEntry.getInteger(0), (int) Dashboard.MainTab.gEntry.getInteger(0), (int) Dashboard.MainTab.bEntry.getInteger(0), (int) Dashboard.MainTab.wEntry.getInteger(0))),
            m_robotLEDs
        )
    );
  }


  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its
   * subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling
   * passing it to a
   * {@link JoystickButton}.
   */
  private void configureButtonBindings() {
    // X-lock while right bumper is engaged
    new JoystickButton(m_driverController, Button.kRightBumper.value)
        .whileTrue(new RunCommand(
            () -> m_robotDrive.setX(),
            m_robotDrive));

    // Reverse shooter and intake while left bumper is engaged
    new JoystickButton(m_driverController, Button.kLeftBumper.value)
        .whileTrue(new RunCommand(
            () -> {m_robotIntake.reverseIntake(); m_robotShooter.reverseShooter();},
            m_robotIntake, m_robotShooter
        ));

    // Reset gyro by pressing right stick
    new JoystickButton(m_driverController, Button.kRightStick.value)
        .onTrue(new RunCommand(
            () -> m_robotDrive.zeroHeading(),
            m_robotDrive));

    // Left trigger activates intake, right trigger: intake and shooter
    // For some reason, JoystickButton doesn't recognize the triggers axes,
    // so we have to bind the triggers differently
    new Trigger(() -> m_driverController.getLeftTriggerAxis() > .5)
        .whileTrue(new RunCommand(
            () -> m_robotIntake.activateIntake(),
            m_robotIntake
        ));
    
    new Trigger(() -> m_driverController.getRightTriggerAxis() > .5)
        .whileTrue(new RunCommand(
            () -> {m_robotShooter.activateShooter(); m_robotIntake.activateIntake();} ,
            m_robotShooter, m_robotIntake
        ));

    // Up on the POV raises the climbers
    new Trigger(() -> m_driverController.getPOV() == 0)
        .whileTrue(new RunCommand(
            () -> m_robotClimbers.raiseClimbers(),
            m_robotClimbers
        ));
    
    // Down on the POV lowers the climbers
    new Trigger(() -> m_driverController.getPOV() == 180)
        .whileTrue(new RunCommand(
            () -> m_robotClimbers.lowerClimbers(),
            m_robotClimbers
        ));

  }

  /**
   * Use this method to configure additional command triggers that are not
   * button-based.
   */
  private void configureOtherTriggers() {

    // If the robot moves, set the LEDs to a dashed electric blue pattern
    new Trigger(() -> m_robotDrive.getModuleSpeed() > 0)
        .whileTrue(new Animate(m_robotLEDs, Constants.LedConstants.kEBlueDashed1, Constants.LedConstants.kEBlueDashed2));

    // If the robot is raising its climbers, set the LEDs to a yellow dashed "up" pattern
    new Trigger(() -> m_robotClimbers.getMovement() == 1)
        .whileTrue(new Animate(m_robotLEDs, Constants.LedConstants.kYellowDashed5, Constants.LedConstants.kYellowDashed4, Constants.LedConstants.kYellowDashed3, Constants.LedConstants.kYellowDashed2, Constants.LedConstants.kYellowDashed1));

    // If the robot is lowering its climbers, set the LEDs to a yellow dashed "down" pattern
    new Trigger(() -> m_robotClimbers.getMovement() == -1)
        .whileTrue(new Animate(m_robotLEDs, Constants.LedConstants.kYellowDashed1, Constants.LedConstants.kYellowDashed2, Constants.LedConstants.kYellowDashed3, Constants.LedConstants.kYellowDashed4, Constants.LedConstants.kYellowDashed5));
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // Create config for trajectory
    TrajectoryConfig config = new TrajectoryConfig(
        AutoConstants.kMaxSpeedMetersPerSecond,
        AutoConstants.kMaxAccelerationMetersPerSecondSquared)
        // Add kinematics to ensure max speed is actually obeyed
        .setKinematics(DriveConstants.kDriveKinematics);

    // An example trajectory to follow. All units in meters.
    Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
        // Start at the origin facing the +X direction
        new Pose2d(0, 0, new Rotation2d(0)),
        // Pass through these two interior waypoints, making an 's' curve path
        List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
        // End 3 meters straight ahead of where we started, facing forward
        new Pose2d(3, 0, new Rotation2d(0)),
        config);

    var thetaController = new ProfiledPIDController(
        AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    SwerveControllerCommand swerveControllerCommand = new SwerveControllerCommand(
        exampleTrajectory,
        m_robotDrive::getPose, // Functional interface to feed supplier
        DriveConstants.kDriveKinematics,

        // Position controllers
        new PIDController(AutoConstants.kPXController, 0, 0),
        new PIDController(AutoConstants.kPYController, 0, 0),
        thetaController,
        m_robotDrive::setModuleStates,
        m_robotDrive);

    // Reset odometry to the starting pose of the trajectory.
    m_robotDrive.resetOdometry(exampleTrajectory.getInitialPose());

    // Run path following command, then stop at the end.
    return swerveControllerCommand.andThen(() -> m_robotDrive.drive(0, 0, 0, false, false));
  }
}
