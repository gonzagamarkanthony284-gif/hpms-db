package hpms.ui.staff;

import javax.swing.*;
import java.awt.*;

/**
 * Read-only staff profile panel for patient view. Shows different fields by role.
 */
public class StaffProfile extends JPanel {

    public StaffProfile(Staff s) {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(6,6,6,6); gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title area (no photo) — compact header
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        JLabel nameLbl = new JLabel(s==null?"(no staff)":s.name); nameLbl.setFont(nameLbl.getFont().deriveFont(Font.BOLD, 16f)); add(nameLbl, gbc);

        gbc.gridy = 1; add(new JLabel(s==null?"":s.role + " — " + (s.department==null?"":s.department)), gbc);

        gbc.gridy = 2; add(new JLabel("Status: " + (s==null?"":(s.status==null?"Active":s.status))), gbc);

        // Details area
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        JPanel details = new JPanel(new GridBagLayout()); add(details, gbc);

        GridBagConstraints d = new GridBagConstraints(); d.insets = new Insets(6,6,6,6); d.fill = GridBagConstraints.HORIZONTAL; d.gridx=0; d.gridy=0; d.weightx=0.2; d.anchor=GridBagConstraints.WEST;

        if (s == null) {
            details.add(new JLabel("No staff selected"), d); return;
        }

        if ("DOCTOR".equals(s.role)) {
            details.add(new JLabel("Specialization:"), d); d.gridx=1; details.add(new JLabel(s.specialization==null?"":s.specialization), d);
            d.gridx=0; d.gridy++; details.add(new JLabel("Sub-specialty:"), d); d.gridx=1; details.add(new JLabel(s.subSpecialization==null?"":s.subSpecialization), d);
            d.gridx=0; d.gridy++; details.add(new JLabel("Years of Practice:"), d); d.gridx=1; details.add(new JLabel(s.yearsPractice==null?"":s.yearsPractice.toString()), d);
            d.gridx=0; d.gridy++; details.add(new JLabel("Clinic Schedule:"), d); d.gridx=1; 
            String pretty = s.clinicSchedule==null?"":makePrettySchedule(s.clinicSchedule);
            JTextArea sched = new JTextArea(pretty); sched.setLineWrap(true); sched.setWrapStyleWord(true); sched.setEditable(false); sched.setBackground(getBackground()); details.add(new JScrollPane(sched), d);
            d.gridx=0; d.gridy++; details.add(new JLabel("Bio:"), d); d.gridx=1; JTextArea bio = new JTextArea(s.bio==null?"":s.bio); bio.setLineWrap(true); bio.setWrapStyleWord(true); bio.setEditable(false); bio.setBackground(getBackground()); details.add(new JScrollPane(bio), d);
        } else if ("NURSE".equals(s.role)) {
            details.add(new JLabel("Nursing Field:"), d); d.gridx=1; details.add(new JLabel(s.nursingField==null?"":s.nursingField), d);
            d.gridx=0; d.gridy++; details.add(new JLabel("Years Experience:"), d); d.gridx=1; details.add(new JLabel(s.yearsExperience==null?"":s.yearsExperience.toString()), d);
            d.gridx=0; d.gridy++; details.add(new JLabel("License #:"), d); d.gridx=1; details.add(new JLabel(s.licenseNumber==null?"":s.licenseNumber), d);
            d.gridx=0; d.gridy++; details.add(new JLabel("Certifications:"), d); d.gridx=1; details.add(new JLabel(s.certifications==null?"":s.certifications), d);
        } else { // CASHIER / ADMIN
            details.add(new JLabel("Phone:"), d); d.gridx=1; details.add(new JLabel(s.phone==null?"":s.phone), d);
            d.gridx=0; d.gridy++; details.add(new JLabel("Email:"), d); d.gridx=1; details.add(new JLabel(s.email==null?"":s.email), d);
            if ("CASHIER".equals(s.role)) { d.gridx=0; d.gridy++; details.add(new JLabel("Employee ID:"), d); d.gridx=1; details.add(new JLabel(s.employeeId==null?"":s.employeeId), d); }
        }
    }

    private static String makePrettySchedule(String raw) {
        if (raw == null || raw.trim().isEmpty()) return "No schedule on file";
        StringBuilder sb = new StringBuilder();
        String[] parts = raw.split(";");
        for (String p : parts) {
            if (!p.contains(":")) continue;
            String[] kv = p.split(":",2); String d = kv[0]; String v = kv[1];
            if (v.equalsIgnoreCase("OFF")) sb.append(d).append(": Off\n"); else sb.append(d).append(": ").append(v).append("\n");
        }
        return sb.toString().trim();
    }

    // convenience main for preview/edit in WindowBuilder
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("StaffProfile Preview"); f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Staff sample = new Staff(); sample.name = "Dr. Alice"; sample.role = "DOCTOR"; sample.department = "General Medicine"; sample.specialization = "Cardiology"; sample.yearsPractice = 7; sample.clinicSchedule = "Mon/Wed 9-12"; sample.bio = "Sample bio about the doctor.";
            f.getContentPane().add(new StaffProfile(sample)); f.setSize(640,420); f.setLocationRelativeTo(null); f.setVisible(true);
        });
    }
}
