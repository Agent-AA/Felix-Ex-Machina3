package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShootingSubsystem extends SubsystemBase {
    
    private final PWMSparkMax m_intakeMotor = new PWMSparkMax(0);
    private final PWMSparkMax m_loaderMotor = new PWMSparkMax(1);

    private final CANSparkMax m_topShooterMotor = new CANSparkMax(14, MotorType.kBrushless);
    private final CANSparkMax m_bottomShooterMotor = new CANSparkMax(15, MotorType.kBrushless);

    public void activateIntake(double speedMetersPerSecond) {
        m_intakeMotor.set(speedMetersPerSecond);
        m_loaderMotor.set(speedMetersPerSecond);
    }

    public void deactivateIntake() {
        m_intakeMotor.set(0);
        m_loaderMotor.set(0);
    }

    public void activateShooter(double speedMetersPerSecond) {
        m_topShooterMotor.set(speedMetersPerSecond);
        m_bottomShooterMotor.set(speedMetersPerSecond);
    }
}
