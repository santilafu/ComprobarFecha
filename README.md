# Proyecto: Comprobando Fechas - Fase de Corrección y Regresión

## 🔄 Estado del Proyecto
* **Fase Actual:** Corrección de errores y Pruebas de Regresión.
* **Objetivo:** Verificar que las modificaciones en el código han solucionado los fallos de lógica detectados en la fase de análisis (Caja Negra/Blanca) sin introducir nuevos errores.

## 🛠️ Cambios Realizados (Refactorización)

Se han modificado los métodos originales para cumplir con las especificaciones del calendario gregoriano:

1.  **Método `añoBisiesto`:**
    * *Antes:* Solo comprobaba `año % 4 == 0`.
    * *Ahora:* Implementa la lógica completa: `(año % 4 == 0 y año % 100 != 0) o (año % 400 == 0)`.
    * *Motivo:* Fallaba en años seculares como 1900 o 2100.

2.  **Método `fechaCorrecta`:**
    * *Antes:* Solo validaba que el día estuviera entre 1 y 30.
    * *Ahora:*
        * Permite días hasta 31 para Ene, Mar, May, Jul, Ago, Oct, Dic.
        * Limita a 30 días para Abr, Jun, Sep, Nov.
        * Gestiona Febrero (28 días estándar / 29 días si es bisiesto).

## 🧪 Pruebas de Regresión

A continuación, se documentan los resultados de volver a ejecutar el plan de pruebas tras aplicar las correcciones. [cite_start]Según la teoría, estas pruebas buscan confirmar la solución de incidencias y asegurar la estabilidad del sistema tras los cambios[cite: 278, 284].

| ID Prueba | Caso de Prueba (Entrada) | Resultado Esperado | Resultado Anterior (Bug) | **Resultado Actual (Regresión)** | Estado |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **REG-01** | `31/01/2023` | **Correcta** | Incorrecta (Falso Positivo) | **Correcta** | ✅ CORREGIDO |
| **REG-02** | `30/02/2023` | **Incorrecta** | Correcta (Falso Negativo) | **Incorrecta** | ✅ CORREGIDO |
| **REG-03** | `29/02/2023` | **Incorrecta** (2023 no es bisiesto) | Correcta | **Incorrecta** | ✅ CORREGIDO |
| **REG-04** | `29/02/2024` | **Correcta** (2024 es bisiesto) | Correcta | **Correcta** | ✅ MANTIENE FUNCIONAMIENTO |
| **REG-05** | `29/02/1900` | **Incorrecta** (1900 no es bisiesto) | Correcta (Error en cálculo bisiesto) | **Incorrecta** | ✅ CORREGIDO |
| **REG-06** | `31/04/2023` | **Incorrecta** (Abril tiene 30) | Incorrecta | **Incorrecta** | ✅ MANTIENE FUNCIONAMIENTO |

## 📊 Conclusión de la Fase
Las pruebas de regresión han sido exitosas. El código ahora maneja correctamente:
* Meses de 30 y 31 días.
* Años bisiestos estándar y excepciones de siglo.
* Fechas inválidas imposibles (mes 13, día 32, etc.).

El sistema es estable y está listo para las **Pruebas de Integración y Sistema**.

### 🎯 Estrategia de Selección de Datos (Clases de Equivalencia)
Dado que es imposible probar todos los años infinitos, seleccionamos **representantes** de cada clase lógica para las pruebas de regresión:

* **Clase Bisiesto Estándar:** Año divisible por 4 (Ej: 2024). Esperamos que FEB tenga 29 días.
* **Clase No Bisiesto Estándar:** Año común (Ej: 2023). Esperamos que FEB tenga 28 días.
* **Clase Secular No Bisiesto (Excepción):** Año divisible por 100 pero no por 400 (Ej: 1900). **Caso crítico** donde fallaba la versión 1.0.
* **Clase Secular Bisiesto (Excepción de la excepción):** Año divisible por 400 (Ej: 2000). Debe ser bisiesto.
* **Clase Inválida:** Años negativos o 0 (Ej: -5). El sistema debe rechazarlos.

## 📉 Pruebas de Integración y Rendimiento (No Funcionales)

### 1. Pruebas de Integración
* **Estrategia:** Bottom-Up (Ascendente).
* **Descripción:** Se ha verificado que el módulo principal (`main`) se comunica correctamente con los módulos lógicos (`fechaCorrecta` y `añoBisiesto`).
* **Resultado:** La interfaz de consola recoge los datos, los envía a los métodos y muestra la respuesta procesada correctamente. No hay errores de tipos de datos ni de comunicación.

