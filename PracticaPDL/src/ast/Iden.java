package ast;

public class Iden extends E{
private String v;
private NodoArbol ref;
public Iden(String v, boolean asignable) {
	  super(asignable);
	   this.v = v;   
	  }
	public String id() {return v;} 
	@Override
	public TipoE tipo() {
		return TipoE.IDEN;
	}
	@Override
	public String imprime(String prev, boolean barra) {
		String s = prev + "\\__" + v;
		if(ref != null) s += "Ref: " + ref.imprime(prev + "    ", false);
		
		return s += "\n";
	}
	public void setRef(NodoArbol ref) {
		this.ref = ref;
	} 
	
}
