package ast;

public class Real extends Num{

	public Real(String v, boolean asignable, int fila, int columna) {
		super(v, asignable);
	     this.fila = fila;
	     this.columna = columna;
		// TODO Auto-generated constructor stub
	}
	public TipoE tipo() {return TipoE.FLOAT;}
	
	

}
