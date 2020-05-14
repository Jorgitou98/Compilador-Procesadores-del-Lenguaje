package ast;

public class InsNew extends Ins {
	// Tipo primitivo
	private Tipos tipo;
	private E var;
	private E valor;
	
	
	public InsNew(Tipos tipo, E var, E valor) {
		super();
		this.tipo = tipo;
		this.var = var;
		this.valor = valor;
	}

	public E getVar() {
		return var;
	}

	public Tipos getTipo() {
		return tipo;
	}

	public E getValor() {
		return valor;
	}

	@Override
	public TipoIns tipo() {
		return TipoIns.INSNEW;
	}

	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst New\n";
		String next = prev;
		if(barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Tipo\n";
		String nextTipo = next + "   |";
		for(int i = 0; i < "__Tipo\n".length(); ++i) {
			nextTipo += " ";
		}
		s += tipo.imprime(nextTipo, false);
		s = s + next + "   \\__Nombre\n";
		String nextNombre = next;
		nextNombre += "   |";
		for(int i = 0; i < "__Nombre".length(); ++i) {
			nextNombre += " ";
		}
		s+= var.imprime(nextNombre, false);
		s = s + next + "   \\__Valor Ini\n";
		String nextValor = next;
		for(int i = 0; i < "   \\__Valor Ini".length(); ++i) {
			nextValor += " ";
		}
		s+= valor.imprime(nextValor, false);

		return s;
	}
}
