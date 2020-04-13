package ast;

public class IfConElse {
	boolean vieneConElse;
	P instElse;
	public IfConElse(boolean vieneConElse, P instElse) {
		super();
		this.vieneConElse = vieneConElse;
		this.instElse = instElse;
	}
	
	
	
	public boolean isVieneConElse() {
		return vieneConElse;
	}



	public P getInstElse() {
		return instElse;
	}
	
}
