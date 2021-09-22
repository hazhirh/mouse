package com.hazhir.mouse;

import javax.swing.*;

public class MainClass {

    static String VERSION = "1.2";

    public static void main(String[] args) throws Exception {
        JFrame mainFrame = new JFrame("No Sleep v " + VERSION);
        mainFrame.setContentPane(new MouseForm().rootPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setSize(500, 350);
        mainFrame.setResizable(false);
    }
}
