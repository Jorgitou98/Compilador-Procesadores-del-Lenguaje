package ast;

public class Suma extends EBin {
   public Suma(E opnd1, E opnd2, boolean asignable, int fila, int columna) {
     super(opnd1,opnd2, "(+)", asignable); 
     this.fila = fila;
     this.columna = columna;
   }     
   public TipoE tipo() {return TipoE.SUMA;}
}
