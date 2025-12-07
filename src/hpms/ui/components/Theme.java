package hpms.ui.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Modern theme system for HPMS with contemporary color palette and component styling
 */
public class Theme {
    // Modern, professional color palette inspired by Material Design & modern web design
    public static final Color PRIMARY = new Color(59, 130, 246); // Vibrant blue
    public static final Color PRIMARY_DARK = new Color(37, 99, 235); // Darker blue for hover/focus
    public static final Color PRIMARY_LIGHT = new Color(191, 219, 254); // Light blue for backgrounds
    
    public static final Color SECONDARY = new Color(107, 114, 128); // Cool gray
    public static final Color SECONDARY_LIGHT = new Color(229, 231, 235); // Light gray
    
    public static final Color SUCCESS = new Color(34, 197, 94); // Green
    public static final Color WARNING = new Color(251, 146, 60); // Orange
    public static final Color DANGER = new Color(239, 68, 68); // Red
    public static final Color INFO = new Color(59, 130, 246); // Blue
    
    // Background and Text colors
    public static final Color BACKGROUND = new Color(249, 250, 251); // Nearly white, slightly warm
    public static final Color SURFACE = new Color(255, 255, 255); // Pure white for cards/panels
    public static final Color FOREGROUND = new Color(15, 23, 42); // Very dark gray/slate
    public static final Color TEXT_SECONDARY = new Color(71, 85, 105); // Medium gray
    public static final Color TEXT_TERTIARY = new Color(148, 163, 184); // Light gray
    
    public static final Color BORDER = new Color(226, 232, 240); // Light border
    public static final Color BORDER_LIGHT = new Color(241, 245, 249); // Very light border
    public static final Color HOVER = new Color(241, 245, 249); // Light hover effect
    public static final Color FOCUS = new Color(219, 234, 254); // Very light blue for focus
    
    public static final Color SELECTED_BACKGROUND = PRIMARY;
    public static final Color SELECTED_FOREGROUND = Color.WHITE;
    
    public static final Color SHADOW = new Color(0, 0, 0, 15); // Shadow color with transparency
    public static final Color SHADOW_HOVER = new Color(0, 0, 0, 25); // Darker shadow on hover

    // Legacy color names for backward compatibility
    public static final Color ACCENT = PRIMARY;
    public static final Color BG = BACKGROUND;
    public static final Color TEXT = FOREGROUND;

    // Modern fonts
    public static final Font APP_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font APP_FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font HEADING_1 = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font HEADING_2 = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font HEADING_3 = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font HEADING_4 = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 11);

    public static void applyGlobalUI() {
        UIManager.put("Label.font", APP_FONT);
        UIManager.put("Button.font", APP_FONT);
        UIManager.put("TextField.font", APP_FONT);
        UIManager.put("ComboBox.font", APP_FONT);
        UIManager.put("Table.font", APP_FONT);
        UIManager.put("TableHeader.font", APP_FONT_BOLD);
        UIManager.put("OptionPane.messageFont", APP_FONT);
        UIManager.put("OptionPane.buttonFont", APP_FONT);
        UIManager.put("Control", BACKGROUND);
        UIManager.put("text", FOREGROUND);
    }

    // Modern button styling with elevation effect
    public static void stylePrimary(JButton b) {
        b.setBackground(PRIMARY);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(APP_FONT_BOLD);
        b.setBorder(createModernButtonBorder());
        b.setOpaque(false);
        b.setContentAreaFilled(true);
    }

    public static void styleSecondary(JButton b) {
        b.setBackground(SECONDARY);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(APP_FONT_BOLD);
        b.setBorder(createModernButtonBorder());
    }

    public static void styleSuccess(JButton b) {
        b.setBackground(SUCCESS);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(APP_FONT_BOLD);
        b.setBorder(createModernButtonBorder());
    }

    public static void styleDanger(JButton b) {
        b.setBackground(DANGER);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(APP_FONT_BOLD);
        b.setBorder(createModernButtonBorder());
    }

    public static void styleOutline(JButton b) {
        b.setBackground(Color.WHITE);
        b.setForeground(PRIMARY);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(APP_FONT_BOLD);
        b.setBorder(new LineBorder(PRIMARY, 2, true));
    }

    public static Border createModernButtonBorder() {
        return BorderFactory.createEmptyBorder(10, 20, 10, 20);
    }

    public static Border createModernCardBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );
    }

    public static Border createModernCardBorderHeavy() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 2, false),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );
    }

    public static void styleTextField(JTextField f) {
        f.setBackground(SURFACE);
        f.setForeground(FOREGROUND);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        f.setFont(APP_FONT);
        f.setCaretColor(PRIMARY);
    }

    public static void stylePasswordField(JPasswordField f) {
        f.setBackground(SURFACE);
        f.setForeground(FOREGROUND);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        f.setFont(APP_FONT);
        f.setCaretColor(PRIMARY);
    }

    public static void styleComboBox(JComboBox<?> cb) {
        cb.setBackground(SURFACE);
        cb.setForeground(FOREGROUND);
        cb.setBorder(new LineBorder(BORDER, 1, true));
        cb.setFont(APP_FONT);
    }

    public static void styleTableHeader(javax.swing.table.JTableHeader h) {
        h.setBackground(PRIMARY_LIGHT);
        h.setForeground(PRIMARY_DARK);
        h.setReorderingAllowed(false);
        h.setFont(APP_FONT_BOLD);
    }

    public static void styleTable(JTable t) {
        t.setBackground(SURFACE);
        t.setForeground(FOREGROUND);
        t.setSelectionBackground(PRIMARY_LIGHT);
        t.setSelectionForeground(FOREGROUND);
        t.setGridColor(BORDER_LIGHT);
        t.setRowHeight(28);
        t.setFont(APP_FONT);
        styleTableHeader(t.getTableHeader());
    }

    public static void styleScrollPane(JScrollPane sp) {
        sp.setBorder(new LineBorder(BORDER, 1, false));
        sp.getViewport().setBackground(SURFACE);
    }

    // Helper method for creating shadow/elevation effect
    public static Border createShadowBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(SHADOW, 1, false),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        );
    }

    // Gradient paint utilities for advanced styling
    public static Paint createGradientPaint(int width, int height) {
        return new GradientPaint(0, 0, PRIMARY_LIGHT, width, height, PRIMARY, true);
    }
}
