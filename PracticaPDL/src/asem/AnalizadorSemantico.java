package asem;

import ast.*;
import errors.GestionErroresTiny;

public class AnalizadorSemantico {
	private NodoArbol raizArbol;
	private TablaDeSimbolos tabla = new TablaDeSimbolos();
	
	public AnalizadorSemantico(NodoArbol raizArbol) {
		this.raizArbol = raizArbol;
	}
	
	public void analizaSemantica() {
		tabla.abreBloque();
		vincula(raizArbol);
		tabla.cierraBloque();
	}
	
	private void vincula(NodoArbol nodo) {
		switch(nodo.tipoNodo()) {
		case INS:
			Ins ins = (Ins) nodo;
			switch(ins.tipo()) {
			case INSASIG:
				InsAsig insasig = (InsAsig) nodo;
				vincula(insasig.getVar());
				vincula(insasig.getValor());
				break;
			case INSCALL:
				InsCall inscall = (InsCall) nodo;
				NodoArbol ref = tabla.declaracionDe(((Iden) inscall.getNombre()).id());
				if (ref != null) GestionErroresTiny.errorSemantico("Variable " + ((Iden) inscall.getNombre()).id() + " no declarada");
				inscall.setRef(ref);
				for(E arg: inscall.getArgumentos()) {
					vincula(arg);
				}
				break;
			case INSCOND:
				InsCond inscond= (InsCond) nodo;
				vincula(inscond.getCondicion());
				tabla.abreBloque();
				vincula(inscond.getInsIf());
				if(inscond.isTieneElse()) vincula(inscond.getInsElse());
				tabla.cierraBloque();
				break;
			case INSDEC:
				InsDec insdec = (InsDec) nodo;
				vincula(insdec.getTipo());
				tabla.insertaId(((Iden) insdec.getVar()).id(), insdec);
				if(insdec.isConValorInicial()) vincula(insdec.getValorInicial());
				break;
			case INSENUM:
				InsEnum insenum = (InsEnum) nodo;
				tabla.insertaId(((Iden) insenum.getNombre()).id(), insenum);
				break;
			case INSFOR:
				InsFor insfor = (InsFor) nodo;
				tabla.abreBloque();
				vincula(insfor.getDecIni());
				if(insfor.getDecIni().tipo() == TipoIns.INSASIG) {
					NodoArbol refFor = tabla.declaracionDe(((Iden)((InsAsig)insfor.getDecIni()).getVar()).id());
					insfor.setVarBucle(refFor);
					if (refFor != null) GestionErroresTiny.errorSemantico("Variable " + (((Iden)((InsAsig)insfor.getDecIni()).getVar()).id()) + " no declarada");
				}
				else {
					NodoArbol refFor2 = tabla.declaracionDe(((Iden)((InsDec)insfor.getDecIni()).getVar()).id());
					insfor.setVarBucle(refFor2);
					if (refFor2 != null) GestionErroresTiny.errorSemantico("Variable " + (((Iden)((InsDec)insfor.getDecIni()).getVar()).id()) + " no declarada");
				}
				vincula(insfor.getCond());
				vincula(insfor.getPaso());
				vincula(insfor.getInst());
				tabla.cierraBloque();
				
				break;
			case INSFUN:
				InsFun insfun = (InsFun) nodo;
				vincula(insfun.getTipoReturn());
				tabla.insertaId(((Iden) insfun.getNombre()).id(), insfun);
				tabla.abreBloque();
				for(Param parametro: insfun.getParametros()) {
					vincula(parametro);
				}
				vincula(insfun.getInstr());
				vincula(insfun.getValorReturn());
				tabla.cierraBloque();
				break;
			case INSNEW:
				InsNew insnew = (InsNew) nodo;
				vincula(insnew.getTipo());
				vincula(insnew.getVar());
				vincula(insnew.getValor());
				break;
			case INSPROC:
				InsProc insproc = (InsProc) nodo;
				tabla.insertaId(((Iden) insproc.getNombre()).id(), insproc);
				tabla.abreBloque();
				for(Param parametro: insproc.getParametros()) {
					vincula(parametro);
				}
				vincula(insproc.getInstr());
				tabla.cierraBloque();
				break;
			case INSSTRUCT:
				InsStruct insstruct = (InsStruct) nodo;
				tabla.insertaId(((Iden)insstruct.getNombreTipo()).id(), insstruct);
				tabla.abreBloque();
				for(Ins instruccion: insstruct.getDeclaraciones()) {
					vincula(instruccion);
				}
				tabla.cierraBloque();
				break;
			case INSSWITCH:
				InsSwitch insswitch = (InsSwitch) nodo;
				NodoArbol refSwitch = tabla.declaracionDe(((Iden)insswitch.getVarSwitch()).id());
				insswitch.setRef(refSwitch);
				if(refSwitch == null) GestionErroresTiny.errorSemantico("Variable " + ((Iden)insswitch.getVarSwitch()).id() + " no declarada");
				for (Case caso: insswitch.getListaCase()) {
					vincula(caso);
				}
				break;
			case INSTYPEDEF:
				InsTypeDef instypedef= (InsTypeDef) nodo;
				vincula(instypedef.getTipo());
				tabla.insertaId(((Iden)instypedef.getNombreNuevo()).id(),instypedef );
				break;
			case INSWHILE:
				InsWhile inswhile= (InsWhile) nodo;
				vincula(inswhile.getCondicion());
				tabla.abreBloque();
				vincula(inswhile.getInsWhile());
				tabla.cierraBloque();
				break;
			default:
				break;
			
			}
			break;
		case CASE:
			// NO hace falta vincula el valor del case, es un literal (entero, caracter etc..)
			Case caso= (Case) nodo;
			tabla.abreBloque();
			vincula(caso.getInstr());
			tabla.cierraBloque();
			break;

		case EBIN:
			EBin expbin = (EBin) nodo;
			vincula(expbin.opnd1());
			vincula(expbin.opnd2());
			break;
		case EUNARIA:
			EUnaria expunaria = (EUnaria) nodo;
			vincula(expunaria.opnd1());
			break;
		case EXP:
			E exp= (E) nodo;
			if(exp.tipo() == TipoE.IDEN) {
				Iden identif = (Iden) exp;
				NodoArbol refIden = tabla.declaracionDe(identif.id());
				identif.setRef(refIden);
				if(refIden == null) GestionErroresTiny.errorSemantico("Variable " + identif.id() + " no declarada");
			}
			else if (exp.tipo() == TipoE.VECTOR) {
				Vector vector = (Vector) exp;
				vincula(vector.getTam());
				vincula(vector.getValorIni());
			}
			else if(exp.tipo() == TipoE.LLAMADAFUN) {
				LlamadaFun llamada = (LlamadaFun) exp;
				NodoArbol refLlamada = tabla.declaracionDe(((Iden) llamada.getIden()).id());
				llamada.setRef(refLlamada);
				if(refLlamada == null) GestionErroresTiny.errorSemantico("Variable " + ((Iden) llamada.getIden()).id() + " no declarada");
				for(E arg: llamada.getArgumentos()) {
					vincula(arg);
				}
			}
			break;
		case PARAM:
			Param param= (Param) nodo;
			vincula(param.getTipo());
			tabla.insertaId(((Iden)param.getIden()).id(), param);
			break;
		case PROG:
			P prog= (P) nodo;
			for (Ins instruccion: prog.getInstr()) {
				vincula(instruccion);
			}
			break;
		case TIPOS:
			Tipos tipo= (Tipos) nodo;
			if(tipo.tipo() == TipoT.USUARIO) {
				TipoUsuario tipousuario = (TipoUsuario) tipo;
				NodoArbol refUsuario = tabla.declaracionDe(tipousuario.getNombreTipo());
				tipousuario.setRef(refUsuario);
				if(refUsuario == null) GestionErroresTiny.errorSemantico("Tipo" + tipousuario.getNombreTipo() + " no declarado");
			}
			break;
		default:
			break;
		}
	}
}
