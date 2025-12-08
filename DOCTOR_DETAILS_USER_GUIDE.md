# Doctor Details Enhancement - User Guide

## Phase 3 New Features

### Overview
The Doctor Details enhancement adds professional doctor profile display, expertise information, and dynamic filtering capabilities to the Staff Management module.

---

## How to Use the New Features

### 1. Accessing the Doctor Management Panel

1. Launch the HPMS application
2. Navigate to **Staff Management** section
3. Click the **"Doctors"** tab

### 2. Filtering Doctors by Department

**To filter by department:**

1. In the "Filter Doctors" panel at the top, click the **Department** dropdown
2. Select a department from the list (e.g., "Cardiology", "Neurology", etc.)
3. The doctor table below will **automatically update** to show only doctors in that department
4. All other doctors will be temporarily hidden

**To clear the department filter:**
- Click the **"Reset Filters"** button
- Or select **"-- All Departments --"** from the Department dropdown

### 3. Filtering Doctors by Specialization

**To filter by specialization:**

1. In the "Filter Doctors" panel, click the **Specialization** dropdown
2. Select a medical specialty (e.g., "Cardiology Specialist", "Neurologist", etc.)
3. The table will show only doctors with that specialization
4. All others will be hidden

**To clear the specialization filter:**
- Click the **"Reset Filters"** button
- Or select **"-- All Specializations --"** from the Specialization dropdown

### 4. Using Combined Filters

**To filter by BOTH department AND specialization:**

1. Click **Department** dropdown and select a department
2. Click **Specialization** dropdown and select a specialization
3. The table will show **only doctors matching BOTH criteria**
4. This narrows results for more precise searching

**Example**: Show all "Cardiologists in the Cardiology Department"

### 5. Understanding the Doctor Table

The doctor table now shows 7 columns:

| Column | Contains | Purpose |
|--------|----------|---------|
| Staff ID | Unique ID | Identifier for system records |
| Name | Doctor's name | Full name of doctor |
| Department | Department name | Where they work |
| **Expertise** | Specialization | **NEW** - Medical specialty |
| Details | License info | License number and backup info |
| Status | Active/Inactive | Current status |
| Joined Date | Registration date | When added to system |

### 6. Viewing Doctor Profile Details

**To view a doctor's full profile:**

1. In the doctor table, click **"View Details"** button (or double-click a row)
2. A **Doctor Profile Dialog** will open showing:
   - **Profile Picture** (left side)
     - High-quality photo if available
     - Placeholder if no photo on file
   - **Professional Information** (right side):
     - Doctor's name (large, bold)
     - Medical specialization (italic)
     - Staff ID
     - Department
     - License number
     - Phone number
     - Email address
     - Professional credentials
     - Certifications

### 7. Understanding the Profile Photo Display

**Photo Display Features:**
- Shows doctor's professional headshot (150×150 pixels)
- Automatically scaled to consistent size
- If no photo available: Shows "No Photo Available" message
- If photo file missing: Shows "Photo Error" message
- Professional gray border for empty states

---

## Feature Details

### Department Filter
- **Source**: Automatically populated from doctors in the system
- **Options**: All unique departments
- **Behavior**: Shows only doctors in selected department
- **Default**: "-- All Departments --" (shows all)

### Specialization Filter
- **Source**: Automatically populated from doctor specialties
- **Options**: All unique medical specialties
- **Behavior**: Shows only doctors with selected specialty
- **Default**: "-- All Specializations --" (shows all)

### Reset Filters Button
- **Function**: Clears all selected filters
- **Result**: Returns table to showing all doctors
- **Keyboard**: N/A (button click only)

---

## Tips & Best Practices

### Efficient Doctor Search

1. **For finding doctors by location**: Use Department filter
2. **For finding specialists**: Use Specialization filter
3. **For precise searches**: Use both filters together
4. **For browsing all**: Keep both filters at default

### Viewing Multiple Doctors

1. Note the desired specialty/department combination
2. Keep filter selections while selecting different doctors
3. Use View Details to compare credentials
4. Reset filters to start a new search

### Finding a Specific Doctor

**Method 1 - By Department:**
1. Select department where doctor works
2. View all doctors in that department
3. Find doctor in smaller filtered list

