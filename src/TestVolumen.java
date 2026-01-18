public class TestVolumen {

    /**
     * PRUEBA DE VOLUMEN Y ESTRÉS AUTOMATIZADA
     * Objetivo: Simular una carga masiva de verificaciones para medir rendimiento.
     */
    public static void main(String[] args) {
        System.out.println("--- INICIO PRUEBA DE VOLUMEN Y ESTRÉS ---");

        long tiempoInicio = System.currentTimeMillis();
        int totalPruebas = 100_000_000; // cien millónes de verificaciones
        int erroresDetectados = 0; // "Errores" aquí serían excepciones no controladas

        System.out.println("Cargando " + totalPruebas + " fechas aleatorias...");

        for (int i = 0; i < totalPruebas; i++) {
            // Generamos datos aleatorios (incluyendo inválidos para estresar la lógica)
            int dia = (int) (Math.random() * 35) + 1; // Días del 1 al 35
            int mes = (int) (Math.random() * 15) + 1; // Meses del 1 al 15
            int anio = (int) (Math.random() * 3000);  // Años del 0 al 3000

            try {
                // Llamamos a la clase estática Fechas
                Fechas.fechaCorrecta(dia, mes, anio);
            } catch (Exception e) {
                erroresDetectados++;
                System.out.println("Fallo crítico en iteración " + i);
            }
        }

        long tiempoFin = System.currentTimeMillis();
        long tiempoTotal = tiempoFin - tiempoInicio;
        long duracion = (tiempoFin - tiempoInicio)/1000; // Duración en segundos

        System.out.println("--- RESULTADOS ---");
        System.out.println("Pruebas ejecutadas: " + totalPruebas);
        System.out.println("Excepciones críticas: " + erroresDetectados);
        System.out.println("Tiempo total: " + tiempoTotal + " ms");
        //Si tarda mucho tiempo es mejor sacar la duracion en segundos
        System.out.println("Tiempo total: " + duracion + "segundos");
        System.out.println("Velocidad: " + (totalPruebas / (duracion > 0 ? duracion : 1)) + " verificaciones/ms");

        if (duracion < 1000) {
            System.out.println("RENDIMIENTO: EXCELENTE (Menos de 1 segundo)");
        } else {
            System.out.println("RENDIMIENTO: ACEPTABLE (Podría optimizarse)");
        }
    }
}