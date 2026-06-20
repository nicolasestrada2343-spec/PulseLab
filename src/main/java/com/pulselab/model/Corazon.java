package com.pulselab.model;

import java.util.ArrayList;
import java.util.List;

public class Corazon {

    private int frecuenciaCardiaca;   // bpm
    private RitmoCardiaco ritmo;
    private List<Onda> ondas;
    private boolean enFibrilacion;
    private double intervaloPR;       // ms (normal: 120-200)
    private double duracionQRS;       // ms (normal: 60-100)
    private double intervaloQT;       // ms (normal: 350-440)

    public Corazon() {
        this.frecuenciaCardiaca = 72;
        this.ondas = new ArrayList<>();
        this.enFibrilacion = false;
        this.intervaloPR = 160;
        this.duracionQRS = 80;
        this.intervaloQT = 380;
    }

    public Corazon(int frecuenciaCardiaca, RitmoCardiaco ritmo) {
        this();
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.ritmo = ritmo;
    }

    // Genera las ondas según el ritmo actual
    public List<Onda> generarOndas() {
        ondas.clear();

        if (ritmo == null) return ondas;

        switch (ritmo.getTipo()) {
            case "NORMAL":
            case "TAQUICARDIA":
            case "BRADICARDIA":
                ondas.add(new Onda("P",   0.15, 80));
                ondas.add(new Onda("Q",  -0.12, 20));
                ondas.add(new Onda("R",   1.0,  20));
                ondas.add(new Onda("S",  -0.15, 20));
                ondas.add(new Onda("T",   0.25, 160));
                break;
            case "FIBRILACION_AURICULAR":
                // Sin onda P definida
                ondas.add(new Onda("Q",  -0.12, 20));
                ondas.add(new Onda("R",   1.0,  20));
                ondas.add(new Onda("S",  -0.15, 20));
                ondas.add(new Onda("T",   0.20, 160));
                break;
            case "FIBRILACION_VENTRICULAR":
                // Ondas caóticas
                ondas.add(new Onda("CAOS", 0.5, 400));
                break;
            case "BLOQUEO_AV":
                // P sin QRS cada cierto ciclo
                ondas.add(new Onda("P", 0.15, 80));
                break;
            default:
                break;
        }

        return ondas;
    }

    // Getters y Setters
    public int getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(int fc) { this.frecuenciaCardiaca = fc; }

    public RitmoCardiaco getRitmo() { return ritmo; }
    public void setRitmo(RitmoCardiaco ritmo) { this.ritmo = ritmo; }

    public List<Onda> getOndas() { return ondas; }

    public boolean isEnFibrilacion() { return enFibrilacion; }
    public void setEnFibrilacion(boolean enFibrilacion) { this.enFibrilacion = enFibrilacion; }

    public double getIntervaloPR() { return intervaloPR; }
    public void setIntervaloPR(double intervaloPR) { this.intervaloPR = intervaloPR; }

    public double getDuracionQRS() { return duracionQRS; }
    public void setDuracionQRS(double duracionQRS) { this.duracionQRS = duracionQRS; }

    public double getIntervaloQT() { return intervaloQT; }
    public void setIntervaloQT(double intervaloQT) { this.intervaloQT = intervaloQT; }

    @Override
    public String toString() {
        return "Corazon{fc=" + frecuenciaCardiaca +
               ", ritmo=" + (ritmo != null ? ritmo.getTipo() : "sin ritmo") +
               ", ondas=" + ondas.size() + "}";
    }
}