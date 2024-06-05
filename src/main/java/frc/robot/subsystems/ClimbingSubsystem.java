package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Dashboard;
import frc.robot.Constants.ClimbConstants;

public class ClimbingSubsystem extends SubsystemBase {

    private final CANSparkMax m_rightClimber = new CANSparkMax(ClimbConstants.kRightClimbMotorCanId, MotorType.kBrushless);
    private final CANSparkMax m_leftClimber = new CANSparkMax(ClimbConstants.kLeftClimbMotorCanId, MotorType.kBrushless);

    private final DigitalInput m_rightLimitSwitch = new DigitalInput(ClimbConstants.kRightLimitSwitchPWMPort);
    private final DigitalInput m_leftLimitSwitch = new DigitalInput(ClimbConstants.kLeftLimitSwitchPWMPort);

    public void raiseClimbers() {
        m_leftClimber.set(-Dashboard.MainTab.climbingSpeedEntry.getDouble(ClimbConstants.kDefaultClimbSpeed)); // left climber is inverted
        m_rightClimber.set(Dashboard.MainTab.climbingSpeedEntry.getDouble(ClimbConstants.kDefaultClimbSpeed));

    }

    public void stopClimbers() {
        m_leftClimber.set(0);
        m_rightClimber.set(0);
    }

    public void lowerClimbers() {
        // as long as the hooks remain clear of the limit switches, they still have room to go down.
        if (m_leftLimitSwitch.get()) m_leftClimber.set(Dashboard.MainTab.climbingSpeedEntry.getDouble(ClimbConstants.kDefaultClimbSpeed)); 
            else m_leftClimber.set(0);
        if (m_rightLimitSwitch.get()) m_rightClimber.set(-Dashboard.MainTab.climbingSpeedEntry.getDouble(ClimbConstants.kDefaultClimbSpeed)); 
            else m_rightClimber.set(0);
    }
}
