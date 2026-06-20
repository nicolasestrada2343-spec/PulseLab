package com.pulselab.model;

public class RitmoCardiaco {

    private String tipo;
    private String descripcion;
    private int frecuenciaMinima;
    private int frecuenciaMaxima;
    private boolean esNormal;

    public RitmoCardiaco(String tipo) {
        this.tipo = tipo;
        configurar(tipo);
    }

    private void configurar(String tipo) {
        switch (tipo) {
            case "NORMAL":
                this.descripcion = "Ritmo sinusal normal";
                this.frecuenciaMinima = 60;
                this.frecuenciaMaxima = 100;
                this.esNormal = true;
                break;
            case "TAQUICARDIA":
                this.descripcion = "Frecuencia cardíaca elevada";
                this.frecuenciaMinima = 100;
                this.frecuenciaMaxima = 220;
                this.esNormal = false;
                break;
            case "BRADICARDIA":
                this.descripcion = "Frecuencia cardíaca baja";
                this.frecuenciaMinima = 20;
                this.frecuenciaMaxima = 60;
                this.esNormal = false;
                break;
            case "FIBRILACION_AURICULAR":
                this.descripcion = "Actividad auricular caótica, sin onda P";
                this.frecuenciaMinima = 100;
                this.frecuenciaMaxima = 175;
                this.esNormal = false;
                break;
            case "FIBRILACION_VENTRICULAR":
                this.descripcion = "Arritmia ventricular grave, sin pulso";
                this.frecuenciaMinima = 200;
                this.frecuenciaMaxima = 350;
                this.esNormal = false;
                break;
            case "PVC":
                this.descripcion = "Contracción ventricular prematura";
                this.frecuenciaMinima = 60;
                this.frecuenciaMaxima = 100;
                this.esNormal = false;
                break;
            case "BLOQUEO_AV":
                this.descripcion = "Bloqueo auriculoventricular de tercer grado";
                this.frecuenciaMinima = 30;
                this.frecuenciaMaxima = 50;
                this.esNormal = false;
                break;
            default:
                this.descripcion = "Ritmo desconocido";
                this.esNormal = false;
        }
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; configurar(tipo); }

    public String getDescripcion() { return descripcion; }
    public int getFrecuenciaMinima() { return frecuenciaMinima; }
    public int getFrecuenciaMaxima() { return frecuenciaMaxima; }
    public boolean isEsNormal() { return esNormal; }

    @Override
    public String toString() {
        return "RitmoCardiaco{tipo='" + tipo + "', normal=" + esNormal + "}";
    }
}