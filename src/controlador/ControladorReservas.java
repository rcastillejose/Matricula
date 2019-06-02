package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;
import baseDeDatos.Modelo;
import modelo.Alumno;
import modelo.Periodo;
import modelo.Reserva;
import vista.JFLogin;
import vista.JFReservar;

public class ControladorReservas implements ActionListener, MouseListener, WindowListener {
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
		iniciarCalendario();
		
		// Añadir las acciones a los botones del formulario padre
				jfre.btnReservar.setActionCommand("reservar");
				jfre.cBCurso.setActionCommand("actualizarCursos");
				
				
				
				// Ponemos a escuchar las acciones del usuario
				jfre.btnReservar.addActionListener(this);
				jfre.cBCurso.addActionListener(this);
				
				jfre.cPDiaReserva.addMouseListener(this);
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
	
	private void iniciarCalendario() {
//		final LocalDate today = LocalDate.now();
		jfre.cPDiaReserva.setBorder(new TitledBorder(null, "Dia de Reserva", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		DatePickerSettings dateSettings = new DatePickerSettings();
//		dateSettings.setDateRangeLimits(today.minusDays(20), today.plusDays(20));
		
//	    dateSettings.setVetoPolicy(new SampleDateVetoPolicy());
	    
	    Map<Integer, Periodo> resultado = modelo.obtenerPeriodosReservas(String.valueOf(jfre.cBCurso.getSelectedItem()));

		for (Integer key : resultado.keySet()) {
			// dcbm.addElement(nodoEjemplar.getInfo().getId());
		
			dateSettings.setDateRangeLimits(resultado.get(key).getDia_inicio(), resultado.get(key).getDia_fin());

		}
		jfre.cPDiaReserva.setSettings(dateSettings);
	    
	}
	
	private void cargarReservas() {
		
		Map<Integer,Reserva> resultado = modelo.obtenerReservas(jfre.cPDiaReserva.getSelectedDate());
			DefaultTableModel dtm = new DefaultTableModel(new Object[][] {},
					new String[] { "Dia de la reserva", "Hora de Inicio", "Periodo"});

			for (Integer key : resultado.keySet()) {

				dtm.addRow(new Object[] { key, resultado.get(key).getReserva_dia(), resultado.get(key).getReserva_hora(),
						resultado.get(key).getIdPeriodo() });
			}
			jfre.tReservas.setModel(dtm);
		
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
	private static class SampleDateVetoPolicy implements DateVetoPolicy {

        /**
         * isDateAllowed, Return true if a date should be allowed, or false if a date should be
         * vetoed.
         */
        @Override
        public boolean isDateAllowed(LocalDate date) {
        	LocalDate today = LocalDate.now();
        	LocalDate maxDate = today.plusMonths(5);
        	

            // Disallow odd numbered saturdays.
            if ((date.getDayOfWeek() == DayOfWeek.SATURDAY) || (date.getDayOfWeek() == DayOfWeek.SUNDAY)) 
                return false;
            
            if (date.isAfter(maxDate) || date.isBefore(today))
            	return false;
            
            
            // Allow all other days.
            return true;
        }
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		cargarReservas();
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
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
