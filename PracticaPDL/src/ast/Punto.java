package ast;

public class Punto extends EBin{
	private TipoUsuario tipo;
	public Punto(E opnd1, E opnd2, boolean asignable, int fila, int columna) {
	     super(opnd1,opnd2, "(.)", asignable);  
	     this.fila = fila;
	     this.columna = columna;
	   }     
	public TipoE tipo() {return TipoE.PUNTO;}
	public String toString() {return "punto("+opnd1().toString()+","+opnd2().toString()+")";}
	
	public TipoUsuario getTipo() {
		return tipo;
	}
	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}
	
	
}
