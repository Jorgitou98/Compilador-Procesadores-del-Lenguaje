package ast;

public class Vector extends E {
	  private E tam;
	  private E valorIni;
	  public Vector(E valorIni, E tam, boolean asignable, int fila, int columna) {
		  super(asignable);
	   this.tam = tam;
	   this.valorIni = valorIni;
	     this.fila = fila;
	     this.columna = columna;
	   
	  }
	  public E getTam() {return tam;} 
	  public E getValorIni() {return valorIni;}
	  public String toString() {return ("vector(valorIni: "+ valorIni.toString() + ", tam: "+ tam.toString() + ")");}
	@Override
	public TipoE tipo() {
		return TipoE.VECTOR;
	}
	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Vector\n";
		String next = prev;
		if (barra) next += "|";
		else next += " ";
		
		s = s + next + "   \\__Tam\n";
		String nextTam = next + "   |";
		
		for (int i = 0; i < "__Tam".length(); ++i) {
			nextTam += " ";
		}
		s = s + tam.imprime(nextTam, false);
		
		s = s + next + "   \\__Valor Ini\n";
		String nextValor = next;
		
		for (int i = 0; i < "   \\__Valor Ini".length(); ++i) {
			nextValor += " ";
		}
		
		s = s + valorIni.imprime(nextValor, false);
		return s;
		
		
	}
	
	}
