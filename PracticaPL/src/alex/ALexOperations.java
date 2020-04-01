package alex;

public class ALexOperations {
  private AnalizadorLexicoTiny alex;
  public ALexOperations(AnalizadorLexicoTiny alex) {
   this.alex = alex;   
  }
  public UnidadLexica unidadInt() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.INT); 
	  } 
  public UnidadLexica unidadBool() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.BOOL); 
	  } 
  public UnidadLexica unidadChar() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.CHAR); 
	  } 
  public UnidadLexica unidadTrue() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.TRUE); 
	  } 
  public UnidadLexica unidadFalse() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.FALSE); 
	  } 
  public UnidadLexica unidadFloat() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.FLOAT); 
	  } 
  public UnidadLexica unidadVector() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.VECTOR); 
	  } 
  public UnidadLexica unidadCreaVector() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.CREAVECTOR); 
	  } 
  public UnidadLexica unidadSize() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.SIZE); 
	  } 
  public UnidadLexica unidadWhile() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.WHILE); 
	  } 
  public UnidadLexica unidadDo() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.DO); 
	  } 
  public UnidadLexica unidadThen() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.THEN); 
	  } 
  public UnidadLexica unidadIf() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.IF); 
	  } 
  public UnidadLexica unidadElse() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.ELSE); 
	  } 
  public UnidadLexica unidadProc() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.PROC); 
	  } 
  public UnidadLexica unidadFun() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.FUN); 
	  } 
  public UnidadLexica unidadReturn() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.RETURN); 
	  } 
  public UnidadLexica unidadStruct() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.STRUCT); 
	  } 
  public UnidadLexica unidadTypedef() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.TYPEDEF); 
	  } 
  public UnidadLexica unidadEnum() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.ENUM); 
	  } 
  
  public UnidadLexica unidadId() {
     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.IDEN,
                                         alex.lexema()); 
  } 
   
  public UnidadLexica unidadEnt() {
     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.ENT,alex.lexema()); 
  } 
  public UnidadLexica unidadReal() {
     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.REAL,alex.lexema()); 
  }
  
  public UnidadLexica unidadCaracter() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.CARACTER,alex.lexema()); 
	  }

  public UnidadLexica unidadSuma() {
     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.SUMA); 
  } 
  public UnidadLexica unidadResta() {
     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.RESTA); 
  } 
  public UnidadLexica unidadMul() {
     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.MUL); 
  } 
  
  public UnidadLexica unidadDivReal() {
     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.DIVREAL); 
  } 
  public UnidadLexica unidadDivEnt() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.DIVENT); 
	  } 
  public UnidadLexica unidadModulo() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.MODULO); 
	  }
  public UnidadLexica unidadAnd() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.AND); 
	  }
  public UnidadLexica unidadOr() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.OR); 
	  }
  public UnidadLexica unidadNot() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.NOT); 
	  }
  public UnidadLexica unidadMenor() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.MENOR); 
	  }
  public UnidadLexica unidadMayor() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.MAYOR); 
	  }
  public UnidadLexica unidadMenorIgual() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.MENORIGUAL); 
	  }
  public UnidadLexica unidadMayorIgual() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.MAYORIGUAL); 
	  }
  public UnidadLexica unidadIgualIgual() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.IGUALIGUAL); 
	  }
  public UnidadLexica unidadDistinto() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.DISTINTO); 
	  }
  public UnidadLexica unidadPunto() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.PUNTO); 
	  }
  public UnidadLexica unidadLlavesAp() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.LLAVESAP); 
	  }
  public UnidadLexica unidadLlavesCierre() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.LLAVESCIERRE); 
	  }
  public UnidadLexica unidadBarra() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.BARRA); 
	  }
  public UnidadLexica unidadPAp() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.PAP); 
	  }
  public UnidadLexica unidadPCierre() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.PCIERRE); 
	  }
  public UnidadLexica unidadIgual() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.IGUAL); 
	  }
  public UnidadLexica unidadPuntoYComa() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.PUNTOYCOMA); 
	  }
  public UnidadLexica unidadCorcheteAp() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.CORCHETEAP); 
	  }
  public UnidadLexica unidadCorcheteCierre() {
	     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.CORCHETECIERRE); 
	  }

  public UnidadLexica unidadComa() {
     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.COMA); 
  } 
  public UnidadLexica unidadEof() {
     return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.EOF); 
  }
  public void error() {
    System.err.println("***"+alex.fila()+" Caracter inexperado: "+alex.lexema());
  }
}
