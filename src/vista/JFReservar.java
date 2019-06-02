package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import modelo.Alumno;


import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;

import javax.swing.JLabel;
import java.awt.Font;
import java.time.DayOfWeek;
import java.time.LocalDate;

import javax.swing.border.TitledBorder;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class JFReservar extends JFrame {

	public JPanel contentPane;
	public JTable tReservas;
	public CalendarPanel cPDiaReserva;
	public JButton btnReservar;
	private JLabel lblCurso;
	public JComboBox cBCurso;
	public Alumno a;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					JFReservar frame = new JFReservar(a);
//					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JFReservar() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 927, 672);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		cPDiaReserva = new CalendarPanel();
		
		JLabel lblHazTuReserva = new JLabel("HAZ TU RESERVA");
		lblHazTuReserva.setFont(new Font("Dialog", Font.BOLD, 21));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Reservas disponibles", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		btnReservar = new JButton("Reservar");
		
		lblCurso = new JLabel("Curso");
		
		cBCurso = new JComboBox();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(369, Short.MAX_VALUE)
					.addComponent(lblHazTuReserva)
					.addGap(352))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(814, Short.MAX_VALUE)
					.addComponent(btnReservar)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(64)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblCurso)
							.addGap(18)
							.addComponent(cBCurso, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(cPDiaReserva, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
							.addGap(91))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblHazTuReserva)
					.addGap(58)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCurso)
						.addComponent(cBCurso, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(cPDiaReserva, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 343, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
					.addComponent(btnReservar)
					.addContainerGap())
		);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		tReservas = new JTable();
		scrollPane.setViewportView(tReservas);
		contentPane.setLayout(gl_contentPane);
	}
//	private static class SampleDateVetoPolicy implements DateVetoPolicy {
//
//        /**
//         * isDateAllowed, Return true if a date should be allowed, or false if a date should be
//         * vetoed.
//         */
//        @Override
//        public boolean isDateAllowed(LocalDate date) {
//        	LocalDate today = LocalDate.now();
//        	LocalDate maxDate = today.plusMonths(5);
//        	
//
//            // Disallow odd numbered saturdays.
//            if ((date.getDayOfWeek() == DayOfWeek.SATURDAY) || (date.getDayOfWeek() == DayOfWeek.SUNDAY)) 
//                return false;
//            
//            if (date.isAfter(maxDate) || date.isBefore(today))
//            	return false;
//            
//            // Allow all other days.
//            return true;
//        }
//	}
}
