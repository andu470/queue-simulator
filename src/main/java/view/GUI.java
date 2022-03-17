package view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class GUI extends JFrame {

    private final JLabel currentTimeLabel;
    private final JTextArea currentTimeField;

    private final JLabel clientsLabel;
    private final JTextField clientsField;

    private final JLabel arrivalLabel;
    private final JLabel arrivalLabelMin;
    private final JLabel arrivalLabelMax;
    private final JTextField arrivalFieldMin;
    private final JTextField arrivalFieldMax;

    private final JLabel serviceLabel;
    private final JLabel serviceLabelMin;
    private final JLabel serviceLabelMax;
    private final JTextField serviceMinField;
    private final JTextField serviceMaxField;

    private final JLabel queuesLabel;
    private final JTextField queuesField;

    private final JLabel simulation;
    private final JTextField simulationField;

    private final JButton startButton;

    private final JTextArea logsField;
    private final JLabel logsLabel;
    private final JScrollPane logScrollPane;

    public GUI(){
        this.setTitle("Queue Simulator");
        this.setBounds(50, 50, 900, 600);
        this.getContentPane().setLayout(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        clientsLabel = new JLabel("Number of clients: ");
        clientsLabel.setBounds(50,20,200,30);
        getContentPane().add(clientsLabel);

        clientsField = new JTextField();
        clientsField.setBounds(250, 20,50,30);
        getContentPane().add(clientsField);

        arrivalLabel = new JLabel("Arrival time: ");
        arrivalLabel.setBounds(50, 50, 300, 30);
        getContentPane().add(arrivalLabel);

        arrivalLabelMin = new JLabel("min: ");
        arrivalLabelMin.setBounds(200, 50, 50, 30);
        getContentPane().add(arrivalLabelMin);

        arrivalFieldMin = new JTextField();
        arrivalFieldMin.setBounds(250, 50, 50, 30);
        getContentPane().add(arrivalFieldMin);

        arrivalLabelMax = new JLabel("max: ");
        arrivalLabelMax.setBounds(350, 50, 50, 30);
        getContentPane().add(arrivalLabelMax);

        arrivalFieldMax = new JTextField();
        arrivalFieldMax.setBounds(400, 50, 50, 30);
        getContentPane().add(arrivalFieldMax);

        serviceLabel = new JLabel("Service time:");
        serviceLabel.setBounds(50, 80, 150, 30);
        getContentPane().add(serviceLabel);

        serviceLabelMin = new JLabel("min: ");
        serviceLabelMin.setBounds(200, 80, 50, 30);
        getContentPane().add(serviceLabelMin);

        serviceMinField = new JTextField();
        serviceMinField.setBounds(250, 80, 50, 30);
        getContentPane().add(serviceMinField);

        serviceLabelMax = new JLabel("max: ");
        serviceLabelMax.setBounds(350, 80, 50, 30);
        getContentPane().add(serviceLabelMax);

        serviceMaxField = new JTextField();
        serviceMaxField.setBounds(400, 80, 50, 30);
        getContentPane().add(serviceMaxField);

        queuesLabel = new JLabel("Number of queues: ");
        queuesLabel.setBounds(50, 110, 300, 30);
        getContentPane().add(queuesLabel);

        queuesField = new JTextField();
        queuesField.setBounds(250, 110, 50, 30);
        getContentPane().add(queuesField);

        simulation = new JLabel("Simulation interval: ");
        simulation.setBounds(50, 140, 200, 30);
        getContentPane().add(simulation);

        simulationField = new JTextField();
        simulationField.setBounds(250, 140, 50, 30);
        getContentPane().add(simulationField);

        startButton = new JButton("Start Simulation");
        startButton.setBounds(50, 200, 150, 50);
        getContentPane().add(startButton);

        logsLabel = new JLabel("Logs: ");
        logsLabel.setBounds(480, 10, 100, 30);
        getContentPane().add(logsLabel);

        logScrollPane = new JScrollPane();
        logScrollPane.setBounds(480, 40, 300, 300);
        logsField = new JTextArea();
        logsField.setEditable(false);
        logsField.setText("");
        logScrollPane.setViewportView(logsField);
        getContentPane().add(logScrollPane);

        currentTimeLabel = new JLabel("Current time:");
        currentTimeLabel.setBounds(250,170,200,30);
        getContentPane().add(currentTimeLabel);

        currentTimeField = new JTextArea();
        currentTimeField.setEditable(false);
        currentTimeField.setBounds(250,200,150,50);
        getContentPane().add(currentTimeField);

        this.setVisible(true);
    }

    public void updateCurrentTime(String string) {
        currentTimeField.setText(string);
    }

    public void updateLogs(String string) {
        try{
            FileWriter myWriter = new FileWriter("logs.txt",true);
            myWriter.write(string+"\n");
            myWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        logsField.append(string + "\n");
        JScrollBar myScrollBar=logScrollPane.getVerticalScrollBar();
        myScrollBar.setValue(myScrollBar.getMaximum());
    }

    public void updateLogsNoSpace(String string) {
        try{
            FileWriter myWriter = new FileWriter("logs.txt",true);
            myWriter.write(string);
            myWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        logsField.append(string+"");
        JScrollBar myScrollBar=logScrollPane.getVerticalScrollBar();
        myScrollBar.setValue(myScrollBar.getMaximum());
    }

    public String getClients(){
        return clientsField.getText();
    }

    public String getArrivalTimeMin() {
        return arrivalFieldMin.getText();
    }

    public String getArrivalTimeMax() {
        return arrivalFieldMax.getText();
    }

    public String getServiceTimeMin() {
        return serviceMinField.getText();
    }

    public String getServiceTimeMax() {
        return serviceMaxField.getText();
    }

    public String getNumberOfQueues() {
        return queuesField.getText();
    }

    public String getSimulationTime() {
        return simulationField.getText();
    }

    public void addStartButtonActionListener(final ActionListener actionListener) {
        startButton.addActionListener(actionListener);
    }

}
