package org.firstinspires.ftc.teamcode;

//------------------------------------------------------------------------------
//
// PootisBotManual
//

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Locale;

/**
 * I made this from scratch.
 *
 * @author Pootis Man
 */
@Autonomous(name = "SensorBot: Balance", group = "feelz")
@Disabled
public class SensorBotBalance extends SensorBotTemplate {

    Acceleration gravity;

    //--------------------------------------------------------------------------
    //
    //
    //
    //--------
    // Constructs the class.
    //
    // The system calls this member when the class is instantiated.
    //--------
    public SensorBotBalance() {
        //
        // Initialize base classes.
        //rna
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    }

    @Override
    public void init() {
        super.init();
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hardwareMap.getAll(BNO055IMU.class).get(0);
        imu.initialize(parameters);
    }

    //--------------------------------------------------------------------------
    //
    // loop
    //
    //-------
    // Initializes the class.
    //
    // The system calls this member repeatedly while the OpMode is running.
    //--------
    @Override public void loop () {
        gravity = imu.getGravity();
        Spherical3D angles = cartesianToSpherical(new Cartesian3D(gravity.xAccel, gravity.yAccel, gravity.zAccel));

        telemetry.addData("(NAV) Status", imu.getSystemStatus().toShortString());
        telemetry.addData("(NAV) Calib.", imu.getCalibrationStatus());

        if (angles.theta <= 160 && angles.theta >= 0) {
            telemetry.addData("Status", "Balancing...");
            if (angles.phi >= 20) {
                left.setPower(0.6);
                right.setPower(0.0);
            }
            else if (angles.phi <= -20) {
                left.setPower(0.0);
                right.setPower(0.6);
            }
            else {
                left.setPower(0.3);
                right.setPower(0.3);
            }
        }
        else if (angles.theta >= -160) {
            telemetry.addData("Status", "Balancing...");
            if (angles.phi >= 20) {
                left.setPower(-0.6);
                right.setPower(0.0);
            }
            else if (angles.phi <= -20) {
                left.setPower(0.0);
                right.setPower(-0.6);
            }
            else {
                left.setPower(-0.3);
                right.setPower(-0.3);
            }
        }
        else
            telemetry.addData("Status", "Balanced! :D");
    }

    public Spherical3D cartesianToSpherical(Cartesian3D cartesian) {
        double
                x2 = cartesian.x * cartesian.x,
                y2 = cartesian.y * cartesian.y,
                z2 = cartesian.z * cartesian.z;
        double r = Math.sqrt(x2 + y2 + z2);
        double theta = Math.acos(cartesian.z / r) * Constants.RADS_TO_DEGS;
        double phi = Math.atan2(cartesian.y, cartesian.x) * Constants.RADS_TO_DEGS;
        return new Spherical3D(r, theta, phi);
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

} // PootisBotManual