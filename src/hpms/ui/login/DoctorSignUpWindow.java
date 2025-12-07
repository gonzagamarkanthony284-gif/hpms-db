package hpms.ui.login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Pattern;

public class DoctorSignUpWindow extends JFrame {
    private final JTextField fullNameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField licenseField = new JTextField();
    private final JComboBox<String> specializationCombo = new JComboBox<>();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();
    private final JCheckBox termsCheckBox = new JCheckBox();
    private final JButton signUpBtn = new JButton("Create Account");
    private final JButton backBtn = new JButton("Back to Login");
    private final JLabel passwordReqLabel = new JLabel();

    public DoctorSignUpWindow() {
        setTitle("Doctor Sign-Up | HPMS");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(900, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        Color bg = Color.WHITE;
        Color fg = new Color(30, 30, 30);
        Color accent = new Color(30, 64, 175);
        Color accentLight = new Color(59, 130, 246);

        getContentPane().setBackground(bg);

        // Main panel with two columns
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bg);

        // Left panel - Branding
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(280, getHeight()));
        leftPanel.setBackground(accent);

        GridBagConstraints lc = new GridBagConstraints();
        lc.insets = new Insets(8, 8, 8, 8);

        JLabel brandLabel = new JLabel("HPMS");
        brandLabel.setFont(new Font("Arial", Font.BOLD, 36));
        brandLabel.setForeground(Color.WHITE);
        lc.gridx = 0;
        lc.gridy = 0;
        leftPanel.add(brandLabel, lc);

        JLabel taglineLabel = new JLabel("Healthcare Professional");
        taglineLabel.setForeground(new Color(200, 220, 240));
        taglineLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        lc.gridy = 1;
        leftPanel.add(taglineLabel, lc);

