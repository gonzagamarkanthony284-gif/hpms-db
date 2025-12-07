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
 * Modern staff details dialog with professional styling
 * Displays comprehensive staff information with account management
 * Shows current password/login code for staff accounts
 */
public class StaffDetailsDialogModern extends JDialog {
    private JLabel accountStatusLbl, currentPasswordLbl;

    public StaffDetailsDialogModern(JFrame owner, Staff staff) {
        super(owner, "Staff Profile - " + staff.name, ModalityType.APPLICATION_MODAL);
        setSize(1100, 950);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BACKGROUND);

        // Modern header
        JPanel header = createHeader(staff);
        add(header, BorderLayout.NORTH);

        // Main body
        JPanel body = new JPanel(new BorderLayout(12, 12));
        body.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        body.setBackground(Theme.BACKGROUND);

        // Top section - Key info cards
        JPanel topSection = new JPanel(new GridLayout(1, 2, 12, 12));
        topSection.setOpaque(false);
        topSection.add(createIdentityCard(staff));
        topSection.add(createAccountCard(staff));
        body.add(topSection, BorderLayout.NORTH);

        // Tabs for detailed info
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setBackground(Theme.SURFACE);
        tabs.setFont(Theme.APP_FONT);
        tabs.addTab("Details", createDetailsPanel(staff));
        tabs.addTab("Appointments", createAppointmentsPanel(staff));
        tabs.addTab("Account", createAccountPanel(staff));
        body.add(tabs, BorderLayout.CENTER);

