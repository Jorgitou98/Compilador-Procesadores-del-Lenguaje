package ast;

public class True extends E{
	
	public True(boolean asignable, int fila, int columna) {
		super(asignable);
	     this.fila = fila;
	     this.columna = columna;
		// TODO Auto-generated constructor stub
	}
	public TipoE tipo() {
		return TipoE.TRUE;
	}
	public String toString() {return "true";}
	@Override
	public String imprime(String prev, boolean barra) {
		return prev + "\\__true" + "\n";
	}

}
