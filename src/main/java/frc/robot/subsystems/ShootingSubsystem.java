package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShootingSubsystem extends SubsystemBase {
    
    private final CANSparkMax m_topShooterMotor = new CANSparkMax(14, MotorType.kBrushless);
    private final CANSparkMax m_bottomShooterMotor = new CANSparkMax(15, MotorType.kBrushless);

    public void activateShooter(double speedMetersPerSecond) {
        m_topShooterMotor.set(speedMetersPerSecond);
        m_bottomShooterMotor.set(speedMetersPerSecond);
    }

    public void deactivateShooter() {
        m_topShooterMotor.set(0);
        m_bottomShooterMotor.set(0);
    }
}
