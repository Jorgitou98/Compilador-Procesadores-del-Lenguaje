package ast;

public class Case {
	private E nombreCase;
	private P instr;
	public Case(E nombreCase, P instr) {
		super();
		this.nombreCase = nombreCase;
		this.instr = instr;
	}
	public E getNombreCase() {
		return nombreCase;
	}
	public P getInstr() {
		return instr;
	}
	
	public String toString() {
		return "case( nombre: " + nombreCase + ", instrucciones: " + instr.toString() + ")";
	}
}
