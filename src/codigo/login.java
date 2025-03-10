package codigo;

public class login {

	private int id_usuarios;
	private String nombre_usuarios;
	private String contraseña_usuarios;
	private String rol;
	
	public login() {}

	public login(int id_usuarios, String nombre_usuarios, String contraseña_usuarios, String rol) {
		super();
		this.id_usuarios = id_usuarios;
		this.nombre_usuarios = nombre_usuarios;
		this.contraseña_usuarios = contraseña_usuarios;
		this.rol = rol;
	}

	public int getId_usuarios() {
		return id_usuarios;
	}

	public void setId_usuarios(int id_usuarios) {
		this.id_usuarios = id_usuarios;
	}

	public String getNombre_usuarios() {
		return nombre_usuarios;
	}

	public void setNombre_usuarios(String nombre_usuarios) {
		this.nombre_usuarios = nombre_usuarios;
	}

	public String getContraseña_usuarios() {
		return contraseña_usuarios;
	}

	public void setContraseña_usuarios(String contraseña_usuarios) {
		this.contraseña_usuarios = contraseña_usuarios;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}


}