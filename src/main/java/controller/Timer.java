package controller;

import view.GUI;

public class Timer implements Runnable {

    private final Thread clockThread;
    private int time;
    private boolean isRunning;
    private final GUI gui;

    public Timer(GUI gui){
        this.clockThread = new Thread(this);
        this.isRunning = false;
        this.time = 0;
        this.gui = gui;
    }

    public void startTime() {
        isRunning = true;
        time = 0;
        clockThread.start();
    }

    public void stopTime() {
        isRunning = false;
    }

    public int getTime() {
        return this.time;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                int currentTime = this.getTime();
                gui.updateCurrentTime(""+currentTime);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            time++;
        }
    }
}
