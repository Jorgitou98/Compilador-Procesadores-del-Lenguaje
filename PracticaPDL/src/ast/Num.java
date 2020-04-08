package ast;

public abstract class Num extends E {
  private String v;

  public Num(String v) {
	  this.v = v;   
}
public String num() {return v;} 
  public String toString() {return v;}  
}
