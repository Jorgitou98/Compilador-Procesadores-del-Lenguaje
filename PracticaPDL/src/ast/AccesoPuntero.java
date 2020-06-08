package ast;

public class AccesoPuntero extends EUnaria {

	public AccesoPuntero(E opnd1, boolean asignable, int fila, int columna) {
		super(opnd1, "(* unario)", asignable);
	     this.fila = fila;
	     this.columna = columna;
	}

	public TipoE tipo() {
		return TipoE.ACCESOPUNTERO;
	}

}
