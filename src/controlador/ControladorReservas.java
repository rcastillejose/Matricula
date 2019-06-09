package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;

import baseDeDatos.Modelo;
import configuracion.ConfiguracionSegura;
import gmail.GmailTool;
import modelo.Alumno;
import modelo.Periodo;
import modelo.Reserva;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import vista.JFLogin;
import vista.JFReservar;
/**
 * Clase que se encargará de controlar la inserción de reservas de los usuarios 
 * creado el 9 jun. 2019
 * @author raul
 *
 */
public class ControladorReservas implements CalendarListener, ActionListener, MouseListener, WindowListener {
	private JFReservar jfre ;
	private Modelo modelo;
	private JFLogin vista;
	private Alumno a;
	public static HashSet<LocalDate> lista=null;
	
	
	boolean reserva;
	
	/**
	 * Constructor de la clase
	 * @param vista Frame del login
	 * @param jfre Frame sobre el que se realizan las reservas
	 * @param modelo Modelo que realizara las acciones sobre la base de datos
	 * @param a alumno que realizará las reservas
	 */
	public ControladorReservas(JFLogin vista, JFReservar jfre, Modelo modelo, Alumno a) {
		this.jfre = jfre;
		this.modelo = modelo;
		this.vista = vista;
		this.a=a;
		inicializar();
	}
	
	/**
	 * Hace el frame visible
	 */
	public void go() {
		jfre.setVisible(true);
	}
	/**
	 * Pone a escuchar las acciones sobre el frame de registroy otras acciones en base al inicio del frame
	 */
	private void inicializar() {

		jfre.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		iniciarEtiqueta();
		actualizarComboboxCursos();
		iniciarCalendario();
		
		jfre.btnReservar.setVisible(false);
		jfre.lblBienvenido.setText("Bienvenido "+a.getNombre());
		
		
		// Añadir las acciones a los botones del formulario padre
		jfre.btnReservar.setActionCommand("reservar");
		jfre.btnEliminarReserva.setActionCommand("eliminarReserva");
		jfre.cBCurso.setActionCommand("actualizarCursos");
		
		
		// Ponemos a escuchar las acciones del usuario
		jfre.btnReservar.addActionListener(this);
		jfre.cBCurso.addActionListener(this);
		jfre.btnEliminarReserva.addActionListener(this);
		
		jfre.tReservas.addMouseListener(this);
		jfre.cPDiaReserva.addCalendarListener(this);
		
		jfre.addWindowListener(this);
	}
	/**
	 * Indica las consecuencias de las acciones que se realizan sobre el frame 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		
		if(command.equals("actualizarCursos")) {
			iniciarCalendario();
		} else if(command.equals("reservar")) {
			crearReserva();
		} else if(command.equals("eliminarReserva")) {
			eliminarReserva();
		}
	}
	
	/**
	 * Poner visible la etiqueta de las etiquetas
	 */
	private void iniciarEtiqueta() {
		int tieneReserva =modelo.comprobarAlumno(a.getEmail());
		if(tieneReserva>0) {
			jfre.btnEliminarReserva.setVisible(true);
			reserva=true;
			jfre.lbTieneReserva.setText(modelo.obtenerReserva(a.getEmail()));
		} else {
			jfre.btnEliminarReserva.setVisible(false);
			reserva=false;
			jfre.lbTieneReserva.setVisible(false);
		}
	}
	
	/**
	 * Actualiza el combobox del frame para que aparezcan en el todos los cursos
	 */
	private void actualizarComboboxCursos() {
		String curso;
		String aux;
		DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
		dcbm.removeAllElements();

		LinkedHashSet<Integer> resultado = modelo.obtenerCursos();
		Iterator it = resultado.iterator();
		while(it.hasNext()) {
			curso =it.next().toString();
			
			dcbm.addElement(curso);
		}
		jfre.cBCurso.setModel(dcbm);
		System.out.println(dcbm.getSelectedItem());
	}
	
