package asem;


import ast.*;
import errors.GestionErroresTiny;

public class AnalizadorSemantico {
	private NodoArbol raizArbol;
	private TablaDeSimbolos tabla = new TablaDeSimbolos();
	private UtilsSemantico utils = new UtilsSemantico(tabla);

	public AnalizadorSemantico(NodoArbol raizArbol) {
		this.raizArbol = raizArbol;
	}

	public void analizaSemantica() {
		tabla.abreBloque();
		vincula(raizArbol);
		tabla.cierraBloque();
		//try {
		if ((new ComprobadorTiposIns()).comprobacionTipos(raizArbol))
			System.out.println("Vinculación y comprobación de tipos correcta");
		//}
		//catch(Exception e) {
			
		//}
	}

	private void vincula(NodoArbol nodo) {
		switch (nodo.tipoNodo()) {
		case INS:
			Ins ins = (Ins) nodo;
			switch (ins.tipo()) {
			case INSASIG:
				InsAsig insasig = (InsAsig) nodo;
				vincula(insasig.getVar());
				vincula(insasig.getValor());
				insasig.setValor(utils.cambiaEnums(insasig.getValor()));
				break;
			case INSCALL:
				InsCall inscall = (InsCall) nodo;
				NodoArbol ref = tabla.declaracionDe(((Iden) inscall.getNombre()).id());
				if (ref == null)
					GestionErroresTiny
							.errorSemantico(nodo.getFila(), nodo.getColumna(), "Procedimiento " + ((Iden) inscall.getNombre()).id() + " no declarado");
				else if(((Ins) ref).tipo() == TipoIns.INSPROC) {
					inscall.setRef(ref);
					for (E arg : inscall.getArgumentos()) {
						vincula(arg);
					}
				}
				else GestionErroresTiny
				.errorSemantico(nodo.getFila(), nodo.getColumna(), "El identificador " + ((Iden) inscall.getNombre()).id() + " no se corresponde con un procedimiento");
				break;
			case INSCOND:
				InsCond inscond = (InsCond) nodo;
				vincula(inscond.getCondicion());
				inscond.setCondicion(utils.cambiaEnums(inscond.getCondicion()));
				tabla.abreBloque();
				vincula(inscond.getInsIf());
				tabla.cierraBloque();
				if (inscond.isTieneElse()) {
					tabla.abreBloque();
					vincula(inscond.getInsElse());
					tabla.cierraBloque();
				}
				break;
			case INSDEC:
				InsDec insdec = (InsDec) nodo;
				vincula(insdec.getTipo());
				tabla.insertaId(((Iden) insdec.getVar()).id(), insdec);
				try{
					insdec.setTipo(utils.tipoBasico(insdec.getTipo()));
				}
				catch(Exception e) {}
				if (insdec.isConValorInicial()) {
					vincula(insdec.getValorInicial());
					insdec.setValorInicial(utils.cambiaEnums(insdec.getValorInicial()));
				}
				break;
			case INSENUM:
				InsEnum insenum = (InsEnum) nodo;
				tabla.insertaId(((Iden) insenum.getNombre()).id(), insenum);
				for(E id: insenum.getListaConstantes()) {
					tabla.insertaId(((Iden) id).id(), insenum);
				}
				break;
			case INSFOR:
				InsFor insfor = (InsFor) nodo;
				tabla.abreBloque();
				vincula(insfor.getDecIni());
				if (insfor.getDecIni().tipo() == TipoIns.INSASIG) {
					NodoArbol refFor = tabla.declaracionDe(((Iden) ((InsAsig) insfor.getDecIni()).getVar()).id());
					insfor.setVarBucle(refFor);
					if (refFor == null)
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Variable "
								+ (((Iden) ((InsAsig) insfor.getDecIni()).getVar()).id()) + " no declarada");
				} else {
					NodoArbol refFor2 = tabla.declaracionDe(((Iden) ((InsDec) insfor.getDecIni()).getVar()).id());
					insfor.setVarBucle(refFor2);
					if (refFor2 == null)
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
								"Variable " + (((Iden) ((InsDec) insfor.getDecIni()).getVar()).id()) + " no declarada");
				}
				vincula(insfor.getCond());
				insfor.setCond(utils.cambiaEnums(insfor.getCond()));
				vincula(insfor.getPaso());
				vincula(insfor.getInst());
				tabla.cierraBloque();

				break;
			case INSFUN:
				InsFun insfun = (InsFun) nodo;
				vincula(insfun.getTipoReturn());
				insfun.setTipoReturn(utils.tipoBasico(insfun.getTipoReturn()));
				tabla.insertaId(((Iden) insfun.getNombre()).id(), insfun);
				tabla.abreBloque();
				for (Param parametro : insfun.getParametros()) {
					vincula(parametro);
				}
				vincula(insfun.getInstr());
				vincula(insfun.getValorReturn());
				insfun.setValorReturn(utils.cambiaEnums(insfun.getValorReturn()));
				tabla.cierraBloque();
				break;
			case INSPROC:
				InsProc insproc = (InsProc) nodo;
				tabla.insertaId(((Iden) insproc.getNombre()).id(), insproc);
				tabla.abreBloque();
				for (Param parametro : insproc.getParametros()) {
					vincula(parametro);
				}
				vincula(insproc.getInstr());
				tabla.cierraBloque();
				break;
			case INSSTRUCT:
				InsStruct insstruct = (InsStruct) nodo;
				tabla.insertaId(((Iden) insstruct.getNombreTipo()).id(), insstruct);
				tabla.abreBloque();
				for (Ins instruccion : insstruct.getDeclaraciones()) {
					vincula(instruccion);
				}
				tabla.cierraBloque();
				break;
			case INSSWITCH:
				InsSwitch insswitch = (InsSwitch) nodo;
				vincula(insswitch.getVarSwitch());
				insswitch.setVarSwitch(utils.cambiaEnums(insswitch.getVarSwitch()));
				for (Case caso : insswitch.getListaCase()) {
					caso.setRef(insswitch);
					vincula(caso);
				}
				break;
			case INSTYPEDEF:
				InsTypeDef instypedef = (InsTypeDef) nodo;
				vincula(instypedef.getTipo());
				tabla.insertaId(((Iden) instypedef.getNombreNuevo()).id(), instypedef);
				break;
			case INSWHILE:
				InsWhile inswhile = (InsWhile) nodo;
				vincula(inswhile.getCondicion());
				inswhile.setCondicion(utils.cambiaEnums(inswhile.getCondicion()));
				tabla.abreBloque();
				vincula(inswhile.getInsWhile());
				tabla.cierraBloque();
				break;
			default:
				break;

			}
			break;
		case CASE:
			// NO hace falta vincula el valor del case, es un literal (entero, caracter
			// etc..)
			
