package hpms.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DoctorSchedule {
    public final String doctorId;
    public String dayOfWeek;  // Monday, Tuesday, etc. or "Daily"
    public LocalTime startTime;  // e.g., 09:00
    public LocalTime endTime;    // e.g., 17:00
    public int slotDurationMinutes = 30;  // Duration of each appointment slot
    public boolean isActive = true;
    public List<LocalDate> offDates = new ArrayList<>();  // Specific dates doctor is unavailable
    
    public DoctorSchedule(String doctorId, String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.doctorId = doctorId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    /**
     * Check if doctor is available at a specific date and time
     */
    public boolean isAvailable(LocalDate date, LocalTime time) {
        if (!isActive) return false;
        if (offDates.contains(date)) return false;
        if (time.isBefore(startTime) || time.isAfter(endTime)) return false;
        return true;
    }
    
    /**
     * Get available time slots for a given date
     */
    public List<LocalTime> getAvailableSlots(LocalDate date) {
        List<LocalTime> slots = new ArrayList<>();
        if (!isActive || offDates.contains(date)) return slots;
        
        LocalTime current = startTime;
        while (current.isBefore(endTime)) {
            slots.add(current);
            current = current.plusMinutes(slotDurationMinutes);
        }
        return slots;
    }
}
