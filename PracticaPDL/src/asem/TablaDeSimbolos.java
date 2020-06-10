package asem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ast.NodoArbol;


public class TablaDeSimbolos {
	private List<Map<String, NodoArbol>> tabla = new ArrayList<>();
	public void abreBloque() {
		tabla.add(new HashMap<>());
	}
	public void cierraBloque() {
		tabla.remove(tabla.size()-1);
	}
	
	public boolean insertaId(String iden, NodoArbol instruccion) {
		boolean insertable = !tabla.get(tabla.size()-1).containsKey(iden);
		if(insertable) {
			tabla.get(tabla.size()-1).put(iden, instruccion);
		}
		else {
			System.err.println("Warning: " + "Fila " + instruccion.getFila() + " Columna: " + instruccion.getColumna() + ".\nLa variable "+ iden + " ya estaba declarada, se ha tomado su primera declaración\n");
		}
		return insertable;
	}
	public NodoArbol declaracionDe(String iden) {
		for(int i = tabla.size()-1; i >= 0; --i) {
			if(tabla.get(i).containsKey(iden)) return tabla.get(i).get(iden);
		}
		return null;
	}
}
