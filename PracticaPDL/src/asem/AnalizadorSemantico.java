package asem;

import java.util.List;

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
		if (comprobacionTipos(raizArbol))
			System.out.println("Comprobación de tipos correcta");
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
				break;
			case INSCALL:
				InsCall inscall = (InsCall) nodo;
				NodoArbol ref = tabla.declaracionDe(((Iden) inscall.getNombre()).id());
				if (ref == null)
					GestionErroresTiny
							.errorSemantico("Procedimiento " + ((Iden) inscall.getNombre()).id() + " no declarado");
				inscall.setRef(ref);
				for (E arg : inscall.getArgumentos()) {
					vincula(arg);
				}
				break;
			case INSCOND:
				InsCond inscond = (InsCond) nodo;
				vincula(inscond.getCondicion());
				tabla.abreBloque();
				vincula(inscond.getInsIf());
				if (inscond.isTieneElse())
					vincula(inscond.getInsElse());
				tabla.cierraBloque();
				break;
			case INSDEC:
				InsDec insdec = (InsDec) nodo;
				vincula(insdec.getTipo());
				if (insdec.getVar().tipo() == TipoE.IDEN)
					tabla.insertaId(((Iden) insdec.getVar()).id(), insdec);
				if (insdec.isConValorInicial())
					vincula(insdec.getValorInicial());
				break;
			case INSENUM:
				InsEnum insenum = (InsEnum) nodo;
				tabla.insertaId(((Iden) insenum.getNombre()).id(), insenum);
				break;
			case INSFOR:
				InsFor insfor = (InsFor) nodo;
				tabla.abreBloque();
				vincula(insfor.getDecIni());
				if (insfor.getDecIni().tipo() == TipoIns.INSASIG) {
					NodoArbol refFor = tabla.declaracionDe(((Iden) ((InsAsig) insfor.getDecIni()).getVar()).id());
					insfor.setVarBucle(refFor);
					if (refFor == null)
						GestionErroresTiny.errorSemantico("Variable "
								+ (((Iden) ((InsAsig) insfor.getDecIni()).getVar()).id()) + " no declarada");
				} else {
					NodoArbol refFor2 = tabla.declaracionDe(((Iden) ((InsDec) insfor.getDecIni()).getVar()).id());
					insfor.setVarBucle(refFor2);
					if (refFor2 == null)
						GestionErroresTiny.errorSemantico(
								"Variable " + (((Iden) ((InsDec) insfor.getDecIni()).getVar()).id()) + " no declarada");
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
				for (Param parametro : insfun.getParametros()) {
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
				NodoArbol refSwitch = tabla.declaracionDe(((Iden) insswitch.getVarSwitch()).id());
				insswitch.setRef(refSwitch);
				if (refSwitch == null)
					GestionErroresTiny
							.errorSemantico("Variable " + ((Iden) insswitch.getVarSwitch()).id() + " no declarada");
				for (Case caso : insswitch.getListaCase()) {
					caso.setRef(refSwitch);
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
			tabla.abreBloque();
			vincula(caso.getInstr());
			tabla.cierraBloque();
			break;

		case EBIN:
			EBin expbin = (EBin) nodo;
			if (expbin.tipo() == TipoE.PUNTO)
				vincula(expbin.opnd1());
			else {
				vincula(expbin.opnd1());
				vincula(expbin.opnd2());
			}
			break;
		case EUNARIA:
			EUnaria expunaria = (EUnaria) nodo;
			vincula(expunaria.opnd1());
			break;
		case EXP:
			E exp = (E) nodo;
			if (exp.tipo() == TipoE.IDEN) {
				Iden identif = (Iden) exp;
				NodoArbol refIden = tabla.declaracionDe(identif.id());
				if (refIden == null)
					GestionErroresTiny.errorSemantico("Variable " + identif.id() + " no declarada");
				else {
					identif.setRef(refIden);
					if(refIden.tipoNodo() == TipoN.PARAM) 
						identif.setTipo(((Param) refIden).getTipo());
					else 
						identif.setTipo(((InsDec) refIden).getTipo());
				}

			} else if (exp.tipo() == TipoE.VECTOR) {
				Vector vector = (Vector) exp;
				vincula(vector.getTam());
				vincula(vector.getValorIni());
			} else if (exp.tipo() == TipoE.LLAMADAFUN) {
				LlamadaFun llamada = (LlamadaFun) exp;
				NodoArbol refLlamada = tabla.declaracionDe(((Iden) llamada.getIden()).id());
				llamada.setRef(refLlamada);
				llamada.setTipo(((InsFun) refLlamada).getTipoReturn());
				if (refLlamada == null)
					GestionErroresTiny.errorSemantico("Funcion " + ((Iden) llamada.getIden()).id() + " no declarada");
				for (E arg : llamada.getArgumentos()) {
					vincula(arg);
				}
			}
			break;
		case PARAM:
			Param param = (Param) nodo;
			vincula(param.getTipo());
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
				if(refUsuario.tipoNodo() == TipoN.INS && ((Ins) refUsuario).tipo() == TipoIns.INSTYPEDEF) {
					//nodo = ((InsTypeDef) refUsuario).getTipo();
					tipousuario.setTipoOrig(((InsTypeDef) refUsuario).getTipo());
				}
				else tipousuario.setRef(refUsuario);
				if (refUsuario == null)
					GestionErroresTiny.errorSemantico("Tipo" + tipousuario.getNombreTipo() + " no declarado");
			}
			break;
		default:
			break;
		}
	}

	private Tipos comprobacionTiposExp(NodoArbol nodo) {
		switch (nodo.tipoNodo()) {
		case EBIN:
			EBin ebin = (EBin) nodo;
			switch (ebin.tipo()) {
			case AND:
				if (comprobacionTiposExp(ebin.opnd1()).tipo() == TipoT.BOOL
						&& comprobacionTiposExp(ebin.opnd2()).tipo() == TipoT.BOOL) {
					return new TipoBool();
				} else {
					GestionErroresTiny.errorSemantico("Error tipos and");
				}
				break;
			case CORCHETES:
				if (comprobacionTiposExp(ebin.opnd1()).tipo() == TipoT.VECTOR
						&& comprobacionTiposExp(ebin.opnd2()).tipo() == TipoT.INT) {
					return comprobacionTiposExp(((Vector) ebin.opnd1()).getValorIni());
				} else {
					GestionErroresTiny.errorSemantico("Error tipos corchetes");
				}
				break;
			case DISTINTO:
				TipoT tipo1Dist = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2Dist = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1Dist == tipo2Dist
						&& (tipo1Dist == TipoT.BOOL || tipo1Dist == TipoT.INT || tipo1Dist == TipoT.FLOAT)) {
					return new TipoBool();
				} else {
					GestionErroresTiny.errorSemantico("Error tipos distinto");
				}
				;
				break;
			case DIVENT:
				TipoT tipo1DivEnt = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2DivEnt = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1DivEnt == TipoT.INT && tipo2DivEnt == TipoT.INT) {
					return new TipoInt();
				} else {
					GestionErroresTiny.errorSemantico("Error tipos divEnt");
				}
				;
				break;
			case DIVREAL:
				TipoT tipo1DivReal = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2DivReal = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1DivReal == tipo2DivReal && (tipo1DivReal == TipoT.FLOAT || tipo1DivReal == TipoT.INT)) {
					return new TipoInt();
				} else {
					GestionErroresTiny.errorSemantico("Error tipos divReal");
				}
				;
				break;
			case IGUALIGUAL:
				TipoT tipo1IgualIgual = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2IgualIgual = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1IgualIgual == tipo2IgualIgual && (tipo1IgualIgual == TipoT.BOOL || tipo1IgualIgual == TipoT.INT
						|| tipo1IgualIgual == TipoT.FLOAT)) {
					return new TipoBool();
				} else {
					GestionErroresTiny.errorSemantico("Error tipos IgualIgual");
				}
				;
				break;
			case MAYOR:
				TipoT tipo1Mayor = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2Mayor = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1Mayor == tipo2Mayor && (tipo1Mayor == TipoT.INT || tipo1Mayor == TipoT.FLOAT)) {
					return new TipoBool();
				} else {
					GestionErroresTiny.errorSemantico("Error tipos mayor");
				}
				;
				break;
			case MAYORIGUAL:
				TipoT tipo1MayorIgual = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2MayorIgual = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1MayorIgual == tipo2MayorIgual
						&& (tipo1MayorIgual == TipoT.INT || tipo1MayorIgual == TipoT.FLOAT)) {
					return new TipoBool();
				} else {
					GestionErroresTiny.errorSemantico("Error tipos mayorIgual");
				}
				;
				break;
			case MENOR:
				TipoT tipo1Menor = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2Menor = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1Menor == tipo2Menor && (tipo1Menor == TipoT.INT || tipo1Menor == TipoT.FLOAT)) {
					return new TipoBool();
				} else {
					GestionErroresTiny.errorSemantico("Error tipos menor");
				}
				;
				break;
			case MENORIGUAL:
				TipoT tipo1MenorIgual = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2MenorIgual = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1MenorIgual == tipo2MenorIgual
						&& (tipo1MenorIgual == TipoT.INT || tipo1MenorIgual == TipoT.FLOAT)) {
					return new TipoBool();
				} else {
					GestionErroresTiny.errorSemantico("Error tipos menorIgual");
				}
				;
				break;
			case MODULO:
				TipoT tipo1Modulo = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2Modulo = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1Modulo == TipoT.INT && tipo2Modulo == TipoT.INT) {
					return new TipoInt();
				} else {
					GestionErroresTiny.errorSemantico("Error tipos modulo");
				}
				;
				break;
			case MUL:
				TipoT tipo1Mul = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2Mul = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1Mul == TipoT.INT && tipo2Mul == TipoT.INT) {
					return new TipoInt();
				} else if (tipo1Mul == TipoT.INT && tipo2Mul == TipoT.FLOAT)
					return new TipoFloat();

				else if (tipo1Mul == TipoT.FLOAT && tipo2Mul == TipoT.INT)
					return new TipoFloat();

				else if (tipo1Mul == TipoT.FLOAT && tipo2Mul == TipoT.FLOAT)
					return new TipoFloat();

				else {
					GestionErroresTiny.errorSemantico("Error tipos MUL");
				}
				;
				break;
			case OR:
				if (comprobacionTiposExp(ebin.opnd1()).tipo() == TipoT.BOOL
						&& comprobacionTiposExp(ebin.opnd2()).tipo() == TipoT.BOOL) {
					return new TipoBool();
				} else {
					GestionErroresTiny.errorSemantico("Error tipos or");
				}
				break;
			case PUNTO:
				Tipos tipo1 = comprobacionTiposExp(ebin.opnd1());
				if (tipo1.tipo() == TipoT.USUARIO) {
					Iden opnd2 = (Iden) ebin.opnd2();
					for (Ins ins : ((InsStruct) ((TipoUsuario) tipo1).getRef()).getDeclaraciones()) {
						if (((Iden) ((InsDec) ins).getVar()).id().equals(opnd2.id())) {
							return ((InsDec) ins).getTipo();
						}
					}
					GestionErroresTiny.errorSemantico(
							"Error tipos punto: el campo sobre el que accedemos no se corresponde con ningún campo del struct");
				}
				GestionErroresTiny.errorSemantico(
						"Error tipos punto: el tipo sobre el que queremos acceder a un campo no es un struct");
				break;
			case RESTA:
				TipoT tipo1Resta = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2Resta = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1Resta == TipoT.INT && tipo2Resta == TipoT.INT) {
					return new TipoInt();
				} else if (tipo1Resta == TipoT.INT && tipo2Resta == TipoT.FLOAT)
					return new TipoFloat();

				else if (tipo1Resta == TipoT.FLOAT && tipo2Resta == TipoT.INT)
					return new TipoFloat();

				else if (tipo1Resta == TipoT.FLOAT && tipo2Resta == TipoT.FLOAT)
					return new TipoFloat();

				else {
					GestionErroresTiny.errorSemantico("Error tipos RESTA");
				}
				;
				break;
			case SUMA:
				TipoT tipo1Suma = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2Suma = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1Suma == TipoT.INT && tipo2Suma == TipoT.INT) {
					return new TipoInt();
				} else if (tipo1Suma == TipoT.INT && tipo2Suma == TipoT.FLOAT)
					return new TipoFloat();

				else if (tipo1Suma == TipoT.FLOAT && tipo2Suma == TipoT.INT)
					return new TipoFloat();

				else if (tipo1Suma == TipoT.FLOAT && tipo2Suma == TipoT.FLOAT)
					return new TipoFloat();

				else {
					GestionErroresTiny.errorSemantico("Error tipos suma");
				}
				;
				break;
			default:
				break;

			}
			break;
		case EUNARIA:
			EUnaria eunaria = (EUnaria) nodo;
			switch (eunaria.tipo()) {
			case ACCESOPUNTERO:
				Tipos tipoAcceso = comprobacionTiposExp(eunaria.opnd1());
				if (tipoAcceso.tipo() == TipoT.PUNTERO)
					return ((TipoPuntero) tipoAcceso).getTipoPuntero();
				else
					GestionErroresTiny.errorSemantico("Error tipos acceso a puntero");
				break;
			case NOT:
				if (comprobacionTiposExp(eunaria.opnd1()).tipo() == TipoT.BOOL)
					return new TipoBool();
				else
					GestionErroresTiny.errorSemantico("Error de tipos not");
				break;

			case RESTAUNARIA:
				Tipos tipo1RestaUnaria = comprobacionTiposExp(eunaria.opnd1());
				if (tipo1RestaUnaria.tipo() == TipoT.INT || tipo1RestaUnaria.tipo() == TipoT.FLOAT)
					return tipo1RestaUnaria;
				else
					GestionErroresTiny.errorSemantico("Error de tipos restaUnaria");
				break;
			case SIZE:
				if (comprobacionTiposExp(eunaria.opnd1()).tipo() == TipoT.VECTOR)
					return new TipoInt();
				else
					GestionErroresTiny.errorSemantico("Error de tipos size: no lo estamos aplicando sobre un vector");
				break;
			case SUMAUNARIA:
				Tipos tipo1SumaUnaria = comprobacionTiposExp(eunaria.opnd1());
				if (tipo1SumaUnaria.tipo() == TipoT.INT || tipo1SumaUnaria.tipo() == TipoT.FLOAT)
					return tipo1SumaUnaria;
				else
					GestionErroresTiny.errorSemantico("Error de tipos sumaUnaria");
				break;
			default:
				break;

			}
			break;
		case EXP:
			E exp = (E) nodo;
			switch (exp.tipo()) {
			case CARACTER:
				return new TipoChar();
			case FALSE:
				return new TipoBool();
			case FLOAT:
				return new TipoFloat();
			case IDEN:
				return ((Iden) exp).getTipo();
			case INT:
				return new TipoInt();
			case LLAMADAFUN:
				LlamadaFun llamada = (LlamadaFun) exp;
				List<Param> parametros = ((InsFun) llamada.getRef()).getParametros();
				if (parametros.size() == llamada.getArgumentos().size()) {
					boolean coincide = true;
					for (int i = 0; i < parametros.size(); ++i) {
						coincide = coincide && (parametros.get(i).getTipo())
								.tipo() == comprobacionTiposExp(llamada.getArgumentos().get(i)).tipo();
					}
					if (coincide)
						return llamada.getTipo();
				}
				GestionErroresTiny
						.errorSemantico("Error llamada funcion: los arumentos no coinciden con el tipo de parámetros");
				break;
			case NULL:
				return new TipoPuntero(null);
			case TRUE:
				return new TipoBool();
			case VECTOR:
				Vector v = (Vector) exp;
				if (comprobacionTiposExp(v.getTam()).tipo() == TipoT.INT) {
					return new TipoVector(comprobacionTiposExp(v.getValorIni()));
				} else
					GestionErroresTiny.errorSemantico("El tamaño del vector no es de tipo entero");
				break;
			default:
				break;

			}

			break;
		default:
			break;

		}
		return new TipoError();
	}

	private boolean comprobacionTipos(NodoArbol nodo) {
		switch (nodo.tipoNodo()) {
		case CASE:
			Case caso = (Case) nodo;
			if (((InsDec) caso.getRef()).getTipo().tipo() == comprobacionTiposExp(caso.getNombreCase()).tipo())
				return comprobacionTipos(caso.getInstr());
			else
				GestionErroresTiny.errorSemantico("Error tipos Case: El tipo del case no es igual al del switch");
			break;
		case INS:
			Ins ins = (Ins) nodo;
			switch (ins.tipo()) {
			case INSASIG:
				InsAsig insAsig = (InsAsig) ins;
				if (insAsig.getVar().isAsignable()) {
					if (comprobacionTiposExp(insAsig.getVar()).tipo() == comprobacionTiposExp(insAsig.getValor())
							.tipo())
						return true;
					else
						GestionErroresTiny.errorSemantico(
								"Error de tipos en la asignación: el tipo de la expresión a la derecha del igual no coincide con el tipo de la variable");
				} else
					GestionErroresTiny.errorSemantico("Error tipo no asignable");
				break;
			case INSCALL:
				InsCall llamada = (InsCall) nodo;
				List<Param> parametros = ((InsProc) llamada.getRef()).getParametros();
				if (parametros.size() == llamada.getArgumentos().size()) {
					boolean coincide = true;
					for (int i = 0; i < parametros.size(); ++i) {
						coincide = coincide && (parametros.get(i).getTipo())
								.tipo() == comprobacionTiposExp(llamada.getArgumentos().get(i)).tipo();
					}
					if (!coincide)
						GestionErroresTiny.errorSemantico(
								"Error insCall: los parámetros no son del mismo tipo que los declarados");

					return coincide;
				}
				GestionErroresTiny.errorSemantico("Error insCall: numero de parametros incorrecto");

				break;
			case INSCOND:
				InsCond insCond = (InsCond) ins;
				if (comprobacionTiposExp(insCond.getCondicion()).tipo() == TipoT.BOOL) {
					boolean correcto = comprobacionTipos(insCond.getInsIf());
					if (insCond.isTieneElse()) {
						correcto = correcto && comprobacionTipos(insCond.getInsElse());
					}
					return correcto;
				}
				GestionErroresTiny.errorSemantico("Error InsCond: la condición no es una expresión booleana");
				break;
			case INSDEC:
				InsDec insDec = (InsDec) ins;
				if (insDec.getVar().tipo() == TipoE.IDEN) {
					if (insDec.isConValorInicial())
						if (insDec.getTipo().tipo() == comprobacionTiposExp(insDec.getValorInicial()).tipo())
							return true;
						else
							GestionErroresTiny.errorSemantico(
									"Error tipos InsDec: el tipo del valor inicial no coincide con el tipo declarado");
					else
						return true;
				} else
					GestionErroresTiny.errorSemantico("Error InsDec, la variable no es un identificador");
				break;
			case INSENUM:
				return true;
			case INSFOR:
				InsFor insFor = (InsFor) ins;
				boolean correcto = comprobacionTipos(insFor.getDecIni())
						&& comprobacionTiposExp(insFor.getCond()).tipo() == TipoT.BOOL;
				if (correcto) {
					Ins decIni = insFor.getDecIni();
					if (decIni.tipo() == TipoIns.INSASIG) {
						if (comprobacionTiposExp(((InsAsig) decIni).getVar())
								.tipo() == comprobacionTiposExp(insFor.getPaso()).tipo())
							return comprobacionTipos(insFor.getInst());

					} else if (decIni.tipo() == TipoIns.INSDEC) {
						if (((InsDec) decIni).getTipo().tipo() == comprobacionTiposExp(insFor.getPaso()).tipo())
							return comprobacionTipos(insFor.getInst());
					}
					GestionErroresTiny.errorSemantico(
							"Error tipos InsFor: El tipo del paso no coincide con el de la variable de la declaracion");
				} else
					GestionErroresTiny.errorSemantico(
							"Error tipos InsFor: La declaracion inicial o la condicion no son correctas");

				break;
			case INSFUN:
				return comprobacionTipos(((InsFun) ins).getInstr());
			case INSNEW:
				InsNew insNew = (InsNew) ins;
				Tipos tipoVar = comprobacionTiposExp(insNew.getVar());
				if (tipoVar.tipo() == TipoT.PUNTERO) {
					if (((TipoPuntero) tipoVar).getTipoPuntero().tipo() == insNew.getTipo().tipo()
							&& comprobacionTiposExp(insNew.getValor()).tipo() == insNew.getTipo().tipo()) {
						return true;
					}
					GestionErroresTiny.errorSemantico(
							"Error tipos InsNew: El tipo del new o del valor inicial no coinciden con el del valor apuntado");
				} else
					GestionErroresTiny.errorSemantico(
							"Error tipos InsNew: La parte izquierda de la expresión no es de tipo puntero");
				break;
			case INSPROC:
				return comprobacionTipos(((InsProc) ins).getInstr());
			case INSSTRUCT:
				InsStruct insStruct = (InsStruct) ins;
				boolean correctoStruct = true;
				for (Ins instr : insStruct.getDeclaraciones())
					correctoStruct = correctoStruct && comprobacionTipos(instr);
				return correctoStruct;
			case INSSWITCH:
				InsSwitch insSwitch = (InsSwitch) ins;
				if (insSwitch.getVarSwitch().isAsignable()) {
					boolean correctoSwitch = true;
					for (Case casoSwitch : insSwitch.getListaCase())
						correctoSwitch = correctoSwitch && comprobacionTipos(casoSwitch);
					return correctoSwitch;
				}
				GestionErroresTiny.errorSemantico("Error tipos InsSwitch: La expresion del switch no es válida");

				break;
			case INSTYPEDEF:
				return true;
			case INSWHILE:
				InsWhile insWhile = (InsWhile) ins;
				if (comprobacionTiposExp(insWhile.getCondicion()).tipo() == TipoT.BOOL) {
					return comprobacionTipos(insWhile.getInsWhile());

				}
				GestionErroresTiny.errorSemantico("Error InsWhile: la condición no es de tipo booleano");
				break;
			default:
				break;

			}
			break;
		case PROG:
			boolean correcto = true;
			for (Ins insProg : ((P) nodo).getInstr())
				correcto = correcto && comprobacionTipos(insProg);
			return correcto;
		default:
			break;

		}
		return false;
	}
}
