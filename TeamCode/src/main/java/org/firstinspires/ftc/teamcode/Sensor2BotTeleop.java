package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Conjured into existence by The Saminator on 12-05-2017.
 */

@TeleOp(name = "Sensor TeleOp", group = "feelz2thesequel")
public class Sensor2BotTeleop extends Sensor2BotTemplate {
    @Override
    public void start() {
    }

    @Override
    public void loop() {
        lm.setPower(gamepad1.left_stick_y * Constants.MOTOR_POWER);
        rm.setPower(gamepad1.right_stick_y * Constants.MOTOR_POWER);

        wp1.setPower(gamepad2.right_stick_y * 0.75);
        wp2.setPower(gamepad2.right_stick_y * 0.75);

        vm1.setPower(gamepad2.left_stick_y);
        vm2.setPower(gamepad2.left_stick_y);
    }
}