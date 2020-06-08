package ast;

public abstract class NodoArbol {
	protected int fila, columna;
	public abstract TipoN tipoNodo();
	public abstract String imprime(String prev, boolean barra);
	public int getFila() {
		return fila;
	}
	public int getColumna() {
		return columna;
	}
	
}
