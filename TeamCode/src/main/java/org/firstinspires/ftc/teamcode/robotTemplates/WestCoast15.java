package org.firstinspires.ftc.teamcode.robotTemplates;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;

/**
 * Created by Frank Portman on 5/21/2018
 */
public class WestCoast15 extends WestCoastDT{
    DcMotor rf, lf, lr, rr;
    double rt, x, y, turnMult, normAngle, sin, mult, cos, fsTurn;
    boolean turn;
    public WestCoast15(double brakePow, double sped){
        super(brakePow);
        speed = sped;
    }
    public void loop(){
        rt = gamepad1.right_trigger;
        updateGamepad1();
        switch(cs) {
            case ARCADE:
                turnMult = 1 - lStick1.magnitude() * (1 - super.turnMult);
                leftPow = (-lStick1.y - turnMult * rStick1.x) * speed;
                rightPow = (-lStick1.y + turnMult * rStick1.x)* speed;
                break;
            case FIELD_CENTRIC:
                y = lStick1.y;
                x = lStick1.x;
                normAngle = Math.toRadians(UniversalFunctions.normalizeAngle(lStick1.angle(), normalizeGyroAngle()));
                if (lStick1.magnitude() < UniversalConstants.Triggered.STICK)
                    brake();
                else {
                    switch (direction) {
                        case FOR:
                            sin = Math.sin(normAngle);
                            if (Math.sin(normAngle) < 0 && turn) {
                                direction = Direction.BACK;
                                mult *= -1;
                                turn = false;
                            } else if (Math.sin(normAngle) >= 0)
                                turn = true;
                            break;
                        case BACK:
                            sin = Math.sin(normAngle);
                            if (Math.sin(normAngle) > 0 && turn) {
                                direction = Direction.FOR;
                                turn = false;
                                mult *= -1;
                            } else if (Math.sin(normAngle) <= 0)
                                turn = true;
                            break;
                    }
                    cos = Math.cos(normAngle);
                    fsTurn = (Math.abs(cos) + 1);
                    leftPow = mult * (-lStick1.magnitude() - fsTurn * cos);
                    rightPow = mult * (-lStick1.magnitude() + fsTurn * cos);
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
    public void setLeftPow(double pow) {
        lf.setPower(pow * speed);
        lr.setPower(pow * speed);
        leftPow = pow;
    }
    public void setRightPow(double pow){
        rf.setPower(pow * speed);
        rr.setPower(pow * speed);
        rightPow = pow;
    }
    public DcMotor[] motors(){
        DcMotor[] motors = {rf, lf, lr, rr};
        return motors;
    }
    public String[] names(){
        String[] names = {"rf", "lf", "lr", "rr"};
        return names;
    }
    public DcMotor.Direction[] dir(){
        DcMotor.Direction[] dir = {FORWARD, REVERSE, REVERSE, FORWARD};
        return dir;
    }
}