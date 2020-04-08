package ast;

public class MenorIgual extends EBin{
	public MenorIgual(E opnd1, E opnd2) {
	     super(opnd1,opnd2);  
	   }     
	public TipoE tipo() {return TipoE.MENORIGUAL;}
	public String toString() {return "menorIgual("+opnd1().toString()+","+opnd2().toString()+")";}
}
