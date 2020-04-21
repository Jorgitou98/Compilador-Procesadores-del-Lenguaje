package ast;

public class Caracter extends E{
	private String v;
	  public Caracter(String v) {
	   this.v = v;   
	  }
	  public String carac() {return v;} 
	  

	  
	public TipoE tipo() {
		return TipoE.CARACTER;
	}
	
	@Override
	public String imprime(String prev, boolean barra) {
		return prev + "\\__" + v + "\n";
	}
}
