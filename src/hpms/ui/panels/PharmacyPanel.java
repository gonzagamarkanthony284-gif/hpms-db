package hpms.ui.panels;

import hpms.model.Medicine;
import hpms.service.MedicineService;
import hpms.ui.components.Theme;
import hpms.util.DataStore;
import hpms.util.IDGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PharmacyPanel extends JPanel {
    private JTable medicineTable;
    private DefaultTableModel tableModel;
    private JLabel totalMedicinesLabel;
    private JLabel lowStockLabel;
    private JTextField searchField;

    public PharmacyPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BACKGROUND);

        // Header with stats
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Middle section with search and table
        JPanel middlePanel = createMiddlePanel();
        add(middlePanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Pharmacy Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Theme.FOREGROUND);
        panel.add(titleLabel, BorderLayout.WEST);

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        statsPanel.setBackground(Theme.BACKGROUND);

        totalMedicinesLabel = createStatLabel("Total Medicines: 0", Theme.PRIMARY);
        lowStockLabel = createStatLabel("Low Stock: 0", new Color(255, 193, 7));

        statsPanel.add(totalMedicinesLabel);
        statsPanel.add(lowStockLabel);
        panel.add(statsPanel, BorderLayout.EAST);

        return panel;
    }

    private JLabel createStatLabel(String text, Color bgColor) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.WHITE);
        label.setBackground(bgColor);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JPanel createMiddlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Theme.BACKGROUND);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Theme.FOREGROUND);
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filterTable();
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Medicine Name", "Generic Name", "Manufacturer", "Price", "Stock", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        medicineTable = new JTable(tableModel);
        medicineTable.setFont(new Font("Arial", Font.PLAIN, 11));
        medicineTable.setRowHeight(25);
        medicineTable.getTableHeader().setBackground(Theme.PRIMARY);
        medicineTable.getTableHeader().setForeground(Color.WHITE);
        medicineTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(medicineTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addBtn = createButton("Add Medicine", Theme.PRIMARY);
        addBtn.addActionListener(e -> showAddMedicineDialog());

        JButton updateStockBtn = createButton("Update Stock", new Color(33, 150, 243));
        updateStockBtn.addActionListener(e -> showUpdateStockDialog());

        JButton lowStockBtn = createButton("Low Stock Alert", new Color(255, 193, 7));
        lowStockBtn.addActionListener(e -> showLowStockMedicines());

        JButton deleteBtn = createButton("Delete", new Color(244, 67, 54));
        deleteBtn.addActionListener(e -> deleteMedicine());

        JButton refreshBtn = createButton("Refresh", new Color(76, 175, 80));
        refreshBtn.addActionListener(e -> refreshTable());

        panel.add(addBtn);
        panel.add(updateStockBtn);
        panel.add(lowStockBtn);
        panel.add(deleteBtn);
        panel.add(refreshBtn);

        return panel;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showAddMedicineDialog() {
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField();
        JTextField genericNameField = new JTextField();
        JTextField manufacturerField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();
        JTextField dosageField = new JTextField();
        JTextField strengthField = new JTextField();
        JTextField expireDateField = new JTextField("YYYY-MM-DD");

        formPanel.add(new JLabel("Medicine Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Generic Name:"));
        formPanel.add(genericNameField);
        formPanel.add(new JLabel("Manufacturer:"));
        formPanel.add(manufacturerField);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Stock Quantity:"));
        formPanel.add(stockField);
        formPanel.add(new JLabel("Dosage Form:"));
        formPanel.add(dosageField);
        formPanel.add(new JLabel("Strength:"));
        formPanel.add(strengthField);
        formPanel.add(new JLabel("Expire Date:"));
        formPanel.add(expireDateField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Add New Medicine", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            if (nameField.getText().isEmpty() || priceField.getText().isEmpty() || stockField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = IDGenerator.nextId("M");
            Medicine med = new Medicine(id, nameField.getText().trim(),
                    manufacturerField.getText().trim(), Double.parseDouble(priceField.getText()));

            DataStore.medicines.put(id, med);
            JOptionPane.showMessageDialog(this, "Medicine added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        }
    }

    private void showUpdateStockDialog() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a medicine", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String medicineId = (String) tableModel.getValueAt(selectedRow, 0);
        Medicine med = DataStore.medicines.get(medicineId);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel currentLabel = new JLabel("Current Stock: " + med.stockQuantity);
        JTextField quantityField = new JTextField();

        panel.add(currentLabel);
        panel.add(new JLabel(""));
        panel.add(new JLabel("New Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Stock", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int newQuantity = Integer.parseInt(quantityField.getText());
                med.stockQuantity = newQuantity;
                JOptionPane.showMessageDialog(this, "Stock updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showLowStockMedicines() {
        List<String> lowStockMeds = MedicineService.getLowStockMedicines();
        if (lowStockMeds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No low stock medicines", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder("Low Stock Medicines:\n\n");
            for (String med : lowStockMeds) {
                sb.append(med).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Low Stock Alert", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteMedicine() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a medicine", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String medicineId = (String) tableModel.getValueAt(selectedRow, 0);
        DataStore.medicines.remove(medicineId);
        JOptionPane.showMessageDialog(this, "Medicine deleted successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
        refreshTable();
    }

    private void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        DefaultTableModel filteredModel = new DefaultTableModel(
                new Object[] { "ID", "Medicine Name", "Generic Name", "Manufacturer", "Price", "Stock", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Medicine med : DataStore.medicines.values()) {
            if (med.name.toLowerCase().contains(searchText) || med.genericName.toLowerCase().contains(searchText)) {
                String status = med.needsRestocking() ? "LOW STOCK" : "OK";
                filteredModel.addRow(new Object[] { med.id, med.name, med.genericName, med.manufacturer,
                        String.format("Rs. %.2f", med.price), med.stockQuantity, status });
            }
        }

        medicineTable.setModel(filteredModel);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        int totalMeds = 0;
        int lowStockCount = 0;

        for (Medicine med : DataStore.medicines.values()) {
            totalMeds++;
            String status = "OK";
            if (med.needsRestocking()) {
                status = "LOW STOCK";
                lowStockCount++;
            }
            tableModel.addRow(new Object[] { med.id, med.name, med.genericName, med.manufacturer,
                    String.format("Rs. %.2f", med.price), med.stockQuantity, status });
        }

        totalMedicinesLabel.setText("Total Medicines: " + totalMeds);
        lowStockLabel.setText("Low Stock: " + lowStockCount);
        searchField.setText("");
    }

    /**
     * Public refresh method to update table and statistics.
     * Called by MainGUI/AdminGUI auto-refresh timer.
     */
    public void refresh() {
        refreshTable();
    }
}
