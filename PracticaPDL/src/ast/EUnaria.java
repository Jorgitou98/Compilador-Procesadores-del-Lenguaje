package ast;

public abstract class EUnaria extends E {
	   private E opnd1;
	   private String name;
	   public EUnaria(E opnd1, String name, boolean asignable) {
			  super(asignable);
	     this.opnd1 = opnd1;
	     this.name = name;
	   }
	   public E opnd1() {return opnd1;}
	   
		public TipoN tipoNodo() {
			return TipoN.EUNARIA;
		}
		
		
	   
	   public void setOpnd1(E opnd1) {
			this.opnd1 = opnd1;
		}
	@Override
	   public String imprime(String prev, boolean barra) {
		   String s = prev + "\\__" + name + "\n";
		   String next = prev;
		   if (barra) next += "|";
		   else next += " ";
		   for(int i = 0; i < ("__"+name).length(); ++i) {
			   next = next + " ";
		   }
		   
		   s = s + opnd1.imprime(next, false);
		   return s;
	   }
	}
