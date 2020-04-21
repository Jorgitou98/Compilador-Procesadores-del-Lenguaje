package ast;

public class Punto extends EBin{
	public Punto(E opnd1, E opnd2) {
	     super(opnd1,opnd2, "(.)");  
	   }     
	public TipoE tipo() {return TipoE.PUNTO;}
	public String toString() {return "punto("+opnd1().toString()+","+opnd2().toString()+")";}
}
