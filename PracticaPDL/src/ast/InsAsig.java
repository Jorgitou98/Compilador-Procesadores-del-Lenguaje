package ast;


public class InsAsig extends Ins{
	private E var;
	private E valor;
	
	
	
	public InsAsig(E var, E valor, int fila, int columna) {
		super();
		this.var = var;
		this.valor = valor;
	     this.fila = fila;
	     this.columna = columna;
	}
	
	



	public E getVar() {
		return var;
	}






	public E getValor() {
		return valor;
	}
	
	





	public void setValor(E valor) {
		this.valor = valor;
	}





	@Override
	public TipoIns tipo() {
		
		return TipoIns.INSASIG;
	}



	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst Asig\n";
		String next = prev;
		if(barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Variable\n";
		String nextVar = next + "   |";
		for(int i = 0; i < "__Variable".length(); ++i) {
			nextVar += " ";
		}
		s += var.imprime(nextVar, false);
		
		s += next + "   \\__Valor\n";
		String nextValor = next;
		for(int i = 0; i < "   \\__Valor".length(); ++i) {
			nextValor += " ";
		}
		s = s + valor.imprime(nextValor, false);
		return s;
	}

}
