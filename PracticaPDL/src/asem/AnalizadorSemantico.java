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
		try {
		if (comprobacionTipos(raizArbol))
			System.out.println("Comprobación de tipos correcta");
		}
		catch(Exception e) {
			
		}
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
				insasig.setValor(cambiaEnums(insasig.getValor()));
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
				inscond.setCondicion(cambiaEnums(inscond.getCondicion()));
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
					insdec.setTipo(tipoBasico(insdec.getTipo()));
				}
				catch(Exception e) {}
				if (insdec.isConValorInicial()) {
					vincula(insdec.getValorInicial());
					insdec.setValorInicial(cambiaEnums(insdec.getValorInicial()));
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
				insfor.setCond(cambiaEnums(insfor.getCond()));
				vincula(insfor.getPaso());
				vincula(insfor.getInst());
				tabla.cierraBloque();

				break;
			case INSFUN:
				InsFun insfun = (InsFun) nodo;
				vincula(insfun.getTipoReturn());
				insfun.setTipoReturn(tipoBasico(insfun.getTipoReturn()));
				tabla.insertaId(((Iden) insfun.getNombre()).id(), insfun);
				tabla.abreBloque();
				for (Param parametro : insfun.getParametros()) {
					vincula(parametro);
				}
				vincula(insfun.getInstr());
				vincula(insfun.getValorReturn());
				insfun.setValorReturn(cambiaEnums(insfun.getValorReturn()));
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
				//NodoArbol refSwitch = tabla.declaracionDe(((Iden) insswitch.getVarSwitch()).id());
				//insswitch.setRef(refSwitch);
				/*if (refSwitch == null)
					GestionErroresTiny
							.errorSemantico(nodo.getFila(), nodo.getColumna(), "Variable " + ((Iden) insswitch.getVarSwitch()).id() + " no declarada");
							*/
				vincula(insswitch.getVarSwitch());
				insswitch.setVarSwitch(cambiaEnums(insswitch.getVarSwitch()));
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
				inswhile.setCondicion(cambiaEnums(inswhile.getCondicion()));
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
			caso.setNombreCase(cambiaEnums(caso.getNombreCase()));
			tabla.abreBloque();
			vincula(caso.getInstr());
			tabla.cierraBloque();
			break;

		case EBIN:
			EBin expbin = (EBin) nodo;
			if (expbin.tipo() == TipoE.PUNTO) {
				vincula(expbin.opnd1());
				expbin.setOpnd1(cambiaEnums(expbin.opnd1()));
			}
			else {
				vincula(expbin.opnd1());
				expbin.setOpnd1(cambiaEnums(expbin.opnd1()));
				vincula(expbin.opnd2());
				expbin.setOpnd2(cambiaEnums(expbin.opnd2()));
			}
			break;
		case EUNARIA:
			EUnaria expunaria = (EUnaria) nodo;
			vincula(expunaria.opnd1());
			expunaria.setOpnd1(cambiaEnums(expunaria.opnd1()));
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
				vincula(vector.getTam()); // Como es un entero no hace falta vincular
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
			param.setTipo(tipoBasico(param.getTipo()));
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
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos and");
				}
				break;
			case CORCHETES:
				if (comprobacionTiposExp(ebin.opnd1()).tipo() == TipoT.VECTOR
						&& comprobacionTiposExp(ebin.opnd2()).tipo() == TipoT.INT) {
					// return ((TipoVector)
					// comprobacionTiposExp((TipoVector)comprobacionTiposExp(ebin.opnd1()))).getTipoVector();
					return ((TipoVector) comprobacionTiposExp(ebin.opnd1())).getTipoVector();
				} else {
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos corchetes");
				}
				break;
			case DISTINTO:
				TipoT tipo1Dist = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2Dist = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1Dist == tipo2Dist
						&& (tipo1Dist == TipoT.BOOL || tipo1Dist == TipoT.INT || tipo1Dist == TipoT.FLOAT)) {
					return new TipoBool();
				} else {
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos distinto");
				}
				;
				break;
			case DIVENT:
				TipoT tipo1DivEnt = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2DivEnt = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1DivEnt == TipoT.INT && tipo2DivEnt == TipoT.INT) {
					return new TipoInt();
				} else {
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos divEnt");
				}
				;
				break;
			case DIVREAL:
				TipoT tipo1DivReal = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2DivReal = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1DivReal == tipo2DivReal && (tipo1DivReal == TipoT.FLOAT || tipo1DivReal == TipoT.INT)) {
					return new TipoInt();
				} else {
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos divReal");
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
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos IgualIgual");
				}
				;
				break;
			case MAYOR:
				TipoT tipo1Mayor = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2Mayor = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1Mayor == tipo2Mayor && (tipo1Mayor == TipoT.INT || tipo1Mayor == TipoT.FLOAT)) {
					return new TipoBool();
				} else {
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos mayor");
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
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos mayorIgual");
				}
				;
				break;
			case MENOR:
				TipoT tipo1Menor = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2Menor = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1Menor == tipo2Menor && (tipo1Menor == TipoT.INT || tipo1Menor == TipoT.FLOAT)) {
					return new TipoBool();
				} else {
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos menor");
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
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos menorIgual");
				}
				;
				break;
			case MODULO:
				TipoT tipo1Modulo = comprobacionTiposExp(ebin.opnd1()).tipo();
				TipoT tipo2Modulo = comprobacionTiposExp(ebin.opnd2()).tipo();
				if (tipo1Modulo == TipoT.INT && tipo2Modulo == TipoT.INT) {
					return new TipoInt();
				} else {
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos modulo");
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
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos MUL");
				}
				;
				break;
			case OR:
				if (comprobacionTiposExp(ebin.opnd1()).tipo() == TipoT.BOOL
						&& comprobacionTiposExp(ebin.opnd2()).tipo() == TipoT.BOOL) {
					return new TipoBool();
				} else {
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos or");
				}
				break;
			case PUNTO:
				Tipos tipo1 = comprobacionTiposExp(ebin.opnd1());
				if (tipo1.tipo() == TipoT.USUARIO) {
					((Punto) ebin).setTipo((TipoUsuario) tipo1);
					Iden opnd2 = (Iden) ebin.opnd2();
					for (Ins ins : ((InsStruct) ((TipoUsuario) tipo1).getRef()).getDeclaraciones()) {
						if (((Iden) ((InsDec) ins).getVar()).id().equals(opnd2.id())) {
							return ((InsDec) ins).getTipo();
						}
					}
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
							"Error tipos punto: el campo sobre el que accedemos no se corresponde con ningún campo del struct");
				}
				GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
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
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos RESTA");
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
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos suma");
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
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos acceso a puntero");
				break;
			case NOT:
				if (comprobacionTiposExp(eunaria.opnd1()).tipo() == TipoT.BOOL)
					return new TipoBool();
				else
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error de tipos not");
				break;

			case RESTAUNARIA:
				Tipos tipo1RestaUnaria = comprobacionTiposExp(eunaria.opnd1());
				if (tipo1RestaUnaria.tipo() == TipoT.INT || tipo1RestaUnaria.tipo() == TipoT.FLOAT)
					return tipo1RestaUnaria;
				else
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error de tipos restaUnaria");
				break;
			case SIZE:
				if (comprobacionTiposExp(eunaria.opnd1()).tipo() == TipoT.VECTOR)
					return new TipoInt();
				else
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error de tipos size: no lo estamos aplicando sobre un vector");
				break;
			case SUMAUNARIA:
				Tipos tipo1SumaUnaria = comprobacionTiposExp(eunaria.opnd1());
				if (tipo1SumaUnaria.tipo() == TipoT.INT || tipo1SumaUnaria.tipo() == TipoT.FLOAT)
					return tipo1SumaUnaria;
				else
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error de tipos sumaUnaria");
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
				if(((Ins) llamada.getRef()).tipo() == TipoIns.INSFUN) {
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
							.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error llamada funcion: los arumentos no coinciden con el tipo de parámetros");
				}
				else GestionErroresTiny
				.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error llamada funcion: el identificador no se corresponde con una función");
				break;
			case TRUE:
				return new TipoBool();
			case VECTOR:
				Vector v = (Vector) exp;
				if (comprobacionTiposExp(v.getTam()).tipo() == TipoT.INT) {
					return new TipoVector(comprobacionTiposExp(v.getValorIni()), exp.getFila(), exp.getColumna());
				} else
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "El tamaño del vector no es de tipo entero");
				break;
			case NEW:
				New n = (New) exp;
				if (n.getTipo().tipo() == TipoT.VECTOR) {
					if (compruebaVectorNew(n.getTipo(), n.getTamanos().size())) {
						return new TipoPuntero(n.getTipo(), exp.getFila(), exp.getColumna());
					} else
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
								"Error tipos InsNew: Puntero a tipo Vector no tiene las dimensiones correctas");
				} else if (n.getTamanos().size() == 0) {
					return new TipoPuntero(n.getTipo(), exp.getFila(), exp.getColumna());
				} else
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
							"Error tipos InsNew: No se puede indicar tamaños a un puntero que no es a un vector");

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
			if(caso.getNombreCase().tipo() == TipoE.IDEN && ((Iden) caso.getNombreCase()).id().equals("default")) {
				return comprobacionTipos(caso.getInstr());
			}
			else if (((InsSwitch) caso.getRef()).getTipoSwitch().tipo() == comprobacionTiposExp(caso.getNombreCase()).tipo())
				return comprobacionTipos(caso.getInstr());
			else
				GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos Case: El tipo del case no es igual al del switch");
			break;
		case INS:
			Ins ins = (Ins) nodo;
			switch (ins.tipo()) {
			case INSASIG:
				InsAsig insAsig = (InsAsig) ins;
				if (insAsig.getVar().isAsignable()) {
					Tipos tipoDer = comprobacionTiposExp(insAsig.getValor());
					if (tipoDer.tipo() == TipoT.VECTOR || tipoDer.tipo() == TipoT.USUARIO) {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
								"Error de tipos en la asignación: el tipo de la expresión de la derecha no puede ser un vector o un tipo definido por el usuario");
						return false;
					}
					if (compruebaTiposDecAsig(comprobacionTiposExp(insAsig.getVar()),
							comprobacionTiposExp(insAsig.getValor())))
						return true;
					else
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
								"Error de tipos en la asignación: el tipo de la expresión a la derecha del igual no coincide con el tipo de la variable");
				} else
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipo no asignable");
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
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
								"Error insCall: los parámetros no son del mismo tipo que los declarados");

					return coincide;
				}
				GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error insCall: numero de parametros incorrecto");

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
				GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error InsCond: la condición no es una expresión booleana");
				break;
			case INSDEC:
				InsDec insDec = (InsDec) ins;
				if (insDec.getVar().tipo() == TipoE.IDEN) {
					if (insDec.isConValorInicial())
						if (compruebaTiposDecAsig(insDec.getTipo(), comprobacionTiposExp(insDec.getValorInicial())))
							return true;
						else
							GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
									"Error tipos InsDec: el tipo del valor inicial no coincide con el tipo declarado");
					else {
						if (insDec.getTipo().tipo() == TipoT.VECTOR) {
							GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
									"Error tipos InsDec: los vectores tiene que tener valor inicial. Usa creaVector(valorIni, tam)");
							return false;
						}
						return true;
					}
				} else
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error InsDec, la variable no es un identificador");
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
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
							"Error tipos InsFor: El tipo del paso no coincide con el de la variable de la declaración");
				} else
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
							"Error tipos InsFor: La declaracion inicial o la condicion no son correctas");

				break;
			case INSFUN:
				InsFun insFun = (InsFun) ins;
				if(comprobacionTiposExp(insFun.getValorReturn()).tipo() == insFun.getTipoReturn().tipo()){
					return comprobacionTipos(((InsFun) ins).getInstr());
				}
				else 
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
							"Error tipos InsFun: El tipo del return no se corresponde con el de la función");
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
				Tipos tipoSwitch = comprobacionTiposExp(insSwitch.getVarSwitch());
				insSwitch.setTipoSwitch(tipoSwitch);
				if (tipoSwitch.tipo() == TipoT.INT || tipoSwitch.tipo()==TipoT.FLOAT || tipoSwitch.tipo() == TipoT.BOOL || tipoSwitch.tipo()==TipoT.CHAR) {
					boolean correctoSwitch = true;
					for (Case casoSwitch : insSwitch.getListaCase())
						correctoSwitch = correctoSwitch && comprobacionTipos(casoSwitch);
					return correctoSwitch;
				}
				GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
						"Error tipos switch: la expresión sobre la que hacemos el switch no se corresponde con un tipo básico");
					
				
				break;
			case INSTYPEDEF:
				return true;
			case INSWHILE:
				InsWhile insWhile = (InsWhile) ins;
				if (comprobacionTiposExp(insWhile.getCondicion()).tipo() == TipoT.BOOL) {
					return comprobacionTipos(insWhile.getInsWhile());

				}
				GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error InsWhile: la condición no es de tipo booleano");
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

	private boolean compruebaTiposDecAsig(Tipos v1, Tipos v2) {
		// Si ninguno es vector o puntero
		if (v1.tipo() != TipoT.VECTOR && v2.tipo() != TipoT.VECTOR && v2.tipo() != TipoT.PUNTERO
				&& v2.tipo() != TipoT.PUNTERO) {
			return v1.tipo() == v2.tipo();
			// Si uno es vector y otro no
		}
		/*
		 * else if ((v1.tipo() != TipoT.VECTOR && v2.tipo() == TipoT.VECTOR) ||
		 * (v1.tipo() == TipoT.VECTOR && v2.tipo() != TipoT.VECTOR)) { return false; }
		 * //Si uno es puntero y otro no else if((v1.tipo() != TipoT.PUNTERO &&
		 * v2.tipo() == TipoT.PUNTERO) || (v1.tipo() == TipoT.PUNTERO && v2.tipo() !=
		 * TipoT.PUNTERO)) { return false; }
		 */
		// Si los dos son vectores
		else if (v1.tipo() == TipoT.VECTOR && v2.tipo() == TipoT.VECTOR) {
			return compruebaTiposDecAsig(((TipoVector) v1).getTipoVector(), ((TipoVector) v2).getTipoVector());
		}
		// Si los dos son punteros
		else if (v1.tipo() == TipoT.PUNTERO && v2.tipo() == TipoT.PUNTERO) {
			return compruebaTiposDecAsig(((TipoPuntero) v1).getTipoPuntero(), ((TipoPuntero) v2).getTipoPuntero());
		} else
			return false;
	}

	private boolean compruebaVectorNew(Tipos tipo, int tam) {
		if (tipo.tipo() == TipoT.VECTOR) {
			return compruebaVectorNew(((TipoVector) tipo).getTipoVector(), tam - 1);
		} else
			return tam == 0;
	}
	private Tipos tipoBasico(Tipos tipo) {
		if(tipo.tipo() == TipoT.PUNTERO) {
			((TipoPuntero)tipo).setTipoPuntero(tipoBasico(((TipoPuntero)tipo).getTipoPuntero()));
		}
		else if(tipo.tipo() == TipoT.USUARIO && ((TipoUsuario)tipo).getTipoOrig() != null) {
				return tipoBasico(tipoOriginal(((TipoUsuario)tipo).getTipoOrig()));
			}
		else if(tipo.tipo() == TipoT.VECTOR) {
			((TipoVector) tipo).setTipoVector(tipoBasico(((TipoVector)tipo).getTipoVector()));
		}
		else if(tipo.tipo()==TipoT.USUARIO && ((Ins)((TipoUsuario)tipo).getRef()).tipo() == TipoIns.INSENUM) {
			return new TipoInt();
		}
		return tipo;
	}
	private Tipos tipoOriginal(Tipos tipo) {
		if(tipo.tipo() == TipoT.USUARIO && ((TipoUsuario) tipo).getTipoOrig()!=null) {
			return tipoBasico(((TipoUsuario) tipo).getTipoOrig());
		}
		else return tipo;
	}
	
	private E cambiaEnums(E exp) {
		if(exp.tipo() == TipoE.PUNTO && exp.opnd1().tipo() == TipoE.IDEN && tabla.declaracionDe(((Iden)exp.opnd1()).id()).tipoNodo() == TipoN.INS && ((Ins)tabla.declaracionDe(((Iden)exp.opnd1()).id())).tipo() == TipoIns.INSENUM){
			if(exp.opnd2().tipo() == TipoE.IDEN) {
				InsEnum insEnum = (InsEnum) tabla.declaracionDe(((Iden)exp.opnd1()).id());
				for(int i = 0; i<insEnum.getListaConstantes().size(); ++i) {
					if(((Iden) insEnum.getListaConstantes().get(i)).id().equals(((Iden)exp.opnd2()).id())){
						return new Ent("" + i, false, exp.getFila(), exp.getColumna());
					}
				}
				GestionErroresTiny.errorSemantico(exp.getFila(), exp.getColumna(),"Error semántico: El valor " + ((Iden)exp.opnd2()).id() + " no se corresponde con ninguno del enumerado " + ((Iden)exp.opnd2()).id());
			}
			else GestionErroresTiny.errorSemantico(exp.getFila(), exp.getColumna(),"Error semántico: No se puede acceder a un valor de un enumerado con algo que no sea un identificador");
		}
		return exp;
	}
}