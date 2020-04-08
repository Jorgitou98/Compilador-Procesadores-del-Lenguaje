package ast;

public class Mayor extends EBin{
	public Mayor(E opnd1, E opnd2) {
	     super(opnd1,opnd2);  
	   }     
	public TipoE tipo() {return TipoE.MAYOR;}
	public String toString() {return "mayor("+opnd1().toString()+","+opnd2().toString()+")";}
}
