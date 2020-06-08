package ast;

import java.util.List;

public class InsEnum extends Ins{
	private E nombre;
	private List<E> listaConstantes;
	
	
	public InsEnum(E nombre, List<E> listaConstantes, int fila, int columna) {
		this.nombre = nombre;
		this.listaConstantes = listaConstantes;
	     this.fila = fila;
	     this.columna = columna;
	}

	@Override
	public TipoIns tipo() {
		return TipoIns.INSENUM;
	}

	public E getNombre() {
		return nombre;
	}

	public List<E> getListaConstantes() {
		return listaConstantes;
	}

	@Override
	public String toString() {
		 String s = "InsEnum(nombre: " + nombre.toString() + ", valores: ";
		 for(E cte: listaConstantes) {
			 s += cte.toString() + ", ";
		 }
		 return s;
	}

	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst Enum\n";
		String next = prev;
		if(barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Nombre\n";
		String nextNombre = next + "   |";
		for(int i = 0; i < "__Nombre".length(); ++i) {
			nextNombre += " ";
		}
		s += nombre.imprime(nextNombre, false);
		s = s + next + "   \\__Valores\n";
		String nextValores = next;
		for (int i = 0; i < "   \\__Valores".length(); ++i) {
			nextValores += " ";
		}
		for(E cte: listaConstantes) {
			 s += cte.imprime(nextValores, false);
		}
		return s;
		
	}
	
	

}
