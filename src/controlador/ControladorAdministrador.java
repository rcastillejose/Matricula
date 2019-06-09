package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import baseDeDatos.Modelo;
import modelo.Periodo;
import modelo.Reserva;
import vista.JFAdministrador;
import vista.JFLogin;
import vista.JFReservasAdmin;
/**
 * Clase que se encarga de controlar todos los paneles del frame del administrador
 * creado el 9 jun. 2019
 * @author raul
 *
 */
public class ControladorAdministrador implements ActionListener, MouseListener, WindowListener {
	private JFAdministrador jfad;
	private Modelo modelo;
	private JFLogin vista;

	private Periodo per;

	/**
	 * Método constructor del controlador del administrador
	 * @param vista frame del login
	 * @param jfad frame del administrador
	 * @param modelo clase que controla los accesos a la base de datos
	 */
	public ControladorAdministrador(JFLogin vista, JFAdministrador jfad, Modelo modelo) {
		this.jfad = jfad;
		this.modelo = modelo;
		this.vista = vista;
		inicializar();
	}

	/**
	 * Metodo que inicializa las ventanas y pone a escuchar las distintas acciones
	 */
	private void inicializar() {
		actualizarComboboxCursos();
		actualizarTablaPeriodos();
		jfad.tPBody.setText(modelo.obtenerMensajeCorreo());
		jfad.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jfad.addWindowListener(this);
		jfad.lbModCurso.setVisible(false);

		// Añadir las acciones a los botones del panel de crear periodos
		jfad.btnAadirCurso.setActionCommand("addCurso");
		jfad.btnCrear.setActionCommand("addPeriodo");
		jfad.cBCursosMod.setActionCommand("actualizarTabla");
		jfad.btnModificar.setActionCommand("modificar");
		jfad.btnMostrarReservas.setActionCommand("reservasPeriodo");
		jfad.btnRealizarCambios.setActionCommand("actualizarCorreo");

		// Ponemos a escuchar las acciones del usuario del panel de crear periodos
		jfad.btnAadirCurso.addActionListener(this);
		jfad.btnCrear.addActionListener(this);
		jfad.cBCursosMod.addActionListener(this);
		jfad.btnModificar.addActionListener(this);
		jfad.btnMostrarReservas.addActionListener(this);
		jfad.btnRealizarCambios.addActionListener(this);
		
		jfad.tPeriodos.addMouseListener(this);
		
	}

	/**
	 * Indica las repercusiones de las acciones sobre el frame
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comando = e.getActionCommand();

		if (comando.equals("addCurso")) {
			insertarCurso();
		} else if (comando.equals("addPeriodo")) {
			crearPeriodo();
		} else if (comando.equals("actualizarTabla")) {
			actualizarTablaPeriodos();
		} else if (comando.equals("modificar")) {
			modificarPeriodo();
		} else if (comando.equals("reservasPeriodo")) {
			mostrarReservas();
		} else if (comando.equals("actualizarCorreo")) {
			actualizarMensajeEnvio();
		}
	}

	/**
	 * Pone el frame visible
	 */
	public void go() {
		jfad.setVisible(true);
	}

	// ****************************************CREACION DE PERIODOS**********************************

	/**
	 * Actualiza todos los combobox en los que se exponen los cursos
	 */
	private void actualizarComboboxCursos() {
		int curso;

		DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
		dcbm.removeAllElements();

		LinkedHashSet<Integer> resultado = modelo.obtenerCursos();
		Iterator it = resultado.iterator();
		while (it.hasNext()) {
			curso = (int) it.next();

			dcbm.addElement(curso);
		}
		jfad.cBCursos.setModel(dcbm);
		jfad.cBCursosMod.setModel(dcbm);
		System.out.println(dcbm.getSelectedItem());
	}

	/**
	 * Metodo que obtiene el último curso creado y añade uno nuevo
	 */
	private void insertarCurso() {
		LinkedHashSet<Integer> resultado = modelo.obtenerCursos();
		Iterator it = resultado.iterator();
		int curso = (int) it.next();

		int cursoINT = curso + 1;
		if (modelo.insertarCurso(cursoINT))
			actualizarComboboxCursos();
	}

