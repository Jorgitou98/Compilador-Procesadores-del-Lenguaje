package code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bloque {
	private Map<String, Integer> identificadores = new HashMap<>();
	private Map<String, Integer> tamanosTipos = new HashMap<>();
	private Map<String, List<Integer>> dimensionesVect = new HashMap<>();
	private Map<String, Boolean> tablaEsEstatico = new HashMap<>();
	private int tamBloque = 0;
	private int ssp = 5;
	private boolean funProc = false;
	private Bloque padre;
	private int posLista;
	private int pa = 0;
	private int nextDir = 5;

	public Bloque(Bloque padre, int posLista) {
		super();
		this.padre = padre;
		this.posLista = posLista;
		if (padre != null) {
			pa = padre.getPa();
			nextDir = padre.getNextDir();
		}
	}

	public Bloque(boolean funProc, Bloque padre, int posLista) {
		super();
		this.funProc = funProc;
		this.padre = padre;
		this.posLista = posLista;
		this.ssp = 5;
		if (padre != null) {
			pa = padre.getPa() + 1;
			if (!funProc) {
				nextDir = padre.getNextDir();
				pa = padre.getPa();
			}
		}
	}

	public int getTamBloque() {
		return tamBloque;
	}

	public Bloque getPadre() {
		return padre;
	}

	public void insertaId(String id, int tam) {
		identificadores.put(id, nextDir);
		tamBloque += tam;
		nextDir += tam;
		ssp += tam;

	}

	public void insertaTipo(String tipo, int tam) {
		tamanosTipos.put(tipo, tam);
	}

	public int dirVar(String id) {
		if (identificadores.containsKey(id))
			return identificadores.get(id);
		if(padre != null) {
			return padre.dirVar(id);
		}
		return -1;
	}

	public int queTamano(String tipo) {
		if (tamanosTipos.containsKey(tipo))
			return tamanosTipos.get(tipo);
		if(padre!=null) {
			return padre.queTamano(tipo);
		}
		return 1;
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

	public int getPa() {
		return pa;
	}

	public int getNextDir() {
		return nextDir;
	}

	public void setNextDir(int nextDir) {
		this.nextDir = nextDir;
	}
	
	public void insertaCampoStruct(String id, int dirRel) {
		identificadores.put(id, dirRel);
	}
	public void insertaDimensiones(String id, List<Integer> dims) {
		dimensionesVect.put(id, dims);
	}
	public List<Integer> dimensionVect(String id){
		if(dimensionesVect.containsKey(id)) {
			return dimensionesVect.get(id);
		}
		else if(padre!= null)
			return padre.dimensionVect(id);
		return null;
	}
	
	public boolean esEstatico(String id) {
		if(tablaEsEstatico.containsKey(id)) {
			return tablaEsEstatico.get(id);
		}
		else if(padre!= null)
			return padre.esEstatico(id);
		return true;
	}
	
	public void insertaEstatico(String id, boolean estatico) {
		tablaEsEstatico.put(id, estatico);
	}

}
