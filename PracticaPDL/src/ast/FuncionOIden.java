package ast;

import java.util.List;

public class FuncionOIden {
	private boolean esIden;
	private List<E> argumentos;
	public FuncionOIden(boolean esIden, List<E> argumentos) {
		super();
		this.esIden = esIden;
		this.argumentos = argumentos;
	}
	public void anadeElem(E elem) {
		this.argumentos.add(0,elem);
	}
	public boolean isEsIden() {
		return esIden;
	}
	public List<E> getArgumentos() {
		return argumentos;
	}
	
	
}
