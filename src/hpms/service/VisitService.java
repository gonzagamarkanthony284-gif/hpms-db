package hpms.service;

import hpms.model.PatientVisit;
import hpms.util.DBConnection;
import hpms.util.LogManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing patient visit/arrival records.
 * Each visit creates a NEW record - original visits are never modified.
 */
public class VisitService {

    /**
     * Create a new patient visit record.
     * This should be called every time a patient arrives for care.
     * 
     * @return List of status messages
     */
    public static List<String> createVisit(String patientId, String registrationType,
            String incidentTime, String broughtBy, String initialBp, String initialHr,
            String initialSpo2, String chiefComplaint, String attendingDoctor) {

        List<String> out = new ArrayList<>();

        if (patientId == null || patientId.trim().isEmpty()) {
            out.add("Error: Patient ID is required");
            return out;
        }

        if (registrationType == null || registrationType.trim().isEmpty()) {
            registrationType = "Walk-in Patient";
        }

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            out.add("Error: Database connection failed");
            return out;
        }

        String sql = "INSERT INTO patient_visits (patient_id, registration_type, incident_time, " +
                "brought_by, initial_bp, initial_hr, initial_spo2, chief_complaint, attending_doctor) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, patientId);
            stmt.setString(2, registrationType);
            stmt.setString(3, incidentTime);
            stmt.setString(4, broughtBy);
            stmt.setString(5, initialBp);
            stmt.setString(6, initialHr);
            stmt.setString(7, initialSpo2);
            stmt.setString(8, chiefComplaint);
            stmt.setString(9, attendingDoctor);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int visitId = rs.getInt(1);
                    out.add("Visit created successfully - Visit ID: " + visitId);
                    LogManager.log("new_visit_created patient=" + patientId + " type=" + registrationType + " visitId="
                            + visitId);
                }
            } else {
                out.add("Error: Failed to create visit record");
            }
        } catch (SQLException e) {
            out.add("Error: " + e.getMessage());
            LogManager.log("visit_creation_error: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }

        return out;
    }

    /**
     * Add diagnosis to an existing visit
     */
    public static List<String> addDiagnosisToVisit(int visitId, String diagnosis, String attendingDoctor) {
        List<String> out = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            out.add("Error: Database connection failed");
            return out;
        }

        String sql = "UPDATE patient_visits SET diagnosis = ?, attending_doctor = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, diagnosis);
            stmt.setString(2, attendingDoctor);
            stmt.setInt(3, visitId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                out.add("Diagnosis added to visit #" + visitId);
                LogManager.log("diagnosis_added_to_visit visitId=" + visitId + " doctor=" + attendingDoctor);
            } else {
                out.add("Error: Visit not found");
            }
        } catch (SQLException e) {
            out.add("Error: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }

        return out;
    }

    /**
     * Create a new diagnosis record (separate from visit)
     * This is for standalone diagnosis entries
     */
    public static List<String> createDiagnosis(String patientId, String diagnosis, String diagnosedBy) {
        List<String> out = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            out.add("Error: Database connection failed");
            return out;
        }

        String sql = "INSERT INTO patient_diagnoses (patient_id, diagnosis, diagnosed_by) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            stmt.setString(2, diagnosis);
            stmt.setString(3, diagnosedBy);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                out.add("New diagnosis record created");
                LogManager.log("new_diagnosis patient=" + patientId + " by=" + diagnosedBy);
            } else {
                out.add("Error: Failed to create diagnosis");
            }
        } catch (SQLException e) {
            out.add("Error: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }

        return out;
    }

    /**
     * Get all visits for a patient
     */
    public static List<PatientVisit> getVisitHistory(String patientId) {
        List<PatientVisit> visits = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            return visits;
        }

        String sql = "SELECT * FROM patient_visits WHERE patient_id = ? ORDER BY visit_date DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String pid = rs.getString("patient_id");
                Timestamp ts = rs.getTimestamp("visit_date");
                LocalDateTime visitDate = ts.toLocalDateTime();
                String regType = rs.getString("registration_type");

                PatientVisit visit = new PatientVisit(id, pid, visitDate, regType);
                visit.incidentTime = rs.getString("incident_time");
                visit.broughtBy = rs.getString("brought_by");
                visit.initialBp = rs.getString("initial_bp");
                visit.initialHr = rs.getString("initial_hr");
                visit.initialSpo2 = rs.getString("initial_spo2");
                visit.chiefComplaint = rs.getString("chief_complaint");
                visit.attendingDoctor = rs.getString("attending_doctor");
                visit.diagnosis = rs.getString("diagnosis");
                visit.treatmentPlan = rs.getString("treatment_plan");
                visit.notes = rs.getString("notes");
                visit.visitStatus = rs.getString("visit_status");

                visits.add(visit);
            }
        } catch (SQLException e) {
            LogManager.log("visit_history_error: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }

        return visits;
    }

    /**
     * Get all diagnosis records for a patient
     */
    public static List<String> getDiagnosisHistory(String patientId) {
        List<String> diagnoses = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            return diagnoses;
        }

        String sql = "SELECT diagnosis, diagnosed_by, created_at FROM patient_diagnoses " +
                "WHERE patient_id = ? ORDER BY created_at DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String diagnosis = rs.getString("diagnosis");
                String diagnosedBy = rs.getString("diagnosed_by");
                Timestamp ts = rs.getTimestamp("created_at");
                String entry = "[" + ts + "] " + diagnosis +
                        (diagnosedBy != null ? " (by: " + diagnosedBy + ")" : "");
                diagnoses.add(entry);
            }
        } catch (SQLException e) {
            LogManager.log("diagnosis_history_error: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }

        return diagnoses;
    }

    /**
     * Get the most recent visit for a patient
     */
    public static PatientVisit getLatestVisit(String patientId) {
        List<PatientVisit> visits = getVisitHistory(patientId);
        return visits.isEmpty() ? null : visits.get(0);
    }

    /**
     * Update visit status
     */
    public static List<String> updateVisitStatus(int visitId, String status) {
        List<String> out = new ArrayList<>();

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            out.add("Error: Database connection failed");
            return out;
        }

        String sql = "UPDATE patient_visits SET visit_status = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, visitId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                out.add("Visit status updated");
            } else {
                out.add("Error: Visit not found");
            }
        } catch (SQLException e) {
            out.add("Error: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }

        return out;
    }
}
