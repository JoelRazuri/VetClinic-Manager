package models;

import java.sql.Date;

public class MedicalShift {
    private int id;
    private String reason;
    private Date date;
    private double price;
    private String status;
    private int idPet;
    private int idClient;

    // Constructor sin ID (para crear nuevos registros)
    public MedicalShift(String reason, Date date, double price, String status, int idPet, int idClient) {
        this.reason = reason;
        this.date = date;
        this.price = price;
        this.status = status;
        this.idPet = idPet;
        this.idClient = idClient;
    }

    // Constructor con ID (para registros desde BD)
    public MedicalShift(int id, String reason, Date date, double price, String status, int idPet, int idClient) {
        this.id = id;
        this.reason = reason;
        this.date = date;
        this.price = price;
        this.status = status;
        this.idPet = idPet;
        this.idClient = idClient;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Motivo: %s | Fecha: %s | Precio: $%.2f | Estado: %s | Mascota ID: %d | Cliente ID: %d",
                id, reason, date, price, status, idPet, idClient);
    }
}