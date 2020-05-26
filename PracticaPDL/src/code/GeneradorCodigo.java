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
	private int nlinea = 0;
	private int nivelAmbito = 0;
	private int sp_ini = nextDir;
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
				for (Param p : insFun.getParametros())
					declaraciones(p);
				for (Ins i : insFun.getInstr().getInstr())
					declaraciones(i);
				cierraAmbito();
				break;
			case INSPROC:
				abreAmbito(true);
				InsProc insProc = (InsProc) ins;
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
			insertIns("and", TipoInsMaquina.RESTA1);
			break;
		case CARACTER:
			// No esta en la maquina P
			break;
		case CORCHETES:
			break;
		case DISTINTO:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("neq", TipoInsMaquina.RESTA1);
			break;
		case DIVENT:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("div", TipoInsMaquina.RESTA1);
			break;
		case DIVREAL:
			// No esta en la maquina P
			break;
		case FALSE:
			insertIns("ldc false", TipoInsMaquina.SUMA1);
			break;
		case FLOAT:
			// No esta en la maquina P
			break;
		case IDEN:
			insertIns("ldc " + bloqueActGenera().dirVar(((Iden) exp).id()), TipoInsMaquina.SUMA1);
			insertIns("ind", TipoInsMaquina.IGUAL);
			break;
		case IGUALIGUAL:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("equ", TipoInsMaquina.RESTA1);
			break;
		case INT:
			insertIns("ldc " + ((Num) exp).num(), TipoInsMaquina.SUMA1);
			break;
		case LLAMADAFUN:
			break;
		case MAYOR:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("grt", TipoInsMaquina.RESTA1);
			break;
		case MAYORIGUAL:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("geq", TipoInsMaquina.RESTA1);
			break;
		case MENOR:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("les", TipoInsMaquina.RESTA1);
			break;
		case MENORIGUAL:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("leq", TipoInsMaquina.RESTA1);
			break;
		case MODULO:
			// pw.println("\\\\ Esto es un modulo");
			generaCodigoExp(new Resta(exp.opnd1(),
					new Mul(exp.opnd2(), new DivEnt(exp.opnd1(), exp.opnd2(), false), false), false));
			break;
		case MUL:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("mul", TipoInsMaquina.RESTA1);
			break;
		case NOT:
			generaCodigoExp(exp.opnd1());
			insertIns("not", TipoInsMaquina.IGUAL);
			break;
		case NULL:
			break;
		case OR:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("or", TipoInsMaquina.RESTA1);
			break;
		case PUNTO:
			break;
		case RESTA:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("sub", TipoInsMaquina.RESTA1);
			break;
		case RESTAUNARIA:
			generaCodigoExp(exp.opnd1());
			insertIns("neg", TipoInsMaquina.IGUAL);
			break;
		case SIZE:
			break;
		case SUMA:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("add", TipoInsMaquina.RESTA1);
			break;
		case SUMAUNARIA:
			generaCodigoExp(exp.opnd1());
			break;
		case TRUE:
			insertIns("ldc true", TipoInsMaquina.SUMA1);
			break;
		case VECTOR:
			break;
		default:
			break;
		}
	}

	private int generaCodigoL(E exp) {
		if (exp.tipo() == TipoE.IDEN) {
			insertIns("ldc " + bloqueActGenera().dirVar(((Iden) exp).id()), TipoInsMaquina.SUMA1);
			return 1;
		}
		return 0;
	}

	private void generaCodigoIns(Ins ins) {
		switch (ins.tipo()) {
		case INSASIG:
			// pw.println("\\\\ Esto es una asignación");
			InsAsig insAsig = (InsAsig) ins;
			generaCodigoL(insAsig.getVar());
			generaCodigoExp(insAsig.getValor());
			insertIns("sto", TipoInsMaquina.RESTA2);
		case INSCALL:
			break;
		case INSCOND:
			InsCond insCond = (InsCond) ins;
			generaCodigoExp(insCond.getCondicion());
			maxAmbitos++;
			nivelAmbito = maxAmbitos;
			int posSalto1 = codigo.size();
			insertIns("fjp ", TipoInsMaquina.RESTA1);
			generaCodigoProg(insCond.getInsIf());
			nivelAmbito = bloqueActGenera().getPadre().getPosLista();
			if (insCond.isTieneElse()) {
				maxAmbitos++;
				nivelAmbito = maxAmbitos;
				int posSalto2 = codigo.size();
				insertIns("ujp ", TipoInsMaquina.IGUAL);
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
				generaCodigoL(insDec.getVar());
				generaCodigoExp(insDec.getValorInicial());
				insertIns("sto", TipoInsMaquina.RESTA2);
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
			insertIns("fjp ", TipoInsMaquina.RESTA1);
			generaCodigoProg(insFor.getInst());
			Iden var;
			if (insFor.getDecIni().tipo() == TipoIns.INSDEC) {
				var = (Iden) ((InsDec) insFor.getDecIni()).getVar();
			} else
				var = (Iden) ((InsAsig) insFor.getDecIni()).getVar();
			generaCodigoIns(new InsAsig(var, insFor.getPaso()));
			insertIns("ujp " + posCond, TipoInsMaquina.IGUAL);
			codigo.get(posFjpFor).setName(codigo.get(posFjpFor).getName() + codigo.size());
			nivelAmbito = bloqueActGenera().getPadre().getPosLista();
			break;
		case INSFUN:
			InsFun insFun = (InsFun) ins;
			maxAmbitos++;
			nivelAmbito = maxAmbitos;
			insertIns("ssp " + bloqueActGenera().getSsp(), TipoInsMaquina.IGUAL);
			int posSepFun = codigo.size();
			insertIns("sep ", TipoInsMaquina.IGUAL);
			generaCodigoProg(insFun.getInstr());
			insertIns("lod 0 0", TipoInsMaquina.SUMA1);
			generaCodigoExp(insFun.getValorReturn());
			insertIns("sto", TipoInsMaquina.RESTA2);
			int tamPilaFun = tamPilaEvaluacion(posSepFun);
			codigo.get(posSepFun).setName(codigo.get(posSepFun).getName() + tamPilaFun);
			insertIns("retf", TipoInsMaquina.IGUAL);
			nivelAmbito = bloqueActGenera().getPadre().getPosLista();
			break;
		case INSNEW:
			break;
		case INSPROC:
			InsProc insProc = (InsProc) ins;
			maxAmbitos++;
			nivelAmbito = maxAmbitos;
			insertIns("ssp " + bloqueActGenera().getSsp(), TipoInsMaquina.IGUAL);
			int posSep = codigo.size();
			insertIns("sep ", TipoInsMaquina.IGUAL);
			generaCodigoProg(insProc.getInstr());
			int tamPila = tamPilaEvaluacion(posSep);
			codigo.get(posSep).setName(codigo.get(posSep).getName() + tamPila);
			insertIns("retp", TipoInsMaquina.IGUAL);
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
			insertIns("fjp ", TipoInsMaquina.RESTA1);
			generaCodigoProg(insWhile.getInsWhile());
			insertIns("ujp " + posSaltoW1, TipoInsMaquina.IGUAL);
			codigo.get(posFjp).setName(codigo.get(posFjp).getName() + codigo.size());
			nivelAmbito = bloqueActGenera().getPadre().getPosLista();
			break;
		default:
			break;

		}
	}

	private void generaCodigoProg(P prog) {
		for (Ins ins : prog.getInstr()) {
			if (ins.tipo() == TipoIns.INSPROC || ins.tipo() == TipoIns.INSFUN) {
				int posUjp = codigo.size();
				insertIns("ujp ", TipoInsMaquina.IGUAL);
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
			declaraciones((P)raiz, true);
			insertIns("ssp " + listaBloques.get(0).getSsp(), TipoInsMaquina.IGUAL);
			insertIns("sep ", TipoInsMaquina.IGUAL);
			generaCodigoProg((P) raiz);
			int tamPila = tamPilaEvaluacion(1);
			codigo.get(1).setName(codigo.get(1).getName() + tamPila);
			insertIns("stp", TipoInsMaquina.IGUAL);
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
		bloqueAct.insertaId(iden, nextDir, tam);
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

	private void insertIns(String ins, TipoInsMaquina tipo) {
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
					switch (codigo.get(i).getTipo()) {
					case RESTA1:
						tam--;
						break;
					case RESTA2:
						tam -= 2;
						break;
					case SUMA1:
						tam++;
						break;
					default:
						break;

					}
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
