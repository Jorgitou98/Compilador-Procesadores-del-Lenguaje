package asint;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import alex.AnalizadorLexicoTiny;
import asem.AnalizadorSemantico;
import ast.P;
import code.GeneradorCodigo;
import errors.GestionErroresTiny;

public class Main {
	public static String nombreFichero;

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Introduzca el fichero de entreada como parámetro");
			System.exit(1);
		}
		nombreFichero = args[0];
		Reader input = null;
		try {
			input = new InputStreamReader(new FileInputStream(nombreFichero));
		} catch (FileNotFoundException f) {
			System.err.println("No se pudo abrir el archivo");
			System.exit(1);
		}
		AnalizadorLexicoTiny alex = new AnalizadorLexicoTiny(input);
		AnalizadorSintacticoTiny asint = new AnalizadorSintacticoTiny(alex);
		asint.setScanner(alex);
		P prog = null;
		try {
			prog = (P) asint.parse().value;
			if (GestionErroresTiny.numErroresSintacticos > 0) {
				System.out.println(GestionErroresTiny.numErroresSintacticos
						+ " errores sintácticos encontrados.\nArregle los errores.\n");
				System.exit(1);
			}
		} catch (Exception e) {
			System.err.println("Encontrado final de fichero. Imposible recuperarse del último error");
			System.exit(1);
		}
		AnalizadorSemantico asem = new AnalizadorSemantico(prog);
		asem.analizaSemantica();
		if (GestionErroresTiny.numErroresSemanticos > 0) {
			System.out.println(GestionErroresTiny.numErroresSemanticos
					+ " errores semánticos encontrados.\nArregle los errores.\n");
			System.exit(1);
		}

		if (prog != null) {
			System.out.println(prog.imprime("", false));
			GeneradorCodigo gc = new GeneradorCodigo();
			gc.generaCodigo(prog);
		}
	}

}
