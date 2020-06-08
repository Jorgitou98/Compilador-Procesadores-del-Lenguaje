package ast;

public class InsCond extends Ins{
	private E condicion;
	private P insIf;
	private P insElse;
	private boolean tieneElse;
	
	

	public InsCond(E condicion, P insIf, P insElse, int fila, int columna) {
		super();
		this.condicion = condicion;
		this.insIf = insIf;
		this.insElse = insElse;
		this.tieneElse = true;
	     this.fila = fila;
	     this.columna = columna;
	}
	



	public InsCond(E condicion, P insIf, int fila, int columna) {
		super();
		this.condicion = condicion;
		this.insIf = insIf;
		this.tieneElse = false;
	     this.fila = fila;
	     this.columna = columna;
	}




	@Override
	public TipoIns tipo() {
		return TipoIns.INSCOND;
	}
	
	public String toString() {
		String s = "InsCond(condicion: " + condicion.toString() + "InsIf: "+ insIf.toString();
		if(tieneElse) {
			s = s + ", InsElse: " + insElse.toString();
		}
		s += ")";
		return s;
	}




	public E getCondicion() {
		return condicion;
	}
	
	




	public P getInsIf() {
		return insIf;
	}




	public P getInsElse() {
		return insElse;
	}




	public boolean isTieneElse() {
		return tieneElse;
	}
	
	


	public void setCondicion(E condicion) {
		this.condicion = condicion;
	}




	public void setInsIf(P insIf) {
		this.insIf = insIf;
	}




	public void setInsElse(P insElse) {
		this.insElse = insElse;
	}




	public void setTieneElse(boolean tieneElse) {
		this.tieneElse = tieneElse;
	}




	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst Cond\n";
		String next = prev;
		if(barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Condicion\n";
		String nextCond = next + "   |";
		for(int i = 0; i < "__Condicion".length(); ++i) {
			nextCond += " ";
		}
		 s += condicion.imprime(nextCond, false);
		 s = s + next + "   \\__If\n";
		 String nextIf = next;
		 if (tieneElse) {
			 nextIf += "   |";
			 for(int i = 0; i < "__If".length(); ++i) {
				 nextIf += " ";
			 }
			 s+= insIf.imprime(nextIf, false);
			 s = s + next +  "   \\__Else\n";
			 String nextElse = next;
			 for(int i = 0; i < "   \\__Else".length(); ++i) {
				 nextElse += " ";
			 }
			 s+= insElse.imprime(nextElse, false);
		 }
		 else {
			 for(int i = 0; i < "   \\__If".length(); ++i) {
				 nextIf += " ";
			 }
			 s+= insIf.imprime(nextIf, false);
		 }
		 
		return s;
	}

}
