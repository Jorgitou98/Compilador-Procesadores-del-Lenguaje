package ast;

public class AccesoPuntero extends EUnaria {

	public AccesoPuntero(E opnd1, boolean asignable) {
		super(opnd1, "(* unario)", asignable);
	}

	public TipoE tipo() {return TipoE.ACCESOPUNTERO;}

}
