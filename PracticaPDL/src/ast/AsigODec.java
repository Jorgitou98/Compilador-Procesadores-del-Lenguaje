package ast;

import java.util.List;

public class AsigODec {
	private boolean esDec;
	private boolean conValorIni;
	private E valor;
	private E idenSiDec;
	private List<CorchetesYPuntosIzq> lista;
	
	public AsigODec(boolean esDec, boolean conValorIni, E valor, E idenSiDec, List<CorchetesYPuntosIzq> lista) {
		super();
		this.esDec = esDec;
		this.conValorIni = conValorIni;
		this.valor = valor;
		this.idenSiDec = idenSiDec;
		this.lista = lista;
	}
	public List<CorchetesYPuntosIzq> getLista() {
		return lista;
	}
	public void setIdenSiDec(Iden idenSiDec) {
		this.idenSiDec = idenSiDec;
	}
	public boolean isEsDec() {
		return esDec;
	}
	public boolean isConValorIni() {
		return conValorIni;
	}
	public E getValor() {
		return valor;
	}
	public E getIdenSiDec() {
		return idenSiDec;
	}
	public void anadeLista (CorchetesYPuntosIzq cyp) {
		lista.add(0, cyp);
	}
	public void setLista(List<CorchetesYPuntosIzq> lista) {
		this.lista = lista;
	}
	
	
}
