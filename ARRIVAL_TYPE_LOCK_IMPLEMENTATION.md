# Arrival Type Lock Implementation

## Overview
Implemented business rule enforcement to prevent modification of the "Type of Arrival" (Registration Type) field after patient record creation.

## Change Summary
**Date:** December 8, 2025  
**Module:** Patient Management - Admin Side  
**File Modified:** `src/hpms/ui/panels/PatientsPanel.java`  

## Business Rule
Once a patient record is created, the **Type of Arrival** (Registration Type) field must be permanently locked and cannot be changed through:
- Edit patient dialog
- Patient forms
- Database updates through the UI

## Field Details
**Field Name:** `registrationType` (displayed as "Type of Registration / Mode of Arrival")

**Possible Values:**
- Walk-in Patient
- Emergency Patient
- Accident / Trauma Case
- Referral Patient
- OB / Maternity Case
- Surgical Admission
- Pediatric Patient
- Geriatric Patient
- Telemedicine / Virtual Consult

## Implementation Details

### Location
- **File:** `src/hpms/ui/panels/PatientsPanel.java`
- **Method:** `showEditPatientDialog()`
- **Lines Modified:** 1909-1926

### Code Changes
```java
// Registration / mode of arrival
String[] regOptions = new String[] {
        "Walk-in Patient",
        "Emergency Patient",
        "Accident / Trauma Case",
        "Referral Patient",
        "OB / Maternity Case",
        "Surgical Admission",
        "Pediatric Patient",
        "Geriatric Patient",
        "Telemedicine / Virtual Consult"
};
JComboBox<String> regCombo = new JComboBox<>(regOptions);
if (p.registrationType != null && !p.registrationType.isEmpty())
    regCombo.setSelectedItem(p.registrationType);

// Make registration type non-editable in edit dialog - it cannot be changed after creation
regCombo.setEnabled(false);
regCombo.setToolTipText("Type of Arrival cannot be changed after patient creation");
```

### Technical Approach
- Used `setEnabled(false)` to disable the JComboBox in the edit dialog
- Added tooltip to inform users why the field is disabled
- Field remains fully editable in the "Add New Patient" dialog
- Field remains read-only in the "Edit Patient" dialog

### User Experience
1. **Add New Patient:** Registration Type is fully selectable (editable)
2. **Edit Existing Patient:** Registration Type displays current value but is grayed out (non-editable)
3. **Tooltip:** Hovering over the disabled field shows: "Type of Arrival cannot be changed after patient creation"

## Verification Steps

### Compilation
```powershell
cd c:\xampp\htdocs\HPMS
javac -encoding UTF-8 -d bin -cp "lib/*" -source 17 -target 17 (Get-ChildItem -Path src -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
```
✅ Compilation successful (no errors)

### Testing Checklist
- [ ] Open HPMS application
- [ ] Navigate to Admin → Patients Panel
- [ ] Add a new patient (verify Registration Type is editable)
- [ ] Save the new patient
- [ ] Edit the same patient (verify Registration Type is grayed out/disabled)
- [ ] Verify tooltip appears on hover
- [ ] Attempt to change Registration Type (should not be possible)
- [ ] Save changes (Registration Type should remain unchanged)

## Related Files
- **Model:** `src/hpms/model/Patient.java` - Contains `registrationType` field
- **Service:** `src/hpms/service/PatientService.java` - `editExtended()` method accepts `registrationType` parameter but won't be changed from UI
- **UI:** `src/hpms/ui/panels/PatientsPanel.java` - Contains both add and edit dialogs

## Data Integrity Benefits
1. **Historical Accuracy:** Preserves how patients originally entered the system
2. **Audit Trail:** Prevents data manipulation that could affect medical/legal records
3. **Billing Accuracy:** Ensures arrival type-based billing remains consistent
4. **Reporting Integrity:** Statistical reports on patient arrival patterns remain accurate

## Note on Patient Type vs Registration Type
This implementation applies to **Registration Type** (arrival mode), NOT **Patient Type** (INPATIENT/OUTPATIENT).

- **registrationType:** Walk-in, Emergency, Referral, etc. → **NOW LOCKED**
- **patientType:** INPATIENT or OUTPATIENT → Still editable (can be changed if patient status changes)

## Future Considerations
If business rules require locking Patient Type as well, a similar approach can be applied to the `patientTypeCombo` field (currently at line 1831-1833 in the edit dialog).
