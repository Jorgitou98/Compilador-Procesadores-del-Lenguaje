package ast;

public class PuntosIzq extends CorchetesYPuntosIzq{
	private E id;
	
	

	public E getId() {
		return id;
	}



	public PuntosIzq(E id) {
		super("PuntosIzq", id);
		this.id = id;
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Punto( " + id.toString() + ")";
	}

}
