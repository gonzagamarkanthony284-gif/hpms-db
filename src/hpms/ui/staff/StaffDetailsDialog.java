package hpms.ui.staff;

import hpms.auth.AuthService;
import hpms.model.Appointment;
import hpms.model.Staff;
import hpms.model.StaffRole;
import hpms.util.DataStore;
import hpms.ui.components.Theme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Modern staff details dialog with professional styling and contemporary design
 * Displays comprehensive staff information with account management
 * Shows current password/login code for staff accounts
 */
public class StaffDetailsDialog extends JDialog {
    private JLabel accountStatusLbl, currentPasswordLbl;

    public StaffDetailsDialog(JFrame owner, Staff staff) {
        super(owner, "Staff Profile - " + staff.name, ModalityType.APPLICATION_MODAL);
        setSize(1000, 900);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // Premium header with gradient-like effect
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 200, 220), 2),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        header.setBackground(new Color(245, 250, 255));

        // Left side - title and info
        JPanel headerLeft = new JPanel();
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));
        headerLeft.setOpaque(false);

        JLabel title = new JLabel(staff.name);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(20, 60, 100));

        JLabel idLbl = new JLabel("ID: " + staff.id + "  •  " + staff.role + "  •  " + staff.department);
        idLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        idLbl.setForeground(new Color(100, 100, 120));

        boolean hasAccount = DataStore.users.containsKey(staff.id);
        String accountText = hasAccount ? "Account: Active" : "Account: Not created";
        JLabel accountLbl = new JLabel(accountText);
        accountLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        accountLbl.setForeground(hasAccount ? new Color(0, 120, 60) : new Color(150, 0, 0));

        headerLeft.add(title);
        headerLeft.add(Box.createVerticalStrut(4));
        headerLeft.add(idLbl);
        headerLeft.add(Box.createVerticalStrut(4));
        
        JPanel accountInfo = new JPanel();
        accountInfo.setLayout(new BoxLayout(accountInfo, BoxLayout.X_AXIS));
        accountInfo.setOpaque(false);
        accountInfo.add(accountLbl);
        headerLeft.add(accountInfo);

        // Right side - account actions
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        headerRight.setOpaque(false);

        JButton changePwdBtn = new JButton("Change Password");
        changePwdBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        changePwdBtn.setBackground(new Color(70, 130, 200));
        changePwdBtn.setForeground(Color.WHITE);
        changePwdBtn.setFocusPainted(false);
        changePwdBtn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        changePwdBtn.addActionListener(e -> changePassword(staff.id, accountLbl));

        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        closeBtn.setBackground(new Color(200, 200, 210));
        closeBtn.setForeground(new Color(40, 40, 40));
        closeBtn.setFocusPainted(false);
        closeBtn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        closeBtn.addActionListener(e -> dispose());

        headerRight.add(changePwdBtn);
        headerRight.add(closeBtn);

        header.add(headerLeft, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Main body
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Details", createDetailsPanel(staff));
        tabs.addTab("Appointments", createAppointmentsPanel(staff));
        tabs.addTab("Account", createAccountPanel(staff, accountLbl));
        add(tabs, BorderLayout.CENTER);
    }

    private Component createDetailsPanel(Staff staff) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;

        int row = 0;

        // Section 1: Contact & Basic - Modern header
        JPanel contactSection = new JPanel();
        contactSection.setLayout(new BoxLayout(contactSection, BoxLayout.Y_AXIS));
        contactSection.setOpaque(false);
        contactSection.add(createSectionLabel("📞 Contact Information"));
        contactSection.add(Box.createVerticalStrut(8));
        
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(contactSection, gbc); row++;
        
        gbc.gridy = row; gbc.gridx = 0; panel.add(createFieldRow("Phone", staff.phone != null ? staff.phone : "N/A"), gbc);
        gbc.gridx = 1; panel.add(createFieldRow("Email", staff.email != null ? staff.email : "N/A"), gbc); row++;

        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(Box.createVerticalStrut(4), gbc); gbc.gridwidth = 1; row++;

        // Section 2: Professional Info - Modern header
        JPanel professionalSection = new JPanel();
        professionalSection.setLayout(new BoxLayout(professionalSection, BoxLayout.Y_AXIS));
        professionalSection.setOpaque(false);
        professionalSection.add(createSectionLabel("🎓 Professional Information"));
        professionalSection.add(Box.createVerticalStrut(8));
        
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(professionalSection, gbc); gbc.gridwidth = 1; row++;
        
        gbc.gridy = row; gbc.gridx = 0; panel.add(createFieldRow("License Number", staff.licenseNumber != null ? staff.licenseNumber : "N/A"), gbc);
        gbc.gridx = 1; panel.add(createFieldRow("Years Experience", staff.yearsExperience != null ? String.valueOf(staff.yearsExperience) : "N/A"), gbc); row++;

        if (staff.role == StaffRole.DOCTOR) {
            gbc.gridy = row; gbc.gridx = 0; panel.add(createFieldRow("Specialty", staff.specialty != null ? staff.specialty : "N/A"), gbc);
            gbc.gridx = 1; panel.add(createFieldRow("Years Practice", staff.yearsExperience != null ? String.valueOf(staff.yearsExperience) : "N/A"), gbc); row++;
        }

        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(Box.createVerticalStrut(4), gbc); gbc.gridwidth = 1; row++;

        // Section 3: Employment - Modern header
        JPanel employmentSection = new JPanel();
        employmentSection.setLayout(new BoxLayout(employmentSection, BoxLayout.Y_AXIS));
        employmentSection.setOpaque(false);
        employmentSection.add(createSectionLabel("💼 Employment Details"));
        employmentSection.add(Box.createVerticalStrut(8));
        
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(employmentSection, gbc); gbc.gridwidth = 1; row++;
        
        gbc.gridy = row; gbc.gridx = 0; panel.add(createFieldRow("Department", staff.department), gbc);
        gbc.gridx = 1; panel.add(createFieldRow("Status", staff.isAvailable ? "🟢 Available" : "🔴 Unavailable"), gbc); row++;

        String joinDate = staff.createdAt != null ? staff.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A";
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(createFieldRow("Joined Date", joinDate), gbc); gbc.gridwidth = 1;

        return new JScrollPane(panel);
    }

    private JLabel createSectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.APP_FONT_BOLD);
        lbl.setFont(lbl.getFont().deriveFont(14f));
        lbl.setForeground(Theme.PRIMARY);
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.PRIMARY));
        return lbl;
    }

    private JPanel createFieldRow(String label, String value) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(true);
        p.setBackground(new Color(248, 250, 255));
        p.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 235, 255), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        p.setPreferredSize(new Dimension(400, 45));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(Theme.APP_FONT_BOLD);
        lbl.setForeground(Theme.PRIMARY);
        lbl.setPreferredSize(new Dimension(140, 30));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        val.setForeground(new Color(40, 40, 60));

        p.add(lbl, BorderLayout.WEST);
        p.add(val, BorderLayout.CENTER);
        return p;
    }

    private JPanel createAppointmentsPanel(Staff staff) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        java.util.List<Appointment> staffAppts = new java.util.ArrayList<>();
        for (Appointment a : DataStore.appointments.values()) {
            if (staff.id.equals(a.staffId)) {
                staffAppts.add(a);
            }
        }

        if (staffAppts.isEmpty()) {
            JLabel noAppts = new JLabel("No appointments scheduled");
            noAppts.setFont(new Font("Arial", Font.PLAIN, 13));
            noAppts.setForeground(new Color(120, 120, 140));
            panel.add(noAppts, BorderLayout.CENTER);
            return panel;
        }

        staffAppts.sort((a, b) -> b.dateTime.compareTo(a.dateTime));

        JPanel apptList = new JPanel();
        apptList.setLayout(new BoxLayout(apptList, BoxLayout.Y_AXIS));
        apptList.setBackground(Color.WHITE);

        for (Appointment a : staffAppts) {
            apptList.add(createAppointmentRow(a));
            apptList.add(Box.createVerticalStrut(8));
        }

        JScrollPane scroll = new JScrollPane(apptList);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAppointmentRow(Appointment a) {
        JPanel row = new JPanel(new BorderLayout(10, 8));
        row.setBackground(new Color(248, 251, 255));
        row.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 230, 250), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel patientLbl = new JLabel("Patient: " + a.patientId);
        patientLbl.setFont(new Font("Arial", Font.BOLD, 12));
        patientLbl.setForeground(new Color(20, 60, 120));

        JLabel dateLbl = new JLabel(a.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        dateLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        dateLbl.setForeground(new Color(100, 110, 130));

        JLabel deptLbl = new JLabel("Department: " + a.department);
        deptLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        deptLbl.setForeground(new Color(80, 90, 110));

        left.add(patientLbl);
        left.add(Box.createVerticalStrut(2));
        left.add(dateLbl);
        left.add(Box.createVerticalStrut(2));
        left.add(deptLbl);

        row.add(left, BorderLayout.CENTER);

        return row;
    }

    private JPanel createAccountPanel(Staff staff, JLabel accountLbl) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        boolean hasAccount = DataStore.users.containsKey(staff.id);

        // Status box - Modern design
        JPanel statusBox = new JPanel(new BorderLayout());
        statusBox.setBackground(hasAccount ? Theme.SUCCESS : Theme.DANGER);
        statusBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(hasAccount ? Theme.SUCCESS : Theme.DANGER, 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        accountStatusLbl = new JLabel(hasAccount ? "✓ Account Active" : "✗ No Account");
        accountStatusLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        accountStatusLbl.setForeground(Color.WHITE);

        String statusMsg = hasAccount ? "Staff account is active. Password is securely stored." : "No login account has been created for this staff member.";
        JLabel statusDetailLbl = new JLabel(statusMsg);
        statusDetailLbl.setFont(Theme.APP_FONT);
        statusDetailLbl.setForeground(Color.WHITE);

        
        JPanel statusContent = new JPanel();
        statusContent.setLayout(new BoxLayout(statusContent, BoxLayout.Y_AXIS));
        statusContent.setOpaque(false);
        statusContent.add(accountStatusLbl);
        statusContent.add(Box.createVerticalStrut(10));
        statusContent.add(statusDetailLbl);
        
        

        statusBox.add(statusContent, BorderLayout.CENTER);

        gbc.gridy = 0;
        content.add(statusBox, gbc);

        gbc.gridy = 1;
        content.add(Box.createVerticalStrut(20), gbc);

        // Action buttons - Modern styling
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        btnPanel.setOpaque(false);

        if (hasAccount) {
            JButton changeBtn = new JButton("🔐 Change Password");
            Theme.styleSecondary(changeBtn);
            changeBtn.setFont(Theme.APP_FONT_BOLD);
            changeBtn.addActionListener(e -> changePassword(staff.id, statusDetailLbl));
            btnPanel.add(changeBtn);
        }
        // Account creation removed - handled in Administrator section

        gbc.gridy = 2;
        content.add(btnPanel, gbc);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BACKGROUND);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void changePassword(String staffId, JLabel statusLbl) {
        JDialog pwdDialog = new JDialog(this, "Set New Password", ModalityType.APPLICATION_MODAL);
        pwdDialog.setSize(450, 300);
        pwdDialog.setLocationRelativeTo(this);
        pwdDialog.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(Theme.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel headerLbl = new JLabel("🔐 Set New Password");
        headerLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLbl.setForeground(Color.WHITE);
        header.add(headerLbl);
        pwdDialog.add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel newPwdLbl = new JLabel("New Password:");
        newPwdLbl.setFont(Theme.APP_FONT_BOLD);
        newPwdLbl.setForeground(Theme.PRIMARY);
        gbc.gridy = 0; gbc.gridx = 0; content.add(newPwdLbl, gbc);

        JPasswordField newPwdField = new JPasswordField();
        Theme.stylePasswordField(newPwdField);
        gbc.gridy = 1; content.add(newPwdField, gbc);

        JLabel confirmLbl = new JLabel("Confirm Password:");
        confirmLbl.setFont(Theme.APP_FONT_BOLD);
        confirmLbl.setForeground(Theme.PRIMARY);
        gbc.gridy = 2; content.add(confirmLbl, gbc);

        JPasswordField confirmPwdField = new JPasswordField();
        Theme.stylePasswordField(confirmPwdField);
        gbc.gridy = 3; content.add(confirmPwdField, gbc);

        pwdDialog.add(content, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(Theme.APP_FONT);
        cancelBtn.setBackground(new Color(200, 200, 210));
        cancelBtn.setForeground(new Color(40, 40, 40));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        cancelBtn.addActionListener(e -> pwdDialog.dispose());

        JButton saveBtn = new JButton("Change Password");
        Theme.stylePrimary(saveBtn);
        saveBtn.setFont(Theme.APP_FONT_BOLD);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        saveBtn.addActionListener(e -> {
            String newPwd = new String(newPwdField.getPassword());
            String confirmPwd = new String(confirmPwdField.getPassword());

            if (newPwd.isEmpty() || confirmPwd.isEmpty()) {
                JOptionPane.showMessageDialog(pwdDialog, "Please fill in all fields", "Missing Fields", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!newPwd.equals(confirmPwd)) {
                JOptionPane.showMessageDialog(pwdDialog, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                newPwdField.setText("");
                confirmPwdField.setText("");
                newPwdField.requestFocus();
                return;
            }

            if (newPwd.length() < 4) {
                JOptionPane.showMessageDialog(pwdDialog, "Password must be at least 4 characters", "Weak Password", JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.util.List<String> result = AuthService.changePasswordNoOld(staffId, newPwd);
            if (result != null && !result.isEmpty() && result.get(0).startsWith("Error:")) {
                JOptionPane.showMessageDialog(pwdDialog, result.get(0), "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(pwdDialog, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                pwdDialog.dispose();
                if (currentPasswordLbl != null) {
                    currentPasswordLbl.setText(newPwd);
                }
            }
        });

        footer.add(cancelBtn);
        footer.add(saveBtn);
        pwdDialog.add(footer, BorderLayout.SOUTH);

        pwdDialog.setVisible(true);
    }
}