        JLabel taglineLabel2 = new JLabel("Management System");
        taglineLabel2.setForeground(new Color(200, 220, 240));
        taglineLabel2.setFont(new Font("Arial", Font.PLAIN, 12));
        lc.gridy = 2;
        leftPanel.add(taglineLabel2, lc);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Right panel - Form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(249, 250, 251));
        rightPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Scroll pane for form
        JScrollPane scrollPane = new JScrollPane(createFormPanel(fg, accent, accentLight));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(249, 250, 251));

        rightPanel.add(scrollPane);

        mainPanel.add(rightPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createFormPanel(Color fg, Color accent, Color accentLight) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(249, 250, 251));
        formPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Title
        JLabel titleLabel = new JLabel("Doctor Sign-Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(fg);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Create your professional account");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(107, 114, 128));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(subtitleLabel);
        formPanel.add(Box.createVerticalStrut(20));

        // Full Name
        formPanel.add(createFormGroup("Full Name *", fullNameField, fg));

        // Email
        formPanel.add(createFormGroup("Email Address *", emailField, fg));

        // Phone
        formPanel.add(createFormGroup("Phone Number *", phoneField, fg));

        // License Number
        formPanel.add(createFormGroup("Medical License Number *", licenseField, fg));

        // Specialization
        populateSpecializations();
        JPanel specPanel = new JPanel();
        specPanel.setLayout(new BoxLayout(specPanel, BoxLayout.Y_AXIS));
        specPanel.setBackground(new Color(249, 250, 251));
        specPanel.setMaximumSize(new Dimension(350, 60));

        JLabel specLabel = new JLabel("Specialization *");
        specLabel.setForeground(fg);
        specLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        specLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        specPanel.add(specLabel);
        specPanel.add(Box.createVerticalStrut(4));

        specializationCombo.setMaximumSize(new Dimension(350, 32));
        specializationCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        specPanel.add(specializationCombo);
        specPanel.add(Box.createVerticalStrut(12));

        formPanel.add(specPanel);

        // Password
        formPanel.add(createPasswordGroup("Password *", passwordField, fg));

        // Password Requirements
        passwordReqLabel.setText("Requirements: 8+ chars, uppercase, lowercase, number");
        passwordReqLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        passwordReqLabel.setForeground(new Color(156, 163, 175));
        passwordReqLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordReqLabel);
        formPanel.add(Box.createVerticalStrut(12));

        // Confirm Password
        formPanel.add(createPasswordGroup("Confirm Password *", confirmPasswordField, fg));

        // Terms
        JPanel termsPanel = new JPanel();
        termsPanel.setLayout(new BoxLayout(termsPanel, BoxLayout.X_AXIS));
        termsPanel.setBackground(new Color(249, 250, 251));
        termsPanel.setMaximumSize(new Dimension(350, 40));
        termsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        termsCheckBox.setBackground(new Color(249, 250, 251));
        termsCheckBox.setText("I agree to Terms & Conditions and Privacy Policy *");
        termsCheckBox.setFont(new Font("Arial", Font.PLAIN, 11));

        termsPanel.add(termsCheckBox);
        formPanel.add(termsPanel);
        formPanel.add(Box.createVerticalStrut(16));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(new Color(249, 250, 251));
        buttonPanel.setMaximumSize(new Dimension(350, 40));

        signUpBtn.setMaximumSize(new Dimension(160, 36));
        signUpBtn.setBackground(accent);
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setFont(new Font("Arial", Font.BOLD, 12));
        signUpBtn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        signUpBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpBtn.addActionListener(e -> handleSignUp());

        backBtn.setMaximumSize(new Dimension(120, 36));
        backBtn.setBackground(Color.WHITE);
        backBtn.setForeground(accent);
        backBtn.setFont(new Font("Arial", Font.BOLD, 12));
        backBtn.setBorder(BorderFactory.createLineBorder(accent, 2));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());

        buttonPanel.add(signUpBtn);
        buttonPanel.add(Box.createHorizontalStrut(12));
        buttonPanel.add(backBtn);
        buttonPanel.add(Box.createHorizontalGlue());

        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalGlue());

        return formPanel;
    }

    private JPanel createFormGroup(String label, JTextField field, Color fg) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(249, 250, 251));
        panel.setMaximumSize(new Dimension(350, 60));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setForeground(fg);
        labelComponent.setFont(new Font("Arial", Font.PLAIN, 12));
        labelComponent.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(labelComponent);
        panel.add(Box.createVerticalStrut(4));

        field.setMaximumSize(new Dimension(350, 32));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 2));
        field.setBackground(Color.WHITE);
        panel.add(field);
        panel.add(Box.createVerticalStrut(12));

        return panel;
    }

    private JPanel createPasswordGroup(String label, JPasswordField field, Color fg) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(249, 250, 251));
        panel.setMaximumSize(new Dimension(350, 60));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setForeground(fg);
        labelComponent.setFont(new Font("Arial", Font.PLAIN, 12));
        labelComponent.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(labelComponent);
        panel.add(Box.createVerticalStrut(4));

        field.setMaximumSize(new Dimension(350, 32));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 2));
        field.setBackground(Color.WHITE);
        panel.add(field);
        panel.add(Box.createVerticalStrut(12));

        return panel;
    }

    private void populateSpecializations() {
        String[] specializations = {
                "-- Select Specialization --",
                "General Practitioner",
                "Cardiologist",
                "Dermatologist",
                "Neurologist",
                "Orthopedic Surgeon",
                "Pediatrician",
                "Psychiatrist",
                "General Surgeon",
                "ENT Specialist",
                "Ophthalmologist",
                "Radiologist",
                "Anesthesiologist",
                "Other"
        };

        for (String spec : specializations) {
            specializationCombo.addItem(spec);
        }
    }

    private void handleSignUp() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String license = licenseField.getText().trim();
        String specialization = (String) specializationCombo.getSelectedItem();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        boolean termsAccepted = termsCheckBox.isSelected();

        // Validation
        StringBuilder errors = new StringBuilder();

        if (fullName.isEmpty()) {
            errors.append("• Full Name is required\n");
        } else if (fullName.length() < 3) {
            errors.append("• Full Name must be at least 3 characters\n");
        }

        if (email.isEmpty()) {
            errors.append("• Email Address is required\n");
        } else if (!isValidEmail(email)) {
            errors.append("• Email Address is invalid\n");
        }

        if (phone.isEmpty()) {
            errors.append("• Phone Number is required\n");
        } else if (!isValidPhone(phone)) {
            errors.append("• Phone Number is invalid (minimum 10 digits)\n");
        }

        if (license.isEmpty()) {
            errors.append("• Medical License Number is required\n");
        } else if (license.length() < 5) {
            errors.append("• Medical License Number must be at least 5 characters\n");
        }

        if (specialization.equals("-- Select Specialization --")) {
            errors.append("• Please select a Specialization\n");
        }

        if (password.isEmpty()) {
            errors.append("• Password is required\n");
        } else if (!isValidPassword(password)) {
            errors.append("• Password must be 8+ characters with uppercase, lowercase, and number\n");
        }

        if (!password.equals(confirmPassword)) {
            errors.append("• Passwords do not match\n");
        }

        if (!termsAccepted) {
            errors.append("• You must agree to Terms & Conditions\n");
        }

        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this, errors.toString(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Success
        JOptionPane.showMessageDialog(this, "Account created successfully!\n\nDoctor Information:\nName: " + fullName + "\nSpecialization: " + specialization, "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear form
        fullNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        licenseField.setText("");
        specializationCombo.setSelectedIndex(0);
        passwordField.setText("");
        confirmPasswordField.setText("");
        termsCheckBox.setSelected(false);

        dispose();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPhone(String phone) {
        String phoneRegex = "^[0-9+\\-\\s()]{10,}$";
        return Pattern.matches(phoneRegex, phone);
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[a-z].*")) return false;
        if (!password.matches(".*[0-9].*")) return false;
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DoctorSignUpWindow().setVisible(true));
    }
}
