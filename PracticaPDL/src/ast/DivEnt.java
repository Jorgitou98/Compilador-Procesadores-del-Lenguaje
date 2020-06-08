package ast;

public class DivEnt extends EBin{
	public DivEnt(E opnd1, E opnd2, boolean asignable, int fila, int columna) {
	     super(opnd1,opnd2, "(div)", asignable);
	     this.fila = fila;
	     this.columna = columna;
	   }     
	public TipoE tipo() {return TipoE.DIVENT;}
}
