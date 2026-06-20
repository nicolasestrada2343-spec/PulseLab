package com.pulselab.model;

import java.util.ArrayList;
import java.util.List;

public class Electrocardiograma {

    private Paciente paciente;
    private List<Derivacion> derivaciones;
    private RitmoCardiaco ritmo;
    private double velocidadPapel;
    private double calibracion;
    private String fecha;
    private String observaciones;

    public Electrocardiograma(Paciente paciente) {
        this.paciente = paciente;
        this.derivaciones = new ArrayList<>();
        this.velocidadPapel = 25.0;
        this.calibracion = 10.0;
        this.observaciones = "";
    }

    public void agregarDerivacion(Derivacion derivacion) {
        this.derivaciones.add(derivacion);
    }

    public Derivacion getDerivacion(String nombre) {
        for (Derivacion d : derivaciones) {
            if (d.getNombre().equals(nombre)) return d;
        }
        return null;
    }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public List<Derivacion> getDerivaciones() { return derivaciones; }

    public RitmoCardiaco getRitmo() { return ritmo; }
    public void setRitmo(RitmoCardiaco ritmo) { this.ritmo = ritmo; }

    public double getVelocidadPapel() { return velocidadPapel; }
    public void setVelocidadPapel(double velocidadPapel) { this.velocidadPapel = velocidadPapel; }

    public double getCalibracion() { return calibracion; }
    public void setCalibracion(double calibracion) { this.calibracion = calibracion; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String obs) { this.observaciones = obs; }

    @Override
    public String toString() {
        return "Electrocardiograma{paciente=" + paciente.getNombre() +
               ", derivaciones=" + derivaciones.size() +
               ", ritmo=" + (ritmo != null ? ritmo.getTipo() : "sin ritmo") + "}";
    }
}