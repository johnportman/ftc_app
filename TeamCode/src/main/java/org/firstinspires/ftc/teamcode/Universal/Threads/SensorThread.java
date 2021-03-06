package org.firstinspires.ftc.teamcode.Universal.Threads;
/*
* Class: Sensor Thread
*
* Description: Contains a function for getting a value of type 'T' returned by a sensor.
*
* Author: Mister Minister Master
*/

import org.firstinspires.ftc.robotcore.external.Func;


public class SensorThread<T> implements Runnable {
    private boolean running           ;
    private T value                   ;
    private Func<T> sensFunc;


    public void run() {
        while (running) {
            value = sensFunc.value();
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

    public SensorThread(Func<T> f) {
        running = true;

        this.sensFunc = f;
    }
}