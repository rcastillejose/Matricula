package modelo;


public abstract class Persona {
	
	protected String documento;
	protected String nombre;
	protected String apellidos;	
	protected String mail;	
	protected String uid;
	protected int gidNumber;
	protected int uidNumber;
	protected String fechaNacimiento;
	protected String password;
	protected byte[] foto;
	
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Persona() {
		super();
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getGidNumber() {
		return gidNumber;
	}

	public void setGidNumber(int gidNumber) {
		this.gidNumber = gidNumber;
	}

	public int getUidNumber() {
		return uidNumber;
	}

	public void setUidNumber(int uidNumber) {
		this.uidNumber = uidNumber;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String toString() {
		return "documento=" + documento + ", \nnombre=" + nombre + ", \napellidos=" + apellidos + ", \nemail="
				+ mail + ", \nuid=" + uid + ", \ngidNumber=" + gidNumber + ", \nuidNumber=" + uidNumber
				+ ", \nfechaNacimiento=" + fechaNacimiento ;
	}
	
	public abstract String obtenerIdentificacion();
	public abstract void establecerIdentificacion(String identificacion);
	
	
}
