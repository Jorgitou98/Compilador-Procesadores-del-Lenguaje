package ast;

import java.util.List;

public class InsCall extends Ins {
	private E nombre;
	private List<E> argumentos;

	private NodoArbol ref;

	public InsCall(E nombre, List<E> argumentos, int fila, int columna) {
		super();
		this.nombre = nombre;
		this.argumentos = argumentos;
	     this.fila = fila;
	     this.columna = columna;
	}

	public void setRef(NodoArbol ref) {
		this.ref = ref;
	}

	public E getNombre() {
		return nombre;
	}

	public List<E> getArgumentos() {
		return argumentos;
	}

	@Override
	public TipoIns tipo() {
		return TipoIns.INSCALL;
	}

	public String toString() {
		String args = "";
		for (E exp : argumentos) {
			args = args + ", " + exp.toString();
		}
		return "call( nombre: " + nombre.toString() + ", argumentos: " + args + ")";
	}

	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst Call\n";
		String next = prev;
		if (barra)
			next += "|";
		else
			next += " ";
		s = s + next + "   \\__Nombre\n";
		String nextNombre = next + "   |";
		for (int i = 0; i < "__Nombre".length(); ++i) {
			nextNombre += " ";
		}
		s += nombre.imprime(nextNombre, false);
		s = s + next + "   \\__Ref\n";

		String nextRef = next + "   |";
		for (int i = 0; i < "__Ref".length(); ++i) {
			nextRef += " ";
		}
		if (ref != null)
			s += ref.imprime(nextRef, false);
		else
			s += nextRef + "\\__ERROR\n";
		s = s + next + "   \\__Argumentos\n";
		String nextArgs = next;
		for (int i = 0; i < "   \\__Argumentos".length(); ++i) {
			nextArgs += " ";
		}
		for (int i = 0; i < argumentos.size(); ++i) {
			if (i == argumentos.size() - 1)
				s += argumentos.get(i).imprime(nextArgs, false);
			else
				s += argumentos.get(i).imprime(nextArgs, true);
		}
		return s;
	}

	public NodoArbol getRef() {
		return ref;
	}
	
	

}
