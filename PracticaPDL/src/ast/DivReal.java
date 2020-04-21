package ast;

public class DivReal extends EBin{
	public DivReal(E opnd1, E opnd2) {
	     super(opnd1,opnd2, "(/)");  
	   }     
	public TipoE tipo() {return TipoE.DIVREAL;}
	public String toString() {return "divReal("+opnd1().toString()+","+opnd2().toString()+")";}
}

