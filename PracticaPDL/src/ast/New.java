package ast;

import java.util.List;

public class New extends E {
	private List<Integer> tamanos;
	private Tipos tipo;
	private int tam;

	public New(List<Integer> tamanos, Tipos tipo, boolean asignable, int fila, int columna) {
		super(asignable);
		this.tamanos = tamanos;
		this.tipo = tipo;
	     this.fila = fila;
	     this.columna = columna;
		// TODO Auto-generated constructor stub
	}
	

	public Tipos getTipo() {
		return tipo;
	}


	public List<Integer> getTamanos() {
		return tamanos;
	}

	@Override
	public TipoE tipo() {
		// TODO Auto-generated method stub
		return TipoE.NEW;
	}
	
	

	public int getTam() {
		return tam;
	}


	public void setTam(int tam) {
		this.tam = tam;
	}


	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__New\n";
		String next = prev;
		if (barra)
			next += "|";
		else
			next += " ";

		if (tamanos.size() > 0) {

			s = s + next + "   \\__Tamanos\n";
			String nextValor = next;

			for (int i = 0; i < "   \\__Tamanos".length(); ++i) {
				nextValor += " ";
			}
			s += nextValor+ "\\__[" + tamanos.get(0);
			for (int i = 1; i < tamanos.size(); ++i) {
				s += ", " + tamanos.get(i);
			}
			s += "]\n";
		}
		return s;
	}

}
