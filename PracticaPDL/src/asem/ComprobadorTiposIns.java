package asem;

import java.util.List;

import ast.Case;
import ast.Iden;
import ast.Ins;
import ast.InsAsig;
import ast.InsCall;
import ast.InsCond;
import ast.InsDec;
import ast.InsFor;
import ast.InsFun;
import ast.InsProc;
import ast.InsStruct;
import ast.InsSwitch;
import ast.InsWhile;
import ast.NodoArbol;
import ast.P;
import ast.Param;
import ast.TipoE;
import ast.TipoIns;
import ast.TipoPuntero;
import ast.TipoT;
import ast.TipoVector;
import ast.Tipos;

import errors.GestionErroresTiny;

public class ComprobadorTiposIns {
	private ComprobadorTiposExp compruebaExp = new ComprobadorTiposExp();
	protected boolean comprobacionTipos(NodoArbol nodo) {
		switch (nodo.tipoNodo()) {
		case CASE:
			Case caso = (Case) nodo;
			if(caso.getNombreCase().tipo() == TipoE.IDEN && ((Iden) caso.getNombreCase()).id().equals("default")) {
				return comprobacionTipos(caso.getInstr());
			}
			else if (((InsSwitch) caso.getRef()).getTipoSwitch().tipo() == compruebaExp.comprobacionTiposExp(caso.getNombreCase()).tipo())
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
					Tipos tipoDer = compruebaExp.comprobacionTiposExp(insAsig.getValor());
					if (tipoDer.tipo() == TipoT.VECTOR || tipoDer.tipo() == TipoT.USUARIO) {
						GestionErroresTiny.errorSemantico(nodo.getFila(), nodo.getColumna(), 
								"Error de tipos en la asignación: el tipo de la expresión de la derecha no puede ser un vector o un tipo definido por el usuario");
						return false;
					}
					if (compruebaTiposDecAsig(compruebaExp.comprobacionTiposExp(insAsig.getVar()),
							compruebaExp.comprobacionTiposExp(insAsig.getValor())))
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
								.tipo() == compruebaExp.comprobacionTiposExp(llamada.getArgumentos().get(i)).tipo();
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
				if (compruebaExp.comprobacionTiposExp(insCond.getCondicion()).tipo() == TipoT.BOOL) {
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
						if (compruebaTiposDecAsig(insDec.getTipo(), compruebaExp.comprobacionTiposExp(insDec.getValorInicial())))
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
						&& compruebaExp.comprobacionTiposExp(insFor.getCond()).tipo() == TipoT.BOOL;
				if (correcto) {
					Ins decIni = insFor.getDecIni();
					if (decIni.tipo() == TipoIns.INSASIG) {
						if (compruebaExp.comprobacionTiposExp(((InsAsig) decIni).getVar())
								.tipo() == compruebaExp.comprobacionTiposExp(insFor.getPaso()).tipo())
							return comprobacionTipos(insFor.getInst());

					} else if (decIni.tipo() == TipoIns.INSDEC) {
						if (((InsDec) decIni).getTipo().tipo() == compruebaExp.comprobacionTiposExp(insFor.getPaso()).tipo())
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
				if(compruebaExp.comprobacionTiposExp(insFun.getValorReturn()).tipo() == insFun.getTipoReturn().tipo()){
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
				Tipos tipoSwitch = compruebaExp.comprobacionTiposExp(insSwitch.getVarSwitch());
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
				if (compruebaExp.comprobacionTiposExp(insWhile.getCondicion()).tipo() == TipoT.BOOL) {
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

}
