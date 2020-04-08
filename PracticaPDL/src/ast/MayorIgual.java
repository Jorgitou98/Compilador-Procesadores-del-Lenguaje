package ast;

public class MayorIgual extends EBin{
	public MayorIgual(E opnd1, E opnd2) {
	     super(opnd1,opnd2);  
	   }     
	public TipoE tipo() {return TipoE.MAYORIGUAL;}
	public String toString() {return "mayorIgual("+opnd1().toString()+","+opnd2().toString()+")";}
}
