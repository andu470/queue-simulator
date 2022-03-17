package controller;

import model.Client;
import model.Queue;
import view.GUI;

import javax.swing.*;
import java.util.*;

public class Controller implements Runnable{

    private GUI gui;
    private int nbClients;
    private int nbQueues;
    private int arrivalMin;
    private int arrivalMax;
    private int serviceMin;
    private int serviceMax;
    private int simulationDuration;

    private Timer timer;
    private Thread controllerThread;
    private Queue[] myQueues;

    private final ArrayList<Client> waitingClients = new ArrayList<>();

    private int maxClientsAtTime = 0;
    private int peakHour = 0;

    public void start(){
        gui = new GUI();
        startSimulation();
        timer = new Timer(gui);
        controllerThread = new Thread(this);
    }

    public boolean checkInput() {
        String numberOfClients = gui.getClients();
        String numberOfQueues = gui.getNumberOfQueues();
        String arrivalMin = gui.getArrivalTimeMin();
        String arrivalMax = gui.getArrivalTimeMax();
        String serviceMin = gui.getServiceTimeMin();
        String serviceMax = gui.getServiceTimeMax();
        String simulationTime = gui.getSimulationTime();

        int nbClients, arrMin, arrMax, serMin, serMax, nbQueues, simTime;
        try {
            nbClients = Integer.parseInt(numberOfClients);
            arrMin = Integer.parseInt(arrivalMin);
            arrMax = Integer.parseInt(arrivalMax);
            serMin = Integer.parseInt(serviceMin);
            serMax = Integer.parseInt(serviceMax);
            nbQueues = Integer.parseInt(numberOfQueues);
            simTime = Integer.parseInt(simulationTime);

            if (arrMin > 0 && serMin > 0 && nbQueues > 0 && simTime > 0 &&
                    nbClients > 0 && arrMin < arrMax && serMin < serMax) {
                setData(nbClients, arrMin, arrMax, serMin, serMax, nbQueues, simTime);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid input data!");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid input data!");
            return false;
        }
    }

    public void setData(int nbClients, int arrMin, int arrMax, int serMin, int serMax,
                        int nbQueues, int simTime) {
        this.nbClients = nbClients;
        this.arrivalMin = arrMin;
        this.arrivalMax = arrMax;
        this.serviceMin = serMin;
        this.serviceMax = serMax;
        this.nbQueues = nbQueues;
        this.simulationDuration = simTime;
    }

    public void startSimulation() {
        gui.addStartButtonActionListener(e -> {
            if (checkInput()) {
                myQueues = new Queue[nbQueues];

                for (int i = 0; i < nbQueues; i++) {
                    myQueues[i] = new Queue(i + 1, timer, gui);
                }

                timer.startTime();
                controllerThread.start();
            }
        });
    }

    public int getShortestQueue() {
        int minIndex = 0;
        int minQ = 0;
        for (Client currentClient : myQueues[0].getClients()) {
            minQ = minQ + currentClient.getServiceTime();
        }
        for (int i = 1; i < myQueues.length; i++) {
            int minWaitingTime = 0;
            for (Client currentClient : myQueues[i].getClients()) {
                minWaitingTime = minWaitingTime + currentClient.getServiceTime();
            }
            if (minWaitingTime < minQ) {
                minQ = minWaitingTime;
                minIndex = i;
            }
        }
        return minIndex;
    }

    public int getMaxNbOfClients() {
        int nrClients = 0;
        for (Queue myQueue : myQueues) {
            nrClients = nrClients + myQueue.getNumberOfClients();
        }
        return nrClients;
    }

    public void createClients(int nbClients){
        for(int i=1;i<=nbClients;i++){
            Random randomVar = new Random();
            int randomArrTime = arrivalMin + randomVar.nextInt((arrivalMax - arrivalMin) + 1);
            int randomSerTime = serviceMin + randomVar.nextInt((serviceMax - serviceMin) + 1);
            Client newClient = new Client(i, randomArrTime, randomSerTime);
            waitingClients.add(newClient);
        }
    }

    public void printQueues(){
        gui.updateLogs("Time "+ timer.getTime());
        gui.updateLogsNoSpace("Waiting clients: ");
        for (Client currentClient : waitingClients) {
            gui.updateLogsNoSpace("(" + currentClient.getId() + "," + currentClient.getArrivalTime() +
                    "," + currentClient.getServiceTime() + ")" + ";");
        }
        gui.updateLogs("");
        for(int i=0;i<nbQueues;i++){
            gui.updateLogsNoSpace("Queue "+(i+1)+":");
            if(myQueues[i].getClients().isEmpty()){
                gui.updateLogs("closed");
            }
            else {
                for (Client currentClient : myQueues[i].getClients()) {
                    gui.updateLogsNoSpace("(" + currentClient.getId() + "," + currentClient.getArrivalTime() +
                            "," + currentClient.getServiceTime() + ")" + ";");
                }
                gui.updateLogs("");
            }
        }
    }

    public void sortClients() {
        Collections.sort(waitingClients, new Comparator<Client>() {
            @Override
            public int compare(Client client1, Client client2) {
                return client1.getArrivalTime() <  client2.getArrivalTime() ? -1 :
                        client1.getArrivalTime() == client2.getArrivalTime() ? 0 : 1;
            }
        });
    }

    @Override
    public void run() {
        createClients(nbClients);
        sortClients();
        boolean closedQueues = false;
        while (timer.getTime() < simulationDuration && !(waitingClients.isEmpty() && closedQueues)) {
            printQueues();

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            while(!waitingClients.isEmpty()&&waitingClients.get(0).getArrivalTime()<=timer.getTime()) {
                int minQIndex = getShortestQueue();
                myQueues[minQIndex].addClientToQueue(waitingClients.get(0));
                waitingClients.remove(0);
            }

            int max = getMaxNbOfClients();
            if (maxClientsAtTime < max) {
                this.maxClientsAtTime = max;
                this.peakHour = timer.getTime();
            }

            int nbClosedQueues = 0;
            for(int i=0;i<nbQueues;i++){
                if(myQueues[i].getClients().isEmpty()){
                    nbClosedQueues++;
                }
            }

            if(nbClosedQueues==nbQueues){
                closedQueues = true;
            }
            else{
                closedQueues = false;
            }

        }

        printQueues();
        int clientsServed = 0;
        int waitingTime = 0;
        int serviceTime = 0;
        for (Queue myQueue : myQueues) {
            clientsServed = clientsServed + myQueue.getTotalClients();
            waitingTime = waitingTime + myQueue.getTotalWaitingTime();
            serviceTime = serviceTime + myQueue.getTotalServiceTime();
            myQueue.stop();
        }

        gui.updateLogs("Average waiting time: " + (float) (waitingTime) / clientsServed);
        gui.updateLogs("Average service time: "+ (float) (serviceTime) / clientsServed);
        gui.updateLogs("Peak hour: " + peakHour + " with " + maxClientsAtTime + " clients");
        gui.updateLogs("--------------------------------------------------------------------");
        timer.stopTime();
    }
}
