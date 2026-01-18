import java.util.Scanner;
import java.time.LocalDate;

public class Fechas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int dia, mes, anio;
        String continuar;

        System.out.println("=== 📅 VERIFICADOR DE FECHAS FINAL (Con Feedback) ===");

        do {
            try {
                System.out.println("\nIntroduce los datos de la fecha:");
                System.out.print("   > Día: ");
                dia = scanner.nextInt();
                System.out.print("   > Mes: ");
                mes = scanner.nextInt();
                System.out.print("   > Año: ");
                anio = scanner.nextInt();

                // 1. Validamos con el método booleano (Requisito PDF)
                boolean esCorrecta = fechaCorrecta(dia, mes, anio);

                System.out.println("   --------------------------------");
                if (esCorrecta) {
                    System.out.println("FECHA VÁLIDA.");
                    if (añoBisiesto(anio) && mes == 2) {
                        System.out.println("      (Info: Es un año bisiesto)");
                    }
                } else {
                    // 2. Si falla, llamamos al método explicativo para saber por qué
                    System.out.println("FECHA INVÁLIDA.");
                    String motivo = obtenerMotivoError(dia, mes, anio);
                    System.out.println("      📝 Motivo: " + motivo);
                }
                System.out.println("   --------------------------------");

            } catch (Exception e) {
                System.out.println("Error: Debes introducir números enteros.");
                scanner.nextLine();
            }

            System.out.print("¿Probar otra? (s/n): ");
            continuar = scanner.next();

        } while (continuar.equalsIgnoreCase("s"));

        System.out.println("Fin del programa.");
        scanner.close();
    }

    /**
     * MÉTODO OBLIGATORIO DEL PDF (No tocar firma)
     * Retorna true/false sin explicaciones.
     */
    public static boolean fechaCorrecta(int dia, int mes, int año) {
        int anioActual = LocalDate.now().getYear();

        // Comprobación rápida (reutilizamos la lógica del mensaje para no repetir código)
        // Si el mensaje de error está vacío (""), es que es correcta.
        return obtenerMotivoError(dia, mes, año).equals("");
    }

    /**
     * NUEVO MÉTODO DE USABILIDAD
     * Analiza la fecha y devuelve un String con el error específico.
     * Retorna cadena vacía "" si no hay errores.
     */
    public static String obtenerMotivoError(int dia, int mes, int año) {
        int anioActual = LocalDate.now().getYear();

        // 1. Validar Año
        if (año < 1900) return "El año no puede ser anterior a 1900.";
        if (año > anioActual) return "El año no puede ser futuro (Máximo: " + anioActual + ").";

        // 2. Validar Mes
        if (mes < 1 || mes > 12) return "El mes debe estar entre 1 y 12.";

        // 3. Validar Día (General)
        if (dia < 1) return "El día no puede ser negativo o cero.";
        if (dia > 31) return "Ningún mes tiene más de 31 días.";

        // 4. Validar días específicos de cada mes
        if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) {
            return "El mes " + mes + " solo tiene 30 días.";
        }

        // 5. Validar Febrero (El más complejo)
        if (mes == 2) {
            boolean esBisiesto = añoBisiesto(año);
            if (esBisiesto && dia > 29) return "Febrero bisiesto solo llega hasta el día 29.";
            if (!esBisiesto && dia > 28) return "Este año NO es bisiesto, febrero solo llega al 28.";
        }

        return ""; // Si llega aquí, no hay errores
    }

    public static boolean añoBisiesto(int año) {
        return (año % 4 == 0 && año % 100 != 0) || (año % 400 == 0);
    }
}