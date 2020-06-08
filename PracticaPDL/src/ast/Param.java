package ast;

public class Param extends NodoArbol{
	private Tipos tipo;
	private TipoParam tipoDeParam;
	private E iden;
	public Param(Tipos tipo,  TipoParam tipoDeParam, E iden, int fila, int columna) {
		super();
		this.tipo = tipo;
		this.iden = iden;
		this.tipoDeParam = tipoDeParam;
	     this.fila = fila;
	     this.columna = columna;
	}
	public Tipos getTipo() {
		return tipo;
	}
	
	public void setTipo(Tipos tipo) {
		this.tipo = tipo;
	}
	public E getIden() {
		return iden;
	}
	public TipoParam getTipoDeParam() {
		return tipoDeParam;
	}
	
	public String toString() {
		return "Param( Tipo: " + tipo.toString() + ", tipo de parametro: " + tipoDeParam.name() + ", iden: " + iden.toString() + ")";
	}
	
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Parametro\n";
		String next = prev;
		if (barra) next+= "|";
		else next += " ";
		s = s + next + "   \\__Tipo\n";
		String nextTipo = next + "   |";
		for(int i = 0; i < "__Tipo".length(); ++i) {
			nextTipo += " ";
		}
		s += tipo.imprime(nextTipo, false);
		s = s + next + "   \\__Identificador\n";
		String nextIden = next + "   |";
		for(int i = 0; i < "__Identificador".length(); ++i) {
			nextIden += " ";
		}
		s += iden.imprime(nextIden, false);
		s = s + next + "   \\__Tipo de parametro\n";
		String nextParam = next;
		for(int i = 0; i < "   \\__Tipo de parametro".length(); ++i) {
			nextParam += " ";
		}
		s += imprimeParam(nextParam);
		return s;
		
		
		
	}
	
	public String imprimeParam(String prev) {
		return prev + "\\__" + tipoDeParam.name() + "\n";
	}
	@Override
	public TipoN tipoNodo() {
		return TipoN.PARAM;
	}
	
	
}
