package ast;

public class TipoUsuario extends Tipos{
	private String nombreTipo;
	private NodoArbol ref;

	
	public TipoUsuario(String nombreTipo) {
		super();
		this.nombreTipo = nombreTipo;
	}
	
	public String getNombreTipo() {
		return nombreTipo;
	}


	public void setRef(NodoArbol ref) {
		this.ref = ref;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nombreTipo;
	}

	@Override
	public String imprime(String prev, boolean barra) {
		return prev + "\\__" + nombreTipo + "Ref: " + ref.imprime(prev+ "    ", false)+ "\n";
	}
	public TipoT tipo() {
		return TipoT.USUARIO;
	}

}
