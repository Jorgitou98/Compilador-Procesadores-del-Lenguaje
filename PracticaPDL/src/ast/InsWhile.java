package ast;

public class InsWhile extends Ins{
	
	private E condicion;
	private P insWhile;
	
	
	
	

	public InsWhile(E condicion, P insWhile) {
		super();
		this.condicion = condicion;
		this.insWhile = insWhile;
	}
	
	





	public E getCondicion() {
		return condicion;
	}







	public P getInsWhile() {
		return insWhile;
	}







	@Override
	public TipoIns tipo() {
		return TipoIns.INSWHILE;
	}
	
	public String toString() {
		return "InsWhile(cond:  " + condicion.toString() + " Instrucciones: " + insWhile.toString() + ")";
	}

}
