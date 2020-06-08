package ast;

public class RestaUnaria extends EUnaria{
	

	public RestaUnaria(E opnd1, boolean asignable, int fila, int columna) {
		super(opnd1, "(- unario)", asignable);
	     this.fila = fila;
	     this.columna = columna;
	}

	public TipoE tipo() {return TipoE.RESTAUNARIA;}

}

