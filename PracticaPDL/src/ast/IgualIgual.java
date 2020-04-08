package ast;

public class IgualIgual extends EBin{
	public IgualIgual(E opnd1, E opnd2) {
	     super(opnd1,opnd2);  
	   }     
	public TipoE tipo() {return TipoE.IGUALIGUAL;}
	public String toString() {return "igualIgual("+opnd1().toString()+","+opnd2().toString()+")";}
}
