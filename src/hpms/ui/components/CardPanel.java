package hpms.ui.components;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends JPanel {
    private int arc = 12;
    public CardPanel() {
        setOpaque(false);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // subtle shadow
        g2.setColor(new Color(0,0,0,8));
        g2.fillRoundRect(4,4,getWidth()-8,getHeight()-8, arc, arc);
        // card background
        g2.setColor(getBackground());
        g2.fillRoundRect(0,0,getWidth()-8,getHeight()-8, arc, arc);
        g2.dispose();
        super.paintComponent(g);
    }
}
