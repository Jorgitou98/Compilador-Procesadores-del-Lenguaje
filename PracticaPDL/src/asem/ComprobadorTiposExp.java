package asem;

import java.util.List;

import ast.E;
import ast.EBin;
import ast.EUnaria;
import ast.Iden;
import ast.Ins;
import ast.InsDec;
import ast.InsFun;
import ast.InsStruct;
import ast.LlamadaFun;
import ast.New;
import ast.NodoArbol;
import ast.Param;
import ast.Punto;
import ast.TipoBool;
import ast.TipoChar;
import ast.TipoE;
import ast.TipoError;
import ast.TipoFloat;
import ast.TipoIns;
import ast.TipoInt;
import ast.TipoParam;
import ast.TipoPuntero;
import ast.TipoT;
import ast.TipoUsuario;
import ast.TipoVector;
import ast.Tipos;
import ast.Vector;
import errors.GestionErroresTiny;

public class ComprobadorTiposExp {

	protected Tipos comprobacionTiposExp(NodoArbol nodo) {
		switch (nodo.tipoNodo()) {
		case EBIN:
			EBin ebin = (EBin) nodo;
			Tipos tipo1 = comprobacionTiposExp(ebin.opnd1());
			Tipos tipo2 = null;
			if (ebin.tipo() != TipoE.PUNTO)
				tipo2 = comprobacionTiposExp(ebin.opnd2());

			if (tipo1.tipo() != TipoT.ERROR || (ebin.tipo() != TipoE.PUNTO && tipo2.tipo() != TipoT.ERROR)) {
				switch (ebin.tipo()) {
				case AND:
					if (tipo1.tipo() == TipoT.BOOL && tipo2.tipo() == TipoT.BOOL) {
						return new TipoBool();
					} else {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos and");
					}
					break;
				case CORCHETES:
					if (tipo1.tipo() == TipoT.VECTOR && tipo2.tipo() == TipoT.INT) {
						// return ((TipoVector)
						// comprobacionTiposExp((TipoVector)tipo1)).getTipoVector();
						return ((TipoVector) tipo1).getTipoVector();
					} else {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos corchetes");
					}
					break;
				case DISTINTO:
					TipoT tipo1Dist = tipo1.tipo();
					TipoT tipo2Dist = tipo2.tipo();
					if (tipo1Dist == tipo2Dist
							&& (tipo1Dist == TipoT.BOOL || tipo1Dist == TipoT.INT || tipo1Dist == TipoT.FLOAT)) {
						return new TipoBool();
					} else {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos distinto");
					}
					;
					break;
				case DIVENT:
					TipoT tipo1DivEnt = tipo1.tipo();
					TipoT tipo2DivEnt = tipo2.tipo();
					if (tipo1DivEnt == TipoT.INT && tipo2DivEnt == TipoT.INT) {
						return new TipoInt();
					} else {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos divEnt");
					}
					;
					break;
				case DIVREAL:
					TipoT tipo1DivReal = tipo1.tipo();
					TipoT tipo2DivReal = tipo2.tipo();
					if (tipo1DivReal == tipo2DivReal && (tipo1DivReal == TipoT.FLOAT || tipo1DivReal == TipoT.INT)) {
						return new TipoInt();
					} else {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos divReal");
					}
					;
					break;
				case IGUALIGUAL:
					TipoT tipo1IgualIgual = tipo1.tipo();
					TipoT tipo2IgualIgual = tipo2.tipo();
					if (tipo1IgualIgual == tipo2IgualIgual && (tipo1IgualIgual == TipoT.BOOL
							|| tipo1IgualIgual == TipoT.INT || tipo1IgualIgual == TipoT.FLOAT)) {
						return new TipoBool();
					} else {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos IgualIgual");
					}
					;
					break;
				case MAYOR:
					TipoT tipo1Mayor = tipo1.tipo();
					TipoT tipo2Mayor = tipo2.tipo();
					if (tipo1Mayor == tipo2Mayor && (tipo1Mayor == TipoT.INT || tipo1Mayor == TipoT.FLOAT)) {
						return new TipoBool();
					} else {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos mayor");
					}
					;
					break;
				case MAYORIGUAL:
					TipoT tipo1MayorIgual = tipo1.tipo();
					TipoT tipo2MayorIgual = tipo2.tipo();
					if (tipo1MayorIgual == tipo2MayorIgual
							&& (tipo1MayorIgual == TipoT.INT || tipo1MayorIgual == TipoT.FLOAT)) {
						return new TipoBool();
					} else {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos mayorIgual");
					}
					;
					break;
				case MENOR:
					TipoT tipo1Menor = tipo1.tipo();
					TipoT tipo2Menor = tipo2.tipo();
					if (tipo1Menor == tipo2Menor && (tipo1Menor == TipoT.INT || tipo1Menor == TipoT.FLOAT)) {
						return new TipoBool();
					} else {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos menor");
					}
					;
					break;
				case MENORIGUAL:
					TipoT tipo1MenorIgual = tipo1.tipo();
					TipoT tipo2MenorIgual = tipo2.tipo();
					if (tipo1MenorIgual == tipo2MenorIgual
							&& (tipo1MenorIgual == TipoT.INT || tipo1MenorIgual == TipoT.FLOAT)) {
						return new TipoBool();
					} else {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos menorIgual");
					}
					;
					break;
				case MODULO:
					TipoT tipo1Modulo = tipo1.tipo();
					TipoT tipo2Modulo = tipo2.tipo();
					if (tipo1Modulo == TipoT.INT && tipo2Modulo == TipoT.INT) {
						return new TipoInt();
					} else {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos modulo");
					}
					;
					break;
				case MUL:
					TipoT tipo1Mul = tipo1.tipo();
					TipoT tipo2Mul = tipo2.tipo();
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
					if (tipo1.tipo() == TipoT.BOOL && tipo2.tipo() == TipoT.BOOL) {
						return new TipoBool();
					} else {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error tipos or");
					}
					break;
				case PUNTO:
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
					TipoT tipo1Resta = tipo1.tipo();
					TipoT tipo2Resta = tipo2.tipo();
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
					TipoT tipo1Suma = tipo1.tipo();
					TipoT tipo2Suma = tipo2.tipo();
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
			}
		case EUNARIA:
			EUnaria eunaria = (EUnaria) nodo;
			Tipos tipo1Unaria = comprobacionTiposExp(eunaria.opnd1());
			// Si algo devolvió tipo error, ya mostró su error. No lo propago, simplemente
			// lo ignoro
			if (tipo1Unaria.tipo() != TipoT.ERROR) {
				switch (eunaria.tipo()) {
				case ACCESOPUNTERO:
					if (tipo1Unaria.tipo() == TipoT.PUNTERO)
						return ((TipoPuntero) tipo1Unaria).getTipoPuntero();
					else
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(),
								"Error tipos acceso a puntero");
					break;
				case NOT:
					if (tipo1Unaria.tipo() == TipoT.BOOL)
						return new TipoBool();
					else
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), "Error de tipos not");
					break;

				case RESTAUNARIA:
					if (tipo1Unaria.tipo() == TipoT.INT || tipo1Unaria.tipo() == TipoT.FLOAT)
						return tipo1Unaria;
					else
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(),
								"Error de tipos restaUnaria");
					break;
				case SIZE:
					if (tipo1Unaria.tipo() == TipoT.VECTOR)
						return new TipoInt();
					else
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(),
								"Error de tipos size: no lo estamos aplicando sobre un vector");
					break;
				case SUMAUNARIA:
					if (tipo1Unaria.tipo() == TipoT.INT || tipo1Unaria.tipo() == TipoT.FLOAT)
						return tipo1Unaria;
					else
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(),
								"Error de tipos sumaUnaria");
					break;
				default:
					break;

				}
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
				if (((Ins) llamada.getRef()).tipo() == TipoIns.INSFUN) {
					List<Param> parametros = ((InsFun) llamada.getRef()).getParametros();
					if (parametros.size() == llamada.getArgumentos().size()) {
						boolean coincide = true;
						for (int i = 0; i < parametros.size(); ++i) {
							if(parametros.get(i).getTipoDeParam() == TipoParam.REFERENCIA ) {
								coincide = llamada.getArgumentos().get(i).isAsignable();
							}
							coincide = coincide && (parametros.get(i).getTipo())
									.tipo() == comprobacionTiposExp(llamada.getArgumentos().get(i)).tipo();
						}
						if (coincide)
							return llamada.getTipo();
						else GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(),
								"Error llamada funcion: los tipos de los argumentos de llamda no coinciden o algún parametro por referencia no es asignable");
					}
					else GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(),
							"Error llamada funcion: el número de argumentos en la llamada es incorrecto");
					
				} else
					GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(),
							"Error llamada funcion: el identificador no se corresponde con una función");
				break;
			case TRUE:
				return new TipoBool();
			case VECTOR:
				Vector v = (Vector) exp;
				Tipos tipotam = comprobacionTiposExp(v.getTam());
				if (tipotam.tipo() != TipoT.ERROR) {
					if (comprobacionTiposExp(v.getTam()).tipo() == TipoT.INT) {
						return new TipoVector(comprobacionTiposExp(v.getValorIni()), exp.getFila(), exp.getColumna());
					} else
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(),
								"El tamaño del vector no es de tipo entero");
				}
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

	private boolean compruebaVectorNew(Tipos tipo, int tam) {

		if (tipo.tipo() == TipoT.VECTOR) {
			return compruebaVectorNew(((TipoVector) tipo).getTipoVector(), tam - 1);
		} else
			return tam == 0;
	}

}
