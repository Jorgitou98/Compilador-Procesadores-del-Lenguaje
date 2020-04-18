package ast;

import java.util.List;

public class InsEnum extends Ins{
	private E nombre;
	private List<E> listaConstantes;
	
	
	public InsEnum(E nombre, List<E> listaConstantes) {
		this.nombre = nombre;
		this.listaConstantes = listaConstantes;
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
	
	

}
