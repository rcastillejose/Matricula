package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import baseDeDatos.Modelo;
import vista.JFAdministrador;
import vista.JFLogin;

public class ControladorAdministrador implements ActionListener, MouseListener, WindowListener {
	private JFAdministrador jfad;
	private Modelo modelo;
	private JFLogin vista;

	public ControladorAdministrador(JFLogin vista, JFAdministrador jfad, Modelo modelo) {
		this.jfad = jfad;
		this.modelo = modelo;
		this.vista = vista;
		inicializar();
	}

	private void inicializar() {

		jfad.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jfad.addWindowListener(this);

		// Añadir las acciones a los botones del formulario padre

		// Ponemos a escuchar las acciones del usuario

	}

	public void go() {
		jfad.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comando = e.getActionCommand();

		if (comando.equals("Add")) {

		} else if (comando.equals("Delete")) {

		} else if (comando.equals("Actualizar")) {

		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Cerrando");
		vista.setVisible(true);
		jfad.dispose();

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
