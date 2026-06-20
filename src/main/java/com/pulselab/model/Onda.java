package com.pulselab.model;

public class Onda {

    private String tipo;         // "P", "Q", "R", "S", "T", "CAOS"
    private double amplitud;     // mV
    private double duracion;     // ms

    public Onda(String tipo, double amplitud, double duracion) {
        this.tipo = tipo;
        this.amplitud = amplitud;
        this.duracion = duracion;
    }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getAmplitud() { return amplitud; }
    public void setAmplitud(double amplitud) { this.amplitud = amplitud; }

    public double getDuracion() { return duracion; }
    public void setDuracion(double duracion) { this.duracion = duracion; }

    @Override
    public String toString() {
        return "Onda{tipo='" + tipo + "', amplitud=" + amplitud + ", duracion=" + duracion + "ms}";
    }
 }