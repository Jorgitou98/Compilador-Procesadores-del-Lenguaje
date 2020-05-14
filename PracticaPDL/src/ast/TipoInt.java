package ast;

public class TipoInt extends Tipos{

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "int";
	}

	@Override
	public String imprime(String prev, boolean barra) {
		return prev + "\\__int\n";
	}
	public TipoT tipo() {
		return TipoT.INT;
	}
}
