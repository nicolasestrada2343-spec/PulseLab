package com.pulselab.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulselab.model.Alarma;
import com.pulselab.model.Corazon;
import com.pulselab.model.Derivacion;
import com.pulselab.model.Electrocardiograma;
import com.pulselab.model.Medico;
import com.pulselab.model.Paciente;
import com.pulselab.model.RitmoCardiaco;
import com.pulselab.repository.MedicoRepository;
import com.pulselab.repository.PacienteRepository;

/**
 * Orquesta la simulación de ECG y delega toda la persistencia
 * (pacientes y médicos) a los repositorios JPA (base de datos H2).
 */
@Service
public class Simulador {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    private Paciente pacienteActivo;
    private Corazon corazon;
    private Electrocardiograma ecg;
    private Alarma alarmaFC;
    private boolean corriendo;

    private static final String[] RITMOS_POSIBLES = {
        "NORMAL", "TAQUICARDIA", "BRADICARDIA",
        "FIBRILACION_AURICULAR", "FIBRILACION_VENTRICULAR",
        "PVC", "BLOQUEO_AV"
    };

    public Simulador(PacienteRepository pacienteRepository, MedicoRepository medicoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        inicializar();
    }

    // Si la base de datos está vacía (primer arranque), se siembra un médico y un paciente de ejemplo.
    private void inicializar() {
        if (medicoRepository.count() == 0) {
            Medico medicoInicial = new Medico("Roberto", "García", "CR-4521", "Cardiología");
            medicoRepository.save(medicoInicial);

            Paciente pacienteInicial = new Paciente("Juan Carlos", "Pérez", "M", 78, 175);
            pacienteInicial.setEdad(54);
            pacienteInicial.setRitmoAsignado("NORMAL");
            pacienteInicial.setModoExamen(false);
            pacienteInicial.setMedico(medicoInicial);
            pacienteRepository.save(pacienteInicial);

            this.pacienteActivo = pacienteInicial;
        } else {
            this.pacienteActivo = pacienteRepository.findAll().get(0);
        }

        RitmoCardiaco ritmo = new RitmoCardiaco(pacienteActivo.getRitmoAsignado());
        this.corazon = new Corazon(72, ritmo);
        corazon.generarOndas();

        this.ecg = new Electrocardiograma(pacienteActivo);
        ecg.setRitmo(ritmo);
        ecg.agregarDerivacion(new Derivacion("I",   0.75));
        ecg.agregarDerivacion(new Derivacion("II",  1.0));
        ecg.agregarDerivacion(new Derivacion("aVR", -0.9));
        ecg.agregarDerivacion(new Derivacion("V1",  -0.5));

        this.alarmaFC = new Alarma("FC", 50, 120);
        this.corriendo = true;
    }

    // ── CREATE ──────────────────────────────────────────────────────
    public Medico agregarMedico(String nombre, String apellido, String matricula, String especialidad) {
        Medico nuevo = new Medico(nombre, apellido, matricula, especialidad);
        return medicoRepository.save(nuevo);
    }

    public boolean existePaciente(String nombreCompleto) {
        return pacienteRepository.findByNombre(nombreCompleto).isPresent();
    }

    // ── Modo profesor: el ritmo se elige a mano ──
    @Transactional
    public Paciente agregarPaciente(String nombre, String apellido, String genero, double peso, double altura,
                                     int edad, String matriculaMedico, String ritmo) {
        String nombreCompleto = nombre + " " + apellido;
        if (existePaciente(nombreCompleto)) {
            return null; // ya existe, no se agrega
        }

        Paciente nuevo = new Paciente(nombre, apellido, genero, peso, altura);
        nuevo.setEdad(edad);
        nuevo.setRitmoAsignado(ritmo);
        nuevo.setModoExamen(false);

        Medico medico = buscarMedicoPorMatricula(matriculaMedico);
        if (medico != null) nuevo.setMedico(medico);

        return pacienteRepository.save(nuevo);
    }

    // ── Modo examen: el ritmo se asigna al azar y se oculta ──
    @Transactional
    public Paciente agregarPacienteAleatorio(String nombre, String apellido, String genero, double peso, double altura,
                                              int edad, String matriculaMedico) {
        String nombreCompleto = nombre + " " + apellido;
        if (existePaciente(nombreCompleto)) {
            return null; // ya existe, no se agrega
        }

        Paciente nuevo = new Paciente(nombre, apellido, genero, peso, altura);
        nuevo.setEdad(edad);

        Random rand = new Random();
        String ritmoAleatorio = RITMOS_POSIBLES[rand.nextInt(RITMOS_POSIBLES.length)];
        nuevo.setRitmoAsignado(ritmoAleatorio);
        nuevo.setModoExamen(true);

        Medico medico = buscarMedicoPorMatricula(matriculaMedico);
        if (medico != null) nuevo.setMedico(medico);

        return pacienteRepository.save(nuevo);
    }

    // ── READ ────────────────────────────────────────────────────────
    public Medico buscarMedicoPorMatricula(String matricula) {
        return medicoRepository.findByMatricula(matricula).orElse(null);
    }

    public Paciente buscarPacientePorNombre(String nombre) {
        return pacienteRepository.findByNombre(nombre).orElse(null);
    }

    public List<Paciente> getPacientes() { return pacienteRepository.findAll(); }
    public List<Medico> getMedicos() { return medicoRepository.findAll(); }

    // ── UPDATE ──────────────────────────────────────────────────────
    // Usa el contrato Diagnosticable: cualquier objeto diagnosticable
    // podría pasar por este mismo flujo de evaluación.
    @Transactional
    public boolean verificarDiagnostico(Paciente paciente, String respuesta) {
        boolean correcto = paciente.evaluarDiagnostico(respuesta);
        pacienteRepository.save(paciente); // UPDATE en la base de datos
        return correcto;
    }

    // ── DELETE ──────────────────────────────────────────────────────
    @Transactional
    public boolean eliminarPaciente(Long id) {
        if (!pacienteRepository.existsById(id)) return false;
        pacienteRepository.deleteById(id);
        return true;
    }

    // ── Simulación en memoria (no se persiste, se recalcula siempre) ──
    public void cambiarRitmo(String tipo) {
        RitmoCardiaco nuevoRitmo = new RitmoCardiaco(tipo);
        corazon.setRitmo(nuevoRitmo);
        corazon.generarOndas();
        ecg.setRitmo(nuevoRitmo);
    }

    public void activarPaciente(Paciente p) {
        this.pacienteActivo = p;
        cambiarRitmo(p.getRitmoAsignado());
    }

    public Paciente getPacienteActivo() { return pacienteActivo; }
    public void setPacienteActivo(Paciente p) { this.pacienteActivo = p; }
    public Corazon getCorazon() { return corazon; }
    public Electrocardiograma getEcg() { return ecg; }
    public Alarma getAlarmaFC() { return alarmaFC; }
    public boolean isCorriendo() { return corriendo; }
    public void setCorriendo(boolean corriendo) { this.corriendo = corriendo; }
}
