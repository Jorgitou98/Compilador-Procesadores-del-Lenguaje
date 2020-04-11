package ast;

import java.util.ArrayList;

public class P {
	private ArrayList<Ins> instr;

	public P(ArrayList<Ins> instr) {
		this.instr = instr;
	}
	
	public String toString() {
		String s = "";
		for(Ins i:instr) {
			s = s + ", "+ i.toString();
		}
		return s;
	}
	

}
