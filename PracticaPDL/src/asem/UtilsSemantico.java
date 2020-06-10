package asem;

import ast.E;
import ast.Ent;
import ast.Iden;
import ast.Ins;
import ast.InsEnum;
import ast.TipoE;
import ast.TipoIns;
import ast.TipoInt;
import ast.TipoN;
import ast.TipoPuntero;
import ast.TipoT;
import ast.TipoUsuario;
import ast.TipoVector;
import ast.Tipos;
import errors.GestionErroresTiny;

public class UtilsSemantico {
	private TablaDeSimbolos tabla;
	
	public UtilsSemantico(TablaDeSimbolos tabla) {
		this.tabla = tabla;
	}
	protected Tipos tipoBasico(Tipos tipo) {
		if(tipo.tipo() == TipoT.PUNTERO) {
			((TipoPuntero)tipo).setTipoPuntero(tipoBasico(((TipoPuntero)tipo).getTipoPuntero()));
		}
		else if(tipo.tipo() == TipoT.USUARIO && ((TipoUsuario)tipo).getTipoOrig() != null) {
				return tipoBasico(tipoOriginal(((TipoUsuario)tipo).getTipoOrig()));
			}
		else if(tipo.tipo() == TipoT.VECTOR) {
			((TipoVector) tipo).setTipoVector(tipoBasico(((TipoVector)tipo).getTipoVector()));
		}
		else if(tipo.tipo()==TipoT.USUARIO && ((TipoUsuario)tipo).getRef() != null && ((Ins)((TipoUsuario)tipo).getRef()).tipo() == TipoIns.INSENUM) {
			return new TipoInt();
		}
		return tipo;
	}
	protected Tipos tipoOriginal(Tipos tipo) {
		if(tipo.tipo() == TipoT.USUARIO && ((TipoUsuario) tipo).getTipoOrig()!=null) {
			return tipoBasico(((TipoUsuario) tipo).getTipoOrig());
		}
		else return tipo;
	}
	
	protected E cambiaEnums(E exp) {
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
