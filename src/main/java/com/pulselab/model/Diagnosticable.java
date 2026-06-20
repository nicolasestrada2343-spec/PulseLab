package com.pulselab.model;

/**
 * Contrato para cualquier entidad del sistema que pueda ser sometida
 * a un diagnóstico clínico por parte de un estudiante (Modo Examen).
 *
 * Permite tratar de forma polimórfica distintos tipos de "casos clínicos"
 * (por ahora solo Paciente, pero el sistema podría extenderse a otros
 * objetos diagnosticables como un ECG aislado o un caso de urgencias).
 */
public interface Diagnosticable {

    /**
     * Evalúa la respuesta del estudiante contra el diagnóstico real
     * y actualiza el estado interno del objeto (diagnosticado, correcto, etc.).
     *
     * @param respuestaEstudiante texto ingresado por el estudiante
     * @return true si el diagnóstico es correcto
     */
    boolean evaluarDiagnostico(String respuestaEstudiante);

    /**
     * @return un resumen legible del resultado del diagnóstico
     */
    String getResultadoDiagnostico();
}
