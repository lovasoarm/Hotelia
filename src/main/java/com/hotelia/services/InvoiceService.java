package com.hotelia.service;

import com.hotelia.dao.InvoiceDAO;
import com.hotelia.dao.ReservationDAO;
import com.hotelia.enums.ReservationStatus;
import com.hotelia.model.Invoice;
import com.hotelia.model.Reservation;
import java.util.List;

public class InvoiceService {

    private final InvoiceDAO invoiceDAO;
    private final ReservationDAO reservationDAO;
    private final AuditLogService auditLogService;

    public InvoiceService(InvoiceDAO invoiceDAO,
                          ReservationDAO reservationDAO,
                          AuditLogService auditLogService) {
        this.invoiceDAO       = invoiceDAO;
        this.reservationDAO   = reservationDAO;
        this.auditLogService  = auditLogService;
    }

    public void generateInvoice(Long reservationId, String username) {
        Reservation reservation = reservationDAO.find(reservationId);
        if (reservation == null) {
            throw new IllegalStateException("Réservation introuvable");
        }

        if (reservation.getStatus() != ReservationStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Impossible : la réservation doit être terminée (COMPLETED)"
            );
        }

        if (findByReservation(reservationId) != null) {
            throw new IllegalStateException(
                    "Une facture existe déjà pour cette réservation"
            );
        }
        Invoice invoice = new Invoice(reservation);
        invoiceDAO.create(invoice);
        auditLogService.log(
                "GENERATE_INVOICE", username,
                "Facture générée pour réservation #" + reservationId +
                        " — Total: " + invoice.getTotalAmount() + " Ar"
        );
    }

    public void markAsPaid(Long id, String username) {
        Invoice invoice = findById(id);
        if (invoice.isPaid()) {
            throw new IllegalStateException("Cette facture est déjà payée");
        }
        invoice.markAsPaid();
        invoiceDAO.update(invoice);
        auditLogService.log(
                "PAY_INVOICE", username,
                "Facture #" + id + " marquée comme payée"
        );
    }

    public Invoice findById(Long id) {
        Invoice invoice = invoiceDAO.find(id);
        if (invoice == null) {
            throw new IllegalStateException("Facture introuvable");
        }
        return invoice;
    }

    public Invoice findByReservation(Long reservationId) {
        try {
            return invoiceDAO.findByReservation(reservationId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Invoice> findAll() {
        return invoiceDAO.findAll();
    }
}