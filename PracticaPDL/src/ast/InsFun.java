package ast;

import java.util.List;

public class InsFun extends Ins{
	private Tipos tipoReturn;
	private E nombre;
	private List<Param> parametros;
	private P instr;
	private E valorReturn;
	private int dirIni;
	private int tamParams = 0;
	private int pa;
	
	
	

	public InsFun(Tipos tipoReturn, E nombre, List<Param> parametros, P instr, E valorReturn, int fila, int columna) {
		super();
		this.tipoReturn = tipoReturn;
		this.nombre = nombre;
		this.parametros = parametros;
		this.instr = instr;
		this.valorReturn = valorReturn;
	     this.fila = fila;
	     this.columna = columna;
	}
	



	public Tipos getTipoReturn() {
		return tipoReturn;
	}
	
	




	public void setTipoReturn(Tipos tipoReturn) {
		this.tipoReturn = tipoReturn;
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




	public E getValorReturn() {
		return valorReturn;
	}
	
	




	public void setValorReturn(E valorReturn) {
		this.valorReturn = valorReturn;
	}




	@Override
	public TipoIns tipo() {
		return TipoIns.INSFUN;
	}
	
	public String toString() {
		String params = "";
		for(Param p: parametros) {
			params = params + ", " + p.toString();
		}
		return "InsFun( tipoReturn: " + tipoReturn.toString() + ", nombre: " + nombre.toString() + ", parametros: " + params + ", instrucciones: " + instr.toString() + ", valorReturn: " + valorReturn.toString() + ")"; 
	}



	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst Fun\n";
		String next = prev;
		if (barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Tipo Return\n";
		String nextTipoReturn = next + "   |";
		for(int i = 0; i < "__Tipo Return".length(); ++i) {
			nextTipoReturn += " ";
		}
		s += tipoReturn.imprime(nextTipoReturn, false);
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
		String nextInstr = next + "   |";
		for(int i = 0; i < "__Instrucciones".length(); ++i) {
			nextInstr += " ";
		}
		s += instr.imprime(nextInstr, false);
		s = s + next + "   \\__Inst Return\n";
		String nextInstReturn = next;
		for(int i = 0; i < "   \\__Inst Return".length(); ++i) {
			nextInstReturn += " ";
		}
		s += valorReturn.imprime(nextInstReturn, false);
		return s;
	}




	public int getDirIni() {
		return dirIni;
	}




	public void setDirIni(int dirIni) {
		this.dirIni = dirIni;
	}




	public int getTamParams() {
		return tamParams;
	}




	public void incTamParams(int inc) {
		this.tamParams += inc;
	}




	public int getPa() {
		return pa;
	}




	public void setPa(int pa) {
		this.pa = pa;
	}
	
	
	
	

}
