package ast;

public class InsTypeDef extends Ins{
	private Tipos tipo;
	private E nombreNuevo;
	
	
	public InsTypeDef(Tipos tipo, E nombreNuevo, int fila, int columna) {
		this.tipo = tipo;
		this.nombreNuevo = nombreNuevo;
	     this.fila = fila;
	     this.columna = columna;
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



	public String imprime(String prev) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst TypeDef\n";
		String next = prev;
		if (barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Tipo antiguo\n";
		String nextTipo = next + "   |";
		for(int i = 0; i < "__Tipo antiguo".length(); ++i) {
			nextTipo += " ";
		}
		s+= tipo.imprime(nextTipo, false);
		s = s + next + "   \\__Nuevo nombre\n";
		String nextNuevoNombre = next;
		for(int i = 0; i < "   \\__Nuevo nombre".length(); ++i) {
			nextNuevoNombre += " ";
		}
		s += nombreNuevo.imprime(nextNuevoNombre, false);
		return s;
	}

}
