package hpms.ui.clinical;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class XrayPanel extends JPanel {
    private JLabel fileLabel;
    private JButton uploadButton;

    // AUTO-SUMMARY fields
    private JCheckBox cbBrokenBone, cbPneumonia, cbNormal, cbFracture, cbTumor;
    private JCheckBox cbLeftArm, cbRightLeg, cbRibs, cbSkull, cbSpine, cbChest;
    private JCheckBox cbMild, cbModerate, cbSevere;
    private JCheckBox cbCast, cbSurgery, cbFollowUp, cbMRI;
    private JTextArea notesArea;

    public XrayPanel() {
        setLayout(new BorderLayout(8,8));
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(new TitledBorder("X-RAY Upload & Summary"));

        JPanel uploadRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        uploadButton = new JButton("Choose X-ray File");
        fileLabel = new JLabel("No file chosen");
        uploadRow.add(uploadButton);
        uploadRow.add(fileLabel);

        top.add(uploadRow, BorderLayout.NORTH);

        // left: checkboxes groups
        JPanel groups = new JPanel(new GridLayout(2,2,8,8));

        // Findings
        JPanel findings = new JPanel(new FlowLayout(FlowLayout.LEFT));
        findings.setBorder(new TitledBorder("Finding"));
        cbBrokenBone = new JCheckBox("Broken Bone");
        cbPneumonia = new JCheckBox("Pneumonia");
        cbNormal = new JCheckBox("Normal");
        cbFracture = new JCheckBox("Fracture");
        cbTumor = new JCheckBox("Tumor/Mass");
        findings.add(cbBrokenBone); findings.add(cbPneumonia); findings.add(cbNormal); findings.add(cbFracture); findings.add(cbTumor);

        // Location
        JPanel location = new JPanel(new FlowLayout(FlowLayout.LEFT));
        location.setBorder(new TitledBorder("Location"));
        cbLeftArm = new JCheckBox("Left Arm"); cbRightLeg = new JCheckBox("Right Leg"); cbRibs = new JCheckBox("Ribs"); cbSkull = new JCheckBox("Skull"); cbSpine = new JCheckBox("Spine"); cbChest = new JCheckBox("Chest");
        location.add(cbLeftArm); location.add(cbRightLeg); location.add(cbRibs); location.add(cbSkull); location.add(cbSpine); location.add(cbChest);

        // Severity
        JPanel severity = new JPanel(new FlowLayout(FlowLayout.LEFT)); severity.setBorder(new TitledBorder("Severity"));
        cbMild = new JCheckBox("Mild"); cbModerate = new JCheckBox("Moderate"); cbSevere = new JCheckBox("Severe"); severity.add(cbMild); severity.add(cbModerate); severity.add(cbSevere);

        // Recommendation
        JPanel recs = new JPanel(new FlowLayout(FlowLayout.LEFT)); recs.setBorder(new TitledBorder("Recommendation"));
        cbCast = new JCheckBox("Cast Needed"); cbSurgery = new JCheckBox("Surgery"); cbFollowUp = new JCheckBox("Follow-up Scan"); cbMRI = new JCheckBox("MRI Recommended");
        recs.add(cbCast); recs.add(cbSurgery); recs.add(cbFollowUp); recs.add(cbMRI);

        groups.add(findings); groups.add(location); groups.add(severity); groups.add(recs);

        top.add(groups, BorderLayout.CENTER);

        // notes
        JPanel notesP = new JPanel(new BorderLayout()); notesP.setBorder(new TitledBorder("Manual Notes"));
        notesArea = new JTextArea(6, 40); notesArea.setLineWrap(true); notesArea.setWrapStyleWord(true);
        notesP.add(new JScrollPane(notesArea), BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(notesP, BorderLayout.CENTER);

        // Wire upload action
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images/DICOM", "jpg","jpeg","png","dcm","dicom"));
                int r = fc.showOpenDialog(XrayPanel.this);
                if (r == JFileChooser.APPROVE_OPTION) {
                    fileLabel.setText(fc.getSelectedFile().getAbsolutePath());
                    // simple placeholder: optionally analyze the file name
                    analyzeFileNameForNotes(fc.getSelectedFile().getName());
                }
            }
        });
    }

    private void analyzeFileNameForNotes(String name) {
        String lower = name.toLowerCase();
        if (lower.contains("fracture") || lower.contains("broken") || lower.contains("fract")) cbFracture.setSelected(true);
        if (lower.contains("pneumonia")) cbPneumonia.setSelected(true);
        if (lower.contains("tumor") || lower.contains("mass")) cbTumor.setSelected(true);
        if (lower.contains("left")) cbLeftArm.setSelected(true);
        if (lower.contains("right")) cbRightLeg.setSelected(true);
    }
}
