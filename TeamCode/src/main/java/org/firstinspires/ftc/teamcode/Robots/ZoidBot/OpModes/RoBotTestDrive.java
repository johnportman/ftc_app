package org.firstinspires.ftc.teamcode.Robots.ZoidBot.OpModes;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Robots.ZoidBot.RoBot;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;

public class RoBotTestDrive extends RoBot {
    boolean switchControlState    = false,
            canSwitchControlState = false,
            switchTurnState       = false,
            canSwitchTurnState    = false;
    @Override
    public void init(){
        super.init();
    }

    @Override
    public void start(){
        super.start();
    }
    @Override
    public void loop(){
        updateGamepad1();
        refreshStartAngle();
        setRobotAngle();
        drivetrain.teleOpLoop(leftStick1, rightStick1, robotAngle);
        switch(drivetrain.controlState){
            case ARCADE:
                if(switchControlState){
                    drivetrain.controlState = drivetrain.controlState.FIELD_CENTRIC;
                    switchControlState = false;
                    canSwitchControlState = false;
                }
                else if(gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER){
                    switchControlState = false;
                    canSwitchControlState = true;
                }
                else if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchControlState)
                    switchControlState = true;
                break;
            case FIELD_CENTRIC:
                switch(drivetrain.turnState){
                    case FAST:
                        if(switchTurnState){
                            drivetrain.turnState = TankDT.FCTurnState.SMOOTH;
                            switchTurnState = false;
                            canSwitchTurnState = false;
                        }
                        else if(gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER){
                            switchTurnState = false;
                            canSwitchTurnState = true;
                        }
                        else if(gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchControlState)
                            switchTurnState = true;
                        break;
                    case SMOOTH:
                        if(switchTurnState){
                            drivetrain.turnState = TankDT.FCTurnState.FAST;
                            switchTurnState = false;
                            canSwitchTurnState = false;
                        }
                        else if(gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER){
                            switchTurnState = false;
                            canSwitchTurnState = true;
                        }
                        else if(gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchControlState)
                            switchTurnState = true;
                        break;
                }
                if(switchControlState){
                    drivetrain.controlState = TankDT.ControlState.TANK;
                    switchControlState = false;
                    canSwitchControlState = false;
                }
                else if(gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER){
                    switchControlState = false;
                    canSwitchControlState = true;
                }
                else if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchControlState)
                    switchControlState = true;
                break;
            case TANK:
                if(switchControlState){
                    drivetrain.controlState = TankDT.ControlState.FIELD_CENTRIC_VECTOR;
                    switchControlState = false;
                    canSwitchControlState = false;
                }
                else if(gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER){
                    switchControlState = false;
                    canSwitchControlState = true;
                }
                else if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchControlState)
                    switchControlState = true;
                break;
            case FIELD_CENTRIC_VECTOR:
                switch(drivetrain.turnState){
                    case FAST:
                        if(switchTurnState){
                            drivetrain.turnState = TankDT.FCTurnState.SMOOTH;
                            switchTurnState = false;
                            canSwitchTurnState = false;
                        }
                        else if(gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER){
                            switchTurnState = false;
                            canSwitchTurnState = true;
                        }
                        else if(gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchControlState)
                            switchTurnState = true;
                        break;
                    case SMOOTH:
                        if(switchTurnState){
                            drivetrain.turnState = TankDT.FCTurnState.FAST;
                            switchTurnState = false;
                            canSwitchTurnState = false;
                        }
                        else if(gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER){
                            switchTurnState = false;
                            canSwitchTurnState = true;
                        }
                        else if(gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchControlState)
                            switchTurnState = true;
                        break;
                }

                if(switchControlState){
                    drivetrain.controlState = TankDT.ControlState.ARCADE;
                    switchControlState = false;
                    canSwitchControlState = false;
                }
                else if(gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER){
                    switchControlState = false;
                    canSwitchControlState = true;
                }
                else if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchControlState)
                    switchControlState = true;
                break;
        }
        telemetry.addData("control State", drivetrain.controlState);
        telemetry.addData("fcTurnState", drivetrain.turnState);
        telemetry.addData("leftvect1", leftStick1);
        telemetry.addData("leftPower", drivetrain.leftPow);
        telemetry.addData("rightPower", drivetrain.rightPow);
        telemetry.addData("angle", Math.toDegrees(robotAngle.angle()));
        telemetry.addData("direction", drivetrain.direction);
        telemetry.addData("turn", drivetrain.turn);
        telemetry.addData("sin", Math.sin(drivetrain.angleBetween));
        telemetry.addData("angleBetween", drivetrain.angleBetween);
        telemetry.addData("angleBetween", Math.toDegrees(leftStick1.angleBetween(robotAngle)));
    }
    public void refreshStartAngle(){
        if(gamepad1.left_stick_button){
            startAngle = Math.toDegrees(leftStick1.angleBetween(robotAngle));
            leftStick1.x = 0;
            leftStick1.y = 0;
        }
    }
}