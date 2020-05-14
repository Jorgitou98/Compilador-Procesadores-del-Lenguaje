package ast;

public abstract class Tipos extends NodoArbol{
	public abstract String imprime(String prev, boolean barra);
	public TipoN tipoNodo() {
			return TipoN.TIPOS;
	}
	public abstract TipoT tipo();
}