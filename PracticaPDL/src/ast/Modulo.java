package ast;

public class Modulo extends EBin{
	public Modulo(E opnd1, E opnd2) {
	     super(opnd1,opnd2);  
	   }     
	public TipoE tipo() {return TipoE.MODULO;}
	public String toString() {return "modulo("+opnd1().toString()+","+opnd2().toString()+")";}

}
