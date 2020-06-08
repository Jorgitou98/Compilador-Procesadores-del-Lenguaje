package ast;

import java.util.List;

public class InsSwitch extends Ins{
	private E varSwitch;
	private List<Case> listaCase;
	private Tipos tipoSwitch;
	public InsSwitch(E varSwitch, List<Case> listaCase, int fila, int columna) {
		super();
		this.listaCase = listaCase;
		this.varSwitch = varSwitch;
	     this.fila = fila;
	     this.columna = columna;
	}
	
	
	


	public Tipos getTipoSwitch() {
		return tipoSwitch;
	}





	public void setTipoSwitch(Tipos tipoSwitch) {
		this.tipoSwitch = tipoSwitch;
	}





	public E getVarSwitch() {
		return varSwitch;
	}
	
	
	public void setVarSwitch(E varSwitch) {
		this.varSwitch = varSwitch;
	}





	public List<Case> getListaCase() {
		return listaCase;
	}
	@Override
	public TipoIns tipo() {
		return TipoIns.INSSWITCH;
	}
	

	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst Switch\n";
		String next = prev;
		if (barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Var Switch\n";
		String nextVar = next + "   |";
		for(int i = 0; i < "__Var Switch".length(); ++i) {
			nextVar += " ";
		}
		s += varSwitch.imprime(nextVar, false);
		
		s = s + next + "   \\__TipoSwitch\n";
		String nextRef = next + "   |";
		for (int i = 0; i < "__TipoSwitch".length(); ++i) {
			nextRef += " ";
		}
		if (tipoSwitch != null)
			s += tipoSwitch.imprime(nextRef, false);
		else
			s += nextRef + "\\__ERROR\n";
		
		s = s + next + "   \\__Cases\n";
		String nextCases = next;
		for(int i = 0; i < "   \\__Cases".length(); ++i) {
			nextCases += " ";
		}
		for(int i = 0; i < listaCase.size(); ++i) {
			if(i == listaCase.size()-1) s += listaCase.get(i).imprime(nextCases, false);
			else s += listaCase.get(i).imprime(nextCases, true);		
		}
		return s;
	}

	
	
	
	

}
