package controlador;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import baseDeDatos.Modelo;
import configuracion.LDAP;
import modelo.Alumno;
import modelo.Profesor;
import vista.JFAbout;
import vista.JFAdministrador;
import vista.JFLogin;
import vista.JFRegistro;
import vista.JFReservar;

/**
 * La clase Controlador se encarga de lanzar la ventana del login, validar los
 * usuarios y redirigirlos a sus respectivos frames según el usuario que sea
 * creado el 9 jun. 2019
 * 
 * @author raul
 *
 */
public class Controlador implements ActionListener {

	private JFLogin vista;
	private Modelo modelo;

	// formularios hijo
	JFAdministrador jfad;
	JFRegistro jfreg;
	JFReservar jfres;

	/**
	 * Constructor principal
	 * 
	 * @param vista  es el frame del login
	 * @param modelo se encargará de realizar las conexiones con la base de datos
	 */
	public Controlador(JFLogin vista, Modelo modelo) {
		this.vista = vista;
		this.modelo = modelo;
		iniciar();
	}

	/**
	 * Pone al frame a escuchar las acciones que se realizan en el frame
	 */
	public void iniciar() {

		vista.setTitle("Proyecto Matriculas");

		// Añadir las acciones a los botones del formulario padre
		vista.btnAcceder.setActionCommand("acceso");
		vista.btnCancelar.setActionCommand("cancelar");
		vista.btnRegistro.setActionCommand("registro");
		vista.btAbout.setActionCommand("about");

		// Ponemos a escuchar las acciones del usuario
		vista.btnAcceder.addActionListener(this);
		vista.btnCancelar.addActionListener(this);
		vista.btnRegistro.addActionListener(this);
		vista.btAbout.addActionListener(this);
	}

	/**
	 * Hace visible el frame
	 */
	public void start() {
		vista.setVisible(true);
	}

	/**
	 * Indica las consecuencias sobre las acciones de la ventana
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		String comando = e.getActionCommand();

		if (comando.equals("acceso")) {
			comprobarLogin();
		} else if (comando.equals("cancelar")) {
			deseaSalir();
		} else if (comando.equals("registro")) {
			abrirFormularioRegistro();
		} else if (comando.equals("about")) {
			abrirAbout();
		}
	}

	/**
	 * Metodo que se ejecutará cuando un usuario pulse al botón de salir para
	 * confirmar la opción
	 */
	private void deseaSalir() {
		int opcion = JOptionPane.showConfirmDialog(vista, "¿De verdad desea salir?", "Confirmacion",
				JOptionPane.YES_NO_OPTION);
		if (opcion == JOptionPane.YES_OPTION) {
			vista.dispose();
		}

	}

	/**
	 * Abre el Frame en el que se realizan los registros de alumnos
	 */
	private void abrirFormularioRegistro() {
		jfreg = new JFRegistro();
		Dimension deskSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ifSize = jfreg.getSize();
		jfreg.setLocation((deskSize.width - ifSize.width) / 2, (deskSize.height - ifSize.height) / 2);
		vista.setVisible(false);
		vista.setLocationRelativeTo(null);
		ControladorRegistro controlReg = new ControladorRegistro(vista, modelo, jfreg);
		controlReg.go();
	}

	/**
	 * Abre el Frame en el que se realizarán las reservas por los alumnos
	 * 
	 * @param a Alumno que ha accedido al frame
	 */
	private void abrirFormularioReservas(Alumno a) {
		JFReservar jfres = new JFReservar();
		Dimension deskSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ifSize = jfres.getSize();
		jfres.setLocation((deskSize.width - ifSize.width) / 2, (deskSize.height - ifSize.height) / 2);
		vista.setVisible(false);
		vista.setLocationRelativeTo(null);
		ControladorReservas controlRes = new ControladorReservas(vista, jfres, modelo, a);
		controlRes.go();
	}

	/**
	 * Abre el Frame del administrador en el que se realizarán las creaciones de
	 * periodos y reservas, asi como otras modificaciones
	 */
	private void abrirFormularioAdministrador() {
		JFAdministrador jfad = new JFAdministrador();
		Dimension deskSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ifSize = jfad.getSize();
		jfad.setLocation((deskSize.width - ifSize.width) / 2, (deskSize.height - ifSize.height) / 2);
		vista.setVisible(false);
		vista.setLocationRelativeTo(null);
		jfad.setVisible(true);
		ControladorAdministrador controlAd = new ControladorAdministrador(vista, jfad, modelo);
	}

	/**
	 * Valida el login del usuario como Profesor, alumno o usuario no valido
	 */
	private void comprobarLogin() {
		// Comprobamos si loguea siendo profesor
		LDAP ldap = new LDAP();
		String login = vista.tfUsuario.getText();
		String passwd = String.valueOf(vista.pFpasswd.getPassword());
		Alumno a;
		Profesor docente = new Profesor();
		docente.setUid(login);
		ldap.obtenerUsuarioLDAPByUID(docente);

		if (!hayCamposVacios()) {
			if (ldap.autenticacionLDAP(login, passwd)
					&& ldap.search("cn=Docente,ou=groups", "memberUid=" + docente.getUidNumber())) {
				abrirFormularioAdministrador();
			} else if (modelo.loguear(login, passwd) != null) {
				a = modelo.loguear(login, passwd);
				abrirFormularioReservas(a);
			} else {
				JOptionPane.showMessageDialog(vista,
						"El login no es correcto, si no te has equivocado registrate antes o prueba a loguearte con el e-mail",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Comprueba si hay campos vacios en el login
	 * 
	 * @return true si alguno de los campos no ha sido relleno
	 */
	private boolean hayCamposVacios() {
		boolean vacio = false;
		if (vista.tfUsuario.getText() == "" || String.valueOf(vista.pFpasswd.getPassword()) == "") {
			vacio = true;
			JOptionPane.showMessageDialog(vista, "Tienes que rellenar todos los campos", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return vacio;
	}

	/**
	 * Vacia los campos del login
	 */
	public void vaciarCampos() {
		vista.tfUsuario.setText("");
		vista.pFpasswd.setText("");
	}

	/**
	 * Abre el Frame de información sobre la aplicación
	 */
	public void abrirAbout() {
		JFAbout about = new JFAbout();
		about.setVisible(true);
	}

}
