package hpms.ui.panels;

import hpms.model.Service;
import hpms.service.ServiceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ServicesPanel extends JPanel {
    private JTable servicesTable;
    private DefaultTableModel tableModel;
    private JButton addBtn, editBtn, deleteBtn, refreshBtn;
    private JTextField searchField;
    private JComboBox<String> filterCombo;

    public ServicesPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(249, 250, 251));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Top panel - Title and controls
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Middle panel - Table
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        // Bottom panel - Actions
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        loadServices();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(249, 250, 251));
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Title
        JLabel titleLabel = new JLabel("Hospital Services");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 30, 30));
        panel.add(titleLabel, BorderLayout.WEST);

        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setBackground(new Color(249, 250, 251));

        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        searchPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(200, 28));
        searchField.setPreferredSize(new Dimension(200, 28));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                searchServices();
            }
        });
        searchPanel.add(searchField);

        searchPanel.add(Box.createHorizontalStrut(10));

        JLabel filterLabel = new JLabel("Filter: ");
        filterLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        searchPanel.add(filterLabel);

        filterCombo = new JComboBox<>(new String[]{"All", "Active", "Inactive"});
        filterCombo.setMaximumSize(new Dimension(100, 28));
        filterCombo.setPreferredSize(new Dimension(100, 28));
        filterCombo.addActionListener(e -> filterServices());
        searchPanel.add(filterCombo);

        panel.add(searchPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(249, 250, 251));

        String[] columns = {"Service ID", "Service Name", "Department", "Description", "Available Beds", "Total Beds", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        servicesTable = new JTable(tableModel);
        servicesTable.setBackground(Color.WHITE);
        servicesTable.setForeground(new Color(30, 30, 30));
        servicesTable.setSelectionBackground(new Color(59, 130, 246));
        servicesTable.setSelectionForeground(Color.WHITE);
        servicesTable.setRowHeight(28);
        servicesTable.getTableHeader().setBackground(new Color(30, 64, 175));
        servicesTable.getTableHeader().setForeground(Color.WHITE);
        servicesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));

        JScrollPane scrollPane = new JScrollPane(servicesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(new Color(249, 250, 251));
        panel.setBorder(new EmptyBorder(15, 0, 0, 0));

        addBtn = new JButton("+ Add Service");
        addBtn.setFont(new Font("Arial", Font.BOLD, 11));
        addBtn.setBackground(new Color(30, 64, 175));
        addBtn.setForeground(Color.WHITE);
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> addService());

        editBtn = new JButton("✎ Edit");
        editBtn.setFont(new Font("Arial", Font.BOLD, 11));
        editBtn.setBackground(new Color(59, 130, 246));
        editBtn.setForeground(Color.WHITE);
        editBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> editService());

        deleteBtn = new JButton("✕ Delete");
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 11));
        deleteBtn.setBackground(new Color(239, 68, 68));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> deleteService());

        refreshBtn = new JButton("⟲ Refresh");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 11));
        refreshBtn.setBackground(new Color(107, 114, 128));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> loadServices());

        panel.add(addBtn);
        panel.add(Box.createHorizontalStrut(8));
        panel.add(editBtn);
        panel.add(Box.createHorizontalStrut(8));
        panel.add(deleteBtn);
        panel.add(Box.createHorizontalStrut(8));
        panel.add(refreshBtn);
        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    private void loadServices() {
        tableModel.setRowCount(0);
        List<Service> servicesList = ServiceService.getAllServices();

        for (Service service : servicesList) {
            Object[] row = {
                    service.serviceId,
                    service.serviceName,
                    service.department,
                    service.description,
                    service.availableBeds,
                    service.totalBeds,
                    service.status
            };
            tableModel.addRow(row);
        }
    }

    private void searchServices() {
        String query = searchField.getText().trim();
        tableModel.setRowCount(0);

        List<Service> results;
        if (query.isEmpty()) {
            results = ServiceService.getAllServices();
        } else {
            results = ServiceService.searchServices(query);
        }

        for (Service service : results) {
            Object[] row = {
                    service.serviceId,
                    service.serviceName,
                    service.department,
                    service.description,
                    service.availableBeds,
                    service.totalBeds,
                    service.status
            };
            tableModel.addRow(row);
        }
    }

    private void filterServices() {
        String filter = (String) filterCombo.getSelectedItem();
        tableModel.setRowCount(0);

        List<Service> servicesList = ServiceService.getAllServices();
        for (Service service : servicesList) {
            if ("All".equals(filter) || 
                ("Active".equals(filter) && "ACTIVE".equals(service.status)) ||
                ("Inactive".equals(filter) && "INACTIVE".equals(service.status))) {
                Object[] row = {
                        service.serviceId,
                        service.serviceName,
                        service.department,
                        service.description,
                        service.availableBeds,
                        service.totalBeds,
                        service.status
                };
                tableModel.addRow(row);
            }
        }
    }

    private void addService() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add New Service", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();
        JSpinner availBedsSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
        JSpinner totalBedsSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        JComboBox<String> deptCombo = new JComboBox<>(new String[]{"Cardiology", "Neurology", "Orthopedics", "Pediatrics", "Oncology", "ER"});

        panel.add(new JLabel("Service Name:"));
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Description:"));
        panel.add(descField);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Department:"));
        panel.add(deptCombo);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Available Beds:"));
        panel.add(availBedsSpinner);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Total Beds:"));
        panel.add(totalBedsSpinner);
        panel.add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();
            int availBeds = (Integer) availBedsSpinner.getValue();
            int totalBeds = (Integer) totalBedsSpinner.getValue();
            String dept = (String) deptCombo.getSelectedItem();

            if (name.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ServiceService.addService(name, desc, availBeds, totalBeds, dept);
            loadServices();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Service added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        panel.add(buttonPanel);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void editService() {
        int selectedRow = servicesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a service to edit", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String serviceId = (String) tableModel.getValueAt(selectedRow, 0);
        Service service = ServiceService.getService(serviceId);

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Service", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextField nameField = new JTextField(service.serviceName);
        JTextField descField = new JTextField(service.description);
        JSpinner availBedsSpinner = new JSpinner(new SpinnerNumberModel(service.availableBeds, 0, 100, 1));
        JSpinner totalBedsSpinner = new JSpinner(new SpinnerNumberModel(service.totalBeds, 1, 100, 1));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE"});
        statusCombo.setSelectedItem(service.status);

        panel.add(new JLabel("Service Name:"));
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Description:"));
        panel.add(descField);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Available Beds:"));
        panel.add(availBedsSpinner);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Total Beds:"));
        panel.add(totalBedsSpinner);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);
        panel.add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel();
        JButton updateBtn = new JButton("Update");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);

        updateBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();
            int availBeds = (Integer) availBedsSpinner.getValue();
            int totalBeds = (Integer) totalBedsSpinner.getValue();
            String status = (String) statusCombo.getSelectedItem();

            if (name.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ServiceService.updateService(serviceId, name, desc, availBeds, totalBeds, status);
            loadServices();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Service updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        panel.add(buttonPanel);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteService() {
        int selectedRow = servicesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a service to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String serviceId = (String) tableModel.getValueAt(selectedRow, 0);
        String serviceName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete service: " + serviceName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            ServiceService.deleteService(serviceId);
            loadServices();
            JOptionPane.showMessageDialog(this, "Service deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
