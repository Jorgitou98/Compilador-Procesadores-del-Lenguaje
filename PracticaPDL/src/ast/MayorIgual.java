package ast;

public class MayorIgual extends EBin{
	public MayorIgual(E opnd1, E opnd2, boolean asignable) {
	     super(opnd1,opnd2, "(>=)", asignable);  
	   }     
	public TipoE tipo() {return TipoE.MAYORIGUAL;}
}
