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

separador = [ \t\r\b\n]
comentarioCorto =  \/\/[^\n]* 
comentarioLargo = #[^#]*
comentario = ({comentarioLargo}|{comentarioCorto})
int = int
bool = bool
float = float
char = char
true = true
false = false
while = while
if = if
else = else
for = for
return = return
void = void
struct = struct
switch = switch
case = case
break = break
default = default
punto = \.
dospuntos = \:
puntoycoma = \;
numeroEntero = ({parteEntera}|0)
numeroReal = {parteEntera} \, {parteDecimal}
caracter = '[^\n]'
igual = \=
coma  = \,
id = {letra}({letra}|{digito})*
parteEntera = {digitoPositivo}{digito}*
parteDecimal = {digito}* {digitoPositivo}
letra  = ([A-Z]|[a-z])
digitoPositivo = [1-9]
digito = ({digitoPositivo}|0)
corcheteApertura = \[
corcheteCierre = \]
parentesisApertura = \(
parentesisCierre = \)
llaveApertura = \{
llaveCierre = \}
operadorSuma = \+
operadorResta = \-
operadorMultiplicacion = \*
operadorDivision = /
operadorModulo = \%
operadorMayor = >
operadorMenor = <
operadorMayorIgual = >=
operadorMenorIgual = <=
operadorIgualIgual = ==
operadorDistinto = !=
operadorNegacion = !
operadorAnd = &&
operadorOr = \|\|

%%


{separador}					{}
{comentario}				{}
{int}						{return ops.unidadInt();}
{bool}                      {return ops.unidadBool();}
{float}                     {return ops.unidadFloat();}
{char}                      {return ops.unidadChar();}
{true}                      {return ops.unidadTrue();}
{false}                     {return ops.unidadFalse();}
{while}                     {return ops.unidadWhile();}
{if}                        {return ops.unidadIf();}
{else}                      {return ops.unidadElse();}
{for}                       {return ops.unidadFor();}
{return}                    {return ops.unidadReturn();}
{void}                      {return ops.unidadVoid();}
{struct}                    {return ops.unidadStruct();}
{switch}                    {return ops.unidadSwitch();}
{case}                      {return ops.unidadCase();}
{break}                     {return ops.unidadBreak();}
{default}                   {return ops.unidadDefault();}
{punto}						{return ops.unidadPunto();}
{dospuntos}                 {return ops.unidadDospuntos();}
{puntoycoma}                {return ops.unidadPuntoycoma();}
{numeroEntero}              {return ops.unidadNumeroEntero();}
{numeroReal}                {return ops.unidadNumeroReal();}
{caracter}					{return ops.unidadCaracter();}
{coma}                      {return ops.unidadComa();}
{id}                        {return ops.unidadId();}
{corcheteApertura}          {return ops.unidadCorcheteApertura();}
{corcheteCierre}            {return ops.unidadCorcheteCierre();}
{parentesisApertura}        {return ops.unidadParentesisApertura();}
{parentesisCierre}          {return ops.unidadParentesisCierre();}
{llaveApertura}				{return ops.unidadLlaveApertura();}
{llaveCierre}				{return ops.unidadLlaveCierre();}
{operadorSuma}              {return ops.unidadSuma();}
{operadorResta}             {return ops.unidadResta();}
{operadorMultiplicacion}    {return ops.unidadMultiplicacion();}
{operadorDivision}          {return ops.unidadDivision();}
{operadorModulo}            {return ops.unidadModulo();}
{operadorMayor}             {return ops.unidadMayor();}
{operadorMenor}             {return ops.unidadMenor();}
{operadorMayorIgual}        {return ops.unidadMayorIgual();}
{operadorMenorIgual}        {return ops.unidadMenorIgual();}
{operadorIgualIgual}        {return ops.unidadIgualIgual();}
{operadorDistinto}          {return ops.unidadDistinto();}
{operadorNegacion}          {return ops.unidadNegacion();}
{operadorAnd}               {return ops.unidadAnd();}
{operadorOr}                {return ops.unidadOr();}
{igual}                     {return ops.unidadIgual();}
[^]							{ops.error();}  