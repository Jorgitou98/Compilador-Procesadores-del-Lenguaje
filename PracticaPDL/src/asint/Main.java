package asint;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import alex.AnalizadorLexicoTiny;
import asem.AnalizadorSemantico;
import ast.P;
import code.GeneradorCodigo;

public class Main {
   public static void main(String[] args) throws Exception {
	 if(args.length < 1) {
		 System.err.println("Introduzca el fichero de entreada como parámetro");
		 System.exit(1);
	 }
     Reader input = new InputStreamReader(new FileInputStream(args[0]));
	 AnalizadorLexicoTiny alex = new AnalizadorLexicoTiny(input);
	 AnalizadorSintacticoTiny asint = new AnalizadorSintacticoTiny(alex);
	 asint.setScanner(alex);
	 P prog = null;
	 try {
	 prog = (P) asint.parse().value;
	 }
	 catch(Exception e) {
		 System.err.println("Encontrado final de fichero. Imposible recuperarse del último error");
	 }

	 AnalizadorSemantico asem = new AnalizadorSemantico(prog);
	 asem.analizaSemantica();
	 if(prog != null) {
		 System.out.println(prog.imprime("", false));
		 GeneradorCodigo gc= new GeneradorCodigo();
		 gc.generaCodigo(prog);
	 }

 }
}   
   
