package ast;

public class InsFor extends Ins{
	private Ins decIni;
	private E cond;
	private E paso;
	private P inst;
	
	

	public InsFor(Ins decIni, E cond, E paso, P inst) {
		super();
		this.decIni = decIni;
		this.cond = cond;
		this.paso = paso;
		this.inst = inst;
	}
	
	



	public P getInst() {
		return inst;
	}





	public Ins getDecIni() {
		return decIni;
	}





	public E getCond() {
		return cond;
	}





	public E getPaso() {
		return paso;
	}





	@Override
	public TipoIns tipo() {
		// TODO Auto-generated method stub
		return TipoIns.INSFOR;
	}
	
	public String toString() {
		return "InsFor( InsIni: " + decIni.toString() + ", condicion: " + cond.toString() + ", paso: " + paso.toString() + "Instr: " + inst.toString() + ")";
	}

}
