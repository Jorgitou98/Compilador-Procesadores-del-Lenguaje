package ast;

public abstract class EUnaria extends E {
	   private E opnd1;
	   public EUnaria(E opnd1) {
	     this.opnd1 = opnd1;
	   }
	   public E opnd1() {return opnd1;}   
	}
