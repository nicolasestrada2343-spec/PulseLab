package com.pulselab.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class SignosVitales {

    private int frecuenciaCardiaca;   // bpm
    private int saturacionOxigeno;    // %
    private int presionSistolica;     // mmHg
    private int presionDiastolica;    // mmHg
    private int frecuenciaRespiratoria; // rpm
    private double temperatura;       // °C

    public SignosVitales() {
        this.frecuenciaCardiaca = 72;
        this.saturacionOxigeno = 98;
        this.presionSistolica = 120;
        this.presionDiastolica = 80;
        this.frecuenciaRespiratoria = 16;
        this.temperatura = 36.7;
    }
    public int getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(int fc) { this.frecuenciaCardiaca = fc; }

    public int getSaturacionOxigeno() { return saturacionOxigeno; }
    public void setSaturacionOxigeno(int spo2) { this.saturacionOxigeno = spo2; }

    public int getPresionSistolica() { return presionSistolica; }
    public void setPresionSistolica(int ps) { this.presionSistolica = ps; }

    public int getPresionDiastolica() { return presionDiastolica; }
    public void setPresionDiastolica(int pd) { this.presionDiastolica = pd; }

    public int getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
    public void setFrecuenciaRespiratoria(int fr) { this.frecuenciaRespiratoria = fr; }

    public double getTemperatura() { return temperatura; }
    public void setTemperatura(double temp) { this.temperatura = temp; }

    @Override
    public String toString() {
        return "SignosVitales{FC=" + frecuenciaCardiaca +
               ", SpO2=" + saturacionOxigeno +
               ", PA=" + presionSistolica + "/" + presionDiastolica +
               ", Temp=" + temperatura + "}";
    }
}