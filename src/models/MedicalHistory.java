package models;

import java.sql.Date;

public class MedicalHistory {
    private int id;
    private Date date;
    private String description;
    private String treatment;
    private String vaccines;
    private int idPet;

    // Constructor sin ID (para crear nuevos registros)
    public MedicalHistory(Date date, String description, String treatment, String vaccines, int idPet) {
        this.date = date;
        this.description = description;
        this.treatment = treatment;
        this.vaccines = vaccines;
        this.idPet = idPet;
    }

    // Constructor con ID (para registros desde BD)
    public MedicalHistory(int id, Date date, String description, String treatment, String vaccines, int idPet) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.treatment = treatment;
        this.vaccines = vaccines;
        this.idPet = idPet;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getVaccines() {
        return vaccines;
    }

    public void setVaccines(String vaccines) {
        this.vaccines = vaccines;
    }

    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Fecha: %s | Descripción: %s | Tratamiento: %s | Vacunas: %s | Mascota ID: %d",
                id, date, description, treatment,
                (vaccines != null && !vaccines.isEmpty()) ? vaccines : "N/A", idPet);
    }
}