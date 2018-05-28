package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;

/**
 * Created by Frank Portman on 5/21/2018
 */
public class WestCoastDT extends Drivetrain {
    public DcMotor rf, lf, la, ra;
    public double turnMult, angleBetween, directionMult = 1, cos;
    public double maxTurn = 1;
    public double leftPow, rightPow;
    public Direction direction;
    public ControlState cs;
    private boolean turn = false;
    public FCTurnState ts;
    public WestCoastDT(double brakePow){
        super(brakePow);
        leftPow = 0;
        rightPow = 0;
    }
    //Different control systems
    public enum ControlState{
        ARCADE,
        TANK,
        FIELD_CENTRIC
    }
    //Two algorithms for turning in field-centric mode
    public enum FCTurnState{
        SMOOTH,
        FAST
    }
    @Override
    public void init(){
        super.init();
    }
    @Override
    public void start(){
        super.start();
    }
    //Basic Tele-Op driving functionality
    public void loop(){
        updateGamepad1();
        switch(cs) {
            case ARCADE:
                turnMult = 1 - leftStick1.magnitude() * (1 - maxTurn);
                leftPow = -leftStick1.y - turnMult * rightStick1.x;
                rightPow = -leftStick1.y + turnMult * rightStick1.x;
                break;
            case FIELD_CENTRIC:
                setRobotAngle();
                angleBetween = leftStick1.angleBetween(robotAngle);
                if (leftStick1.magnitude() < UniversalConstants.Triggered.STICK)
                    brake();
                else {
                    switch (direction) {
                        case FOR:
                            if (Math.sin(angleBetween) < 0 && turn) {
                                direction = Direction.BACK;
                                directionMult *= -1;
                                turn = false;
                            } else if (Math.sin(angleBetween) >= 0)
                                turn = true;
                            break;
                        case BACK:
                            if (Math.sin(angleBetween) > 0 && turn) {
                                direction = Direction.FOR;
                                turn = false;
                                directionMult *= -1;
                            } else if (Math.sin(angleBetween) <= 0)
                                turn = true;
                            break;
                    }
                    cos = Math.cos(angleBetween);
                    switch(ts){
                        case FAST:
                            turnMult = Math.abs(cos) + 1;
                            leftPow = directionMult * (-leftStick1.magnitude() - turnMult * cos);
                            rightPow = directionMult * (-leftStick1.magnitude() + turnMult * cos);
                            break;
                        case SMOOTH:
                            if(cos > 0) {
                                rightPow = -directionMult;
                                leftPow = directionMult * Math.cos(2 * angleBetween);
                            }
                            else{
                                leftPow = -directionMult;
                                rightPow = directionMult * Math.cos(2 * angleBetween);
                            }
                    }
                }
                break;
            case TANK:
                leftPow = -gamepad1.left_stick_y;
                rightPow = -gamepad1.right_stick_y;
                break;
        }
        setLeftPow();
        setRightPow();
    }
    //returns the direction the robot is moving
    public Direction setTurnDir(){
        if(leftPow + rightPow > 0)
            direction = Direction.FOR;
        else if(leftPow != rightPow)
            direction = Direction.BACK;
        return direction;
    }
    //Sets the power of the left motor(s)
    public void setLeftPow(double pow){
        setPower(lf, pow);
        setPower(la, pow);
        leftPow = pow;
    }
    //Sets the power of the right motor(s)
    public void setRightPow(double pow){
        setPower(rf, pow);
        setPower(ra, pow);
        rightPow = pow;
    }
    //Sets the power of the left motor(s) to the leftPow variable
    public void setLeftPow(){
        setLeftPow(leftPow);
    }
    //Sets the power of the right motor(s) to the rightPow variable
    public void setRightPow(){
        setRightPow(rightPow);
    }
    //Sets the maximum speed of the drive motors
    public void setSpeed(double speed){
        maxSpeed = speed;
    }
    public void initMotors(){
        rf = hardwareMap.dcMotor.get("rf");
        lf = hardwareMap.dcMotor.get("lf");
        la = hardwareMap.dcMotor.get("la");
        ra = hardwareMap.dcMotor.get("ra");
        rf.setDirection(REVERSE);
        ra.setDirection(REVERSE);
        lf.setDirection(FORWARD);
        la.setDirection(FORWARD);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.UNKNOWN);
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.UNKNOWN);
        la.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.UNKNOWN);
        ra.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.UNKNOWN);
    }
    public void brake(){

    }
}
