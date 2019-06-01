package modelo;
/**
 * 
 * creado el 28 may. 2019
 * @author raul
 *
 */
public class Alumno{

	private String email;
	private String nombre;
	private String apellidos;
	private String login;

	/**
	 * 
	 * @param email
	 * @param nombre
	 * @param apellidos
	 * @param login
	 */
	public Alumno(String email, String nombre, String apellidos, String login) {
		this.email = email;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.login = login;
	}


	/**
	 * @return el parámetro email
	 */
	public String getEmail() {
		return email;
	}



	/**
	 * @param email Pasamos el parametro email para cambiarlo
	 */
	public void setEmail(String email) {
		this.email = email;
	}



	/**
	 * @return el parámetro nombre
	 */
	public String getNombre() {
		return nombre;
	}



	/**
	 * @param nombre Pasamos el parametro nombre para cambiarlo
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	/**
	 * @return el parámetro apellidos
	 */
	public String getApellidos() {
		return apellidos;
	}



	/**
	 * @param apellidos Pasamos el parametro apellidos para cambiarlo
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}



	/**
	 * @return el parámetro login
	 */
	public String getLogin() {
		return login;
	}



	/**
	 * @param login Pasamos el parametro login para cambiarlo
	 */
	public void setLogin(String login) {
		this.login = login;
	}



	@Override
	public boolean equals(Object o) {
		if(o==null) {
			return false;
		}else if( this.getEmail().compareTo(((Alumno)o).getEmail())==0) {
			return true;
		}else return false;
	}
	

	/* (non-Javadoc)
	 * se sobreescribirá el metodo
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Alumno [email=" + email + ", nombre=" + nombre + ", apellidos=" + apellidos + ", login=" + login + "]";
	}

}
