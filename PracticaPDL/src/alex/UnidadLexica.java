package alex;

import java_cup.runtime.Symbol;
public class UnidadLexica extends Symbol {
   private int fila;
   private int columna;
   public UnidadLexica(int fila, int columna, int clase, String lexema) {
     super(clase, new TokenValue(lexema, fila, columna));
	 this.fila = fila;
	 this.columna = columna;
   }
   public int clase () {return sym;}
   public String lexema() {return ((TokenValue) value).getLexema();}
   public int fila() {return fila;}
   public int columna() {return columna;}
}
