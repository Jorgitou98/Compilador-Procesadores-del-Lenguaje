package ast;

import java.util.List;

public class InsCall extends Ins{
	private E nombre;
	private List<E> argumentos;
	
	

	public InsCall(E nombre, List<E> argumentos) {
		super();
		this.nombre = nombre;
		this.argumentos = argumentos;
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
		for( E exp: argumentos) {
			args = args + ", " + exp.toString();
		}
		return "call( nombre: " + nombre.toString() + ", argumentos: " + args + ")";
	}

}
