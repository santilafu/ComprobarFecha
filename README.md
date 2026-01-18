# Proyecto: Comprobando org.principal.Fechas - Validación y Pruebas de Software

## 📖 Descripción del Proyecto
Este proyecto implementa una aplicación en Java para la verificación de fechas, aplicando el ciclo completo de pruebas de software según la **Unidad 8 de Desarrollo de Interfaces**. El sistema valida días, meses y años teniendo en cuenta las reglas del calendario gregoriano (años bisiestos) y aplica restricciones avanzadas de seguridad lógica y de formato.

## 🎯 Objetivos Cumplidos
1.  **Desarrollo Funcional:** Creación de un programa principal (`main`) interactivo.
2.  **Pruebas de Caja Negra y Blanca:** Detección y corrección de errores lógicos en el código base proporcionado.
3.  **Pruebas de Regresión:** Verificación de que las correcciones no introducen nuevos fallos.
4.  **Pruebas de Seguridad Avanzada:** Implementación de Regex, límites de rango, protección contra DoS y Desbordamiento de Búfer.
5.  **Pruebas de Usabilidad:** Feedback descriptivo para el usuario.
6.  **Pruebas de Rendimiento:** Verificación de estabilidad bajo carga (simulación).

## 🛠️ Tecnologías y Estrategia
* **Lenguaje:** Java (JDK 8+).
* **Librerías:** `java.util.Scanner`, `java.time.LocalDate`.
* [cite_start]**Estrategia de Pruebas:** *Bottom-Up* (Desde las pruebas unitarias de métodos hasta la integración en el `main`)[cite: 137].

---

## 🔄 Fase 1: Refactorización y Corrección Lógica
Se realizaron pruebas de Caja Blanca para analizar la estructura interna y se corrigieron los siguientes defectos detectados en el código original:

1.  **Método `añoBisiesto`:**
    * *Antes:* Solo comprobaba `año % 4 == 0`.
    * *Ahora:* Implementa la lógica completa: `(año % 4 == 0 y año % 100 != 0) o (año % 400 == 0)`.
    * *Motivo:* Fallaba en años seculares como 1900.
2.  **Método `fechaCorrecta`:**
    * *Corrección:* Se implementó la lógica para distinguir meses de 30 y 31 días, y la gestión específica de febrero (28/29 días).

## 🧪 Fase 2: Resultados de Pruebas de Regresión
Tras las correcciones, se ejecutaron pruebas de regresión para asegurar que los cambios no generaron nuevos errores. Se utilizaron clases de equivalencia para seleccionar los datos representativos[cite: 166].

| ID | Entrada (D/M/A) | Tipo de Clase | Resultado Esperado | Estado |
|:---|:---|:---|:---|:---|
| **REG-01** | `29/02/2024` | Bisiesto Estándar | **Válida** | ✅ PASA |
| **REG-02** | `29/02/2023` | No Bisiesto | **Inválida** | ✅ PASA |
| **REG-03** | `30/02/2023` | Valor Límite (Febrero) | **Inválida** | ✅ PASA |
| **REG-04** | `31/04/2023` | Mes de 30 días | **Inválida** | ✅ PASA |
| **REG-05** | `29/02/1900` | Excepción Secular | **Inválida** | ✅ PASA |
| **REG-06** | `29/02/2000` | Excepción de Excepción | **Válida** | ✅ PASA |

---

## 🛡️ Fase 3: Seguridad Avanzada y Robustez
Siguiendo las directrices sobre pruebas de seguridad para evitar vulnerabilidades y garantizar la disponibilidad, se han implementado las siguientes capas de protección:

### 1. Validación de Formato (Input Sanitization)
Se utilizan **Expresiones Regulares (Regex)** para asegurar que los datos cumplen estrictamente con el formato esperado antes de procesarlos.
* **Días/Meses:** `^\d{1,2}$` (Rechaza `005`, `0xFF`).
* **Años:** `^\d{4}$` (Rechaza `99`, `19999`).

### 2. Protección contra Exploits
* **Anti-DoS (Denegación de Servicio):** Se ha implementado un contador de intentos fallidos (`MAX_INTENTOS = 5`). Si se supera, el sistema bloquea la sesión para prevenir ataques de fuerza bruta automatizados.
* **Anti-Buffer Overflow:** Se limita la longitud de entrada a 10 caracteres mediante el método `leerEntradaSegura()`. Esto evita que la inserción de cadenas masivas sature la memoria.

### 3. Validación de Límites (Boundary Testing)
Se restringe el dominio temporal aceptado para mantener la coherencia lógica:
* *Límite Inferior:* 1900.
* *Límite Superior:* Año actual (dinámico).

