package com.pulselab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pulselab.model.Paciente;
import com.pulselab.service.AsistenteMedicoIA;
import com.pulselab.service.Simulador;

@Controller
public class DashboardController {

    private final Simulador simulador;
    private final AsistenteMedicoIA asistenteMedicoIA;

    public DashboardController(Simulador simulador, AsistenteMedicoIA asistenteMedicoIA) {
        this.simulador = simulador;
        this.asistenteMedicoIA = asistenteMedicoIA;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("paciente", simulador.getPacienteActivo());
        model.addAttribute("pacientes", simulador.getPacientes());
        model.addAttribute("medicos", simulador.getMedicos());
        model.addAttribute("corazon", simulador.getCorazon());
        return "dashboard";
    }

    @PostMapping("/agregar-medico")
    public String agregarMedico(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String matricula,
            @RequestParam String especialidad) {
        simulador.agregarMedico(nombre, apellido, matricula, especialidad);
        return "redirect:/";
    }

    // Modo profesor: ritmo elegido a mano
    @PostMapping("/agregar-paciente")
    public String agregarPaciente(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String genero,
            @RequestParam double peso,
            @RequestParam double altura,
            @RequestParam int edad,
            @RequestParam String matriculaMedico,
            @RequestParam String ritmo,
            RedirectAttributes redirectAttributes) {
        Paciente nuevo = simulador.agregarPaciente(nombre, apellido, genero, peso, altura, edad, matriculaMedico, ritmo);
        if (nuevo == null) {
            redirectAttributes.addFlashAttribute("error", "Ya existe un paciente con ese nombre y apellido.");
        } else {
            simulador.activarPaciente(nuevo);
        }
        return "redirect:/";
    }

    // Modo examen: ritmo al azar y oculto
    @PostMapping("/agregar-paciente-examen")
    public String agregarPacienteExamen(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String genero,
            @RequestParam double peso,
            @RequestParam double altura,
            @RequestParam int edad,
            @RequestParam String matriculaMedico,
            RedirectAttributes redirectAttributes) {
        Paciente nuevo = simulador.agregarPacienteAleatorio(nombre, apellido, genero, peso, altura, edad, matriculaMedico);
        if (nuevo == null) {
            redirectAttributes.addFlashAttribute("error", "Ya existe un paciente con ese nombre y apellido.");
        } else {
            simulador.activarPaciente(nuevo);
        }
        return "redirect:/";
    }

    // Activar (poner en pantalla) un paciente ya existente
    @PostMapping("/activar-paciente")
    public String activarPaciente(@RequestParam String nombre) {
        Paciente p = simulador.buscarPacientePorNombre(nombre);
        if (p != null) simulador.activarPaciente(p);
        return "redirect:/";
    }

    // Estudiante envía su diagnóstico
    @PostMapping("/diagnosticar")
    public String diagnosticar(@RequestParam String nombre, @RequestParam String respuesta) {
        Paciente p = simulador.buscarPacientePorNombre(nombre);
        if (p != null) {
            simulador.verificarDiagnostico(p, respuesta);
        }
        return "redirect:/";
    }

    // Eliminar un paciente (DELETE en base de datos)
    @PostMapping("/eliminar-paciente")
    public String eliminarPaciente(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        boolean eliminado = simulador.eliminarPaciente(id);
        if (!eliminado) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar: paciente no encontrado.");
        }
        return "redirect:/";
    }

    // Solicitar recomendación de IA (acotada al dominio cardiológico) para el paciente activo
    @PostMapping("/recomendacion-ia")
    public String recomendacionIA(@RequestParam String nombre, RedirectAttributes redirectAttributes) {
        Paciente p = simulador.buscarPacientePorNombre(nombre);
        if (p != null) {
            String recomendacion = asistenteMedicoIA.generarRecomendacion(p);
            redirectAttributes.addFlashAttribute("recomendacionIA", recomendacion);
        }
        return "redirect:/";
    }
}
