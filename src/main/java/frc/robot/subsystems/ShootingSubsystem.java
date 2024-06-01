package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Dashboard;

public class ShootingSubsystem extends SubsystemBase {
    
    private final CANSparkMax m_topShooterMotor = new CANSparkMax(14, MotorType.kBrushless);
    private final CANSparkMax m_bottomShooterMotor = new CANSparkMax(15, MotorType.kBrushless);

    public void activateShooter() {
        m_topShooterMotor.set(getShootingSpeed());
        m_bottomShooterMotor.set(getShootingSpeed());
    }

    public void deactivateShooter() {
        m_topShooterMotor.set(0);
        m_bottomShooterMotor.set(0);
    }

    public void reverseShooter() {
        m_topShooterMotor.set(-getShootingSpeed());
        m_bottomShooterMotor.set(-getShootingSpeed());
    }

    private double getShootingSpeed() {
        return Dashboard.DriveTab.shootingSpeedEntry.getDouble(Constants.ShootingConstants.kdefaultShootSpeed);
    }
}
