package ast;

public class TipoUsuario extends Tipos{
	private String nombreTipo;

	
	public TipoUsuario(String nombreTipo) {
		super();
		this.nombreTipo = nombreTipo;
	}
	
	public String getNombreTipo() {
		return nombreTipo;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nombreTipo;
	}

	@Override
	public String imprime(String prev, boolean barra) {
		return prev + "\\__" + nombreTipo + "\n";
	}
	

}
