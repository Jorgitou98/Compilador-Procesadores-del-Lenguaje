package ast;

public class Menor extends EBin{
	public Menor(E opnd1, E opnd2) {
	     super(opnd1,opnd2);  
	   }     
	public TipoE tipo() {return TipoE.MENOR;}
	public String toString() {return "menor("+opnd1().toString()+","+opnd2().toString()+")";}
}
