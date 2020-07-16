# Desarrollo de un compilador

Esta práctica para la asignatura de **Procesadores del Lenguaje** consistió en el desarrollo de un compilador que además generase código máquina ejecutable para un lenguaje de programación escogido por nosotros, que al menos debía ser Turing completo.

Se recomienda consultar la [memoria de la práctica](https://github.com/Jorgitou98/PracticasProcesadoresDelLenguaje/blob/master/Memoria%20de%20la%20pr%C3%A1ctica.pdf) que explica las instrucciones de las que goza el lenguaje reconocido, su sintaxis exacta y diversos detalles de implementación relevantes como la gestión y recuperación de errores.

## Fases de desarrollo

La compilación del lenguaje consta fundamentalmente de 3 fases:

| Fase de compilación | Herramienta principal usada | Función | Paquetes JAVA|
|--|--|--|--|
| Léxica | *JLex* |Reconocimiento de los ***tokens*** válidos para alguna construcción sintáctica del lenguaje | `alex`|
| Sintáctica | Parser *CUP* | Comprobación de que el programa de entrada es sintáticamente correcto y construcción del ***AST*** que recoge su estructura jerárquica | `asint` `ast` |
| Semántica | Tabla de símbolos| **Vinculación** de apariciones de uso de variables y funciones con sus instrucciones de declaración y **comprobación estática de tipos** | `asem` |


Además, siempre y cuando la compilación se supere sin errores se genera código para la [***máquina P***](https://github.com/Jorgitou98/PracticasProcesadoresDelLenguaje/blob/master/maquinaP.zip) (desarrollada en Haskell por un profesor), que sobre una memoria de 100 posiciones simula el comportamiento del programa. Incluyendo un espacio de ***Heap*** para emular gestión de memoria dinámica que permite nuestro lenguaje mediante la instrucióon ***new***.

La generación de código se realiza en el paquete ***code*** y consta de 2 fases:

| Fase de generación de código | Función | Clase JAVA
|--|--|--|
| Asignación estática de memoria | Establecer las **direcciones de memoria** de la máquina P donde se almacenarán las variables de programa calculando el tamaño de los tipos definidos por el usuario (*struct*, *typedef*...)  | `AsignadorDirecciones.java`
| Generación de instrucciones máquina | Escribir en un fichero de salida las instrucciones máquina que al ser ejecutadas sobre la *máquina P* emulan el comportamiento de la instrucción reconocida en la compilación  | `GeneradorCodigo.java` |

Se recomienda consultar el [repertorio de instrucciones máquina](https://github.com/Jorgitou98/PracticasProcesadoresDelLenguaje/blob/master/Instrucciones%20de%20la%20maquina%20p.pdf) y algunos de los [patrones de generación](https://github.com/Jorgitou98/PracticasProcesadoresDelLenguaje/blob/master/Patrones%20de%20generacion%20de%20codigo.pdf) de las sentencias más habituales en los lenguajes de programación, que se han utilizado en esta fase de la práctica.

## Funcionalidades más importantes del compilador y extras
A continuación se enumeran algunas de los aspectos más importantes del compilador:

- Se ha implementado cierta **recuperación de errores**. La compilación no se detiene con errores dentro de una misma fase y se muestran todos los errores léxicos, sintácticos o semánticos que existan, no solo el primero.

- Se muestran la **fila** y **columna** asociadas al *token* donde se ha producido el error y un mensaje suficientemente descriptivo que permita localizarlo y solventarlo.

- Se permiten **vectores dinámicos** en el sentido de vectores suyo tamaño solo es conocido en tiempo de compilación.

- El lenguaje permite **anidamiento arbitrario de funciones y procedimientos** (i.e. procedimientos o funciones locales al ámbito de otro procedimiento o función).

- Ausencia de una función de inicio de la ejecución (***main***). El programa se ejecuta *de arriba a abajo* según el usuario lo escribe.

- Cierto ***azúcar sintáctico*** que haga mas legible y cómoda la programación en nuestro lenguaje. Por ejemplo el bucle ***for*** que podría ser siempre simulado con un *while* o la instrucción ***switch*** que puede obetnerse a partir de sucesivos *if's*.

- Alguna **función predefinida** para la que se genera automáticamente código que desempeña labores rutinarias y habituales para simplificar la labor al usuario. 
	- La función `CreaVector(Tamaño, Valor Inicial)` para dar tamaño y valores inicial a las posiciones de un vector.
	
	- Una función ***size*** que devuelva automáticamente el tamaño de un vector mediante la sintaxis: `vector.size()`
	
- Visualización por la salida estándar de java de una representación intuitiva y detallada del ***AST*** (árbol de sintaxis abstracta) que resume la estructura del programa compilado.

## Juegos de prueba del compilador

La práctica incluye alrededor de une veintena de **ficheros de prueba** tanto con ejemplos de programas correctos que generan un fichero de salida en *código P*, como con errores de diversa índole para probar la correcta detección y recuperación de los mismos.

Para los ficheros correctos se genera como salida u fichero con idéntico nombre que el de entrada precedido de la palabra *code*.
