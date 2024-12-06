package frc.robot.commands;

import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.Drive.DriveSubsystem;

public class AutoAim extends Command {

    private final XboxController m_driverController = RobotContainer.m_driverController;
    private final DriveSubsystem m_robotDrive = RobotContainer.m_robotDrive;
    private final VisionSubsystem m_robotVision = RobotContainer.m_robotVision;
    private final int aprilTagId;

    private double forward, strafe, turn, targetYaw;
    private boolean targetVisible;

    public AutoAim(int aprilTagId) {
        addRequirements(RobotContainer.m_robotDrive, RobotContainer.m_robotVision);
        this.aprilTagId = aprilTagId;
    }

    @Override
    public void initialize() {
        // Calculate drivetrain commands from Joystick values
        forward = -m_driverController.getLeftY() * Constants.DriveConstants.kMaxSpeedMetersPerSecond;
        strafe = -m_driverController.getLeftX() * Constants.DriveConstants.kMaxSpeedMetersPerSecond;
        turn = -m_driverController.getRightX() * Constants.DriveConstants.kMaxAngularSpeed;

        // Read relevant data from VisionSubsystem
        targetVisible = false;
        targetYaw = 0.0;

        if (m_robotVision.aprilTagsVisible()) {
            // At least one aprilTag was seen by the camer
            for (PhotonTrackedTarget aprilTag : m_robotVision.getAprilTags()) {
                if (aprilTag.getFiducialId() == aprilTagId) {
                    // Found target tag, record its information
                    targetYaw = aprilTag.getYaw();
                    targetVisible = true;
                }
            }
        } 
    }

    @Override
    public void execute() {
        if (targetVisible) {
            // Auto-align to target
            turn = -1.0 * targetYaw * Constants.ModuleConstants.kTurningP * Constants.DriveConstants.kMaxAngularSpeed;
            m_robotDrive.drive(forward, strafe, turn, true, true);
        }
    }
}