**Method 2 - By Specialty:**
1. Select medical specialty needed
2. View all specialists in that field
3. Compare qualifications/credentials

---

## Troubleshooting

### Filter dropdowns are empty
**Cause**: No doctors in system yet
**Solution**: Add doctors to system first via "Add Staff" button

### Doctor doesn't appear when filtering
**Possible Causes**:
- Doctor not assigned to selected department
- Doctor doesn't have selected specialization
- Doctor record may be inactive
**Solution**: Check doctor's record in Edit dialog

### Photo not displaying in profile
**Possible Causes**:
- No photo file assigned to doctor
- Photo file path incorrect
- File doesn't exist at specified location
**Solution**: Upload doctor's photo via Edit Staff dialog

### Filter selections not updating table
**Cause**: Rare UI refresh issue
**Solution**: Close and reopen Doctor Management tab

---

## Related Features

### Adding a Doctor
1. Click **"Add Staff"** button
2. Select "Doctor" as role
3. Fill in details including:
   - Name
   - Department
   - Specialization
   - License number
   - Contact information
   - Professional credentials
   - Profile photo path

### Editing Doctor Information
1. Click **"Edit"** button in Staff Management
2. Select doctor from list
3. Update any field:
   - Department
   - Specialization
   - License number
   - Contact details
   - Credentials
   - Photo path
4. Click Save

### Deleting a Doctor
1. Click **"Delete"** button
2. Confirm deletion
3. Doctor removed from system
4. Filters automatically update

---

## Data Fields Explained

### Specialty/Expertise
- Medical field of specialization (e.g., "Cardiology", "Pediatrics")
- Used for filtering by medical specialty
- Displayed in profile and table

### Department
- Hospital/facility department where doctor works
- May differ from specialty (e.g., works in Outpatient but specialized in Surgery)
- Used for filtering by location

### License Number
- Medical license identification
- Proves credentials and legal standing
- Displayed in details and profile

### Qualifications
- Educational background and degrees
- Professional certifications
- Displayed in profile details

### Certifications
- Additional professional certifications
- Specialty board certifications
- Displayed in profile details

---

## Keyboard Shortcuts

| Shortcut | Action | Status |
|----------|--------|--------|
| Tab | Move between filters | Supported |
| Enter | Select dropdown item | Supported |
| Escape | Close detail dialog | Supported |

---

## Examples

### Example 1: Find all Cardiologists
1. Click **Specialization** dropdown
2. Select **"Cardiology"** (or similar)
3. Table shows only cardiologists

### Example 2: Find doctors in Emergency Department
1. Click **Department** dropdown
2. Select **"Emergency Department"**
3. Table shows all doctors working there

### Example 3: Find Neurologists in Neurology Department
1. Click **Department** → select "Neurology Department"
2. Click **Specialization** → select "Neurology"
3. Table shows doctors matching both criteria

### Example 4: Compare doctor profiles
1. Select filters to narrow list
2. Click "View Details" on first doctor
3. Review credentials and photo
4. Close dialog (filters unchanged)
5. View Details on next doctor
6. Compare information

---

## FAQ

**Q: Can I search by doctor name?**
A: Not yet, use keyboard Ctrl+F or browser search on the page

**Q: Can I save filter preferences?**
A: No, filters reset when you exit the Doctor Management panel

**Q: What if a doctor has no photo?**
A: A placeholder message displays instead - functionality unchanged

**Q: Can I sort the table by clicking columns?**
A: No, use filters to find doctors instead

**Q: How many doctors can the system handle?**
A: Filters work efficiently with hundreds of doctors

**Q: Do filters work with deleted doctors?**
A: No, deleted doctors are hidden from all views

---

## Support

For issues or feature requests:
1. Check this guide for troubleshooting
2. Verify doctor records have correct information
3. Contact system administrator if problems persist

---

## Version Information

- **Feature**: Doctor Details Enhancement (Phase 3)
- **Introduced**: Current Version
- **Status**: Production Ready ✅
- **Compatibility**: All existing features unchanged

---

*Last Updated: Current Session*
*For HPMS Healthcare Management System*
