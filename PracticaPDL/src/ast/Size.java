package ast;

public class Size extends EUnaria{
	public Size(E opnd1) {
		super(opnd1, "(Size)");
	}

	public TipoE tipo() {return TipoE.SIZE;}

}
