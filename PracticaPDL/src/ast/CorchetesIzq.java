package ast;

public class CorchetesIzq extends CorchetesYPuntosIzq{
	private E expr;
	
	

	public CorchetesIzq(E expr) {
		super("CorchetesIzq", expr);
		this.expr = expr;
	}
	
	



	public E getExpr() {
		return expr;
	}
}
