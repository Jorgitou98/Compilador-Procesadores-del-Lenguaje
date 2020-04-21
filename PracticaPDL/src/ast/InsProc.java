package ast;

import java.util.List;

public class InsProc extends Ins{
	private E nombre;
	private List<Param> parametros;
	private P instr;
	
	

	public InsProc(E nombre, List<Param> parametros, P instr) {
		super();
		this.nombre = nombre;
		this.parametros = parametros;
		this.instr = instr;
	}
	
	



	public E getNombre() {
		return nombre;
	}





	public List<Param> getParametros() {
		return parametros;
	}





	public P getInstr() {
		return instr;
	}





	@Override
	public TipoIns tipo() {
		return TipoIns.INSPROC;
	}
	
	public String toString() {
		String params = "";
		for(Param p: parametros) {
			params = params + ", " + p.toString();
		}
		return "InsProc( nombre: " + nombre.toString() + ", parametros: " + params + ", instrucciones: " + instr.toString() + ")"; 
	}



	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst Proc\n";
		String next = prev;
		if (barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Nombre\n";
		String nextNombre = next + "   |";
		for(int i = 0; i < "__Nombre".length(); ++i) {
			nextNombre += " ";
		}
		s += nombre.imprime(nextNombre, false);
		s = s + next + "   \\__Parametros\n";
		String nextParam = next + "   |";
		for(int i = 0; i < "__Parametros".length(); ++i) {
			nextParam += " ";
		}
		for(int i = 0; i < parametros.size(); ++i) {
			if(i == parametros.size()-1) s += parametros.get(i).imprime(nextParam, false);
			else s += parametros.get(i).imprime(nextParam, true);		
		}
		s = s + next + "   \\__Instrucciones\n";
		String nextInstr = next;
		for(int i = 0; i < "   \\__Instrucciones".length(); ++i) {
			nextInstr += " ";
		}
		s += instr.imprime(nextInstr, false);
		return s;
	}


}
