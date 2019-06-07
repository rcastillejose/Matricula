package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTabbedPane;
import java.awt.Color;
import java.awt.FlowLayout;
import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;

import java.awt.Dimension;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

import javax.swing.JSpinner;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JCheckBox;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.border.LineBorder;
import javax.swing.JComboBox;

public class JFAdministrador extends JFrame {

	public JPanel contentPane;
	public JButton btnCrear;
	public JSpinner spReservasNumero;
	public JTable tPeriodos;
	public TimePicker tPHoraIni;
	public JSpinner spReservasMinutos;
	public JButton btnModificar;
	public JButton btnEliminar;
	public JTextPane tPBody;
	public JCheckBox chAdjunto;
	public JButton btnRealizarCambios;
	public DatePicker dPDiaInicio;
	public DatePicker dPDiaFin;
	public JButton btnAadirCurso;
	public JComboBox cBCursos;

	/**
	 * Create the frame.
	 */
	public JFAdministrador() {
		
		setTitle("Panel para el administrador");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 924, 672);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Crear Periodos", null, panel, null);
		
		JLabel lblCrearPeriodo = new JLabel("CREAR PERIODO");
		lblCrearPeriodo.setFont(new Font("Dialog", Font.BOLD, 21));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Reservas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel panel_4 = new JPanel();
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Periodo de Desarrollo", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		
		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new TitledBorder(null, "Cursos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(350)
							.addComponent(lblCrearPeriodo))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(53)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 369, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
									.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
										.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 290, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 368, GroupLayout.PREFERRED_SIZE))
									.addComponent(panel_5, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 801, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap(55, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(21)
					.addComponent(lblCrearPeriodo)
					.addGap(37)
					.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(69)
							.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(13, Short.MAX_VALUE))
		);
		
		cBCursos = new JComboBox();
		
		btnAadirCurso = new JButton("Añadir Curso");
		GroupLayout gl_panel_9 = new GroupLayout(panel_9);
		gl_panel_9.setHorizontalGroup(
			gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
					.addGap(34)
					.addComponent(cBCursos, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(163, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_panel_9.createSequentialGroup()
					.addContainerGap(152, Short.MAX_VALUE)
					.addComponent(btnAadirCurso)
					.addContainerGap())
		);
		gl_panel_9.setVerticalGroup(
			gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
					.addGap(27)
					.addComponent(cBCursos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
					.addComponent(btnAadirCurso)
					.addContainerGap())
		);
		panel_9.setLayout(gl_panel_9);
		
		JLabel label = new JLabel("Día de inicio");
		
		JLabel label_1 = new JLabel("Día de fin");
		
		TimePicker tPHoraFin = new TimePicker();
		tPHoraFin.getComponentTimeTextField().setEditable(false);
		
		JLabel label_2 = new JLabel("Hora de inicio");
		
		JLabel label_3 = new JLabel("Hora de fin");
		
		tPHoraIni = new TimePicker();
		tPHoraIni.getComponentTimeTextField().setEditable(false);

        // Crear el calendario, aumentarle el tamaño y vetar los dias que no estén permitidos
        DatePickerSettings dateSettings = new DatePickerSettings();
        int newHeight = (int) (dateSettings.getSizeDatePanelMinimumHeight() * 1.6);
        int newWidth = (int) (dateSettings.getSizeDatePanelMinimumWidth() * 1.6);
        dateSettings.setSizeDatePanelMinimumHeight(newHeight);
        dateSettings.setSizeDatePanelMinimumWidth(newWidth);
        dPDiaInicio = new DatePicker(dateSettings);
        dateSettings.setVetoPolicy(new SampleDateVetoPolicy());
        
        // Crear el calendario, aumentarle el tamaño y vetar los dias que no estén permitidos
        DatePickerSettings dateSettings2 = new DatePickerSettings();
        int newHeight2 = (int) (dateSettings2.getSizeDatePanelMinimumHeight() * 1.6);
        int newWidth2 = (int) (dateSettings2.getSizeDatePanelMinimumWidth() * 1.6);
        dateSettings2.setSizeDatePanelMinimumHeight(newHeight2);
        dateSettings2.setSizeDatePanelMinimumWidth(newWidth2);
        dPDiaFin = new DatePicker(dateSettings2);
        dateSettings2.setVetoPolicy(new SampleDateVetoPolicy());
        
//		el setDateRangeLimits no es compatible con el setVetoPolicy 
//      dateSettings.setDateRangeLimits(LocalDate.now().minusDays(20),LocalDate.now().plusMonths(5));
        
		
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(
			gl_panel_5.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addGap(70)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_1))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
						.addComponent(dPDiaInicio, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(dPDiaFin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel_5.createSequentialGroup()
							.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(tPHoraIni, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_5.createSequentialGroup()
							.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(tPHoraFin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(109))
		);
		gl_panel_5.setVerticalGroup(
			gl_panel_5.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_5.createSequentialGroup()
					.addGap(43)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(label_2)
						.addComponent(tPHoraIni, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(dPDiaInicio, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(label_3)
						.addComponent(tPHoraFin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(dPDiaFin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(61))
		);
		panel_5.setLayout(gl_panel_5);
		FlowLayout fl_panel_4 = new FlowLayout(FlowLayout.RIGHT, 5, 5);
		fl_panel_4.setAlignOnBaseline(true);
		panel_4.setLayout(fl_panel_4);
		
		btnCrear = new JButton("Crear");
		panel_4.add(btnCrear);
		
		JLabel lblO = new JLabel("o");
		
		JLabel lblIndicaLasReservas = new JLabel("Indica las reservas por dia");
		
		spReservasNumero = new JSpinner();
		
		JLabel lblIntervaloEntreReservas = new JLabel("Intervalo entre reservas");
		
		spReservasMinutos = new JSpinner();
		
		JLabel lblMinutos = new JLabel("minutos");
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(lblIntervaloEntreReservas)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(spReservasMinutos, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblMinutos))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(183)
							.addComponent(lblO))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(lblIndicaLasReservas)
							.addGap(18)
							.addComponent(spReservasNumero, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(38, Short.MAX_VALUE))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addContainerGap(32, Short.MAX_VALUE)
							.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup()
									.addGap(2)
									.addComponent(lblIntervaloEntreReservas))
								.addGroup(gl_panel_3.createSequentialGroup()
									.addGap(2)
									.addComponent(lblMinutos)))
							.addGap(21)
							.addComponent(lblO)
							.addGap(13))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(32)
							.addComponent(spReservasMinutos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(5)
							.addComponent(lblIndicaLasReservas)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panel_3.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(spReservasNumero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
		);
		panel_3.setLayout(gl_panel_3);
		panel.setLayout(gl_panel);
		
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Modificar Periodos", null, panel_1, null);
		
		JLabel lblModififarPeriodo = new JLabel("MODIFICAR PERIODO");
		lblModififarPeriodo.setFont(new Font("Dialog", Font.BOLD, 21));
		
		JPanel panel_6 = new JPanel();
		
		JPanel panel_7 = new JPanel();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(327)
					.addComponent(lblModififarPeriodo)
					.addContainerGap(342, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(291)
					.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(335, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
					.addContainerGap(58, Short.MAX_VALUE)
					.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 795, GroupLayout.PREFERRED_SIZE)
					.addGap(56))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(21)
					.addComponent(lblModififarPeriodo)
					.addGap(40)
					.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 370, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(78, Short.MAX_VALUE))
		);
		
		btnModificar = new JButton("Modificar");
		panel_7.add(btnModificar);
		
		btnEliminar = new JButton("Eliminar");
		panel_7.add(btnEliminar);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_6.add(scrollPane, BorderLayout.CENTER);
		
		tPeriodos = new JTable();
		scrollPane.setViewportView(tPeriodos);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Modificar Correos", null, panel_2, null);
		
		JLabel lblModificarCorreos = new JLabel("MODIFICAR CORREOS");
		lblModificarCorreos.setFont(new Font("Dialog", Font.BOLD, 21));
		
		JLabel lblCuerpoDelMensaje = new JLabel("Cuerpo del mensaje");
		
		tPBody = new JTextPane();
		
		JLabel lbldeseaIncluirEl = new JLabel("¿Desea incluir el archivo adjunto?");
		
		chAdjunto = new JCheckBox("");
		
		JPanel panel_8 = new JPanel();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(324)
							.addComponent(lblModificarCorreos))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(87)
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(tPBody, GroupLayout.PREFERRED_SIZE, 756, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCuerpoDelMensaje)
								.addGroup(gl_panel_2.createSequentialGroup()
									.addComponent(lbldeseaIncluirEl)
									.addGap(18)
									.addComponent(chAdjunto)))))
					.addContainerGap(66, Short.MAX_VALUE))
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap(588, Short.MAX_VALUE)
					.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(26)
					.addComponent(lblModificarCorreos)
					.addGap(27)
					.addComponent(lblCuerpoDelMensaje)
					.addGap(18)
					.addComponent(tPBody, GroupLayout.PREFERRED_SIZE, 271, GroupLayout.PREFERRED_SIZE)
					.addGap(31)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
						.addComponent(lbldeseaIncluirEl)
						.addComponent(chAdjunto))
					.addPreferredGap(ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
					.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		panel_8.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnRealizarCambios = new JButton("Realizar Cambios");
		panel_8.add(btnRealizarCambios);
		panel_2.setLayout(gl_panel_2);
		
		
		
		// le añado un modelo para que el spinner tenga un valor minimo y uno maximo
		SpinnerNumberModel modeloSpinner = new SpinnerNumberModel();
        modeloSpinner.setMaximum(60);
        modeloSpinner.setMinimum(5);
        modeloSpinner.setValue(5);
        spReservasMinutos.setModel(modeloSpinner);
		
     // le añado un modelo para que el spinner tenga un valor minimo y uno maximo
        SpinnerNumberModel modeloSpinner2 = new SpinnerNumberModel();
        modeloSpinner2.setMaximum(72);
        modeloSpinner2.setMinimum(0);
        spReservasNumero.setModel(modeloSpinner2);
        
        
     
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
}
