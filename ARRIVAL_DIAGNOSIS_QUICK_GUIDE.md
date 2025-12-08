# ARRIVAL & DIAGNOSIS HISTORY - QUICK REFERENCE GUIDE

## Feature Overview

The system now preserves complete medical history by creating new records for each visit instead of overwriting original data.

```
┌─────────────────────────────────────────────────────────────────┐
│                   PATIENT MEDICAL HISTORY                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  FIRST ARRIVAL (LOCKED - NEVER CHANGES)                        │
│  ════════════════════════════════════════                       │
│  Type: Emergency Patient                                        │
│  Time: 14:30 (2025-12-01)                                       │
│  BP: 120/80                                                     │
│  Complaint: Chest pain                                          │
│  Status: LOCKED ✓ (Cannot be modified)                         │
│                                                                 │
│  ─────────────────────────────────────────────────────────────  │
│                                                                 │
│  VISIT 2 (NEW RECORD)                                          │
│  ═════════════════════════════════════════                       │
│  Type: Walk-in Patient                                          │
│  Time: 16:00 (2025-12-08)                                       │
│  BP: 115/75                                                     │
│  Complaint: Follow-up check                                     │
│  Doctor: Dr. Smith (D0002)                                      │
│  Diagnosis: Hypertension (resolved)                             │
│                                                                 │
│  ─────────────────────────────────────────────────────────────  │
│                                                                 │
│  VISIT 3 (NEW RECORD)                                          │
│  ═════════════════════════════════════════                       │
│  Type: Emergency Patient                                        │
│  Time: 22:00 (2025-12-15)                                       │
│  BP: 140/90                                                     │
│  Complaint: Severe headache                                     │
│  Doctor: Dr. Johnson (D0001)                                    │
│  Diagnosis: Migraine                                            │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## How to Use the Feature

### Recording a First Arrival (When Adding New Patient)

```
STEP 1: Click "Add New Patient"
        ↓
STEP 2: Fill in patient demographics
        ↓
STEP 3: Enter FIRST ARRIVAL DATA:
        - Type of Arrival
        - Incident time
        - Brought by (optional)
        - Initial vitals
        - Chief complaint
        ↓
STEP 4: Click "Save Patient"
        ↓
FIRST ARRIVAL DATA IS NOW LOCKED ✓
Cannot be changed through UI
```

### Recording a Subsequent Visit

```
STEP 1: Right-click patient in list
        ↓
STEP 2: Select "Record New Arrival/Visit"
        ↓
STEP 3: NEW VISIT DIALOG OPENS
        ↓
STEP 4: Fill in NEW ARRIVAL DATA:
        - Type of Arrival (can be different)
        - Incident time (optional)
        - Brought by (optional)
        - Initial vitals
        - Chief complaint
        - Attending doctor
        ↓
STEP 5: Click "Record Visit"
        ↓
NEW VISIT RECORD CREATED ✓
Original arrival data UNCHANGED ✓
Complete history preserved ✓
```

---

## Data Organization

### In Database (patients table)
Stores FIRST arrival only - this data is permanent.

```sql
Query:
SELECT registration_type, incident_time, initial_bp, chief_complaint
FROM patients
WHERE id = 'P0001';

Result:
registration_type | incident_time | initial_bp | chief_complaint
Emergency Patient | 14:30         | 120/80     | Chest pain
(LOCKED)          | (LOCKED)      | (LOCKED)   | (LOCKED)
```

### In Database (patient_visits table)
Stores all visits AFTER the first - one row per visit.

```sql
Query:
SELECT id, visit_date, registration_type, incident_time, 
       initial_bp, chief_complaint
FROM patient_visits
WHERE patient_id = 'P0001'
ORDER BY visit_date DESC;

