package ast;

public class Mul extends EBin {
   public Mul(E opnd1, E opnd2, boolean asignable) {
     super(opnd1,opnd2, "(*)", asignable);  
   }     
   public TipoE tipo() {return TipoE.MUL;}
}