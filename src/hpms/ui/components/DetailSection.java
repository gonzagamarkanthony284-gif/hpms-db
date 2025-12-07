package hpms.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Small shared helpers for section headers and content blocks used in vertical BoxLayouts.
 * Ensures consistent left alignment and sizing across dialogs and panels.
 */
public class DetailSection {
    public static JLabel createHeader(String text) {
        JLabel header = new JLabel(text);
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setForeground(new Color(20, 80, 80));
        header.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setHorizontalAlignment(SwingConstants.LEFT);
        return header;
    }

    public static JComponent createContent(String txt) {
        JTextArea ta = new JTextArea(txt == null || txt.isEmpty() ? "-" : txt);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(false);
        ta.setBackground(new Color(250, 250, 250));
        ta.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        ta.setAlignmentX(Component.LEFT_ALIGNMENT);
        ta.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return ta;
    }
}