Result:
id | visit_date          | registration_type | incident_time | initial_bp | chief_complaint
3  | 2025-12-15 22:00:00 | Emergency Patient | 22:00         | 140/90     | Severe headache
2  | 2025-12-10 10:30:00 | Referral Patient  | NULL          | 118/78     | Follow-up visit
1  | 2025-12-08 16:00:00 | Walk-in Patient   | 16:00         | 115/75     | Routine check
```

### In Database (patient_diagnoses table)
Stores all diagnoses - one row per diagnosis.

```sql
Query:
SELECT diagnosis, diagnosed_by, created_at
FROM patient_diagnoses
WHERE patient_id = 'P0001'
ORDER BY created_at DESC;

Result:
diagnosis          | diagnosed_by | created_at
Migraine           | D0001        | 2025-12-15 22:30:00
Hypertension       | D0002        | 2025-12-08 16:15:00
Acute MI           | D0001        | 2025-12-01 14:35:00
```

---

## Key Principles

### ✅ First Arrival Data is Locked
- Cannot be edited through UI
- Cannot be overwritten in database
- Immutable and permanent
- Available for audit trail

### ✅ New Visits Create Separate Records
- Each visit gets its own database row
- Complete independence from previous visits
- Full details captured (vitals, complaint, doctor)
- Timestamp automatically recorded

### ✅ Diagnoses Are Tracked Separately
- Each diagnosis gets its own record
- Doctor attribution captured
- Timestamp recorded
- No modification of previous diagnoses

### ✅ Complete History Available
- All visits retrievable in chronological order
- Can see entire medical timeline
- No data loss or overwrites
- Full audit trail maintained

---

## User Interface Guide

### Patient Management Panel (List View)

```
Right-click any patient →
┌─────────────────────────────────────┐
│ View / Edit                         │
│ Add Clinical Info                   │
│ Attach Files                        │
│ Record New Arrival/Visit ← NEW     │
└─────────────────────────────────────┘
```

### New Visit Dialog

When you select "Record New Arrival/Visit", this dialog opens:

```
┌──────────────────────────────────────────────────┐
│ Record New Arrival/Visit - Patient Name (ID)     │
│                                                  │
│ Recording a new visit for: John Doe (P0001)     │
│ This creates a NEW record without modifying     │
│ the original arrival data.                       │
├──────────────────────────────────────────────────┤
│                                                  │
│ Type of Arrival:              [Dropdown]         │
│   - Walk-in Patient                              │
│   - Emergency Patient          ✓ Selected        │
│   - Referral Patient                             │
│   - (5 more options)                             │
│                                                  │
│ Time of Incident (HH:mm):     [14:30]            │
│                                                  │
│ Brought By:                                      │
│ ☐ Ambulance  ☐ Family  ☐ Bystander  ☐ Police   │
│                                                  │
│ Initial BP (Sys/Dia):         [120 / 80]        │
│ Heart Rate:                   [78]               │
│ SpO2 (%):                     [98]               │
│                                                  │
│ Chief Complaint:                                 │
│ ┌────────────────────────────────────────────┐  │
│ │ Experiencing chest discomfort and slight   │  │
│ │ shortness of breath. Pain started 2 hours  │  │
│ │ ago.                                       │  │
│ └────────────────────────────────────────────┘  │
│                                                  │
│ Attending Doctor:             [Dr. Smith (D0002)]│
│                                                  │
│                    [Record Visit]  [Cancel]     │
└──────────────────────────────────────────────────┘
```

---

## What Gets Preserved

### First Arrival (patients table)
- ✅ Registration type (e.g., "Emergency Patient")
- ✅ Incident time
- ✅ Brought by information
- ✅ Initial blood pressure
- ✅ Initial heart rate
- ✅ Initial SpO2
- ✅ Chief complaint
- ✅ Creation timestamp

### Subsequent Visits (patient_visits table)
- ✅ Visit ID (auto-generated)
- ✅ Visit date/time (auto-captured)
- ✅ Registration type for that visit
- ✅ Incident details (time, brought by)
- ✅ Vitals (BP, HR, SpO2)
- ✅ Chief complaint
- ✅ Attending doctor
- ✅ Diagnosis for that visit
- ✅ Treatment plan
- ✅ Visit status

### Diagnoses (patient_diagnoses table)
- ✅ Diagnosis text
- ✅ Doctor who made diagnosis
- ✅ Timestamp of diagnosis
- ✅ Link to patient

---

## Example Workflow

### Day 1: Patient Arrives for First Time
```
1. Admin creates patient: John Doe, Age 45
2. Enters first arrival data:
   - Type: Emergency Patient
   - Time: 14:30
   - BP: 120/80
   - Complaint: Chest pain
