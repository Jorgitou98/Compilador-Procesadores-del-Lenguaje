package ast;

public class InsFor extends Ins{
	private Ins decIni;
	private E cond;
	private E paso;
	private P inst;
	private NodoArbol varBucle;
	

	public InsFor(Ins decIni, E cond, E paso, P inst) {
		super();
		this.decIni = decIni;
		this.cond = cond;
		this.paso = paso;
		this.inst = inst;
	}
	
	



	public void setVarBucle(NodoArbol varBucle) {
		this.varBucle = varBucle;
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



	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__Inst For\n";
		String next = prev;
		if(barra) next += "|";
		else next += " ";
		s = s + next + "   \\__Ini\n";
		String nextIni = next + "   |";
		for(int i = 0; i < "__Ini".length(); ++i) {
			nextIni += " ";
		}
		s += decIni.imprime(nextIni, false);
		s = s + next + "   \\__Condicion\n";
		String nextCond = next + "   |";
		for(int i = 0; i < "__Condicion".length(); ++i) {
			nextCond += " ";
		}
		s += cond.imprime(nextCond, false);
		s = s + next + "   \\__Paso\n";
		String nextPaso = next + "   |";
		for(int i = 0; i < "__Paso".length(); ++i) {
			nextPaso += " ";
		}
		s += paso.imprime(nextPaso, false);
		s = s + next + "   \\__Instrucciones\n";
		String nextInst = next;
		for(int i = 0; i < "   \\__Instrucciones".length(); ++i) {
			nextInst += " ";
		}
		s += inst.imprime(nextInst, false);
		return s;
	}

}
