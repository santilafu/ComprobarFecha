package org.principal;

import java.util.Scanner;
import java.time.LocalDate;

public class Fechas {

    // CONSTANTES DE SEGURIDAD
    private static final int MAX_INTENTOS_FALLIDOS = 3;
    private static final int MAX_LONGITUD_INPUT = 6; // Nadie necesita más de 6 caracteres para un dato

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int dia = 0, mes = 0, anio = 0;
        String entradaTemp;
        String continuar = "s";
        int contadorErrores = 0; // Contador de seguridad

        System.out.println("=== 🛡️ VERIFICADOR DE FECHAS (- Release Candidate) ===");
        System.out.println("   > Build: Stable | Environment: Production Ready");
        System.out.println("Nota: Por seguridad, el sistema se bloqueará tras " + MAX_INTENTOS_FALLIDOS + " errores consecutivos.");

        do {
            try {
                // SEGURIDAD: Protección contra Fuerza Bruta
                if (contadorErrores >= MAX_INTENTOS_FALLIDOS) {
                    System.out.println("\n⛔ ALERTA DE SEGURIDAD: Demasiados intentos fallidos.");
                    System.out.println("   El sistema se ha bloqueado para evitar automatización.");
                    break; // Salimos del bucle y cerramos el programa
                }

                System.out.println("\nIntroduce los datos de la fecha (Intento " + (contadorErrores + 1) + "):");

                // VALIDACIÓN DEL DÍA
                System.out.print("   > Día (1 a 31): ");
                entradaTemp = leerEntradaSegura(scanner);
                if (entradaTemp == null || !entradaTemp.matches("^\\d{1,2}$")) {
                    System.out.println("Error: Formato inválido o longitud excesiva.");
                    contadorErrores++;
                    continue;
                }
                dia = Integer.parseInt(entradaTemp);

                // VALIDACIÓN DEL MES
                System.out.print("   > Mes (1 a 12): ");
                entradaTemp = leerEntradaSegura(scanner);
                if (entradaTemp == null || !entradaTemp.matches("^\\d{1,2}$")) {
                    System.out.println("Error: Formato inválido.");
                    contadorErrores++;
                    continue;
                }
                mes = Integer.parseInt(entradaTemp);

                //VALIDACIÓN DEL AÑO
                System.out.print("   > Año de 1900 a actual: ");
                entradaTemp = leerEntradaSegura(scanner);
                if (entradaTemp == null || !entradaTemp.matches("^\\d{4}$")) {
                    System.out.println("Error: El año debe tener 4 dígitos.");
                    contadorErrores++;
                    continue;
                }
                anio = Integer.parseInt(entradaTemp);

                // VALIDACIÓN LÓGICA
                boolean esCorrecta = fechaCorrecta(dia, mes, anio);

                System.out.println("   --------------------------------");
                if (esCorrecta) {
                    System.out.println("FECHA VÁLIDA.");
                    if (añoBisiesto(anio) && mes == 2) System.out.println("      (Info: Año bisiesto)");
                    contadorErrores = 0; // RESETEAMOS el contador si acierta (Comportamiento usuario real)
                } else {
                    System.out.println("LÓGICA INVÁLIDA.");
                    System.out.println(" 📝 Motivo: " + obtenerMotivoError(dia, mes, anio));
                    // No aumentamos contadorErrores aquí porque la entrada fue "segura", solo lógica incorrecta.
                    // (Opcional: puedes aumentarlo si quieres ser muy estricto).
                }
                System.out.println("   --------------------------------");

            } catch (Exception e) {
                System.out.println("Excepción no controlada.");
                scanner.nextLine();
                contadorErrores++;
                continuar = "s";
            }

            if (contadorErrores < MAX_INTENTOS_FALLIDOS) {
                System.out.print("¿Probar otra? (s/n): ");
                continuar = leerEntradaSegura(scanner);
                if (continuar == null) continuar = "n"; // Salir si hay error raro
            }

        } while (continuar.equalsIgnoreCase("s") && contadorErrores < MAX_INTENTOS_FALLIDOS);

        System.out.println("Fin de sesión segura.");
        scanner.close();
    }

    /**
     * MÉTODO DE SEGURIDAD AUXILIAR
     * Lee la entrada y verifica que no supere la longitud máxima permitida
     * para evitar desbordamientos de memoria o logs masivos.
     */
    private static String leerEntradaSegura(Scanner sc) {
        String input = sc.next();
        if (input.length() > MAX_LONGITUD_INPUT) {
            // Rechazo silencioso o explícito
            return null; // Retornamos null para indicar violación de seguridad
        }
        return input;
    }

    //MÉTODOS DE LÓGICA
    public static boolean fechaCorrecta(int dia, int mes, int año) {
        return obtenerMotivoError(dia, mes, año).equals("");
    }

    public static String obtenerMotivoError(int dia, int mes, int año) {
        int anioActual = LocalDate.now().getYear();
        if (año < 1900) return "El año no puede ser anterior a 1900.";
        if (año > anioActual) return "El año no puede ser futuro.";
        if (mes < 1 || mes > 12) return "El mes debe estar entre 1 y 12.";
        if (dia < 1) return "El día no puede ser negativo o cero.";
        if (dia > 31) return "Ningún mes tiene más de 31 días.";
        if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) return "El mes " + mes + " solo tiene 30 días.";
        if (mes == 2) {
            boolean esBisiesto = añoBisiesto(año);
            if (esBisiesto && dia > 29) return "Febrero bisiesto solo llega hasta el día 29.";
            if (!esBisiesto && dia > 28) return "Este año NO es bisiesto, febrero solo llega al 28.";
        }
        return "";
    }

    public static boolean añoBisiesto(int año) {
        return (año % 4 == 0 && año % 100 != 0) || (año % 400 == 0);
    }
}