// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.util.WPIUtilJNI;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;
import frc.utils.SwerveUtils;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
  // Create MAXSwerveModules
  private final MAXSwerveModule m_frontLeft = new MAXSwerveModule(
      DriveConstants.kFrontLeftDrivingCanId,
      DriveConstants.kFrontLeftTurningCanId,
      DriveConstants.kFrontLeftAbsoluteEncoderCanId,
      DriveConstants.kFrontLeftChassisAngularOffset);

  private final MAXSwerveModule m_frontRight = new MAXSwerveModule(
      DriveConstants.kFrontRightDrivingCanId,
      DriveConstants.kFrontRightTurningCanId,
      DriveConstants.kFrontRightAbsoluteEncoderCanId,
      DriveConstants.kFrontRightChassisAngularOffset);

  private final MAXSwerveModule m_rearLeft = new MAXSwerveModule(
      DriveConstants.kRearLeftDrivingCanId,
      DriveConstants.kRearLeftTurningCanId,
      DriveConstants.kRearLeftAbsoluteEncoderCanId,
      DriveConstants.kBackLeftChassisAngularOffset);

  private final MAXSwerveModule m_rearRight = new MAXSwerveModule(
      DriveConstants.kRearRightDrivingCanId,
      DriveConstants.kRearRightTurningCanId,
      DriveConstants.kRearRightAbsoluteEncoderCanId,
      DriveConstants.kBackRightChassisAngularOffset);

  // The gyro sensor
  private final Pigeon2 m_gyro = new Pigeon2(20);

  // MAXSwerve NetworkTables publishers for AdvantageScope
  private final StructArrayPublisher<SwerveModuleState> setPointsPublisher;
  private final StructArrayPublisher<SwerveModuleState> actualValuesPublisher;
  private final StructArrayPublisher<Rotation2d> gyroAnglePublisher;

  // Shuffleboard Tab
  private final ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");

  // Shuffleboard PIDController entries
  private final GenericEntry drivingPEntry = driveTab
    .add("Driving P", ModuleConstants.kDrivingP)
    .withWidget(BuiltInWidgets.kNumberSlider)
    .getEntry();

  private final GenericEntry drivingIEntry = driveTab
    .add("Driving I", ModuleConstants.kDrivingI)
    .withWidget(BuiltInWidgets.kNumberSlider)
    .getEntry();

  private final GenericEntry drivingDEntry = driveTab
    .add("Driving D", ModuleConstants.kDrivingD)
    .withWidget(BuiltInWidgets.kNumberSlider)
    .getEntry();

  private final GenericEntry turningPEntry = driveTab
    .add("Turning P", ModuleConstants.kTurningP)
    .withWidget(BuiltInWidgets.kNumberSlider)
    .getEntry();

  private final GenericEntry turningIEntry = driveTab
    .add("Turning I", ModuleConstants.kTurningI)
    .withWidget(BuiltInWidgets.kNumberSlider)
    .getEntry();

  private final GenericEntry turningDEntry = driveTab
    .add("Turning D", ModuleConstants.kTurningD)
    .withWidget(BuiltInWidgets.kNumberSlider)
    .getEntry();


  // Slew rate filter variables for controlling lateral acceleration
  private double m_currentRotation = 0.0;
  private double m_currentTranslationDir = 0.0;
  private double m_currentTranslationMag = 0.0;

  private SlewRateLimiter m_magLimiter = new SlewRateLimiter(DriveConstants.kMagnitudeSlewRate);
  private SlewRateLimiter m_rotLimiter = new SlewRateLimiter(DriveConstants.kRotationalSlewRate);
  private double m_prevTime = WPIUtilJNI.now() * 1e-6;

  // Odometry class for tracking robot pose
  SwerveDriveOdometry m_odometry = new SwerveDriveOdometry(
      DriveConstants.kDriveKinematics,
      Rotation2d.fromDegrees(getHeading()),
      new SwerveModulePosition[] {
          m_frontLeft.getPosition(),
          m_frontRight.getPosition(),
          m_rearLeft.getPosition(),
          m_rearRight.getPosition()
      });

  /** Creates a new DriveSubsystem. */
  public DriveSubsystem() {

    // Start publishing various values to NetworkTables. These are used for visualizing
    // the swerve module states in AdvantageScope.
    setPointsPublisher = NetworkTableInstance.getDefault()
      .getStructArrayTopic("/SwerveStates/SetPoints", SwerveModuleState.struct).publish();
    actualValuesPublisher = NetworkTableInstance.getDefault()
      .getStructArrayTopic("/SwerveStates/ActualValues", SwerveModuleState.struct).publish();
    gyroAnglePublisher = NetworkTableInstance.getDefault()
      .getStructArrayTopic("/GyroAngle", Rotation2d.struct).publish();
  }

  @Override
  public void periodic() {
    // Update the odometry in the periodic block
    m_odometry.update(
        Rotation2d.fromDegrees(getHeading()),
        new SwerveModulePosition[] {
            m_frontLeft.getPosition(),
            m_frontRight.getPosition(),
            m_rearLeft.getPosition(),
            m_rearRight.getPosition()
        });

    // Update MAXSwerveModule states for AdvantageScope
    setPointsPublisher.set(new SwerveModuleState[] {
      m_frontLeft.getDesiredState(),
      m_frontRight.getDesiredState(),
      m_rearLeft.getDesiredState(),
      m_rearRight.getDesiredState()
    });
    actualValuesPublisher.set(new SwerveModuleState[] {
      m_frontLeft.getState(),
      m_frontRight.getState(),
      m_rearLeft.getState(),
      m_rearRight.getState()
    });
    gyroAnglePublisher.set(new Rotation2d[] {
      Rotation2d.fromDegrees(getHeading())
    });

    // Update MAXSwerveModule PID values from Shuffleboard
    setDrivingPIDValues(new double[] {
      drivingPEntry.getDouble(ModuleConstants.kDrivingP),
      drivingIEntry.getDouble(ModuleConstants.kDrivingI),
      drivingDEntry.getDouble(ModuleConstants.kDrivingD)
    });
    setTurningPIDValues(new double[] {
      turningPEntry.getDouble(ModuleConstants.kTurningP),
      turningIEntry.getDouble(ModuleConstants.kTurningI),
      turningDEntry.getDouble(ModuleConstants.kTurningD)
    });
  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    m_odometry.resetPosition(
        Rotation2d.fromDegrees(getHeading()),
        new SwerveModulePosition[] {
            m_frontLeft.getPosition(),
            m_frontRight.getPosition(),
            m_rearLeft.getPosition(),
            m_rearRight.getPosition()
        },
        pose);
  }

  /**
   * Method to drive the robot using joystick info.
   *
   * @param xSpeed        Speed of the robot in the x direction (forward).
   * @param ySpeed        Speed of the robot in the y direction (sideways).
   * @param rot           Angular rate of the robot.
   * @param fieldRelative Whether the provided x and y speeds are relative to the
   *                      field.
   * @param rateLimit     Whether to enable rate limiting for smoother control.
   */
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative, boolean rateLimit) {

    double xSpeedCommanded;
    double ySpeedCommanded;

    if (rateLimit) {
      // Convert XY to polar for rate limiting
      double inputTranslationDir = Math.atan2(ySpeed, xSpeed);
      double inputTranslationMag = Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2));

      // Calculate the direction slew rate based on an estimate of the lateral acceleration
      double directionSlewRate;
      if (m_currentTranslationMag != 0.0) {
        directionSlewRate = Math.abs(DriveConstants.kDirectionSlewRate / m_currentTranslationMag);
      } else {
        directionSlewRate = 500.0; //some high number that means the slew rate is effectively instantaneous
      }


      double currentTime = WPIUtilJNI.now() * 1e-6;
      double elapsedTime = currentTime - m_prevTime;
      double angleDif = SwerveUtils.AngleDifference(inputTranslationDir, m_currentTranslationDir);
      if (angleDif < 0.45*Math.PI) {
        m_currentTranslationDir = SwerveUtils.StepTowardsCircular(m_currentTranslationDir, inputTranslationDir, directionSlewRate * elapsedTime);
        m_currentTranslationMag = m_magLimiter.calculate(inputTranslationMag);
      }
      else if (angleDif > 0.85*Math.PI) {
        if (m_currentTranslationMag > 1e-4) { //some small number to avoid floating-point errors with equality checking
          // keep currentTranslationDir unchanged
          m_currentTranslationMag = m_magLimiter.calculate(0.0);
        }
        else {
          m_currentTranslationDir = SwerveUtils.WrapAngle(m_currentTranslationDir + Math.PI);
          m_currentTranslationMag = m_magLimiter.calculate(inputTranslationMag);
        }
      }
      else {
        m_currentTranslationDir = SwerveUtils.StepTowardsCircular(m_currentTranslationDir, inputTranslationDir, directionSlewRate * elapsedTime);
        m_currentTranslationMag = m_magLimiter.calculate(0.0);
      }
      m_prevTime = currentTime;

      xSpeedCommanded = m_currentTranslationMag * Math.cos(m_currentTranslationDir);
      ySpeedCommanded = m_currentTranslationMag * Math.sin(m_currentTranslationDir);
      m_currentRotation = m_rotLimiter.calculate(rot);


    } else {
      xSpeedCommanded = xSpeed;
      ySpeedCommanded = ySpeed;
      m_currentRotation = rot;
    }

    // Convert the commanded speeds into the correct units for the drivetrain
    double xSpeedDelivered = xSpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond;
    double ySpeedDelivered = ySpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond;
    double rotDelivered = m_currentRotation * DriveConstants.kMaxAngularSpeed;

    var swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(
        fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered, Rotation2d.fromDegrees(getHeading()))
            : new ChassisSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered));
    SwerveDriveKinematics.desaturateWheelSpeeds(
        swerveModuleStates, DriveConstants.kMaxSpeedMetersPerSecond);
    m_frontLeft.setDesiredState(swerveModuleStates[0]);
    m_frontRight.setDesiredState(swerveModuleStates[1]);
    m_rearLeft.setDesiredState(swerveModuleStates[2]);
    m_rearRight.setDesiredState(swerveModuleStates[3]);
  }

  /**
   * Sets the wheels into an X formation to prevent movement.
   */
  public void setX() {
    m_frontLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
    m_frontRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
    m_rearLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
    m_rearRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
  }

  /**
   * Sets the swerve ModuleStates.
   *
   * @param desiredStates The desired SwerveModule states.
   */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(
        desiredStates, DriveConstants.kMaxSpeedMetersPerSecond);
    m_frontLeft.setDesiredState(desiredStates[0]);
    m_frontRight.setDesiredState(desiredStates[1]);
    m_rearLeft.setDesiredState(desiredStates[2]);
    m_rearRight.setDesiredState(desiredStates[3]);
  }

  /** Resets the drive encoders to currently read a position of 0. */
  public void resetEncoders() {
    m_frontLeft.resetEncoders();
    m_rearLeft.resetEncoders();
    m_frontRight.resetEncoders();
    m_rearRight.resetEncoders();
  }

  /** Zeroes the heading of the robot. */
  public void zeroHeading() {
    m_gyro.reset();
  }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getHeading() {
    // the m_gyro.getAngle() is negative because we need to invert it
    return Rotation2d.fromDegrees(-m_gyro.getAngle()).getDegrees();
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return m_gyro.getRate() * (DriveConstants.kGyroReversed ? -1.0 : 1.0);
  }

  /**
   * Returns the P, I, and D gains for the driving motors on the MAXSwerveModules
   *
   * @return The P, I, and D gains for the driving motors on the MAXSwerveModules
   */
  public double[] getDrivingPIDValues() {
    // because the PID values are the same for all modules,
    // we only need to retrieve them from one
    return m_frontLeft.getDrivingPIDValues();
  }

  /**
   * Sets the P, I, and D gains for the driving motors on the MAXSwerveModules
   *
   * @param values The P, I, and D gains for the driving motors on the MAXSwerveModules
   * @throws IllegalArgumentException the values argument does not have exactly 3 elements
   */
  public void setDrivingPIDValues(double[] values) throws IllegalArgumentException {
    // Set the PID values for all modules
    try {
      m_frontLeft.setDrivingPIDValues(values);
      m_frontRight.setDrivingPIDValues(values);
      m_rearLeft.setDrivingPIDValues(values);
      m_rearRight.setDrivingPIDValues(values);
    } catch (IllegalArgumentException e) {
      throw e;
    }
  }

  /**
   * Returns the P, I, and D gains for the turning motors on the MAXSwerve Modules
   *
   * @return The P, I, and D gains for the turning motors on the MAXSwerve Modules
   */
  public double[] getTurningPIDValues() {
    // because the PID values are the same for all modules,
    // we only need to retrieve them from one
    return m_frontLeft.getTurningPIDValues();
  }

  /**
   * Sets the P, I, and D gains for the turning motors on the MAXSwerveModules
   *
   * @param values The P, I, and D gains for the turning motors on the MAXSwerveModules
   * @throws IllegalArgumentException the values argument does not have exactly 3 elements
   */
  public void setTurningPIDValues(double[] values) throws IllegalArgumentException {
    // Set the PID values for all modules
    try {
      m_frontLeft.setTurningPIDValues(values);
      m_frontRight.setTurningPIDValues(values);
      m_rearLeft.setTurningPIDValues(values);
      m_rearRight.setTurningPIDValues(values);
    } catch (IllegalArgumentException e) {
      throw e;
    }
  }
}