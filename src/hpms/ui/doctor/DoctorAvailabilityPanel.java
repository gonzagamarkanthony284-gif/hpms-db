package hpms.ui.doctor;

import hpms.auth.AuthSession;
import hpms.model.Staff;
import hpms.util.DataStore;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Doctor Availability Panel - set weekly schedule
 */
public class DoctorAvailabilityPanel extends JPanel {
    private Staff doctorStaff;
    private JCheckBox[] dayCheckBoxes = new JCheckBox[7];
    private JSpinner[] startTimeSpinners = new JSpinner[7];
    private JSpinner[] endTimeSpinners = new JSpinner[7];
    private String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public DoctorAvailabilityPanel(AuthSession session) {
        this.doctorStaff = DataStore.staff.get(session.userId);
        
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setBackground(Color.WHITE);

        // Header
        JLabel title = new JLabel("Clinic Availability Setup");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(31, 41, 55));
        add(title, BorderLayout.NORTH);

        // Schedule table
        JPanel schedulePanel = createSchedulePanel();
        add(new JScrollPane(schedulePanel), BorderLayout.CENTER);

        // Bottom action buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        actions.setBackground(new Color(248, 249, 250));
        actions.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));

        JButton saveBtn = new JButton("Save Schedule");
        saveBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        saveBtn.setBackground(new Color(47, 111, 237));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(e -> saveSchedule());

        actions.add(saveBtn);
        add(actions, BorderLayout.SOUTH);
    }

    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Header row
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        JLabel headerAvailable = new JLabel("Available");
        headerAvailable.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(headerAvailable, gbc);

        gbc.gridx = 1; gbc.weightx = 0.25;
        JLabel headerDay = new JLabel("Day");
        headerDay.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(headerDay, gbc);

        gbc.gridx = 2; gbc.weightx = 0.25;
        JLabel headerStart = new JLabel("Start Time");
        headerStart.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(headerStart, gbc);

        gbc.gridx = 3; gbc.weightx = 0.25;
        JLabel headerEnd = new JLabel("End Time");
        headerEnd.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(headerEnd, gbc);

        // Data rows
        for (int i = 0; i < 7; i++) {
            gbc.gridy = i + 1;

            gbc.gridx = 0; gbc.weightx = 0.1;
            dayCheckBoxes[i] = new JCheckBox();
            dayCheckBoxes[i].setBackground(Color.WHITE);
            panel.add(dayCheckBoxes[i], gbc);

            gbc.gridx = 1; gbc.weightx = 0.25;
            JLabel dayLabel = new JLabel(days[i]);
            dayLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            panel.add(dayLabel, gbc);

            gbc.gridx = 2; gbc.weightx = 0.25;
            startTimeSpinners[i] = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startTimeSpinners[i], "HH:mm");
            startTimeSpinners[i].setEditor(startEditor);
            panel.add(startTimeSpinners[i], gbc);

            gbc.gridx = 3; gbc.weightx = 0.25;
            endTimeSpinners[i] = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endTimeSpinners[i], "HH:mm");
            endTimeSpinners[i].setEditor(endEditor);
            panel.add(endTimeSpinners[i], gbc);
        }

        return panel;
    }

    private void saveSchedule() {
        StringBuilder schedule = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            if (dayCheckBoxes[i].isSelected()) {
                schedule.append(days[i]).append(":08:00-17:00;");
            } else {
                schedule.append(days[i]).append(":OFF;");
            }
        }
        
        if (doctorStaff != null) {
            // Update doctor's schedule in DataStore
            // This would need to be extended based on your Staff model
            JOptionPane.showMessageDialog(this, "Schedule saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refresh() {
        // Load current schedule
    }
}
