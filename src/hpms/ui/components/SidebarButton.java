package hpms.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarButton extends JButton {
    private boolean selected;

    public SidebarButton(String text) {
        super(text);
        setOpaque(true);
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        setHorizontalAlignment(SwingConstants.LEFT);
        setFont(Theme.APP_FONT);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected) {
                    setBackground(Theme.HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selected) {
                    setBackground(Theme.BACKGROUND);
                }
            }
        });
    }

    public void setSelectedState(boolean sel) {
        this.selected = sel;
        if (sel) {
            setBackground(Theme.SELECTED_BACKGROUND);
            setForeground(Theme.SELECTED_FOREGROUND);
            // left accent bar for modern look
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,6,0,0, Theme.PRIMARY), BorderFactory.createEmptyBorder(12,14,12,14)));
        } else {
            setBackground(Theme.BACKGROUND);
            setForeground(Theme.FOREGROUND);
            setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        }
    }
}
