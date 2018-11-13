package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.Drivetrain;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Components.Sensors.Cameras.MotoG4;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.DriveBotFieldCentricTest;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Vision.Detectors.BlockDetector;
import org.firstinspires.ftc.teamcode.Vision.Detectors.GenericDetector;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Size;

import ftc.vision.Detector;

@Autonomous(name = "sample test", group = "none")
public class VisionTest extends WestBot15 {
    BlockDetector detector;
    boolean hasDrove;
    double prevLeft0, prevRight = 0;
    Point newNewPoint = new Point();
    Vector2 sampleVect = new Vector2();
    public void init(){
        msStuckDetectInit = 500000;
        super.init();
        activateGamepad1();
        detector = new BlockDetector();
        detector.opState = Detector.OperatingState.TUNING;
        FtcRobotControllerActivity.frameGrabber.detector = detector;

        drivetrain.controlState = TankDT.ControlState.FIELD_CENTRIC;
        drivetrain.direction = TankDT.Direction.FOR;
    }
    public void initLoop(){
        //telemetry.addData("location 1", motoG4.rearCamera.getObjectLocation(detector.elements.get(0), detector.result().size(), 2));
    }
    @Override
    public void start(){
        super.start();

    }

    public void loop(){
        drivetrain.maxSpeed = 0.3;
        setRobotAngle();
        Vector2 temp = new Vector2(detector.element.y, -detector.element.x);
        temp.x -= 480/ 2;
        temp.y += 640 / 2;

        double vertAng = temp.y / 640 * motoG4.rearCamera.verticalAngleOfView();
        double horiAng = temp.x / 480 * motoG4.rearCamera.horizontalAngleOfView();

        double newY = (11.66666666666 - 2 / 2) / Math.tan(-vertAng) - 3.375;
        double newX = newY * Math.tan(horiAng) + 2.333333333333333;
        Point newPoint = new Point(newX, newY);
        newNewPoint = new Point(newX, newY);
        if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER) {
            hasDrove = true;
            sampleVect = new Vector2(newX, newY);
        }
        if(hasDrove) {
            drivetrain.updateLocation(drivetrain.averageLeftEncoders() - prevLeft0, drivetrain.averageRightEncoders() - prevRight);
            prevLeft0 = drivetrain.averageLeftEncoders();
            prevRight = drivetrain.averageRightEncoders();
            drivetrain.driveToPoint(sampleVect.x, sampleVect.y, TankDT.Direction.FOR);
            if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER)
                hasDrove = false;
        }
        telemetry.addData("sample location: ", newPoint);
        telemetry.addData("robot location: ", drivetrain.position);
    }

    public void stop(){
        super.stop();
        detector.isInitialized = false;
    }
}
