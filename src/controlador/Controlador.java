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
import vista.JFAdministrador;
import vista.JFLogin;
import vista.JFRegistro;
import vista.JFReservar;


public class Controlador implements ActionListener {

	private JFLogin vista;
	private Modelo modelo;

	// formularios hijo
	JFAdministrador jfad;
	JFRegistro jfreg;
	JFReservar jfres;

	public Controlador(JFLogin vista, Modelo modelo) {
		this.vista = vista;
		this.modelo = modelo;
		iniciar();
	}

	public void iniciar() {

		
		vista.setTitle("Proyecto Matriculas");

		// Añadir las acciones a los botones del formulario padre
		vista.btnAcceder.setActionCommand("acceso");
		vista.btnCancelar.setActionCommand("cancelar");
		vista.btnRegistro.setActionCommand("registro");

		// Ponemos a escuchar las acciones del usuario
		vista.btnAcceder.addActionListener(this);
		vista.btnCancelar.addActionListener(this);
		vista.btnRegistro.addActionListener(this);
	}

	public void start() {
		vista.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		String comando = e.getActionCommand();

		if (comando.equals("acceso")) {
			comprobarLogin();
		} else if(comando.equals("cancelar")) {
			deseaSalir();
		} else if (comando.equals("registro")) {
			abrirFormularioRegistro();
		}
	}

	private void deseaSalir() {
		int opcion = JOptionPane.showConfirmDialog(vista, "¿De verdad desea salir?",
				"Confirmacion", JOptionPane.YES_NO_OPTION);
		if (opcion == JOptionPane.YES_OPTION) {
			vista.dispose();
		}
		
	}
	
	private void abrirFormularioRegistro() {
			jfreg = new JFRegistro();
			Dimension deskSize = Toolkit.getDefaultToolkit().getScreenSize() ;
			Dimension ifSize = jfreg.getSize();
			jfreg.setLocation((deskSize.width - ifSize.width) / 2, (deskSize.height - ifSize.height) /2);
			vista.setVisible(false);
			vista.setLocationRelativeTo(null);	
			ControladorRegistro controlReg = new ControladorRegistro(vista,modelo,jfreg);
			controlReg.go();
	}
	
	private void abrirFormularioReservas(Alumno a) {
		JFReservar jfres = new JFReservar();
		Dimension deskSize = Toolkit.getDefaultToolkit().getScreenSize() ;
		Dimension ifSize = jfres.getSize();
		jfres.setLocation((deskSize.width - ifSize.width) / 2, (deskSize.height - ifSize.height) /2);
		vista.setVisible(false);
		vista.setLocationRelativeTo(null);	
		ControladorReservas controlRes = new ControladorReservas(vista,jfres,modelo,a);
		controlRes.go();
	}
	
	
	private void abrirFormularioAdministrador() {
		JFAdministrador jfad = new JFAdministrador();
		Dimension deskSize = Toolkit.getDefaultToolkit().getScreenSize() ;
		Dimension ifSize = jfad.getSize();
		jfad.setLocation((deskSize.width - ifSize.width) / 2, (deskSize.height - ifSize.height) /2);
		vista.setVisible(false);
		vista.setLocationRelativeTo(null);	
		jfad.setVisible(true);
		ControladorAdministrador controlAd = new ControladorAdministrador(vista,jfad,modelo);
	}
	
	private void comprobarLogin() {
		// Comprobamos si loguea siendo profesor
		LDAP ldap = new LDAP();
		String login=vista.tfUsuario.getText();
		String passwd =String.valueOf(vista.pFpasswd.getPassword());
		Alumno a;
		Profesor docente = new Profesor();
		docente.setUid(login);
		ldap.obtenerUsuarioLDAPByUID(docente);
		
		
		if (!hayCamposVacios()) {
			if (ldap.autenticacionLDAP(login,passwd) && ldap.search("cn=Docente,ou=groups","memberUid="+docente.getUidNumber())) {
				abrirFormularioAdministrador();
			} else if (modelo.loguear(login, passwd)!=null  ) {
				a=modelo.loguear(login, passwd);
				abrirFormularioReservas(a);
			} else {
				JOptionPane.showMessageDialog(vista, "El login no es correcto, si no te has equivocado registrate antes o prueba a loguearte con el e-mail"
						, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
		
	private boolean hayCamposVacios() {
		boolean vacio = false;
		if(vista.tfUsuario.getText()=="" || String.valueOf(vista.pFpasswd.getPassword())=="") {
			vacio=true;
			JOptionPane.showMessageDialog(vista, "Tienes que rellenar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return vacio;
	}
	
	public void vaciarCampos() {
		vista.tfUsuario.setText("");
		vista.pFpasswd.setText("");
	}

}