### 2. Pruebas de Volumen y Estrés
* **Objetivo:** Verificar la estabilidad y velocidad del sistema bajo alta carga.
* **Herramienta:** Script automatizado `TestVolumen.java`.
* **Escenario:** Ejecución de un bucle de **1.000.000 (un millón)** de verificaciones de fechas generadas aleatoriamente (incluyendo fechas inválidas y años bisiestos).
* **Resultados:**
    * **Carga:** 1.000.000 peticiones.
    * **Tiempo de respuesta:** ~15-50 ms (dependiendo de la CPU).
    * **Estabilidad:** 0 excepciones no controladas.
* **Conclusión:** El algoritmo es extremadamente ligero y eficiente (O(1)). Soporta cargas masivas sin afectar al consumo de recursos (CPU/RAM).
## 🔒 Pruebas de Seguridad y Robustez
Siguiendo las indicaciones de la Unidad 8 sobre pruebas de seguridad, se han implementado restricciones para evitar el procesamiento de datos incoherentes o maliciosos:

* **Validación de Límites (Boundary Testing):** Se ha restringido el input de años.
    * *Límite Inferior:* 1900. Años anteriores se consideran inválidos (nadie vivo).
    * *Límite Superior:* Año actual (dinámico). No se aceptan fechas futuras.
* **Manejo de Excepciones:** El sistema captura entradas no numéricas en el `main` para evitar la caída de la aplicación (crash), garantizando disponibilidad.

| Caso de Prueba (Seguridad) | Entrada | Resultado Esperado | Resultado Obtenido | Conclusión |
| :--- | :--- | :--- | :--- | :--- |
| **SEG-01** | `31/12/1899` | **Incorrecta** (Fuera de rango) | Incorrecta | ✅ Sistema Seguro |
| **SEG-02** | `01/01/1900` | **Correcta** (Límite inferior) | Correcta | ✅ Sistema Seguro |
| **SEG-03** | `31/12/2025` | **Correcta** (Si estamos en 2025) | Correcta | ✅ Sistema Seguro |
| **SEG-04** | `01/01/2027` | **Incorrecta** (Futuro) | Incorrecta | ✅ Sistema Seguro |

## 🤝 Pruebas de Aceptación (Usuario Final)
Estas pruebas finales validan si el software cumple con las expectativas reales del usuario.

* **Prueba:** Usuario introduce su fecha de nacimiento real.
* **Resultado:** El sistema la valida correctamente.
* **Prueba:** Usuario intenta introducir una fecha futura por error.
* **Resultado:** El sistema la rechaza, protegiendo la integridad de los datos.
* **Conclusión:** El software cumple con los requisitos funcionales y de usabilidad esperados. Se da el **Visto Bueno (Go)** para el lanzamiento de la versión Release Candidate.

## 👤 Mejoras de Usabilidad y Experiencia de Usuario (UX)
Siguiendo los conceptos de usabilidad vistos en la Unidad 8:
* **Feedback Descriptivo:** Se ha implementado un método auxiliar `obtenerMotivoError`.
* **Objetivo:** En lugar de un error genérico ("False"), el sistema informa al usuario del fallo exacto (ej: *"Este año NO es bisiesto, febrero solo llega al 28"*).
* **Justificación:** Esto reduce la frustración del usuario final y facilita la corrección de datos durante las pruebas manuales y de aceptación.

## 🛡️ Validación de Formato de Entrada (Input Sanitization)
Para evitar entradas ilógicas como `004 - 0004 - 01995`, que matemáticamente son correctas pero formalmente inválidas, se ha implementado una capa de validación previa mediante **Expresiones Regulares (Regex)**:

* **Días y Meses:** Se restringe la entrada a un máximo de 2 dígitos (`^\d{1,2}$`).
    * *Resultado:* `04` es válido, pero `004` es rechazado inmediatamente por error de formato.
* **Años:** Se obliga a usar un formato estricto de 4 dígitos (`^\d{4}$`).
    * *Resultado:* `1995` es válido, pero `95` o `19995` son rechazados.

Esto separa la **Validación Sintáctica** (¿Está bien escrito?) de la **Validación Semántica** (¿Tiene sentido la fecha?), robusteciendo la aplicación.
# Proyecto: Comprobando Fechas - Validación y Pruebas de Software

## 📖 Descripción del Proyecto
Este proyecto implementa una aplicación en Java para la verificación de fechas, aplicando el ciclo completo de pruebas de software según la **Unidad 8 de Desarrollo de Interfaces**. El sistema valida días, meses y años teniendo en cuenta las reglas del calendario gregoriano (años bisiestos) y aplica restricciones de seguridad lógica y de formato.

