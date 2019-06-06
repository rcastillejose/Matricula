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
import java.awt.FlowLayout;

public class JFReservar extends JFrame {

	public JPanel contentPane;
	public JTable tReservas;
	public CalendarPanel cPDiaReserva;
	public JButton btnReservar;
	private JLabel lblCurso;
	public JComboBox cBCurso;
	public Alumno a;
	public JButton btnEliminarReserva;
	public JLabel lblBienvenido;


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
		
		JPanel panel_1 = new JPanel();
		
		JPanel panel_2 = new JPanel();
		
		lblBienvenido = new JLabel("Bienvenido");
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(808, Short.MAX_VALUE)
					.addComponent(btnReservar)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblBienvenido)
					.addPreferredGap(ComponentPlacement.RELATED, 278, Short.MAX_VALUE)
					.addComponent(lblHazTuReserva)
					.addGap(352))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGap(64)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(cPDiaReserva, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
					.addGap(91))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblHazTuReserva))
						.addComponent(lblBienvenido))
					.addGap(39)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(cPDiaReserva, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 343, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
					.addComponent(btnReservar)
					.addContainerGap())
		);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblCurso = new JLabel("Curso");
		panel_2.add(lblCurso);
		
		cBCurso = new JComboBox();
		panel_2.add(cBCurso);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnEliminarReserva = new JButton("Eliminar reserva");
		panel_1.add(btnEliminarReserva);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		tReservas = new JTable();
		scrollPane.setViewportView(tReservas);
		contentPane.setLayout(gl_contentPane);
		
	}
}
