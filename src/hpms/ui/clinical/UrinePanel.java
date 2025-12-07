package hpms.ui.clinical;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UrinePanel extends JPanel {
    private JLabel fileLabel;
    private JButton uploadButton;

    private JRadioButton rbClear, rbCloudy, rbBlood;
    private JRadioButton rbGlucosePos, rbGlucoseNeg;
    private JRadioButton rbProteinPos, rbProteinNeg;
    private JRadioButton rbWBCNormal, rbWBCHigh;
    private JRadioButton rbRBCNormal, rbRBCHigh;
    private JRadioButton rbInterpretNormal, rbInterpretUTI;
    private JCheckBox cbStartAntibiotics, cbUrineCulture, cbFollowUp;
    private JTextArea notesArea;

    public UrinePanel() {
        setLayout(new BorderLayout(8,8));
        setBorder(new TitledBorder("Urinalysis"));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        uploadButton = new JButton("Choose Urine File"); fileLabel = new JLabel("No file chosen"); top.add(uploadButton); top.add(fileLabel);

        JPanel content = new JPanel(new GridLayout(3,1,8,8));

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); p1.setBorder(new TitledBorder("Color/Clarity / Glucose / Protein"));
        rbClear = new JRadioButton("Clear"); rbCloudy = new JRadioButton("Cloudy"); rbBlood = new JRadioButton("Blood-tinged"); ButtonGroup bgColor = new ButtonGroup(); bgColor.add(rbClear); bgColor.add(rbCloudy); bgColor.add(rbBlood);
        rbGlucosePos = new JRadioButton("Glucose Positive"); rbGlucoseNeg = new JRadioButton("Glucose Negative"); ButtonGroup bgG = new ButtonGroup(); bgG.add(rbGlucoseNeg); bgG.add(rbGlucosePos);
        rbProteinPos = new JRadioButton("Protein Positive"); rbProteinNeg = new JRadioButton("Protein Negative"); ButtonGroup bgP = new ButtonGroup(); bgP.add(rbProteinNeg); bgP.add(rbProteinPos);
        p1.add(rbClear); p1.add(rbCloudy); p1.add(rbBlood); p1.add(rbGlucoseNeg); p1.add(rbGlucosePos); p1.add(rbProteinNeg); p1.add(rbProteinPos);

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT)); p2.setBorder(new TitledBorder("Cells"));
        rbWBCNormal = new JRadioButton("WBC Normal"); rbWBCHigh = new JRadioButton("WBC High"); ButtonGroup bgWBC = new ButtonGroup(); bgWBC.add(rbWBCNormal); bgWBC.add(rbWBCHigh);
        rbRBCNormal = new JRadioButton("RBC Normal"); rbRBCHigh = new JRadioButton("RBC High"); ButtonGroup bgRBC = new ButtonGroup(); bgRBC.add(rbRBCNormal); bgRBC.add(rbRBCHigh);
        p2.add(rbWBCNormal); p2.add(rbWBCHigh); p2.add(rbRBCNormal); p2.add(rbRBCHigh);

        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT)); p3.setBorder(new TitledBorder("Interpretation & Action"));
        rbInterpretNormal = new JRadioButton("Interpret: Normal"); rbInterpretUTI = new JRadioButton("Interpret: UTI Suspected"); ButtonGroup bgInt = new ButtonGroup(); bgInt.add(rbInterpretNormal); bgInt.add(rbInterpretUTI);
        cbStartAntibiotics = new JCheckBox("Start Antibiotics"); cbUrineCulture = new JCheckBox("Urine Culture"); cbFollowUp = new JCheckBox("Follow-up");
        p3.add(rbInterpretNormal); p3.add(rbInterpretUTI); p3.add(cbStartAntibiotics); p3.add(cbUrineCulture); p3.add(cbFollowUp);

        content.add(p1); content.add(p2); content.add(p3);

        notesArea = new JTextArea(4, 40); notesArea.setLineWrap(true); notesArea.setWrapStyleWord(true);
        JPanel notes = new JPanel(new BorderLayout()); notes.setBorder(new TitledBorder("Notes")); notes.add(new JScrollPane(notesArea), BorderLayout.CENTER);

        add(top, BorderLayout.NORTH); add(content, BorderLayout.CENTER); add(notes, BorderLayout.SOUTH);

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF/Images", "pdf","jpg","jpeg","png"));
                int r = fc.showOpenDialog(UrinePanel.this);
                if (r == JFileChooser.APPROVE_OPTION) {
                    fileLabel.setText(fc.getSelectedFile().getAbsolutePath());
                    // no heavy processing here, optional heuristics
                    String n = fc.getSelectedFile().getName().toLowerCase();
                    if (n.contains("cloudy")) rbCloudy.setSelected(true);
                    if (n.contains("blood")) rbBlood.setSelected(true);
                }
            }
        });
    }
}
