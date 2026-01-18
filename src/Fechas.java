import java.util.Scanner;
import java.time.LocalDate;

public class Fechas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int dia = 0, mes = 0, anio = 0;
        String entradaTemp; // Variable para analizar el texto antes de convertirlo
        String continuar = "s";

        System.out.println("=== 📅 VERIFICADOR DE FECHAS FINAL (Formato Estricto) ===");

        do {
            try {
                System.out.println("\nIntroduce los datos de la fecha:");

                // VALIDACIÓN DEL DÍA (Formato)
                System.out.print("   > Día (del 1 al 31): ");
                entradaTemp = scanner.next();
                // Regex: ^\d{1,2}$ significa "exactamente 1 o 2 dígitos numéricos"
                if (!entradaTemp.matches("^\\d{1,2}$")) {
                    System.out.println(" Error de Formato: El día no puede tener más de 2 dígitos (ej: '004' es inválido).");
                    continue; // Vuelve a empezar el bucle
                }
                dia = Integer.parseInt(entradaTemp); // Ahora sí convertimos a entero

                //VALIDACIÓN DEL MES (Formato)
                System.out.print("   > Mes (del 1 al 12): ");
                entradaTemp = scanner.next();
                if (!entradaTemp.matches("^\\d{1,2}$")) {
                    System.out.println("Error de Formato: El mes no puede tener más de 2 dígitos.");
                    continue;
                }
                mes = Integer.parseInt(entradaTemp);

                // VALIDACIÓN DEL AÑO (Formato)
                System.out.print("   > Año (de 1900 hasta la fecha actual): ");
                entradaTemp = scanner.next();
                // Regex: ^\d{4}$ significa "exactamente 4 dígitos"
                if (!entradaTemp.matches("^\\d{4}$")) {
                    System.out.println("Error de Formato: El año debe tener exactamente 4 dígitos (ej: 1995).");
                    continue;
                }
                anio = Integer.parseInt(entradaTemp);

                // VALIDACIÓN LÓGICA (Tu método existente)
                boolean esCorrecta = fechaCorrecta(dia, mes, anio);

                System.out.println("   --------------------------------");
                if (esCorrecta) {
                    System.out.println("FECHA VÁLIDA y FORMATO CORRECTO.");
                    if (añoBisiesto(anio) && mes == 2) {
                        System.out.println("      (Info: Es un año bisiesto)");
                    }
                } else {
                    System.out.println("LÓGICA INVÁLIDA.");
                    String motivo = obtenerMotivoError(dia, mes, anio);
                    System.out.println("📝 Motivo: " + motivo);
                }
                System.out.println("   --------------------------------");

            } catch (Exception e) {
                System.out.println("Error: Entrada desconocida.");
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

        //Validar Año
        if (año < 1900) return "El año no puede ser anterior a 1900.";
        if (año > anioActual) return "El año no puede ser futuro (Máximo: " + anioActual + ").";

        //Validar Mes
        if (mes < 1 || mes > 12) return "El mes debe estar entre 1 y 12.";

        // Validar Día (General)
        if (dia < 1) return "El día no puede ser negativo o cero.";
        if (dia > 31) return "Ningún mes tiene más de 31 días.";

        //Validar días específicos de cada mes
        if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) {
            return "El mes " + mes + " solo tiene 30 días.";
        }

        //Validar Febrero (El más complejo)
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