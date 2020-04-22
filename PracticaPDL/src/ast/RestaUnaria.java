package ast;

public class RestaUnaria extends EUnaria{
	

	public RestaUnaria(E opnd1, boolean asignable) {
		super(opnd1, "(- unario)", asignable);
	}

	public TipoE tipo() {return TipoE.RESTAUNARIA;}

}

