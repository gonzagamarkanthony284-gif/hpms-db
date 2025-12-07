package hpms.ui.clinical;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BloodPanel extends JPanel {
    private JLabel fileLabel;
    private JButton uploadButton;

    // CBC
    private JTextField tfWBC, tfHGB, tfPLT; private JRadioButton wbcNormal, wbcLow, wbcHigh; private JRadioButton hgbNormal, hgbLow, hgbHigh; private JRadioButton pltNormal, pltLow, pltHigh;
    // Chemistry
    private JTextField tfFBS, tfCreat, tfALT; private JRadioButton fbsNormal, fbsLow, fbsHigh; private JRadioButton crNormal, crLow, crHigh; private JRadioButton altNormal, altLow, altHigh;
    // Infection markers
    private JRadioButton crpNormal, crpElevated; private JRadioButton bcNoGrowth, bcPending, bcPositive;
    // Interpretation
    private JRadioButton interpNormal, interpAbnormal;
    // Critical flags
    private JCheckBox flagAnemia, flagInfection, flagDiabetes, flagKidneyLiver;
    // Action needed
    private JCheckBox actIV, actAntib, actRefer, actRepeat;
    private JTextArea notesArea;

    public BloodPanel() {
        setLayout(new BorderLayout(8,8)); setBorder(new TitledBorder("Blood Test"));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT)); uploadButton = new JButton("Choose Blood File"); fileLabel = new JLabel("No file chosen"); top.add(uploadButton); top.add(fileLabel);

        JPanel center = new JPanel(new GridLayout(4,1,8,8));

        // CBC panel
        JPanel cbc = new JPanel(new FlowLayout(FlowLayout.LEFT)); cbc.setBorder(new TitledBorder("CBC"));
        cbc.add(new JLabel("WBC:")); tfWBC = new JTextField(6); cbc.add(tfWBC); wbcNormal = new JRadioButton("Normal"); wbcLow = new JRadioButton("Low"); wbcHigh = new JRadioButton("High"); ButtonGroup bgW = new ButtonGroup(); bgW.add(wbcNormal); bgW.add(wbcLow); bgW.add(wbcHigh); cbc.add(wbcNormal); cbc.add(wbcLow); cbc.add(wbcHigh);
        cbc.add(new JLabel("HGB:")); tfHGB = new JTextField(6); cbc.add(tfHGB); hgbNormal = new JRadioButton("Normal"); hgbLow = new JRadioButton("Low"); hgbHigh = new JRadioButton("High"); ButtonGroup bgH = new ButtonGroup(); bgH.add(hgbNormal); bgH.add(hgbLow); bgH.add(hgbHigh); cbc.add(hgbNormal); cbc.add(hgbLow); cbc.add(hgbHigh);
        cbc.add(new JLabel("PLT:")); tfPLT = new JTextField(6); cbc.add(tfPLT); pltNormal = new JRadioButton("Normal"); pltLow = new JRadioButton("Low"); pltHigh = new JRadioButton("High"); ButtonGroup bgP = new ButtonGroup(); bgP.add(pltNormal); bgP.add(pltLow); bgP.add(pltHigh); cbc.add(pltNormal); cbc.add(pltLow); cbc.add(pltHigh);

        // Chemistry panel
        JPanel chem = new JPanel(new FlowLayout(FlowLayout.LEFT)); chem.setBorder(new TitledBorder("Chemistry"));
        chem.add(new JLabel("FBS:")); tfFBS = new JTextField(6); chem.add(tfFBS); fbsNormal = new JRadioButton("Normal"); fbsLow = new JRadioButton("Low"); fbsHigh = new JRadioButton("High"); ButtonGroup bgF = new ButtonGroup(); bgF.add(fbsNormal); bgF.add(fbsLow); bgF.add(fbsHigh); chem.add(fbsNormal); chem.add(fbsLow); chem.add(fbsHigh);
        chem.add(new JLabel("Creatinine:")); tfCreat = new JTextField(6); chem.add(tfCreat); crNormal = new JRadioButton("Normal"); crLow = new JRadioButton("Low"); crHigh = new JRadioButton("High"); ButtonGroup bgCr = new ButtonGroup(); bgCr.add(crNormal); bgCr.add(crLow); bgCr.add(crHigh); chem.add(crNormal); chem.add(crLow); chem.add(crHigh);
        chem.add(new JLabel("ALT:")); tfALT = new JTextField(6); chem.add(tfALT); altNormal = new JRadioButton("Normal"); altLow = new JRadioButton("Low"); altHigh = new JRadioButton("High"); ButtonGroup bgAlt = new ButtonGroup(); bgAlt.add(altNormal); bgAlt.add(altLow); bgAlt.add(altHigh); chem.add(altNormal); chem.add(altLow); chem.add(altHigh);

        // Infection / interpretation
        JPanel inf = new JPanel(new FlowLayout(FlowLayout.LEFT)); inf.setBorder(new TitledBorder("Infection Markers & Interpretation"));
        crpNormal = new JRadioButton("CRP/ESR: Normal"); crpElevated = new JRadioButton("CRP/ESR: Elevated"); ButtonGroup bgCRP = new ButtonGroup(); bgCRP.add(crpNormal); bgCRP.add(crpElevated); inf.add(crpNormal); inf.add(crpElevated);
        bcNoGrowth = new JRadioButton("Blood Culture: No Growth"); bcPending = new JRadioButton("Pending"); bcPositive = new JRadioButton("Positive"); ButtonGroup bgBC = new ButtonGroup(); bgBC.add(bcNoGrowth); bgBC.add(bcPending); bgBC.add(bcPositive); inf.add(bcNoGrowth); inf.add(bcPending); inf.add(bcPositive);
        interpNormal = new JRadioButton("Interpret: Normal"); interpAbnormal = new JRadioButton("Interpret: Abnormal"); ButtonGroup bgI = new ButtonGroup(); bgI.add(interpNormal); bgI.add(interpAbnormal); inf.add(interpNormal); inf.add(interpAbnormal);

        // flags and actions
        JPanel flags = new JPanel(new FlowLayout(FlowLayout.LEFT)); flags.setBorder(new TitledBorder("Critical Flags / Actions"));
        flagAnemia = new JCheckBox("Anemia"); flagInfection = new JCheckBox("Infection"); flagDiabetes = new JCheckBox("Diabetes"); flagKidneyLiver = new JCheckBox("Kidney/Liver Issue");
        actIV = new JCheckBox("IV Fluids"); actAntib = new JCheckBox("Antibiotics"); actRefer = new JCheckBox("Specialist Referral"); actRepeat = new JCheckBox("Repeat Test");
        flags.add(flagAnemia); flags.add(flagInfection); flags.add(flagDiabetes); flags.add(flagKidneyLiver); flags.add(actIV); flags.add(actAntib); flags.add(actRefer); flags.add(actRepeat);

        center.add(cbc); center.add(chem); center.add(inf); center.add(flags);

        notesArea = new JTextArea(4,40); notesArea.setLineWrap(true); notesArea.setWrapStyleWord(true);
        JPanel notes = new JPanel(new BorderLayout()); notes.setBorder(new TitledBorder("Notes & Recommendation")); notes.add(new JScrollPane(notesArea), BorderLayout.CENTER);

        add(top, BorderLayout.NORTH); add(center, BorderLayout.CENTER); add(notes, BorderLayout.SOUTH);

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser(); fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF/Images", "pdf","jpg","jpeg","png"));
                int r = fc.showOpenDialog(BloodPanel.this);
                if (r == JFileChooser.APPROVE_OPTION) { fileLabel.setText(fc.getSelectedFile().getAbsolutePath()); }
            }
        });
    }
}
