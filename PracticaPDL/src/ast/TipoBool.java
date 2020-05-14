package ast;

public class TipoBool extends Tipos{
	
	public String toString() {
		// TODO Auto-generated method stub
		return "bool";
	}

	@Override
	public String imprime(String prev, boolean barra) {
		return prev + "\\__bool\n";
	}
	public TipoT tipo() {
		return TipoT.BOOL;
	}
}
