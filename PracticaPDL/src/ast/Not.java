package ast;

public class Not extends EUnaria{
	

	public Not(E opnd1) {
		super(opnd1, "(!)");
	}

	public TipoE tipo() {return TipoE.NOT;}

}
