package hpms.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IconButton extends JButton {
    public IconButton(String text, Icon icon) {
        super(text, icon);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.TOP);
        setIconTextGap(6);
        // consistent button size so icons align nicely in toolbars
        setPreferredSize(new Dimension(72, 72));
        setOpaque(true);
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
        setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFocusPainted(false);
        setFont(Theme.APP_FONT);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { setBackground(Theme.HOVER); }
            @Override
            public void mouseExited(MouseEvent e) { setBackground(Theme.BACKGROUND); }
            @Override
            public void mousePressed(MouseEvent e) { setBackground(Theme.PRIMARY.darker()); setForeground(Color.WHITE); }
            @Override
            public void mouseReleased(MouseEvent e) { setBackground(Theme.HOVER); setForeground(Theme.FOREGROUND); }
        });
    }
}
