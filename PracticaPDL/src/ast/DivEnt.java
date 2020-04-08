package ast;

public class DivEnt extends EBin{
	public DivEnt(E opnd1, E opnd2) {
	     super(opnd1,opnd2);  
	   }     
	public TipoE tipo() {return TipoE.DIVENT;}
	public String toString() {return "divEnt("+opnd1().toString()+","+opnd2().toString()+")";}
}
