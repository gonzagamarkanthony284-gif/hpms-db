package hpms.ui;

import hpms.model.*;
import hpms.util.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Locale;
import java.awt.image.BufferedImage;

/**
 * Modern patient details dialog with clean UI/UX:
 * - Large patient photo on the left
 * - Patient info + doctor expertise clearly displayed on the right
 * - Full medical details in tabs below
 */
public class PatientDetailsDialogNew extends JDialog {
    public PatientDetailsDialogNew(Window owner, Patient p) {
        super(owner, "Patient Details - " + p.name, ModalityType.APPLICATION_MODAL);
        setSize(1000, 700);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // Header with close button
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 210)), 
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        header.setBackground(new Color(245, 250, 255));
        
        JLabel title = new JLabel(p.name);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel idLbl = new JLabel("ID: " + p.id);
        idLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        idLbl.setForeground(new Color(90, 90, 90));
        
        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.add(title);
        headerLeft.add(Box.createHorizontalStrut(15));
        headerLeft.add(idLbl);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        
        header.add(headerLeft, BorderLayout.WEST);
        header.add(closeBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Main content: Two-column layout
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 20));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // LEFT COLUMN: Large patient photo
        JPanel photoPanel = new JPanel(new BorderLayout());
        photoPanel.setBackground(Color.WHITE);
        photoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        JLabel photoLabel = new JLabel();
        photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        photoLabel.setVerticalAlignment(SwingConstants.CENTER);
        photoLabel.setIcon(loadPatientPhoto(p.photoPath, 200, 280));
        photoPanel.add(photoLabel, BorderLayout.CENTER);
        mainContent.add(photoPanel);

        // RIGHT COLUMN: Patient info + doctor expertise
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Patient Information Section
        infoPanel.add(createSectionHeader("PATIENT INFORMATION"));
        infoPanel.add(createInfoRow("Name:", p.name, 14, true));
        infoPanel.add(createInfoRow("Age / Gender:", p.age + " / " + (p.gender == null ? "N/A" : p.gender.name()), 12, false));
        infoPanel.add(createInfoRow("Birthday:", p.birthday == null ? "N/A" : p.birthday, 11, false));
        infoPanel.add(createInfoRow("Contact:", p.contact == null ? "" : p.contact, 11, false));
        infoPanel.add(createInfoRow("Address:", p.address == null ? "" : p.address, 11, false));
        infoPanel.add(createInfoRow("Type:", p.patientType, 11, false));
        infoPanel.add(Box.createVerticalStrut(15));

        // Vitals Section
        infoPanel.add(createSectionHeader("VITALS & HEALTH"));
        infoPanel.add(createInfoRow("Height/Weight:", 
                (p.heightCm == null ? "N/A" : String.format("%.0f cm", p.heightCm)) + " / " +
                (p.weightKg == null ? "N/A" : String.format("%.0f kg", p.weightKg)), 11, false));
        
        Double bmi = p.getBmi();
        String bmiDisplay = "N/A";
        Color bmiColor = Color.BLACK;
        if (bmi != null) {
            String category;
            if (bmi < 18.5) { category = "Underweight"; bmiColor = new Color(0x2E86C1); }
            else if (bmi < 25.0) { category = "Normal"; bmiColor = new Color(0x27AE60); }
            else if (bmi < 30.0) { category = "Overweight"; bmiColor = new Color(0xF39C12); }
            else { category = "Obese"; bmiColor = new Color(0xE74C3C); }
            bmiDisplay = String.format("%.1f (%s)", bmi, category);
        }
        infoPanel.add(createInfoRowColored("BMI:", bmiDisplay, bmiColor, 11));
        infoPanel.add(Box.createVerticalStrut(15));

        // Assignment Section
        Room assignedRoom = null;
        for (Room r : DataStore.rooms.values())
            if (p.id.equals(r.occupantPatientId)) { assignedRoom = r; break; }
        infoPanel.add(createSectionHeader("ASSIGNMENT & DOCTOR"));
        infoPanel.add(createInfoRow("Room:", assignedRoom == null ? "Not assigned" : assignedRoom.id, 11, false));

        // Doctor Information with Expertise
        Appointment latestAppt = DataStore.appointments.values().stream()
                .filter(a -> a.patientId.equals(p.id))
                .max((a, b) -> a.dateTime.compareTo(b.dateTime))
                .orElse(null);
        
        final Room fAssignedRoom = assignedRoom;
        final Appointment fLatestAppt = latestAppt;

        if (fLatestAppt != null) {
            Staff doctor = DataStore.staff.get(fLatestAppt.staffId);
            if (doctor != null) {
                infoPanel.add(createInfoRow("Primary Doctor:", doctor.name, 12, true));
                
                String expertise = "-";
                if (doctor.specialty != null && !doctor.specialty.trim().isEmpty()) 
                    expertise = doctor.specialty;
                else if (doctor.subSpecialization != null && !doctor.subSpecialization.trim().isEmpty())
                    expertise = doctor.subSpecialization;
                else if (doctor.department != null && !doctor.department.trim().isEmpty())
                    expertise = doctor.department;
                
                infoPanel.add(createInfoRow("Expertise:", expertise, 11, false));
                infoPanel.add(createInfoRow("Last Visit:", fLatestAppt.dateTime.toLocalDate().toString(), 11, false));
            }
        } else {
            infoPanel.add(createInfoRow("Primary Doctor:", "None", 12, true));
        }

        infoPanel.add(Box.createVerticalGlue());
        mainContent.add(infoPanel);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.add(mainContent, BorderLayout.NORTH);

        // Medical details in tabbed pane below
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.addTab("Medical History", createMedicalPanel(p));
        tabs.addTab("Visits", createVisitsPanel(p));
        tabs.addTab("Insurance", createInsurancePanel(p));
        centerWrapper.add(tabs, BorderLayout.CENTER);

        add(centerWrapper, BorderLayout.CENTER);

        // Footer with actions
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        JButton printBtn = new JButton("Print Summary");
        printBtn.addActionListener(e -> {
            JTextArea summary = new JTextArea(buildSummary(p, fAssignedRoom, fLatestAppt));
            summary.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(summary), "Patient Summary", JOptionPane.INFORMATION_MESSAGE);
        });
        footer.add(printBtn);
        footer.add(closeBtn);

        add(footer, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JLabel createSectionHeader(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(0, 102, 102));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel createInfoRow(String label, String value, int fontSize, boolean bold) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JLabel lblField = new JLabel(label);
        lblField.setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, fontSize));
        lblField.setForeground(new Color(50, 50, 50));
        lblField.setPreferredSize(new Dimension(130, 20));

        JLabel valField = new JLabel(value);
        valField.setFont(new Font("Arial", Font.PLAIN, fontSize));
        valField.setForeground(new Color(80, 80, 80));

        panel.add(lblField);
        panel.add(valField);
        return panel;
    }

    private JPanel createInfoRowColored(String label, String value, Color valueColor, int fontSize) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JLabel lblField = new JLabel(label);
        lblField.setFont(new Font("Arial", Font.BOLD, fontSize));
        lblField.setForeground(new Color(50, 50, 50));
        lblField.setPreferredSize(new Dimension(130, 20));

        JLabel valField = new JLabel(value);
        valField.setFont(new Font("Arial", Font.PLAIN, fontSize));
        valField.setForeground(valueColor);

        panel.add(lblField);
        panel.add(valField);
        return panel;
    }

    private Icon loadPatientPhoto(String path, int w, int h) {
        try {
            if (path != null && !path.trim().isEmpty()) {
                ImageIcon icon = new ImageIcon(path);
                if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                    Image scaled = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
            }
        } catch (Exception ignored) {
        }
        // Placeholder silhouette
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(235, 238, 242));
        g2.fillRect(0, 0, w, h);
        g2.setColor(new Color(180, 190, 200));
        g2.fillOval(w / 4, h / 8, w / 2, h / 2);
        g2.fillRoundRect(w / 4, h / 2, w / 2, h / 3, 30, 30);
        g2.dispose();
        return new ImageIcon(img);
    }

    private String buildSummary(Patient p, Room r, Appointment a) {
        StringBuilder sb = new StringBuilder();
        sb.append("PATIENT SUMMARY\n");
        sb.append("===============\n\n");
        sb.append("ID: ").append(p.id).append("\n");
        sb.append("Name: ").append(p.name).append("\n");
        sb.append("Age/Gender: ").append(p.age).append(" / ").append(p.gender).append("\n");
        sb.append("Birthday: ").append(p.birthday).append("\n");
        sb.append("Contact: ").append(p.contact).append("\n");
        sb.append("Address: ").append(p.address).append("\n");
        sb.append("Type: ").append(p.patientType).append("\n\n");

        sb.append("VITALS\n");
        sb.append("------\n");
        sb.append("Height: ").append(p.heightCm == null ? "N/A" : p.heightCm).append(" cm\n");
        sb.append("Weight: ").append(p.weightKg == null ? "N/A" : p.weightKg).append(" kg\n");
        Double bmi = p.getBmi();
        if (bmi != null) {
            String cat;
            if (bmi < 18.5) cat = "Underweight";
            else if (bmi < 25.0) cat = "Normal";
            else if (bmi < 30.0) cat = "Overweight";
            else cat = "Obese";
            sb.append("BMI: ").append(String.format("%.2f (%s)", bmi, cat)).append("\n");
        } else {
            sb.append("BMI: N/A\n");
        }
        sb.append("\n");

        sb.append("ASSIGNMENT\n");
        sb.append("----------\n");
        sb.append("Room: ").append(r == null ? "Not assigned" : r.id).append("\n");
        if (a != null) {
            sb.append("Last Doctor Visit: ").append(a.dateTime).append("\n");
        }
        sb.append("\n");

        sb.append("MEDICAL\n");
        sb.append("-------\n");
        sb.append("Allergies: ").append(p.allergies == null || p.allergies.isEmpty() ? "None" : p.allergies).append("\n");
        sb.append("Medications: ").append(p.medications == null || p.medications.isEmpty() ? "None" : p.medications).append("\n");
        sb.append("Past History: ").append(p.pastMedicalHistory == null || p.pastMedicalHistory.isEmpty() ? "None" : p.pastMedicalHistory).append("\n");

        return sb.toString();
    }

    private JScrollPane createMedicalPanel(Patient p) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(createSection("Allergies:", p.allergies));
        panel.add(createSection("Current Medications:", p.medications));
        panel.add(createSection("Past Medical History:", p.pastMedicalHistory));
        panel.add(Box.createVerticalGlue());

        return new JScrollPane(panel);
    }

    private JScrollPane createVisitsPanel(Patient p) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        int visitCount = 0;
        for (Appointment a : DataStore.appointments.values()) {
            if (a.patientId.equals(p.id)) {
                visitCount++;
                JLabel visitLabel = new JLabel(a.dateTime + " - " + a.department);
                visitLabel.setFont(new Font("Arial", Font.PLAIN, 11));
                panel.add(visitLabel);
            }
        }
        if (visitCount == 0) {
            JLabel noVisits = new JLabel("No visits recorded");
            noVisits.setForeground(Color.GRAY);
            panel.add(noVisits);
        }
        panel.add(Box.createVerticalGlue());

        return new JScrollPane(panel);
    }

    private JScrollPane createInsurancePanel(Patient p) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(createSection("Insurance Provider:", p.insuranceProvider));
        panel.add(createSection("Insurance ID:", p.insuranceId));
        panel.add(createSection("Policy Holder:", p.policyHolderName));
        panel.add(Box.createVerticalGlue());

        return new JScrollPane(panel);
    }

    private JPanel createSection(String title, String content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 11));
        titleLabel.setForeground(new Color(50, 50, 50));

        JTextArea contentArea = new JTextArea(content == null ? "" : content);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 11));
        contentArea.setBackground(new Color(250, 250, 250));
        contentArea.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentArea, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        return panel;
    }
}