			Case caso = (Case) nodo;
			if(!(caso.getNombreCase().tipo() == TipoE.IDEN && ((Iden) caso.getNombreCase()).id().equals("default"))) {
				vincula(caso.getNombreCase());
			}
			caso.setNombreCase(utils.cambiaEnums(caso.getNombreCase()));
			tabla.abreBloque();
			vincula(caso.getInstr());
			tabla.cierraBloque();
			break;

		case EBIN:
			EBin expbin = (EBin) nodo;
			if (expbin.tipo() == TipoE.PUNTO) {
				vincula(expbin.opnd1());
				expbin.setOpnd1(utils.cambiaEnums(expbin.opnd1()));
			}
			else {
				vincula(expbin.opnd1());
				expbin.setOpnd1(utils.cambiaEnums(expbin.opnd1()));
				vincula(expbin.opnd2());
				expbin.setOpnd2(utils.cambiaEnums(expbin.opnd2()));
			}
			break;
		case EUNARIA:
			EUnaria expunaria = (EUnaria) nodo;
			vincula(expunaria.opnd1());
			expunaria.setOpnd1(utils.cambiaEnums(expunaria.opnd1()));
			break;
		case EXP:
			E exp = (E) nodo;
			if (exp.tipo() == TipoE.IDEN) {
				Iden identif = (Iden) exp;
				NodoArbol refIden = tabla.declaracionDe(identif.id());
				if (refIden == null)
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Variable " + identif.id() + " no declarada");
				else {
					identif.setRef(refIden);
					if (refIden.tipoNodo() == TipoN.PARAM)
						identif.setTipo(((Param) refIden).getTipo());
					else if (((Ins) refIden).tipo() == TipoIns.INSDEC) {
						identif.setTipo(((InsDec) refIden).getTipo());
					}
					else if(((Ins)refIden).tipo()!= TipoIns.INSENUM) GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "El identificador " + identif.id() + " no se corresponde con una variable");
				}

			} else if (exp.tipo() == TipoE.VECTOR) {
				Vector vector = (Vector) exp;
				vincula(vector.getTam());
				vincula(vector.getValorIni());
			} else if (exp.tipo() == TipoE.LLAMADAFUN) {
				LlamadaFun llamada = (LlamadaFun) exp;
				NodoArbol refLlamada = tabla.declaracionDe(((Iden) llamada.getIden()).id());
				if (refLlamada == null)
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Funcion " + ((Iden) llamada.getIden()).id() + " no declarada");
				else if(((Ins) refLlamada).tipo() == TipoIns.INSFUN){
					llamada.setRef(refLlamada);
					llamada.setTipo(((InsFun) refLlamada).getTipoReturn());
					
					for (E arg : llamada.getArgumentos()) {
						vincula(arg);
					}
				}
				else {
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "El identificador de la llamada " + ((Iden) llamada.getIden()).id() + " no se corresponde con una función");
				}
			}
			break;
		case PARAM:
			Param param = (Param) nodo;
			vincula(param.getTipo());
			param.setTipo(utils.tipoBasico(param.getTipo()));
			tabla.insertaId(((Iden) param.getIden()).id(), param);
			break;
		case PROG:
			P prog = (P) nodo;
			for (Ins instruccion : prog.getInstr()) {
				vincula(instruccion);
			}
			break;
		case TIPOS:
			Tipos tipo = (Tipos) nodo;
			if (tipo.tipo() == TipoT.USUARIO) {
				TipoUsuario tipousuario = (TipoUsuario) tipo;
				NodoArbol refUsuario = tabla.declaracionDe(tipousuario.getNombreTipo());
				if (refUsuario == null)
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Tipo " + tipousuario.getNombreTipo() + " no declarado");
				else if (refUsuario.tipoNodo() == TipoN.INS && ((Ins) refUsuario).tipo() == TipoIns.INSTYPEDEF) {
					// nodo = ((InsTypeDef) refUsuario).getTipo();
					tipousuario.setTipoOrig(((InsTypeDef) refUsuario).getTipo());
					tipousuario.setRef(refUsuario);
				} else
					tipousuario.setRef(refUsuario);
				
			} else if (tipo.tipo() == TipoT.VECTOR) {
				vincula(((TipoVector) tipo).getTipoVector());
			} else if (tipo.tipo() == TipoT.PUNTERO) {
				vincula(((TipoPuntero) tipo).getTipoPuntero());
			}
			break;
		default:
			break;
		}
	}



}