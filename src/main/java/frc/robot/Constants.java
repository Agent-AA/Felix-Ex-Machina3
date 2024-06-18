// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.LED.*;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final class DriveConstants {
    // Driving Parameters - Note that these are not the maximum capable speeds of
    // the robot, rather the allowed maximum speeds
    public static final double kMaxSpeedMetersPerSecond = 2.0;
    public static final double kMaxAngularSpeed = 2 * Math.PI; // radians per second

    public static final double kDirectionSlewRate = 1.2; // radians per second
    public static final double kMagnitudeSlewRate = 1.8; // percent per second (1 = 100%)
    public static final double kRotationalSlewRate = 2.0; // percent per second (1 = 100%)

    // Chassis configuration
    public static final double kTrackWidth = Units.inchesToMeters(24.25);
    // Distance between centers of right and left wheels on robot
    public static final double kWheelBase = Units.inchesToMeters(24.25);
    // Distance between front and back wheels on robot
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

    // Angular offsets of the modules relative to the chassis in radians
    public static final double kFrontLeftChassisAngularOffset = 0.147;
    public static final double kFrontRightChassisAngularOffset = 2.046;
    public static final double kBackLeftChassisAngularOffset = -1.321;
    public static final double kBackRightChassisAngularOffset = 1.801;

    // SPARK MAX CAN IDs
    public static final int kFrontLeftDrivingCanId = 2;
    public static final int kRearLeftDrivingCanId = 6;
    public static final int kFrontRightDrivingCanId = 4;
    public static final int kRearRightDrivingCanId = 8;

    public static final int kFrontLeftTurningCanId = 3;
    public static final int kRearLeftTurningCanId = 7;
    public static final int kFrontRightTurningCanId = 5;
    public static final int kRearRightTurningCanId = 9;

    // ENCODER CAN IDs
    public static final int kFrontLeftAbsoluteEncoderCanId = 10;
    public static final int kRearLeftAbsoluteEncoderCanId = 12;
    public static final int kFrontRightAbsoluteEncoderCanId = 11;
    public static final int kRearRightAbsoluteEncoderCanId = 13;

    public static final boolean kGyroReversed = false;
  }

  public static final class ModuleConstants {
    // The MAXSwerve module can be configured with one of three pinion gears: 12T, 13T, or 14T.
    // This changes the drive speed of the module (a pinion gear with more teeth will result in a
    // robot that drives faster).
    public static final int kDrivingMotorPinionTeeth = 14;

    // Invert the turning encoder, since the output shaft rotates in the opposite direction of
    // the steering motor in the MAXSwerve Module.
    public static final boolean kTurningEncoderInverted = true;

    // Calculations required for driving motor conversion factors and feed forward
    public static final double kDrivingMotorFreeSpeedRps = NeoMotorConstants.kFreeSpeedRpm / 60;
    public static final double kWheelDiameterMeters = 0.1016;
    public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;
    // 45 teeth on the wheel's bevel gear, 22 teeth on the first-stage spur gear, 15 teeth on the bevel pinion
    public static final double kDrivingMotorReduction = (45.0 * 22) / (kDrivingMotorPinionTeeth * 15);
    public static final double kDriveWheelFreeSpeedRps = (kDrivingMotorFreeSpeedRps * kWheelCircumferenceMeters)
        / kDrivingMotorReduction;

    public static final double kDrivingEncoderPositionFactor = (kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction; // meters
    public static final double kDrivingEncoderVelocityFactor = ((kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction) / 60.0; // meters per second

    public static final double kTurningEncoderPositionFactor = (2 * Math.PI); // radians
    public static final double kTurningEncoderVelocityFactor = (2 * Math.PI) / 60.0; // radians per second

    public static final double kTurningEncoderPositionPIDMinInput = 0; // radians
    public static final double kTurningEncoderPositionPIDMaxInput = kTurningEncoderPositionFactor; // radians

    public static final double kDrivingP = 0.04;
    public static final double kDrivingI = 0;
    public static final double kDrivingD = 0;
    public static final double kDrivingFF = 1 / kDriveWheelFreeSpeedRps;
    public static final double kDrivingMinOutput = -1;
    public static final double kDrivingMaxOutput = 1;

    public static final double kTurningP = 0.69; // .69 seems to have the fastest time without overshooting
    public static final double kTurningI = 0;
    public static final double kTurningD = 0;
    public static final double kTurningFF = 0;
    public static final double kTurningMinOutput = -1;
    public static final double kTurningMaxOutput = 1;

    public static final IdleMode kDrivingMotorIdleMode = IdleMode.kBrake;
    public static final IdleMode kTurningMotorIdleMode = IdleMode.kBrake;

    public static final int kDrivingMotorCurrentLimit = 50; // amps
    public static final int kTurningMotorCurrentLimit = 20; // amps
  }

  public static final class ClimbConstants {
    public static final int kRightClimbMotorCanId = 18;
    public static final int kLeftClimbMotorCanId = 19;

    public static final int kRightLimitSwitchPWMPort = 8;
    public static final int kLeftLimitSwitchPWMPort = 9;

    public static final double kDefaultClimbSpeed = .75;
  }

  public static final class ShootingConstants {
    public static final int kTopShooterMotorCanId = 14;
    public static final int kBottomShooterMotorCanId = 15;

    public static final double kDefaultShootSpeed = .75;
  }

  public static final class IntakeConstants {
    public static final int kIntakeMotorPWMPort = 0;
    public static final int kLoaderMotorPWMPort = 1;
  }

  public static final class OIConstants {
    public static final int kDriverControllerPort = 0;
    public static final double kDriveDeadband = 0.05;
  }

  public static final class LedConstants {
    public static final int kCANdleId = 17;
    public static final int kNumLeds = 128;

    // Colors
    public static final CANdleColor kBlack = new CANdleColor(0, 0, 0);
    public static final CANdleColor kRed = new CANdleColor(255, 0, 0);
    public static final CANdleColor kElectricBlue = new CANdleColor(0, 82, 130);
    public static final CANdleColor kElectricYellow = new CANdleColor(255, 255, 51);

    // Color Blocks
    public static final CANdleColorBlock kEBlueBlock = new CANdleColorBlock(kElectricBlue, 2);
    public static final CANdleColorBlock kBlackBlock = new CANdleColorBlock(kBlack, 1);
    public static final CANdleColorBlock kDoubleBlack = new CANdleColorBlock(kBlack, 2);

    public static final CANdleColorBlock k3Yellow = new CANdleColorBlock(kElectricYellow, 3);
    public static final CANdleColorBlock k2Yellow = new CANdleColorBlock(kElectricYellow, 2);
    public static final CANdleColorBlock kYellow = new CANdleColorBlock(kElectricYellow, 1);

    // Color Patterns
    public static final CANdlePattern kEBlueDashed1 = new CANdlePattern(.25, kEBlueBlock, kBlackBlock);
    public static final CANdlePattern kEBlueDashed2 = new CANdlePattern(.25, kBlackBlock, kEBlueBlock);

    // The next three patterns are quite verbose, but right now, I couldn't think of a better way to do it.
    public static final CANdlePattern kYellowDashed1 = new CANdlePattern(.10, 
      kBlackBlock, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,

      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kBlackBlock
      );

    // This pattern is nearly identical to the previous one, but the first and last
    // blocks of each of the two sections are different
    public static final CANdlePattern kYellowDashed2 = new CANdlePattern(.10,
      kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k2Yellow,

      k2Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack
    );

    public static final CANdlePattern kYellowDashed3 = new CANdlePattern(.10,
      kYellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, kYellow,

      kYellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, kYellow
    );

    public static final CANdlePattern kYellowDashed4 = new CANdlePattern(.10,
      k2Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,

                kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k2Yellow
    );

    public static final CANdlePattern kYellowDashed5 = new CANdlePattern(.10,
      k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow,
      kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kBlackBlock,

                kBlackBlock, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack, k3Yellow, kDoubleBlack,
      k3Yellow, kDoubleBlack, k3Yellow
    );
}

  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = 4.0;
    public static final double kMaxAccelerationMetersPerSecondSquared = 3;
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
    public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;

    public static final double kPXController = 1;
    public static final double kPYController = 1;
    public static final double kPThetaController = 1;

    // Constraint for the motion profiled robot angle controller
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints = new TrapezoidProfile.Constraints(
        kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
  }

  public static final class NeoMotorConstants {
    public static final double kFreeSpeedRpm = 5676;
  }
}
