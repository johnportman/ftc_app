package org.firstinspires.ftc.teamcode.Components.Sensors.Cameras;

import org.firstinspires.ftc.teamcode.Components.Sensors.DigitalCamera;
import org.opencv.core.Point3;

import java.lang.Math;
public class MotoG4{
    //TODO: Figure out of difference ratio of surface view height and width and the image sensor height and width needs to be accounted for
    //image sensor ratio: approx 1.348717949, surface view ratio: approx 1.333333333
    public static RearCamera rear;
    public static FrontCamera front;
    public MotoG4(){
        rear = new RearCamera();
        front = new FrontCamera(3.584 / 2 / Math.tan(14 * Math.PI / 15));
    }
    public void setLocationAndOrientation(Point3 location, Point3 orientation){
        rear.setLocationAndOrientation(location, orientation);
        front.setLocation(rear);
        front.updateOrientation(Math.PI, Math.PI, Math.PI);
    }
    public void setLocation(Point3 location){
        front.setLocation(location);
        rear.setLocation(location);
    }
    public Point3 getLocation(){
        return new Point3(rear.x, rear.y, rear.z);
    }
    /*public void setOrientation(Point3 orientation){
        rear.xAng
    }*/
}

class RearCamera extends DigitalCamera{
    static final double FOCAL_LENGTH = 3.6,
            PIXEL_SIZE = 1.12 * Math.pow(10, -3),
            NUM_PIXELS_WIDTH = 4208,
            NUM_PIXELS_HEIGHT = 3120;

    RearCamera() {
        super(FOCAL_LENGTH, PIXEL_SIZE, NUM_PIXELS_WIDTH, NUM_PIXELS_HEIGHT);
    }

}
class FrontCamera extends DigitalCamera{
    static final double FOCAL_LENGTH = 0,
            PIXEL_SIZE = 1.4 * Math.pow(10, -3),
            NUM_PIXELS_WIDTH = 2560,
            NUM_PIXELS_HEIGHT = 1920;/*
        public static final double FOCAL_LENGTH = 3.584 / 2 / Math.tan(14 * Math.PI / 15),
                PIXEL_SIZE = 1.4 * Math.pow(10, -3),
                NUM_PIXELS_WIDTH = 2560,
                NUM_PIXELS_HEIGHT = 1920;
*/
    FrontCamera(double focalLength) {
        super(focalLength, PIXEL_SIZE, NUM_PIXELS_WIDTH, NUM_PIXELS_HEIGHT);
    }
}