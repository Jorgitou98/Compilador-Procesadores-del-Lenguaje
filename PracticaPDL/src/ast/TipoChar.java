package ast;

public class TipoChar extends Tipos{

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "char";
	}

	@Override
	public String imprime(String prev, boolean barra) {
		return prev + "\\__char\n";
	}
	public TipoT tipo() {
		return TipoT.CHAR;
	}
}
