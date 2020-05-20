package asint;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import alex.AnalizadorLexicoTiny;
import asem.AnalizadorSemantico;
import ast.P;

public class Main {
   public static void main(String[] args) throws Exception {
     Reader input = new InputStreamReader(new FileInputStream("input_3.txt"));
	 AnalizadorLexicoTiny alex = new AnalizadorLexicoTiny(input);
	 AnalizadorSintacticoTiny asint = new AnalizadorSintacticoTiny(alex);
	 asint.setScanner(alex);
	 //asint.parse();
	 //System.out.println(asint.parse().value);
	 P prog = (P) asint.parse().value;
	 AnalizadorSemantico asem = new AnalizadorSemantico(prog);
	 asem.analizaSemantica();
	 //System.out.println(CUP$AnalizadorSintacticoTiny$actions.numErrores);
	 System.out.println(prog.imprime("", false));
 }
}   
   
