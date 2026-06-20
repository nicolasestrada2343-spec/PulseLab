package com.pulselab.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "medicos")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String matricula;
    private String especialidad;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private List<Paciente> pacientes = new ArrayList<>();

    protected Medico() {
        // Constructor requerido por JPA
    }

    public Medico(String nombre, String apellido, String matricula, String especialidad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.pacientes = new ArrayList<>();
    }

    public void agregarPaciente(Paciente paciente) {
        this.pacientes.add(paciente);
        paciente.setMedico(this);
    }

    public void quitarPaciente(Paciente paciente) {
        this.pacientes.remove(paciente);
        paciente.setMedico(null);
    }

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public List<Paciente> getPacientes() { return pacientes; }

    @Override
    public String toString() {
        return "Medico{nombre='" + nombre + " " + apellido +
               "', matricula='" + matricula + "', especialidad='" + especialidad + "'}";
    }
}
