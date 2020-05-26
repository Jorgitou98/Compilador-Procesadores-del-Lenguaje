package code;

import java.util.HashMap;
import java.util.Map;

public class Bloque {
	private Map<String, Integer> identificadores = new HashMap<>();
	private Map<String, Integer> tamanosTipos = new HashMap<>();
	private int tamBloque = 0;
	private int ssp = 0;
	private boolean funProc = false;
	private Bloque padre;
	private int posLista;
	public Bloque(Bloque padre, int posLista) {
		super();
		this.padre = padre;
		this.posLista = posLista;
	}
	
	
	public Bloque(boolean funProc, Bloque padre, int posLista) {
		super();
		this.funProc = funProc;
		this.padre = padre;
		this.posLista = posLista;
		this.ssp = 5;
	}


	public int getTamBloque() {
		return tamBloque;
	}
	public Bloque getPadre() {
		return padre;
	}
	
	public void insertaId(String id, int dir, int tam) {
		identificadores.put(id, dir);
		tamBloque+= tam;
		
	}
	public void insertaTipo(String tipo, int tam) {
		tamanosTipos.put(tipo, tam);
	}
	
	public int dirVar(String id) {
		if(identificadores.containsKey(id)) return identificadores.get(id);
		return padre.dirVar(id);
	}
	
	public int queTamano(String tipo) {
		if(tamanosTipos.containsKey(tipo)) return tamanosTipos.get(tipo);
		return padre.queTamano(tipo);
	}

	public int getPosLista() {
		return posLista;
	}


	public int getSsp() {
		return ssp;
	}


	public void setSsp(int ssp) {
		this.ssp = ssp;
	}


	public boolean isFunProc() {
		return funProc;
	}
	
	
	
	
	
}
