package ast;

public class SumaUnaria extends EUnaria{
	

	public SumaUnaria(E opnd1, boolean asignable) {
		super(opnd1, "(+ unario)", asignable);
	}

	public TipoE tipo() {return TipoE.SUMAUNARIA;}

}

