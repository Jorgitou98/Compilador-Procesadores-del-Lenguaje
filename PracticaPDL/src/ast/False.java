package ast;

public class False extends E{
	
	public TipoE tipo() {
		return TipoE.FALSE;
	}
	@Override
	public String imprime(String prev, boolean barra) {
		return prev + "\\__false" + "\n";
	}

}
