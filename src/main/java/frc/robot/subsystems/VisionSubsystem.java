package frc.robot.subsystems;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;

import frc.robot.Constants;

public class VisionSubsystem {
    PhotonCamera camera = new PhotonCamera(Constants.VisionConstants.VISION_CAMERA_NAME);

    public PhotonPipelineResult getLatestResult() {
        return camera.getLatestResult();
    }
}
