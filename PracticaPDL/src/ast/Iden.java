package ast;

public class Iden extends E{
private String v;
public Iden(String v) {
	   this.v = v;   
	  }
	  public String id() {return v;} 
	@Override
	public TipoE tipo() {
		return TipoE.IDEN;
	}
	@Override
	public String imprime(String prev, boolean barra) {
		return prev + "\\__" + v + "\n";
	}  
}
