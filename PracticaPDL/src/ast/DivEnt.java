package ast;

public class DivEnt extends EBin{
	public DivEnt(E opnd1, E opnd2) {
	     super(opnd1,opnd2, "(div)");  
	   }     
	public TipoE tipo() {return TipoE.DIVENT;}
}
