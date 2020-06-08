package ast;

public abstract class EBin extends E {
	   private E opnd1;
	   private E opnd2;
	   private String name;
	   public EBin(E opnd1, E opnd2, String name, boolean asignable) {
			  super(asignable);
		   this.opnd1 = opnd1;
		    this.opnd2 = opnd2;
		    this.name = name;
	}
	public E opnd1() {return opnd1;}
	public E opnd2() {return opnd2;}
	
	
	
	public void setOpnd1(E opnd1) {
		this.opnd1 = opnd1;
	}
	public void setOpnd2(E opnd2) {
		this.opnd2 = opnd2;
	}
	public TipoN tipoNodo() {
		return TipoN.EBIN;
	}
	
	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__"+ name + '\n';
		String next = prev;
		if(barra) next += "|";
		else next +=" ";
		for(int i = 0; i < ("__" + name).length(); ++i) {
			next = next + " ";
		}
		
		s = s + opnd1().imprime(next, true) + opnd2().imprime(next, false);
		return s;
	}
	}
