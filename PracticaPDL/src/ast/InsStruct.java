package ast;

import java.util.List;

public class InsStruct extends Ins{
	private E nombreTipo;
	private List<Ins> declaraciones;
	
	
	public InsStruct(E nombreTipo, List<Ins> declaraciones, int fila, int columna) {
		this.nombreTipo = nombreTipo;
		this.declaraciones = declaraciones;
	     this.fila = fila;
	     this.columna = columna;
	}



	public E getNombreTipo() {
		return nombreTipo;
	}



	public List<Ins> getDeclaraciones() {
		return declaraciones;
	}



	@Override
	public TipoIns tipo() {
		return TipoIns.INSSTRUCT;
	}


	public String toString() {
		String s = "InsStruct(nombre: " + nombreTipo + ", declaraciones: ";
		for (Ins ins: declaraciones) {
			s += ins.toString() + ", ";
		}
		s += ")";
		return s;
	}

	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst Struct\n";
		String next = prev;
		if (barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Nombre\n";
		String nextNombre = next + "   |";
		for(int i = 0; i < "__Nombre".length(); ++i) {
			nextNombre += " ";
		}
		s += nombreTipo.imprime(nextNombre, false);
		s = s + next + "   \\__Declaraciones\n";
		String nextDec = next;
		for(int i = 0; i < "   \\__Declaraciones".length(); ++i) {
			nextDec += " ";
		}
		for(int i = 0; i < declaraciones.size(); ++i) {
			if(i == declaraciones.size()-1) s += declaraciones.get(i).imprime(nextDec, false);
			else s += declaraciones.get(i).imprime(nextDec, true);		
		}
		return s;
	}

	
}
