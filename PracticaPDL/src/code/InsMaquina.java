package code;

public class InsMaquina {
	private String name;
	private TipoInsMaquina tipo;
	public InsMaquina(String name, TipoInsMaquina tipo) {
		super();
		this.name = name;
		this.tipo = tipo;
	}
	public String getName() {
		return name;
	}
	public TipoInsMaquina getTipo() {
		return tipo;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTipo(TipoInsMaquina tipo) {
		this.tipo = tipo;
	}
	
	
}
