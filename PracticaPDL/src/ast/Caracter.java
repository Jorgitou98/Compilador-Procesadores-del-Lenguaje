package ast;

public class Caracter extends E{
	private String v;
	  public Caracter(String v) {
	   this.v = v;   
	  }
	  public String carac() {return v;} 
	  public String toString() {return v;}
	  
	public TipoE tipo() {
		return TipoE.CARACTER;
	}  
}
