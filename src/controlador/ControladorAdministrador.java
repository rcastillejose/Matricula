package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import baseDeDatos.Modelo;
import modelo.Periodo;
import modelo.Reserva;
import vista.JFAdministrador;
import vista.JFLogin;

public class ControladorAdministrador implements ActionListener, MouseListener, WindowListener {
	private JFAdministrador jfad;
	private Modelo modelo;
	private JFLogin vista;

	public ControladorAdministrador(JFLogin vista, JFAdministrador jfad, Modelo modelo) {
		this.jfad = jfad;
		this.modelo = modelo;
		this.vista = vista;
		inicializar();
	}

	private void inicializar() {
		actualizarComboboxCursos();
		actualizarTablaPeriodos();
		jfad.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jfad.addWindowListener(this);

		// Añadir las acciones a los botones del panel de crear periodos
		jfad.btnAadirCurso.setActionCommand("addCurso");
		jfad.btnCrear.setActionCommand("addPeriodo");
		jfad.cBCursosMod.setActionCommand("actualizarTabla");
		
		// Ponemos a escuchar las acciones del usuario del panel de crear periodos
		jfad.btnAadirCurso.addActionListener(this);
		jfad.btnCrear.addActionListener(this);
		jfad.cBCursosMod.addActionListener(this);

		jfad.tPeriodos.addMouseListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comando = e.getActionCommand();

		if (comando.equals("addCurso")) {
			insertarCurso();
		} else if (comando.equals("addPeriodo")) {
			crearPeriodo();
		} else if (comando.equals("actualizarTabla")) {
			actualizarTablaPeriodos();

		}
	}

	public void go() {
		jfad.setVisible(true);
	}
	
	// ****************************************CREAR PERIODO**********************************

	private void actualizarComboboxCursos() {
		int curso;
		
		DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
		dcbm.removeAllElements();

		LinkedHashSet<Integer> resultado = modelo.obtenerCursos();
		Iterator it = resultado.iterator();
		while(it.hasNext()) {
			curso =(int) it.next();
			
			dcbm.addElement(curso);
		}
		jfad.cBCursos.setModel(dcbm);
		jfad.cBCursosMod.setModel(dcbm);
		System.out.println(dcbm.getSelectedItem());
	}
	
	private void insertarCurso() {
		LinkedHashSet<Integer> resultado = modelo.obtenerCursos();
		Iterator it = resultado.iterator();
		int curso=(int) it.next();
		
		int cursoINT = curso+1;
		if(modelo.insertarCurso(cursoINT))
			actualizarComboboxCursos();
	}
	
	private boolean comprobarCampos() {
		boolean vacio = true;
		if (jfad.dPDiaInicio.getDate()!=null && jfad.dPDiaFin.getDate()!= null) {
			if(jfad.tPHoraIni.getTime()!=null && jfad.tPHoraFin.getTime()!=null) {
			vacio = false;
			}else {
				JOptionPane.showMessageDialog(vista, "Define todas las horas", "Error",JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(vista, "Define todos los días", "Error",JOptionPane.ERROR_MESSAGE);
		}
		return vacio;
	}
	
	private void crearPeriodo() {
		Date diaIni;
		Date diaFin;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm");
		Time horaIni;
		Time horaFin;
		Time intervalo;
		int curso;
	
		if (!comprobarCampos()) {
			diaIni = Date.valueOf(jfad.dPDiaInicio.getDate());
			diaFin = Date.valueOf(jfad.dPDiaFin.getDate());
			horaIni = Time.valueOf(LocalTime.parse(jfad.tPHoraIni.getTime().toString(),dtf));
			horaFin = Time.valueOf(LocalTime.parse(jfad.tPHoraFin.getTime().toString(),dtf));
			if((int) jfad.spReservasMinutos.getValue()<10) {
				intervalo = Time.valueOf(LocalTime.parse("0:0"+jfad.spReservasMinutos.getValue(), dtf));
			} else
				intervalo = Time.valueOf(LocalTime.parse("0:"+jfad.spReservasMinutos.getValue(), dtf));
			curso = Integer.parseInt(jfad.cBCursos.getSelectedItem().toString());
						System.out.println(diaIni.toString()+ diaFin.toString()+ horaIni.toString()+ horaFin.toString()+ intervalo.toString()+ curso);
			if(modelo.addPeriodo(diaIni,diaFin,horaIni,horaFin,intervalo,curso)) {
				JOptionPane.showMessageDialog(vista, "Periodo creado con exito", "Info",JOptionPane.INFORMATION_MESSAGE);
			}
		} 
	}
	//***************************************************MODIFICAR PERIODO
	
	
	private void actualizarTablaPeriodos() {
		Map<Integer,Periodo> periodos = modelo.obtenerPeriodos(Integer.parseInt(jfad.cBCursosMod.getSelectedItem().toString()));
		DefaultTableModel dtm = new DefaultTableModel(new Object[][] {},
				new String[] { "Periodo","Dia inicio", "Dia fin", "Hora inicio","Hora_fin","Habilitado"});

		for (Integer key : periodos.keySet()) {

			dtm.addRow(new Object[] { key, periodos.get(key).getDia_inicio(), periodos.get(key).getDia_fin(),
					periodos.get(key).getHora_inicio(),periodos.get(key).getHora_fin(),periodos.get(key).getHabilitado()});
		}
		jfad.tPeriodos.setModel(dtm);
	}

	private void rellenarCampos() {
		
		Periodo p = modelo.obtenerPeriodo(Integer.parseInt(jfad.tPeriodos.getValueAt(jfad.tPeriodos.getSelectedRow(), 0).toString()));
		jfad.dPDiaIniMod.setDate(p.getDia_inicio());
		jfad.dPDiaFinMod.setDate(p.getDia_fin());
		jfad.tPHoraInicioMod.setTime(p.getHora_inicio());
		jfad.tPHoraFinMod.setTime(p.getHora_fin());
		String intervalo = p.getIntervalo().toString();
		System.out.println(intervalo);
		int intervalo2=Integer.parseInt(intervalo.substring(3));
		jfad.spIntervaloMod.setValue(intervalo2);
		if(p.getHabilitado())
		jfad.checkHabilitado.setSelected(true);
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

		if (jfad.tPeriodos.getSelectedRow()!=-1) {
			rellenarCampos();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Cerrando");
		vista.setVisible(true);
		jfad.dispose();

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
