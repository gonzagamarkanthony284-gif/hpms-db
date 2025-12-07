package hpms.ui.clinical;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StoolPanel extends JPanel {
    private JLabel fileLabel;
    private JButton uploadButton;

    private JRadioButton rbParasitesNone, rbParasitesPresent;
    private JTextField parasitesDetail;
    private JRadioButton rbBacteriaNormal, rbBacteriaAbnormal;
    private JTextField bacteriaDetail;
    private JRadioButton rbOccultPos, rbOccultNeg;
    private JComboBox<String> consistencyCombo;
    private JRadioButton rbInterpretNormal, rbInterpretAbnormal;
    private JCheckBox cbActionAntibiotics, cbActionRetest, cbActionRefer;
    private JTextArea notesArea;

    public StoolPanel() {
        setLayout(new BorderLayout(8,8));
        setBorder(new TitledBorder("Stool Exam"));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        uploadButton = new JButton("Choose Stool File"); fileLabel = new JLabel("No file chosen"); top.add(uploadButton); top.add(fileLabel);

        JPanel content = new JPanel(new GridLayout(3,1,8,8));

        // Parasites row
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); p1.setBorder(new TitledBorder("Parasites"));
        rbParasitesNone = new JRadioButton("None"); rbParasitesPresent = new JRadioButton("Present"); parasitesDetail = new JTextField(20);
        ButtonGroup bgPar = new ButtonGroup(); bgPar.add(rbParasitesNone); bgPar.add(rbParasitesPresent);
        p1.add(rbParasitesNone); p1.add(rbParasitesPresent); p1.add(new JLabel("Details:")); p1.add(parasitesDetail);

        // Bacteria & Occult
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT)); p2.setBorder(new TitledBorder("Bacteria / Occult"));
        rbBacteriaNormal = new JRadioButton("Normal"); rbBacteriaAbnormal = new JRadioButton("Abnormal"); bacteriaDetail = new JTextField(20);
        ButtonGroup bgBac = new ButtonGroup(); bgBac.add(rbBacteriaNormal); bgBac.add(rbBacteriaAbnormal);
        rbOccultPos = new JRadioButton("Occult: Positive"); rbOccultNeg = new JRadioButton("Occult: Negative"); ButtonGroup bgOcc = new ButtonGroup(); bgOcc.add(rbOccultPos); bgOcc.add(rbOccultNeg);
        p2.add(rbBacteriaNormal); p2.add(rbBacteriaAbnormal); p2.add(new JLabel("Example:")); p2.add(bacteriaDetail); p2.add(rbOccultPos); p2.add(rbOccultNeg);

        // Interpretation / Action
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT)); p3.setBorder(new TitledBorder("Interpretation & Action"));
        consistencyCombo = new JComboBox<>(new String[]{"Normal","Diarrhea","Constipated"});
        rbInterpretNormal = new JRadioButton("Interpret: Normal"); rbInterpretAbnormal = new JRadioButton("Interpret: Abnormal - Requires treatment"); ButtonGroup bgInt = new ButtonGroup(); bgInt.add(rbInterpretNormal); bgInt.add(rbInterpretAbnormal);
        cbActionAntibiotics = new JCheckBox("Antibiotics"); cbActionRetest = new JCheckBox("Re-test"); cbActionRefer = new JCheckBox("Refer to GI Specialist");
        p3.add(new JLabel("Consistency:")); p3.add(consistencyCombo); p3.add(rbInterpretNormal); p3.add(rbInterpretAbnormal); p3.add(cbActionAntibiotics); p3.add(cbActionRetest); p3.add(cbActionRefer);

        content.add(p1); content.add(p2); content.add(p3);

        notesArea = new JTextArea(4, 40); notesArea.setLineWrap(true); notesArea.setWrapStyleWord(true);
        JPanel notes = new JPanel(new BorderLayout()); notes.setBorder(new TitledBorder("Notes")); notes.add(new JScrollPane(notesArea), BorderLayout.CENTER);

        add(top, BorderLayout.NORTH); add(content, BorderLayout.CENTER); add(notes, BorderLayout.SOUTH);

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF/Images", "pdf","jpg","jpeg","png"));
                int r = fc.showOpenDialog(StoolPanel.this);
                if (r == JFileChooser.APPROVE_OPTION) {
                    fileLabel.setText(fc.getSelectedFile().getAbsolutePath());
                    analyzeFilename(fc.getSelectedFile().getName());
                }
            }
        });
    }

    private void analyzeFilename(String name) {
        String lower = name.toLowerCase();
        if (lower.contains("e.coli") || lower.contains("ecoli")) { rbBacteriaAbnormal.setSelected(true); bacteriaDetail.setText("E. coli detected"); }
        if (lower.contains("parasite")) { rbParasitesPresent.setSelected(true); parasitesDetail.setText("Parasite identified"); }
        if (lower.contains("occult")) { rbOccultPos.setSelected(true); }
    }
}
