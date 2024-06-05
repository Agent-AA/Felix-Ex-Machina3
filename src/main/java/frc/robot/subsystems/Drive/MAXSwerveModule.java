// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Drive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.RelativeEncoder;

import frc.robot.Constants.ModuleConstants;

public class MAXSwerveModule {
  private final CANSparkMax m_drivingSparkMax;
  private final CANSparkMax m_turningSparkMax;

  private final RelativeEncoder m_drivingEncoder;
  private final CANcoder m_turningEncoder;

  private final PIDController m_drivingPIDController;
  private final PIDController m_turningPIDController;

  private double m_chassisAngularOffset = 0;
  private SwerveModuleState m_desiredState = new SwerveModuleState(0.0, new Rotation2d());

  /**
   * Constructs a MAXSwerveModule and configures the driving and turning motor,
   * encoder, and PID controller. This configuration is specific to the REV
   * MAXSwerve Module built with NEOs, SPARKS MAX, and a Through Bore
   * Encoder.
   */
  public MAXSwerveModule(int drivingCANId, int turningCANId, int turningEncoderCANId, double chassisAngularOffset) {
    m_drivingSparkMax = new CANSparkMax(drivingCANId, MotorType.kBrushless);
    m_turningSparkMax = new CANSparkMax(turningCANId, MotorType.kBrushless);

    // Factory reset, so we get the SPARKS MAX to a known state before configuring
    // them. This is useful in case a SPARK MAX is swapped out.
    m_drivingSparkMax.restoreFactoryDefaults();
    m_turningSparkMax.restoreFactoryDefaults();

    m_drivingSparkMax.setInverted(true);
    m_turningSparkMax.setInverted(true);

    // Setup encoders and PID controllers for the driving and turning SPARKS MAX.
    m_drivingEncoder = m_drivingSparkMax.getEncoder(); // for some reason this works even though I don't think it should???
    m_turningEncoder = new CANcoder(turningEncoderCANId);

    m_drivingPIDController = new PIDController(ModuleConstants.kDrivingP, ModuleConstants.kDrivingI, ModuleConstants.kDrivingD);
    m_turningPIDController = new PIDController(ModuleConstants.kTurningP, ModuleConstants.kTurningI, ModuleConstants.kTurningD);

    // Apply position and velocity conversion factors for the driving encoder. The
    // native units for position and velocity are rotations and RPM, respectively,
    // but we want meters and meters per second to use with WPILib's swerve APIs.
    m_drivingEncoder.setPositionConversionFactor(ModuleConstants.kDrivingEncoderPositionFactor);
    m_drivingEncoder.setVelocityConversionFactor(ModuleConstants.kDrivingEncoderVelocityFactor);

    // Enable PID wrap around for the turning motor. This will allow the PID
    // controller to go through 0 to get to the setpoint i.e. going from 350 degrees
    // to 10 degrees will go through 0 rather than the other direction which is a
    // longer route.

    m_turningPIDController.enableContinuousInput(ModuleConstants.kTurningEncoderPositionPIDMinInput, ModuleConstants.kTurningEncoderPositionPIDMaxInput);

    // Set the PID gains for the driving motor. Note these are example gains, and you
    // may need to tune them for your own robot!
    m_drivingPIDController.setP(ModuleConstants.kDrivingP);
    m_drivingPIDController.setI(ModuleConstants.kDrivingI);
    m_drivingPIDController.setD(ModuleConstants.kDrivingD);

    m_drivingSparkMax.setIdleMode(ModuleConstants.kDrivingMotorIdleMode);
    m_turningSparkMax.setIdleMode(ModuleConstants.kTurningMotorIdleMode);
    m_drivingSparkMax.setSmartCurrentLimit(ModuleConstants.kDrivingMotorCurrentLimit);
    m_turningSparkMax.setSmartCurrentLimit(ModuleConstants.kTurningMotorCurrentLimit);

    // Save the SPARK MAX configurations. If a SPARK MAX browns out during
    // operation, it will maintain the above configurations.
    m_drivingSparkMax.burnFlash();
    m_turningSparkMax.burnFlash();

    m_chassisAngularOffset = chassisAngularOffset;
    m_desiredState.angle = getAngle();
    m_drivingEncoder.setPosition(0);
  }

  /**
   * Returns the current angle of the module.
   *
   * @return The current angle of the module.
   */
  public Rotation2d getAngle() {
    // The encoder's getPosition() method returns total rotations, so we need to
    // multiply by 2pi to get the angle in radians and then modulo to get the non-cumulative angle.
    return new Rotation2d(((m_turningEncoder.getPosition().getValueAsDouble() * 2 * Math.PI) % (2 * Math.PI)) - m_chassisAngularOffset);
  }

  /**
   * Returns the current state of the module.
   *
   * @return The current state of the module.
   */
  public SwerveModuleState getState() {
    // Apply chassis angular offset to the encoder position to get the position
    // relative to the chassis.
    return new SwerveModuleState(m_drivingEncoder.getVelocity(), getAngle());
  }

  /**
   * Returns the current position of the module.
   *
   * @return The current position of the module.
   */
  public SwerveModulePosition getPosition() {
    // Apply chassis angular offset to the encoder position to get the position
    // relative to the chassis.
    return new SwerveModulePosition(
        m_drivingEncoder.getPosition(), getAngle());
  }

  /**
   * Sets the P, I, and D gains for the driving motor.
   *
   * @param values The P, I, and D gains for the driving motor.
   * @throws IllegalArgumentException the values argument does not have exactly 3 elements.
   */
  public void setDrivingPIDValues(double[] values) throws IllegalArgumentException {

    if (values.length != 3) throw new IllegalArgumentException("Argument does not have exactly 3 elements.");

    m_drivingPIDController.setP(values[0]);
    m_drivingPIDController.setI(values[1]);
    m_drivingPIDController.setD(values[2]);
  }

  /**
   * Sets the P, I, and D gains for the turning motor.
   *
   * @param values The P, I, and D gains for the turning motor.
   * @throws IllegalArgumentException the values argument does not have exactly 3 elements.
   */
  public void setTurningPIDValues(double[] values) throws IllegalArgumentException {

    if (values.length != 3) throw new IllegalArgumentException("Argument does not have exactly 3 elements.");

    m_turningPIDController.setP(values[0]);
    m_turningPIDController.setI(values[1]);
    m_turningPIDController.setD(values[2]);
  }

  /**
   * Sets the desired state for the module.
   *
   * @param desiredState Desired state with speed and angle.
   */
  public void setDesiredState(SwerveModuleState desiredState) {

    // Optimize the reference state to avoid spinning further than 90 degrees.
    SwerveModuleState optimizedDesiredState = SwerveModuleState.optimize(desiredState, getAngle());

    // Command driving and turning SPARKS MAX towards their respective setpoints.
    m_drivingSparkMax.set(m_drivingPIDController.calculate(getState().speedMetersPerSecond, optimizedDesiredState.speedMetersPerSecond));
    m_turningSparkMax.set(m_turningPIDController.calculate(getAngle().getRadians(), optimizedDesiredState.angle.getRadians()));
    m_desiredState = optimizedDesiredState;
  }

  /**
   * Gets the desired state for the module
   *
   * @return Desired state with speed and angle.
   */
  public SwerveModuleState getDesiredState() {
    return m_desiredState;
  }

  /** Zeroes all the SwerveModule encoders. */
  public void resetEncoders() {
    m_drivingEncoder.setPosition(0);
  }
}
