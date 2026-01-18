package org.test;

import org.principal.Fechas;

public class TestAutomator {

    /**
     * MOTOR DE PRUEBAS AUTOMATIZADAS (Simulando JUnit)
     * Ejecuta los casos de prueba de regresión definidos en la estrategia.
     */
    public static void main(String[] args) {
        System.out.println("INICIANDO BATERÍA DE PRUEBAS AUTOMATIZADAS");

        int pruebasPasadas = 0;
        int pruebasTotales = 0;

        // --- CASOS DE PRUEBA DE REGRESIÓN (Definidos en README) ---

        // Caso 1: Bisiesto Estándar
        pruebasTotales++;
        if (ejecutarTest("REG-01", 29, 2, 2024, true)) pruebasPasadas++;

        // Caso 2: No Bisiesto (Falso Positivo común)
        pruebasTotales++;
        if (ejecutarTest("REG-02", 29, 2, 2023, false)) pruebasPasadas++;

        // Caso 3: Fecha Inexistente (30 de Febrero)
        pruebasTotales++;
        if (ejecutarTest("REG-03", 30, 2, 2023, false)) pruebasPasadas++;

        // Caso 4: Mes de 30 días (31 de Abril)
        pruebasTotales++;
        if (ejecutarTest("REG-04", 31, 4, 2023, false)) pruebasPasadas++;

        // Caso 5: Excepción Secular (1900 no es bisiesto)
        pruebasTotales++;
        if (ejecutarTest("REG-05", 29, 2, 1900, false)) pruebasPasadas++;

        // Caso 6: Excepción de Excepción (2000 sí es bisiesto)
        pruebasTotales++;
        if (ejecutarTest("REG-06", 29, 2, 2000, true)) pruebasPasadas++;

        // Caso 7: Seguridad - Año muy antiguo
        pruebasTotales++;
        if (ejecutarTest("SEG-01", 1, 1, 1800, false)) pruebasPasadas++;

        // --- RESULTADOS FINALES ---
        System.out.println("\n------------------------------------------------");
        System.out.println("RESUMEN DE EJECUCIÓN:");
        System.out.println("   > Pruebas Totales: " + pruebasTotales);
        System.out.println("   > Pruebas Pasadas: " + pruebasPasadas);
        System.out.println("   > Pruebas Fallidas: " + (pruebasTotales - pruebasPasadas));

        if (pruebasPasadas == pruebasTotales) {
            System.out.println("RESULTADO: EL SISTEMA ES ESTABLE.");
        } else {
            System.out.println("RESULTADO: SE HAN DETECTADO REGRESIONES.");
        }
    }

    /**
     * Método auxiliar para ejecutar una prueba individual y comparar el resultado.
     * @param id Identificador de la prueba
     * @param d Día
     * @param m Mes
     * @param a Año
     * @param esperado true si la fecha DEBERÍA ser válida, false si debería fallar
     */
    public static boolean ejecutarTest(String id, int d, int m, int a, boolean esperado) {
        boolean resultadoReal = Fechas.fechaCorrecta(d, m, a);
        boolean pasaTest = (resultadoReal == esperado);

        System.out.print(String.format("Test %-6s [%02d/%02d/%d] -> Esperado: %-5s | Actual: %-5s ",
                id, d, m, a, esperado, resultadoReal));

        if (pasaTest) {
            System.out.println("OK");
            return true;
        } else {
            System.out.println("FALLO");
            return false;
        }
    }
}