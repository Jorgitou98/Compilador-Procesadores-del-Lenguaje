package ast;

public class Iden extends E {
	private String v;
	private NodoArbol ref;

	public Iden(String v, boolean asignable) {
		super(asignable);
		this.v = v;
	}

	public String id() {
		return v;
	}

	@Override
	public TipoE tipo() {
		return TipoE.IDEN;
	}

	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__" + v + '\n';
		if (ref != null) {
			s += prev + "\\__Ref\n";
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

	public void setRef(NodoArbol ref) {
		this.ref = ref;
	}

}
