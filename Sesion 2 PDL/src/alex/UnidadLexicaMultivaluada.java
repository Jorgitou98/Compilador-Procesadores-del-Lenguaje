package alex;

import asint.ClaseLexica;

public class UnidadLexicaMultivaluada extends UnidadLexica {
  private String lexema;
  public UnidadLexicaMultivaluada(int fila, int clase, String lexema) {
     super(fila, clase);  
     this.lexema = lexema;
   }
  public String lexema() {return lexema;}
  public String toString() {
    return "[clase:"+clase()+",fila:"+fila()+",lexema:"+lexema()+"]";  
  }
}
