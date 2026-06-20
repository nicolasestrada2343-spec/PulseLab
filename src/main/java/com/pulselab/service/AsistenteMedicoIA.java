package com.pulselab.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.pulselab.model.Paciente;
import com.pulselab.model.SignosVitales;

/**
 * Servicio de Inteligencia Artificial de PulseLab.
 *
 * Consume la API de Groq (modelos Llama, compatible con el formato de
 * OpenAI Chat Completions) para generar recomendaciones de cuidado a
 * partir del diagnóstico cardíaco de un paciente.
 *
 * RESTRICCIÓN DE DOMINIO (obligatoria según el enunciado del proyecto):
 * el asistente está instruido mediante un "system prompt" para responder
 * EXCLUSIVAMENTE sobre arritmias y cuidado cardíaco dentro de PulseLab.
 * No debe comportarse como un chatbot general ni responder temas ajenos
 * (entretenimiento, otras especialidades médicas, programación, etc.).
 */
@Service
public class AsistenteMedicoIA {

    private final RestClient restClient;
    private final String apiKey;
    private final String apiUrl;
    private final String modelo;

    private static final String SYSTEM_PROMPT = """
            Eres el Asistente Médico de PulseLab, un simulador educativo de electrocardiogramas (ECG).

            TU ÚNICO PROPÓSITO es explicar, en lenguaje claro para un estudiante de medicina,
            qué cuidados y recomendaciones generales corresponden a un paciente simulado según
            el ritmo cardíaco que se te indique (ej. taquicardia, bradicardia, fibrilación
            auricular, fibrilación ventricular, PVC, bloqueo AV, o ritmo normal).

            REGLAS ESTRICTAS:
            1. SOLO respondes sobre el dominio de arritmias cardíacas y cuidado del paciente
               dentro de este simulador.
            2. Si la pregunta o el contexto se sale de ese dominio (entretenimiento, otras
               especialidades médicas no cardíacas, programación, temas personales, etc.),
               responde únicamente: "Solo puedo ayudarte con recomendaciones relacionadas al
               ritmo cardíaco diagnosticado en PulseLab."
            3. Nunca des un diagnóstico definitivo ni reemplaces a un médico real: aclara que
               esto es una herramienta educativa de simulación, no asesoría médica real.
            4. Responde en español, en máximo 4-5 oraciones, en tono profesional y claro.
            """;

    public AsistenteMedicoIA(
            @Value("${groq.api.key}") String apiKey,
            @Value("${groq.api.url}") String apiUrl,
            @Value("${groq.api.model}") String modelo) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.modelo = modelo;
        this.restClient = RestClient.create();
    }

    /**
     * Genera una recomendación de cuidado para el paciente, basada en su
     * ritmo cardíaco diagnosticado y sus signos vitales actuales.
     */
    public String generarRecomendacion(Paciente paciente) {
        if (apiKey == null || apiKey.isBlank()) {
            return "⚠ La integración de IA no está configurada. Define la variable de entorno GROQ_API_KEY.";
        }
        if (paciente == null || paciente.getRitmoAsignado() == null) {
            return "No hay un diagnóstico disponible para generar recomendaciones todavía.";
        }

        String contexto = construirContexto(paciente);

        try {
            Map<String, Object> body = Map.of(
                    "model", modelo,
                    "messages", List.of(
                            Map.of("role", "system", "content", SYSTEM_PROMPT),
                            Map.of("role", "user", "content", contexto)
                    ),
                    "temperature", 0.4,
                    "max_tokens", 300
            );

            Map<String, Object> response = restClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            return extraerTexto(response);

        } catch (Exception e) {
            return "⚠ No se pudo obtener la recomendación de IA en este momento (" + e.getMessage() + ").";
        }
    }

    private String construirContexto(Paciente paciente) {
        SignosVitales sv = paciente.getSignosVitales();
        StringBuilder sb = new StringBuilder();
        sb.append("Paciente diagnosticado con ritmo: ").append(paciente.getRitmoAsignado()).append(".\n");
        sb.append("Edad: ").append(paciente.getEdad()).append(" años, género: ").append(paciente.getGenero()).append(".\n");
        if (sv != null) {
            sb.append("Frecuencia cardíaca: ").append(sv.getFrecuenciaCardiaca()).append(" bpm. ");
            sb.append("Presión arterial: ").append(sv.getPresionSistolica()).append("/").append(sv.getPresionDiastolica()).append(" mmHg. ");
            sb.append("SpO2: ").append(sv.getSaturacionOxigeno()).append("%.\n");
        }
        sb.append("Genera recomendaciones generales de cuidado para este caso, dentro del dominio cardiológico únicamente.");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private String extraerTexto(Map<String, Object> response) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            return "⚠ No se pudo interpretar la respuesta de la IA.";
        }
    }
}
