package errors;

import alex.UnidadLexica;
import ast.Iden;

public class GestionErroresTiny {
	public static int numErroresSintacticos = 0;
	public static int numErroresSemanticos = 0;

	public static void errorLexico(int fila, int columna, String lexema) {
		System.err.println(
				"***ERROR lexico. Fila: " + fila + ", Columna: " + columna + ": Elemento inesperado " + lexema);
		System.exit(1);
	}

	public static void errorSintactico(UnidadLexica unidadLexica) {
		System.err.println("***ERROR sintactico. Fila: " + unidadLexica.fila() + ", Columna: " + unidadLexica.columna()
				+ " Elemento inesperado " + unidadLexica.lexema());
		// System.exit(1);
	}

	public static void errorSemantico(int fila, int columna, String mensaje) {
		numErroresSemanticos++;
		if(numErroresSemanticos >= 15) {
			System.out.println("Exceso de errores sem�nticos. 15 errores encontrados. Comprobaci�n abortada");
			System.exit(1);
		}
		System.err.println("***ERROR semantico. Fila: " + fila + " Columna: " + columna + ". \n" + mensaje + '\n');
	}
	
}
