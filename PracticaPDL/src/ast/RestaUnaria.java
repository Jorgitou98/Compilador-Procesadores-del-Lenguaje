package ast;

public class RestaUnaria extends EUnaria{
	

	public RestaUnaria(E opnd1) {
		super(opnd1);
	}

	public TipoE tipo() {return TipoE.RESTAUNARIA;}
	public String toString() {return "restaUnaria("+opnd1().toString()+")";}

}

