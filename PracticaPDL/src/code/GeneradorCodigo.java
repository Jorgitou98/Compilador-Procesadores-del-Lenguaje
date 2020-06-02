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
	private List<Bloque> listaBloques = new ArrayList<>();
	private int nextDir = 5;
	private File archivo = new File("codigo.txt");
	private PrintWriter pw;
	private int nivelAmbito = 0;
	private List<InsMaquina> codigo = new ArrayList<>();
	private Bloque bloqueAct = null;
	private int maxAmbitos = 0;

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
				((Iden) insDec.getVar()).setPa(bloqueAct.getPa() + 1);
				if (insDec.getTipo().tipo() == TipoT.USUARIO) {
					int tamano = queTamano(((TipoUsuario) insDec.getTipo()).getNombreTipo());
					insertaId(((Iden) insDec.getVar()).id(), tamano);
				} else {
					insertaId(((Iden) insDec.getVar()).id(), 1);
				}
				break;
			case INSENUM:
				break;
			case INSFOR:
				abreAmbito();
				InsFor insFor = (InsFor) ins;
				Ins decIni = insFor.getDecIni();
				declaraciones(decIni);
				for (Ins instFor : insFor.getInst().getInstr()) {
					declaraciones(instFor);
				}
				cierraAmbito();
				break;
			case INSFUN:
				abreAmbito(true);
				InsFun insFun = (InsFun) ins;
				insFun.setPa(bloqueAct.getPa());
				for (Param p : insFun.getParametros())
					declaraciones(p);
				for (Ins i : insFun.getInstr().getInstr())
					declaraciones(i);
				cierraAmbito();
				break;
			case INSPROC:
				abreAmbito(true);
				InsProc insProc = (InsProc) ins;
				insProc.setPa(bloqueAct.getPa());
				for (Param p : insProc.getParametros())
					declaraciones(p);
				for (Ins i : insProc.getInstr().getInstr())
					declaraciones(i);
				cierraAmbito();
				break;
			case INSSTRUCT:
				InsStruct insStruct = (InsStruct) ins;
				int tamTipo = 0;
				for (Ins i : insStruct.getDeclaraciones()) {
					InsDec decStruct = (InsDec) i;
					if (decStruct.getTipo().tipo() == TipoT.USUARIO)
						tamTipo += queTamano(((TipoUsuario) decStruct.getTipo()).getNombreTipo());
					else
						tamTipo++;
				}
				insertaTipo(((Iden) insStruct.getNombreTipo()).id(), tamTipo);
				break;
			case INSSWITCH:
				InsSwitch insSwitch = (InsSwitch) ins;
				for (Case casoSwitch : insSwitch.getListaCase())
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
			((Iden) param.getIden()).setPa(bloqueAct.getPa() + 1);
			if (param.getTipo().tipo() == TipoT.USUARIO) {
				int tamano = queTamano(((TipoUsuario) param.getTipo()).getNombreTipo());
				insertaId(((Iden) param.getIden()).id(), tamano);
			} else {
				insertaId(((Iden) param.getIden()).id(), 1);
			}
			break;
		case PROG:
			P prog = (P) nodo;
			abreAmbito();
			for (Ins insP : prog.getInstr())
				declaraciones(insP);
			cierraAmbito();
			break;
		default:
			break;

		}
	}

	private void declaraciones(P nodo, boolean ini) {
		P prog = (P) nodo;
		abreAmbito(ini);
		for (Ins insP : prog.getInstr())
			declaraciones(insP);
		cierraAmbito();
	}

	private void generaCodigoExp(E exp) {
		switch (exp.tipo()) {
		case ACCESOPUNTERO:
			break;
		case AND:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("and", -1);
			break;
		case CARACTER:
			// No esta en la maquina P
			break;
		case CORCHETES:
			break;
		case DISTINTO:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("neq", -1);
			break;
		case DIVENT:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("div", -1);
			break;
		case DIVREAL:
			// No esta en la maquina P
			break;
		case FALSE:
			insertIns("ldc false", 1);
			break;
		case FLOAT:
			// No esta en la maquina P
			break;
		case IDEN:
			generaCodigoL(exp);
			insertIns("ind", 0);
			break;
		case IGUALIGUAL:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("equ", -1);
			break;
		case INT:
			insertIns("ldc " + ((Num) exp).num(), 1);
			break;
		case LLAMADAFUN:
			LlamadaFun llamadaFun = (LlamadaFun) exp;
			int l = ((InsFun) llamadaFun.getRef()).getDirIni();
			int s = ((InsFun) llamadaFun.getRef()).getTamParams();
			int n = ((InsFun) llamadaFun.getRef()).getPa();
			int m = bloqueActGenera().getPa() + 1;
			insertIns("mst " + (m - n), 5);
			generaCodigoA(((InsFun) llamadaFun.getRef()).getParametros(), llamadaFun.getArgumentos());
			insertIns("cup " + s + " " + l, 0);
			break;
		case MAYOR:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("grt", -1);
			break;
		case MAYORIGUAL:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("geq", -1);
			break;
		case MENOR:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("les", -1);
			break;
		case MENORIGUAL:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("leq", -1);
			break;
		case MODULO:
			// pw.println("\\\\ Esto es un modulo");
			generaCodigoExp(new Resta(exp.opnd1(),
					new Mul(exp.opnd2(), new DivEnt(exp.opnd1(), exp.opnd2(), false), false), false));
			break;
		case MUL:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("mul", -1);
			break;
		case NOT:
			generaCodigoExp(exp.opnd1());
			insertIns("not", 0);
			break;
		case NULL:
			break;
		case OR:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("or", -1);
			break;
		case PUNTO:
			break;
		case RESTA:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("sub", -1);
			break;
		case RESTAUNARIA:
			generaCodigoExp(exp.opnd1());
			insertIns("neg", 0);
			break;
		case SIZE:
			break;
		case SUMA:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("add", -1);
			break;
		case SUMAUNARIA:
			generaCodigoExp(exp.opnd1());
			break;
		case TRUE:
			insertIns("ldc true", 1);
			break;
		case VECTOR:
			break;
		default:
			break;
		}
	}

	private void generaCodigoL(E exp) {
		if (exp.tipo() == TipoE.IDEN) {
			Iden id;
			if (((Iden) exp).getRef().tipoNodo() == TipoN.INS) {
				id = ((Iden) ((InsDec) ((Iden) exp).getRef()).getVar());
			}
			else {
				id = ((Iden) ((Param) ((Iden) exp).getRef()).getIden());
			}
			insertIns("lda " + (bloqueActGenera().getPa() + 1 - id.getPa()) + " " + bloqueActGenera().dirVar(((Iden) exp).id()), 1);
			System.out.println((bloqueActGenera().getPa() + 1 + " " + id.getPa()));
		}

	}

	private void generaCodigoL1(E exp) {
		if (exp.tipo() == TipoE.IDEN) {
			insertIns("lda " + 0 + " " + bloqueActGenera().dirVar(((Iden) exp).id()), 1);
		}
	}

	private void generaCodigoIns(Ins ins) {
		switch (ins.tipo()) {
		case INSASIG:
			// pw.println("\\\\ Esto es una asignación");
			InsAsig insAsig = (InsAsig) ins;
			generaCodigoL(insAsig.getVar());
			generaCodigoExp(insAsig.getValor());
			insertIns("sto", -2);
			break;
		case INSCALL:
			InsCall insCall = (InsCall) ins;
			int l = ((InsProc) insCall.getRef()).getDirIni();
			int s = ((InsProc) insCall.getRef()).getTamParams();
			int n = ((InsProc) insCall.getRef()).getPa();
			int m = bloqueActGenera().getPa() + 1;
			insertIns("mst " + (m - n), 5);
			generaCodigoA(((InsProc) insCall.getRef()).getParametros(), insCall.getArgumentos());
			insertIns("cup " + s + " " + l, 0);

			break;
		case INSCOND:
			InsCond insCond = (InsCond) ins;
			generaCodigoExp(insCond.getCondicion());
			maxAmbitos++;
			nivelAmbito = maxAmbitos;
			int posSalto1 = codigo.size();
			insertIns("fjp ", -1);
			generaCodigoProg(insCond.getInsIf());
			nivelAmbito = bloqueActGenera().getPadre().getPosLista();
			if (insCond.isTieneElse()) {
				maxAmbitos++;
				nivelAmbito = maxAmbitos;
				int posSalto2 = codigo.size();
				insertIns("ujp ", 0);
				codigo.get(posSalto1).setName(codigo.get(posSalto1).getName() + codigo.size());
				generaCodigoProg(insCond.getInsElse());
				codigo.get(posSalto2).setName(codigo.get(posSalto2).getName() + codigo.size());
				nivelAmbito = bloqueActGenera().getPadre().getPosLista();
			} else
				codigo.get(posSalto1).setName(codigo.get(posSalto1).getName() + codigo.size());
			break;
		case INSDEC:
			InsDec insDec = (InsDec) ins;
			if (insDec.isConValorInicial()) {
				generaCodigoL1(insDec.getVar());
				generaCodigoExp(insDec.getValorInicial());
				insertIns("sto", -2);
				break;
			}
			break;
		case INSENUM:
			break;
		case INSFOR:
			maxAmbitos++;
			nivelAmbito = maxAmbitos;
			InsFor insFor = (InsFor) ins;
			generaCodigoIns(insFor.getDecIni());
			int posCond = codigo.size();
			generaCodigoExp(insFor.getCond());
			int posFjpFor = codigo.size();
			insertIns("fjp ", -1);
			generaCodigoProg(insFor.getInst());
			Iden var;
			if (insFor.getDecIni().tipo() == TipoIns.INSDEC) {
				var = (Iden) ((InsDec) insFor.getDecIni()).getVar();
			} else
				var = (Iden) ((InsAsig) insFor.getDecIni()).getVar();
			generaCodigoIns(new InsAsig(var, insFor.getPaso()));
			insertIns("ujp " + posCond, 0);
			codigo.get(posFjpFor).setName(codigo.get(posFjpFor).getName() + codigo.size());
			nivelAmbito = bloqueActGenera().getPadre().getPosLista();
			break;
		case INSFUN:
			InsFun insFun = (InsFun) ins;
			maxAmbitos++;
			nivelAmbito = maxAmbitos;
			insFun.setDirIni(codigo.size());
			for (Param p : insFun.getParametros()) {
				if (p.getTipo().tipo() == TipoT.USUARIO) {
					insFun.incTamParams(bloqueActGenera().queTamano(((TipoUsuario) p.getTipo()).getNombreTipo()));
				} else {
					insFun.incTamParams(1);
				}
			}
			insertIns("ssp " + bloqueActGenera().getSsp(), 0);
			int posSepFun = codigo.size();
			insertIns("sep ", 0);
			generaCodigoProg(insFun.getInstr());
			insertIns("lda 0 0", 1);
			generaCodigoExp(insFun.getValorReturn());
			insertIns("sto", -2);
			int tamPilaFun = tamPilaEvaluacion(posSepFun);
			codigo.get(posSepFun).setName(codigo.get(posSepFun).getName() + tamPilaFun);
			insertIns("retf", 0);
			nivelAmbito = bloqueActGenera().getPadre().getPosLista();
			break;
		case INSNEW:
			break;
		case INSPROC:
			InsProc insProc = (InsProc) ins;
			maxAmbitos++;
			nivelAmbito = maxAmbitos;
			insProc.setDirIni(codigo.size());
			for (Param p : insProc.getParametros()) {
				if (p.getTipo().tipo() == TipoT.USUARIO) {
					insProc.incTamParams(bloqueActGenera().queTamano(((TipoUsuario) p.getTipo()).getNombreTipo()));
				} else {
					insProc.incTamParams(1);
				}
			}
			insertIns("ssp " + bloqueActGenera().getSsp(), 0);
			int posSep = codigo.size();
			insertIns("sep ", 0);
			generaCodigoProg(insProc.getInstr());
			int tamPila = tamPilaEvaluacion(posSep);
			codigo.get(posSep).setName(codigo.get(posSep).getName() + tamPila);
			insertIns("retp", 0);
			nivelAmbito = bloqueActGenera().getPadre().getPosLista();
			break;
		case INSSTRUCT:
			break;
		case INSSWITCH:
			
			break;
		case INSTYPEDEF:
			break;
		case INSWHILE:
			InsWhile insWhile = (InsWhile) ins;
			maxAmbitos++;
			nivelAmbito = maxAmbitos;
			int posSaltoW1 = codigo.size();
			generaCodigoExp(insWhile.getCondicion());
			int posFjp = codigo.size();
			insertIns("fjp ", -1);
			generaCodigoProg(insWhile.getInsWhile());
			insertIns("ujp " + posSaltoW1, 0);
			codigo.get(posFjp).setName(codigo.get(posFjp).getName() + codigo.size());
			nivelAmbito = bloqueActGenera().getPadre().getPosLista();
			break;
		default:
			break;

		}
	}

	private void generaCodigoA(List<Param> params, List<E> args) {
		for (int i = 0; i < params.size(); ++i) {
			if (params.get(i).getTipoDeParam() == TipoParam.VALOR) {
				Tipos tipo = params.get(i).getTipo();
				if (tipo.tipo() == TipoT.USUARIO) {
					generaCodigoL(args.get(i));
					insertIns("movs " + bloqueActGenera().queTamano(((TipoUsuario) tipo).getNombreTipo()),
							bloqueActGenera().queTamano(((TipoUsuario) tipo).getNombreTipo()) - 1);
				} else {
					generaCodigoExp(args.get(i));
				}
			} else {
				generaCodigoL(args.get(i));
			}
		}
	}

	private void generaCodigoProg(P prog) {
		for (Ins ins : prog.getInstr()) {
			if (ins.tipo() == TipoIns.INSPROC || ins.tipo() == TipoIns.INSFUN) {
				int posUjp = codigo.size();
				insertIns("ujp ", 0);
				generaCodigoIns(ins);
				codigo.get(posUjp).setName(codigo.get(posUjp).getName() + codigo.size());
			} else
				generaCodigoIns(ins);
		}
	}

	public void generaCodigo(NodoArbol raiz) {
		try {
			if (archivo.createNewFile()) {

			}

			pw = new PrintWriter(archivo);
			declaraciones((P) raiz, true);
			insertIns("ssp " + listaBloques.get(0).getSsp(), 0);
			insertIns("sep ", 0);
			generaCodigoProg((P) raiz);
			int tamPila = tamPilaEvaluacion(1);
			codigo.get(1).setName(codigo.get(1).getName() + tamPila);
			insertIns("stp", 0);
			write();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void abreAmbito(boolean funProc) {
		Bloque b = new Bloque(funProc, bloqueAct, listaBloques.size());
		listaBloques.add(b);
		bloqueAct = b;
	}

	private void abreAmbito() {
		Bloque b = new Bloque(bloqueAct, listaBloques.size());
		listaBloques.add(b);
		bloqueAct = b;
	}

	public void insertaId(String iden, int tam) {
		bloqueAct.insertaId(iden, tam);
		nextDir += tam;
		bloqueAct.setSsp(bloqueAct.getSsp() + tam);
	}

	private void cierraAmbito() {
		nextDir -= bloqueAct.getTamBloque();
		if (!bloqueAct.isFunProc()) {
			bloqueAct.getPadre().setSsp(Math.max(bloqueAct.getPadre().getSsp(), nextDir));
		}
		bloqueAct = bloqueAct.getPadre();

	}

	private void insertaTipo(String iden, int tam) {
		bloqueAct.insertaTipo(iden, tam);
	}

	public int queTamano(String tipo) {
		return bloqueAct.queTamano(tipo);
	}

	private void write() {
		for (int i = 0; i < codigo.size(); ++i) {
			pw.println("{" + i + "}" + codigo.get(i).getName() + ";");
		}
	}

	private void insertIns(String ins, int tipo) {
		codigo.add(new InsMaquina(ins, tipo));
	}

	private Bloque bloqueActGenera() {
		return listaBloques.get(nivelAmbito);
	}

	private int tamPilaEvaluacion(int ini) {
		int tam = 0;
		int max = 0;
		int cuenta = 0;
		for (int i = ini; i < codigo.size(); i++) {
			if (cuenta == 0) {
				if (codigo.get(i).getName().substring(0, 3).equals("ssp")) {
					cuenta++;
				} else {
					tam += codigo.get(i).getTipo();
				}
			} else {
				if (codigo.get(i).getName().length() > 3 && (codigo.get(i).getName().substring(0, 4).equals("retp")
						|| codigo.get(i).getName().substring(0, 4).equals("retf"))) {
					cuenta--;
				}
			}
			max = Math.max(max, tam);
		}
		return max;
	}
}