	/**
	 * Inicia el calendario del frame utilizando las propiedades asignadas
	 */
	private void iniciarCalendario() {

		lista = modelo.obtenerDias(Integer.parseInt(jfre.cBCurso.getSelectedItem().toString()));
		jfre.cPDiaReserva.setBorder(new TitledBorder(null, "Dia de Reserva", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		DatePickerSettings dateSettings = new DatePickerSettings();    
		jfre.cPDiaReserva.setSettings(dateSettings);
	    dateSettings.setVetoPolicy(new SampleDateVetoPolicy());
	    
	}
	
	/**
	 * Carga las reservas del día que haya sido seleccionado en la tabla
	 */
	private void cargarReservas() {
		Date dia = Date.valueOf(jfre.cPDiaReserva.getSelectedDate());
		Map<Integer,Reserva> resultado = modelo.obtenerReservas(dia);
			DefaultTableModel dtm = new DefaultTableModel(new Object[][] {},
					new String[] { "Día de la reserva", "Hora de Inicio"});

			for (Integer key : resultado.keySet()) {

				dtm.addRow(new Object[] {resultado.get(key).getReserva_dia(), resultado.get(key).getReserva_hora()});
			}
			jfre.tReservas.setModel(dtm);
		
	}
	
	/**
	 * Asigna la reserva seleccionada al alumno que ha accedido a la aplicacion, o modifica la reserva si este ya tenia una
	 */
	private void crearReserva() {
		boolean cambio=false;
		boolean crear=false;
		System.out.println(jfre.tReservas.getValueAt(jfre.tReservas.getSelectedRow(), 1).toString());
		System.out.println(jfre.tReservas.getValueAt(jfre.tReservas.getSelectedRow(), 2).toString());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm");
		LocalTime hora = LocalTime.parse(jfre.tReservas.getValueAt(jfre.tReservas.getSelectedRow(), 2).toString(),dtf);
		Date dia=Date.valueOf(jfre.tReservas.getValueAt(jfre.tReservas.getSelectedRow(), 1).toString());
		//Time hora = Time.valueOf(jfre.tReservas.getValueAt(jfre.tReservas.getSelectedRow(), 2).toString()+":00");
		
		if(reserva) {
			int opcion = JOptionPane.showConfirmDialog(vista, "¿De verdad desea modificar su reserva?",
					"Confirmacion", JOptionPane.YES_NO_OPTION);
			if (opcion == JOptionPane.YES_OPTION) {
				cambio=modelo.modificarReserva(a.getEmail(), dia.toLocalDate(), hora);
				
			}
			
			if(cambio) {
				
				JOptionPane.showMessageDialog(vista, "Se ha modificado su reserva, revisa tu correo para mas información","Info",JOptionPane.INFORMATION_MESSAGE);
				mandarCorreo();
				iniciarEtiqueta();
			} else {
				JOptionPane.showMessageDialog(vista, "No se ha podido modificar su reserva","Error",JOptionPane.ERROR_MESSAGE);
			}
		} else {
			crear=modelo.reservar(a.getEmail(), dia.toLocalDate(), hora);
			if(crear) {
				
				JOptionPane.showMessageDialog(vista, "Se ha creado su reserva, revisa tu correo para mas información","Info",JOptionPane.INFORMATION_MESSAGE);
				mandarCorreo();
				iniciarEtiqueta();
			} else {
				JOptionPane.showMessageDialog(vista, "No se ha podido crear su reserva","Error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Elimina la reserva que tenga el usuario
	 */
	private void eliminarReserva() {
		int opcion = JOptionPane.showConfirmDialog(vista, "Desea eliminar su reserva","Info",JOptionPane.INFORMATION_MESSAGE);
		if(opcion==JOptionPane.YES_OPTION) {
			modelo.eliminarReserva(a.getEmail());
			inicializar();
		} 
	}
	
	/**
	 * envia un correo al alumno cuando se realice la reserva
	 */
	public void mandarCorreo() {
		String cuerpo = modelo.obtenerMensajeCorreo();
		String to = a.getEmail();
		
		String from = (new ConfiguracionSegura()).getMailFrom();
		String subject = "Confirmación de reserva";
		
		String reportUrl = "/reports/correo.jasper";
		
		InputStream reportFile = null;
		
		reportFile = getClass().getResourceAsStream(reportUrl);
		
		Map<String, Object> parametros = new HashMap<String,Object>();
			parametros.put("email", a.getEmail());
		
		
		try {
			JasperPrint print = JasperFillManager.fillReport(reportFile,  parametros, modelo.conectar());
			JasperExportManager.exportReportToPdfFile(print,"reserva.pdf");
	
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		ArrayList<File> files = new ArrayList<File>();
		File envio = new File("reserva.pdf");
		files.add(envio);
		
		GmailTool.sendHtmlWithAttachment(to, from, subject, cuerpo, files);
		envio.delete();
	
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		if (jfre.tReservas.getSelectedRow()!=-1) {
			jfre.btnReservar.setVisible(true);
		}
	}
	
	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Cerrando");
		vista.setVisible(true);
		jfre.dispose();
	}
	
	private static class SampleDateVetoPolicy implements DateVetoPolicy {

        /**
         * isDateAllowed, Return true if a date should be allowed, or false if a date should be
         * vetoed.
         */
        @Override
        public boolean isDateAllowed(LocalDate date) {
             	
            return lista.contains(date);
        }
	}
	
	@Override
	public void selectedDateChanged(CalendarSelectionEvent arg0) {
		// TODO Auto-generated method stub
		cargarReservas();
		
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

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void yearMonthChanged(YearMonthChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
