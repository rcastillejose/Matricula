package modelo;

public class Profesor extends Persona {

	private String tutor;
	private String telefono1;
	private String telefono2;
	private String cargo;

	public String getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public Profesor() {
		super();
	}

	public void setTutor(String tutor) {
		this.tutor = tutor;
	}

	public String getTutor() {
		return tutor;
	}

	@Override
	public boolean equals(Object o) {
		boolean iguales = false;
		try {

			if (o == null) {
				iguales = false;
			} else if (((Profesor) o).getDocumento().compareTo(this.getDocumento()) == 0) {
				iguales = true;
			} else
				iguales = false;
		} catch (NullPointerException ne) {
			if (((Profesor) o).getDocumento() == null || this.getDocumento() == null)
				System.out.println("No establecido el DNI.");
			iguales = false;
		}
		return iguales;
	}

	@Override
	public String toString() {
		return "\nProfesor [\n" + super.toString() + ", \ntutor=" + tutor + ", \ntelefono1=" + telefono1 + ", \ntelefono2="
				+ telefono2 + ", \ncargo=" + cargo + "\n]";
	}

	@Override
	public String obtenerIdentificacion() {
		// TODO Auto-generated method stub
		return documento;
	}

	@Override
	public void establecerIdentificacion(String identificacion) {
		this.documento=identificacion;		
	}

}
