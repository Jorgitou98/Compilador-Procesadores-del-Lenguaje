package ast;

public class InsFor extends Ins{
	private Ins decIni;
	private E cond;
	private Ins paso;
	private P inst;
	private NodoArbol varBucle;
	

	public InsFor(Ins decIni, E cond, Ins paso, P inst, int fila, int columna) {
		super();
		this.decIni = decIni;
		this.cond = cond;
		this.paso = paso;
		this.inst = inst;
	     this.fila = fila;
	     this.columna = columna;
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





	public Ins getPaso() {
		return paso;
	}
	
	





	public void setCond(E cond) {
		this.cond = cond;
	}





	public void setPaso(Ins paso) {
		this.paso = paso;
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
		
		s = s + next + "   \\__Ref\n";
		String nextRef = next + "   |";
		for (int i = 0; i < "__Ref".length(); ++i) {
			nextRef += " ";
		}
		if (varBucle != null)
			s += varBucle.imprime(nextRef, false);
		else
			s += nextRef + "\\__ERROR\n";
		
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
