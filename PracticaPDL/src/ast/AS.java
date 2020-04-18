package ast;

import java.util.ArrayList;
import java.util.List;

public class AS {
	public E and(E opnd1, E opnd2) {return new And(opnd1,opnd2);}
	public E caracter(String v) {return new Caracter(v);}
	public E corchetes(E opnd1, E opnd2) {return new Corchetes(opnd1,opnd2);}
	public E distinto(E opnd1, E opnd2) {return new Distinto(opnd1,opnd2);}
	public E divEnt(E opnd1, E opnd2) {return new DivEnt(opnd1,opnd2);}
	public E divReal(E opnd1, E opnd2) {return new DivReal(opnd1,opnd2);}
	public E ent(String v) {return new Ent(v);}
	public E falso() {return new False();}
	public E iden(String v) {return new Iden(v);}
	public E igualIgual(E opnd1, E opnd2) {return new IgualIgual(opnd1,opnd2);}
	public E llamadaFun(E iden, List<E> arg) {return new LlamadaFun(iden,arg);}
	public E mayor(E opnd1, E opnd2) {return new Mayor(opnd1,opnd2);}
	public E mayorIgual(E opnd1, E opnd2) {return new MayorIgual(opnd1,opnd2);}  
	public E menor(E opnd1, E opnd2) {return new Menor(opnd1,opnd2);}
	public E menorIgual(E opnd1, E opnd2) {return new MenorIgual(opnd1,opnd2);}
	public E modulo(E opnd1, E opnd2) {return new Modulo(opnd1,opnd2);}
	public E mul(E opnd1, E opnd2) {return new Mul(opnd1,opnd2);}
	public E not(E opnd1) {return new Not(opnd1);}
	public E or(E opnd1, E opnd2) {return new Or(opnd1,opnd2);}
	public E punto(E opnd1, E opnd2) {return new Punto(opnd1,opnd2);}
	public E real(String v) {return new Real(v);}
	public E resta(E opnd1, E opnd2) {return new Resta(opnd1,opnd2);}
	public E restaUnaria(E opnd1) {return new RestaUnaria(opnd1);}
	public E size(E opnd1) {return new Size(opnd1);}
	public E suma(E opnd1, E opnd2) {return new Suma(opnd1,opnd2);}
	public E sumaUnaria(E opnd1) {return new SumaUnaria(opnd1);}
	public E verdadero() {return new True();}
	public E vector(E tam, E valorIni) {return new Vector(tam,valorIni);}
	public Ins insIfConElse(E cond, P insIf, P insElse) {return new InsCond(cond, insIf, insElse);}
	public Ins insIfSinElse(E cond, P insIf) {return new InsCond(cond, insIf);}
	public Ins insWhile(E cond, P ins) {return new InsWhile(cond, ins);}
	public Ins insFor(Ins decIni, E cond, E paso, P ins) {return new InsFor(decIni, cond, paso, ins);}
	public Ins insDec(Tipos tipo, E var, boolean conValorIni, E valorIni) {return new InsDec(tipo, var, conValorIni, valorIni);}
	public Ins insAsig(E var, List<CorchetesYPuntosIzq> cyp, E valor) { return new  InsAsig(var, cyp, valor); }
  	public P programa() {return new P();}
  	public Tipos tipoInt() {return new TipoInt();}
  	public Tipos tipoBool() {return new TipoBool();}
  	public Tipos tipoChar() {return new TipoChar();}
  	public Tipos tipoFloat() {return new TipoFloat();}
  	public Tipos tipoVector(Tipos tipo) {return new TipoVector(tipo);}
  	public Tipos tipoUsuario(String nombre) {return new TipoUsuario(nombre);}
  	public CorchetesYPuntosIzq corchetesIzq (E expr) {return new CorchetesIzq(expr); }
  	public CorchetesYPuntosIzq puntosIzq (E id) {return new PuntosIzq(id); }
	public FuncionOIden funcionOIden (boolean esIden, List<E> arg) {return new FuncionOIden ( esIden, arg);}
	public IfConElse ifConElse (boolean vieneConElse, P insElse) {return new IfConElse ( vieneConElse, insElse);}
	
	
	
	
	
	
	
}
