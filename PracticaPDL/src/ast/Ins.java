package ast;

public abstract class Ins extends NodoArbol{
	public abstract TipoIns tipo(); 
	public abstract String imprime(String prev, boolean barra);
	public TipoN tipoNodo() {
		return TipoN.INS;
	}
}
