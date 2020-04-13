package ast;

import java.util.List;

public class LlamadaFun extends E{
	private List<E> argumentos;
	private E iden ;
	public LlamadaFun(E iden, List<E> argumentos) {
		this.argumentos = argumentos;
		this.iden = iden;
	}
	public TipoE tipo() {
		return TipoE.LLAMADAFUN;
	}
	
	public List<E> getArgumentos() {
		return argumentos;
	}
	public E getIden() {
		return iden;
	}
	public String toString() {
		String args ="";
		for(E exp:argumentos) {
			args = args + ", " +exp.toString();
		}
		return "funcion( iden: " + iden.toString() + ", argumentos: " + args + ")";
		
	}

}
