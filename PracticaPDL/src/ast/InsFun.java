package ast;

import java.util.List;

public class InsFun extends Ins{
	private Tipos tipoReturn;
	private E nombre;
	private List<Param> parametros;
	private P instr;
	private E valorReturn;
	
	

	public InsFun(Tipos tipoReturn, E nombre, List<Param> parametros, P instr, E valorReturn) {
		super();
		this.tipoReturn = tipoReturn;
		this.nombre = nombre;
		this.parametros = parametros;
		this.instr = instr;
		this.valorReturn = valorReturn;
	}
	



	public Tipos getTipoReturn() {
		return tipoReturn;
	}




	public E getNombre() {
		return nombre;
	}




	public List<Param> getParametros() {
		return parametros;
	}




	public P getInstr() {
		return instr;
	}




	public E getValorReturn() {
		return valorReturn;
	}




	@Override
	public TipoIns tipo() {
		return TipoIns.INSFUN;
	}
	
	public String toString() {
		String params = "";
		for(Param p: parametros) {
			params = params + ", " + p.toString();
		}
		return "InsFun( tipoReturn: " + tipoReturn.toString() + ", nombre: " + nombre.toString() + ", parametros: " + params + ", instrucciones: " + instr.toString() + ", valorReturn: " + valorReturn.toString() + ")"; 
	}

}
