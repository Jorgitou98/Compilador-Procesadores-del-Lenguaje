package code;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ast.*;
import errors.GestionErroresTiny;

public class GeneradorCodigo {
	private List<Pair<Map<String, Integer>, Integer>> bloquesMem = new ArrayList<>();
	private List<Map<String, Integer>> tamanosTipos = new ArrayList<>();
	private int nextDir = 5;
	private File archivo = new File("codigo.txt");
	private PrintWriter pw;
	private int nlinea = 0;
	private int nivelAmbito = 0;

	private void declaraciones(NodoArbol nodo) {
		switch (nodo.tipoNodo()) {
		case CASE:
			Case caso = (Case) nodo;
			declaraciones(caso.getInstr());
			break;
		case INS:
			Ins ins = (Ins) nodo;
			switch (ins.tipo()) {
			case INSCOND:
				InsCond insCond = (InsCond) ins;
				declaraciones(insCond.getInsIf());
				if (insCond.isTieneElse()) {
					declaraciones(insCond.getInsElse());
				}
				break;
			case INSDEC:
				InsDec insDec = (InsDec) ins;
				
				if (insDec.getTipo().tipo() == TipoT.USUARIO) {
					int tamano = tamanosTipos.get(tamanosTipos.size() - 1)
							.get(((TipoUsuario) insDec.getTipo()).getNombreTipo());
					insertaId(((Iden) insDec.getVar()).id(), nextDir, tamano);
				} else {
					insertaId(((Iden) insDec.getVar()).id(), nextDir, 1);
				}
				break;
			case INSENUM:
				break;
			case INSFOR:
				abreAmbito();
				InsFor insFor = (InsFor) ins;
				Ins decIni = insFor.getDecIni();
				declaraciones(decIni);
				for(Ins instFor : insFor.getInst().getInstr()) {
					declaraciones(instFor);
				}
				cierraAmbito();
				break;
			case INSFUN:
				abreAmbito();
				InsFun insFun = (InsFun) ins;
				for(Param p :insFun.getParametros())
					declaraciones(p);
				for(Ins i: insFun.getInstr().getInstr())
					declaraciones(i);
				cierraAmbito();
				break;
			case INSPROC:
				abreAmbito();
				InsProc insProc = (InsProc) ins;
				for(Param p :insProc.getParametros())
					declaraciones(p);
				for(Ins i: insProc.getInstr().getInstr())
					declaraciones(i);
				cierraAmbito();
				break;
			case INSSTRUCT:
				InsStruct insStruct = (InsStruct) ins;
				int tamTipo = 0;
				for (Ins i: insStruct.getDeclaraciones()) {
					InsDec decStruct = (InsDec) i;
					if(decStruct.getTipo().tipo() == TipoT.USUARIO)
						tamTipo+= queTamano(((TipoUsuario) decStruct.getTipo()).getNombreTipo());
					else tamTipo++;
				}
				insertaTipo(((Iden)insStruct.getNombreTipo()).id(), tamTipo);
				break;
			case INSSWITCH:
				InsSwitch insSwitch = (InsSwitch) ins;
				for(Case casoSwitch: insSwitch.getListaCase())
					declaraciones(casoSwitch);
				break;
			case INSWHILE:
				InsWhile insWhile = (InsWhile) ins;
				declaraciones(insWhile.getInsWhile());
				break;
			default:
				break;

			}
			break;
		case PARAM:
			Param param = (Param) nodo;
			if (param.getTipo().tipo() == TipoT.USUARIO) {
				int tamano = tamanosTipos.get(tamanosTipos.size() - 1)
						.get(((TipoUsuario) param.getTipo()).getNombreTipo());
				insertaId(((Iden) param.getIden()).id(), nextDir, tamano);
			} else {
				insertaId(((Iden) param.getIden()).id(), nextDir, 1);
			}
			break;
		case PROG:
			P prog = (P) nodo;
			abreAmbito();
			for(Ins insP: prog.getInstr())
				declaraciones(insP);
			cierraAmbito();
			break;
		default:
			break;

		}
	}
	private void generaCodigoExp(E exp){
		switch(exp.tipo()) {
		case ACCESOPUNTERO:
			break;
		case AND:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			writeIns("and");
			break;
		case CARACTER:
			//No esta en la maquina P
			break;
		case CORCHETES:
			break;
		case DISTINTO:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			writeIns("neq");
			break;
		case DIVENT:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			writeIns("div");
			break;
		case DIVREAL:
			//No esta en la maquina P
			break;
		case FALSE:
			writeIns("ldc false");
			break;
		case FLOAT:
			// No esta en la maquina P
			break;
		case IDEN:
			writeIns("ldc " +  dirVar(((Iden) exp).id()));
			writeIns("ind");
			break;
		case IGUALIGUAL:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			writeIns("equ");
			break;
		case INT:
			writeIns("ldc "+ ((Num) exp).num());
			break;
		case LLAMADAFUN:
			break;
		case MAYOR:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			writeIns("grt");
			break;
		case MAYORIGUAL:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			writeIns("geq");
			break;
		case MENOR:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			writeIns("les");
			break;
		case MENORIGUAL:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			writeIns("leq");
			break;
		case MODULO:
			pw.println("\\\\ Esto es un modulo");
			generaCodigoExp(new Resta(exp.opnd1(), new Mul(exp.opnd2(), new DivEnt(exp.opnd1(), exp.opnd2(), false), false), false));
			break;
		case MUL:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			writeIns("mul");
			break;
		case NOT:
			generaCodigoExp(exp.opnd1());
			writeIns("not");
			break;
		case NULL:
			break;
		case OR:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			writeIns("or");
			break;
		case PUNTO:
			break;
		case RESTA:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			writeIns("sub");
			break;
		case RESTAUNARIA:
			generaCodigoExp(exp.opnd1());
			writeIns("neg");
			break;
		case SIZE:
			break;
		case SUMA:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			writeIns("add");
			break;
		case SUMAUNARIA:
			generaCodigoExp(exp.opnd1());
			break;
		case TRUE:
			writeIns("ldc true");
			break;
		case VECTOR:
			break;
		default:
			break;
		}
	}
	
