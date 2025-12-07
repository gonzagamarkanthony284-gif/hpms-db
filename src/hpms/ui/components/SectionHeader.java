package hpms.ui.components;

import javax.swing.*;
import java.awt.*;

public class SectionHeader extends JPanel {
    public SectionHeader(String title, Icon icon) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Theme.BG);
        JLabel l = new JLabel(title, icon, JLabel.LEFT);
        l.setFont(Theme.APP_FONT.deriveFont(Font.BOLD, 18f));
        l.setForeground(Theme.TEXT);
        add(l);
    }
    public static SectionHeader info(String title) { return new SectionHeader(title, UIManager.getIcon("OptionPane.informationIcon")); }

    public SectionHeader(String title, String subtitle, Icon icon) {
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        JLabel l = new JLabel(title, icon, JLabel.LEFT);
        l.setFont(Theme.APP_FONT.deriveFont(Font.BOLD, 18f));
        l.setForeground(Theme.TEXT);
        JLabel sub = new JLabel(subtitle);
        sub.setFont(Theme.APP_FONT.deriveFont(Font.PLAIN, 12f));
        sub.setForeground(new java.awt.Color(110,110,120));
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); p.setBackground(Theme.BG); p.add(l); p.add(Box.createVerticalStrut(2)); p.add(sub);
        add(p, BorderLayout.CENTER);
    }

    public static SectionHeader info(String title, String subtitle) { return new SectionHeader(title, subtitle, UIManager.getIcon("OptionPane.informationIcon")); }
}

