package ast;

public class RestaUnaria extends EUnaria{
	

	public RestaUnaria(E opnd1) {
		super(opnd1, "(- unario)");
	}

	public TipoE tipo() {return TipoE.RESTAUNARIA;}

}

