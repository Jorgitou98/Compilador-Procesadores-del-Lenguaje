package ast;

import java.util.ArrayList;
import java.util.List;

public class P extends NodoArbol{
	private List<Ins> instr;

	public P() {
		this.instr = new ArrayList<Ins>();
	}
	
	public String toString() {
		String s = "Programa(";
		for(Ins i:instr) {
			s = s + ", " + i.toString();
		}
		s += ")";
		return s;
	}
	public void anadeIns(Ins ins) {
		this.instr.add(0, ins);
	}
	
	
	public List<Ins> getInstr() {
		return instr;
	}

	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Programa\n";
		String next = prev;
		if(barra) next += "|";
		else next += " ";
		for(int i = 0; i < "\\__".length(); ++i) {
			next+= " ";
		}
		for(int i = 0; i < instr.size(); ++i) {
			if (i == instr.size() -1) s += instr.get(i).imprime(next, false);
			else s += instr.get(i).imprime(next, true);
			
		}
		return s;
	}
	public TipoN tipoNodo() {
		return TipoN.PROG;
	}

}
