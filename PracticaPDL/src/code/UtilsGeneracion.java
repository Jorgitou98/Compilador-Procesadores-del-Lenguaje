package code;

import java.util.ArrayList;
import java.util.List;

import ast.Case;
import ast.E;
import ast.Iden;
import ast.IgualIgual;
import ast.InsCond;
import ast.Num;
import ast.P;
import ast.TipoE;
import ast.TipoT;
import ast.TipoUsuario;
import ast.TipoVector;
import ast.Tipos;
import ast.Vector;

public class UtilsGeneracion {

	private AsignadorDirecciones asignaDirs;

	UtilsGeneracion(AsignadorDirecciones asignaDirs){
		this.asignaDirs = asignaDirs;
	}
	
	protected void abreAmbito(boolean funProc) {
		Bloque b = new Bloque(funProc, asignaDirs.getBloqueAct(), AsignadorDirecciones.listaBloques.size());
		AsignadorDirecciones.listaBloques.add(b);
		asignaDirs.setBloqueAct(b);
	}

	protected void abreAmbito() {
		Bloque b = new Bloque(asignaDirs.getBloqueAct(), AsignadorDirecciones.listaBloques.size());
		AsignadorDirecciones.listaBloques.add(b);
		asignaDirs.setBloqueAct(b);
		//b.setNextDir(b.getPadre().getNextDir());
	}

	protected void insertaId(String iden, int tam) {
		asignaDirs.getBloqueAct().insertaId(iden, tam);
		//asignaDirs.setNextDir(asignaDirs.getNextDir() + tam);
		//asignaDirs.getBloqueAct().setSsp(asignaDirs.getBloqueAct().getSsp() + tam);
	}

	protected void cierraAmbito() {
		if (!asignaDirs.getBloqueAct().isFunProc()) {
			asignaDirs.getBloqueAct().getPadre().setSsp(Math.max(asignaDirs.getBloqueAct().getPadre().getSsp(), asignaDirs.getBloqueAct().getNextDir()));
			asignaDirs.getBloqueAct().getPadre().setNextDir(asignaDirs.getBloqueAct().getNextDir());
		}
		else{
			//asignaDirs.setNextDir(asignaDirs.getNextDir() - asignaDirs.getBloqueAct().getTamBloque());
		}
		asignaDirs.setBloqueAct(asignaDirs.getBloqueAct().getPadre());

	}

	protected void insertaTipo(String iden, int tam) {
		asignaDirs.getBloqueAct().insertaTipo(iden, tam);
	}

	protected int queTamano(String tipo) {
		return asignaDirs.getBloqueAct().queTamano(tipo);
	}
	
	protected int calculaTamVector(Tipos tipo, E v) {
		if (tipo.tipo() == TipoT.VECTOR) {
			return Integer.parseInt(((Num) ((Vector) v).getTam()).num())
					* calculaTamVector(((TipoVector) tipo).getTipoVector(), ((Vector) v).getValorIni());
		} else if (tipo.tipo() == TipoT.USUARIO) {
			return queTamano(((TipoUsuario) tipo).getNombreTipo());
		} else
			return 1;
	}
	
	protected int calculaTamVectorDinamico(Tipos tipo) {
		int tam = 2; 
		while (tipo.tipo() == TipoT.VECTOR) {
			tam++;
			tipo = ((TipoVector) tipo).getTipoVector();
		}
		return tam;
	}
	
	protected List<Integer> dimensionesVector(Tipos tipo, E exp) {
		if (exp.tipo() != TipoE.VECTOR) {
			List<Integer> lista = new ArrayList<>();
			if (tipo.tipo() == TipoT.USUARIO) {
				lista.add(queTamano(((TipoUsuario) tipo).getNombreTipo()));
			} else
				lista.add(1);
			return lista;
		} else {
			List<Integer> lista = dimensionesVector(((TipoVector) tipo).getTipoVector(), ((Vector) exp).getValorIni());
			lista.add(0, Integer.parseInt(((Num) ((Vector) exp).getTam()).num()));
			return lista;
		}
	}
	
	protected boolean esVectorEstatico(Tipos tipo, E exp) {
		while(exp.tipo() == TipoE.VECTOR) {
			if(((Vector) exp).getTam().tipo() != TipoE.INT) {
				return false;
			}
			else exp = ((Vector) exp).getValorIni();
		}
		return true;
	}
	
	protected Tipos tipoFinal(Tipos tipo) {
		if (tipo.tipo() == TipoT.VECTOR) {
			return tipoFinal(((TipoVector) tipo).getTipoVector());
		} else
			return tipo;
	}

	protected int tipoFinalSize(Tipos tipo) {
		if (tipo.tipo() == TipoT.USUARIO) {
			return GeneradorCodigo.bloqueActGenera().queTamano(((TipoUsuario) tipo).getNombreTipo());
		} else
			return 1;
	}

	protected String stringPuntos(E exp) {
		if (exp.tipo() == TipoE.PUNTO) {
			return stringPuntos(exp.opnd1()) + "." + ((Iden) exp.opnd2()).id();
		} else
			return ((Iden) exp).id();
	}
	
	protected P switchACond(E var, List<Case> lista, int i) {
		if(i == lista.size()-1) {
			return lista.get(i).getInstr();
		}
		else {
			InsCond cond = new InsCond(new IgualIgual(var, lista.get(i).getNombreCase(), false, 0, 0), lista.get(i).getInstr(), switchACond(var, lista, i+1), 0, 0);
			P prog = new P();
			prog.anadeIns(cond);
			return prog;
		}
	}
	
	
}