	private void generaCodigoL(E exp) {
		if(exp.tipo() == TipoE.IDEN) {
			writeIns("ldc " + dirVar(((Iden) exp).id()));
		}
	}
	private void generaCodigoIns(Ins ins) {
		switch(ins.tipo()) {
		case INSASIG:
			pw.println("\\\\ Esto es una asignación");
			InsAsig insAsig = (InsAsig) ins;
			generaCodigoL(insAsig.getVar());
			generaCodigoExp(insAsig.getValor());
			writeIns("sto");
			break;
		case INSCALL:
			break;
		case INSCOND:
			break;
		case INSDEC:
			InsDec insDec = (InsDec) ins;
			if(insDec.isConValorInicial()) {
				generaCodigoL(insDec.getVar());
				generaCodigoExp(insDec.getValorInicial());
				writeIns("sto");
			}
			break;
		case INSENUM:
			break;
		case INSFOR:
			break;
		case INSFUN:
			break;
		case INSNEW:
			break;
		case INSPROC:
			break;
		case INSSTRUCT:
			break;
		case INSSWITCH:
			break;
		case INSTYPEDEF:
			break;
		case INSWHILE:
			break;
		default:
			break;
			
		}
	}
	
	private void generaCodigoProg(P prog){
		for(Ins ins: prog.getInstr()) {
			generaCodigoIns(ins);
		}
	}
	
	public void generaCodigo(NodoArbol raiz) {
		try {
			if(archivo.createNewFile()) {
				
			}
			
			pw = new PrintWriter(archivo);
			declaraciones(raiz);
			writeIns("ssp 10");
			generaCodigoProg((P)raiz);
			writeIns("stp");
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void abreAmbito() {
		bloquesMem.add(new Pair<>(new HashMap<>(), 0));
		tamanosTipos.add(new HashMap<>());
	}
	public void insertaId(String iden, int dir, int tam) {
		bloquesMem.get(bloquesMem.size()-1).getKey().put(iden, dir);
		bloquesMem.get(bloquesMem.size()-1).setValue(bloquesMem.get(bloquesMem.size()-1).getValue() + tam);
	}
	private void cierraAmbito() {
		nextDir -= bloquesMem.get(bloquesMem.size()-1).getValue();
	}
	private void insertaTipo(String iden, int tam) {
		tamanosTipos.get(tamanosTipos.size()-1).put(iden, tam);
	}
	public int queTamano(String tipo) {
		for(int i = tamanosTipos.size()-1; i >= 0; --i) {
			if(tamanosTipos.get(i).containsKey(tipo)) return tamanosTipos.get(i).get(tipo);
		}
		return -1;
	}
	private void writeIns(String ins) {
		pw.println("{" + nlinea + "} " + ins + ";");
		nlinea++;
	}
	private int dirVar(String nombre) {
		for(int i = nivelAmbito; i >= 0; i--) {
			if(bloquesMem.get(i).getKey().containsKey(nombre))
				return bloquesMem.get(i).getKey().get(nombre);
		}
		return -1;
	}
}
