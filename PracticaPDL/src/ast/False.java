package ast;

public class False extends E{
	
	public False(boolean asignable, int fila, int columna) {
		super(asignable);
	     this.fila = fila;
	     this.columna = columna;
	}
	public TipoE tipo() {
		return TipoE.FALSE;
	}
	@Override
	public String imprime(String prev, boolean barra) {
		return prev + "\\__false" + "\n";
	}

}
