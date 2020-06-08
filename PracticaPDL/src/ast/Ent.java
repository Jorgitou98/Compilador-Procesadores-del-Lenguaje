package ast;

public class Ent extends Num{

	public Ent(String v, boolean asignable, int fila, int columna) {
		super(v, asignable);
	     this.fila = fila;
	     this.columna = columna;
	}
	public TipoE tipo() {return TipoE.INT;}
	
	

}
