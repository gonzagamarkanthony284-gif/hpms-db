# Outpatient Rules Implementation

## Completed Tasks
- [x] Modify RoomService.assign() to check if patient status is OUTPATIENT and prevent assignment.
- [x] Modify DischargeService.discharge() to check if patient is OUTPATIENT and if there was no INPATIENT status on the same day, prevent discharge.

## Followup Steps
- [ ] Test the changes to ensure outpatients cannot be assigned rooms.
- [ ] Test discharge logic for outpatients.
