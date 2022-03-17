package model;

import controller.Timer;
import view.GUI;

import java.util.ArrayList;

public class Queue implements Runnable {

    private final ArrayList<Client> clients;
    private final int queueIndex;
    private boolean isRunning;
    private int clientsServed;
    private int totalWaitingTime;
    private int totalServiceTime;
    private final Timer timer;
    private final Thread queueThread;
    private final GUI gui;
    private int clientsUnserved;

    public Queue(int queueIndex, Timer timer, GUI gui){
        this.clients = new ArrayList<>();
        this.queueIndex = queueIndex;
        this.isRunning = false;
        this.clientsServed = 0;
        this.clientsUnserved = 0;
        this.totalWaitingTime = 0;
        this.totalServiceTime = 0;
        this.timer = timer;
        this.queueThread = new Thread(this);
        this.gui = gui;

        start();
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public int getNumberOfClients() {
        return this.clients.size();
    }

    public void addClientToQueue(Client myClient) { this.clients.add(myClient); }

    public void removeClientFromQueue(Client myClient) {
        this.clients.remove(myClient);
    }

    public int getTotalClients() {
        return clientsServed;
    }

    public int getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public int getTotalServiceTime() { return totalServiceTime; }

    @Override
    public void run() {
        while (isRunning || clients.size() > 0) {
            if (!clients.isEmpty()) {
                Client currentClient = this.clients.get(0);
                int serviceTime = currentClient.getServiceTime();

                if (clientsUnserved == clientsServed) {
                    totalWaitingTime = totalWaitingTime + timer.getTime() - currentClient.getArrivalTime();
                    clientsUnserved++;
                }

                currentClient.setServiceTime(serviceTime-1);

                if(currentClient.getServiceTime()==0) {
                    clientsServed++;
                    totalServiceTime = totalServiceTime + timer.getTime() - currentClient.getArrivalTime();
                    removeClientFromQueue(currentClient);
                }

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start() {
        this.isRunning = true;
        queueThread.start();
    }

    public void stop() {
        this.isRunning = false;
    }
}
