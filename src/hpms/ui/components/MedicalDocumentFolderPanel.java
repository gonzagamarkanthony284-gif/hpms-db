package hpms.ui.components;

import hpms.model.FileAttachment;
import hpms.service.AttachmentService;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class MedicalDocumentFolderPanel extends JPanel {
    private String patientId;
    private JTable attachmentTable;
    private DefaultTableModel tableModel;
    private JLabel statsLabel;
    private JButton uploadBtn, downloadBtn, viewBtn, deleteBtn;
    private List<FileAttachment> currentAttachments;

    public MedicalDocumentFolderPanel(String patientId) {
        this.patientId = patientId;
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BG);

        // Top: Header with stats and buttons
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        // Left: Header and stats
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        headerPanel.setOpaque(false);
        JLabel titleLbl = new JLabel("📁 Medical Document Folder");
        titleLbl.setFont(Theme.APP_FONT.deriveFont(Font.BOLD, 14f));
        titleLbl.setForeground(new Color(0, 102, 102));

        statsLabel = new JLabel("Loading...");
        statsLabel.setFont(Theme.APP_FONT.deriveFont(Font.PLAIN, 10f));
        statsLabel.setForeground(new Color(100, 100, 100));

        headerPanel.add(titleLbl, BorderLayout.WEST);
        headerPanel.add(statsLabel, BorderLayout.SOUTH);

        // Right: Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        actionPanel.setOpaque(false);

        uploadBtn = new JButton("📤 Upload");
        uploadBtn.setToolTipText("Upload a new medical document");
        uploadBtn.setFont(Theme.APP_FONT.deriveFont(9f));

        viewBtn = new JButton("👁 View");
        viewBtn.setToolTipText("Preview selected document");
        viewBtn.setFont(Theme.APP_FONT.deriveFont(9f));

        downloadBtn = new JButton("💾 Download");
        downloadBtn.setToolTipText("Download selected document");
        downloadBtn.setFont(Theme.APP_FONT.deriveFont(9f));

        deleteBtn = new JButton("🗑 Delete");
        deleteBtn.setToolTipText("Delete selected document");
        deleteBtn.setFont(Theme.APP_FONT.deriveFont(9f));
        deleteBtn.setForeground(new Color(200, 0, 0));

        actionPanel.add(uploadBtn);
        actionPanel.add(viewBtn);
        actionPanel.add(downloadBtn);
        actionPanel.add(deleteBtn);

        topPanel.add(headerPanel, BorderLayout.WEST);
        topPanel.add(actionPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Center: Attachment table
        createAttachmentTable();
        JScrollPane scrollPane = new JScrollPane(attachmentTable);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        add(scrollPane, BorderLayout.CENTER);

        // Load attachments
        refreshAttachments();
    }

    private void createAttachmentTable() {
        tableModel = new DefaultTableModel(
                new String[] { "File Name", "Type", "Category", "Size", "Uploaded By", "Date" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        attachmentTable = new JTable(tableModel);
        attachmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        attachmentTable.setRowHeight(24);
        attachmentTable.setFont(Theme.APP_FONT.deriveFont(9f));
        attachmentTable.getTableHeader().setFont(Theme.APP_FONT.deriveFont(Font.BOLD, 10f));

        // Render file icons based on type
        attachmentTable.getColumn("File Name").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(String.valueOf(value));
                if (row < currentAttachments.size()) {
                    FileAttachment att = currentAttachments.get(row);
                    String icon = getIconForFile(att);
                    label.setText(icon + " " + att.fileName);
                }
                label.setFont(Theme.APP_FONT.deriveFont(9f));
                if (isSelected) {
                    label.setBackground(new Color(200, 220, 220));
                    label.setOpaque(true);
                }
                return label;
            }
        });

        // Size column renderer
        attachmentTable.getColumn("Size").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                if (row < currentAttachments.size()) {
                    FileAttachment att = currentAttachments.get(row);
                    setText(att.getFormattedSize());
                }
                return this;
            }
        });

        // Category column renderer with color
        attachmentTable.getColumn("Category").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel label = new JLabel();
                if (row < currentAttachments.size()) {
                    FileAttachment att = currentAttachments.get(row);
                    label.setText(att.category);
                    label.setBackground(getCategoryColor(att.category));
                    label.setOpaque(true);
                    label.setForeground(Color.WHITE);
                    label.setHorizontalAlignment(CENTER);
                }
                label.setFont(Theme.APP_FONT.deriveFont(8f));
                return label;
            }
        });

        // Date column renderer
        attachmentTable.getColumn("Date").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                if (row < currentAttachments.size()) {
                    FileAttachment att = currentAttachments.get(row);
                    setText(att.uploadedAt.toLocalDate().toString());
                }
                return this;
            }
        });

        // Set column widths
        attachmentTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        attachmentTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        attachmentTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        attachmentTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        attachmentTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        attachmentTable.getColumnModel().getColumn(5).setPreferredWidth(90);
    }

    private String getIconForFile(FileAttachment att) {
        if (att.isImage())
            return "🖼";
        if (att.isPdf())
            return "📄";
        if (att.isDicom())
            return "🔬";
        switch (att.fileType) {
            case "X-ray":
                return "🔍";
            case "Lab Results":
                return "📊";
            case "Medical Certificate":
                return "📜";
            default:
                return "📎";
        }
    }

    private Color getCategoryColor(String category) {
        switch (category) {
            case "Imaging":
                return new Color(65, 105, 225);
            case "Laboratory":
                return new Color(255, 140, 0);
            case "Documentation":
                return new Color(34, 139, 34);
            case "Consultation":
                return new Color(220, 20, 60);
            case "Discharge":
                return new Color(75, 0, 130);
            case "Prescription":
                return new Color(0, 128, 128);
            case "Insurance":
                return new Color(128, 128, 0);
            default:
                return new Color(128, 128, 128);
        }
    }

    public void refreshAttachments() {
        currentAttachments = AttachmentService.getAttachmentsByPatient(patientId);
        tableModel.setRowCount(0);

        for (FileAttachment att : currentAttachments) {
            tableModel.addRow(new Object[] {
                    att.fileName,
                    att.fileType,
                    att.category,
                    att.getFormattedSize(),
                    att.uploadedBy,
                    att.uploadedAt.toLocalDate()
            });
        }

        // Update statistics
        java.util.Map<String, Object> stats = AttachmentService.getAttachmentStats(patientId);
        int totalFiles = (int) stats.getOrDefault("totalFiles", 0);
        long totalSize = (long) stats.getOrDefault("totalSize", 0);

        StringBuilder statText = new StringBuilder();
        statText.append(String.format("📊 %d file%s | ", totalFiles, totalFiles != 1 ? "s" : ""));

        if (totalSize > 0) {
            double sizeInMB = totalSize / (1024.0 * 1024.0);
            if (sizeInMB < 1) {
                statText.append(String.format("%.2f KB", totalSize / 1024.0));
            } else {
                statText.append(String.format("%.2f MB", sizeInMB));
            }
        } else {
            statText.append("0 KB");
        }

        statsLabel.setText(statText.toString());
    }

    public JButton getUploadButton() {
        return uploadBtn;
    }

    public JButton getViewButton() {
        return viewBtn;
    }

    public JButton getDownloadButton() {
        return downloadBtn;
    }

    public JButton getDeleteButton() {
        return deleteBtn;
    }

    public FileAttachment getSelectedAttachment() {
        int row = attachmentTable.getSelectedRow();
        if (row >= 0 && row < currentAttachments.size()) {
            return currentAttachments.get(row);
        }
        return null;
    }

    public int getSelectedRow() {
        return attachmentTable.getSelectedRow();
    }
}
