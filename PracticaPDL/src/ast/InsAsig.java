package ast;

import java.util.List;

public class InsAsig extends Ins{
	private E var;
	private List<CorchetesYPuntosIzq> cyp;
	private E valor;
	
	
	
	public InsAsig(E var, List<CorchetesYPuntosIzq> cyp, E valor) {
		super();
		this.var = var;
		this.cyp = cyp;
		this.valor = valor;
	}
	
	



	public E getVar() {
		return var;
	}





	public List<CorchetesYPuntosIzq> getCyp() {
		return cyp;
	}





	public E getValor() {
		return valor;
	}





	@Override
	public TipoIns tipo() {
		
		return TipoIns.INSASIG;
	}



	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst Asig\n";
		String next = prev;
		if(barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Variable\n";
		String nextVar = next + "   |";
		for(int i = 0; i < "__Variable".length(); ++i) {
			nextVar += " ";
		}
		s += var.imprime(nextVar, false);
		String nextPyc = nextVar + "    ";
		for (int i = 0; i < cyp.size(); ++i ) {
			if(i == cyp.size()-1) s+= (cyp.get(i)).imprime(nextPyc, false);
			else s+= (cyp.get(i)).imprime(nextPyc, true);
			
		}
		s += next + "   \\__Valor\n";
		String nextValor = next;
		for(int i = 0; i < "   \\__Valor".length(); ++i) {
			nextValor += " ";
		}
		s = s + valor.imprime(nextValor, false);
		return s;
	}

}
