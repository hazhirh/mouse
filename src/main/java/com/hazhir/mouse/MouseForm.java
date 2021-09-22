package com.hazhir.mouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MouseForm {
    JPanel rootPanel;
    private JButton statusButton;
    private JButton exitButton;
    private JLabel statusLabel;
    private JTextArea log;
    private JScrollPane scrollPane;
    private JButton clearButton;
    private boolean status = true;
    private Point lastPoint;
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public MouseForm() {
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        statusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = !status;
                if (status) {
                    statusButton.setText("Deactivate");
                    statusLabel.setText("Status: Working...");
                    log("Scheduling a new worker...");
                    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                    runScheduler();
                } else {
                    statusButton.setText("Activate");
                    statusLabel.setText("Status: Idle");
                    log("Killing current worker, application will be idle until reactivated again manually.");
                    scheduledExecutorService.shutdownNow();
                }
            }
        });

        runScheduler();
        log("Welcome to No Sleep Version " + MainClass.VERSION);
        log("Application is activated by default and prevents your computer from going to sleep");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.setText("");
            }
        });
    }

    private void runScheduler(){
        scheduledExecutorService.scheduleWithFixedDelay(new Action(), 30, 60, TimeUnit.SECONDS);
    }

    private void log(String message) {
        log.append("[" + Calendar.getInstance().getTime() + "]: \n" + message + "\n");
        log.validate();
        scrollPane.validate();
        if (scrollPane.getVerticalScrollBar().isShowing()) {
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        }
    }

    class Action implements Runnable {
        private Robot robot;
        private Random random = new Random();

        public Action() {
            try {
                robot = new Robot();
            } catch (Exception e) {
                //JOptionPane.showMessageDialog(this, "Failed to create robot!", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }

        @Override
        public void run() {
            Point currentPoint = MouseInfo.getPointerInfo().getLocation();
            if (currentPoint.equals(lastPoint)){
                robot.mouseMove(random.nextInt(1000), random.nextInt(1000));
                lastPoint = MouseInfo.getPointerInfo().getLocation();
                log("Mouse pointer moved to: " + lastPoint.toString());
            } else {
                log("Mouse pointer move detected, i won't be interrupting...");
                lastPoint = MouseInfo.getPointerInfo().getLocation();
            }
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress("yahoo.com", 80), 3000);
                log("connected!");
            } catch (Exception e) {
                e.printStackTrace();
                log("failed to connect!");
            }
        }
    }
}
