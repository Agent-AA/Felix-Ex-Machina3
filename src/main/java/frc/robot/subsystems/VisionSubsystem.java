package frc.robot.subsystems;

import java.io.IOException;
import java.util.Optional;

import org.photonvision.*;
import org.photonvision.PhotonPoseEstimator.*;
import org.photonvision.targeting.*;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * The VisionSubsystem is one of the more complex robot subsystems, and unlike the others,
 * exists without any commands.
 */
public class VisionSubsystem extends SubsystemBase{

    private static final PhotonCamera camera = new PhotonCamera(Constants.VisionConstants.VISION_CAMERA_NAME);
    // TODO add correct camera offsets
    private static final Transform3d robotToCamera = new Transform3d(new Translation3d(0.0, 0.0, 0.0), new Rotation3d());
    private PhotonPoseEstimator estimator;
    private AprilTagFieldLayout fieldLayout;
    private PhotonPipelineResult result;


    public VisionSubsystem() {
        try {
            // Load our field layout from the deploy directory and construct our pose
            fieldLayout = new AprilTagFieldLayout(Filesystem.getDeployDirectory().toString() + "/2024-crescendo.json");
            estimator = new PhotonPoseEstimator(fieldLayout, PoseStrategy.CLOSEST_TO_REFERENCE_POSE, camera, robotToCamera);
        } catch (IOException e) {
            System.out.println("Failed to load field layout file from" + Filesystem.getDeployDirectory().toString() + "/2024-crescendo.json");
            e.printStackTrace();
        }
    }

    @Override
    public void periodic() {
        result = camera.getLatestResult();
    }

    /**
     * Returns the {@link PhotonTrackedTarget} representation of the target
     * with the specified id. PhotonVision docs</a> require that the method first check if the camera is tracking any targets at all,
     * or else <code>result.getTargets()</code> will throw a {@link NullPointerException}.</p>
     * 
     * @param id the id of the target to get
     * @return a {@link PhotonTrackedTarget} object, or null if the target is not being tracked.
     */
    public PhotonTrackedTarget getTrackedTarget(int id) {
        try {
            for (PhotonTrackedTarget target : result.getTargets()) {
                if (target.getFiducialId() == id) {
                    return target;
                }
            } return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

     /**
     * <p>Returns the "best" {@link PhotonTrackedTarget}, i.e. april tags, that the camera is currently tracking. The
     * <a href="https://docs.photonvision.org/en/v2025.0.0-beta-4/docs/programming/photonlib/getting-target-data.html">
     * PhotonVision docs</a> require that the method first check if the camera is tracking any targets at all,
     * or else <code>result.getTargets()</code> will throw a {@link NullPointerException}.</p>
     * 
     * <p> The "best" target can be defined in any of several ways but is done so through the PhotonVision
     * pipeline configuration through the browser interface.</p>
     * 
     * @return a {@link PhotonTrackedTarget} object, or null if no targets are being tracked.
     */
    public PhotonTrackedTarget getBestTarget() {
        try {
            return result.getBestTarget();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Returns a {@link Transform3d} offset to the target with the specified id,
     * where X = forward, Y = left, and Z = up.
     * 
     * @param id the id of the target to get
     * @return a {@link Transform3d} offset, or null if the target is not being tracked.
     */
    public Transform3d getTargetOffset(int id) {
        try {
            PhotonTrackedTarget target = getTrackedTarget(id);
            return target.getBestCameraToTarget();
        } catch (NullPointerException e) {
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
        try {
            return getBestTarget().getBestCameraToTarget();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Returns the estimated robot pose based on PhotonVision as an {@link Optional} of {@link EstimatedRobotPose}.
     * 
     * @return an {@link Optional} of {@link EstimatedRobotPose}.
     */
    public Optional<EstimatedRobotPose> estimateRobotPose() {
        return estimator.update();
    }
}