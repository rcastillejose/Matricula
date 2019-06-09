package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Toolkit;

public class JFRegistro extends JFrame {

	public JPanel contentPane;
	public JTextField tFNombre;
	public JTextField tFApellido1;
	public JTextField tFApellido2;
	public JTextField tFEmail;
	public JButton btnRegistro;



	/**
	 * Create the frame.
	 */
	public JFRegistro() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(JFRegistro.class.getResource("/img/icolog.png")));
		setTitle("Panel de registro");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 706, 412);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblRegistrate = new JLabel("REGISTRATE");
		lblRegistrate.setFont(new Font("Dialog", Font.BOLD, 21));
		
		JLabel lblIntroduceTusDatos = new JLabel("Introduce tus datos:");
		
		JLabel lblNombre = new JLabel("Nombre*");
		
		JLabel lblerApellido = new JLabel("1er Apellido*");
		
		JLabel lbloApellido = new JLabel("2o Apellido");
		
		JLabel lblEmail = new JLabel("Email*");
		
		tFNombre = new JTextField();
		tFNombre.setColumns(10);
		
		tFApellido1 = new JTextField();
		tFApellido1.setColumns(10);
		
		tFApellido2 = new JTextField();
		tFApellido2.setColumns(10);
		
		tFEmail = new JTextField();
		tFEmail.setColumns(10);
		
		btnRegistro = new JButton("Registro");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(59)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblNombre)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(tFNombre, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblEmail)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(tFEmail, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblIntroduceTusDatos)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblRegistrate, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblerApellido)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tFApellido1, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
									.addGap(40)
									.addComponent(lbloApellido)))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(tFApellido2, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(75, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(590, Short.MAX_VALUE)
					.addComponent(btnRegistro)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblRegistrate)
					.addGap(18)
					.addComponent(lblIntroduceTusDatos)
					.addGap(76)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNombre)
						.addComponent(tFNombre, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblerApellido)
						.addComponent(tFApellido1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lbloApellido)
						.addComponent(tFApellido2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(54)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEmail)
						.addComponent(tFEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
					.addComponent(btnRegistro)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
}
