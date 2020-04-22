package ast;

public class Or extends EBin{
	public Or(E opnd1, E opnd2, boolean asignable) {
	     super(opnd1,opnd2, "(||)", asignable);  
	   }     
	public TipoE tipo() {return TipoE.OR;}

}