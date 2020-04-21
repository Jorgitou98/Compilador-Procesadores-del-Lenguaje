package ast;

public abstract class CorchetesYPuntosIzq {
	private String name;
	private E expr;
	
	public CorchetesYPuntosIzq(String name, E expr) {
		super();
		this.name = name;
		this.expr = expr;
	}

	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__" + name + "\n";
		String next = prev;
		if(barra) next += "|";
		else next+=" ";
		for(int i = 0; i < ("__" + name).length(); ++i) {
			next += " ";
		}
		s = s + expr.imprime(next, false);
		return s;
	}
}
