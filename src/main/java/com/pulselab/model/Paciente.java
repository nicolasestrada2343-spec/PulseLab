package com.pulselab.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "pacientes")
public class Paciente implements Diagnosticable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private int edad;
    private String genero;
    private double peso;
    private double altura;

    @Column(length = 2000)
    private String historial;

    @Embedded
    private SignosVitales signosVitales;

    // El ECG se calcula en tiempo real a partir del ritmo asignado;
    // no tiene sentido persistirlo en la base de datos.
    @Transient
    private Electrocardiograma ecg;

    private String ritmoAsignado;     // tipo de ritmo real, ej: "NORMAL", "TAQUICARDIA"
    private boolean modoExamen;       // true = el ritmo está oculto al estudiante
    private boolean diagnosticado;    // true = el estudiante ya respondió
    private boolean diagnosticoCorrecto;
    private String respuestaEstudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    protected Paciente() {
        // Constructor requerido por JPA
    }

    public Paciente(String nombre, String apellido, String genero, double peso, double altura) {
        this.nombre = nombre + " " + apellido;
        this.genero = genero;
        this.peso = peso;
        this.altura = altura;
        this.edad = 0;
        this.historial = "";
        this.signosVitales = new SignosVitales();
        this.modoExamen = false;
        this.diagnosticado = false;
        this.diagnosticoCorrecto = false;
    }

    //Implementación de Diagnosticable
    @Override
    public boolean evaluarDiagnostico(String respuestaEstudiante) {
        this.respuestaEstudiante = respuestaEstudiante;
        this.diagnosticado = true;
        boolean correcto = this.ritmoAsignado != null &&
                this.ritmoAsignado.equalsIgnoreCase(respuestaEstudiante.trim().replace(" ", "_"));
        this.diagnosticoCorrecto = correcto;
        return correcto;
    }

    @Override
    public String getResultadoDiagnostico() {
        if (!diagnosticado) return "Diagnóstico pendiente";
        return diagnosticoCorrecto
                ? "Correcto (" + ritmoAsignado + ")"
                : "Incorrecto. Respuesta: " + respuestaEstudiante + " | Real: " + ritmoAsignado;
    }

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }

    public String getHistorial() { return historial; }
    public void agregarHistorial(String nota) { this.historial += nota + "\n"; }

    public SignosVitales getSignosVitales() { return signosVitales; }
    public void setSignosVitales(SignosVitales sv) { this.signosVitales = sv; }

    public Electrocardiograma getEcg() { return ecg; }
    public void setEcg(Electrocardiograma ecg) { this.ecg = ecg; }

    public String getRitmoAsignado() { return ritmoAsignado; }
    public void setRitmoAsignado(String ritmoAsignado) { this.ritmoAsignado = ritmoAsignado; }

    public boolean isModoExamen() { return modoExamen; }
    public void setModoExamen(boolean modoExamen) { this.modoExamen = modoExamen; }

    public boolean isDiagnosticado() { return diagnosticado; }
    public void setDiagnosticado(boolean diagnosticado) { this.diagnosticado = diagnosticado; }

    public boolean isDiagnosticoCorrecto() { return diagnosticoCorrecto; }
    public void setDiagnosticoCorrecto(boolean diagnosticoCorrecto) { this.diagnosticoCorrecto = diagnosticoCorrecto; }

    public String getRespuestaEstudiante() { return respuestaEstudiante; }
    public void setRespuestaEstudiante(String respuestaEstudiante) { this.respuestaEstudiante = respuestaEstudiante; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    @Override
    public String toString() {
        return "Paciente{nombre='" + nombre + "', edad=" + edad + ", genero='" + genero + "'}";
    }
}
