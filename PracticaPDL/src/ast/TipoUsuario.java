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
		String s = prev + "\\__" + nombreTipo + '\n';
		s += prev + "\\__Ref\n";
		if (ref != null) {
			String next = prev;
			if (barra)
				next += "|";
			else
				next += " ";
			for (int i = 0; i < "__Ref".length(); ++i) {
				next+=" ";
			}
			s+= ref.imprime(next, false);
		}

		return s;
	}
	public TipoT tipo() {
		return TipoT.USUARIO;
	}

}
