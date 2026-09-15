package com.cecamed.model;

import java.time.LocalDateTime;

public class Auditoria {
    private int id;
    private String usuario;
    private String accion; // CREAR, MODIFICAR, ELIMINAR, LOGIN, LOGOUT
    private String entidad; // Paciente, Cita, HistorialMedico, Usuario
    private int idEntidad;
    private String detalles;
    private LocalDateTime fechaHora;

    public Auditoria() {}

    public Auditoria(String usuario, String accion, String entidad, int idEntidad, String detalles) {
        this.usuario = usuario;
        this.accion = accion;
        this.entidad = entidad;
        this.idEntidad = idEntidad;
        this.detalles = detalles;
        this.fechaHora = LocalDateTime.now();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getEntidad() { return entidad; }
    public void setEntidad(String entidad) { this.entidad = entidad; }

    public int getIdEntidad() { return idEntidad; }
    public void setIdEntidad(int idEntidad) { this.idEntidad = idEntidad; }

    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) { this.detalles = detalles; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    @Override
    public String toString() {
        return "Auditoria{" +
                "usuario='" + usuario + '\'' +
                ", accion='" + accion + '\'' +
                ", entidad='" + entidad + '\'' +
                ", fechaHora=" + fechaHora +
                '}';
    }
}
