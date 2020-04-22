package ast;

public class Distinto extends EBin{
	public Distinto(E opnd1, E opnd2, boolean asignable) {
	     super(opnd1,opnd2, "(!=)", asignable);  
	   }     
	public TipoE tipo() {return TipoE.DISTINTO;}
}
