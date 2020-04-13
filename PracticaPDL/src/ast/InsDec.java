package ast;

public class InsDec extends Ins{
	
	private Tipos tipo;
	private E var;
	private boolean conValorInicial;
	private E valorInicial;
	
	

	public Tipos getTipo() {
		return tipo;
	}



	public E getVar() {
		return var;
	}



	public boolean isConValorInicial() {
		return conValorInicial;
	}



	public E getValorInicial() {
		return valorInicial;
	}



	public InsDec(Tipos tipo, E var, boolean conValorInicial, E valorInicial) {
		super();
		this.tipo = tipo;
		this.var = var;
		this.conValorInicial = conValorInicial;
		this.valorInicial = valorInicial;
	}



	@Override
	public TipoIns tipo() {
		return TipoIns.INSDEC;
	}
	
	public String toString() {
		String s = "InsDec( Tipo: " + tipo.toString() + ", nombre: " + var.toString();
		if(conValorInicial) {
			s = s + ", valor inicial: " + valorInicial.toString();
		}
		s += ")";
		return s;
	}

}
