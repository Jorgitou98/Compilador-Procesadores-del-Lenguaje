package ast;

public class Size extends EUnaria{
	public Size(E opnd1) {
		super(opnd1);
	}

	public TipoE tipo() {return TipoE.SIZE;}
	public String toString() {return "size("+opnd1().toString()+")";}

}
