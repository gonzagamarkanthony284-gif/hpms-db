package hpms.ui.doctor;

import hpms.auth.AuthSession;
import hpms.auth.AuthService;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Change Password Panel - for all users to change their password
 */
public class ChangePasswordPanel extends JPanel {
    private AuthSession session;
    private JPasswordField newField;
    private JPasswordField confirmField;

    public ChangePasswordPanel(AuthSession session) {
        this.session = session;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setBackground(Color.WHITE);

        // Header
        JLabel title = new JLabel("Change Password");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(31, 41, 55));
        add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // New Password
        gbc.gridy = 0; gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel newLabel = new JLabel("New Password:");
        newLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(newLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        newField = new JPasswordField(20);
        newField.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(newField, gbc);

        // Confirm Password
        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(confirmLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        confirmField = new JPasswordField(20);
        confirmField.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(confirmField, gbc);

        // Password requirements
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        JLabel reqLabel = new JLabel("Password must be at least 6 characters long");
        reqLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        reqLabel.setForeground(new Color(107, 114, 128));
        formPanel.add(reqLabel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttonPanel.setBackground(new Color(248, 249, 250));
        buttonPanel.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));

        JButton saveBtn = new JButton("Change Password");
        saveBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        saveBtn.setBackground(new Color(47, 111, 237));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        saveBtn.addActionListener(e -> changePassword());

        JButton resetBtn = new JButton("Clear");
        resetBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        resetBtn.setBackground(new Color(229, 231, 235));
        resetBtn.setForeground(new Color(31, 41, 55));
        resetBtn.setFocusPainted(false);
        resetBtn.addActionListener(e -> clearFields());

        buttonPanel.add(resetBtn);
        buttonPanel.add(saveBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void changePassword() {
        String newPwd = new String(newField.getPassword());
        String confirm = new String(confirmField.getPassword());

        // Validation
        if (newPwd.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!newPwd.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (newPwd.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Change password without requiring the current password
        java.util.List<String> result = AuthService.changePasswordNoOld(session.userId, newPwd);
        
        if (result != null && !result.isEmpty()) {
            if (result.get(0).startsWith("Error:")) {
                JOptionPane.showMessageDialog(this, result.get(0), "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }
        }
    }

    private void clearFields() {
        newField.setText("");
        confirmField.setText("");
    }

    public void refresh() {
        // No refresh needed for this panel
    }
}
