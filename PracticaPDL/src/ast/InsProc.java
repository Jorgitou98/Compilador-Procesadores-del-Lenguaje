package ast;

import java.util.List;

public class InsProc extends Ins{
	private E nombre;
	private List<Param> parametros;
	private P instr;
	
	

	public InsProc(E nombre, List<Param> parametros, P instr) {
		super();
		this.nombre = nombre;
		this.parametros = parametros;
		this.instr = instr;
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





	@Override
	public TipoIns tipo() {
		return TipoIns.INSPROC;
	}
	
	public String toString() {
		String params = "";
		for(Param p: parametros) {
			params = params + ", " + p.toString();
		}
		return "InsProc( nombre: " + nombre.toString() + ", parametros: " + params + ", instrucciones: " + instr.toString() + ")"; 
	}

}
