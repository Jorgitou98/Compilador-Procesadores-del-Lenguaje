package ast;

public class Iden extends E{
private String v;
public Iden(String v) {
	   this.v = v;   
	  }
	  public String id() {return v;} 
	  public String toString() {return v;}
	@Override
	public TipoE tipo() {
		return TipoE.IDEN;
	}  
}
