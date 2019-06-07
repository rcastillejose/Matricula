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

public class ControladorReservas implements CalendarListener, ActionListener, MouseListener, WindowListener {
	private JFReservar jfre ;
	private Modelo modelo;
	private JFLogin vista;
	private Alumno a;
	public static HashSet<LocalDate> lista=null;
	
	
	boolean reserva;
	
	public ControladorReservas(JFLogin vista, JFReservar jfre, Modelo modelo, Alumno a) {
		this.jfre = jfre;
		this.modelo = modelo;
		this.vista = vista;
		this.a=a;
		inicializar();
	}
	
	public void go() {
		jfre.setVisible(true);
	}
	
	private void inicializar() {

		jfre.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		actualizarComboboxCursos();
		iniciarCalendario();
		int tieneReserva =modelo.comprobarAlumno(a.getEmail());
		if(tieneReserva>0) {
			jfre.btnEliminarReserva.setVisible(true);
			reserva=true;
		} else {
			jfre.btnEliminarReserva.setVisible(false);
			reserva=false;
		}
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
		jfre.cBCurso.setModel(dcbm);
		System.out.println(dcbm.getSelectedItem());
	}
	
	
	private void iniciarCalendario() {

		lista = modelo.obtenerDias(String.valueOf(jfre.cBCurso.getSelectedItem()));
		jfre.cPDiaReserva.setBorder(new TitledBorder(null, "Dia de Reserva", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		DatePickerSettings dateSettings = new DatePickerSettings();    
		jfre.cPDiaReserva.setSettings(dateSettings);
	    dateSettings.setVetoPolicy(new SampleDateVetoPolicy());
	    
	}
	
	private void cargarReservas() {
		Date dia = Date.valueOf(jfre.cPDiaReserva.getSelectedDate());
		Map<Integer,Reserva> resultado = modelo.obtenerReservas(dia);
			DefaultTableModel dtm = new DefaultTableModel(new Object[][] {},
					new String[] { "Reserva","Dia de la reserva", "Hora de Inicio", "Periodo"});

			for (Integer key : resultado.keySet()) {

				dtm.addRow(new Object[] { key, resultado.get(key).getReserva_dia(), resultado.get(key).getReserva_hora(),
						resultado.get(key).getIdPeriodo() });
			}
			jfre.tReservas.setModel(dtm);
		
	}
	
	private void crearReserva() {
		boolean cambio=false;
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
				inicializar();
				JOptionPane.showMessageDialog(vista, "Se ha modificado su reserva, revisa tu correo para mas información","Info",JOptionPane.INFORMATION_MESSAGE);
				mandarCorreo();
			} else {
				JOptionPane.showMessageDialog(vista, "No se ha podido modificar su reserva","Error",JOptionPane.ERROR_MESSAGE);
			}
		} else {
			cambio=modelo.reservar(a.getEmail(), dia.toLocalDate(), hora);
			if(cambio) {
				inicializar();
				JOptionPane.showMessageDialog(vista, "Se ha creado su reserva, revisa tu correo para mas información","Info",JOptionPane.INFORMATION_MESSAGE);
				
			} else {
				JOptionPane.showMessageDialog(vista, "No se ha podido crear su reserva","Error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	private void eliminarReserva() {
		int opcion = JOptionPane.showConfirmDialog(vista, "Desea eliminar su reserva","Info",JOptionPane.INFORMATION_MESSAGE);
		if(opcion==JOptionPane.YES_OPTION) {
			modelo.eliminarReserva(a.getEmail());
			inicializar();
		} 
	}
	
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
