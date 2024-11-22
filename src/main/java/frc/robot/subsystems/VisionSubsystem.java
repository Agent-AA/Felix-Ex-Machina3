package frc.robot.subsystems;

import org.photonvision.PhotonCamera;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class VisionSubsystem extends SubsystemBase {
    // This camera is were we get all of our robot's vision data
    PhotonCamera camera = new PhotonCamera(Constants.VisionConstants.VISION_CAMERA_NAME);
    
    private final Pigeon2 m_gyro = new Pigeon2(20);


    /**
     * This method is called periodically by the {@link CommandScheduler} like the
     * method of the same name in {@link RobotContainer}.
     */
    @Override
    public void periodic() {
        System.out.println(camera.getLatestResult());
        System.out.println(getHeading());
    }

    /**
     * Returns the heading of the robot.
     * 
     * @return the robot's heading in degrees, from -180 to 180
     */
    public double getHeading() {
        // the m_gyro.getAngle() is negative because we need to invert it
        return Rotation2d.fromDegrees(-m_gyro.getAngle()).getDegrees();
    }
}