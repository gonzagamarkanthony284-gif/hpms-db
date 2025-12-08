package hpms.model;

import java.time.LocalDateTime;
import java.io.File;

public class FileAttachment {
    public final int id;
    public final String patientId;
    public String fileName;
    public String filePath;
    public String fileType; // e.g., "X-ray", "Lab Result", "Medical Certificate", "PDF Report", "Image"
    public String category; // e.g., "Imaging", "Laboratory", "Documentation"
    public long fileSize; // in bytes
    public String mimeType; // e.g., "image/jpeg", "application/pdf"
    public String description; // free text notes about the file
    public String uploadedBy; // staff/user who uploaded it
    public final LocalDateTime uploadedAt;
    public LocalDateTime lastModified;
    public String fileHash; // for integrity verification (SHA-256)
    public boolean isEncrypted; // security flag
    public String status; // "Active", "Archived", "Deleted" (soft delete)

    // Constructor for database reads (with existing ID)
    public FileAttachment(int id, String patientId, String fileName, String filePath, String fileType,
            String category, long fileSize, String mimeType, String description,
            String uploadedBy, LocalDateTime uploadedAt, LocalDateTime lastModified,
            String fileHash, boolean isEncrypted, String status) {
        this.id = id;
        this.patientId = patientId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.category = category;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.description = description;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
        this.lastModified = lastModified;
        this.fileHash = fileHash;
        this.isEncrypted = isEncrypted;
        this.status = status;
    }

    // Constructor for new file uploads (auto-generate ID)
    public FileAttachment(String patientId, String fileName, String filePath, String fileType,
            String category, String description, String uploadedBy) {
        this.id = -1; // Will be assigned by database
        this.patientId = patientId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.category = category;
        this.fileSize = 0;
        this.mimeType = getMimeType(filePath);
        this.description = description;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.fileHash = "";
        this.isEncrypted = false;
        this.status = "Active";

        // Calculate actual file size if file exists
        try {
            File f = new File(filePath);
            if (f.exists()) {
                this.fileSize = f.length();
            }
        } catch (Exception e) {
            this.fileSize = 0;
        }
    }

    // Determine MIME type from file extension
    public static String getMimeType(String filePath) {
        if (filePath == null)
            return "application/octet-stream";

        String lower = filePath.toLowerCase();
        if (lower.endsWith(".pdf"))
            return "application/pdf";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg"))
            return "image/jpeg";
        if (lower.endsWith(".png"))
            return "image/png";
        if (lower.endsWith(".gif"))
            return "image/gif";
        if (lower.endsWith(".bmp"))
            return "image/bmp";
        if (lower.endsWith(".dicom") || lower.endsWith(".dcm"))
            return "application/dicom";
        if (lower.endsWith(".docx"))
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        if (lower.endsWith(".xlsx"))
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        if (lower.endsWith(".txt"))
            return "text/plain";
        return "application/octet-stream";
    }

    // Format file size for display
    public String getFormattedSize() {
        if (fileSize <= 0)
            return "0 B";
        final String[] units = new String[] { "B", "KB", "MB", "GB" };
        int unitIndex = 0;
        double size = fileSize;
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        return String.format("%.2f %s", size, units[unitIndex]);
    }

    // Get file extension
    public String getExtension() {
        if (fileName == null)
            return "";
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(lastDot + 1).toLowerCase();
        }
        return "";
    }

    // Check if file is an image
    public boolean isImage() {
        return mimeType != null && mimeType.startsWith("image/");
    }

    // Check if file is a PDF
    public boolean isPdf() {
        return mimeType != null && mimeType.equals("application/pdf");
    }

    // Check if file is a DICOM image
    public boolean isDicom() {
        return mimeType != null && mimeType.equals("application/dicom");
    }

    @Override
    public String toString() {
        return fileName + " (" + getFormattedSize() + ")";
    }
}
