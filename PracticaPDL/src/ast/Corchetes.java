package ast;

public class Corchetes extends EBin{
    public Corchetes (E opnd1, E opnd2) {
	     super(opnd1,opnd2);  
	   }     
	public TipoE tipo() {return TipoE.CORCHETES;}
	public String toString() {return "corchetes("+opnd1().toString()+","+opnd2().toString()+")";}
}
