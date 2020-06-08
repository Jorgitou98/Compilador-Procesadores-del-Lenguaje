package ast;

public class Null extends E{
	public Null(boolean asignable, int fila, int columna) {
		super(asignable);
	     this.fila = fila;
	     this.columna = columna;
		// TODO Auto-generated constructor stub
	}
	public TipoE tipo() {
		return TipoE.NULL;
	}
	public String toString() {return "null";}
	@Override
	public String imprime(String prev, boolean barra) {
		return prev + "\\__null" + "\n";
	}
}
