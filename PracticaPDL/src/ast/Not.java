package ast;

public class Not extends EUnaria{
	

	public Not(E opnd1, boolean asignable, int fila, int columna) {
		super(opnd1, "(!)", asignable);
	     this.fila = fila;
	     this.columna = columna;
	}

	public TipoE tipo() {return TipoE.NOT;}

}