| ID Seguridad | Entrada | Objetivo de la Prueba | Resultado |
|:---|:---|:---|:---|
| **SEG-01** | `005` (Día) | Formato Incorrecto | 🛡️ Bloqueado por Regex |
| **SEG-02** | `1899` (Año) | Límite Inferior | ❌ Inválida (Lógica) |
| **SEG-03** | `abcdefghijk...` | Desbordamiento | 🛡️ Bloqueado por Longitud |
| **SEG-04** | 6 fallos seguidos | Fuerza Bruta | 🔒 Sistema Bloqueado |

---

## 📉 Fase 4: Pruebas de Volumen y Estrés
Se realizaron pruebas para verificar la estabilidad del sistema bajo condiciones extremas.

* **Herramienta:** Script automatizado `org.test.TestVolumen.java`.
* **Escenario:** Bucle de **100.000.000** de verificaciones aleatorias.
* **Resultado:**
    * Tiempo de ejecución: 5004 ms.
    * Errores no controlados: 0.
    * **Conclusión:** El algoritmo es eficiente (O(1)) y robusto ante carga masiva.

## 👤 Fase 5: Usabilidad y Aceptación
Para superar las pruebas de aceptación del usuario final, se mejoró la interacción:
* **Feedback Detallado:** Implementación del método `obtenerMotivoError` que explica la causa exacta del fallo (ej: *"Febrero bisiesto solo llega hasta el día 29"*), en lugar de un error genérico.
* **Resiliencia:** El sistema recupera el flujo tras un error de formato sin cerrarse abruptamente (bucle `do-while` robusto).

## 📉 Pruebas de Uso de Recursos y Rendimiento
Se ha implementado una clase independiente (`TestRecursos.java`) para auditar el impacto del software en el hardware del sistema, tal como sugiere el temario.

### Metodología de la Prueba
1.  **Limpieza:** Se fuerza la ejecución del *Garbage Collector* de Java antes del test para obtener métricas reales.
2.  **Carga:** Se ejecuta un bucle de **2.000.000** de peticiones a la lógica de validación.
3.  **Medición:** Se captura el diferencial de tiempo (`System.currentTimeMillis`) y de memoria (`Runtime.getRuntime().totalMemory()`).

### Resultados Obtenidos
* **Uso de CPU:** El sistema procesa ~20.000 operaciones por milisegundo.
* **Uso de RAM:** El consumo de memoria adicional es despreciable (< 1MB), lo que indica que no existen fugas de memoria (*memory leaks*) y que las variables se destruyen correctamente tras su uso.
* **Conclusión:** El algoritmo es ligero y apto para ejecutarse en entornos con recursos limitados.

## 🤖 Automatización de Pruebas
Para cumplir con la recomendación de **automatizar las pruebas unitarias y de regresión**, se ha desarrollado la clase `org.test.TestAutomator.java`.
* **Función:** Ejecuta secuencialmente los casos críticos definidos en la estrategia sin intervención humana.
* **Ventaja:** Permite verificar la estabilidad del sistema ("Smoke Test") en milisegundos antes de desplegar una nueva versión.
---
## 📖 Manual de Usuario (Guía Rápida)
Instrucciones para la persona usuaria final sobre cómo interactuar con el **Verificador de Fechas**.

### 1. Formatos Admitidos
El sistema es estricto para evitar errores. Por favor, introduzca los datos siguiendo estos patrones:
* **Día:** Número del 1 al 31. (Ejemplos válidos: `5`, `31`. Inválidos: `05`, `005`).
* **Mes:** Número del 1 al 12. (Ejemplos válidos: `1`, `12`. Inválidos: `01`).
* **Año:** Número de 4 dígitos entre 1900 y el año actual. (Ejemplo: `1995`. Inválido: `95`).

### 2. Códigos de Error Comunes
Si la fecha es rechazada, el sistema le indicará el motivo:
* *Error de Formato:* Ha introducido letras, símbolos o ceros a la izquierda innecesarios.
* *Lógica Inválida:* Ha intentado crear una fecha imposible (ej: 30 de Febrero).
* *Bloqueo de Seguridad:* Si falla 5 veces seguidas, el programa se cerrará por su seguridad.
### 3. Ejecución del Programa
1. Compile el programa usando `javac Fechas.java`.

### 4. Ejecución de Baterías de Pruebas

`java TestAutomator`  # Para pruebas funcionales rápidas

`java TestRecursos`  # Para auditoría de hardware (RAM/CPU)

---

## 💻 Instrucciones de Instalación y Ejecución
Pasos para desplegar el proyecto en cualquier entorno compatible con Java.

**Requisitos Previos:**
* Java Development Kit (JDK) 8 o superior.

**Compilación:**
```bash
javac Fechas.java TestAutomator.java TestRecursos.java
```
---

**Autor:** Santiago Lafuente Hernández

**Asignatura:** Desarrollo de Interfaces - Unidad 8