package ast;

public class Size extends EUnaria{
	public Size(E opnd1, boolean asignable) {
		super(opnd1, "(Size)", asignable);
	}

	public TipoE tipo() {return TipoE.SIZE;}

}
