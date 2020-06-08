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
					int dirBase = bloqueAct.dirVar(((Iden) insDec.getVar()).id());
					insertaDirStruct(insDec, dirBase);

				} else if (insDec.getTipo().tipo() == TipoT.VECTOR) {
					if (insDec.isConValorInicial()) {
						int tamano = calculaTamVector(insDec.getTipo(), insDec.getValorInicial());
						bloqueAct.insertaDimensiones(((Iden) insDec.getVar()).id(),
								dimensionesVector(insDec.getTipo(), insDec.getValorInicial()));
						// ((Iden) insDec.getVar()).setDimensiones(dimensionesVector(insDec.getTipo(),
						// insDec.getValorInicial()));
						insertaId(((Iden) insDec.getVar()).id(), tamano);
						insertaTipo(((Iden) insDec.getVar()).id(), tamano);
					}
				} else if (insDec.getTipo().tipo() == TipoT.PUNTERO && insDec.isConValorInicial()
						&& insDec.getValorInicial().tipo() == TipoE.NEW) {
					insertaId(((Iden) insDec.getVar()).id(), 1);
					New n = ((New) insDec.getValorInicial());
					if (((TipoPuntero) insDec.getTipo()).getTipoPuntero().tipo() == TipoT.VECTOR) {
						int tam = 1;
						for (int dim : n.getTamanos()) {
							tam *= dim;
						}
						if (((TipoVector) ((TipoPuntero) insDec.getTipo()).getTipoPuntero()).getTipoVector()
								.tipo() == TipoT.USUARIO) {
							tam *= queTamano(
									((TipoUsuario) ((TipoVector) ((TipoPuntero) insDec.getTipo()).getTipoPuntero())
											.getTipoVector()).getNombreTipo());
						}
						n.setTam(tam);
						List<Integer> dims = new ArrayList<>(n.getTamanos());
						dims.add(tipoFinalSize(tipoFinal(n.getTipo())));
						bloqueAct.insertaDimensiones(((Iden) insDec.getVar()).id(), dims);
					} else if (((TipoPuntero) insDec.getTipo()).getTipoPuntero().tipo() == TipoT.USUARIO) {
						n.setTam(queTamano(
								((TipoUsuario) ((TipoPuntero) insDec.getTipo()).getTipoPuntero()).getNombreTipo()));
					} else
						n.setTam(1);

				} else {
					if (insDec.getTipo().tipo() == TipoT.PUNTERO && insDec.isConValorInicial()
							&& ((TipoPuntero) insDec.getTipo()).getTipoPuntero().tipo() == TipoT.VECTOR
							&& insDec.getValorInicial().tipo() == TipoE.IDEN) {
						bloqueAct.insertaDimensiones(((Iden) insDec.getVar()).id(),
								bloqueAct.dimensionVect(((Iden) insDec.getValorInicial()).id()));
					}
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
					bloqueAct.insertaCampoStruct(
							((Iden) insStruct.getNombreTipo()).id() + "." + ((Iden) decStruct.getVar()).id(), tamTipo);
					if (decStruct.getTipo().tipo() == TipoT.USUARIO)
						tamTipo += queTamano(((TipoUsuario) decStruct.getTipo()).getNombreTipo());
					else if (decStruct.getTipo().tipo() == TipoT.VECTOR) {
						int tamVect = calculaTamVector(decStruct.getTipo(), decStruct.getValorInicial());
						tamTipo += tamVect;
						insertaTipo(((Iden) insStruct.getNombreTipo()).id() + "." + ((Iden) decStruct.getVar()).id(),
								tamVect);
					} 
					else if (decStruct.getTipo().tipo() == TipoT.PUNTERO && decStruct.isConValorInicial()
							&& decStruct.getValorInicial().tipo() == TipoE.NEW) {
						New n = ((New) decStruct.getValorInicial());
						if (((TipoPuntero) decStruct.getTipo()).getTipoPuntero().tipo() == TipoT.VECTOR) {
							int tam = 1;
							for (int dim : n.getTamanos()) {
								tam *= dim;
							}
							if (((TipoVector) ((TipoPuntero) decStruct.getTipo()).getTipoPuntero()).getTipoVector()
									.tipo() == TipoT.USUARIO) {
								tam *= queTamano(
										((TipoUsuario) ((TipoVector) ((TipoPuntero) decStruct.getTipo()).getTipoPuntero())
												.getTipoVector()).getNombreTipo());
							}
							n.setTam(tam);
							List<Integer> dims = new ArrayList<>(n.getTamanos());
							dims.add(tipoFinalSize(tipoFinal(n.getTipo())));
							bloqueAct.insertaDimensiones(((Iden) decStruct.getVar()).id(), dims);
						} 
						else if (((TipoPuntero) decStruct.getTipo()).getTipoPuntero().tipo() == TipoT.USUARIO) {
							n.setTam(queTamano(
									((TipoUsuario) ((TipoPuntero) decStruct.getTipo()).getTipoPuntero()).getNombreTipo()));
						} 
						else
							n.setTam(1);
						tamTipo++;
					}
					else if (decStruct.getTipo().tipo() == TipoT.PUNTERO && decStruct.isConValorInicial()
							&& ((TipoPuntero) decStruct.getTipo()).getTipoPuntero().tipo() == TipoT.VECTOR
							&& decStruct.getValorInicial().tipo() == TipoE.IDEN) {
						bloqueAct.insertaDimensiones(((Iden) decStruct.getVar()).id(),
								bloqueAct.dimensionVect(((Iden) decStruct.getValorInicial()).id()));
						tamTipo++;
					}
					else
						tamTipo++;

				}
				insertaTipo(((Iden) insStruct.getNombreTipo()).id(), tamTipo);
				break;
			case INSSWITCH:
				InsSwitch insSwitch = (InsSwitch) ins;
				declaraciones(switchACond(insSwitch.getVarSwitch(), insSwitch.getListaCase(), 0).getInstr().get(0));
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

	private void insertaDirStruct(InsDec dec, int dirBase) {
		if (dec.getTipo().tipo() == TipoT.USUARIO) {
			for (Ins insDec : ((InsStruct) ((TipoUsuario) dec.getTipo()).getRef()).getDeclaraciones()) {
				InsDec insDec1 = (InsDec) insDec;
				int dir = dirBase + bloqueAct
						.dirVar(((TipoUsuario) dec.getTipo()).getNombreTipo() + "." + ((Iden) insDec1.getVar()).id());
				insertaDirStruct(new InsDec(insDec1.getTipo(),
						new Iden(((Iden) dec.getVar()).id() + "." + ((Iden) insDec1.getVar()).id(), true, 0, 0),
						insDec1.isConValorInicial(), insDec1.getValorInicial(), 0, 0), dir);
			}
			bloqueAct.insertaTipo(((Iden) dec.getVar()).id(), queTamano(((TipoUsuario) dec.getTipo()).getNombreTipo()));
		} else if (dec.getTipo().tipo() == TipoT.VECTOR) {
			bloqueAct.insertaTipo(((Iden) dec.getVar()).id(), calculaTamVector(dec.getTipo(), dec.getValorInicial()));
			bloqueAct.insertaCampoStruct(((Iden) dec.getVar()).id(), dirBase);
			bloqueAct.insertaDimensiones(((Iden) dec.getVar()).id(),
					dimensionesVector(dec.getTipo(), dec.getValorInicial()));
		} else {
			bloqueAct.insertaCampoStruct(((Iden) dec.getVar()).id(), dirBase);
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
			System.out.println(((Iden) exp).getTipo());
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
		case NULL:
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

	private Pair<List<Integer>, Integer> generaCodigoL(E exp) {
		Pair<List<Integer>, Integer> par = null;
		if (exp.tipo() == TipoE.IDEN) {
			Iden id;
			if (((Iden) exp).getRef().tipoNodo() == TipoN.INS) {
				id = ((Iden) ((InsDec) ((Iden) exp).getRef()).getVar());
				List<Integer> lista = bloqueActGenera()
						.dimensionVect(((Iden) ((InsDec) ((Iden) exp).getRef()).getVar()).id());
				par = new Pair<>(lista, 0);
			} else {
				id = ((Iden) ((Param) ((Iden) exp).getRef()).getIden());
			}
			insertIns("lda " + (bloqueActGenera().getPa() + 1 - id.getPa()) + " "
					+ bloqueActGenera().dirVar(((Iden) exp).id()), 1);
			System.out.println((bloqueActGenera().getPa() + 1 + " " + id.getPa()));
		} else if (exp.tipo() == TipoE.CORCHETES) {
			par = generaCodigoL(exp.opnd1());
			generaCodigoExp(exp.opnd2());
			int prod = 1;
			for (int i = par.getValue() + 1; i < par.getKey().size(); ++i) {
				prod *= par.getKey().get(i);
			}
			insertIns("chk 0 " + (par.getKey().get(par.getValue()) - 1), 0);
			insertIns("ixa " + prod, -1);
			par.setValue(par.getValue() + 1);

		} else if (exp.tipo() == TipoE.PUNTO) {
			par = generaCodigoL(exp.opnd1());
			for (Ins dec : ((InsStruct) ((Punto) exp).getTipo().getRef()).getDeclaraciones()) {
				InsDec insDec = (InsDec) dec;
				if (((Iden) insDec.getVar()).id().equals(((Iden) exp.opnd2()).id())
						&& insDec.getTipo().tipo() == TipoT.VECTOR) {
					List<Integer> lista = dimensionesVector(insDec.getTipo(), insDec.getValorInicial());
					par.setKey(lista);
					par.setValue(0);
				}
			}
			int despl = bloqueActGenera()
					.dirVar(((Punto) exp).getTipo().getNombreTipo() + "." + ((Iden) exp.opnd2()).id());
			insertIns("inc " + despl, 0);

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
					asignacionMultiple(iden, tipoFinal(tipo), dir1 + i * tipoFinalSize(tipoFinal(tipo)), dir2, valorIni,
							pa);
				} else
					asignacionMultiple(iden, tipoFinal(tipo), dir1 + i * tipoFinalSize(tipoFinal(tipo)),
							dir2 + i * tipoFinalSize(tipoFinal(tipo)), null, pa);
			}
		} else if (tipo.tipo() == TipoT.USUARIO) {
			for (Ins insDec : ((InsStruct) ((TipoUsuario) tipo).getRef()).getDeclaraciones()) {
				int despl = bloqueActGenera()
						.dirVar(((TipoUsuario) tipo).getNombreTipo() + "." + ((Iden) ((InsDec) insDec).getVar()).id());
				Iden id = new Iden(iden.id() + "." + ((Iden) ((InsDec) insDec).getVar()).id(), true, 0, 0);
				if (((InsDec) insDec).getTipo().tipo() == TipoT.VECTOR) {
					bloqueActGenera().insertaDimensiones(id.id(),
							dimensionesVector(((InsDec) insDec).getTipo(), ((InsDec) insDec).getValorInicial()));
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
				if (insDec.getTipo().tipo() == TipoT.VECTOR) {
					/*
					 * int dirIni = bloqueActGenera().dirVar(((Iden) insDec.getVar()).id());
					 * //((Iden) insDec.getVar()).setDimensiones(dimensionesVector(insDec.getTipo(),
					 * insDec.getValorInicial())); for (int i = 0; i <
					 * bloqueActGenera().queTamano(((Iden) insDec.getVar()).id()); i= i +
					 * ((Iden)insDec.getVar()).getDimensiones().get(((Iden)insDec.getVar()).
					 * getDimensiones().size() -1)) { int posAct = codigo.size();
					 * generaCodigoExp(calculaValorIni(insDec.getValorInicial())); for( int j = 0; j
					 * < ((Iden)insDec.getVar()).getDimensiones().get(((Iden)insDec.getVar()).
					 * getDimensiones().size() -1); j++) { codigo.add(posAct, new
					 * InsMaquina("lda 0 " + (dirIni + i+ j), 1)); codigo.add(posAct + 3, new
					 * InsMaquina("sto", -2)); posAct += 4; } }
					 */
					bloqueActGenera().insertaDimensiones(((Iden) insDec.getVar()).id(),
							dimensionesVector(insDec.getTipo(), insDec.getValorInicial()));
					if (tipoFinal(insDec.getTipo()).tipo() != TipoT.USUARIO) {
						asignacionMultiple((Iden) insDec.getVar(), insDec.getTipo(),
								bloqueActGenera().dirVar(((Iden) insDec.getVar()).id()), -1,
								calculaValorIni(insDec.getValorInicial()), 0);
					} else {
						asignacionMultiple((Iden) insDec.getVar(), insDec.getTipo(),
								bloqueActGenera().dirVar(((Iden) insDec.getVar()).id()),
								bloqueActGenera().dirVar(stringPuntos(calculaValorIni(insDec.getValorInicial()))),
								calculaValorIni(insDec.getValorInicial()), 0);
					}

				} else if (insDec.getTipo().tipo() == TipoT.USUARIO) {
					insDec.setConValorInicial(false);
					generaCodigoIns(insDec);
					asignacionMultiple((Iden) insDec.getVar(), insDec.getTipo(),
							bloqueActGenera().dirVar(((Iden) insDec.getVar()).id()),
							bloqueActGenera().dirVar(stringPuntos((insDec.getValorInicial()))), null, 0);
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
			generaCodigoIns(new InsAsig(var, insFor.getPaso(), 0, 0));
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
		case INSSWITCH:
			InsSwitch insSwitch = (InsSwitch) ins;
			generaCodigoProg(switchACond(insSwitch.getVarSwitch(), insSwitch.getListaCase(), 0));
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

	private int calculaTamVector(Tipos tipo, E v) {
		if (tipo.tipo() == TipoT.VECTOR) {
			return Integer.parseInt(((Num) ((Vector) v).getTam()).num())
					* calculaTamVector(((TipoVector) tipo).getTipoVector(), ((Vector) v).getValorIni());
		} else if (tipo.tipo() == TipoT.USUARIO) {
			return queTamano(((TipoUsuario) tipo).getNombreTipo());
		} else
			return 1;
	}

	private E calculaValorIni(E v) {
		if (v.tipo() == TipoE.VECTOR) {
			return calculaValorIni(((Vector) v).getValorIni());
		} else
			return v;
	}

	private List<Integer> dimensionesVector(Tipos tipo, E exp) {
		if (exp.tipo() != TipoE.VECTOR) {
			List<Integer> lista = new ArrayList<>();
			if (tipo.tipo() == TipoT.USUARIO) {
				lista.add(queTamano(((TipoUsuario) tipo).getNombreTipo()));
			} else
				lista.add(1);
			return lista;
		} else {
			List<Integer> lista = dimensionesVector(((TipoVector) tipo).getTipoVector(), ((Vector) exp).getValorIni());
			lista.add(0, Integer.parseInt(((Num) ((Vector) exp).getTam()).num()));
			return lista;
		}
	}

	private Tipos tipoFinal(Tipos tipo) {
		if (tipo.tipo() == TipoT.VECTOR) {
			return tipoFinal(((TipoVector) tipo).getTipoVector());
		} else
			return tipo;
	}

	private int tipoFinalSize(Tipos tipo) {
		if (tipo.tipo() == TipoT.USUARIO) {
			return bloqueActGenera().queTamano(((TipoUsuario) tipo).getNombreTipo());
		} else
			return 1;
	}

	private String stringPuntos(E exp) {
		if (exp.tipo() == TipoE.PUNTO) {
			return stringPuntos(exp.opnd1()) + "." + ((Iden) exp.opnd2()).id();
		} else
			return ((Iden) exp).id();
	}
	
	private P switchACond(E var, List<Case> lista, int i) {
		if(i == lista.size()-1) {
			return lista.get(i).getInstr();
		}
		else {
			InsCond cond = new InsCond(new IgualIgual(var, lista.get(i).getNombreCase(), false, 0, 0), lista.get(i).getInstr(), switchACond(var, lista, i+1), 0, 0);
			P prog = new P();
			prog.anadeIns(cond);
			return prog;
		}
	}
}
