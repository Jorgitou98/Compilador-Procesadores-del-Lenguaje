package ast;

public class DivEnt extends EBin{
	public DivEnt(E opnd1, E opnd2, boolean asignable) {
	     super(opnd1,opnd2, "(div)", asignable);  
	   }     
	public TipoE tipo() {return TipoE.DIVENT;}
}
