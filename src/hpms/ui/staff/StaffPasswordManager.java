package hpms.ui.staff;

import hpms.auth.AuthService;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Password management utility with masked display and change functionality
 */
public class StaffPasswordManager extends JPanel {
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox showPasswordCheckbox;
    private JLabel passwordStatusLabel;
    private JLabel currentPasswordValueLabel;
    private JButton revealButton;
    private String currentPlaintext;
    private boolean showCurrent;
    private String staffId;
    private Runnable onPasswordChanged;

    public StaffPasswordManager(String staffId, Runnable onPasswordChanged) {
        this.staffId = staffId;
        this.onPasswordChanged = onPasswordChanged;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 230, 245), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Section Title
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Change Password");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(30, 70, 140));
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        
        currentPlaintext = AuthService.getLastPlaintextForUI(staffId);
        showCurrent = currentPlaintext != null && !currentPlaintext.isEmpty();
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel currentPanel = new JPanel(new BorderLayout());
        currentPanel.setOpaque(true);
        currentPanel.setBackground(new Color(240, 246, 255));
        currentPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 200, 220), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        JLabel curPrefix = new JLabel("Current Password:");
        curPrefix.setFont(new Font("Arial", Font.BOLD, 12));
        curPrefix.setForeground(new Color(60, 80, 120));
        currentPasswordValueLabel = new JLabel(mask(currentPlaintext));
        currentPasswordValueLabel.setFont(new Font("Courier New", Font.BOLD, 14));
        currentPasswordValueLabel.setForeground(new Color(220, 100, 20));
        left.add(curPrefix);
        left.add(currentPasswordValueLabel);

        revealButton = new JButton(showCurrent ? "Hide" : "Show");
        revealButton.setBackground(new Color(220, 100, 20));
        revealButton.setForeground(Color.WHITE);
        revealButton.setFocusPainted(false);
        revealButton.setFont(new Font("Arial", Font.BOLD, 11));
        revealButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        revealButton.addActionListener(e -> {
            if (currentPlaintext == null || currentPlaintext.isEmpty()) {
                int r = JOptionPane.showConfirmDialog(this,
                        "Current password is not available. Generate a new password now?",
                        "Generate Password",
                        JOptionPane.OK_CANCEL_OPTION);
                if (r == JOptionPane.OK_OPTION) {
                    String pwd = AuthService.resetPassword(staffId);
                    if (pwd != null && !pwd.isEmpty()) {
                        currentPlaintext = pwd;
                        showCurrent = true;
                        revealButton.setEnabled(true);
                        updateCurrentPasswordDisplay();
                        passwordStatusLabel.setText("✓ New password generated");
                        passwordStatusLabel.setForeground(new Color(0, 140, 60));
                    } else {
                        passwordStatusLabel.setText("✗ Failed to generate password");
                        passwordStatusLabel.setForeground(new Color(200, 0, 0));
                    }
                }
                return;
            }
            showCurrent = !showCurrent;
            updateCurrentPasswordDisplay();
        });
        if (currentPlaintext == null || currentPlaintext.isEmpty()) {
            revealButton.setEnabled(true);
        }

        currentPanel.add(left, BorderLayout.CENTER);
        currentPanel.add(revealButton, BorderLayout.EAST);
        add(currentPanel, gbc);
        gbc.gridwidth = 1;

        // Removed verification input — only New and Confirm are required

        // New Password
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel newLabel = new JLabel("New Password *");
        newLabel.setFont(new Font("Arial", Font.BOLD, 11));
        newLabel.setForeground(new Color(60, 80, 120));
        add(newLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        newPasswordField = new JPasswordField(20);
        newPasswordField.setBackground(Color.WHITE);
        newPasswordField.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230)));
        newPasswordField.setPreferredSize(new Dimension(200, 28));
        add(newPasswordField, gbc);

        // Confirm Password
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel confirmLabel = new JLabel("Confirm Password *");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 11));
        confirmLabel.setForeground(new Color(60, 80, 120));
        add(confirmLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setBackground(Color.WHITE);
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 230)));
        confirmPasswordField.setPreferredSize(new Dimension(200, 28));
        add(confirmPasswordField, gbc);

        // Show Password Checkbox
        gbc.gridy++;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setOpaque(false);
        showPasswordCheckbox.setFont(new Font("Arial", Font.PLAIN, 11));
        showPasswordCheckbox.addActionListener(e -> togglePasswordVisibility());
        add(showPasswordCheckbox, gbc);
        gbc.gridwidth = 1;

        // Status Label
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        passwordStatusLabel = new JLabel(" ");
        passwordStatusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        passwordStatusLabel.setForeground(new Color(100, 100, 100));
        add(passwordStatusLabel, gbc);
        gbc.gridwidth = 1;

        // Buttons
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttonPanel.setOpaque(false);

        JButton changeButton = new JButton("Change Password");
        changeButton.setFont(new Font("Arial", Font.BOLD, 11));
        changeButton.setBackground(new Color(47, 111, 237));
        changeButton.setForeground(Color.WHITE);
        changeButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        changeButton.setFocusPainted(false);
        changeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changeButton.addActionListener(e -> changePassword());

        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.BOLD, 11));
        clearButton.setBackground(new Color(200, 200, 210));
        clearButton.setForeground(new Color(40, 40, 40));
        clearButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.addActionListener(e -> clearFields());

        buttonPanel.add(changeButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, gbc);
        
        
    }

    private void togglePasswordVisibility() {
        char echoChar = showPasswordCheckbox.isSelected() ? (char) 0 : '*';
        newPasswordField.setEchoChar(echoChar);
        confirmPasswordField.setEchoChar(echoChar);
    }

    

    private void changePassword() {
        String newPwd = new String(newPasswordField.getPassword());
        String confirmPwd = new String(confirmPasswordField.getPassword());

        // Validation
        if (newPwd.isEmpty() || confirmPwd.isEmpty()) {
            passwordStatusLabel.setText("✗ All fields are required");
            passwordStatusLabel.setForeground(new Color(200, 0, 0));
            return;
        }

        if (!newPwd.equals(confirmPwd)) {
            passwordStatusLabel.setText("✗ New passwords do not match");
            passwordStatusLabel.setForeground(new Color(200, 0, 0));
            return;
        }

        if (newPwd.length() < 6) {
            passwordStatusLabel.setText("✗ Password must be at least 6 characters");
            passwordStatusLabel.setForeground(new Color(200, 0, 0));
            return;
        }

        if (!newPwd.matches(".*[0-9].*")) {
            passwordStatusLabel.setText("✗ Password must contain at least one number");
            passwordStatusLabel.setForeground(new Color(200, 0, 0));
            return;
        }

        // Change password without requiring current password
        java.util.List<String> changeResult = AuthService.changePasswordNoOld(staffId, newPwd);
        if (changeResult != null && !changeResult.isEmpty()) {
            if (changeResult.get(0).contains("success") || changeResult.get(0).contains("changed")) {
                passwordStatusLabel.setText("✓ Password changed successfully");
                passwordStatusLabel.setForeground(new Color(0, 140, 60));
                clearFields();
                
                // Log the change
                logPasswordChange(staffId, "Password changed successfully");
                
                if (onPasswordChanged != null) {
                    onPasswordChanged.run();
                }
                currentPlaintext = newPwd;
                showCurrent = true;
                revealButton.setEnabled(true);
                updateCurrentPasswordDisplay();
            } else {
                passwordStatusLabel.setText("✗ Error: " + changeResult.get(0));
                passwordStatusLabel.setForeground(new Color(200, 0, 0));
            }
        } else {
            passwordStatusLabel.setText("✗ Failed to change password");
            passwordStatusLabel.setForeground(new Color(200, 0, 0));
        }
    }

    private void clearFields() {
        newPasswordField.setText("");
        confirmPasswordField.setText("");
        showPasswordCheckbox.setSelected(false);
        togglePasswordVisibility();
    }

    private void logPasswordChange(String staffId, String message) {
        // Log to system or DataStore as needed
    }

    /**
     * Reset fields and status
     */
    public void reset() {
        clearFields();
        passwordStatusLabel.setText(" ");
        
    }

    private String mask(String s) {
        if (s == null || s.isEmpty()) return "(not available)";
        return "••••••••";
    }

    private void updateCurrentPasswordDisplay() {
        if (currentPasswordValueLabel == null) return;
        currentPasswordValueLabel.setText(showCurrent ? (currentPlaintext == null || currentPlaintext.isEmpty() ? "(not available)" : currentPlaintext) : mask(currentPlaintext));
        if (revealButton != null) revealButton.setText(showCurrent ? "Hide" : "Show");
    }
}
