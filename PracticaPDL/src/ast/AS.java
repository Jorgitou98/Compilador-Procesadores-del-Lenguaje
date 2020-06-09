package ast;

import java.util.ArrayList;
import java.util.List;

public class AS {
	
	public E and(E opnd1, E opnd2, boolean asignable, int fila, int columna)
	{ return new And(opnd1,opnd2, asignable, fila, columna);}
	
	public E caracter(String v, boolean asignable, int fila, int columna)
	{return new Caracter(v, asignable, fila, columna);}
	
	public E corchetes(E opnd1, E opnd2, boolean asignable, int fila, int columna)
	{return new Corchetes(opnd1,opnd2, asignable, fila, columna);}
	
	public E distinto(E opnd1, E opnd2, boolean asignable, int fila, int columna) 
	{return new Distinto(opnd1,opnd2, asignable, fila, columna);}
	
	public E divEnt(E opnd1, E opnd2, boolean asignable, int fila, int columna)
	{return new DivEnt(opnd1,opnd2, asignable, fila, columna);}
	
	public E divReal(E opnd1, E opnd2, boolean asignable, int fila, int columna) 
	{return new DivReal(opnd1,opnd2, asignable, fila, columna);}
	
	public E ent(String v, boolean asignable, int fila, int columna)
	{return new Ent(v, asignable, fila, columna);}
	
	public E falso(boolean asignable, int fila, int columna)
	{return new False(asignable, fila, columna);}
	
	public E iden(String v, boolean asignable, int fila, int columna) 
	{return new Iden(v, asignable, fila, columna);}
	
	public E igualIgual(E opnd1, E opnd2, boolean asignable, int fila, int columna)
	{return new IgualIgual(opnd1,opnd2, asignable, fila, columna);}
	
	public E llamadaFun(E iden, List<E> arg, boolean asignable, int fila, int columna) 
	{return new LlamadaFun(iden,arg, asignable, fila, columna);}
	
	public E mayor(E opnd1, E opnd2, boolean asignable, int fila, int columna)
	{return new Mayor(opnd1,opnd2, asignable, fila, columna);}
	
	public E mayorIgual(E opnd1, E opnd2, boolean asignable, int fila, int columna) 
	{return new MayorIgual(opnd1,opnd2, asignable, fila, columna);}  
	
	public E menor(E opnd1, E opnd2, boolean asignable, int fila, int columna)
	{return new Menor(opnd1,opnd2, asignable, fila, columna);}
	
	public E menorIgual(E opnd1, E opnd2, boolean asignable, int fila, int columna)
	{return new MenorIgual(opnd1,opnd2, asignable, fila, columna);}
	
	public E modulo(E opnd1, E opnd2, boolean asignable, int fila, int columna)
	{return new Modulo(opnd1,opnd2, asignable, fila, columna);}
	
	public E mul(E opnd1, E opnd2, boolean asignable, int fila, int columna) 
	{return new Mul(opnd1,opnd2, asignable, fila, columna);}
	
	public E not(E opnd1, boolean asignable, int fila, int columna) 
	{return new Not(opnd1, asignable, fila, columna);}
	
	public E or(E opnd1, E opnd2, boolean asignable, int fila, int columna)
	{return new Or(opnd1,opnd2, asignable, fila, columna);}
	
	public E punto(E opnd1, E opnd2, boolean asignable, int fila, int columna) 
	{return new Punto(opnd1,opnd2, asignable, fila, columna);}
	
	public E real(String v, boolean asignable, int fila, int columna) 
	{return new Real(v, asignable, fila, columna);}
	
	public E resta(E opnd1, E opnd2, boolean asignable, int fila, int columna) 
	{return new Resta(opnd1,opnd2, asignable, fila, columna);}
	
	public E restaUnaria(E opnd1, boolean asignable, int fila, int columna) 
	{return new RestaUnaria(opnd1, asignable, fila, columna);}
	
	public E accesoPuntero(E opnd1, boolean asignable, int fila, int columna)
	{return new AccesoPuntero(opnd1, asignable, fila, columna);}
	
	public E size(E opnd1, boolean asignable, int fila, int columna)
	{return new Size(opnd1, asignable, fila, columna);}
	
