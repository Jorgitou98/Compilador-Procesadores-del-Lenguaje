package ast;

public class InsCond extends Ins{
	private E condicion;
	private P insIf;
	private P insElse;
	private boolean tieneElse;
	
	

	public InsCond(E condicion, P insIf, P insElse) {
		super();
		this.condicion = condicion;
		this.insIf = insIf;
		this.insElse = insElse;
		this.tieneElse = true;
	}
	



	public InsCond(E condicion, P insIf) {
		super();
		this.condicion = condicion;
		this.insIf = insIf;
		this.tieneElse = false;
	}




	@Override
	public TipoIns tipo() {
		return TipoIns.INSCOND;
	}
	
	public String toString() {
		String s = "InsCond(condicion: " + condicion.toString() + "InsIf: "+ insIf.toString();
		if(tieneElse) {
			s = s + ", InsElse: " + insElse.toString();
		}
		s += ")";
		return s;
	}




	public E getCondicion() {
		return condicion;
	}




	public P getInsIf() {
		return insIf;
	}




	public P getInsElse() {
		return insElse;
	}




	public boolean isTieneElse() {
		return tieneElse;
	}

}
