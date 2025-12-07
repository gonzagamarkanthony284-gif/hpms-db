package hpms.ui.clinical;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import hpms.model.Patient;
import hpms.util.DataStore;
import hpms.service.PatientService;

public class DashboardPanel extends JPanel implements PatientService.ClinicalUpdateListener {
    private JTable table;
    private DefaultTableModel model;
    private JTextArea overallAssessment;
    private JTextField reviewingDoctor;
    private JTextArea finalDiagnosis, treatmentPlan;
    private JTextField signatureField, dateField;

    public DashboardPanel() {
        setLayout(new BorderLayout(6,6));
        setBorder(new TitledBorder("Patient Summary Dashboard"));

        model = new DefaultTableModel(new String[]{"Test", "Status", "Key Finding", "Action"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        // add default rows
        model.addRow(new Object[]{"X-ray", "Not Uploaded", "-", "-"});
        model.addRow(new Object[]{"Stool Exam", "Not Uploaded", "-", "-"});
        model.addRow(new Object[]{"Urinalysis", "Not Uploaded", "-", "-"});
        model.addRow(new Object[]{"Blood Test", "Not Uploaded", "-", "-"});

        add(new JScrollPane(table), BorderLayout.NORTH);

        JPanel mid = new JPanel(new BorderLayout(6,6));
        mid.setBorder(new TitledBorder("Assessment & Alerts"));
        overallAssessment = new JTextArea(5, 60); overallAssessment.setLineWrap(true); overallAssessment.setWrapStyleWord(true);
        mid.add(new JScrollPane(overallAssessment), BorderLayout.CENTER);

        JPanel alerts = new JPanel(new FlowLayout(FlowLayout.LEFT));
        alerts.add(new JLabel("\uD83D\uDFE2 NORMAL")); alerts.add(new JLabel("\uD83D\uDFE1 WARNING")); alerts.add(new JLabel("\uD83D\uDD34 CRITICAL"));
        mid.add(alerts, BorderLayout.SOUTH);

        add(mid, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(2,1,6,6));
        JPanel review = new JPanel(new GridLayout(1,2,6,6)); review.setBorder(new TitledBorder("Physician Review"));
        JPanel left = new JPanel(new BorderLayout()); reviewingDoctor = new JTextField(30); left.add(new JLabel("Reviewing Doctor"), BorderLayout.NORTH); left.add(reviewingDoctor, BorderLayout.CENTER);
        JPanel right = new JPanel(new BorderLayout()); finalDiagnosis = new JTextArea(4,30); finalDiagnosis.setLineWrap(true); finalDiagnosis.setWrapStyleWord(true); right.add(new JLabel("Final Diagnosis"), BorderLayout.NORTH); right.add(new JScrollPane(finalDiagnosis), BorderLayout.CENTER);
        review.add(left); review.add(right);

        JPanel planSig = new JPanel(new GridLayout(1,2,6,6)); JPanel plan = new JPanel(new BorderLayout()); treatmentPlan = new JTextArea(4,30); treatmentPlan.setLineWrap(true); treatmentPlan.setWrapStyleWord(true); plan.add(new JLabel("Treatment Plan"), BorderLayout.NORTH); plan.add(new JScrollPane(treatmentPlan), BorderLayout.CENTER);
        JPanel sig = new JPanel(new GridLayout(3,1,6,6)); signatureField = new JTextField(20); dateField = new JTextField(20); sig.add(new JLabel("Signature")); sig.add(signatureField); sig.add(new JLabel("Date")); sig.add(dateField);
        planSig.add(plan); planSig.add(sig);

        bottom.add(review); bottom.add(planSig);

        add(bottom, BorderLayout.SOUTH);

        // register for clinical updates so the dashboard auto-refreshes
        try { PatientService.addClinicalUpdateListener(this); } catch (Exception ex) { /* ignore */ }
    }

    // Optional: Public API methods to update table programmatically
    public void updateTestRow(int rowIndex, String status, String finding, String action) {
        model.setValueAt(status, rowIndex, 1);
        model.setValueAt(finding, rowIndex, 2);
        model.setValueAt(action, rowIndex, 3);
    }

    @Override
    public void clinicalUpdated(String patientId) {
        // simple refresh logic: lookup patient and update rows
        SwingUtilities.invokeLater(() -> {
            try {
                Patient p = DataStore.patients.get(patientId);
                if (p == null) return;
                String xrStat = p.xrayStatus==null?"Not Uploaded":p.xrayStatus; String xrFind = p.xraySummary==null?"-":p.xraySummary; String xrAct = xrStat.contains("Critical")?"Notify Clinician":"Review";
                updateTestRow(0, xrStat, xrFind, xrAct);

                String stStat = p.stoolStatus==null?"Not Uploaded":p.stoolStatus; String stFind = p.stoolSummary==null?"-":p.stoolSummary; String stAct = stStat.contains("Critical")?"Treat/Refer":(stFind.contains("E. coli")?"Antibiotics":"Review");
                updateTestRow(1, stStat, stFind, stAct);

                String urStat = p.urineStatus==null?"Not Uploaded":p.urineStatus; String urFind = p.urineSummary==null?"-":p.urineSummary; String urAct = urStat.contains("Critical")?"Urgent":(urFind.toLowerCase().contains("uti")?"Culture/Antibiotics":"Review");
                updateTestRow(2, urStat, urFind, urAct);

                String blStat = p.bloodStatus==null?"Not Uploaded":p.bloodStatus; String blFind = p.bloodSummary==null?"-":p.bloodSummary; String blAct = blStat.contains("Critical")?"Immediate Care":(blFind.toLowerCase().contains("wbc")?"Infection workup":"Review");
                updateTestRow(3, blStat, blFind, blAct);

                // small overall assessment: list critical items
                StringBuilder overall = new StringBuilder();
                if (xrStat.contains("Critical") || stStat.contains("Critical") || urStat.contains("Critical") || blStat.contains("Critical")) overall.append("CRITICAL findings present\n");
                if (overall.length()==0) overall.append("No critical flags detected. Review all notes for treatment plan.");
                overallAssessment.setText(overall.toString());
            } catch (Exception ex) { /* swallow UI update errors */ }
        });
    }
}
