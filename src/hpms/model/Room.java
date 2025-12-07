package hpms.model;

public class Room {
    public final String id; public RoomStatus status; public String occupantPatientId;
    public Room(String id, RoomStatus status, String occupantPatientId) { this.id = id; this.status = status; this.occupantPatientId = occupantPatientId; }
}

