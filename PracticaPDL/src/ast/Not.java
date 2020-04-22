package ast;

public class Not extends EUnaria{
	

	public Not(E opnd1, boolean asignable) {
		super(opnd1, "(!)", asignable);
	}

	public TipoE tipo() {return TipoE.NOT;}

}
