package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
		actualizarComboboxCursos();
		jfad.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jfad.addWindowListener(this);

		// Añadir las acciones a los botones del panel de crear periodos
		jfad.btnAadirCurso.setActionCommand("addCurso");
		jfad.btnCrear.setActionCommand("addPeriodo");
		
		
		// Ponemos a escuchar las acciones del usuario del panel de crear periodos
		jfad.btnAadirCurso.addActionListener(this);
		jfad.btnCrear.addActionListener(this);

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comando = e.getActionCommand();

		if (comando.equals("addCurso")) {
			insertarCurso();
		} else if (comando.equals("addPeriodo")) {
			crearPeriodo();
		} else if (comando.equals("Actualizar")) {

		}
	}

	public void go() {
		jfad.setVisible(true);
	}
	
	// ****************************************CREAR PERIODO**********************************

	private void actualizarComboboxCursos() {
		String curso;
		String aux;
		DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
		dcbm.removeAllElements();

		LinkedHashSet<String> resultado = modelo.obtenerCursos();
		Iterator it = resultado.iterator();
		while(it.hasNext()) {
			curso =it.next().toString();
			aux = curso.substring(0,4);
			dcbm.addElement(aux);
		}
		jfad.cBCursos.setModel(dcbm);
		System.out.println(dcbm.getSelectedItem());
	}
	
	private void insertarCurso() {
		LinkedHashSet<String> resultado = modelo.obtenerCursos();
		Iterator it = resultado.iterator();
		int curso;
		curso = Integer.parseInt(it.next().toString())+1;
		if(modelo.insertarCurso(curso))
			actualizarComboboxCursos();
	}
	
	private boolean comprobarCampos() {
		boolean vacio = true;
		if (jfad.dPDiaInicio.getDate()!=null && jfad.dPDiaFin.getDate()!= null) {
			if(jfad.tPHoraIni.getTime()!=null && jfad.tPHoraFin.getTime()!=null) {
			vacio = false;
			}else {
				JOptionPane.showMessageDialog(vista, "Define todas las horas", "Error",JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(vista, "Define todos los días", "Error",JOptionPane.ERROR_MESSAGE);
		}
		return vacio;
	}
	
	private void crearPeriodo() {
		Date diaIni;//=Date.valueOf( diaini;
		Date diaFin;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm");
		LocalTime horaIni;//LocalTime.parse(
		LocalTime horaFin;
		LocalTime intervalo;
		String curso;
		if (!comprobarCampos()) {
			diaIni = Date.valueOf(jfad.dPDiaInicio.getDate());
			diaFin = Date.valueOf(jfad.dPDiaFin.getDate());
			horaIni = LocalTime.parse(jfad.tPHoraIni.getTime().toString(),dtf);
			horaFin = LocalTime.parse(jfad.tPHoraFin.getTime().toString(),dtf);
			if((int) jfad.spReservasMinutos.getValue()<10) {
				intervalo = LocalTime.parse("0:0"+jfad.spReservasMinutos.getValue(), dtf);
			} else
				intervalo = LocalTime.parse("0:"+jfad.spReservasMinutos.getValue(), dtf);
			curso = jfad.cBCursos.getSelectedItem().toString();
			
			
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
