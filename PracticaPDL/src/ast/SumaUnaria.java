package ast;

public class SumaUnaria extends EUnaria{
	

	public SumaUnaria(E opnd1) {
		super(opnd1);
	}

	public TipoE tipo() {return TipoE.SUMAUNARIA;}
	public String toString() {return "sumaUnaria("+opnd1().toString()+")";}

}

