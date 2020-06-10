package code;

import java.util.ArrayList;
import java.util.List;

import ast.Case;
import ast.Iden;
import ast.Ins;
import ast.InsCond;
import ast.InsDec;
import ast.InsFor;
import ast.InsFun;
import ast.InsProc;
import ast.InsStruct;
import ast.InsSwitch;
import ast.InsWhile;
import ast.New;
import ast.NodoArbol;
import ast.P;
import ast.Param;
import ast.TipoE;
import ast.TipoPuntero;
import ast.TipoT;
import ast.TipoUsuario;
import ast.TipoVector;

public class AsignadorDirecciones {
	
	
	protected static List<Bloque> listaBloques = new ArrayList<>();
	private int nextDir = 5;
	private Bloque bloqueAct = null;
	private UtilsGeneracion utils;

	protected void declaraciones(NodoArbol nodo) {
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
					int tamano = utils.queTamano(((TipoUsuario) insDec.getTipo()).getNombreTipo());
					utils.insertaId(((Iden) insDec.getVar()).id(), tamano);
					int dirBase = bloqueAct.dirVar(((Iden) insDec.getVar()).id());
					insertaDirStruct(insDec, dirBase);

				} else if (insDec.getTipo().tipo() == TipoT.VECTOR) {
					if (insDec.isConValorInicial()) {
						int tamano;
						if(utils.esVectorEstatico(insDec.getTipo(), insDec.getValorInicial())) {
							bloqueAct.insertaDimensiones(((Iden) insDec.getVar()).id(), utils.dimensionesVector(insDec.getTipo(), insDec.getValorInicial()));
							tamano = utils.calculaTamVector(insDec.getTipo(), insDec.getValorInicial());
							bloqueAct.insertaEstatico(((Iden) insDec.getVar()).id(), true);
						}
						else {
							tamano = utils.calculaTamVectorDinamico(insDec.getTipo());
							bloqueAct.insertaEstatico(((Iden) insDec.getVar()).id(), false);
						}
						utils.insertaId(((Iden) insDec.getVar()).id(), tamano);
						utils.insertaTipo(((Iden) insDec.getVar()).id(), tamano);
					}
				} else if (insDec.getTipo().tipo() == TipoT.PUNTERO && insDec.isConValorInicial()
						&& insDec.getValorInicial().tipo() == TipoE.NEW) {
					utils.insertaId(((Iden) insDec.getVar()).id(), 1);
					New n = ((New) insDec.getValorInicial());
					if (((TipoPuntero) insDec.getTipo()).getTipoPuntero().tipo() == TipoT.VECTOR) {
						int tam = 1;
						for (int dim : n.getTamanos()) {
							tam *= dim;
						}
						if (((TipoVector) ((TipoPuntero) insDec.getTipo()).getTipoPuntero()).getTipoVector()
								.tipo() == TipoT.USUARIO) {
							tam *= utils.queTamano(
									((TipoUsuario) ((TipoVector) ((TipoPuntero) insDec.getTipo()).getTipoPuntero())
											.getTipoVector()).getNombreTipo());
						}
						n.setTam(tam);
						List<Integer> dims = new ArrayList<>(n.getTamanos());
						dims.add(utils.tipoFinalSize(utils.tipoFinal(n.getTipo())));
						bloqueAct.insertaDimensiones(((Iden) insDec.getVar()).id(), dims);
						bloqueAct.insertaEstatico(((Iden) insDec.getVar()).id(), true);
					} else if (((TipoPuntero) insDec.getTipo()).getTipoPuntero().tipo() == TipoT.USUARIO) {
						n.setTam(utils.queTamano(
								((TipoUsuario) ((TipoPuntero) insDec.getTipo()).getTipoPuntero()).getNombreTipo()));
					} else
						n.setTam(1);

				} else {
					if (insDec.getTipo().tipo() == TipoT.PUNTERO && insDec.isConValorInicial()
							&& ((TipoPuntero) insDec.getTipo()).getTipoPuntero().tipo() == TipoT.VECTOR
							&& insDec.getValorInicial().tipo() == TipoE.IDEN) {
						bloqueAct.insertaDimensiones(((Iden) insDec.getVar()).id(),
								bloqueAct.dimensionVect(((Iden) insDec.getValorInicial()).id()));
						bloqueAct.insertaEstatico(((Iden) insDec.getVar()).id(), true);
					}
					utils.insertaId(((Iden) insDec.getVar()).id(), 1);
				}
				break;
			case INSENUM:
				break;
			case INSFOR:
				utils.abreAmbito();
				InsFor insFor = (InsFor) ins;
				Ins decIni = insFor.getDecIni();
				declaraciones(decIni);
				for (Ins instFor : insFor.getInst().getInstr()) {
					declaraciones(instFor);
				}
				utils.cierraAmbito();
				break;
			case INSFUN:
				utils.abreAmbito(true);
				InsFun insFun = (InsFun) ins;
				insFun.setPa(bloqueAct.getPa());
				for (Param p : insFun.getParametros())
					declaraciones(p);
				for (Ins i : insFun.getInstr().getInstr())
					declaraciones(i);
				utils.cierraAmbito();
				break;
			case INSPROC:
				utils.abreAmbito(true);
				InsProc insProc = (InsProc) ins;
				insProc.setPa(bloqueAct.getPa());
				for (Param p : insProc.getParametros())
					declaraciones(p);
				for (Ins i : insProc.getInstr().getInstr())
					declaraciones(i);
				utils.cierraAmbito();
				break;
			case INSSTRUCT:
				InsStruct insStruct = (InsStruct) ins;
				int tamTipo = 0;
				for (Ins i : insStruct.getDeclaraciones()) {
					InsDec decStruct = (InsDec) i;
					bloqueAct.insertaCampoStruct(
							((Iden) insStruct.getNombreTipo()).id() + "." + ((Iden) decStruct.getVar()).id(), tamTipo);
					if (decStruct.getTipo().tipo() == TipoT.USUARIO)
						tamTipo += utils.queTamano(((TipoUsuario) decStruct.getTipo()).getNombreTipo());
					else if (decStruct.getTipo().tipo() == TipoT.VECTOR) {
						int tamVect;
						if(utils.esVectorEstatico(decStruct.getTipo(), decStruct.getValorInicial())) {
							tamVect = utils.calculaTamVector(decStruct.getTipo(), decStruct.getValorInicial());
						}
						else {
							tamVect = utils.calculaTamVectorDinamico(decStruct.getTipo());
						}
						tamTipo += tamVect;
						utils.insertaTipo(((Iden) insStruct.getNombreTipo()).id() + "." + ((Iden) decStruct.getVar()).id(),
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
								tam *= utils.queTamano(
										((TipoUsuario) ((TipoVector) ((TipoPuntero) decStruct.getTipo()).getTipoPuntero())
												.getTipoVector()).getNombreTipo());
							}
							n.setTam(tam);
							List<Integer> dims = new ArrayList<>(n.getTamanos());
							dims.add(utils.tipoFinalSize(utils.tipoFinal(n.getTipo())));
							bloqueAct.insertaDimensiones(((Iden) decStruct.getVar()).id(), dims);
						} 
						else if (((TipoPuntero) decStruct.getTipo()).getTipoPuntero().tipo() == TipoT.USUARIO) {
							n.setTam(utils.queTamano(
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
				utils.insertaTipo(((Iden) insStruct.getNombreTipo()).id(), tamTipo);
				break;
			case INSSWITCH:
				InsSwitch insSwitch = (InsSwitch) ins;
				declaraciones(utils.switchACond(insSwitch.getVarSwitch(), insSwitch.getListaCase(), 0).getInstr().get(0));
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
				int tamano = utils.queTamano(((TipoUsuario) param.getTipo()).getNombreTipo());
				utils.insertaId(((Iden) param.getIden()).id(), tamano);
			} else {
				utils.insertaId(((Iden) param.getIden()).id(), 1);
			}
			break;
		case PROG:
			P prog = (P) nodo;
			utils.abreAmbito();
			for (Ins insP : prog.getInstr())
				declaraciones(insP);
			utils.cierraAmbito();
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
			bloqueAct.insertaTipo(((Iden) dec.getVar()).id(), utils.queTamano(((TipoUsuario) dec.getTipo()).getNombreTipo()));
		} else if (dec.getTipo().tipo() == TipoT.VECTOR) {
			if(utils.esVectorEstatico(dec.getTipo(), dec.getValorInicial())) {
				bloqueAct.insertaTipo(((Iden) dec.getVar()).id(), utils.calculaTamVector(dec.getTipo(), dec.getValorInicial()));
				bloqueAct.insertaDimensiones(((Iden) dec.getVar()).id(), utils.dimensionesVector(dec.getTipo(), dec.getValorInicial()));
				bloqueAct.insertaEstatico(((Iden) dec.getVar()).id(), true);
			}
			else {
				bloqueAct.insertaTipo(((Iden) dec.getVar()).id(), utils.calculaTamVectorDinamico(dec.getTipo()));
				bloqueAct.insertaEstatico(((Iden) dec.getVar()).id(), false);
			}
			bloqueAct.insertaCampoStruct(((Iden) dec.getVar()).id(), dirBase);
		} else {
			bloqueAct.insertaCampoStruct(((Iden) dec.getVar()).id(), dirBase);
		}
	}

	protected void declaraciones(P nodo, boolean ini) {
		utils = new UtilsGeneracion(this);
		P prog = (P) nodo;
		utils.abreAmbito(ini);
		for (Ins insP : prog.getInstr())
			declaraciones(insP);
		utils.cierraAmbito();
	}


	public UtilsGeneracion getUtils() {
		return utils;
	}


	public int getNextDir() {
		return nextDir;
	}


	public void setNextDir(int nextDir) {
		this.nextDir = nextDir;
	}


	public Bloque getBloqueAct() {
		return bloqueAct;
	}


	public void setBloqueAct(Bloque bloqueAct) {
		this.bloqueAct = bloqueAct;
	}
	
	
}
