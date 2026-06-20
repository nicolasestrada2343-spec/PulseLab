package com.pulselab.model;

public class Alarma {

    private String tipo;
    private String mensaje;
    private int umbralMinimo;
    private int umbralMaximo;
    private boolean activa;

    public Alarma(String tipo, int umbralMinimo, int umbralMaximo) {
        this.tipo = tipo;
        this.umbralMinimo = umbralMinimo;
        this.umbralMaximo = umbralMaximo;
        this.activa = false;
    }

    public boolean verificar(int valorActual) {
        if (valorActual < umbralMinimo || valorActual > umbralMaximo) {
            this.activa = true;
            this.mensaje = "⚠ ALARMA " + tipo + ": valor " + valorActual + " fuera de rango [" + umbralMinimo + "-" + umbralMaximo + "]";
            return true;
        }
        this.activa = false;
        this.mensaje = "";
        return false;
    }

    public String getTipo() { return tipo; }
    public String getMensaje() { return mensaje; }
    public int getUmbralMinimo() { return umbralMinimo; }
    public void setUmbralMinimo(int v) { this.umbralMinimo = v; }
    public int getUmbralMaximo() { return umbralMaximo; }
    public void setUmbralMaximo(int v) { this.umbralMaximo = v; }
    public boolean isActiva() { return activa; }

    @Override
    public String toString() {
        return "Alarma{tipo='" + tipo + "', rango=[" + umbralMinimo + "-" + umbralMaximo + "], activa=" + activa + "}";
    }
}