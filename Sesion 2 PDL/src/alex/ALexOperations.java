package alex;

import asint.ClaseLexica;

public class ALexOperations {
  private AnalizadorLexicoTiny alex;
  public ALexOperations(AnalizadorLexicoTiny alex) {
   this.alex = alex;   
  }
  public UnidadLexica unidadId() {
     return new UnidadLexica(alex.fila(),ClaseLexica.IDEN,alex.lexema()); 
  } 
 
  public UnidadLexica unidadEnt() {
     return new UnidadLexica(alex.fila(),ClaseLexica.ENT,alex.lexema()); 
  } 
  public UnidadLexica unidadSuma() {
     return new UnidadLexica(alex.fila(),ClaseLexica.MAS,"+"); 
  } 
  public UnidadLexica unidadResta() {
     return new UnidadLexica(alex.fila(),ClaseLexica.MENOS,"-"); 
  } 
  public UnidadLexica unidadMul() {
     return new UnidadLexica(alex.fila(),ClaseLexica.POR,"*"); 
  } 
  public UnidadLexica unidadDiv() {
     return new UnidadLexica(alex.fila(),ClaseLexica.DIV,"/"); 
  } 
  public UnidadLexica unidadPAp() {
     return new UnidadLexica(alex.fila(),ClaseLexica.PAP,"("); 
  } 
  public UnidadLexica unidadPCierre() {
     return new UnidadLexica(alex.fila(),ClaseLexica.PCIERRE,")"); 
  } 
  public UnidadLexica unidadIgual() {
     return new UnidadLexica(alex.fila(),ClaseLexica.IGUAL,"="); 
  } 
  public UnidadLexica unidadComa() {
     return new UnidadLexica(alex.fila(),ClaseLexica.COMA,","); 
  } 
  public UnidadLexica unidadEof() {
     return new UnidadLexica(alex.fila(),ClaseLexica.EOF,"<EOF>"); 
  }
  
  
  
  public UnidadLexica unidadPrint() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.PRINT,"print"); 
	  }
  public UnidadLexica unidadLfilter() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.LFILTER,"lfilter"); 
	  }
  public UnidadLexica unidadLmap() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.LMAP,"lmap"); 
	  }
  public UnidadLexica unidadLreduce() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.LREDUCE,"lreduce"); 
	  }
  public UnidadLexica unidadDistinto() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.DISTINTO,"!="); 
	  }
  public UnidadLexica unidadMayor() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.MAYOR,">"); 
	  }
  public UnidadLexica unidadMenor() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.MENOR,"<"); 
	  }
  public UnidadLexica unidadIgualigual() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.IGUALIGUAL,"=="); 
	  }
  public UnidadLexica unidadConcat() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.CONCAT,"#"); 
	  }
  public UnidadLexica unidadCorcheteAbre() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.CORCHETEABRE,"["); 
	  }
  public UnidadLexica unidadCorcheteCierra() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.CORCHETECIERRA,"]"); 
	  }
  
}
