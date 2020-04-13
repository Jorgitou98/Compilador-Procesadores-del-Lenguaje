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
	public String toString() {
		return "vector<" + tipoVector.toString() + ">";
	}
	
	
}
