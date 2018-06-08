package org.firstinspires.ftc.teamcode.Components.Mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Components.Sensors.MotorEncoder;
import org.firstinspires.ftc.teamcode.robotUniversal.PIDController;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;
import org.opencv.core.Range;

/**
 * Created by Frank Portman on 6/2/2018
 */
public class IncrementalMotor {
    public DcMotor motor;
    public MotorEncoder encoder;
    public double desiredPow;
    public double acceleration;
    public double decelleration;
    public double currentPow;
    public double minPow;
    public IncrementalMotor(DcMotor dc, double accPerSec, double decPerSec, double min){
        motor = dc;
        encoder = new MotorEncoder(motor);
        encoder.initEncoder();
        acceleration = Math.abs(accPerSec);
        decelleration = Math.abs(decPerSec);
        minPow = min;
    }
    public IncrementalMotor(DcMotor dc, double acc, double dec){
        motor = dc;
        encoder = new MotorEncoder(motor);
        encoder.initEncoder();
        acceleration = Math.abs(acc);
        decelleration = Math.abs(dec);
        minPow = acceleration;
    }
    public double getPower(){
        return encoder.updateEncoder();
    }
    public synchronized void setPower(){
        if(currentPow != desiredPow) {
            if (currentPow > 0)
                currentPow += currentPow < desiredPow ? acceleration : -decelleration;
            else if (currentPow < 0)
                currentPow += currentPow < desiredPow ? decelleration : -acceleration;
            else
                currentPow += UniversalFunctions.sign(desiredPow) * minPow;
        }
        if(UniversalFunctions.withinTolerance(Math.abs(desiredPow), Math.abs(currentPow), decelleration, acceleration))
            currentPow = desiredPow;
        currentPow = UniversalFunctions.clamp(-1, currentPow, 1);
        motor.setPower(currentPow);
    }
    public void stop(){
        motor.setPower(0);
    }

}
