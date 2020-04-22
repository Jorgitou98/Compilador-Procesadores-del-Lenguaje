package ast;

public class And extends EBin{
	public And(E opnd1, E opnd2, boolean asignable) {
	     super(opnd1,opnd2, "(&&)", asignable);  
	   }     
	public TipoE tipo() {return TipoE.AND;}
	

}