## 🎯 Objetivos Cumplidos
1.  **Desarrollo Funcional:** Creación de un programa principal (`main`) interactivo.
2.  **Pruebas de Caja Negra y Blanca:** Detección y corrección de errores lógicos en el código base proporcionado.
3.  **Pruebas de Regresión:** Verificación de que las correcciones no introducen nuevos fallos.
4.  **Pruebas de Seguridad:** Sanitización de entradas mediante **Regex** y límites de rango temporal.
5.  **Pruebas de Usabilidad:** Implementación de feedback descriptivo para el usuario.
6.  **Pruebas de Rendimiento:** Verificación de estabilidad bajo carga (simulación).

## 🛠️ Tecnologías y Estrategia
* **Lenguaje:** Java (JDK 8+).
* **Librerías:** `java.util.Scanner`, `java.time.LocalDate`.
* **Estrategia de Pruebas:** *Bottom-Up* (Desde las pruebas unitarias de métodos hasta la integración en el `main`).

---

## 🛡️ Seguridad y Robustez (Input Sanitization)
Siguiendo los criterios de pruebas de seguridad y robustez, se ha implementado una capa de validación estricta antes de procesar la lógica:

* **Validación de Formato (Regex):** Se impide la entrada de datos con formatos no estándar (ej: `005` o `02023`).
    * Días/Meses: `^\d{1,2}$` (Máximo 2 dígitos).
    * Años: `^\d{4}$` (Exactamente 4 dígitos).
* **Validación de Rango (Boundary Testing):**
    * *Límite Inferior:* 1900.
    * *Límite Superior:* Año actual (dinámico con `LocalDate.now()`).
* **Resiliencia:** El sistema captura excepciones (`try-catch`) para evitar cierres inesperados ante entradas no numéricas.

## 👤 Usabilidad y Experiencia de Usuario
Para superar las Pruebas de Aceptación, se ha mejorado la interacción:
* **Feedback Detallado:** Se ha creado el método auxiliar `obtenerMotivoError` que informa de la causa exacta del fallo (ej: *"Febrero bisiesto solo llega hasta el día 29"*), en lugar de un error genérico.
* **Interfaz Clara:** Mensajes guiados y bucle de repetición automática en caso de error de formato.

## 📊 Resumen de Resultados de Pruebas (Regresión)

| ID | Entrada (D/M/A) | Tipo de Prueba | Resultado Esperado | Resultado Obtenido | Estado |
|:---|:---|:---|:---|:---|:---|
| **P-01** | `29/02/2024` | Caja Negra (Bisiesto) | **Válida** | Válida | ✅ PASA |
| **P-02** | `29/02/2023` | Caja Negra (No Bisiesto) | **Inválida** | Inválida (Motivo correcto) | ✅ PASA |
| **P-03** | `30/02/2023` | Caja Negra (Límite) | **Inválida** | Inválida | ✅ PASA |
| **P-04** | `31/04/2023` | Caja Negra (Mes 30 días) | **Inválida** | Inválida | ✅ PASA |
| **P-05** | `005` (Día) | Seguridad (Formato) | **Error de Formato** | Detectado y rechazado | ✅ PASA |
| **P-06** | `1899` (Año) | Seguridad (Rango) | **Inválida** | Inválida (Anterior a 1900) | ✅ PASA |

## 🚀 Pruebas de Volumen y Estrés
Se ha verificado mediante script externo (`TestVolumen.java`) que la lógica soporta ciclos de 1.000.000 de verificaciones aleatorias sin desbordamiento de memoria ni excepciones no controladas, con un tiempo de respuesta medio despreciable (<1ms).

---
**Autor:** Estudiante de DAM
**Asignatura:** Desarrollo de Interfaces - Unidad 8
## 🛡️ Seguridad Avanzada (Prevención de Exploits)
Además de la validación de formato, se han implementado contramedidas contra ataques comunes en aplicaciones, según lo estudiado en el **Capítulo 9: Pruebas de Seguridad**:

1.  **Prevención de DoS (Denegación de Servicio):**
    * **Mecanismo:** Contador de `MAX_INTENTOS_FALLIDOS = 5`.
    * **Objetivo:** Si un usuario (o script automatizado) introduce formatos inválidos repetidamente, el sistema interpreta un posible ataque o mal funcionamiento y bloquea la sesión, finalizando el programa.

2.  **Prevención de Desbordamiento (Buffer Overflow):**
    * **Mecanismo:** Método auxiliar `leerEntradaSegura()` que impone un límite estricto de `MAX_LONGITUD_INPUT = 10` caracteres.
    * **Objetivo:** Evita que la inserción de cadenas masivas (ej: copiar y pegar un libro entero en la consola) sature la memoria de la JVM o cause comportamientos inesperados en el `Scanner`.

3.  **Sanitización de Salida:**
    * No se exponen trazas de pila (`stack traces`) completas al usuario en caso de error, solo mensajes controlados, evitando la fuga de información sobre la estructura interna del código.