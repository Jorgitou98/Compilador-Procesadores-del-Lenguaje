package errors;

import alex.UnidadLexica;

public class GestionErroresTiny {
   public static void errorLexico(int fila, int columna, String lexema) {
     System.err.println("***ERROR léxico. Fila: "+ fila + ", Columna: " + columna + ": Elemento inesperado " + lexema); 
     System.exit(1);
   }  
   public void errorSintactico(UnidadLexica unidadLexica) {
     System.err.print("***ERROR sintáctico. Fila: "+ unidadLexica.fila() + ", Columna: " + unidadLexica.columna() + ": Elemento inesperado "+ unidadLexica.value);
     System.exit(1);
   }
}

