package org.firstinspires.ftc.teamcode.robotUniversal;

/*
* Class: Sensor Thread
*
* Description: Contains a function for getting a value of type 'T' returned by a sensor.
*
* Author: Mister Minister Master
*/

public class SensorThread<T> implements Runnable {
    private boolean running;
    private T value;
    private SensorFunction<T> sensFunc;


    public void run() {
        while (running) {
            value = sensFunc.sensFunc();
        }
    }

    boolean getRunning() {
        return this.running;
    }

    void setRunning(boolean r) {
        this.running = r;
    }

    T getValue() {
        return this.value;
    }


    public SensorThread(SensorFunction<T> f) {
        running = true;

        this.sensFunc = f;
    }

    public interface SensorFunction<T> {
        T sensFunc();
    }
}