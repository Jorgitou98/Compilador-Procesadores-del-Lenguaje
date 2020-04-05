package alex;


public class UnidadLexicaUnivaluada extends UnidadLexica {
   public String lexema() {throw new UnsupportedOperationException();}   
   public UnidadLexicaUnivaluada(int fila, int clase) {
     super(fila,clase, null);  
   }
  public String toString() {
    return "[clase:"+clase()+",fila:"+fila();  
  }   
}
