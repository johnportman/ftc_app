package org.firstinspires.ftc.teamcode.robotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotTemplates.Mecanum2;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;


/**
 * Created by Frank Portman on 5/27/2018
 */
@TeleOp(name = "Mec2 Test Drive", group = "Test Drive")
public class Mecanum2TestDrive extends Mecanum2 {
    boolean switchMode = false, canSwitch = false;
    double rt;
    public void init(){
        super.init();
        super.maxSpeed = 1;
        super.brakePow = 0.01;
        super.ControlState = ControlState.ARCADE;
        super.activateGamepad1();
    }
    public void start(){
        super.start();
    }
    @Override
    public void loop(){
        super.loop();
        rt = gamepad1.right_trigger;
        switch(ControlState){
            case ARCADE:
                if(switchMode){
                    ControlState = ControlState.FIELD_CENTRIC;
                    switchMode = false;
                    canSwitch = false;
                }
                else if(rt < UniversalConstants.Triggered.TRIGGER){
                    switchMode = false;
                    canSwitch = true;
                }
                else if(rt > UniversalConstants.Triggered.TRIGGER && canSwitch)
                    switchMode = true;
                break;
            case FIELD_CENTRIC:
                if(switchMode){
                    ControlState = ControlState.ARCADE;
                    switchMode = false;
                    canSwitch = false;
                }
                else if(rt < UniversalConstants.Triggered.TRIGGER){
                    switchMode = false;
                    canSwitch = true;
                }
                else if(rt > UniversalConstants.Triggered.TRIGGER && canSwitch)
                    switchMode = true;
                break;
        }
    }
}
