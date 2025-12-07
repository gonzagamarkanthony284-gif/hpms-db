package hpms.ui.clinical;

import javax.swing.*;
import java.awt.*;

/**
 * Main frame with sidebar that loads the clinical panels into a center CardLayout area.
 * WindowBuilder-friendly: all components created in the constructor, pure Swing.
 */
public class ClinicalSystemMainFrame extends JFrame {
    private CardLayout cards;
    private JPanel center;

    public ClinicalSystemMainFrame() {
        super("Clinical Medical Test Upload System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 720);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel(); sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS)); sidebar.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        JButton btnXray = new JButton("X-ray Upload"); JButton btnStool = new JButton("Stool Exam"); JButton btnUrine = new JButton("Urinalysis"); JButton btnBlood = new JButton("Blood Test"); JButton btnDash = new JButton("Dashboard");
        Dimension btnSize = new Dimension(160, 32); btnXray.setMaximumSize(btnSize); btnStool.setMaximumSize(btnSize); btnUrine.setMaximumSize(btnSize); btnBlood.setMaximumSize(btnSize); btnDash.setMaximumSize(btnSize);
        sidebar.add(btnXray); sidebar.add(Box.createVerticalStrut(8)); sidebar.add(btnStool); sidebar.add(Box.createVerticalStrut(8)); sidebar.add(btnUrine); sidebar.add(Box.createVerticalStrut(8)); sidebar.add(btnBlood); sidebar.add(Box.createVerticalStrut(8)); sidebar.add(Box.createVerticalStrut(12)); sidebar.add(btnDash);

        // Center with CardLayout
        cards = new CardLayout(); center = new JPanel(cards);
        XrayPanel x = new XrayPanel(); StoolPanel s = new StoolPanel(); UrinePanel u = new UrinePanel(); BloodPanel b = new BloodPanel(); DashboardPanel d = new DashboardPanel();
        center.add(x, "XRAY"); center.add(s, "STOOL"); center.add(u, "URINE"); center.add(b, "BLOOD"); center.add(d, "DASHBOARD");

        // default card
        cards.show(center, "XRAY");

        // wire buttons
        btnXray.addActionListener(e -> cards.show(center, "XRAY"));
        btnStool.addActionListener(e -> cards.show(center, "STOOL"));
        btnUrine.addActionListener(e -> cards.show(center, "URINE"));
        btnBlood.addActionListener(e -> cards.show(center, "BLOOD"));
        btnDash.addActionListener(e -> cards.show(center, "DASHBOARD"));

        root.add(sidebar, BorderLayout.WEST);
        root.add(center, BorderLayout.CENTER);

        // footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT)); JButton close = new JButton("Close"); close.addActionListener(e -> dispose()); footer.add(close);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    public static void main(String[] args) {
        // Launch a standalone window for manual QA
        EventQueue.invokeLater(() -> {
            ClinicalSystemMainFrame f = new ClinicalSystemMainFrame();
            f.setVisible(true);
        });
    }
}
