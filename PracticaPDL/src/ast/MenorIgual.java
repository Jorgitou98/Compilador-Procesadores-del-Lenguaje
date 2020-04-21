package ast;

public class MenorIgual extends EBin{
	public MenorIgual(E opnd1, E opnd2) {
	     super(opnd1,opnd2, "(<=)");  
	   }     
	public TipoE tipo() {return TipoE.MENORIGUAL;}
}
