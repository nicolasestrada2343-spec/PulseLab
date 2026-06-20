package com.pulselab.model;

import java.util.List;

public class Derivacion {

    private String nombre;        // "I", "II", "III", "aVR", "V1"...
    private double factor;        // factor de transformación respecto a lead II
    private List<Onda> ondas;

    public Derivacion(String nombre, double factor) {
        this.nombre = nombre;
        this.factor = factor;
    }
    public double transformar(double valorLeadII) {
        return valorLeadII * factor;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getFactor() { return factor; }
    public void setFactor(double factor) { this.factor = factor; }

    public List<Onda> getOndas() { return ondas; }
    public void setOndas(List<Onda> ondas) { this.ondas = ondas; }

    @Override
    public String toString() {
        return "Derivacion{nombre='" + nombre + "', factor=" + factor + "}";
    }
}