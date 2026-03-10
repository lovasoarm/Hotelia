package com.hotelia.service;

import com.hotelia.daos.ClientDAO;
import com.hotelia.daos.ReservationDAO;
import com.hotelia.daos.RoomDAO;
import com.hotelia.enums.ReservationStatus;
import com.hotelia.models.Client;
import com.hotelia.models.Reservation;
import com.hotelia.models.Room;
import java.time.LocalDate;
import java.util.List;

public class ReservationService {

    private final ReservationDAO reservationDAO;
    private final RoomDAO roomDAO;
    private final ClientDAO clientDAO;
    private final AuditLogService auditLogService;

    public ReservationService(ReservationDAO reservationDAO,
                              RoomDAO roomDAO,
                              ClientDAO clientDAO,
                              AuditLogService auditLogService) {
        this.reservationDAO  = reservationDAO;
        this.roomDAO         = roomDAO;
        this.clientDAO       = clientDAO;
        this.auditLogService = auditLogService;
    }

    public void createReservation(Long clientId, Long roomId,
                                  LocalDate checkIn, LocalDate checkOut,
                                  String username) {

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalStateException("La date de départ doit être après l'arrivée");
        }

        Client client = clientDAO.find(clientId);
        if (client == null) {
            throw new IllegalStateException("Client introuvable");
        }

        Room room = roomDAO.find(roomId);
        if (room == null) {
            throw new IllegalStateException("Chambre introuvable");
        }

        if (!room.isAvailable(checkIn, checkOut)) {
            throw new IllegalStateException("Chambre non disponible sur ces dates");
        }

        Reservation reservation = new Reservation(client, room, checkIn, checkOut);
        reservationDAO.create(reservation);

        auditLogService.log(
                "CREATE_RESERVATION", username,
                "Réservation créée — Client: " + client.getFullName() +
                        " Chambre: " + room.getRoomNumber()
        );
    }

    public void confirm(Long id, String username) {
        Reservation r = findById(id);

        if (r.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Seule une réservation PENDING peut être confirmée");
        }
        r.confirm();
        reservationDAO.update(r);
        auditLogService.log("CONFIRM_RESERVATION", username, "Réservation #" + id + " confirmée");
    }

    public void cancel(Long id, String username) {
        Reservation r = findById(id);

        if (r.getStatus() == ReservationStatus.COMPLETED) {
            throw new IllegalStateException("Impossible d'annuler une réservation terminée");
        }
        r.cancel();
        reservationDAO.update(r);
        auditLogService.log("CANCEL_RESERVATION", username, "Réservation #" + id + " annulée");
    }

    public void complete(Long id, String username) {
        Reservation r = findById(id);
        if (r.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Seule une réservation confirmée peut être terminée");
        }
        r.complete();
        reservationDAO.update(r);
        auditLogService.log("COMPLETE_RESERVATION", username, "Réservation #" + id + " terminée");
    }

    public Reservation findById(Long id) {
        Reservation r = reservationDAO.find(id);
        if (r == null) {
            throw new IllegalStateException("Réservation introuvable");
        }
        return r;
    }

    public List<Reservation> findAll() {
        return reservationDAO.findAll();
    }
}