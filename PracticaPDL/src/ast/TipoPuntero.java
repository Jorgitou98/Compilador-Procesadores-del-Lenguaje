package ast;

public class TipoPuntero extends Tipos{
	private Tipos tipoPuntero;

	public TipoPuntero(Tipos tipoPuntero, int fila, int columna) {
		super();
		this.tipoPuntero = tipoPuntero;
		this.fila = fila;
		this.columna = columna;
	}

	public Tipos getTipoPuntero() {
		return tipoPuntero;
	}
	
	

	public void setTipoPuntero(Tipos tipoPuntero) {
		this.tipoPuntero = tipoPuntero;
	}

	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__puntero\n";
		String next = prev;
		if (barra) next += "|";
		else next += " ";
		for(int i = 0; i < "__puntero".length(); ++i) {
			next += " ";
		}
		s += tipoPuntero.imprime(next, false);
		return s;
	}
	
	public TipoT tipo() {
		return TipoT.PUNTERO;
	}
}
