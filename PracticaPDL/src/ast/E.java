package ast;

public abstract class E {
	   public abstract TipoE tipo(); 
	   public E opnd1() {throw new UnsupportedOperationException("opnd1");} 
	   public E opnd2() {throw new UnsupportedOperationException("opnd2");} 
	   public String num() {throw new UnsupportedOperationException("num");}
	}
