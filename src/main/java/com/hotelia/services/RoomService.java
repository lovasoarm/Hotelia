package com.hotelia.service;

import com.hotelia.dao.RoomDAO;
import com.hotelia.enums.RoomStatus;
import com.hotelia.model.Room;
import java.util.List;

public class RoomService {

    private final RoomDAO roomDAO;
    private final AuditLogService auditLogService;

    public RoomService(RoomDAO roomDAO, AuditLogService auditLogService) {
        this.roomDAO         = roomDAO;
        this.auditLogService = auditLogService;
    }

    public void createRoom(Room room, String username) {

        if (room.getRoomNumber() == null || room.getRoomNumber().isBlank()) {
            throw new IllegalStateException("Le numéro de chambre est obligatoire");
        }
        if (room.getPricePerNight() <= 0) {
            throw new IllegalStateException("Le prix doit être supérieur à 0");
        }
        roomDAO.create(room);
        auditLogService.log(
                "CREATE_ROOM", username,
                "Chambre " + room.getRoomNumber() + " créée"
        );
    }

    public Room findById(Long id) {
        Room room = roomDAO.find(id);
        if (room == null) {
            throw new IllegalStateException("Chambre introuvable");
        }
        return room;
    }

    public List<Room> findAll() {
        return roomDAO.findAll();
    }

    public void updateRoom(Room room, String username) {
        if (room == null) {
            throw new IllegalStateException("Chambre invalide");
        }
        roomDAO.update(room);
        auditLogService.log(
                "UPDATE_ROOM", username,
                "Chambre #" + room.getId() + " mise à jour"
        );
    }

    public void updateStatus(Long id, RoomStatus status, String username) {
        Room room = findById(id);
        room.updateStatus(status);
        roomDAO.update(room);
        auditLogService.log(
                "UPDATE_ROOM_STATUS", username,
                "Chambre " + room.getRoomNumber() + " → " + status.name()
        );
    }

    public void deleteRoom(Long id, String username) {
        Room room = findById(id);

        if (!room.getReservations().isEmpty()) {
            throw new IllegalStateException(
                    "Impossible : cette chambre a des réservations"
            );
        }
        roomDAO.delete(room);
        auditLogService.log(
                "DELETE_ROOM", username,
                "Chambre " + room.getRoomNumber() + " supprimée"
        );
    }
}