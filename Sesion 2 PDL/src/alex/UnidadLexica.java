package alex;

import asint.ClaseLexica;

public abstract class UnidadLexica {
   private int clase;
   private int fila;
   private int columna;

public UnidadLexica(int fila, int clase) {
    this.fila = fila;
    this.clase = clase;
}
public int clase () {return clase;}
   public abstract String lexema();
   public int fila() {return fila;}
   public int columna() {return columna;}
}
