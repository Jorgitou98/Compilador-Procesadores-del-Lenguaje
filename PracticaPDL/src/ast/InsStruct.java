package ast;

import java.util.List;

public class InsStruct extends Ins{
	private E nombreTipo;
	private List<Ins> declaraciones;
	
	
	public InsStruct(E nombreTipo, List<Ins> declaraciones) {
		this.nombreTipo = nombreTipo;
		this.declaraciones = declaraciones;
	}



	public E getNombreTipo() {
		return nombreTipo;
	}



	public List<Ins> getDeclaraciones() {
		return declaraciones;
	}



	@Override
	public TipoIns tipo() {
		return TipoIns.INSSTRUCT;
	}


	public String toString() {
		String s = "InsStruct(nombre: " + nombreTipo + ", declaraciones: ";
		for (Ins ins: declaraciones) {
			s += ins.toString() + ", ";
		}
		s += ")";
		return s;
	}
	
}
