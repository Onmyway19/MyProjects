package com.studentms;

import com.studentms.ui.StudentManagementUI;

import javax.swing.*;

/**
 * Entry point for the Student Management System application.
 */
public class Main {
    public static void main(String[] args) {
        // Use system look and feel for a polished appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fall back to default look and feel
        }

        // Override specific UI defaults for dark theme consistency
        UIManager.put("OptionPane.background", new java.awt.Color(40, 44, 55));
        UIManager.put("Panel.background", new java.awt.Color(40, 44, 55));
        UIManager.put("OptionPane.messageForeground", new java.awt.Color(230, 235, 245));

        SwingUtilities.invokeLater(() -> {
            StudentManagementUI ui = new StudentManagementUI();
            ui.setVisible(true);
        });
    }
}
