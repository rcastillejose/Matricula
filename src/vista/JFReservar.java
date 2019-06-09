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
import java.awt.Toolkit;

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
	public JLabel lbTieneReserva;


	/**
	 * Create the frame.
	 */
	public JFReservar() {
		setTitle("Realiza y gestiona tu reserva");
		setIconImage(Toolkit.getDefaultToolkit().getImage(JFReservar.class.getResource("/img/icolog.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 925, 599);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		cPDiaReserva = new CalendarPanel();
		
		JLabel lblHazTuReserva = new JLabel("HAZ TU RESERVA");
		lblHazTuReserva.setFont(new Font("Dialog", Font.BOLD, 21));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Reservas disponibles", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel panel_1 = new JPanel();
		
		JPanel panel_2 = new JPanel();
		
		lblBienvenido = new JLabel("Bienvenido");
		
		lbTieneReserva = new JLabel("New label");
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblBienvenido)
						.addComponent(lbTieneReserva))
					.addPreferredGap(ComponentPlacement.RELATED, 276, Short.MAX_VALUE)
					.addComponent(lblHazTuReserva)
					.addGap(352))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(64)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(cPDiaReserva, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(70, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(511)
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblHazTuReserva))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblBienvenido)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lbTieneReserva)))
					.addGap(34)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cPDiaReserva, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 343, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblCurso = new JLabel("Curso");
		panel_2.add(lblCurso);
		
		cBCurso = new JComboBox();
		panel_2.add(cBCurso);
		panel_1.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		btnEliminarReserva = new JButton("Eliminar reserva");
		panel_1.add(btnEliminarReserva);
		
		btnReservar = new JButton("Reservar");
		panel_1.add(btnReservar);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		tReservas = new JTable();
		scrollPane.setViewportView(tReservas);
		contentPane.setLayout(gl_contentPane);
		
	}
}
