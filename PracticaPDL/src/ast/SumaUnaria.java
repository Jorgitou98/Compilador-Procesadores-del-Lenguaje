package ast;

public class SumaUnaria extends EUnaria{
	

	public SumaUnaria(E opnd1) {
		super(opnd1, "(+ unario)");
	}

	public TipoE tipo() {return TipoE.SUMAUNARIA;}

}

