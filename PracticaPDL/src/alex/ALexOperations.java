package alex;

import asint.ClaseLexica;
import errors.GestionErroresTiny;

public class ALexOperations {
  private AnalizadorLexicoTiny alex;
  public ALexOperations(AnalizadorLexicoTiny alex) {
   this.alex = alex;   
  }
  public UnidadLexica unidadInt() {
	     return new UnidadLexica(alex.fila(), alex.columna(), ClaseLexica.INT, "int"); 
	  } 
  public UnidadLexica unidadBool() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.BOOL, "bool"); 
	  } 
  public UnidadLexica unidadChar() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.CHAR, "char"); 
	  } 
  public UnidadLexica unidadTrue() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.TRUE, "true"); 
	  } 
  public UnidadLexica unidadFalse() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.FALSE, "false"); 
	  } 
  public UnidadLexica unidadNew() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.NEW, "new"); 
	  } 
  public UnidadLexica unidadFloat() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.FLOAT, "float"); 
	  } 
  public UnidadLexica unidadVector() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.VECTOR, "vector"); 
	  } 
  public UnidadLexica unidadCreaVector() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.CREAVECTOR, "creaVector"); 
	  } 
  public UnidadLexica unidadSize() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.SIZE, "size"); 
	  } 
  public UnidadLexica unidadWhile() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.WHILE, "while"); 
	  } 
  public UnidadLexica unidadFor() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.FOR, "for"); 
	  } 
  public UnidadLexica unidadSwitch() {
	     return new UnidadLexica(alex.fila(),alex.columna(),ClaseLexica.SWITCH, "switch"); 
	  } 
  public UnidadLexica unidadCase() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.CASE, "case"); 
	  } 
  public UnidadLexica unidadBreak() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.BREAK, "break"); 
	  }
  public UnidadLexica unidadDefault() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.DEFAULT, "default"); 
	  } 
  public UnidadLexica unidadIf() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.IF, "if"); 
	  } 
  public UnidadLexica unidadCall() {
	  return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.CALL, "call"); 
  }
  
  public UnidadLexica unidadElse() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.ELSE, "else"); 
	  } 
  public UnidadLexica unidadProc() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.PROC, "proc"); 
	  } 
  public UnidadLexica unidadFun() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.FUN, "fun"); 
	  } 
  public UnidadLexica unidadReturn() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.RETURN, "return"); 
	  } 
  public UnidadLexica unidadStruct() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.STRUCT, "struct"); 
	  } 
  public UnidadLexica unidadTypedef() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.TYPEDEF, "typedef"); 
	  } 
  public UnidadLexica unidadEnum() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.ENUM, "enum"); 
	  } 
  
  public UnidadLexica unidadId() {
     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.IDEN, alex.lexema()); 
  } 
   
  public UnidadLexica unidadEnt() {
     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.ENT,alex.lexema()); 
  } 
  public UnidadLexica unidadReal() {
     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.REAL,alex.lexema()); 
  }
  
  public UnidadLexica unidadCaracter() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.CARACTER,alex.lexema()); 
	  }

  public UnidadLexica unidadSuma() {
     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.SUMA, "+"); 
  } 
  public UnidadLexica unidadResta() {
     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.RESTA, "-"); 
  } 
  public UnidadLexica unidadMul() {
     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.MUL, "*"); 
  } 
  
  public UnidadLexica unidadDivReal() {
     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.DIVREAL, "/"); 
  } 
  public UnidadLexica unidadDivEnt() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.DIVENT, "div"); 
	  } 
  public UnidadLexica unidadModulo() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.MODULO, "%"); 
	  }
  public UnidadLexica unidadAnd() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.AND, "&&"); 
	  }
  public UnidadLexica unidadOr() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.OR, "||"); 
	  }
  public UnidadLexica unidadNot() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.NOT, "!"); 
	  }
  public UnidadLexica unidadMenor() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.MENOR, "<"); 
	  }
  public UnidadLexica unidadMayor() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.MAYOR, ">"); 
	  }
  public UnidadLexica unidadMenorIgual() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.MENORIGUAL, "<="); 
	  }
  public UnidadLexica unidadMayorIgual() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.MAYORIGUAL, ">="); 
	  }
  public UnidadLexica unidadIgualIgual() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.IGUALIGUAL, "=="); 
	  }
  public UnidadLexica unidadDistinto() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.DISTINTO, "!="); 
	  }
  public UnidadLexica unidadPunto() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.PUNTO, "."); 
	  }
  public UnidadLexica unidadLlavesAp() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.LLAVESAP, "{"); 
	  }
  public UnidadLexica unidadLlavesCierre() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.LLAVESCIERRE, "}"); 
	  }
  public UnidadLexica unidadBarra() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.BARRA, "|"); 
	  }
  public UnidadLexica unidadPAp() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.PAP, "("); 
	  }
  public UnidadLexica unidadPCierre() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.PCIERRE, ")"); 
	  }
  public UnidadLexica unidadIgual() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.IGUAL, "="); 
	  }
  public UnidadLexica unidadDosPuntos() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.DOSPUNTOS, ":"); 
	  }
  public UnidadLexica unidadPuntoYComa() {
	     return new UnidadLexica(alex.fila(),alex.columna(),ClaseLexica.PUNTOYCOMA, ";"); 
	  }
  public UnidadLexica unidadCorcheteAp() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.CORCHETEAP, "["); 
	  }
  public UnidadLexica unidadCorcheteCierre() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.CORCHETECIERRE, "]"); 
	  }

  public UnidadLexica unidadComa() {
     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.COMA, ","); 
  }
  public UnidadLexica unidadAmpersand() {
	     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.AMPERSAND, ","); 
	  }
  public UnidadLexica unidadEof() {
     return new UnidadLexica(alex.fila(),alex.columna(), ClaseLexica.EOF, "<EOF>"); 
  }
  public void error() {
	  GestionErroresTiny.errorLexico(alex.fila(), alex.columna(), alex.lexema());
  }

}
