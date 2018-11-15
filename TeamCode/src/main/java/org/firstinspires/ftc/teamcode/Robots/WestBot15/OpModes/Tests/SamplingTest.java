package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.Vision.Detectors.BlockDetector;
import org.opencv.core.Point;

import ftc.vision.Detector;

@Autonomous(name = "sampling test", group = "none")
public class SamplingTest extends WestBot15 {
    BlockDetector detector;
    boolean hasDrove;
    double prevLeft, prevRight = 0;
    double hardNewY;
    boolean hasDriven = false;
    Point newNewPoint = new Point();
    double rightEncPosition, leftEncPosition;
    Vector2 sampleVect = new Vector2();
    Pose robotPose = new Pose();
    double xAng = 0;
    public void init(){
        drivetrain.position = new Pose();
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
        drivetrain.updateLocation(drivetrain.averageLeftEncoders() - prevLeft, drivetrain.averageRightEncoders() - prevRight);
        setRobotAngle();
        drivetrain.maxSpeed = 0.2;
        Vector2 temp = new Vector2(detector.element.y, -detector.element.x);
        temp.x -= 480/ 2;
        temp.y += 640 / 2;

        double vertAng = temp.y / 640 * motoG4.rearCamera.verticalAngleOfView();
        double horiAng = temp.x / 480 * motoG4.rearCamera.horizontalAngleOfView();

        double newY = (10 - 2 / 2) / Math.tan(-vertAng);

        if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && !hasDrove) {
            hasDrove = true;
        }
        if(gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER && hasDrove) {
            hasDrove = false;
            drivetrain.setLeftPow(0);
            drivetrain.setRightPow(0);
        }
        int i;
        i = gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER ? 1 : -1;

        if(hasDrove){
            if(gamepad1.right_stick_button){
                hasDriven = true;
                hardNewY = newY;
                rightEncPosition = drivetrain.averageRightEncoders();
                leftEncPosition = drivetrain.averageLeftEncoders();
            }
            else
                hasDriven = false;
            if(hasDriven){
                if(drivetrain.averageLeftEncoders() - leftEncPosition > drivetrain.ENC_PER_INCH * hardNewY) {
                    drivetrain.setLeftPow(0);
                    drivetrain.setRightPow(0);
                }
                else {
                    drivetrain.setRightPow(1);
                    drivetrain.setLeftPow(1);
                }
            }
            else{
                drivetrain.setLeftPow(-i * Math.sin(horiAng));
                drivetrain.setRightPow(i * Math.sin(horiAng));
            }
        }
        /*if(hasDrove) {
            drivetrain.updateLocation(drivetrain.averageLeftEncoders() - prevLeft0, drivetrain.averageRightEncoders() - prevRight);
            prevLeft0 = drivetrain.averageLeftEncoders();
            prevRight = drivetrain.averageRightEncoders();
            drivetrain.driveToPoint(sampleVect.x, sampleVect.y, TankDT.Direction.FOR);
            if(gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER) {
                hasDrove = false;
                drivetrain.setLeftPow(0);
                drivetrain.setRightPow(0);
            }
        }*/
        telemetry.addData("hasDrove", hasDrove);
        telemetry.addData("horiAng: ", Math.toDegrees(horiAng));
        telemetry.addData("robot ang: ", Math.toDegrees(robotAngle.angle()));
        telemetry.addData("left pow", drivetrain.leftFore.getPower());
        telemetry.addData("hasDriven, ", hasDriven);
        telemetry.addData("cosThing, ", Math.abs(Math.cos(xAng - robotAngle.angle())));
        telemetry.addData("desired distance, ", drivetrain.ENC_PER_INCH * hardNewY);
        telemetry.addData("distance traveled, ", drivetrain.averageLeftEncoders() - leftEncPosition );

    }

    public void stop(){
        super.stop();
        detector.isInitialized = false;
    }
    public void updateLocation(double leftChange, double rightChange){
        leftChange = leftChange / drivetrain.ENC_PER_INCH;
        rightChange = rightChange / drivetrain.ENC_PER_INCH;
        double angle = 0;
        Vector2 turnVector = new Vector2();
        if(rightChange == leftChange)
            turnVector.setFromPolar(rightChange, robotPose.angle);
        else {
            double radius = drivetrain.DISTANCE_BETWEEN_WHEELS / 2 * (leftChange + rightChange) / (rightChange - leftChange);
            angle = (rightChange - leftChange) / (drivetrain.DISTANCE_BETWEEN_WHEELS);
            radius = Math.abs(radius);
            turnVector.setFromPolar(radius, angle);
            turnVector.setFromPolar(radius - turnVector.x, angle);
            if(Math.min(leftChange, rightChange) == -UniversalFunctions.maxAbs(leftChange, rightChange))
                turnVector.x *= -1;
        }
        turnVector.rotate(robotPose.angle);
        robotPose.add(turnVector);
        robotPose.angle += angle;
    }
}