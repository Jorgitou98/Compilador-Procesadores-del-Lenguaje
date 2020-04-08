package ast;

public class Vector extends E {
	  private E tam;
	  private E valorIni;
	  public Vector(E tam, E valorIni) {
	   this.tam = tam;
	   this.valorIni = valorIni;
	   
	  }
	  public E tam() {return tam;} 
	  public E valorIni() {return valorIni;}
	  public String toString() {return ("vector(valorIni: "+ valorIni.toString() + ", tam: "+ tam.toString() + ")");}
	@Override
	public TipoE tipo() {
		return TipoE.VECTOR;
	}  
	}
