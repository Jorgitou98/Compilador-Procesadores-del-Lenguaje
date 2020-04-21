package ast;

public class True extends E{
	
	public TipoE tipo() {
		return TipoE.TRUE;
	}
	public String toString() {return "true";}
	@Override
	public String imprime(String prev, boolean barra) {
		return prev + "\\__true" + "\n";
	}

}
