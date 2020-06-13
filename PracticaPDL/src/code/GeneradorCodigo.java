package code;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import asint.Main;
import ast.*;

public class GeneradorCodigo {

	private File archivo = new File("code_" + Main.nombreFichero);
	private PrintWriter pw;
	private static int nivelAmbito = 0;
	private List<InsMaquina> codigo = new ArrayList<>();
	private int numInst = 0;
	private int maxAmbitos = 0;
	private UtilsGeneracion utils;

	private void generaCodigoExp(E exp) {
		switch (exp.tipo()) {
		case ACCESOPUNTERO:
			generaCodigoL(exp);
			insertIns("ind", 0);
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
			generaCodigoL(exp);
			insertIns("ind", 0);
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
			if (((Iden) exp).getTipo().tipo() == TipoT.USUARIO) {
				for (Ins ins : ((InsStruct) ((TipoUsuario) ((Iden) exp).getTipo()).getRef()).getDeclaraciones()) {
					Iden iden = new Iden(((Iden) exp).id() + "." + ((Iden) ((InsDec) ins).getVar()).id(), true, 0, 0);
					iden.setTipo(((InsDec) ins).getTipo());
					generaCodigoL1(iden);
					insertIns("ind", 0);
				}
			} else {
				generaCodigoL(exp);
				insertIns("ind", 0);
			}
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
					new Mul(exp.opnd2(), new DivEnt(exp.opnd1(), exp.opnd2(), false, 0, 0), false, 0, 0), false, 0, 0));
			break;
		case MUL:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("mul", -1);
			break;
		case NEW:
			New nuevo = (New) exp;
			insertIns("ldc " + nuevo.getTam(), 1);
			insertIns("new", -2);
			break;
		case NOT:
			generaCodigoExp(exp.opnd1());
			insertIns("not", 0);
			break;
		case OR:
			generaCodigoExp(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			insertIns("or", -1);
			break;
		case PUNTO:
			generaCodigoL(exp);
			insertIns("ind", 0);
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
			Pair<Pair<List<Integer>, Integer>, Pair<Boolean, Pair<Integer, Integer>>>par = calculaSize(exp.opnd1());
			if(par.getValue().getKey()) {
				insertIns("ldc " + par.getKey().getKey().get(par.getKey().getValue()), 1);
			}
			else {
				insertIns("lda " + (bloqueActGenera().getPa() + 1 - par.getValue().getValue().getValue()) + " " + (2 + par.getValue().getValue().getKey() + par.getKey().getValue()), 1);
				insertIns("ind", 0);
			}
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
		default:
			break;
		}
	}
	
	/*
	 * La función devuelve un Par de pares. Esxplicamos que es cada uno de los valores retornados:
	 * 	- Par1 (Key):
	 * 		- Elemento 1 (Key):
	 * 				- Para vectores estáticos: lista con el tamaño por cada una de las dimensiones
	 * 				- Para vectores dinámicos: lista de tamaño 2:
	 * 						- pos 0: número de dimensiones del vector o matriz dinámica
	 * 						- pos 1: tamaño de los elementos de la matriz (si es de tipo básico 1, si
	 * 								 es un registro el tamaño del mismo
	 * 				- Para elementos que no sean vectores va a null
	 * 		- Elemento 2 (Value):
	 * 				- Índice de la dimensión de la matriz o vector (estático o dinámico) en la que estamos
	 * 				- La idea es que el identificador lo pone a 0 y cada operador corchetes que aplicamos lo
	 * 				  incrementa en 1
	 * 				- A null si el identificador final no se corresponde con un vector o matriz dinámica.
	 * 	- Par 2 (Value):
	 * 		- Elemento 1 (Key):
	 * 				- Booleano que indica si se trata de un vector estático o dinámico
	 * 				- A null si el identificador final no se corresponde con un vector
	 * 		- Elemento 2 (value):
	 * 				- Dirección estática de comienzo del vector en memoria	
	 */

	private Pair<Pair<List<Integer>, Integer>, Pair<Boolean, Integer>> generaCodigoL(E exp) {
		Pair<Pair<List<Integer>, Integer>, Pair<Boolean, Integer>> par = new Pair<>(new Pair<>(null, null),
				new Pair<>(null, null));
		if (exp.tipo() == TipoE.IDEN) {
			Iden id;
			if (((Iden) exp).getRef().tipoNodo() == TipoN.INS) {
				id = ((Iden) ((InsDec) ((Iden) exp).getRef()).getVar());
				// Si no es vector me devuelve un null y no pasa nada (está bien así)
				List<Integer> lista = bloqueActGenera()
						.dimensionVect(((Iden) ((InsDec) ((Iden) exp).getRef()).getVar()).id());
				par = new Pair<>(new Pair<>(lista, 0),
						new Pair<>(
								bloqueActGenera().esEstatico(((Iden) ((InsDec) ((Iden) exp).getRef()).getVar()).id()),
								bloqueActGenera().dirVar(((Iden) exp).id())));
				insertIns("lda " + (bloqueActGenera().getPa() + 1 - id.getPa()) + " "
						+ bloqueActGenera().dirVar(((Iden) exp).id()), 1);
			} else {
				id = ((Iden) ((Param) ((Iden) exp).getRef()).getIden());
				if (((Param) ((Iden) exp).getRef()).getTipoDeParam() == TipoParam.REFERENCIA) {
					insertIns("lod 0 " + bloqueActGenera().dirVar(((Iden) exp).id()), 1);
				} else {
					insertIns("lda " + (bloqueActGenera().getPa() + 1 - id.getPa()) + " "
							+ bloqueActGenera().dirVar(((Iden) exp).id()), 1);
				}
			}
			// insertIns("lda " + (bloqueActGenera().getPa() + 1 - id.getPa()) + " " +
			// bloqueActGenera().dirVar(((Iden) exp).id()), 1);
			if (!bloqueActGenera().esEstatico(((Iden) exp).id())) {
				insertIns("ind", 0);
				List<Integer> l = new ArrayList<>();
				// Añado el numero de dimensiones del vector
				l.add(0, bloqueActGenera().queTamano(id.id()));
				// Añado el tamaño de los elementos del vector
				l.add(1, utils.tipoFinalSize(utils.tipoFinal(((InsDec) ((Iden) exp).getRef()).getTipo())));
				par.getKey().setKey(l);
			}
		} else if (exp.tipo() == TipoE.CORCHETES) {
			par = generaCodigoL(exp.opnd1());
			// Si es estatico
			if (par.getValue().getKey()) {
				generaCodigoExp(exp.opnd2());
				int prod = 1;
				for (int i = par.getKey().getValue() + 1; i < par.getKey().getKey().size(); ++i) {
					prod *= par.getKey().getKey().get(i);
				}
				insertIns("chk 0 " + (par.getKey().getKey().get(par.getKey().getValue()) - 1), 0);
				insertIns("ixa " + prod, -1);
				par.getKey().setValue(par.getKey().getValue() + 1);
			}
			// Si es dinámico
			else {
				if (par.getKey().getValue() == 0) {
					generaCodigoExp(exp.opnd2());
				} else {
					insertIns("ldo " + (par.getValue().getValue() + 2 + par.getKey().getValue()), 1);
					insertIns("mul", -1);
					generaCodigoExp(exp.opnd2());
					insertIns("add", -1);
				}
				// Si somos la ultima dimension
				if (par.getKey().getValue() + 3 == par.getKey().getKey().get(0)) {
					insertIns("ldc " + par.getKey().getKey().get(1), 1);
					insertIns("mul", -1);
					insertIns("add", -1);
				}
				par.getKey().setValue(par.getKey().getValue() + 1);
			}

		} else if (exp.tipo() == TipoE.PUNTO) {
			par = generaCodigoL(exp.opnd1());
			int despl = bloqueActGenera().dirVar(((Punto) exp).getTipo().getNombreTipo() + "." + ((Iden) exp.opnd2()).id());
			insertIns("inc " + despl, 0);
			for (Ins dec : ((InsStruct) ((Punto) exp).getTipo().getRef()).getDeclaraciones()) {
				InsDec insDec = (InsDec) dec;
				if (((Iden) insDec.getVar()).id().equals(((Iden) exp.opnd2()).id())
						&& insDec.getTipo().tipo() == TipoT.VECTOR) {
					if (utils.esVectorEstatico(insDec.getTipo(), insDec.getValorInicial())) {
						List<Integer> lista = utils.dimensionesVector(insDec.getTipo(), insDec.getValorInicial());
						par.getKey().setKey(lista);
					} else {
						List<Integer> l = new ArrayList<>();
						l.add(0, bloqueActGenera().queTamano(((Punto) exp).getTipo().getNombreTipo() + "." + ((Iden) insDec.getVar()).id()));
						l.add(1, utils.tipoFinalSize(utils.tipoFinal(insDec.getTipo())));
						par.getKey().setKey(l);
						par.getValue().setValue(par.getValue().getValue() + bloqueActGenera().dirVar(((Punto) exp).getTipo().getNombreTipo() + "." + ((Iden)insDec.getVar()).id()));
						insertIns("ind", 0);
					}
					par.getKey().setValue(0);
					par.getValue().setKey(utils.esVectorEstatico(insDec.getTipo(), insDec.getValorInicial()));
					par.getValue().setValue(bloqueActGenera().dirVar(((Iden) insDec.getVar()).id()));
				}
				else {
					
				}
			}

		} else if (exp.tipo() == TipoE.ACCESOPUNTERO) {
			par = generaCodigoL(exp.opnd1());
			insertIns("ind", 0);
		}
		return par;

	}

	private void generaCodigoL1(E exp) {
		if (exp.tipo() == TipoE.IDEN) {
			insertIns("lda " + 0 + " " + bloqueActGenera().dirVar(((Iden) exp).id()), 1);
		}
	}

	private void asignacionMultiple(Iden iden, Tipos tipo, int dir1, int dir2, E valorIni, int pa) {
		if (tipo.tipo() == TipoT.VECTOR) {
			int dimension = 1;
			for (int i = 0; i < bloqueActGenera().dimensionVect(iden.id()).size() - 1; ++i) {
				dimension *= bloqueActGenera().dimensionVect(iden.id()).get(i);
			}
			for (int i = 0; i < dimension; ++i) {
				if (valorIni != null) {
					asignacionMultiple(iden, utils.tipoFinal(tipo),
							dir1 + i * utils.tipoFinalSize(utils.tipoFinal(tipo)), dir2, valorIni, pa);
				} else
					asignacionMultiple(iden, utils.tipoFinal(tipo),
							dir1 + i * utils.tipoFinalSize(utils.tipoFinal(tipo)),
							dir2 + i * utils.tipoFinalSize(utils.tipoFinal(tipo)), null, pa);
			}
		} else if (tipo.tipo() == TipoT.USUARIO) {
			for (Ins insDec : ((InsStruct) ((TipoUsuario) tipo).getRef()).getDeclaraciones()) {
				int despl = bloqueActGenera()
						.dirVar(((TipoUsuario) tipo).getNombreTipo() + "." + ((Iden) ((InsDec) insDec).getVar()).id());
				Iden id = new Iden(iden.id() + "." + ((Iden) ((InsDec) insDec).getVar()).id(), true, 0, 0);
				if (((InsDec) insDec).getTipo().tipo() == TipoT.VECTOR) {
					bloqueActGenera().insertaDimensiones(id.id(),
							utils.dimensionesVector(((InsDec) insDec).getTipo(), ((InsDec) insDec).getValorInicial()));
				}
				asignacionMultiple(id, ((InsDec) insDec).getTipo(), dir1 + despl, dir2 + despl, null, pa);
			}
		} else {
			insertIns("lda " + pa + " " + dir1, 1);
			if (dir2 != -1) {
				insertIns("lda " + pa + " " + dir2, 1);
				insertIns("ind", 0);
			} else
				generaCodigoExp(valorIni);
			insertIns("sto", -2);
		}
	}

	private void generaCodigoIns(Ins ins) {
		switch (ins.tipo()) {
		case INSASIG:
			insertComentario("\n\\\\Esto es una asignación\n");
			InsAsig insAsig = (InsAsig) ins;
			generaCodigoL(insAsig.getVar());
			generaCodigoExp(insAsig.getValor());
			insertIns("sto", -2);
			break;
		case INSCALL:
			insertComentario("\n\\\\Esto es una llamada a procedimiento\n");
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
			insertComentario("\n\\\\Esto es un if ó if con else\n");
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
				codigo.get(posSalto1).setName(codigo.get(posSalto1).getName() + numInst);
				generaCodigoProg(insCond.getInsElse());
				codigo.get(posSalto2).setName(codigo.get(posSalto2).getName() + numInst);
				nivelAmbito = bloqueActGenera().getPadre().getPosLista();
			} else
				codigo.get(posSalto1).setName(codigo.get(posSalto1).getName() + numInst);
			break;
		case INSDEC:
			InsDec insDec = (InsDec) ins;
			if (insDec.isConValorInicial()) {
				insertComentario("\n\\\\Esto es una declaracion\n");
				if (insDec.getTipo().tipo() == TipoT.VECTOR) {
					if (utils.esVectorEstatico(insDec.getTipo(), insDec.getValorInicial())) {
						// bloqueActGenera().insertaDimensiones(((Iden) insDec.getVar()).id(),
						// dimensionesVector(insDec.getTipo(), insDec.getValorInicial()));
						if (utils.tipoFinal(insDec.getTipo()).tipo() != TipoT.USUARIO) {
							asignacionMultiple((Iden) insDec.getVar(), insDec.getTipo(),
									bloqueActGenera().dirVar(((Iden) insDec.getVar()).id()), -1,
									calculaValorIni(insDec.getValorInicial()), 0);
						} else {
							asignacionMultiple((Iden) insDec.getVar(), insDec.getTipo(),
									bloqueActGenera().dirVar(((Iden) insDec.getVar()).id()),
									bloqueActGenera()
											.dirVar(utils.stringPuntos(calculaValorIni(insDec.getValorInicial()))),
									calculaValorIni(insDec.getValorInicial()), 0);
						}
					} else {
						// generaCodigoL1(insDec.getVar());
						E v = insDec.getValorInicial();
						insertIns("lda 0 " + (bloqueActGenera().dirVar(((Iden) insDec.getVar()).id()) + 1), 1);
						generaCodigoExp(((Vector) v).getTam());
						v = ((Vector) v).getValorIni();
						while (v.tipo() == TipoE.VECTOR) {
							generaCodigoExp(((Vector) v).getTam());
							insertIns("mul", -1);
							v = ((Vector) v).getValorIni();
						}
						insertIns("ldc " + utils.tipoFinalSize(utils.tipoFinal(insDec.getTipo())), 1);
						insertIns("mul", -1);
						insertIns("sto", -2);
						// Aquí tengo guardada en la segunda posicion el tamaño dinamico

						// Cargo la direccion estatica (aqui guardare la direccion dinamica)
						generaCodigoL1(insDec.getVar());

						// Cargo el tamaño que justo me acabo de guaradar
						insertIns("lda 0 " + (bloqueActGenera().dirVar(((Iden) insDec.getVar()).id()) + 1), 1);
						insertIns("ind", 0);

						insertIns("new", -2);
						// Aqui ya tengo reservada la memoria dinámica y guradada la dirección donde
						// empieza.

						// Ahora voy a irme guardando las dimensiones
						int dirTam = bloqueActGenera().dirVar(((Iden) insDec.getVar()).id()) + 2;
						v = insDec.getValorInicial();
						while (v.tipo() == TipoE.VECTOR) {
							insertIns("lda 0 " + dirTam, 1);
							generaCodigoExp(((Vector) v).getTam());
							insertIns("sto", -2);
							v = ((Vector) v).getValorIni();
							dirTam++;
						}
					}

				} else if (insDec.getTipo().tipo() == TipoT.USUARIO) {
					insDec.setConValorInicial(false);
					generaCodigoIns(insDec);
					asignacionMultiple((Iden) insDec.getVar(), insDec.getTipo(),
							bloqueActGenera().dirVar(((Iden) insDec.getVar()).id()),
							bloqueActGenera().dirVar(utils.stringPuntos((insDec.getValorInicial()))), null, 0);
				} else if (insDec.getTipo().tipo() == TipoT.PUNTERO && insDec.getValorInicial().tipo() == TipoE.NEW) {
					generaCodigoL1(insDec.getVar());
					generaCodigoExp(insDec.getValorInicial());
				} else {
					generaCodigoL1(insDec.getVar());
					generaCodigoExp(insDec.getValorInicial());
					insertIns("sto", -2);
				}
			} else if (insDec.getTipo().tipo() == TipoT.USUARIO) {
				for (Ins decStruct : (((InsStruct) ((TipoUsuario) insDec.getTipo()).getRef()).getDeclaraciones())) {
					InsDec decStr = (InsDec) decStruct;
					generaCodigoIns(new InsDec(decStr.getTipo(),
							new Iden(((Iden) insDec.getVar()).id() + "." + ((Iden) decStr.getVar()).id(), true, 0, 0),
							decStr.isConValorInicial(), decStr.getValorInicial(), 0, 0));
				}
			}
			break;
		case INSENUM:
			break;
		case INSFOR:
			insertComentario("\n\\\\Esto es un bucle for\n");
			maxAmbitos++;
			nivelAmbito = maxAmbitos;
			InsFor insFor = (InsFor) ins;
			generaCodigoIns(insFor.getDecIni());
			int posCond = numInst;
			generaCodigoExp(insFor.getCond());
			int posFjpFor = codigo.size();
			insertIns("fjp ", -1);
			generaCodigoProg(insFor.getInst());
			Iden var;
			if (insFor.getDecIni().tipo() == TipoIns.INSDEC) {
				var = (Iden) ((InsDec) insFor.getDecIni()).getVar();
			} else
				var = (Iden) ((InsAsig) insFor.getDecIni()).getVar();
			generaCodigoIns(insFor.getPaso());
			insertIns("ujp " + posCond, 0);
			codigo.get(posFjpFor).setName(codigo.get(posFjpFor).getName() + numInst);
			nivelAmbito = bloqueActGenera().getPadre().getPosLista();
			break;
		case INSFUN:
			insertComentario("\n\\\\Esto es una declaracion de función\n");
			InsFun insFun = (InsFun) ins;
			maxAmbitos++;
			nivelAmbito = maxAmbitos;
			insFun.setDirIni(numInst);
			for (Param p : insFun.getParametros()) {
				if (p.getTipo().tipo() == TipoT.USUARIO && p.getTipoDeParam() == TipoParam.VALOR) {
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
		case INSPROC:
			insertComentario("\n\\\\Esto es una declaracion de procedimiento\n");
			InsProc insProc = (InsProc) ins;
			maxAmbitos++;
			nivelAmbito = maxAmbitos;
			insProc.setDirIni(numInst);
			for (Param p : insProc.getParametros()) {
				if (p.getTipo().tipo() == TipoT.USUARIO && p.getTipoDeParam() == TipoParam.VALOR) {
					insProc.incTamParams(bloqueActGenera().queTamano(((TipoUsuario) p.getTipo()).getNombreTipo()));
				} else {
					insProc.incTamParams(1);
				}
			}
			Bloque b = bloqueActGenera();
			insertIns("ssp " + bloqueActGenera().getSsp(), 0);
			int posSep = codigo.size();
			insertIns("sep ", 0);
			generaCodigoProg(insProc.getInstr());
			int tamPila = tamPilaEvaluacion(posSep);
			codigo.get(posSep).setName(codigo.get(posSep).getName() + tamPila);
			insertIns("retp", 0);
			nivelAmbito = bloqueActGenera().getPadre().getPosLista();
			break;
		case INSSWITCH:
			insertComentario("\n\\\\Esto es un switch\n");
			InsSwitch insSwitch = (InsSwitch) ins;
			generaCodigoProg(utils.switchACond(insSwitch.getVarSwitch(), insSwitch.getListaCase(), 0));
			break;
		case INSTYPEDEF:
			break;
		case INSWHILE:
			insertComentario("\n\\\\Esto es un bucle while\n");
			InsWhile insWhile = (InsWhile) ins;
			maxAmbitos++;
			nivelAmbito = maxAmbitos;
			int posSaltoW1 = numInst;
			generaCodigoExp(insWhile.getCondicion());
			int posFjp = codigo.size();
			insertIns("fjp ", -1);
			generaCodigoProg(insWhile.getInsWhile());
			insertIns("ujp " + posSaltoW1, 0);
			codigo.get(posFjp).setName(codigo.get(posFjp).getName() + numInst);
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
				if (tipo.tipo() == TipoT.USUARIO || tipo.tipo() == TipoT.VECTOR) {
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
				codigo.get(posUjp).setName(codigo.get(posUjp).getName() + numInst);
			} else
				generaCodigoIns(ins);
		}
	}

	public void generaCodigo(NodoArbol raiz) {
		try {
			try {
				archivo.createNewFile();
			} catch (Exception e) {
				System.out.println("Problema al crear el fichero de código máquina");
			}
			pw = new PrintWriter(archivo);
			AsignadorDirecciones asignaDic = new AsignadorDirecciones();
			asignaDic.declaraciones((P) raiz, true);
			utils = asignaDic.getUtils();
			insertIns("ssp " + AsignadorDirecciones.listaBloques.get(0).getSsp(), 0);
			insertIns("sep ", 0);
			generaCodigoProg((P) raiz);
			int tamPila = tamPilaEvaluacion(1);
			codigo.get(1).setName(codigo.get(1).getName() + tamPila);
			insertIns("stp", 0);
			write();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void write() {
		int linea = 0;
		for (InsMaquina ins : codigo) {
			if (!ins.isComentario()) {
				pw.println("{" + linea + "}" + ins.getName() + ";");
				linea++;
			} else
				pw.println(ins.getName());
		}
	}

	private void insertIns(String ins, int tipo) {
		codigo.add(new InsMaquina(ins, tipo));
		numInst++;
	}
	
	private void insertComentario(String ins) {
		InsMaquina insMaquina = new InsMaquina(ins, 0);
		insMaquina.setComentario(true);
		codigo.add(insMaquina);
	}

	protected static Bloque bloqueActGenera() {
		return AsignadorDirecciones.listaBloques.get(nivelAmbito);
	}

	private int tamPilaEvaluacion(int ini) {
		int tam = 0;
		int max = 0;
		int cuenta = 0;
		for (int i = ini; i < codigo.size(); i++) {
			if (cuenta == 0) {
				if (codigo.get(i).getName().length() > 2 && codigo.get(i).getName().substring(0, 3).equals("ssp")) {
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

	private E calculaValorIni(E v) {
		if (v.tipo() == TipoE.VECTOR) {
			return calculaValorIni(((Vector) v).getValorIni());
		} else
			return v;
	}
	
	/*
	 * La función devuelve un Par de pares. Esxplicamos que es cada uno de los valores retornados:
	 * 	- Par1 (Key):
	 * 		- Elemento 1 (Key):
	 * 				- Para vectores estáticos: lista con el tamaño por cada una de las dimensiones
	 * 				- Para vectores dinámicos: lista de tamaño 2:
	 * 						- pos 0: número de dimensiones del vector o matriz dinámica
	 * 						- pos 1: tamaño de los elementos de la matriz (si es de tipo básico 1, si
	 * 								 es un registro el tamaño del mismo
	 * 				- Para elementos que no sean vectores va a null
	 * 		- Elemento 2 (Value):
	 * 				- Índice de la dimensión de la matriz o vector (estático o dinámico) en la que estamos
	 * 				- La idea es que el identificador lo pone a 0 y cada operador corchetes que aplicamos lo
	 * 				  incrementa en 1
	 * 				- A null si el identificador final no se corresponde con un vector o matriz dinámica.
	 * 	- Par 2 (Value):
	 * 		- Elemento 1 (Key):
	 * 				- Booleano que indica si se trata de un vector estático o dinámico
	 * 				- A null si el identificador final no se corresponde con un vector
	 * 		- Elemento 2 (Value) PAR:
	 * 				- Elemento 1 (Key):	
	 * 					- Dirección estática de comienzo del vector en memoria
	 * 				- Elemnto 2 (Value):
	 * 					- Profundidad de anidamiento del director final (para poder hacer luego el load del tamaño correctamente)	
	 */
	
	private Pair<Pair<List<Integer>, Integer>, Pair<Boolean, Pair<Integer, Integer>>> calculaSize(E exp) {
		Pair<Pair<List<Integer>, Integer>, Pair<Boolean, Pair<Integer,Integer>>> par = new Pair<>(new Pair<>(null, null), new Pair<>(null, new Pair<>(null, null)));
		if (exp.tipo() == TipoE.IDEN) {
			Iden id;
			if (((Iden) exp).getRef().tipoNodo() == TipoN.INS) {
				id = ((Iden) ((InsDec) ((Iden) exp).getRef()).getVar());
				// Si no es vector me devuelve un null y no pasa nada (está bien así)
				List<Integer> lista = bloqueActGenera().dimensionVect(((Iden) ((InsDec) ((Iden) exp).getRef()).getVar()).id());
				par = new Pair<>(new Pair<>(lista, 0),
						new Pair<>(
								bloqueActGenera().esEstatico(((Iden) ((InsDec) ((Iden) exp).getRef()).getVar()).id()),
								new Pair<>(bloqueActGenera().dirVar(((Iden) exp).id()), id.getPa())));
			} else {
				id = ((Iden) ((Param) ((Iden) exp).getRef()).getIden());
			}
		} else if (exp.tipo() == TipoE.CORCHETES) {
			par = calculaSize(exp.opnd1());
			par.getKey().setValue(par.getKey().getValue() + 1);
		
		} else if (exp.tipo() == TipoE.PUNTO) {
			par = calculaSize(exp.opnd1());
			for (Ins dec : ((InsStruct) ((Punto) exp).getTipo().getRef()).getDeclaraciones()) {
				InsDec insDec = (InsDec) dec;
				if (((Iden) insDec.getVar()).id().equals(((Iden) exp.opnd2()).id())
						&& insDec.getTipo().tipo() == TipoT.VECTOR) {
					if (utils.esVectorEstatico(insDec.getTipo(), insDec.getValorInicial())) {
						List<Integer> lista = utils.dimensionesVector(insDec.getTipo(), insDec.getValorInicial());
						par.getKey().setKey(lista);
					}
	
					par.getKey().setValue(0);
					par.getValue().setKey(utils.esVectorEstatico(insDec.getTipo(), insDec.getValorInicial()));
					par.getValue().getValue().setKey(par.getValue().getValue().getKey() + bloqueActGenera().dirVar(((Punto) exp).getTipo().getNombreTipo() + "." + ((Iden)insDec.getVar()).id()));
					par.getValue().getValue().setValue(((Iden) insDec.getVar()).getPa()+1);
				}
			}
		}
		return par;
	}

}
