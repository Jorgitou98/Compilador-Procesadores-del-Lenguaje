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
				+ ": Elemento inesperado " + unidadLexica.lexema());
		// System.exit(1);
	}

	public static void errorSemantico(int fila, int columna, String mensaje) {
		numErroresSemanticos++;
		if(numErroresSemanticos >= 10) {
			System.out.println("Exceso de errores semánticos. Comprobación abortada");
			System.exit(1);
		}
		System.err.println("***ERROR semantico. Fila: " + fila + " Columna: " + columna + ". " + mensaje);
	}
	
	public static void warningInicializacin(Iden iden) {
		System.err.println("***Warning. Fila: " + iden.getFila()+ " Columna: " + iden.getColumna() + ". Variable posblemente no inicializada");
	}
}
