package ast;

public class Case extends NodoArbol{
	private E nombreCase;
	private P instr;
	private NodoArbol ref;
	public Case(E nombreCase, P instr, int fila, int columna) {
		super();
		this.nombreCase = nombreCase;
		this.instr = instr;
	     this.fila = fila;
	     this.columna = columna;
	}
	public E getNombreCase() {
		return nombreCase;
	}
	
	
	public void setNombreCase(E nombreCase) {
		this.nombreCase = nombreCase;
	}
	public P getInstr() {
		return instr;
	}
	
	public String toString() {
		return "case( nombre: " + nombreCase + ", instrucciones: " + instr.toString() + ")";
		
	}
	
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Case\n";
		String next = prev;
		if (barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Nombre Case\n";
		String nextNombre = next + "   |";
		for(int i = 0; i < "__Nombre Case".length(); ++i) {
			nextNombre += " ";
		}
		s += nombreCase.imprime(nextNombre, false);
		s = s + next + "   \\__Instrucciones\n";
		String nextInstr = next;
		for(int i = 0; i < "   \\__Instrucciones".length(); ++i) {
			nextInstr += " ";
		}
		s += instr.imprime(nextInstr, false);
		return s;
		
	}
	public TipoN tipoNodo() {
		return TipoN.CASE;
	}
	public NodoArbol getRef() {
		return ref;
	}
	public void setRef(NodoArbol ref) {
		this.ref = ref;
	}
	
	
	
}
