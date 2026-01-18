import java.time.LocalDate;
import java.util.Scanner;

public class Fechas {

    /**
     * PROGRAMA PRINCIPAL (Interfaz de Usuario)
     * Actúa como driver para las pruebas de integración manuales.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int dia, mes, anio;
        String continuar;

        System.out.println("=== 📅 VERIFICADOR DE FECHAS PRO 2.0 ===");
        System.out.println("Sistema listo para pruebas de integración y usuario.");

        do {
            try {
                // Entradas (Input)
                System.out.println("\nIntroduce los datos de la fecha a comprobar:");
                System.out.print("   > Día: ");
                dia = scanner.nextInt();
                System.out.print("   > Mes: ");
                mes = scanner.nextInt();
                System.out.print("   > Año: ");
                anio = scanner.nextInt();

                // Integración: El main llama a los métodos de lógica.
                // Si la comunicación falla aquí, la prueba de integración falla.
                boolean esCorrecta = fechaCorrecta(dia, mes, anio);

                // Extra: Llamamos a añoBisiesto solo para informar al usuario (valor añadido)
                boolean esBisiesto = añoBisiesto(anio);

                // 3. Salidas (Output)
                System.out.println("   --------------------------------");
                if (esCorrecta) {
                    System.out.println(" RESULTADO: La fecha es VÁLIDA.");
                    if (esBisiesto && mes == 2) {
                        System.out.println("      (Nota: Año bisiesto detectado correctamente)");
                    }
                } else {
                    System.out.println(" RESULTADO: La fecha es INVÁLIDA.");
                }
                System.out.println("   --------------------------------");

            } catch (Exception e) {
                System.out.println(" Error: Por favor, introduce solo números enteros.");
                scanner.nextLine(); // Limpiar el buffer del scanner para evitar bucles infinitos
            }

            System.out.print("¿Probar otra fecha? (s/n): ");
            continuar = scanner.next();

        } while (continuar.equalsIgnoreCase("s"));

        System.out.println("Cerrando aplicación...");
        scanner.close();
    }

    /**
     * LÓGICA DE NEGOCIO: Validador de fechas.
     * Corregido tras las pruebas unitarias y de regresión.
     */
    /**
     * Validador de fechas con SEGURIDAD AÑADIDA (Límites de rango).
     */
    public static boolean fechaCorrecta(int dia, int mes, int año) {
        // 1. Obtener el año actual del sistema
        int anioActual = LocalDate.now().getYear();

        // 2. NUEVA VALIDACIÓN DE SEGURIDAD (Rango 1900 - Actualidad)
        // Si el año es menor a 1900 O mayor al actual, es incorrecto.
        if (año < 1900 || año > anioActual) {
            return false;
        }

        // 3. Validaciones de meses y días (Lógica estándar)
        if (mes < 1 || mes > 12 || dia < 1 || dia > 31) {
            return false;
        }

        // Meses de 30 días
        if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) {
            return false;
        }

        // Febrero
        if (mes == 2) {
            boolean bisiesto = añoBisiesto(año);
            if (bisiesto && dia > 29) return false;
            if (!bisiesto && dia > 28) return false;
        }

        return true;
    }
    /**
     * LÓGICA DE NEGOCIO: Calculadora de años bisiestos.
     * Implementa la regla gregoriana completa.
     */
    public static boolean añoBisiesto(int año) {
        // Un año es bisiesto si es divisible por 4,
        // EXCEPTO si es divisible por 100 (fin de siglo),
        // A MENOS QUE sea divisible por 400.
        return (año % 4 == 0 && año % 100 != 0) || (año % 400 == 0);
    }
}