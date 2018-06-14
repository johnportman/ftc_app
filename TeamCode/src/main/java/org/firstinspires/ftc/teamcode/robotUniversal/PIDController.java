package org.firstinspires.ftc.teamcode.robotUniversal;

/**
 * Created by Frank Portman on 6/2/2018
 */
public class PIDController {
    public double error        ,
                  setpoint     ,
                  processVar   ,
                  integral      = 0,
                  deltaTime    ,
                  derivative   ,
                  prevError    ,
                  currentOutput,
                  time         ,
                  TI            = 0,
                  TD            = 0,
                  currentTime  ,
                  integralMax   = Double.POSITIVE_INFINITY,
                  sse           = 0;
    public double KP            = 0;
    public double KI            = 0;
    public double KD            = 0;
    public PIDController(double kp, double ki, double kd, double tc){
        KP = kp;
        KI = ki;
        KD = kd;
        deltaTime = tc;
    }
    public PIDController(double kp, double ki, double kd, double tc, double im){
        KP = kp;
        KI = ki;
        KD = kd;
        deltaTime = tc;
        integralMax = im;
    }
    //Sets TI and TD to the given parameters
    public void setStandardForm(double integralTime, double derivativeTime){
        TI = integralTime;
        TD = derivativeTime;
    }
    //one iteration of an ideal PID loop
    public void idealLoop(){
        if(time + deltaTime >= System.currentTimeMillis()) {
            error = setpoint - processVar;
            integral += error * deltaTime;
            integral = integralMax > integral ? integral : integralMax;
            derivative = (error - prevError) / deltaTime;
            time = System.currentTimeMillis();
            currentOutput = KP * error + KI * integral + KD * derivative;
            prevError = error;
            calculateSSE();
        }
    }
    //one iteration of a standard PID loop
    public void standardLoop(){
        if(time + deltaTime >= currentTime) {
            error = setpoint - processVar;
            if(time + TI >= currentTime) {
                integral += error * deltaTime;
                integral = integralMax > integral ? integral : integralMax;
            }
            if(time + TD >= currentTime)
                derivative = (error - prevError) / deltaTime;
            time = System.currentTimeMillis();
            currentOutput = KP * error + KP / TI * integral + KP * TD * derivative;
            prevError = error;
            calculateSSE();
        }
        currentTime = System.currentTimeMillis();
    }
    public void calculateSSE(){
        sse = setpoint / (processVar - currentOutput);
    }
}