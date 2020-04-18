package alex;

import asint.ClaseLexica;

public class ALexOperations {
  private AnalizadorLexicoTiny alex;
  public ALexOperations(AnalizadorLexicoTiny alex) {
   this.alex = alex;   
  }
  public UnidadLexica unidadInt() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.INT, "int"); 
	  } 
  public UnidadLexica unidadBool() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.BOOL, "bool"); 
	  } 
  public UnidadLexica unidadChar() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.CHAR, "char"); 
	  } 
  public UnidadLexica unidadTrue() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.TRUE, "true"); 
	  } 
  public UnidadLexica unidadFalse() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.FALSE, "false"); 
	  } 
  public UnidadLexica unidadFloat() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.FLOAT, "float"); 
	  } 
  public UnidadLexica unidadVector() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.VECTOR, "vector"); 
	  } 
  public UnidadLexica unidadCreaVector() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.CREAVECTOR, "creaVector"); 
	  } 
  public UnidadLexica unidadSize() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.SIZE, "size"); 
	  } 
  public UnidadLexica unidadWhile() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.WHILE, "while"); 
	  } 
  public UnidadLexica unidadFor() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.FOR, "for"); 
	  } 
  public UnidadLexica unidadSwitch() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.SWITCH, "switch"); 
	  } 
  public UnidadLexica unidadCase() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.CASE, "case"); 
	  } 
  public UnidadLexica unidadBreak() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.BREAK, "break"); 
	  }
  public UnidadLexica unidadDefault() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.DEFAULT, "default"); 
	  } 
  public UnidadLexica unidadIf() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.IF, "if"); 
	  } 
  public UnidadLexica unidadCall() {
	  return new UnidadLexica(alex.fila(),ClaseLexica.CALL, "call"); 
  }
  
  public UnidadLexica unidadElse() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.ELSE, "else"); 
	  } 
  public UnidadLexica unidadProc() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.PROC, "proc"); 
	  } 
  public UnidadLexica unidadFun() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.FUN, "fun"); 
	  } 
  public UnidadLexica unidadReturn() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.RETURN, "return"); 
	  } 
  public UnidadLexica unidadStruct() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.STRUCT, "struct"); 
	  } 
  public UnidadLexica unidadTypedef() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.TYPEDEF, "typedef"); 
	  } 
  public UnidadLexica unidadEnum() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.ENUM, "enum"); 
	  } 
  
  public UnidadLexica unidadId() {
     return new UnidadLexica(alex.fila(),ClaseLexica.IDEN,
                                         alex.lexema()); 
  } 
   
  public UnidadLexica unidadEnt() {
     return new UnidadLexica(alex.fila(),ClaseLexica.ENT,alex.lexema()); 
  } 
  public UnidadLexica unidadReal() {
     return new UnidadLexica(alex.fila(),ClaseLexica.REAL,alex.lexema()); 
  }
  
  public UnidadLexica unidadCaracter() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.CARACTER,alex.lexema()); 
	  }

  public UnidadLexica unidadSuma() {
     return new UnidadLexica(alex.fila(),ClaseLexica.SUMA, "+"); 
  } 
  public UnidadLexica unidadResta() {
     return new UnidadLexica(alex.fila(),ClaseLexica.RESTA, "-"); 
  } 
  public UnidadLexica unidadMul() {
     return new UnidadLexica(alex.fila(),ClaseLexica.MUL, "*"); 
  } 
  
  public UnidadLexica unidadDivReal() {
     return new UnidadLexica(alex.fila(),ClaseLexica.DIVREAL, "/"); 
  } 
  public UnidadLexica unidadDivEnt() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.DIVENT, "div"); 
	  } 
  public UnidadLexica unidadModulo() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.MODULO, "%"); 
	  }
  public UnidadLexica unidadAnd() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.AND, "&&"); 
	  }
  public UnidadLexica unidadOr() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.OR, "||"); 
	  }
  public UnidadLexica unidadNot() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.NOT, "!"); 
	  }
  public UnidadLexica unidadMenor() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.MENOR, "<"); 
	  }
  public UnidadLexica unidadMayor() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.MAYOR, ">"); 
	  }
  public UnidadLexica unidadMenorIgual() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.MENORIGUAL, "<="); 
	  }
  public UnidadLexica unidadMayorIgual() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.MAYORIGUAL, ">="); 
	  }
  public UnidadLexica unidadIgualIgual() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.IGUALIGUAL, "=="); 
	  }
  public UnidadLexica unidadDistinto() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.DISTINTO, "!="); 
	  }
  public UnidadLexica unidadPunto() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.PUNTO, "."); 
	  }
  public UnidadLexica unidadLlavesAp() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.LLAVESAP, "{"); 
	  }
  public UnidadLexica unidadLlavesCierre() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.LLAVESCIERRE, "}"); 
	  }
  public UnidadLexica unidadBarra() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.BARRA, "|"); 
	  }
  public UnidadLexica unidadPAp() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.PAP, "("); 
	  }
  public UnidadLexica unidadPCierre() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.PCIERRE, ")"); 
	  }
  public UnidadLexica unidadIgual() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.IGUAL, "="); 
	  }
  public UnidadLexica unidadDosPuntos() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.DOSPUNTOS, ":"); 
	  }
  public UnidadLexica unidadPuntoYComa() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.PUNTOYCOMA, ";"); 
	  }
  public UnidadLexica unidadCorcheteAp() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.CORCHETEAP, "["); 
	  }
  public UnidadLexica unidadCorcheteCierre() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.CORCHETECIERRE, "]"); 
	  }

  public UnidadLexica unidadComa() {
     return new UnidadLexica(alex.fila(),ClaseLexica.COMA, ","); 
  }
  public UnidadLexica unidadAmpersand() {
	     return new UnidadLexica(alex.fila(),ClaseLexica.AMPERSAND, ","); 
	  }
  public UnidadLexica unidadEof() {
     return new UnidadLexica(alex.fila(),ClaseLexica.EOF, "<EOF>"); 
  }
  public void error() {
    System.err.println("***"+alex.fila()+" Caracter inexperado: "+alex.lexema());
  }
}
