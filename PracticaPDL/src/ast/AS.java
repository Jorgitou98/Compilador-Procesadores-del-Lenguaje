package ast;

import java.util.ArrayList;
import java.util.List;

public class AS {
	public E and(E opnd1, E opnd2, boolean asignable) {return new And(opnd1,opnd2, asignable);}
	public E caracter(String v, boolean asignable) {return new Caracter(v, asignable);}
	public E corchetes(E opnd1, E opnd2, boolean asignable) {return new Corchetes(opnd1,opnd2, asignable);}
	public E distinto(E opnd1, E opnd2, boolean asignable) {return new Distinto(opnd1,opnd2, asignable);}
	public E divEnt(E opnd1, E opnd2, boolean asignable) {return new DivEnt(opnd1,opnd2, asignable);}
	public E divReal(E opnd1, E opnd2, boolean asignable) {return new DivReal(opnd1,opnd2, asignable);}
	public E ent(String v, boolean asignable) {return new Ent(v, asignable);}
	public E falso(boolean asignable) {return new False(asignable);}
	public E iden(String v, boolean asignable) {return new Iden(v, asignable);}
	public E igualIgual(E opnd1, E opnd2, boolean asignable) {return new IgualIgual(opnd1,opnd2, asignable);}
	public E llamadaFun(E iden, List<E> arg, boolean asignable) {return new LlamadaFun(iden,arg, asignable);}
	public E mayor(E opnd1, E opnd2, boolean asignable) {return new Mayor(opnd1,opnd2, asignable);}
	public E mayorIgual(E opnd1, E opnd2, boolean asignable) {return new MayorIgual(opnd1,opnd2, asignable);}  
	public E menor(E opnd1, E opnd2, boolean asignable) {return new Menor(opnd1,opnd2, asignable);}
	public E menorIgual(E opnd1, E opnd2, boolean asignable) {return new MenorIgual(opnd1,opnd2, asignable);}
	public E modulo(E opnd1, E opnd2, boolean asignable) {return new Modulo(opnd1,opnd2, asignable);}
	public E mul(E opnd1, E opnd2, boolean asignable) {return new Mul(opnd1,opnd2, asignable);}
	public E not(E opnd1, boolean asignable) {return new Not(opnd1, asignable);}
	public E or(E opnd1, E opnd2, boolean asignable) {return new Or(opnd1,opnd2, asignable);}
	public E punto(E opnd1, E opnd2, boolean asignable) {return new Punto(opnd1,opnd2, asignable);}
	public E real(String v, boolean asignable) {return new Real(v, asignable);}
	public E resta(E opnd1, E opnd2, boolean asignable) {return new Resta(opnd1,opnd2, asignable);}
	public E restaUnaria(E opnd1, boolean asignable) {return new RestaUnaria(opnd1, asignable);}
	public E size(E opnd1, boolean asignable) {return new Size(opnd1, asignable);}
	public E suma(E opnd1, E opnd2, boolean asignable) {return new Suma(opnd1,opnd2, asignable);}
	public E sumaUnaria(E opnd1, boolean asignable) {return new SumaUnaria(opnd1, asignable);}
	public E verdadero(boolean asignable) {return new True(asignable);}
	public E vector(E valorIni, E tam, boolean asignable) {return new Vector(valorIni, tam, asignable);}
	public Ins insIfConElse(E cond, P insIf, P insElse) {return new InsCond(cond, insIf, insElse);}
	public Ins insIfSinElse(E cond, P insIf) {return new InsCond(cond, insIf);}
	public Ins insWhile(E cond, P ins) {return new InsWhile(cond, ins);}
	public Ins insFor(Ins decIni, E cond, E paso, P ins) {return new InsFor(decIni, cond, paso, ins);}
	public Ins insDec(Tipos tipo, E var, boolean conValorIni, E valorIni) {return new InsDec(tipo, var, conValorIni, valorIni);}
	public Ins insAsig(E var, E valor) { return new  InsAsig(var, valor); }
	public Ins insCall(E iden, List<E> argumentos) {return new InsCall(iden, argumentos); }
  	public Ins insSwitch(E varSwitch, List<Case> lista) {return new InsSwitch(varSwitch, lista); }
	public Ins insFun(Tipos tipoReturn, E nombre, List<Param> parametros, P instr, E valorReturn) { return new InsFun(tipoReturn, nombre, parametros, instr, valorReturn); }
  	public Ins insProc(E nombre, List<Param> parametros, P instr) { return new InsProc(nombre, parametros, instr); }
	public P programa() {return new P();}
  	public Tipos tipoInt() {return new TipoInt();}
  	public Tipos tipoBool() {return new TipoBool();}
  	public Tipos tipoChar() {return new TipoChar();}
  	public Tipos tipoFloat() {return new TipoFloat();}
  	public Tipos tipoVector(Tipos tipo) {return new TipoVector(tipo);}
  	public Tipos tipoUsuario(String nombre) {return new TipoUsuario(nombre);}
	public Case createCase(E var, P instr) { return new Case(var, instr); }
	public Param param(Tipos tipo, TipoParam tipoDeParam, E iden) { return new Param(tipo, tipoDeParam, iden); }
	public InsStruct insStruct(E nombreTipo, List<Ins> declaraciones) {return new InsStruct(nombreTipo, declaraciones);}
	public InsEnum insEnum(E nombre, List<E> listaConstantes) {return new InsEnum(nombre, listaConstantes);}
	public InsTypeDef insTypeDef(Tipos tipo, E nombreNuevo) {return new InsTypeDef(tipo, nombreNuevo);}
	
	
	
	
	
	
	
}
