package alex;

import errors.GestionErroresTiny;

%%
%cup
%line
%class AnalizadorLexicoTiny
%unicode
%public

%{
  private ALexOperations ops;
  private GestionErroresTiny errores;
  public String lexema() {return yytext();}
  public int fila() {return yyline+1;}
  public void fijaGestionErrores(GestionErroresTiny errores) {
   this.errores = errores;
  }
%}

%eofval{
  return ops.unidadEof();
%eofval}

%init{
  ops = new ALexOperations(this);
%init}

letra  = ([A-Z]|[a-z])
digitoPositivo = [1-9]
digito = ({digitoPositivo}|0)
parteEntera = {digitoPositivo}{digito}*
parteDecimal = {digito}* {digitoPositivo}
separador = [ \t\r\b\n]
comentario = //[^\n\r\rn]* 
identificador = {letra}({letra}|{digito})*
numeroEntero = [\+,\-]?{parteEntera}
numeroReal = [\+,\-]?{parteEntera}\.{parteDecimal}
operadorSuma = \+
operadorResta = \-
operadorMultiplicacion = \*
operadorDivision = /
parentesisApertura = \(
parentesisCierre = \)
igual = \=
coma  = \,
print = print
lfilter = lfilter
lmap = lmap
distinto = !=
mayor = >
menor = <
igualigual = ==
concat = #
corcheteAbre = \[
corcheteCierra = \]
%%
{separador}               {}
{comentario}              {}
{identificador}           {return ops.unidadId();}
{numeroEntero}            {return ops.unidadEnt();}
{operadorSuma}            {return ops.unidadSuma();}
{operadorResta}           {return ops.unidadResta();}
{operadorMultiplicacion}  {return ops.unidadMul();}
{operadorDivision}        {return ops.unidadDiv();}
{parentesisApertura}      {return ops.unidadPAp();}
{parentesisCierre}        {return ops.unidadPCierre();} 
{igual}                   {return ops.unidadIgual();} 
{coma}                    {return ops.unidadComa();}
{print}					  {return ops.unidadPrint();}
{lfilter}				  {return ops.unidadLfilter();}
{lmap}					  {return ops.unidadLmap();}
{distinto}                {return ops.unidadDistinto();}
{mayor}					  {return ops.unidadMayor();}
{menor}					  {return ops.unidadMenor();}
{igualigual}              {return ops.unidadIgualigual();}
{concat}                  {return ops.unidadConcat();}
{corcheteAbre}            {return ops.unidadCorcheteAbre();}
{corcheteCierra}          {return ops.unidadCorcheteCierra();}
[^]                       {errores.errorLexico(fila(),lexema());}  