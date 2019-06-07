package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JPasswordField;

public class JFLogin extends JFrame {

	public JPanel contentPane;
	public JTextField tfUsuario;
	public JButton btnAcceder;
	public JButton btnCancelar;
	public JButton btnRegistro;
	public JPasswordField pFpasswd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFLogin frame = new JFLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JFLogin() {
		setResizable(false);
	
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 501, 392);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panelSuperior = new JPanel();
		
		JPanel panelLog = new JPanel();
		
		JPanel panel = new JPanel();
		
		btnCancelar = new JButton("Salir");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panelSuperior, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
						.addComponent(panelLog, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 466, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 466, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCancelar, Alignment.TRAILING))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panelSuperior, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelLog, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnCancelar)
					.addGap(18))
		);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnAcceder = new JButton("Acceder");
		panel.add(btnAcceder);
		
		btnRegistro = new JButton("Quiero registrarme");
		panel.add(btnRegistro);
		
		JLabel lblUsuario = new JLabel("Usuario");
		
		JLabel lblContrasea = new JLabel("Contraseña");
		
		tfUsuario = new JTextField();
		tfUsuario.setColumns(10);
		
		pFpasswd = new JPasswordField();
		GroupLayout gl_panelLog = new GroupLayout(panelLog);
		gl_panelLog.setHorizontalGroup(
			gl_panelLog.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelLog.createSequentialGroup()
					.addGap(54)
					.addGroup(gl_panelLog.createParallelGroup(Alignment.LEADING)
						.addComponent(lblContrasea)
						.addComponent(lblUsuario))
					.addGap(20)
					.addGroup(gl_panelLog.createParallelGroup(Alignment.TRAILING)
						.addComponent(tfUsuario, 296, 296, 296)
						.addComponent(pFpasswd, GroupLayout.PREFERRED_SIZE, 297, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panelLog.setVerticalGroup(
			gl_panelLog.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelLog.createSequentialGroup()
					.addGap(34)
					.addGroup(gl_panelLog.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsuario)
						.addComponent(tfUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panelLog.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblContrasea)
						.addComponent(pFpasswd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(21, Short.MAX_VALUE))
		);
		panelLog.setLayout(gl_panelLog);
		panelSuperior.setLayout(new BorderLayout(0, 0));
		
		JLabel lblHola = new JLabel("");
		lblHola.setIcon(new ImageIcon(JFLogin.class.getResource("/img/icolog.png")));
		lblHola.setHorizontalAlignment(SwingConstants.CENTER);
		panelSuperior.add(lblHola, BorderLayout.CENTER);
		
		JLabel lblPideCitaIes = new JLabel("Pide cita en IES La Vereda");
		lblPideCitaIes.setFont(new Font("Dialog", Font.BOLD, 16));
		lblPideCitaIes.setHorizontalAlignment(SwingConstants.CENTER);
		panelSuperior.add(lblPideCitaIes, BorderLayout.SOUTH);
		contentPane.setLayout(gl_contentPane);
	}
}
