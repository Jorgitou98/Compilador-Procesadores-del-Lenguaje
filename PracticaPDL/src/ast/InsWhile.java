package ast;

public class InsWhile extends Ins{
	
	private E condicion;
	private P insWhile;
	
	
	
	

	public InsWhile(E condicion, P insWhile, int fila, int columna) {
		super();
		this.condicion = condicion;
		this.insWhile = insWhile;
	     this.fila = fila;
	     this.columna = columna;
	}
	
	





	public E getCondicion() {
		return condicion;
	}
	
	







	public void setCondicion(E condicion) {
		this.condicion = condicion;
	}







	public P getInsWhile() {
		return insWhile;
	}







	@Override
	public TipoIns tipo() {
		return TipoIns.INSWHILE;
	}
	
	public String toString() {
		return "InsWhile(cond:  " + condicion.toString() + " Instrucciones: " + insWhile.toString() + ")";
	}



	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst While\n";
		String next = prev;
		if (barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Condicion\n";
		String nextCond = next + "   |";
		for(int i = 0; i < "__Condicion".length(); ++i) {
			nextCond += " ";
		}
		s += condicion.imprime(nextCond, false);
		s = s + next + "   \\__Instrucciones\n";
		String nextInstr = next;
		for(int i = 0; i < "   \\__Instrucciones".length(); ++i) {
			nextInstr += " ";
		}
		s += insWhile.imprime(nextInstr, false);
		return s;
	}

}
