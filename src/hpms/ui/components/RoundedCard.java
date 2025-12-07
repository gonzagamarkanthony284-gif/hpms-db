package hpms.ui.components;

import javax.swing.*;
import java.awt.*;

public class RoundedCard extends JPanel {
    private int radius = 15;
    private Color backgroundColor = Color.WHITE;
    private int shadowSize = 5;

    public RoundedCard() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(shadowSize, shadowSize, shadowSize, shadowSize));
    }

    public RoundedCard(int radius) {
        this();
        this.radius = radius;
    }

    public RoundedCard(Color bgColor) {
        this();
        this.backgroundColor = bgColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw shadow
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillRoundRect(shadowSize, shadowSize, width - shadowSize * 2, height - shadowSize * 2, radius, radius);

        // Draw card
        g2.setColor(backgroundColor);
        g2.fillRoundRect(shadowSize, shadowSize, width - shadowSize * 2 - 1, height - shadowSize * 2 - 1, radius, radius);

        // Draw border
        g2.setColor(Theme.BORDER);
        g2.drawRoundRect(shadowSize, shadowSize, width - shadowSize * 2 - 1, height - shadowSize * 2 - 1, radius, radius);

        g2.dispose();
    }
}
