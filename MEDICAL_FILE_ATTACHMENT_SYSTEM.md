# Medical Information File Attachment System - Implementation Guide

## Overview

The Medical Information File Attachment System provides a comprehensive file management solution for storing, organizing, and retrieving medical documents within the HPMS patient management system.

## Features Implemented

### 1. File Attachment Storage
- **Secure Storage**: Files are stored in the database with full metadata tracking
- **Categorization**: Documents organized by category (Imaging, Laboratory, Documentation, etc.)
- **Multiple Formats**: Supports PDF, Images (JPG/PNG), DICOM, Office documents, and text files
- **File Metadata**: Tracks file size, MIME type, upload date, and uploader identity
- **Soft Delete**: Files can be archived/deleted without physical removal from storage

### 2. Medical Document Folder UI
- **Visual Folder Structure**: Resembles paper-based medical records in digital form
- **Document Table**: Lists all attachments with file icons, names, types, categories, and metadata
- **Quick Statistics**: Displays total files and total storage size
- **Category Badges**: Color-coded badges for visual organization

### 3. File Operations

#### Upload
- Browse and select files from local system
- Input file type and category
- Add description/notes
- Automatic file size calculation
- MIME type detection

#### View/Preview
- Opens file with system default application
- Supports images, PDFs, and DICOM viewers
- Error handling for missing files

#### Download
- Save files to local system
- Maintains original filename
- Supports batch operations (one at a time)

#### Delete
- Soft delete with confirmation
- Marked as 'Deleted' in database
- Can be permanently purged later
- Recoverable via database restoration

### 4. File Organization

#### Categories Available
1. **Imaging**: X-ray, MRI, CT Scan, Ultrasound, DICOM Images
2. **Laboratory**: Lab Results, Blood Tests, Urinalysis, Pathology Reports
3. **Documentation**: Certificates, Progress Notes, Treatment Plans, Discharge Summaries
4. **Consultation**: Reports, Specialist Opinions
5. **Discharge**: Summaries, Instructions, Medication Lists
6. **Prescription**: Prescriptions, Medication Lists
7. **Insurance**: Insurance Documents, Authorizations, Claims
8. **Other**: General Documents, Images, PDFs

Each category has predefined file types for consistency.

## Database Schema

### patient_file_attachments Table

```sql
CREATE TABLE patient_file_attachments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(20) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(100),
    category VARCHAR(100),
    file_size BIGINT DEFAULT 0,
    mime_type VARCHAR(100),
    description TEXT,
    uploaded_by VARCHAR(100),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    file_hash VARCHAR(255),
    is_encrypted BOOLEAN DEFAULT FALSE,
    status ENUM('Active', 'Archived', 'Deleted') DEFAULT 'Active',
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_patient (patient_id),
    INDEX idx_category (category),
    INDEX idx_status (status),
    INDEX idx_uploaded_at (uploaded_at),
    INDEX idx_patient_category (patient_id, category)
);
```

**Indexes**: Optimized for patient lookup, category filtering, and date-based queries.

## Model Classes

### FileAttachment.java

**Location**: `src/hpms/model/FileAttachment.java`

**Key Fields**:
- `id`: Unique identifier (auto-generated)
- `patientId`: Associated patient
- `fileName`: Original filename
- `filePath`: Full file path
- `fileType`: Type descriptor (X-ray, Lab Results, etc.)
- `category`: Organization category
- `fileSize`: File size in bytes
- `mimeType`: MIME type for proper handling
- `description`: User-provided notes
- `uploadedBy`: User who uploaded
- `uploadedAt`: Upload timestamp
- `isEncrypted`: Security flag
- `status`: Active/Archived/Deleted

**Helper Methods**:
- `getFormattedSize()`: Human-readable file size (B, KB, MB, GB)
- `getExtension()`: Extract file extension
- `isImage()`: Check if file is image
- `isPdf()`: Check if file is PDF
- `isDicom()`: Check if file is DICOM

## Service Layer

### AttachmentService.java

**Location**: `src/hpms/service/AttachmentService.java`

**Key Methods**:

```java
// Upload a file
uploadAttachment(patientId, fileName, filePath, fileType, category, description, uploadedBy)

// Retrieve attachments
getAttachmentsByPatient(patientId)
getAttachmentsByCategory(patientId, category)
getAttachmentById(attachmentId)

// Manage attachments
updateAttachment(attachmentId, description, fileType, category)
deleteAttachment(attachmentId)

// Statistics
getAttachmentStats(patientId)

// Configuration
getAvailableCategories()
getFileTypesForCategory(category)
```

## UI Components

### MedicalDocumentFolderPanel.java

**Location**: `src/hpms/ui/components/MedicalDocumentFolderPanel.java`

**Features**:
- **Table Display**: Shows all attachments with columns for name, type, category, size, uploader, date
- **File Icons**: Visual indicators based on file type
- **Color-Coded Categories**: Different colors for different document categories
- **Statistics Display**: Total files and storage usage
- **Action Buttons**: Upload, View, Download, Delete buttons
- **Responsive Design**: Resizable layout with proper scrolling

**Button Actions**:
- **Upload**: Opens file chooser and metadata entry dialog
- **View**: Previews selected file with system default viewer
- **Download**: Saves file to user-selected location
- **Delete**: Removes file with confirmation

## Integration with PatientsPanel

### Menu Entry
Added to right-click context menu on patient list:
- **Position**: Between "Add Clinical Info" and "Attach Files"
- **Label**: "📁 Medical Document Folder"
- **Shortcut**: Right-click patient → Medical Document Folder

