package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {
    
    private final PWMSparkMax m_intakeMotor = new PWMSparkMax(0);
    private final PWMSparkMax m_loaderMotor = new PWMSparkMax(1);

    public void activateIntake(double speedMetersPerSecond) {
        m_intakeMotor.set(speedMetersPerSecond);
        m_loaderMotor.set(speedMetersPerSecond);
    }

    public void deactivateIntake() {
        m_intakeMotor.set(0);
        m_loaderMotor.set(0);
    }
}
