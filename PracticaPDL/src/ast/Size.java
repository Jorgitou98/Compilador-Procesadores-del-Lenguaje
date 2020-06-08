package ast;

public class Size extends EUnaria{
	public Size(E opnd1, boolean asignable, int fila, int columna) {
		super(opnd1, "(Size)", asignable);
	     this.fila = fila;
	     this.columna = columna;
	}

	public TipoE tipo() {return TipoE.SIZE;}

}
