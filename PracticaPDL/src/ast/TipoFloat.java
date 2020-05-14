package ast;

public class TipoFloat extends Tipos{

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "float";
	}

	@Override
	public String imprime(String prev, boolean barra) {
		return prev + "\\__float\n";
	}
	public TipoT tipo() {
		return TipoT.FLOAT;
	}
}
