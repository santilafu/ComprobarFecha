package org.test;

import org.principal.Fechas;

public class TestRecursos {

    /**
     * PRUEBA DE USO DE RECURSOS (CPU y RAM)
     * Ejecuta una carga masiva para monitorizar el comportamiento del Hardware.
     * Referencia: Unidad 8 - Apartado "Pruebas de uso de recursos".
     */
    public static void main(String[] args) {
        System.out.println("=== INICIANDO AUDITORÍA DE RECURSOS DE SISTEMA ===");
        System.out.println("   > Objetivo: Medir consumo de RAM y Tiempo de CPU bajo estrés.");

        //PREPARACIÓN DEL ENTORNO
        // Obtenemos la instancia del entorno de ejecución de Java
        Runtime runtime = Runtime.getRuntime();

        // Ejecutamos el Garbage Collector explícitamente para limpiar basura previa
        // y tener una medición "limpia" de la memoria inicial.
        runtime.gc();

        // Medición Inicial
        long memoriaTotalInicial = runtime.totalMemory();
        long memoriaLibreInicial = runtime.freeMemory();
        long memoriaUsadaInicio = memoriaTotalInicial - memoriaLibreInicial;
        long tiempoInicio = System.currentTimeMillis();

        // EJECUCIÓN DE CARGA (Simulación de estrés)
        // Probamos con 2 millones de ciclos para forzar al procesador
        int totalIteraciones = 2_000_000;
        System.out.println("   > Ejecutando " + totalIteraciones + " verificaciones...");

        for (int i = 0; i < totalIteraciones; i++) {
            // Generamos datos aleatorios volátiles (no se guardan en variables para no llenar la RAM artificialmente)
            int d = (int)(Math.random() * 30) + 1;
            int m = (int)(Math.random() * 12) + 1;
            int a = (int)(Math.random() * 2050) + 1900;

            // Llamada al sistema bajo prueba
            Fechas.fechaCorrecta(d, m, a);
        }

        // MEDICIÓN FINAL
        long tiempoFin = System.currentTimeMillis();
        long memoriaTotalFinal = runtime.totalMemory();
        long memoriaLibreFinal = runtime.freeMemory();
        long memoriaUsadaFin = memoriaTotalFinal - memoriaLibreFinal;

        // CÁLCULOS Y REPORTE
        long duracion = tiempoFin - tiempoInicio;
        long deltaMemoriaBytes = memoriaUsadaFin - memoriaUsadaInicio;

        // Convertimos a Megabytes (MB) para que sea legible por humanos
        double memoriaUsadaMB = deltaMemoriaBytes / (1024.0 * 1024.0);

        System.out.println("\n=== 📊 INFORME DE RENDIMIENTO ===");

        // Reporte de CPU (Tiempo)
        System.out.println("1. 🕒 TIEMPO DE PROCESAMIENTO (CPU):");
        System.out.println("   - Duración Total: " + duracion + " ms");
        System.out.println("   - Rendimiento: " + (totalIteraciones / (duracion > 0 ? duracion : 1)) + " ops/ms");

        if (duracion < 1000) System.out.println("   ✅ ESTADO: ÓPTIMO (Bajo latencia)");
        else System.out.println("   ⚠️ ESTADO: MEJORABLE");

        // Reporte de RAM (Memoria)
        System.out.println("\n2. 💾 CONSUMO DE MEMORIA (RAM):");
        System.out.println("   - Memoria Inicio: " + (memoriaUsadaInicio / 1024) + " KB");
        System.out.println("   - Memoria Final:  " + (memoriaUsadaFin / 1024) + " KB");
        System.out.println("   - Diferencia Real: " + String.format("%.4f", memoriaUsadaMB) + " MB");

        // Criterio de aceptación: Que el consumo extra sea despreciable (< 2MB para 2M de ops)
        // Nota: Java gestiona la memoria automáticamente, por lo que a veces puede dar negativo si el GC salta en medio.
        // Eso también es un resultado válido (significa que limpia bien).
        if (memoriaUsadaMB < 5.0) {
            System.out.println(" ✅ ESTADO: EFICIENTE (Bajo consumo de recursos)");
        } else {
            System.out.println(" ⚠️ ESTADO: ALTO CONSUMO (Posible fuga de memoria)");
        }
    }
}