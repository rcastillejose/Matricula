package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;

public class JFAbout extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public JFAbout() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 452, 349);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblBienvenidoAEsta = new JLabel("Bienvenido a esta aplicación para pedir cita previa");
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(JFAbout.class.getResource("/img/spmb.png")));
		
		JLabel lblAutorRalCastillejos = new JLabel("Autor: Raúl Castillejos Escudero 1ºDAW");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(69)
							.addComponent(lblAutorRalCastillejos))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(91)
							.addComponent(label))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(34)
							.addComponent(lblBienvenidoAEsta)))
					.addContainerGap(209, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblBienvenidoAEsta)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label)
					.addGap(18)
					.addComponent(lblAutorRalCastillejos)
					.addContainerGap(71, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}

}