	/**
	 * Comprueba que no hayan campos sin rellenar
	 * @return true si hay algún campo vacío
	 */
	private boolean comprobarCampos() {
		boolean vacio = true;
		if (jfad.dPDiaInicio.getDate() != null && jfad.dPDiaFin.getDate() != null) {
			if (jfad.tPHoraIni.getTime() != null && jfad.tPHoraFin.getTime() != null) {
				vacio = false;
			} else {
				JOptionPane.showMessageDialog(vista, "Define todas las horas", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(vista, "Define todos los días", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return vacio;
	}

	/**
	 * Método que recoge los datos de los campos y realiza una nueva inserción en la base de datos 
	 */
	private void crearPeriodo() {
		Date diaIni;
		Date diaFin;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm");
		Time horaIni;
		Time horaFin;
		Time intervalo;
		int curso;

		if (!comprobarCampos()) {
			diaIni = Date.valueOf(jfad.dPDiaInicio.getDate());
			diaFin = Date.valueOf(jfad.dPDiaFin.getDate());
			horaIni = Time.valueOf(LocalTime.parse(jfad.tPHoraIni.getTime().toString(), dtf));
			horaFin = Time.valueOf(LocalTime.parse(jfad.tPHoraFin.getTime().toString(), dtf));
			if ((int) jfad.spReservasMinutos.getValue() < 10) {
				intervalo = Time.valueOf(LocalTime.parse("0:0" + jfad.spReservasMinutos.getValue(), dtf));
			} else
				intervalo = Time.valueOf(LocalTime.parse("0:" + jfad.spReservasMinutos.getValue(), dtf));
			curso = Integer.parseInt(jfad.cBCursos.getSelectedItem().toString());
			System.out.println(diaIni.toString() + diaFin.toString() + horaIni.toString() + horaFin.toString()
					+ intervalo.toString() + curso);
			if (modelo.addPeriodo(diaIni, diaFin, horaIni, horaFin, intervalo, curso)) {
				JOptionPane.showMessageDialog(vista, "Periodo creado con exito", "Info",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	// ***********************************************************************************************************************************
	// ***************************************************MODIFICAR PERIODO***************************************************************
	// ***********************************************************************************************************************************

	/**
	 * Actualiza la tabla en la que aparecen los periodos
	 */
	private void actualizarTablaPeriodos() {
		Map<Integer, Periodo> periodos = modelo
				.obtenerPeriodos(Integer.parseInt(jfad.cBCursosMod.getSelectedItem().toString()));
		DefaultTableModel dtm = new DefaultTableModel(new Object[][] {},
				new String[] { "Periodo", "Dia inicio", "Dia fin", "Hora inicio", "Hora_fin", "Habilitado" });

		for (Integer key : periodos.keySet()) {

			dtm.addRow(new Object[] { key, periodos.get(key).getDia_inicio(), periodos.get(key).getDia_fin(),
					periodos.get(key).getHora_inicio(), periodos.get(key).getHora_fin(),
					periodos.get(key).getHabilitado() });
		}
		jfad.tPeriodos.setModel(dtm);
	}

	/**
	 * Recoge el periodo de la tabla y te rellena los campos para modificarlos
	 */
	private void rellenarCampos() {
		jfad.lbModCurso.setVisible(true);
		per = modelo.obtenerPeriodo(
				Integer.parseInt(jfad.tPeriodos.getValueAt(jfad.tPeriodos.getSelectedRow(), 0).toString()));
		jfad.dPDiaIniMod.setDate(per.getDia_inicio());
		jfad.dPDiaFinMod.setDate(per.getDia_fin());
		jfad.tPHoraInicioMod.setTime(per.getHora_inicio());
		jfad.tPHoraFinMod.setTime(per.getHora_fin());
		String intervalo = per.getIntervalo().toString();
		System.out.println(intervalo);
		int intervalo2 = Integer.parseInt(intervalo.substring(3));
		jfad.spIntervaloMod.setValue(intervalo2);
		if (per.getHabilitado())
			jfad.checkHabilitado.setSelected(true);
	}

	/**
	 * Método que comprueba que los campos modificados estén rellenos
	 * @return
	 */
	private boolean comprobarCamposMod() {
		boolean vacio = true;
		if (jfad.dPDiaIniMod.getDate() != null && jfad.dPDiaFinMod.getDate() != null) {
			if (jfad.tPHoraInicioMod.getTime() != null && jfad.tPHoraFinMod.getTime() != null) {
				vacio = false;
			} else {
				JOptionPane.showMessageDialog(vista, "Define todas las horas", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(vista, "Define todos los días", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return vacio;
	}

	/**
	 * Método que recoge los datos que se hayan modificado del periodo y los actualiza sobre el periodo base
	 */
	private void modificarPeriodo() {
		Date diaIni;
		Date diaFin;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm");
		Time horaIni;
		Time horaFin;
		Time intervalo;
		int curso;
		boolean habilitado;

		if(jfad.tPeriodos.getSelectedRow()!=-1) {
			if (!comprobarCamposMod()) {
				diaIni = Date.valueOf(jfad.dPDiaIniMod.getDate());
				diaFin = Date.valueOf(jfad.dPDiaFinMod.getDate());
				horaIni = Time.valueOf(LocalTime.parse(jfad.tPHoraInicioMod.getTime().toString(), dtf));
				horaFin = Time.valueOf(LocalTime.parse(jfad.tPHoraFinMod.getTime().toString(), dtf));
				if ((int) jfad.spIntervaloMod.getValue() < 10) {
					intervalo = Time.valueOf(LocalTime.parse("0:0" + jfad.spIntervaloMod.getValue(), dtf));
				} else
					intervalo = Time.valueOf(LocalTime.parse("0:" + jfad.spIntervaloMod.getValue(), dtf));
				habilitado = jfad.checkHabilitado.isSelected();
				curso = Integer.parseInt(jfad.cBCursosMod.getSelectedItem().toString());
				System.out.println(diaIni.toString() + diaFin.toString() + horaIni.toString() + horaFin.toString()
						+ intervalo.toString() + curso);
				if (modelo.modPeriodo(per.getIdPeriodo(), diaIni, diaFin, horaIni, horaFin, intervalo, habilitado, curso)) {
					JOptionPane.showMessageDialog(vista, "Periodo creado con exito", "Info",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} else {
			JOptionPane.showMessageDialog(vista, "Selecciona un periodo antes de modificarlo", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Crea un frame con las reservas que hayan en un periodo seleccionado
	 */
	private void mostrarReservas() {
		JFReservasAdmin jfres =new JFReservasAdmin();
		jfres.setVisible(true);
		
		Map<Integer,Reserva> reservas = modelo.obtenerReservasPeriodo(per.getIdPeriodo());
		DefaultTableModel dtm = new DefaultTableModel(new Object[][] {},
				new String[] { "Dia", "Hora", "Email" });

		for (Integer key : reservas.keySet()) {

			dtm.addRow(new Object[] { reservas.get(key).getReserva_dia(), reservas.get(key).getReserva_hora(),reservas.get(key).getEmail() });
		}
		jfres.tReservas.setModel(dtm);
	}

	// ***********************************************************************************************************************************
	// ***************************************************MODIFICAR CORREO ***************************************************************
	// ***********************************************************************************************************************************
	
	/**
	 * Actualiza el mensaje que se envía a los alumnos al realizar las reservas
	 */
	private void actualizarMensajeEnvio() {
		String mensaje = jfad.tPBody.getText();
		if(modelo.updateMensaje(mensaje)) {
			JOptionPane.showMessageDialog(vista, "El mensaje se ha actualizado correctamente","Info",JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(vista, "No se ha podido actualizar","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

		if (jfad.tPeriodos.getSelectedRow() != -1) {
			rellenarCampos();
		} else {
			JOptionPane.showMessageDialog(vista, "Escoge un periodo para ver las reservas", "Info", JOptionPane.INFORMATION_MESSAGE);
		}
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
