package main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import baseDeDatos.Modelo;
import controlador.Controlador;
import vista.JFLogin;

/**
 * 
 * creado el 28 may. 2019
 * @author raul
 *
 */
public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				//Creamos los objetos modelo y vista
				Modelo modelo = new Modelo();
				JFLogin vista = new JFLogin();
				new Controlador(vista,modelo).start();
				Dimension deskSize = Toolkit.getDefaultToolkit().getScreenSize() ;
				Dimension ifSize = vista.getSize();
				vista.setLocation((deskSize.width - ifSize.width) / 2, (deskSize.height - ifSize.height) /2);
				vista.setLocationRelativeTo(null);	
				vista.setVisible(true);	
				
			}
		});
	}

}
