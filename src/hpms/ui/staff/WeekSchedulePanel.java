package hpms.ui.staff;

import javax.swing.*;
import java.awt.*;
import hpms.ui.components.Theme;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Small reusable component that captures a doctor's weekly schedule.
 * It exposes helpers to validate and serialize the schedule into a compact string
 * that can be stored in the existing staff.clinicSchedule field.
 */
public class WeekSchedulePanel extends JPanel {

    private final String[] days = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    private final Map<String, JCheckBox> onDuty = new LinkedHashMap<>();
    private final Map<String, JTextField> start = new LinkedHashMap<>();
    private final Map<String, JTextField> end = new LinkedHashMap<>();

    public WeekSchedulePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Create inner panel with GridBagLayout
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 18, 15, 18);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // header row
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.15; gbc.anchor = GridBagConstraints.WEST;
        JLabel dayHeader = new JLabel("Day");
        dayHeader.setFont(new Font("Arial", Font.BOLD, 11));
        dayHeader.setForeground(Theme.PRIMARY);
        innerPanel.add(dayHeader, gbc);

        gbc.gridx = 1; gbc.weightx = 0.12; gbc.anchor = GridBagConstraints.CENTER;
        JLabel dutyHeader = new JLabel("On Duty");
        dutyHeader.setFont(new Font("Arial", Font.BOLD, 11));
        dutyHeader.setForeground(Theme.PRIMARY);
        innerPanel.add(dutyHeader, gbc);

        gbc.gridx = 2; gbc.weightx = 0.23; gbc.anchor = GridBagConstraints.WEST;
        JLabel startHeader = new JLabel("Start (HH:mm)");
        startHeader.setFont(new Font("Arial", Font.BOLD, 11));
        startHeader.setForeground(Theme.PRIMARY);
        innerPanel.add(startHeader, gbc);

        gbc.gridx = 3; gbc.weightx = 0.23; gbc.anchor = GridBagConstraints.WEST;
        JLabel endHeader = new JLabel("End (HH:mm)");
        endHeader.setFont(new Font("Arial", Font.BOLD, 11));
        endHeader.setForeground(Theme.PRIMARY);
        innerPanel.add(endHeader, gbc);

        // separator
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(10, 18, 20, 18);
        innerPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1; gbc.insets = new Insets(15, 18, 15, 18);

        int r = 2;
        for (String d : days) {
            // Day label
            gbc.gridy = r; gbc.gridx = 0; gbc.weightx = 0.15; gbc.anchor = GridBagConstraints.WEST;
            JLabel dayLabel = new JLabel(d);
            dayLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            innerPanel.add(dayLabel, gbc);

            // On Duty checkbox
            gbc.gridx = 1; gbc.weightx = 0.12; gbc.anchor = GridBagConstraints.CENTER;
            JCheckBox cb = new JCheckBox();
            cb.setBackground(Color.WHITE);
            cb.setToolTipText("Check if this day is a working day");
            onDuty.put(d, cb);
            innerPanel.add(cb, gbc);

            // Start time
            gbc.gridx = 2; gbc.weightx = 0.25; gbc.anchor = GridBagConstraints.WEST;
            JTextField s = new JTextField("08:00");
            s.setToolTipText("Start time in HH:mm");
            s.setPreferredSize(new Dimension(120, 32));
            s.setMaximumSize(new Dimension(120, 32));
            Theme.styleTextField(s);
            start.put(d, s);
            innerPanel.add(s, gbc);

            // End time
            gbc.gridx = 3; gbc.weightx = 0.25; gbc.anchor = GridBagConstraints.WEST;
            JTextField e = new JTextField("17:00");
            e.setToolTipText("End time in HH:mm");
            e.setPreferredSize(new Dimension(120, 32));
            e.setMaximumSize(new Dimension(120, 32));
            Theme.styleTextField(e);
            end.put(d, e);
            innerPanel.add(e, gbc);

            r++;
        }

        // Create scrollpane with innerPanel
        JScrollPane scrollPane = new JScrollPane(innerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        
        add(scrollPane, BorderLayout.CENTER);

        innerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        setPreferredSize(new Dimension(650, 400));
        setMaximumSize(new Dimension(1200, 600));

        // sensible defaults: Mon-Fri on duty
        for (String d : days) {
            if ("Sat".equals(d) || "Sun".equals(d)) onDuty.get(d).setSelected(false);
            else onDuty.get(d).setSelected(true);
        }
    }

    /**
     * Validates inputs and returns null if validation passes or an error message.
     */
    public String validateSchedule() {
        for (String d : days) {
            JTextField s = start.get(d);
            JTextField e = end.get(d);
            if (onDuty.get(d).isSelected()) {
                String sv = s.getText().trim(); String ev = e.getText().trim();
                if (sv.isEmpty() || ev.isEmpty()) return "Start and end times required for " + d;
                if (!sv.matches("^[0-2][0-9]:[0-5][0-9]$")) return "Invalid start time for " + d + ". Use HH:mm";
                if (!ev.matches("^[0-2][0-9]:[0-5][0-9]$")) return "Invalid end time for " + d + ". Use HH:mm";
                // simple ordering check
                try {
                    String[] pa = sv.split(":"), pb = ev.split(":");
                    int si = Integer.parseInt(pa[0]) * 60 + Integer.parseInt(pa[1]);
                    int ei = Integer.parseInt(pb[0]) * 60 + Integer.parseInt(pb[1]);
                    if (ei <= si) return "End time must be after start time for " + d;
                } catch (Exception ex) { return "Invalid times for " + d; }
            }
        }
        return null;
    }

    /**
     * Serializes schedule to a compact string: Mon:08:00-17:00;Tue:OFF;...
     */
    public String getScheduleString() {
        StringBuilder out = new StringBuilder();
        for (String d : days) {
            if (out.length() > 0) out.append(";");
            if (onDuty.get(d).isSelected()) {
                out.append(d).append(":").append(start.get(d).getText().trim()).append("-").append(end.get(d).getText().trim());
            } else {
                out.append(d).append(":OFF");
            }
        }
        return out.toString();
    }

    /**
     * Populate GUI from schedule string (best-effort) - format produced by getScheduleString
     */
    public void setScheduleFromString(String schedule) {
        if (schedule == null || schedule.trim().isEmpty()) return;
        String[] parts = schedule.split(";");
        for (String p : parts) {
            if (!p.contains(":")) continue;
            String[] kv = p.split(":",2);
            String d = kv[0]; String v = kv[1];
            if (!onDuty.containsKey(d)) continue;
            if (v.equalsIgnoreCase("OFF")) { onDuty.get(d).setSelected(false); }
            else if (v.contains("-")) {
                String[] t = v.split("-",2);
                onDuty.get(d).setSelected(true);
                start.get(d).setText(t[0]); end.get(d).setText(t[1]);
            }
        }
    }
}
