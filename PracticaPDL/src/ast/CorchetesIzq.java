package ast;

public class CorchetesIzq extends CorchetesYPuntosIzq{
	private E expr;
	
	

	public CorchetesIzq(E expr) {
		super();
		this.expr = expr;
	}
	
	



	public E getExpr() {
		return expr;
	}





	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "CorchetesIzq( " + expr.toString() + ")";
	}

}
