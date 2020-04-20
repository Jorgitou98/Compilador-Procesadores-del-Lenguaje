package alex;
import asint.ClaseLexica;

public class ALexOperations {
  private AnalizadorLexicoTiny alex;
  public ALexOperations(AnalizadorLexicoTiny alex) {
   this.alex = alex;   
  }
  

  public UnidadLexica unidadInt() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.INT,
	                                         "int"); 
	  } 
  public UnidadLexica unidadBool() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.BOOL,
	                                         "bool"); 
	  } 
  public UnidadLexica unidadFloat() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.FLOAT,
	                                         "float"); 
	  } 
  public UnidadLexica unidadChar() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.CHAR,
	                                         "char"); 
	  } 
  public UnidadLexica unidadTrue() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.TRUE,
	                                         "true"); 
	  } 
  public UnidadLexica unidadFalse() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.FALSE,
	                                         "false"); 
	  } 
  public UnidadLexica unidadWhile() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.WHILE,
	                                         "while"); 
	  } 
  public UnidadLexica unidadIf() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.IF,
	                                         "if"); 
	  } 
  public UnidadLexica unidadElse() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.ELSE,
	                                         "else"); 
	  } 
  public UnidadLexica unidadFor() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.FOR,
	                                         "for"); 
	  } 
  public UnidadLexica unidadReturn() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.RETURN,
	                                         "return"); 
	  } 
  public UnidadLexica unidadVoid() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.VOID,
	                                         "void"); 
	  } 
  public UnidadLexica unidadStruct() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.STRUCT,
	                                         "struct"); 
	  } 
  public UnidadLexica unidadSwitch() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.SWITCH,
	                                         "switch"); 
	  } 
  public UnidadLexica unidadCase() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.CASE,
	                                         "case"); 
	  } 
  public UnidadLexica unidadBreak() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.BREAK,
	                                         "break"); 
	  } 
  public UnidadLexica unidadDefault() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.DEFAULT,
	                                         "default"); 
	  } 
  public UnidadLexica unidadPunto() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.PUNTO,
	                                         "."); 
	  } 
  public UnidadLexica unidadDospuntos() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.DOSPUNTOS,
	                                         ":"); 
	  } 
  public UnidadLexica unidadPuntoycoma() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.PUNTOYCOMA,
	                                         ";"); 
	  } 
  public UnidadLexica unidadNumeroEntero() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.ENTERO,
	    		 alex.lexema()); 
	  } 
  public UnidadLexica unidadNumeroReal() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.REAL, 
	    		 alex.lexema()); 
	  } 
  public UnidadLexica unidadCaracter() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.CARACTER, 
	    		 alex.lexema()); 
	  } 
  public UnidadLexica unidadComa() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.COMA,
	                                         ","); 
	  } 
  public UnidadLexica unidadId() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.IDEN,
	                                         alex.lexema()); 
	  } 
  public UnidadLexica unidadCorcheteApertura() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.CA,
	                                         "["); 
	  } 
  public UnidadLexica unidadCorcheteCierre() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.CC,
	                                         "]"); 
	  } 
  public UnidadLexica unidadParentesisApertura() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.PA,
	                                         "("); 
	  } 
  public UnidadLexica unidadParentesisCierre() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.PC,
	                                         ")"); 
	  } 
  public UnidadLexica unidadLlaveApertura() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.LLA,
	                                         "{"); 
	  } 
  public UnidadLexica unidadLlaveCierre() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.LLC,
	                                         "}"); 
	  } 
  public UnidadLexica unidadSuma() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.SUMA,
	                                         "+"); 
	  } 
  public UnidadLexica unidadResta() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.RESTA,
	                                         "-"); 
	  } 
  public UnidadLexica unidadMultiplicacion() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.MULT,
	                                         "*"); 
	  } 
  public UnidadLexica unidadDivision() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.DIV,
	                                         "/"); 
	  } 
  public UnidadLexica unidadModulo() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.MOD,
	                                         "%"); 
	  } 
  public UnidadLexica unidadMayor() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.MAYOR,
	                                         ">"); 
	  } 
  public UnidadLexica unidadMenor() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.MENOR,
	                                         "<"); 
	  } 
  public UnidadLexica unidadMayorIgual() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.MAI,
	                                         ">="); 
	  } 
  public UnidadLexica unidadMenorIgual() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.MEI,
	                                         "<="); 
	  } 
  public UnidadLexica unidadIgualIgual() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.IGIG,
	                                         "=="); 
	  } 
  public UnidadLexica unidadDistinto() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.DIST,
	                                         "!="); 
	  } 
  public UnidadLexica unidadNegacion() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.NEG,
	                                         "!"); 
	  } 
  public UnidadLexica unidadAnd() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.AND,
	                                         "&&"); 
	  } 
  public UnidadLexica unidadOr() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.OR,
	                                         "||"); 
	  } 
  public UnidadLexica unidadIgual() {
	     return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.IG,
	                                         alex.lexema()); 
	  }  
  public UnidadLexica unidadEof() {
	  	return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.EOF, "EOF"); 
  }
  public void error() {
    System.err.println("***"+alex.fila()+" Caracter inesperado: "+alex.lexema());
  }
}