	public E suma(E opnd1, E opnd2, boolean asignable, int fila, int columna) 
	{return new Suma(opnd1,opnd2, asignable, fila, columna);}
	
	public E sumaUnaria(E opnd1, boolean asignable, int fila, int columna) 
	{return new SumaUnaria(opnd1, asignable, fila, columna);}
	
	public E verdadero(boolean asignable, int fila, int columna) 
	{return new True(asignable, fila, columna);}
	
	public E vector(E valorIni, E tam, boolean asignable, int fila, int columna) 
	{return new Vector(valorIni, tam, asignable, fila, columna);}
	
	public E nuevo(List<Integer> lista, Tipos tipo, boolean asignable, int fila, int columna)
	{return new New(lista, tipo, asignable, fila, columna);}
	
	public Ins insIfConElse(E cond, P insIf, P insElse, int fila, int columna) 
	{return new InsCond(cond, insIf, insElse, fila, columna);}
	
	public Ins insIfSinElse(E cond, P insIf, int fila, int columna)
	{return new InsCond(cond, insIf, fila, columna);}
	
	public Ins insWhile(E cond, P ins, int fila, int columna) 
	{return new InsWhile(cond, ins, fila, columna);}
	
	public Ins insFor(Ins decIni, E cond, Ins paso, P ins, int fila, int columna) 
	{return new InsFor(decIni, cond, paso, ins, fila, columna);}
	
	public Ins insDec(Tipos tipo, E var, boolean conValorIni, E valorIni, int fila, int columna)
	{return new InsDec(tipo, var, conValorIni, valorIni, fila, columna);}
	
	public Ins insAsig(E var, E valor, int fila, int columna)
	{ return new  InsAsig(var, valor, fila, columna); }
	
	public Ins insCall(E iden, List<E> argumentos, int fila, int columna) 
	{return new InsCall(iden, argumentos, fila, columna); }
	
  	public Ins insSwitch(E varSwitch, List<Case> lista, int fila, int columna) 
  	{return new InsSwitch(varSwitch, lista, fila, columna); }
  	
	public Ins insFun(Tipos tipoReturn, E nombre, List<Param> parametros, P instr, E valorReturn, int fila, int columna)
	{ return new InsFun(tipoReturn, nombre, parametros, instr, valorReturn, fila, columna); }
	
  	public Ins insProc(E nombre, List<Param> parametros, P instr, int fila, int columna) 
  	{ return new InsProc(nombre, parametros, instr, fila, columna); }
  	
	public Ins insStruct(E nombreTipo, List<Ins> declaraciones, int fila, int columna) 
	{return new InsStruct(nombreTipo, declaraciones, fila, columna);}
	
	public Ins insEnum(E nombre, List<E> listaConstantes, int fila, int columna)
	{return new InsEnum(nombre, listaConstantes, fila, columna);}
	
	public Ins insTypeDef(Tipos tipo, E nombreNuevo, int fila, int columna)
	{return new InsTypeDef(tipo, nombreNuevo, fila, columna);}
  	
	public P programa()
	{return new P();}
	
  	public Tipos tipoInt() 
  	{return new TipoInt();}
  	
  	public Tipos tipoBool() 
  	{return new TipoBool();}
  	
  	public Tipos tipoChar() 
  	{return new TipoChar();}
  	
  	public Tipos tipoFloat() 
  	{return new TipoFloat();}
  	
  	public Tipos tipoPuntero(Tipos tipo, int fila, int columna) 
  	{return new TipoPuntero(tipo, fila, columna);}
  	
  	public Tipos tipoVector(Tipos tipo, int fila, int columna) 
  	{return new TipoVector(tipo, fila, columna);}
  	
  	public Tipos tipoUsuario(String nombre, int fila, int columna) 
  	{return new TipoUsuario(nombre, fila, columna);}
  	
	public Case createCase(E var, P instr, int fila, int columna) 
	{ return new Case(var, instr, fila, columna); }
	
	public Param param(Tipos tipo, TipoParam tipoDeParam, E iden, int fila, int columna) 
	{ return new Param(tipo, tipoDeParam, iden, fila, columna); }

	
	
	
	
	
	
}
