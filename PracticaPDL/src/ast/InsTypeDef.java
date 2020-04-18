package ast;

public class InsTypeDef extends Ins{
	private Tipos tipo;
	private E nombreNuevo;
	
	
	public InsTypeDef(Tipos tipo, E nombreNuevo) {
		this.tipo = tipo;
		this.nombreNuevo = nombreNuevo;
	}
	

	public Tipos getTipo() {
		return tipo;
	}


	public E getNombreNuevo() {
		return nombreNuevo;
	}


	@Override
	public TipoIns tipo() {
		return TipoIns.INSTYPEDEF;
	}
	
	public String toString() {
		return "InsTypeDef(tipo: " + tipo.toString() + " nuevoNombre: " + nombreNuevo.toString();
	}

}
