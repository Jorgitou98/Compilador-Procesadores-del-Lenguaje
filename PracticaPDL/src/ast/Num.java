package ast;

public abstract class Num extends E {
  private String v;

  public Num(String v, boolean asignable) {
	  super(asignable);
	  this.v = v;   
}
public String num() {return v;} 

@Override
public String imprime(String prev, boolean barra) {return prev + "\\" + "__" + v + "\n";}  
}
