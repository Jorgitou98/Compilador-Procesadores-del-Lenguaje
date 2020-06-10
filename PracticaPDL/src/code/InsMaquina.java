package code;

public class InsMaquina {
	private String name;
	private int tipo;
	private boolean comentario = false;
	public InsMaquina(String name, int tipo) {
		super();
		this.name = name;
		this.tipo = tipo;
	}
	public String getName() {
		return name;
	}
	public int getTipo() {
		return tipo;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public boolean isComentario() {
		return comentario;
	}
	public void setComentario(boolean comentario) {
		this.comentario = comentario;
	}
	
	
}