### Dialog Implementation
- Full-featured window showing folder structure
- Integration with authentication system for tracking who uploaded what
- Real-time refresh after operations
- Error handling with user-friendly messages

## User Workflow

### Uploading a Medical Document

1. **Select Patient**: Click on patient in list
2. **Open Medical Folder**: Right-click → "📁 Medical Document Folder"
3. **Upload File**: Click "📤 Upload" button
4. **Choose File**: Browse and select document (PDF, Image, etc.)
5. **Enter Metadata**: 
   - Select category (Imaging, Laboratory, etc.)
   - Select file type (X-ray, Lab Results, etc.)
   - Add optional description
6. **Confirm**: Click "Upload" button
7. **Verification**: File appears in document folder with timestamp

### Organizing Documents

- **By Category**: Table shows category with color badge
- **By Date**: Most recent first
- **By Type**: File type column shows document classification
- **By Size**: Total storage displayed in statistics

### Accessing Documents

1. **View**: Double-click file or select and click "👁 View"
2. **Download**: Select file and click "💾 Download"
3. **Delete**: Select file and click "🗑 Delete" (with confirmation)

## Security Features

### Access Control
- Files linked to patient via `patient_id` foreign key
- Uploads tracked with `uploaded_by` username
- Soft delete maintains audit trail

### Data Integrity
- File hash storage capability for verification
- MIME type validation
- File size limits enforceable via service layer
- Database cascading deletes for patient records

### Privacy
- Encryption flag for sensitive documents
- Audit trail of all operations
- Timestamp tracking for compliance

## File Type Support

### Images
- **Formats**: JPG, JPEG, PNG, GIF, BMP
- **Use Cases**: Medical photos, scans, reports
- **MIME Types**: image/jpeg, image/png, image/gif, image/bmp

### Documents
- **PDF**: application/pdf (Medical certificates, reports)
- **DICOM**: application/dicom (Radiology images)
- **Office**: .docx, .xlsx (Records, forms)
- **Text**: .txt (Notes, reports)

### File Size Display
- **Automatic Formatting**: 0 B to GB range
- **Example**: 1,536 KB displays as "1.50 MB"

## Database Integration

### SQL Script
File: `database_schema.sql`

The `patient_file_attachments` table is included in the main schema. To apply:

```sql
-- From database_schema.sql
USE hpms_db;

-- Table already created with:
-- - Proper indexes for performance
-- - Foreign key constraints
-- - Enum status for consistency
-- - Timestamp tracking
```

### Queries
- **Get Patient's Files**: SELECT * FROM patient_file_attachments WHERE patient_id = ? AND status = 'Active'
- **Get By Category**: Add AND category = ? condition
- **Date Range**: Add AND uploaded_at BETWEEN ? AND ?
- **Statistics**: SUM(file_size), COUNT(*) grouped by category

## Performance Considerations

### Indexes
- `idx_patient`: Fast lookup by patient ID
- `idx_category`: Filter by document type
- `idx_status`: Soft delete queries
- `idx_uploaded_at`: Date-based filtering
- `idx_patient_category`: Combined queries

### Storage
- Files stored on disk with database references
- No file size limits enforced (configurable)
- Soft deletes prevent database bloat
- Regular archival process recommended

## Error Handling

### Upload Errors
- Missing required fields
- File not found
- Invalid file type
- Database constraints

### Retrieval Errors
- Patient not found
- File not found
- Database connection issues

### User Feedback
- Clear error messages
- Success confirmations
- Operation logging
- No silent failures

## Compilation Status

✅ **All classes compile successfully**

```
FileAttachment.java: ✅ Compiles
AttachmentService.java: ✅ Compiles
MedicalDocumentFolderPanel.java: ✅ Compiles
PatientsPanel.java: ✅ Updated and compiles (0 errors)
Database Schema: ✅ Updated with patient_file_attachments table
```

## Testing Checklist

- [ ] Add patient to system
- [ ] Open Medical Document Folder from right-click menu
- [ ] Upload various file types (PDF, JPG, PNG)
- [ ] Verify files appear in folder with correct metadata
- [ ] Test each category selection
- [ ] Preview files by clicking View
- [ ] Download file to local system
- [ ] Delete file with confirmation
- [ ] Verify refresh after operations
- [ ] Check statistics update correctly
- [ ] Test with large files
- [ ] Verify file paths are correctly stored
- [ ] Check database entries for uploaded files

## Future Enhancements

1. **Batch Operations**: Upload/download multiple files
2. **File Sharing**: Share documents with other staff
3. **Annotations**: Add notes/markup to images
4. **Compression**: Auto-compress large files
5. **Version Control**: Track document versions
6. **Full-Text Search**: Search document content
7. **Expiration**: Auto-archive old documents
8. **Encryption**: At-rest encryption for sensitive files
9. **Audit Reports**: Detailed access logs
10. **Integration**: Direct upload from lab/imaging systems

## Support & Troubleshooting

### File Not Uploading
- Check file size and type
- Verify write permissions to storage directory
- Check database connectivity

### Files Not Appearing
- Refresh dialog (close and reopen)
- Verify database entries exist
- Check file path validity

### Preview Not Working
- Ensure file exists at stored path
- Check if system has viewer for file type
- Try downloading and opening manually

## Configuration

All configuration is currently hardcoded. Future versions should support:
- Max file size limits
- Allowed file types per category
- Storage directory path
- Database table names
- Category definitions

## Version History

- **v1.0** (Current): Initial implementation with core file management features
