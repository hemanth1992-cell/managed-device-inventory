package com.example.manageddeviceinventory.desktop;

import javax.swing.*;
import java.awt.*;

public class DesktopCompanion {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Managed Device Inventory");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(420, 180);
            frame.setLayout(new BorderLayout());

            JLabel statusLabel = new JLabel("Status: Running", SwingConstants.CENTER);
            statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
            frame.add(statusLabel, BorderLayout.CENTER);

            JPanel buttons = new JPanel(new FlowLayout());
            JButton pause = new JButton("Pause 60 min");
            JButton resume = new JButton("Resume");
            buttons.add(pause);
            buttons.add(resume);
            frame.add(buttons, BorderLayout.SOUTH);

            pause.addActionListener(e -> statusLabel.setText("Status: Paused until +60 min"));
            resume.addActionListener(e -> statusLabel.setText("Status: Running"));

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
