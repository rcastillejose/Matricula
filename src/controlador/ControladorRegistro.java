package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import baseDeDatos.Modelo;
import configuracion.ConfiguracionSegura;
import gmail.GmailTool;
import modelo.Alumno;
import modelo.Password;
import vista.JFLogin;
import vista.JFRegistro;

/**
 * Es la clase que se encargará de gestionar los registros de los usuarios
 * creado el 9 jun. 2019
 * @author raul
 *
 */
public class ControladorRegistro implements ActionListener,WindowListener {

	private JFLogin vista;
	private Modelo modelo;
	private JFRegistro jfreg;
	
	/**
	 * Constructor de la clase
	 * @param vista es el Frame del login
	 * @param modelo el encargado de realizar las conexiones con la base de datos
	 * @param jfreg el frame de registro de los usuarios
	 */
	public ControladorRegistro(JFLogin vista,Modelo modelo, JFRegistro jfreg) {
		super();
		this.vista=vista;
		this.modelo = modelo;
		this.jfreg = jfreg;
		iniciar();
	}
	
	/**
	 * Inicia el frame de registro
	 */
	public void go() {
		jfreg.setVisible(true);
	}
	
	/**
	 * Pone a escuchar las acciones sobre el frame de registro
	 */
	public void iniciar() {
		jfreg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		jfreg.btnRegistro.setActionCommand("registrar");
		
		jfreg.btnRegistro.addActionListener(this);
		
		jfreg.addWindowListener(this);
	}

	/**
	 * Indica las consecuencias de las acciones que se realizan sobre el frame
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		
		if (command.equals("registrar")) {
			registrar();
		}
	}
	/**
	 * Indica la acción cuando se cierra la ventana
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		vista.setVisible(true);
		jfreg.dispose();
	}
	
	/**
	 * Se recoge la información de los campos y se realiza una inserción del nuevo alumno, enviando la información de acceso por correo
	 */
	public void registrar() {
		String nombre;
		String apellidos;
		String email;
		String login;
		Password pass=null;
		
		if (!camposVacios()) {
			nombre = jfreg.tFNombre.getText();
			apellidos = jfreg.tFApellido1.getText()+" "+jfreg.tFApellido2.getText();
			email = jfreg.tFEmail.getText();
			login = String.valueOf(nombre.charAt(0))+jfreg.tFApellido1.getText().toLowerCase();
			String passwd = pass.generarPassWord(pass.MAYUSCULAS+pass.MINUSCULAS+pass.NUMEROS, 8);
			Alumno a = new Alumno(email,nombre,apellidos,login);
			
			if(modelo.insertarAlumno(a, passwd)) {
				JOptionPane.showMessageDialog(vista, "Has sido registrado, revisa tu correo para la contraseña de acceso");
				
				String to = email;
				String from = (new ConfiguracionSegura()).getMailFrom();
				String subject = "Confirmación de cuenta";
				String body = "<h1>Bienvenido a IES LA VEREDA</h1><p>Buenas "+nombre+", tu cuenta ha sido creada con éxito.</p><p>Puedes acceder con tu correo o con el usuario: "+login+
						"</p><p>Tu contraseña de acceso es: "+passwd;
			
				GmailTool.sendHtml(to, from, subject, body);
			
			}
			
		} else {
			JOptionPane.showMessageDialog(vista, "Rellena todos los campos para continuar","Error",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	/**
	 * comprueba si los campos del Frame están vacios
	 * @return true si hay algún campo vacío
	 */
	public boolean camposVacios() {
		boolean vacio;
		int nombre = (jfreg.tFNombre.getText().length());
		int apellido =(jfreg.tFApellido1.getText().length());
		int email = (jfreg.tFEmail.getText().length());
		if(nombre<1  && apellido < 1 && email < 1) {
			vacio=true;	
		} else {
			vacio=false;
		}
				
		
		return vacio;
	}
	
	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
