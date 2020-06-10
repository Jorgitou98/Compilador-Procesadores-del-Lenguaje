package ast;

import java.util.ArrayList;
import java.util.List;

public class Iden extends E {
	private String v;
	private NodoArbol ref;
	private Tipos tipo;
	private int dir;
	private int pa;
	private List<Integer> dimensiones = new ArrayList<>();
	private boolean inicializado = false;
	

	public Iden(String v, boolean asignable, int fila, int columna) {
		super(asignable);
	     this.fila = fila;
	     this.columna = columna;
		this.v = v;
	}

	public String id() {
		return v;
	}

	@Override
	public TipoE tipo() {
		return TipoE.IDEN;
	}

	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__" + v + '\n';
		if (ref != null) {
			s += prev + "\\__Ref\n";
			String next = prev;
			if (barra)
				next += "|";
			else
				next += " ";
			for (int i = 0; i < "__Ref".length(); ++i) {
				next+=" ";
			}
			s+= ref.imprime(next, false);
		}

		return s;
	}

	public void setRef(NodoArbol ref) {
		this.ref = ref;
	}
	
	public void setTipo(Tipos tipo) {
		this.tipo = tipo;
	}

	public Tipos getTipo() {
		return tipo;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public int getPa() {
		return pa;
	}

	public void setPa(int pa) {
		this.pa = pa;
	}

	public NodoArbol getRef() {
		return ref;
	}

	public List<Integer> getDimensiones() {
		return dimensiones;
	}

	public void setDimensiones(List<Integer> dimensiones) {
		this.dimensiones = dimensiones;
	}

	public boolean isInicializado() {
		return inicializado;
	}

	public void setInicializado(boolean inicializado) {
		this.inicializado = inicializado;
	}
	
	
	
	
	
	

}
