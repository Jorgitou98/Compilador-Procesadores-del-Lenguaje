package ast;

public class Suma extends EBin {
   public Suma(E opnd1, E opnd2, boolean asignable) {
     super(opnd1,opnd2, "(+)", asignable);  
   }     
   public TipoE tipo() {return TipoE.SUMA;}
}
