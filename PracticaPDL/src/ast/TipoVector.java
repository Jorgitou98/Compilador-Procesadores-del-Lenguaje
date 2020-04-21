package ast;

public class TipoVector extends Tipos{
	private Tipos tipoVector;

	public TipoVector(Tipos tipoVector) {
		super();
		this.tipoVector = tipoVector;
	}

	public Tipos getTipoVector() {
		return tipoVector;
	}

	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__vector\n";
		String next = prev;
		if (barra) next += "|";
		else next += " ";
		for(int i = 0; i < "__vector".length(); ++i) {
			next += " ";
		}
		s += tipoVector.imprime(next, false);
		return s;
	}
	
	
}
