package controlador;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;

import baseDeDatos.Modelo;
import modelo.Alumno;

import vista.JFAdministrador;
import vista.JFLogin;
import vista.JFReservar;

public class ControladorReservas implements WindowListener {
	private JFReservar jfre ;
	private Modelo modelo;
	private JFLogin vista;
	private Alumno a;
	
	public ControladorReservas(JFLogin vista, JFReservar jfre, Modelo modelo, Alumno a) {
		this.jfre = jfre;
		this.modelo = modelo;
		this.vista = vista;
		this.a=a;
		inicializar();
	}
	private void inicializar() {
		jfre.setVisible(true);
		jfre.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jfre.addWindowListener(this);
		actualizarComboboxCursos();
		// Anadir las acciones a los botones del formulario padre

		// Ponemos a escuchar las acciones del usuario

	}

	public void go() {
		jfre.setVisible(true);
	}
	
	private void actualizarComboboxCursos() {
		String curso;
		String aux;
		DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
		dcbm.removeAllElements();

		ArrayList<String> resultado = modelo.obtenerCursos();
		for (int i = 0; i<resultado.size();i++) {
			curso =resultado.get(i);
			aux = curso.substring(0,4);
			dcbm.addElement(aux);
		}
		jfre.cBCurso.setModel(dcbm);
		System.out.println(dcbm.getSelectedItem());
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Cerrando");
		vista.setVisible(true);
		jfre.dispose();
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