3. Saves patient → Data LOCKED ✓

Database state:
patients table:
  P0001 | John Doe | Emergency Patient | (locked)
```

### Day 8: Patient Returns for Follow-up
```
1. Right-click patient John Doe
2. Select "Record New Arrival/Visit"
3. Enter new visit data:
   - Type: Walk-in Patient
   - Time: 16:00
   - BP: 115/75
   - Complaint: Follow-up check
   - Doctor: Dr. Smith
4. Click "Record Visit"

Database state:
patients table:
  P0001 | John Doe | Emergency Patient | (still locked)

patient_visits table:
  1 | P0001 | 2025-12-08 16:00 | Walk-in | ...
```

### Day 15: Patient Returns with New Complaint
```
1. Right-click patient John Doe
2. Select "Record New Arrival/Visit"
3. Enter new visit data:
   - Type: Emergency Patient
   - Time: 22:00
   - BP: 140/90
   - Complaint: Severe headache
   - Doctor: Dr. Johnson
4. Click "Record Visit"

Database state:
patients table:
  P0001 | John Doe | Emergency Patient | (still locked)

patient_visits table:
  1 | P0001 | 2025-12-08 16:00 | Walk-in | ...
  2 | P0001 | 2025-12-15 22:00 | Emergency | ...
```

### Query Complete History
```sql
SELECT * FROM patients WHERE id = 'P0001'
  UNION
SELECT * FROM patient_visits WHERE patient_id = 'P0001'
ORDER BY visit_date ASC;

Result: All 3 visits (1 original + 2 subsequent) displayed
in chronological order
```

---

## Important Notes

🔒 **First Arrival is Locked**
- You CANNOT edit the registration type in the edit patient dialog
- The field will be DISABLED (grayed out)
- This is intentional and by design

📝 **Each New Visit is Independent**
- Separate database record
- Can have different arrival type
- Different vitals
- Different doctor
- Different diagnosis

⏰ **Timestamps Are Automatic**
- You don't need to enter them
- Database captures them automatically
- Ensures accuracy and prevents manipulation

👤 **Doctor Attribution**
- Each visit records which doctor was attending
- Each diagnosis records who diagnosed
- Complete accountability trail

✅ **No Data Loss**
- Original data preserved forever
- All subsequent visits preserved
- Complete medical history available
- Perfect for audits and legal compliance

---

## Troubleshooting

### Q: I can't edit the arrival type for an existing patient
**A:** This is correct! The arrival type is LOCKED after first creation. This is by design to preserve the original data. If you need to record a new arrival, use "Record New Arrival/Visit" instead.

### Q: How do I see all the visits for a patient?
**A:** Run this SQL query:
```sql
SELECT id, visit_date, registration_type, diagnosis
FROM patient_visits
WHERE patient_id = 'P0001'
ORDER BY visit_date DESC;
```

### Q: Where is the first arrival data stored?
**A:** In the `patients` table, in the registration_type and related columns. This is the original arrival and it's locked.

### Q: Can I delete or modify a visit record?
**A:** The system prevents modifications of historical visits to maintain integrity. Contact your system administrator if you need to correct a record.

---

## Summary

The Arrival & Diagnosis History feature ensures:

✅ **First arrival data is permanently preserved**  
✅ **Each new visit creates a new record**  
✅ **Complete medical timeline is maintained**  
✅ **No data loss or overwrites**  
✅ **Full audit trail for compliance**  
✅ **Easy-to-use UI for recording visits**  

The system is ready for healthcare compliance and legal audits!
