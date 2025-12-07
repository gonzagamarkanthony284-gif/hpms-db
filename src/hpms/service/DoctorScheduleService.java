package hpms.service;

import hpms.model.*;
import hpms.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.*;

public class DoctorScheduleService {
    
    public static List<String> setSchedule(String doctorId, String dayOfWeek, String startTime, String endTime, int slotDuration) {
        List<String> out = new ArrayList<>();
        
        if (DataStore.staff.get(doctorId) == null) {
            out.add("Error: Doctor not found");
            return out;
        }
        
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            
            if (start.isAfter(end)) {
                out.add("Error: Start time must be before end time");
                return out;
            }
            
            DoctorSchedule schedule = new DoctorSchedule(doctorId, dayOfWeek, start, end);
            schedule.slotDurationMinutes = slotDuration;
            
            DataStore.doctorSchedules.put(doctorId + "_" + dayOfWeek, schedule);
            DataStore.log("set_doctor_schedule " + doctorId + " " + dayOfWeek);
            out.add("Schedule set successfully");
            
        } catch (Exception e) {
            out.add("Error: Invalid time format");
        }
        
        return out;
    }
    
    public static List<LocalTime> getAvailableSlots(String doctorId, LocalDate date) {
        List<LocalTime> slots = new ArrayList<>();
        
        // Get schedule for this day of week
        String dayOfWeek = date.getDayOfWeek().toString();
        DoctorSchedule schedule = DataStore.doctorSchedules.get(doctorId + "_" + dayOfWeek);
        
        if (schedule == null || !schedule.isAvailable(date, schedule.startTime)) {
            return slots;  // No schedule or off date
        }
        
        // Get all available times
        LocalTime current = schedule.startTime;
        while (current.isBefore(schedule.endTime)) {
            // Check if slot is already booked
            boolean isBooked = false;
            for (Appointment apt : DataStore.appointments.values()) {
                if (apt.staffId.equals(doctorId) && 
                    apt.dateTime.toLocalDate().equals(date) && 
                    apt.dateTime.toLocalTime().equals(current)) {
                    isBooked = true;
                    break;
                }
            }
            
            if (!isBooked) {
                slots.add(current);
            }
            current = current.plusMinutes(schedule.slotDurationMinutes);
        }
        
        return slots;
    }
    
    public static List<String> addOffDate(String doctorId, String date) {
        List<String> out = new ArrayList<>();
        
        try {
            LocalDate offDate = LocalDate.parse(date);
            String dayOfWeek = offDate.getDayOfWeek().toString();
            DoctorSchedule schedule = DataStore.doctorSchedules.get(doctorId + "_" + dayOfWeek);
            
            if (schedule == null) {
                // Create a default schedule and mark as off
                schedule = new DoctorSchedule(doctorId, dayOfWeek, LocalTime.of(9, 0), LocalTime.of(17, 0));
                DataStore.doctorSchedules.put(doctorId + "_" + dayOfWeek, schedule);
            }
            
            schedule.offDates.add(offDate);
            DataStore.log("add_off_date " + doctorId + " " + date);
            out.add("Off date added");
            
        } catch (Exception e) {
            out.add("Error: Invalid date format");
        }
        
        return out;
    }
}
