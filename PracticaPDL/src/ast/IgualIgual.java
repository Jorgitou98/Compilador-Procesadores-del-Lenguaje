package ast;

public class IgualIgual extends EBin{
	public IgualIgual(E opnd1, E opnd2, boolean asignable) {
	     super(opnd1,opnd2, "(==)", asignable);  
	   }     
	public TipoE tipo() {return TipoE.IGUALIGUAL;}
}
