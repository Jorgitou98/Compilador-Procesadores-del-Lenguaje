package ast;

import java.util.List;

public class InsAsig extends Ins{
	private E var;
	private List<CorchetesYPuntosIzq> cyp;
	private E valor;
	
	
	
	public InsAsig(E var, List<CorchetesYPuntosIzq> cyp, E valor) {
		super();
		this.var = var;
		this.cyp = cyp;
		this.valor = valor;
	}
	
	



	public E getVar() {
		return var;
	}





	public List<CorchetesYPuntosIzq> getCyp() {
		return cyp;
	}





	public E getValor() {
		return valor;
	}





	@Override
	public TipoIns tipo() {
		
		return TipoIns.INSASIG;
	}
	
	public String toString() {
		String s = "InsAsig( variable: " + var;
		for(CorchetesYPuntosIzq c:cyp) {
				s  = s + ", " + c.toString();
		}
		s = s + " valor inicial: " + valor.toString() + ")";
		return s;
	}

}
