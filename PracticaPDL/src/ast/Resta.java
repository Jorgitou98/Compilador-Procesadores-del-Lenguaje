package ast;

public class Resta extends EBin{
	public Resta(E opnd1, E opnd2, boolean asignable) {
	     super(opnd1,opnd2, "(-)", asignable);  
	   }     
	public TipoE tipo() {return TipoE.RESTA;}

}
