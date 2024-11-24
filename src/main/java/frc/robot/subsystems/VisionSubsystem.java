package frc.robot.subsystems;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * The VisionSubsystem is one of the more complex robot subsystems, and unlike the others,
 * exists without any commands.
 */
public class VisionSubsystem extends SubsystemBase{
    // This camera is where we get all of our robot's vision data
    PhotonCamera camera = new PhotonCamera(Constants.VisionConstants.VISION_CAMERA_NAME);
    // These following variables store camera data, updated from the periodic() method.
    private PhotonPipelineResult result;
    private boolean hasTarget;

    /**
     * This method is called periodically by the {@link CommandScheduler}.
     */
    @Override
    public void periodic() {
        // Get latest results from the camera
        result = camera.getLatestResult();
        hasTarget = result.hasTargets();
    }

    /**
     * <p>Returns a List of {@link PhotonTrackedTarget}s, i.e. april tags, that the camera is currently tracking. The
     * <a href="https://docs.photonvision.org/en/v2025.0.0-beta-4/docs/programming/photonlib/getting-target-data.html">
     * PhotonVision docs</a> require that the method first check if the camera is tracking any targets at all,
     * or else result.getTargets() will throw a {@link NullPointerException}.<p>
     * 
     * @return a List of {@link PhotonTrackedTarget} objects, or null if no targets are being tracked.
     */
    public List<PhotonTrackedTarget> getTrackedTargets() {
        if (hasTarget) {
            return result.getTargets();
        } else {
            return null;
        }
    }

     /**
     * <p>Returns the "best" {@link PhotonTrackedTarget}, i.e. april tags, that the camera is currently tracking. The
     * <a href="https://docs.photonvision.org/en/v2025.0.0-beta-4/docs/programming/photonlib/getting-target-data.html">
     * PhotonVision docs</a> require that the method first check if the camera is tracking any targets at all,
     * or else result.getTargets() will throw a {@link NullPointerException}.</p>
     * 
     * <p> The "best" target can be defined in any of several ways but is dones so through the PhotonVision
     * pipeline configuration through the browser interface.</p>
     * 
     * @return a {@link PhotonTrackedTarget} objects, or null if no targets are being tracked.
     */
    public PhotonTrackedTarget getBestTarget() {
        if (hasTarget) {
            return result.getBestTarget();
        } else {
            return null;
        }
    }

    /**
     * Returns the best target's offset to the robot as a {@link Transform3d}, where X = forward,
     * Y = left, and Z = up.
     * 
     * @return a {@link Transform3d} offset, or null if no targets are being tracked.
     */
    public Transform3d getBestTargetOffset() {
        if (hasTarget) {
            return getBestTarget().getBestCameraToTarget();
        } else {
            return null;
        }
    }
}