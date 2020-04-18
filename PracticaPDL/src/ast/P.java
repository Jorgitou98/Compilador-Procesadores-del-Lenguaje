package ast;

import java.util.ArrayList;
import java.util.List;

public class P {
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
	

}
