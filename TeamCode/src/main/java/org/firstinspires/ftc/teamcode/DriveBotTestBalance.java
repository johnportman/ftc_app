package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;

/**
 * Conjured into existence by The Saminator on 03-06-2018.
 */
@Autonomous(name = "DriveBot Balance", group = "this is a test")
public class DriveBotTestBalance extends DriveBotTestTemplate {

    static class Angles {
        //public static final double Y_TOLERANCE = 0.375; // Actually the tolerance for sin(theta).
        public static final double PHI_BASELINE = 90;
        public static final double PHI_TOLERANCE = 3.75;
        public static final double THETA_BASELINE = 90;
        public static final double THETA_TOLERANCE = 3.75;
    }

    Acceleration gravity;
    Spherical3D angles;

    //--------------------------------------------------------------------------
    //
    // loop
    //
    //-------
    // Initializes the class.
    //
    // The system calls this member repeatedly while the OpMode is running.
    //--------
    @Override
    public void loop() {
        gravity = imu.getGravity();
        angles = cartesianToSpherical(new Cartesian3D(gravity.zAccel, gravity.xAccel, gravity.yAccel));

        telemetry.addData("(NAV) Status", imu.getSystemStatus().toShortString());
        telemetry.addData("(NAV) Calib.", imu.getCalibrationStatus());

        telemetry.addData("Theta angle ( acos (z / sqrt(x^2 + y^2 + z^2)) )", angles.theta);
        telemetry.addData("Phi angle ( atan (y / x) )", angles.phi);
        telemetry.addData("X gravity", gravity.xAccel);
        telemetry.addData("Y gravity", gravity.yAccel);
        telemetry.addData("Z gravity", gravity.zAccel);

        if (angles.theta > Angles.THETA_TOLERANCE) {
            double leftPow = -0.125;
            double rightPow = -0.125;
            // Account for fore/back tilt
            double sign = Math.cos(org.firstinspires.ftc.teamcode.Constants.DEGS_TO_RADS * (angles.phi - Angles.PHI_BASELINE));
            sign /= Math.abs(sign);
            double foreBack = sign * Math.sin(org.firstinspires.ftc.teamcode.Constants.DEGS_TO_RADS * (angles.theta - Angles.THETA_BASELINE)); // * Math.cos(org.firstinspires.ftc.teamcode.Constants.DEGS_TO_RADS * (angles.phi - Angles.PHI_BASELINE));
            foreBack += Math.abs(foreBack) / foreBack;
            leftPow *= foreBack;
            rightPow *= foreBack;
            /*// Account for left/right tilt
            double sinPhi = Math.sin(org.firstinspires.ftc.teamcode.Constants.DEGS_TO_RADS * (angles.phi - Angles.PHI_BASELINE));
            if (Math.abs(sinPhi) >= Angles.Y_TOLERANCE) {
                if (sinPhi > 0)
                    leftPow *= -1;
                else
                    rightPow *= -1;
            }*/
            // Set powers
            setLeftPow(leftPow);
            setRightPow(rightPow);
        }
        else {
            setLeftPow(0.0);
            setRightPow(0.0);
        }
    }

    public Spherical3D cartesianToSpherical(Cartesian3D cartesian) {
        double
                x2 = cartesian.x * cartesian.x,
                y2 = cartesian.y * cartesian.y,
                z2 = cartesian.z * cartesian.z;
        double r = Math.sqrt(x2 + y2 + z2);
        double theta = Math.acos(cartesian.z / r) * org.firstinspires.ftc.teamcode.Constants.RADS_TO_DEGS;
        double phi = Math.atan2(cartesian.y, cartesian.x) * org.firstinspires.ftc.teamcode.Constants.RADS_TO_DEGS;
        return new Spherical3D(r, theta, phi);
    }
}
