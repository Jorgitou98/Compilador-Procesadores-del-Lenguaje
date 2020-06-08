package ast;

public class InsDec extends Ins{
	
	private Tipos tipo;
	private E var;
	private boolean conValorInicial;
	private E valorInicial;
	
	

	public Tipos getTipo() {
		return tipo;
	}
	
	



	public void setTipo(Tipos tipo) {
		this.tipo = tipo;
	}





	public E getVar() {
		return var;
	}



	public boolean isConValorInicial() {
		return conValorInicial;
	}



	public E getValorInicial() {
		return valorInicial;
	}
	
	



	public void setValorInicial(E valorInicial) {
		this.valorInicial = valorInicial;
	}





	public InsDec(Tipos tipo, E var, boolean conValorInicial, E valorInicial, int fila, int columna) {
		super();
		this.tipo = tipo;
		this.var = var;
		this.conValorInicial = conValorInicial;
		this.valorInicial = valorInicial;
	     this.fila = fila;
	     this.columna = columna;
	}



	@Override
	public TipoIns tipo() {
		return TipoIns.INSDEC;
	}
	
	public String toString() {
		String s = "InsDec( Tipo: " + tipo.toString() + ", nombre: " + var.toString();
		if(conValorInicial) {
			s = s + ", valor inicial: " + valorInicial.toString();
		}
		s += ")";
		return s;
	}


	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst Dec\n";
		String next = prev;
		if(barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Tipo\n";
		String nextTipo = next + "   |";
		for(int i = 0; i < "__Tipo\n".length(); ++i) {
			nextTipo += " ";
		}
		s += tipo.imprime(nextTipo, false);
		s = s + next + "   \\__Nombre\n";
		String nextNombre = next;
		if(conValorInicial) {
			nextNombre += "   |";
			for(int i = 0; i < "__Nombre".length(); ++i) {
				nextNombre += " ";
			}
			s+= var.imprime(nextNombre, false);
			s = s + next + "   \\__Valor Ini\n";
			String nextValor = next;
			for(int i = 0; i < "   \\__Valor Ini".length(); ++i) {
				nextValor += " ";
			}
			s+= valorInicial.imprime(nextValor, false);
		}
		else {
			for(int i = 0; i < "   \\__Nombre".length(); ++i) {
				nextNombre += " ";
			}
			s += var.imprime(nextNombre, false);
		}
		return s;
	}



	public void setConValorInicial(boolean conValorInicial) {
		this.conValorInicial = conValorInicial;
	}

	
	
	

}
