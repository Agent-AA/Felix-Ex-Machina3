package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class IntakeSubsystem extends SubsystemBase {
    
    private final PWMSparkMax m_intakeMotor = new PWMSparkMax(IntakeConstants.kIntakeMotorPWMPort);
    private final PWMSparkMax m_loaderMotor = new PWMSparkMax(IntakeConstants.kLoaderMotorPWMPort);

    public void activateIntake() {
        m_intakeMotor.set(1);
        m_loaderMotor.set(1);
    }

    public void deactivateIntake() {
        m_intakeMotor.set(0);
        m_loaderMotor.set(0);
    }

    public void reverseIntake() {
        m_intakeMotor.set(-1);
        m_intakeMotor.set(-1);
    }
}