        add(body, BorderLayout.CENTER);
    }

    private JPanel createHeader(Staff staff) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        header.setBackground(Theme.PRIMARY_LIGHT);

        // Left side
        JPanel headerLeft = new JPanel();
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));
        headerLeft.setOpaque(false);

        JLabel title = new JLabel(staff.name);
        title.setFont(Theme.HEADING_2);
        title.setForeground(Theme.PRIMARY_DARK);

        JLabel idLbl = new JLabel("ID: " + staff.id + "  •  " + staff.role + "  •  " + staff.department);
        idLbl.setFont(Theme.SMALL_FONT);
        idLbl.setForeground(Theme.TEXT_SECONDARY);

        headerLeft.add(title);
        headerLeft.add(Box.createVerticalStrut(6));
        headerLeft.add(idLbl);

        // Right side - buttons
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerRight.setOpaque(false);

        JButton changePwdBtn = new JButton("Change Password");
        Theme.stylePrimary(changePwdBtn);
        changePwdBtn.setFont(Theme.APP_FONT);
        changePwdBtn.addActionListener(e -> changePassword(staff.id));

        JButton closeBtn = new JButton("Close");
        Theme.styleOutline(closeBtn);
        closeBtn.setFont(Theme.APP_FONT);
        closeBtn.addActionListener(e -> dispose());

        headerRight.add(changePwdBtn);
        headerRight.add(closeBtn);

        header.add(headerLeft, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);
        return header;
    }

    private JPanel createIdentityCard(Staff staff) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.SURFACE);
        card.setBorder(Theme.createModernCardBorder());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        int y = 0;
        c.gridy = y++; card.add(createSectionTitle("Contact Information"), c);
        c.gridy = y++; card.add(createFieldRow("Name", staff.name, true), c);
        c.gridy = y++; card.add(createFieldRow("Phone", staff.phone != null ? staff.phone : "N/A", false), c);
        c.gridy = y++; card.add(createFieldRow("Email", staff.email != null ? staff.email : "N/A", false), c);
        c.gridy = y++; card.add(Box.createVerticalStrut(8), c);
        c.gridy = y++; card.add(createSectionTitle("Professional Details"), c);
        c.gridy = y++; card.add(createFieldRow("License #", staff.licenseNumber != null ? staff.licenseNumber : "N/A", false), c);
        c.gridy = y++; card.add(createFieldRow("Years Experience", staff.yearsExperience != null ? String.valueOf(staff.yearsExperience) : "N/A", false), c);

        return card;
    }

    private JPanel createAccountCard(Staff staff) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.SURFACE);
        card.setBorder(Theme.createModernCardBorder());

        card.add(createSectionTitle("Account Status"));
        card.add(Box.createVerticalStrut(12));

        boolean hasAccount = DataStore.users.containsKey(staff.id);

        // Status badge
        accountStatusLbl = new JLabel(hasAccount ? "✓ Active" : "✗ Not Created");
        accountStatusLbl.setFont(Theme.HEADING_3);
        accountStatusLbl.setForeground(hasAccount ? Theme.SUCCESS : Theme.DANGER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusPanel.setOpaque(false);
        statusPanel.add(accountStatusLbl);
        card.add(statusPanel);

        card.add(Box.createVerticalStrut(16));

        JLabel msg = new JLabel(hasAccount ? "Password is stored securely" : "No account created yet");
        msg.setFont(Theme.APP_FONT);
        msg.setForeground(Theme.TEXT_SECONDARY);
        card.add(msg);

        card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel createDetailsPanel(Staff staff) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.SURFACE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;

        int row = 0;

        // Basic Information
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(createSectionLabel("Basic Information"), gbc); gbc.gridwidth = 1; row++;
        gbc.gridy = row; gbc.gridx = 0; panel.add(createFieldRow("Name", staff.name, true), gbc);
        gbc.gridx = 1; panel.add(createFieldRow("ID", staff.id, true), gbc); row++;
        gbc.gridy = row; gbc.gridx = 0; panel.add(createFieldRow("Role", staff.role.toString(), false), gbc);
        gbc.gridx = 1; panel.add(createFieldRow("Department", staff.department, false), gbc); row++;

        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(Box.createVerticalStrut(12), gbc); gbc.gridwidth = 1; row++;

        // Contact Information
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(createSectionLabel("Contact Information"), gbc); gbc.gridwidth = 1; row++;
        gbc.gridy = row; gbc.gridx = 0; panel.add(createFieldRow("Phone", staff.phone != null ? staff.phone : "N/A", false), gbc);
        gbc.gridx = 1; panel.add(createFieldRow("Email", staff.email != null ? staff.email : "N/A", false), gbc); row++;

        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(Box.createVerticalStrut(12), gbc); gbc.gridwidth = 1; row++;

        // Professional Information
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(createSectionLabel("Professional Information"), gbc); gbc.gridwidth = 1; row++;
        gbc.gridy = row; gbc.gridx = 0; panel.add(createFieldRow("License Number", staff.licenseNumber != null ? staff.licenseNumber : "N/A", false), gbc);
        gbc.gridx = 1; panel.add(createFieldRow("Years Experience", staff.yearsExperience != null ? String.valueOf(staff.yearsExperience) : "N/A", false), gbc); row++;

        if (staff.role == StaffRole.DOCTOR) {
            gbc.gridy = row; gbc.gridx = 0; panel.add(createFieldRow("Specialty", staff.specialty != null ? staff.specialty : "N/A", false), gbc);
            gbc.gridx = 1; panel.add(createFieldRow("Years Practice", staff.yearsExperience != null ? String.valueOf(staff.yearsExperience) : "N/A", false), gbc); row++;
        }

        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(Box.createVerticalStrut(12), gbc); gbc.gridwidth = 1; row++;

        // Employment
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(createSectionLabel("Employment"), gbc); gbc.gridwidth = 1; row++;
        String joinDate = staff.createdAt != null ? staff.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A";
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(createFieldRow("Joined Date", joinDate, false), gbc); gbc.gridwidth = 1; row++;
        gbc.gridy = row; gbc.gridx = 0; panel.add(createFieldRow("Status", staff.isAvailable ? "Available" : "Unavailable", false), gbc);
        gbc.gridx = 1; panel.add(createFieldRow("Department", staff.department, false), gbc);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Theme.SURFACE);
        wrapper.add(new JScrollPane(panel), BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createAppointmentsPanel(Staff staff) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.SURFACE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        java.util.List<Appointment> staffAppts = new java.util.ArrayList<>();
        for (Appointment a : DataStore.appointments.values()) {
            if (staff.id.equals(a.staffId)) {
                staffAppts.add(a);
            }
        }

        if (staffAppts.isEmpty()) {
            JLabel noAppts = new JLabel("No appointments scheduled");
            noAppts.setFont(Theme.APP_FONT);
            noAppts.setForeground(Theme.TEXT_TERTIARY);
            panel.add(noAppts, BorderLayout.CENTER);
            return panel;
        }

        staffAppts.sort((a, b) -> b.dateTime.compareTo(a.dateTime));

        JPanel apptList = new JPanel();
        apptList.setLayout(new BoxLayout(apptList, BoxLayout.Y_AXIS));
        apptList.setBackground(Theme.SURFACE);

        for (Appointment a : staffAppts) {
            apptList.add(createAppointmentRow(a));
            apptList.add(Box.createVerticalStrut(8));
        }

        JScrollPane scroll = new JScrollPane(apptList);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.SURFACE);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAppointmentRow(Appointment a) {
        JPanel row = new JPanel(new BorderLayout(10, 8));
        row.setBackground(Theme.FOCUS);
        row.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Theme.PRIMARY_LIGHT, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel patientLbl = new JLabel("Patient: " + a.patientId);
        patientLbl.setFont(Theme.APP_FONT_BOLD);
        patientLbl.setForeground(Theme.PRIMARY_DARK);

        JLabel dateLbl = new JLabel(a.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        dateLbl.setFont(Theme.SMALL_FONT);
        dateLbl.setForeground(Theme.TEXT_SECONDARY);

        JLabel deptLbl = new JLabel("Department: " + a.department);
        deptLbl.setFont(Theme.SMALL_FONT);
        deptLbl.setForeground(Theme.TEXT_SECONDARY);

        left.add(patientLbl);
        left.add(Box.createVerticalStrut(2));
        left.add(dateLbl);
        left.add(Box.createVerticalStrut(2));
        left.add(deptLbl);

        row.add(left, BorderLayout.CENTER);

        return row;
    }

    private JPanel createAccountPanel(Staff staff) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.SURFACE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Theme.SURFACE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        boolean hasAccount = DataStore.users.containsKey(staff.id);

        // Status section
        JPanel statusBox = new JPanel(new BorderLayout());
        statusBox.setBackground(hasAccount ? new Color(230, 250, 235) : new Color(255, 240, 240));
        statusBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(hasAccount ? Theme.SUCCESS : Theme.DANGER, 2, true),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        JLabel statusLbl = new JLabel(hasAccount ? "✓ Account Active" : "✗ No Account");
        statusLbl.setFont(Theme.HEADING_3);
        statusLbl.setForeground(hasAccount ? Theme.SUCCESS : Theme.DANGER);

        JPanel statusContent = new JPanel();
        statusContent.setLayout(new BoxLayout(statusContent, BoxLayout.Y_AXIS));
        statusContent.setOpaque(false);
        statusContent.add(statusLbl);
        statusContent.add(Box.createVerticalStrut(10));

        JLabel statusMsg = new JLabel(hasAccount ? "Staff account is active. Password is securely stored." : "No login account has been created for this staff member.");
        statusMsg.setFont(Theme.APP_FONT);
        statusMsg.setForeground(Theme.TEXT_SECONDARY);
        statusContent.add(statusMsg);

        // Current password shown within the password manager below

        statusBox.add(statusContent, BorderLayout.CENTER);

        gbc.gridy = 0;
        content.add(statusBox, gbc);

        gbc.gridy = 1;
        content.add(Box.createVerticalStrut(16), gbc);

        // Action buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        btnPanel.setOpaque(false);

        if (hasAccount) {
            JButton changeBtn = new JButton("Change Password");
            Theme.styleSecondary(changeBtn);
            changeBtn.addActionListener(e -> changePassword(staff.id));
            btnPanel.add(changeBtn);
        }
        // Account creation removed - handled in Administrator section

        gbc.gridy = 2;
        content.add(btnPanel, gbc);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.SURFACE);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JLabel createSectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.HEADING_4);
        lbl.setForeground(Theme.PRIMARY_DARK);
        return lbl;
    }

    private JLabel createSectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.HEADING_4);
        lbl.setForeground(Theme.PRIMARY_DARK);
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.PRIMARY_LIGHT));
        return lbl;
    }

    private JPanel createFieldRow(String label, String value, boolean bold) {
        JPanel p = new JPanel(new BorderLayout(6, 0));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(400, 30));

        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(bold ? Theme.APP_FONT_BOLD : Theme.APP_FONT);
        lbl.setForeground(Theme.TEXT_SECONDARY);
        lbl.setPreferredSize(new Dimension(130, 30));

        JLabel val = new JLabel(value);
        val.setFont(Theme.APP_FONT);
        val.setForeground(Theme.FOREGROUND);

        p.add(lbl, BorderLayout.WEST);
        p.add(val, BorderLayout.CENTER);
        return p;
    }

    private void changePassword(String staffId) {
        if (!DataStore.users.containsKey(staffId)) {
            JOptionPane.showMessageDialog(this, "No account exists.");
            return;
        }
        JPasswordField nw = new JPasswordField();
        JPasswordField nw2 = new JPasswordField();
        Object[] msg = new Object[]{"New Password", nw, "Confirm New", nw2};
        int r = JOptionPane.showConfirmDialog(this, msg, "Change Staff Password", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            String nstr = new String(nw.getPassword());
            String n2 = new String(nw2.getPassword());
            if (!nstr.equals(n2)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match");
                return;
            }
            java.util.List<String> out = AuthService.changePasswordNoOld(staffId, nstr);
            if (!out.isEmpty() && out.get(0).startsWith("Error:")) {
                JOptionPane.showMessageDialog(this, out.get(0));
            } else {
                JOptionPane.showMessageDialog(this, "Password changed successfully");
                if (currentPasswordLbl != null) {
                    currentPasswordLbl.setText(nstr);
                }
            }
        }
    }
}
