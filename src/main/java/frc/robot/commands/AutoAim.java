package frc.robot.commands;

import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Constants.OIConstants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.Drive.DriveSubsystem;

public class AutoAim extends Command {

    private final XboxController m_driverController = RobotContainer.m_driverController;
    private final DriveSubsystem m_robotDrive = RobotContainer.m_robotDrive;
    private final VisionSubsystem m_robotVision = RobotContainer.m_robotVision;
    private final int aprilTagId;

    private final double yawTolerance;
    private double forward, strafe, turn, targetYaw;
    private boolean targetVisible;

    /**
     * Constructs a new AutoAim command.
     * 
     * @param aprilTagId the ID of the aprilTag to recognize and aim at. This command will *only* work for this tag.
     * @param yawTolerance the maximum error (in degrees, we think), to tolerate being off by. Otherwise, the robot will correct itself.
     */
    public AutoAim(int aprilTagId, double yawTolerance) {
        addRequirements(m_robotDrive, m_robotVision);
        this.aprilTagId = aprilTagId;
        this.yawTolerance = yawTolerance;
    }

    @Override
    public void execute() {
        // Calculate drivetrain commands from Joystick values
        forward = -MathUtil.applyDeadband(m_driverController.getLeftY(), OIConstants.kDriveDeadband);
        strafe = -MathUtil.applyDeadband(m_driverController.getLeftX(), OIConstants.kDriveDeadband);
        turn = -MathUtil.applyDeadband(m_driverController.getRightX(), OIConstants.kDriveDeadband);
        targetYaw = 0.0;

        // Read relevant data from VisionSubsystem
        if (m_robotVision.aprilTagsVisible()) {
            // At least one aprilTag was seen by the camera
            for (PhotonTrackedTarget aprilTag : m_robotVision.getAprilTags()) {
                if (aprilTag.getFiducialId() == aprilTagId) {
                    // Found target tag, record its information
                    targetYaw = aprilTag.getYaw();
                    targetVisible = true;
                }
            }
        } else {targetVisible = false;}

        // If target aprilTag is seen
        if (targetVisible && Math.abs(targetYaw) > yawTolerance) {
            // Auto-align to target
            turn = targetYaw * Constants.ModuleConstants.kTurningP * Constants.AutoConstants.kMaxAngularSpeedRadiansPerSecond;
            m_robotDrive.drive(forward, strafe, turn, true, false);
        }
    }

    @Override
    public boolean isFinished() {
        if (!targetVisible || Math.abs(targetYaw) < yawTolerance) {
            return true;
        } else {
            return false;
        }
    }
}
