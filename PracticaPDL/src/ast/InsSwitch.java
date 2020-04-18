package ast;

import java.util.List;

public class InsSwitch extends Ins{
	private E varSwitch;
	private List<CorchetesYPuntosIzq> cyp;
	private List<Case> listaCase;
	public InsSwitch(E varSwitch, List<CorchetesYPuntosIzq> cyp, List<Case> listaCase) {
		super();
		this.listaCase = listaCase;
		this.varSwitch = varSwitch;
		this.cyp = cyp;
	}
	public List<CorchetesYPuntosIzq> getCyp() {
		return cyp;
	}
	public E getVarSwitch() {
		return varSwitch;
	}
	public List<Case> getListaCase() {
		return listaCase;
	}
	@Override
	public TipoIns tipo() {
		return TipoIns.INSSWITCH;
	}
	
	public String toString() {
		String cases = "";
		for (Case c: listaCase) {
			cases = cases + ", " + c.toString();
		}
		String spyc = "";
		for (CorchetesYPuntosIzq c: cyp) {
			spyc = spyc + ", " + c.toString();
		}
		return "InsSwitch(varSwitch: " + varSwitch.toString()+ ", PuntosYCorcehetes: " +spyc+ ", Cases: " + cases + ")";
	}
	
	
	

}
