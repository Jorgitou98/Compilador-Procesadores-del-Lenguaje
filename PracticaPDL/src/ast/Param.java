package ast;

public class Param {
	private Tipos tipo;
	private TipoParam tipoDeParam;
	private E iden;
	public Param(Tipos tipo,  TipoParam tipoDeParam, E iden) {
		super();
		this.tipo = tipo;
		this.iden = iden;
		this.tipoDeParam = tipoDeParam;
	}
	public Tipos getTipo() {
		return tipo;
	}
	public E getIden() {
		return iden;
	}
	public TipoParam getTipoDeParam() {
		return tipoDeParam;
	}
	
	public String toString() {
		return "Param( Tipo: " + tipo.toString() + ", tipo de parametro: " + tipoDeParam.name() + ", iden: " + iden.toString() + ")";
	}
	
	
}
